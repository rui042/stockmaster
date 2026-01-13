package stockmaster.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

        HttpSession session = request.getSession(false);
        Integer storeId = (session != null) ? (Integer) session.getAttribute("storeId") : null;

        if (storeId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String keyword = safe(request.getParameter("keyword"));
        String category = safe(request.getParameter("category"));
        String selectedShelfSeqStr = safe(request.getParameter("shelfSeq"));
        int selectedShelfSeq = (selectedShelfSeqStr != null && !selectedShelfSeqStr.isEmpty())
                ? Integer.parseInt(selectedShelfSeqStr) : -1;

        // クリック座標
        String xStr = safe(request.getParameter("xPct"));
        String yStr = safe(request.getParameter("yPct"));
        Double clickX = (xStr != null && !xStr.isEmpty()) ? Double.parseDouble(xStr) : null;
        Double clickY = (yStr != null && !yStr.isEmpty()) ? Double.parseDouble(yStr) : null;

        request.setAttribute("keyword", keyword != null ? keyword : "");
        request.setAttribute("category", category != null ? category : "");
        request.setAttribute("floorImage",
                request.getContextPath() + "/resources/floorplan_" + storeId + ".png");

        StockDao stockDao = new StockDao();
        ShelfDao shelfDao = new ShelfDao();

        boolean isInitial = ((keyword == null || keyword.isEmpty())
                && (category == null || category.isEmpty())
                && selectedShelfSeq == -1
                && clickX == null && clickY == null);

        List<StockBean> itemList = new ArrayList<>();
        List<ShelfBean> shelfList = new ArrayList<>();
        List<ShelfBean> hotspots = new ArrayList<>();
        ShelfBean selectedShelf = null;

        if (!isInitial) {

            // 商品検索
            if (keyword != null && !keyword.isEmpty()) {
                itemList = stockDao.findByStoreAndKeyword(storeId, keyword);
            } else {
                itemList = stockDao.findByStore(storeId);
            }

            // 棚一覧（店舗限定）
            shelfList = shelfDao.findByStore(storeId).stream()
                    .sorted((a, b) -> Integer.compare(a.getShelfSeq(), b.getShelfSeq()))
                    .collect(Collectors.toList());
            request.setAttribute("shelfList", shelfList);

            // shelfSeq → ShelfBean のマップ
            Map<Integer, ShelfBean> shelfMap = shelfList.stream()
                    .collect(Collectors.toMap(ShelfBean::getShelfSeq, s -> s));

            // カテゴリフィルタ
            if ((keyword == null || keyword.isEmpty()) && category != null && !category.isEmpty()) {
                itemList = itemList.stream()
                        .filter(item -> {
                            ShelfBean shelf = shelfMap.get(item.getShelfSeq());
                            return shelf != null && category.equals(shelf.getCategory());
                        })
                        .collect(Collectors.toList());
            }

            // 棚SEQ指定
            if (selectedShelfSeq != -1) {
                selectedShelf = shelfMap.get(selectedShelfSeq);
                itemList = itemList.stream()
                        .filter(item -> item.getShelfSeq() == selectedShelfSeq)
                        .collect(Collectors.toList());
            }

            // クリック座標 → 最寄り棚
            if (selectedShelf == null && clickX != null && clickY != null) {
                selectedShelf = shelfDao.findNearestShelf(storeId, clickX, clickY);

                if (selectedShelf != null) {
                    int seq = selectedShelf.getShelfSeq();
                    itemList = itemList.stream()
                            .filter(item -> item.getShelfSeq() == seq)
                            .collect(Collectors.toList());
                }
            }

            // StockBean に棚情報を埋め込む
            for (StockBean item : itemList) {
                ShelfBean shelf = shelfMap.get(item.getShelfSeq());
                if (shelf != null) {
                    item.setGenre(shelf.getCategory());
                    item.setShelfId(shelf.getShelfId());
                }
            }

            request.setAttribute("itemList", itemList);
            request.setAttribute("resultCount", itemList.size());

            // ピン（検索結果の棚）
            hotspots = itemList.stream()
                    .map(item -> shelfMap.get(item.getShelfSeq()))
                    .filter(shelf -> shelf != null)
                    .collect(Collectors.toList());

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