package stockmaster.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import stockmaster.dao.RegisterDao;

@WebServlet("/productRegister")
public class ProductRegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        // 商品登録処理メソッド
        boolean forceRegister = "true".equals(request.getParameter("forceRegister"));

        // ログイン状態確認
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
        String note = request.getParameter("note");

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

        // 棚番号の形式チェック
        if (!shelfId.matches("^[A-Za-z]+-[0-9]+$")) {
            sendJson(response, "error", "棚番号は「英字-数字」の形式で入力してください（例：A-01）。");
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
            RegisterDao dao = new RegisterDao();

            // 分類不一致チェック
            if (!forceRegister) {
                String conflictMessage = dao.checkShelfCategory(shelfId, storeId, category);
                if (conflictMessage != null) {
                    sendJson(response, "warning", conflictMessage);
                    return;
                }
            }

            // 備考不一致チェック
            String noteConflictMessage = dao.checkShelfNote(shelfId, storeId, note);
            if (noteConflictMessage != null && !forceRegister) {
                sendJson(response, "warning", noteConflictMessage);
                return;
            }

            // 実際の登録処理
            String result = dao.registerProduct(itemId, name, category, shelfId, price, stockNow, stockMin, storeId, note, forceRegister);

            if ("success".equals(result)) {
                sendJson(response, "success", "商品登録が完了しました。");
            } else {
                sendJson(response, "error", result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendJson(response, "error", "登録に失敗しました: " + e.getMessage());
        }
    }

    /** JSONを返す */
    private void sendJson(HttpServletResponse response, String status, String message) throws IOException {
        // message内の改行・ダブルクオートをエスケープ
        String escapedMessage = message.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");

        String json = String.format(
                "{\"status\":\"%s\",\"message\":\"%s\"}",
                status, escapedMessage);

        response.getWriter().print(json);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ログイン状態確認
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
        request.setAttribute("loginUser", loginUser);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/productRegister.jsp");
        dispatcher.forward(request, response);
    }
}
