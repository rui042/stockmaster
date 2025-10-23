package stockmaster;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/receiveStock")
public class ReceiveStockServlet extends HttpServlet {

    // ★ 本来はDBを使うが、ここではサンプル用にメモリ上のデータを用意
    private static Map<String, Integer> stockDB = new HashMap<>();

    static {
        stockDB.put("P001", 120);
        stockDB.put("P002", 80);
        stockDB.put("P003", 45);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 入荷フォームを表示
        req.getRequestDispatcher("/views/receiveStock.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String productId = req.getParameter("productId");
        String qtyStr = req.getParameter("quantity");

        int quantity = 0;
        try {
            quantity = Integer.parseInt(qtyStr);
        } catch (NumberFormatException e) {
            req.setAttribute("message", "数量が不正です");
            req.getRequestDispatcher("/views/stockResult.jsp").forward(req, resp);
            return;
        }

        // 在庫更新処理
        if (stockDB.containsKey(productId)) {
            int current = stockDB.get(productId);
            int updated = current + quantity;
            stockDB.put(productId, updated);
            req.setAttribute("message", "商品ID " + productId + " の在庫を " + current + " → " + updated + " に更新しました。");
        } else {
            req.setAttribute("message", "商品ID " + productId + " は存在しません。");
        }

        // 結果画面へ
        req.getRequestDispatcher("/views/stockResult.jsp").forward(req, resp);
    }
}