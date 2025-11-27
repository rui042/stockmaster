package stockmaster.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import stockmaster.bean.StoreBean;

@WebServlet("/searchStore")
public class SearchStoreServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    	HttpSession session = req.getSession(true);

        // ゲストフラグを確認
        String guestFlag = req.getParameter("guest");
        String username = null;

        if ("true".equals(guestFlag)) {
            // ログイン情報をリセットしてゲスト扱いにする
            session.removeAttribute("loginUser");
            session.removeAttribute("username");
            session.removeAttribute("isStaff");
            session.removeAttribute("chatHistory");
            session.removeAttribute("currentStepKey");

            session.setAttribute("isGuest", true);
            username = "ゲスト";
            // System.out.println("ゲストユーザーとして店舗検索にアクセス");
        } else {
            // 通常ログインユーザーを確認
            username = (String) session.getAttribute("username");
            if (username != null) {
                System.out.println("ログイン中ユーザー: " + username);
            } else {
                System.out.println("未ログインユーザーが店舗検索にアクセス");
            }
        }

        /** // デバッグ表示（任意）
        if (username != null) {
            System.out.println("ログイン中ユーザー: " + username);
        } else {
            System.out.println("未ログインユーザーが店舗検索にアクセス");
        } */

        String areaIdStr = req.getParameter("areaId");

        List<StoreBean> storeList = new ArrayList<>();

        try {
            Class.forName("org.h2.Driver");

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:h2:tcp://localhost/~/stockmaster;MODE=MySQL", "sa", "")) {

                String sql;
                PreparedStatement stmt;

                if (areaIdStr != null && !areaIdStr.isEmpty()) {
                    // 地域IDが指定されていれば STORE_ID で絞り込み
                    sql = "SELECT STORE_ID, STORE_NAME, STORE_ADDRESS, STORE_PHONE, OPEN_TIME, CLOSE_TIME "
                        + "FROM STORES WHERE STORE_ID = ?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, Integer.parseInt(areaIdStr));
                } else {
                    // 地域IDが空なら全件表示
                    sql = "SELECT STORE_ID, STORE_NAME, STORE_ADDRESS, STORE_PHONE, OPEN_TIME, CLOSE_TIME FROM STORES";
                    stmt = conn.prepareStatement(sql);
                }

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Time open = rs.getTime("OPEN_TIME");
                    Time close = rs.getTime("CLOSE_TIME");

                    StoreBean store = new StoreBean(
                            rs.getInt("STORE_ID"),
                            rs.getString("STORE_NAME"),
                            rs.getString("STORE_ADDRESS"),
                            rs.getString("STORE_PHONE"),
                            open != null ? open.toLocalTime() : LocalTime.MIN,
                            close != null ? close.toLocalTime() : LocalTime.MAX
                    );
                    store.updateOpenNow();
                    storeList.add(store);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException(e);
        }

        req.setAttribute("storeList", storeList);
        req.setAttribute("searched", true);
        req.setAttribute("areaId", areaIdStr); // 選択状態保持用
        // ログインしていれば名前を渡してヘッダーに表示
        req.setAttribute("username", username);

        // JSPにフォワード
        req.getRequestDispatcher("/views/searchStore.jsp").forward(req, resp);
    }
}
