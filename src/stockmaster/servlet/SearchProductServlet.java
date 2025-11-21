package stockmaster.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import stockmaster.bean.StockBean;
import stockmaster.bean.UserBean;
import stockmaster.dao.StockDao;

@WebServlet("/searchProduct")
public class SearchProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        UserBean loginUser = (session != null) ? (UserBean) session.getAttribute("loginUser") : null;

        if (loginUser == null) {
            // 未ログインならログイン画面へリダイレクト
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String keyword = request.getParameter("keyword");
        String productId = request.getParameter("productId");

        // 仮にログインユーザーに紐づく店舗ID
        int storeId = loginUser.getStoreId();

        StockDao dao = new StockDao();
        List<StockBean> results = new ArrayList<>();

        if (productId != null && !productId.trim().isEmpty()) {
            // 商品IDで検索（バーコード検索）
            StockBean bean = dao.findByItemId(storeId, productId.trim());
            if (bean != null) {
                results.add(bean);
            }
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            // キーワード検索
            results = dao.findByStoreAndKeyword(storeId, keyword.trim());
        } else {
            // 店舗全体の在庫を取得
            results = dao.findByStore(storeId);
        }

        request.setAttribute("results", results);
        request.getRequestDispatcher("/views/searchProduct.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}