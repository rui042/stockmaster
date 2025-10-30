package stockmaster.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import stockmaster.bean.UserBean;
import stockmaster.dao.UserDao;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("[RegisterServlet] doPost START");

        request.setCharacterEncoding("UTF-8");

        String userId = request.getParameter("userId");
        String name = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        System.out.println("[RegisterServlet] 入力値 userId=" + userId + ", name=" + name + ", email=" + email);

        // 入力チェック
        if (userId == null || userId.isEmpty() ||
            name == null || name.isEmpty() ||
            email == null || email.isEmpty() ||
            password == null || password.isEmpty() ||
            confirmPassword == null || confirmPassword.isEmpty()) {

            System.out.println("[RegisterServlet] 入力不足");
            request.setAttribute("error", "すべての項目を入力してください。");
            request.getRequestDispatcher("views/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            System.out.println("[RegisterServlet] パスワード不一致");
            request.setAttribute("error", "パスワードが一致しません。");
            request.getRequestDispatcher("views/register.jsp").forward(request, response);
            return;
        }

        // UserBean に詰める
        UserBean user = new UserBean(userId, password, name, email, false);

        UserDao userDao = new UserDao();
        boolean success = userDao.insert(user);

        System.out.println("[RegisterServlet] insert結果=" + success);

        if (success) {
            System.out.println("[RegisterServlet] 登録成功 → login.jspへリダイレクト");
            response.sendRedirect(request.getContextPath() + "views/login.jsp");
        } else {
            System.out.println("[RegisterServlet] 登録失敗 → register.jspへ戻す");
            request.setAttribute("error", "登録に失敗しました。ユーザーIDが既に存在する可能性があります。");
            request.getRequestDispatcher("views/register.jsp").forward(request, response);
        }
    }
}