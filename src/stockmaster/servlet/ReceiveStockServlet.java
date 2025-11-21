package stockmaster.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import stockmaster.bean.StockBean;
import stockmaster.bean.UserBean;
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

        HttpSession session = req.getSession(false);
        UserBean loginUser = (session != null) ? (UserBean) session.getAttribute("loginUser") : null;

        if (loginUser == null) {
            json = "{\"status\":\"error\",\"message\":\"ログインが必要です。\"}";
            resp.getWriter().write(json);
            return;
        }

        // storeId はセッションから取得（hiddenフィールドは参考程度）
        int storeId = loginUser.getStoreId();
        String productId = req.getParameter("productId");
        String quantityParam = req.getParameter("quantity");

        // 入力チェック
        if (productId == null || productId.trim().isEmpty()
                || quantityParam == null || quantityParam.trim().isEmpty()) {
            json = "{\"status\":\"error\",\"message\":\"商品ID・数量をすべて指定してください。\"}";
            resp.getWriter().write(json);
            return;
        }

        int quantity;

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

            // 現在の在庫を取得
            StockBean stock = dao.findByItemId(storeId, productId.trim());
            if (stock == null) {
                json = "{\"status\":\"error\",\"message\":\"該当の在庫レコードが見つかりません。\"}";
                resp.getWriter().write(json);
                return;
            }

            int current = stock.getStockNow();
            int updated = current + quantity;

            boolean updatedFlag = dao.receiveStock(storeId, productId.trim(), quantity, loginUser.getUserId());

            if (updatedFlag) {
                // 出荷処理と同じ形式のメッセージに統一
                String message = "商品ID " + productId.trim() + " の在庫を " + current + " → " + updated + " に更新しました。";
                json = String.format("{\"status\":\"success\",\"message\":\"%s\"}", message);
            } else {
                json = "{\"status\":\"error\",\"message\":\"更新に失敗しました。\"}";
            }
        } catch (Exception e) {
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
