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

	  HttpSession session = req.getSession(true);

	    // ログインせずに使用を押された場合直前のログイン認証をリセット
	    String guestFlag = req.getParameter("guest");
	    if ("true".equals(guestFlag)) {
	      session.removeAttribute("loginUser");
	      session.removeAttribute("username");
	      session.removeAttribute("isStaff");
	      session.removeAttribute("chatHistory");
	      session.removeAttribute("currentStepKey");
	    }

    // メニュー表示（JSPの“実ファイルパス”へ forward）
    req.getRequestDispatcher("/views/menu.jsp").forward(req, resp);
  }
}