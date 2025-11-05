package stockmaster.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    // セッション破棄
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }

    // URLを変えずに login.jsp へ遷移（内部フォワード）
    request.getRequestDispatcher("/views/login.jsp").forward(request, response);
  }
}
