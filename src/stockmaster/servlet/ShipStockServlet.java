package stockmaster.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import stockmaster.bean.StoreBean;
import stockmaster.dao.Dao;
import stockmaster.dao.StoreDao;

@WebServlet("/shipStock")
public class ShipStockServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // 店舗リストを取得
            StoreDao storeDao = new StoreDao();
            List<StoreBean> storeList = storeDao.findAll();

            // JSPに渡す
            req.setAttribute("storeList", storeList);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "店舗情報の取得に失敗しました。");
        }

        // shipStock.jsp にフォワード
        req.getRequestDispatcher("/views/shipStock.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        String storeIdStr = req.getParameter("storeId");
        String productId = req.getParameter("productId");
        String qtyStr = req.getParameter("quantity");

        String message;
        String status = "error";

        if (storeIdStr == null || productId == null || qtyStr == null ||
            storeIdStr.isEmpty() || productId.isEmpty() || qtyStr.isEmpty()) {
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"全ての項目を入力してください・。\"}");
            return;
        }

        int storeId;
        int quantity;
        try {
            storeId = Integer.parseInt(storeIdStr);
            quantity = Integer.parseInt(qtyStr);
        } catch (NumberFormatException e) {
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"入力値が不正です。\"}");
            return;
        }

        try (Connection conn = new Dao(){}.getConnection()) {

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

                    // 即時在庫更新
                    PreparedStatement psUpdate = conn.prepareStatement(
                        "UPDATE STOCK SET STOCK_NOW = ? WHERE ITEM_ID = ? AND STORE_ID = ?"
                    );
                    psUpdate.setInt(1, updated);
                    psUpdate.setString(2, productId);
                    psUpdate.setInt(3, storeId);
                    psUpdate.executeUpdate();

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
