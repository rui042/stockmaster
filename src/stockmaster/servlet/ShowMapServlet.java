package stockmaster.servlet;1

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import stockmaster.bean.ShelfBean;
import stockmaster.bean.StockBean;
import stockmaster.dao.ShelfDao;
import stockmaster.dao.StockDao;

@WebServlet("/showMap")
public class ShowMapServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String storeIdStr = request.getParameter("storeId");
        int storeId = (storeIdStr != null && !storeIdStr.isEmpty()) ? Integer.parseInt(storeIdStr) : 0;

        String keyword = safe(request.getParameter("keyword"));
        String category = safe(request.getParameter("category"));
        String selectedShelfId = safe(request.getParameter("shelfId"));

        request.setAttribute("storeId", storeId);
        request.setAttribute("keyword", keyword != null ? keyword : "");
        request.setAttribute("category", category != null ? category : "");
        request.setAttribute("floorImage", request.getContextPath() + "/resources/floorplan.png");

        StockDao stockDao = new StockDao();
        ShelfDao shelfDao = new ShelfDao();

        boolean isInitial = (storeId == 0)
                || ((keyword == null || keyword.isEmpty())
                    && (category == null || category.isEmpty())
                    && (selectedShelfId == null || selectedShelfId.isEmpty()));

        List<StockBean> itemList = new ArrayList<>();
        List<ShelfBean> shelfList = new ArrayList<>();
        List<ShelfBean> hotspots = new ArrayList<>();
        ShelfBean selectedShelf = null;
        int resultCount = 0;

        if (!isInitial) {
            // 商品検索（キーワード優先）
            if (keyword != null && !keyword.isEmpty()) {
                itemList = stockDao.findByStoreAndKeyword(storeId, keyword);
            } else {
                itemList = stockDao.findByStore(storeId);
            }

            // 棚一覧取得
            shelfList = shelfDao.findByStore(storeId);
            request.setAttribute("shelfList", shelfList);

            Map<String, ShelfBean> shelfMap = shelfList.stream()
                    .filter(s -> s.getShelfId() != null)
                    .collect(Collectors.toMap(ShelfBean::getShelfId, s -> s));

            // カテゴリ指定がある場合は棚ジャンルでフィルタ
            if ((keyword == null || keyword.isEmpty()) && category != null && !category.isEmpty()) {
                itemList = itemList.stream()
                        .filter(item -> {
                            ShelfBean shelf = shelfMap.get(item.getShelfId());
                            return shelf != null && category.equals(shelf.getCategory());
                        })
                        .collect(Collectors.toList());
            }

            // 棚指定がある場合はその棚の商品だけに絞る
            if (selectedShelfId != null && !selectedShelfId.isEmpty()) {
                itemList = itemList.stream()
                        .filter(item -> selectedShelfId.equals(item.getShelfId()))
                        .collect(Collectors.toList());
                selectedShelf = shelfMap.get(selectedShelfId);
            }

            // ★ StockBean に棚ジャンルを埋め込む
            for (StockBean item : itemList) {
                ShelfBean shelf = shelfMap.get(item.getShelfId());
                if (shelf != null) {
                    item.setGenre(shelf.getCategory()); // ← 棚のカテゴリをセット
                }
            }

            resultCount = (itemList != null) ? itemList.size() : 0;
            request.setAttribute("itemList", itemList);
            request.setAttribute("resultCount", resultCount);

            // 検索結果の棚だけピン表示
            if (itemList != null && !itemList.isEmpty()) {
                hotspots = itemList.stream()
                        .map(item -> shelfMap.get(item.getShelfId()))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
            request.setAttribute("hotspots", hotspots);

            request.setAttribute("selectedShelf", selectedShelf);
        } else {
            request.setAttribute("itemList", null);
            request.setAttribute("resultCount", 0);
            request.setAttribute("shelfList", null);
            request.setAttribute("hotspots", null);
            request.setAttribute("selectedShelf", null);
        }

        RequestDispatcher rd = request.getRequestDispatcher("/views/showmap.jsp");
        rd.forward(request, response);
    }

    private String safe(String s) {
        return (s == null) ? null : s.trim();
    }
}