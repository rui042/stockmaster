package stockmaster;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/productRegister")  // ← メニューの action と合わせる
public class ProductRegisterServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 商品登録フォームを表示
    req.getRequestDispatcher("/views/productForm.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // フォーム送信後の登録処理
    String name = req.getParameter("name");
    String price = req.getParameter("price");

    // 登録処理（DB保存など）をここに書く想定
    req.setAttribute("message", "商品を登録しました: " + name + " / 価格: " + price);

    req.getRequestDispatcher("/views/productResult.jsp").forward(req, resp);
  }
}