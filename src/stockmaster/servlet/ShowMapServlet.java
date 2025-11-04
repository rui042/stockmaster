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


     // 棚の商品情報を取得
        List<ShowMapBean> itemList = new ArrayList<>();

        try {
            // JDBCドライバのロード（最初に一度だけ呼び出す）
            Class.forName("org.h2.Driver");

            // 検索キーワード取得
            String keyword = req.getParameter("keyword");

            try (Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/stockmaster;MODE=MySQL", "sa", "");
                 PreparedStatement stmt = conn.prepareStatement(
                     "SELECT STORE_ID, SHELF_ID, CATEGORY, ITEM_NAME, PRICE, STOCK_NOW, STOCK_MIN FROM MAP_VIEW WHERE STORE_ID = ?"
                    		 +(keyword != null && !keyword.isEmpty() ? " AND ITEM_NAME LIKE ?" : "")
                		 )) {

                stmt.setInt(1, storeId);
                if (keyword != null && !keyword.isEmpty()) {
                    stmt.setString(2, "%" + keyword + "%");
                  }

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int sid = rs.getInt("STORE_ID");
                    String shelfId = rs.getString("SHELF_ID");
                    String category = rs.getString("CATEGORY");
                    String itemName = rs.getString("ITEM_NAME");
                    int price = rs.getInt("PRICE");
                    int stockNow = rs.getInt("STOCK_NOW");
                    int stockMin = rs.getInt("STOCK_MIN");


                    itemList.add(new ShowMapBean(shelfId, category, itemName, price, stockNow, stockMin));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException(e);
        }

        // JSPへデータを渡して画面遷移
        req.setAttribute("resultCount", itemList.size());	// 件数
        req.setAttribute("itemList", itemList);
        req.getRequestDispatcher("/views/showmap.jsp").forward(req, resp);
    }
}


