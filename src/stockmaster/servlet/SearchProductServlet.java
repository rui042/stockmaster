package stockmaster.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/searchProduct")
public class SearchProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String keyword = req.getParameter("keyword");
        String genre = req.getParameter("genre");

        // ★ 本来はDB検索するところ。ここではサンプルデータを用意
        List<Map<String, String>> allProducts = new ArrayList<>();

        allProducts.add(makeProduct("P001", "りんご", "果物", "120"));
        allProducts.add(makeProduct("P002", "みかん", "果物", "80"));
        allProducts.add(makeProduct("P003", "牛乳", "飲料", "200"));
        allProducts.add(makeProduct("P004", "ポテトチップス", "お菓子", "50"));
        allProducts.add(makeProduct("P005", "トマト", "野菜", "60"));
        allProducts.add(makeProduct("P006", "洗剤", "日用品", "30"));

        // フィルタリング
        List<Map<String, String>> results = new ArrayList<>();
        for (Map<String, String> p : allProducts) {
            boolean match = true;
            if (keyword != null && !keyword.isEmpty()) {
                if (!p.get("name").contains(keyword)) {
                    match = false;
                }
            }
            if (genre != null && !genre.isEmpty()) {
                if (!p.get("genre").equals(genre)) {
                    match = false;
                }
            }
            if (match) results.add(p);
        }

        // JSPに渡す
        req.setAttribute("results", results);
        req.getRequestDispatcher("/views/searchProduct.jsp").forward(req, resp);
    }

    private Map<String, String> makeProduct(String id, String name, String genre, String stock) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("genre", genre);
        map.put("stock", stock);
        return map;
    }
}