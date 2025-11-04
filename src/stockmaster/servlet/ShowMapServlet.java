package stockmaster.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import stockmaster.bean.ShowMapBean;

@WebServlet("/showMap")
public class ShowMapServlet extends HttpServlet {

    public static class Hotspot {
        public String id;
        public String title;
        public double xPct;
        public double yPct;
        public double wPct;
        public double hPct;
        public String desc;
        public Hotspot(String id, String title, double xPct, double yPct, double wPct, double hPct, String desc){
            this.id=id; this.title=title; this.xPct=xPct; this.yPct=yPct; this.wPct=wPct; this.hPct=hPct; this.desc=desc;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Hotspot> hotspots = new ArrayList<>();
        hotspots.add(new Hotspot("s1","倉庫A", 12.5, 18.0, 8.0, 6.0, "在庫:120"));
        hotspots.add(new Hotspot("s2","店舗B", 40.0, 20.0, 9.0, 7.0, "営業中"));
        hotspots.add(new Hotspot("s3","配送C", 70.0, 55.0, 10.0, 8.0, "入荷予定"));

        req.setAttribute("hotspots", hotspots);
        req.setAttribute("floorImage", req.getContextPath() + "/resources/floorplan.png");

     // storeId を取得（GETパラメータから）
        String storeIdParam = req.getParameter("storeId");
        int storeId = 0;
        if (storeIdParam != null && !storeIdParam.isEmpty()) {
            storeId = Integer.parseInt(storeIdParam);
        } else {
            // storeIdが指定されていない場合の処理（例：エラー表示やデフォルト店舗）
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "店舗IDが指定されていません");
            return;
        }


     // 検索キーワード取得
        String keyword = req.getParameter("keyword");
        String category = req.getParameter("category");
     // 棚の商品情報を取得
        List<ShowMapBean> itemList = new ArrayList<>();

        try {
            // JDBCドライバのロード（最初に一度だけ呼び出す）
            Class.forName("org.h2.Driver");

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:h2:tcp://localhost/~/stockmaster;MODE=MySQL", "sa", "")) {

                String sql;
                PreparedStatement stmt;

                if (keyword != null && !keyword.isEmpty()) {
                    // 商品名が指定されていれば、商品名で絞り込み（カテゴリは無視）
                    sql = "SELECT STORE_ID, SHELF_ID, CATEGORY, ITEM_NAME, PRICE, STOCK_NOW, STOCK_MIN " +
                          "FROM MAP_VIEW WHERE STORE_ID = ? AND ITEM_NAME LIKE ?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, storeId);
                    stmt.setString(2, "%" + keyword + "%");

                } else if (category != null && !category.isEmpty()) {
                    // 商品名が空でカテゴリが指定されていれば、カテゴリで絞り込み
                    sql = "SELECT STORE_ID, SHELF_ID, CATEGORY, ITEM_NAME, PRICE, STOCK_NOW, STOCK_MIN " +
                          "FROM MAP_VIEW WHERE STORE_ID = ? AND CATEGORY = ?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, storeId);
                    stmt.setString(2, category);

                } else {
                    // 両方空なら全件表示
                    sql = "SELECT STORE_ID, SHELF_ID, CATEGORY, ITEM_NAME, PRICE, STOCK_NOW, STOCK_MIN " +
                          "FROM MAP_VIEW WHERE STORE_ID = ?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, storeId);
                }


              ResultSet rs = stmt.executeQuery();
              while (rs.next()) {
                  itemList.add(new ShowMapBean(
                          rs.getString("SHELF_ID"),
                          rs.getString("CATEGORY"),
                          rs.getString("ITEM_NAME"),
                          rs.getInt("PRICE"),
                          rs.getInt("STOCK_NOW"),
                          rs.getInt("STOCK_MIN")
                  ));
              }
          }
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException(e);
        }

        // 検索条件と結果をJSPへ渡す
        req.setAttribute("itemList", itemList);
        req.setAttribute("resultCount", itemList.size());
        req.setAttribute("keyword", keyword);
        req.setAttribute("category", category);
        req.setAttribute("storeId", storeId);

        req.getRequestDispatcher("/views/showmap.jsp").forward(req, resp);
    }
}
