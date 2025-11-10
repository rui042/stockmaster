package stockmaster.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import stockmaster.bean.UserBean;

@WebServlet("/productRegister")
public class ProductRegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        // ▼ ログイン状態確認
        HttpSession session = request.getSession(false);
        UserBean loginUser = (session != null) ? (UserBean) session.getAttribute("loginUser") : null;

        if (loginUser == null) {
            // 未ログイン時はJSONでエラーを返す
            sendJson(response, "error", "ログインが必要です。");
            return;
        }

        // 一部後に数値変換
        String itemId = request.getParameter("itemId");
        String name = request.getParameter("name");
        String category = request.getParameter("category");
        String shelfId = request.getParameter("shelf");
        String priceStr = request.getParameter("price");
        String stockNowStr = request.getParameter("stockNow");
        String stockMinStr = request.getParameter("stockMin");
        String storeIdStr = request.getParameter("storeId");

        String message = "";
        String status = "error";

        // 数値変換チェック
        int price, stockNow, stockMin, storeId;
        try {
            price = Integer.parseInt(priceStr);
            stockNow = Integer.parseInt(stockNowStr);
            stockMin = Integer.parseInt(stockMinStr);
            storeId = Integer.parseInt(storeIdStr);
        } catch (NumberFormatException e) {
            message = "価格・在庫数・店舗IDは整数で入力してください。";
            sendJson(response, status, message);
            return;
        }

        // 商品番号チェック（13桁）
        if (!itemId.matches("\\d{13}")) {
            message = "商品番号は13桁の数字で入力してください。";
            sendJson(response, status, message);
            return;
        }

        // 文字数チェック
        if (name.length() > 100 || category.length() > 50 || shelfId.length() > 20) {
            message = "商品名・分類・棚番号の文字数が制限を超えています。";
            sendJson(response, status, message);
            return;
        }

        // 値の範囲チェック
        if (price < 0 || stockNow < 1 || stockMin < 0) {
            message = "価格は0以上、在庫数は1以上で入力してください。";
            sendJson(response, status, message);
            return;
        }

        try {
            Class.forName("org.h2.Driver");

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:h2:tcp://localhost/~/stockmaster;MODE=MySQL", "sa", "")) {

                conn.setAutoCommit(false);

                // 商品番号の重複チェック
                String checkItem = "SELECT COUNT(*) FROM ITEMS WHERE ITEM_ID = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkItem)) {
                    checkStmt.setString(1, itemId);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        message = "この商品番号は既に登録されています。";
                        sendJson(response, status, message);
                        return;
                    }
                }

                // 商品登録
                String sqlItem = "INSERT INTO ITEMS (ITEM_ID, ITEM_NAME, PRICE) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sqlItem)) {
                    stmt.setString(1, itemId);
                    stmt.setString(2, name);
                    stmt.setInt(3, price);
                    stmt.executeUpdate();
                }

                // 棚登録（存在しない場合のみ）
                String checkShelf = "SELECT COUNT(*) FROM SHELF WHERE SHELF_ID = ? AND STORE_ID = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkShelf)) {
                    checkStmt.setString(1, shelfId);
                    checkStmt.setInt(2, storeId);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) == 0) {
                        String insertShelf = "INSERT INTO SHELF (SHELF_ID, STORE_ID, CATEGORY) VALUES (?, ?, ?)";
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertShelf)) {
                            insertStmt.setString(1, shelfId);
                            insertStmt.setInt(2, storeId);
                            insertStmt.setString(3, category);
                            insertStmt.executeUpdate();
                        }
                    }
                }

                // 在庫登録
                String sqlStock = "INSERT INTO STOCK (ITEM_ID, SHELF_ID, STORE_ID, STOCK_NOW, STOCK_MIN) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sqlStock)) {
                    stmt.setString(1, itemId);
                    stmt.setString(2, shelfId);
                    stmt.setInt(3, storeId);
                    stmt.setInt(4, stockNow);
                    stmt.setInt(5, stockMin);
                    stmt.executeUpdate();
                }

                conn.commit();
                message = "商品登録が完了しました";
                status = "success";

            } catch (SQLException e) {
                e.printStackTrace();
                message = "登録に失敗しました（SQLエラー）: " + e.getMessage();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            message = "JDBCドライバのロードに失敗しました";
        }

        sendJson(response, status, message);
    }

    /** JSONを返す */
    private void sendJson(HttpServletResponse response, String status, String message) throws IOException {
        String json = String.format(
                "{\"status\":\"%s\",\"message\":\"%s\"}",
                status, message.replace("\"", "'"));
        response.getWriter().print(json);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ▼ ログイン状態確認
        HttpSession session = request.getSession(false);
        UserBean loginUser = (session != null) ? (UserBean) session.getAttribute("loginUser") : null;

        if (loginUser == null) {
            // 未ログイン → ログイン画面へ
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<Map<String, Object>> storeList = new ArrayList<>();

        // 店舗選択リスト取得
        try {
            Class.forName("org.h2.Driver");

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:h2:tcp://localhost/~/stockmaster;MODE=MySQL", "sa", "")) {

                String sql = "SELECT STORE_ID, STORE_NAME FROM STORES ORDER BY STORE_ID";
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        Map<String, Object> store = new HashMap<>();
                        store.put("storeId", rs.getInt("STORE_ID"));
                        store.put("storeName", rs.getString("STORE_NAME"));
                        storeList.add(store);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("storeList", storeList);
        request.setAttribute("loginUser", loginUser); // JSPでもユーザー情報使用可

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/productRegister.jsp");
        dispatcher.forward(request, response);
    }
}
