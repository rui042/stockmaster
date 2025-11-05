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

import stockmaster.dao.Dao;

@WebServlet("/shipStock")
public class ShipStockServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        String productId = req.getParameter("productId");
        String qtyStr = req.getParameter("quantity");
        System.out.println("[ShipStockServlet] 商品ID=" + productId + ", 数量=" + qtyStr);

        int quantity = 0;
        String message = "";
        String status = "error";

        try {
            quantity = Integer.parseInt(qtyStr);
        } catch (NumberFormatException e) {
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"数量が不正です。\"}");
            return;
        }

        try (Connection conn = new Dao(){}.getConnection()) { // Dao は抽象クラスなので匿名サブクラス化して利用
            // 在庫取得
            PreparedStatement select = conn.prepareStatement(
                "SELECT STOCK_NOW FROM STOCK WHERE ITEM_ID = ?"
            );
            select.setString(1, productId);
            ResultSet rs = select.executeQuery();

            if (rs.next()) {
                int current = rs.getInt("STOCK_NOW");

                if (current >= quantity) {
                    int updated = current - quantity;

                    PreparedStatement update = conn.prepareStatement(
                        "UPDATE STOCK SET STOCK_NOW = ? WHERE ITEM_ID = ?"
                    );
                    update.setInt(1, updated);
                    update.setString(2, productId);
                    update.executeUpdate();

                    message = "商品ID " + productId + " の在庫を " + current + " → " + updated + " に更新しました。";
                    status = "success";
                } else {
                    message = "商品ID " + productId + " の在庫が不足しています（現在: " + current + "）";
                }
            } else {
                message = "商品ID " + productId + " は存在しません。";
            }

        } catch (Exception e) {
            e.printStackTrace();
            message = "データベースエラーが発生しました。";
        }

        // JSON文字列を返す（Gsonなし）
        String json = String.format(
            "{\"status\":\"%s\",\"message\":\"%s\"}",
            status,
            message.replace("\"", "'")
        );
        resp.getWriter().write(json);
    }

    // GETでアクセスされた場合（初期画面表示用）
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/views/shipStock.jsp").forward(req, resp);
    }
}
