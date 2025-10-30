package stockmaster.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/searchStore")
public class SearchStoreServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    // 検索条件の取得（今は未使用）
    String name = request.getParameter("name");
    String area = request.getParameter("area");
    String category = request.getParameter("category");
    String openOnly = request.getParameter("openOnly");

    // 仮の店舗リスト（DAOができるまでのダミーデータ）
    //List<Store> storeList = new ArrayList<>();
    //.add(new Store(1, "札幌ラーメン一番", "札幌市中央区南1条", "011-123-4567"));
    //storeList.add(new Store(2, "カフェ・ド・蒼", "札幌市北区北24条", "011-987-6543"));

    //request.setAttribute("storeList", storeList);
    request.getRequestDispatcher("/views/storeSearch.jsp").forward(request, response);
  }
}