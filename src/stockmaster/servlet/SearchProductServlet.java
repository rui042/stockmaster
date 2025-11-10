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

import stockmaster.bean.Item;
import stockmaster.dao.ItemDao;

@WebServlet("/searchProduct")
public class SearchProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ✅ セッション確認（ログイン状態チェック）
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            // 未ログインならログイン画面へリダイレクト
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 🔹 ログイン中ユーザー確認（デバッグ用）
        String username = (String) session.getAttribute("username");
        System.out.println("ログイン中ユーザー: " + username);

        String keyword = request.getParameter("keyword");
        String productId = request.getParameter("productId");

        ItemDao dao = new ItemDao();
        List<Item> results = new ArrayList<>();

        if (productId != null && !productId.isEmpty()) {
            // バーコード検索
            Item item = dao.searchById(productId);
            if (item != null) results.add(item);
        } else if (keyword != null && !keyword.isEmpty()) {
            // キーワード検索
            results = dao.searchByKeyword(keyword);
        } else {
            // 🔽 初期表示：全件取得
            results = dao.findAll();
        }

        request.setAttribute("results", results);

        // 🔹 結果をJSPへフォワード
        request.getRequestDispatcher("/views/searchProduct.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
