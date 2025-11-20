package stockmaster.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import stockmaster.dao.StockDao;

@WebServlet("/receiveStock")
public class ReceiveStockServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * 入荷（POST）
     * パラメータ:
     *   storeId (数字)
     *   productId (文字列)
     *   quantity (数字)
     *
     * レスポンス: application/json
     *   {"status":"success","message":"..."} または {"status":"error","message":"..."}
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        String json;
        String storeIdParam = req.getParameter("storeId");
        String productId = req.getParameter("productId");
        String quantityParam = req.getParameter("quantity");

        // 入力チェック
        if (storeIdParam == null || storeIdParam.trim().isEmpty()
                || productId == null || productId.trim().isEmpty()
                || quantityParam == null || quantityParam.trim().isEmpty()) {

            json = "{\"status\":\"error\",\"message\":\"店舗ID・商品ID・数量をすべて指定してください。\"}";
            resp.getWriter().write(json);
            return;
        }

        int storeId;
        int quantity;
        try {
            storeId = Integer.parseInt(storeIdParam.trim());
        } catch (NumberFormatException e) {
            json = "{\"status\":\"error\",\"message\":\"店舗IDは数値で指定してください。\"}";
            resp.getWriter().write(json);
            return;
        }

        try {
            quantity = Integer.parseInt(quantityParam.trim());
            if (quantity <= 0) {
                json = "{\"status\":\"error\",\"message\":\"数量は1以上の整数を指定してください。\"}";
                resp.getWriter().write(json);
                return;
            }
        } catch (NumberFormatException e) {
            json = "{\"status\":\"error\",\"message\":\"数量は数値で指定してください。\"}";
            resp.getWriter().write(json);
            return;
        }

        // ビジネス処理
        try {
            StockDao dao = new StockDao();
            boolean updated = dao.receiveStock(storeId, productId.trim(), quantity);

            if (updated) {
                json = "{\"status\":\"success\",\"message\":\"在庫を更新しました。\"}";
            } else {
                // update できなかった（該当レコードなしなど）
                json = "{\"status\":\"error\",\"message\":\"該当の在庫レコードが見つからなかったか、更新に失敗しました。\"}";
            }
        } catch (Exception e) {
            // サーバー側エラー。ログは出すがクライアントには汎用メッセージを返す
            e.printStackTrace();
            json = "{\"status\":\"error\",\"message\":\"サーバーエラーが発生しました。管理者に連絡してください。\"}";
        }

        resp.getWriter().write(json);
    }

    /**
     * GET は JSP にフォワード（画面表示）
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/views/receiveStock.jsp").forward(req, resp);
    }
}
