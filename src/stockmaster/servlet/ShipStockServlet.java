package stockmaster.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import stockmaster.bean.UserBean;
import stockmaster.dao.Dao;

@WebServlet("/shipStock")
public class ShipStockServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ▼ ログイン状態確認
        HttpSession session = req.getSession(false);
        UserBean loginUser = (session != null) ? (UserBean) session.getAttribute("loginUser") : null;

        if (loginUser == null) {
            // 未ログイン → ログイン画面にリダイレクト
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.setAttribute("loginUser", loginUser); // ログイン中ユーザー情報を渡す
        // shipStock.jsp にフォワード
        req.getRequestDispatcher("/views/shipStock.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        // ▼ ログイン状態確認
        HttpSession session = req.getSession(false);
        UserBean loginUser = (session != null) ? (UserBean) session.getAttribute("loginUser") : null;

        if (loginUser == null) {
            // 未ログインならJSONでエラーを返す
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"ログインが必要です。\"}");
            return;
        }

        // 店舗IDはセッションから固定
        int storeId = loginUser.getStoreId();
        String productId = req.getParameter("productId");
        String qtyStr = req.getParameter("quantity");

        String message;
        String status = "error";

        if (productId == null || productId.isEmpty() || qtyStr == null || qtyStr.isEmpty()) {
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"商品IDと数量を入力してください。\"}");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(qtyStr);
            if (quantity <= 0) {
                resp.getWriter().write("{\"status\":\"error\",\"message\":\"数量は1以上の整数を指定してください。\"}");
                return;
            }
        } catch (NumberFormatException e) {
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"数量は数値で指定してください。\"}");
            return;
        }

        try (Connection conn = new Dao() {}.getConnection()) {
        	conn.setAutoCommit(false); // トランザクション開始

            // 在庫確認
            PreparedStatement psSelect = conn.prepareStatement(
                "SELECT STOCK_NOW FROM STOCK WHERE ITEM_ID = ? AND STORE_ID = ?"
            );
            psSelect.setString(1, productId);
            psSelect.setInt(2, storeId);
            ResultSet rs = psSelect.executeQuery();

            if (rs.next()) {
                int current = rs.getInt("STOCK_NOW");

                if (current >= quantity) {
                    int updated = current - quantity;

                    // 在庫更新
                    PreparedStatement psUpdate = conn.prepareStatement(
                        "UPDATE STOCK SET STOCK_NOW = ? WHERE ITEM_ID = ? AND STORE_ID = ?"
                    );
                    psUpdate.setInt(1, updated);
                    psUpdate.setString(2, productId);
                    psUpdate.setInt(3, storeId);
                    psUpdate.executeUpdate();

                    // 最新状態をSTOCK_STATUSに記録
                    PreparedStatement psStatus = conn.prepareStatement(
                            "MERGE INTO STOCK_STATUS (ITEM_ID, STORE_ID, LAST_ACTION_TYPE, QUANTITY, ACTION_AT) " +
                            "KEY (ITEM_ID, STORE_ID, LAST_ACTION_TYPE) " +
                            "VALUES (?, ?, 'SHIP', ?, CURRENT_TIMESTAMP)"
                    );
                    psStatus.setString(1, productId);
                    psStatus.setInt(2, storeId);
                    psStatus.setInt(3, quantity);
                    psStatus.executeUpdate();

                    conn.commit();

                    message = "商品ID " + productId + " の在庫を " + current + " → " + updated + " に更新しました。";
                    status = "success";
                } else {
                    message = "在庫が不足しています（現在: " + current + "）";
                }
            } else {
                message = "該当する商品がこの店舗に存在しません。";
            }

        } catch (Exception e) {
            e.printStackTrace();
            message = "データベースエラーが発生しました。";
        }

        // JSONレスポンス
        String json = String.format("{\"status\":\"%s\",\"message\":\"%s\"}",
            status, message.replace("\"", "'"));
        resp.getWriter().write(json);
    }
}
