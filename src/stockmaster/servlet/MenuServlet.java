package stockmaster.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/menu")
public class MenuServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

	  	// ログインしていない場合（ゲストアクセス）として username を null に設定
	    HttpSession session = req.getSession();
	    if (session.getAttribute("username") == null) {
	      session.setAttribute("username", null);
	    }

    // メニュー表示（JSPの“実ファイルパス”へ forward）
    req.getRequestDispatcher("/views/menu.jsp").forward(req, resp);
  }
}