package stockmaster.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import stockmaster.bean.UserBean;
import stockmaster.dao.UserDao;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 🔹 GETで来た場合はログイン画面を表示
        req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String userId = req.getParameter("userId");
        String password = req.getParameter("password");

        System.out.println("入力値: userId=" + userId + ", password=" + password);

        UserDao dao = new UserDao();
        UserBean user = dao.findByIdAndPassword(userId, password);

        if (user != null) {
            System.out.println("ログイン成功: " + user.getName());

            HttpSession session = req.getSession();
            session.setAttribute("username", user.getName());
            session.setAttribute("isStaff", user.isStaff());

            // ✅ メニューにリダイレクト
            resp.sendRedirect(req.getContextPath() + "/menu");

        } else {
            System.out.println("ログイン失敗: 該当ユーザーなし");
            req.setAttribute("error", "ユーザーIDまたはパスワードが間違っています");
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
        }
    }
}
