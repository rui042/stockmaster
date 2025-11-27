package stockmaster.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/resetPassword")
public class ResetPasswordServlet extends HttpServlet {

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/views/resetPassword.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");

        // JDBCドライバのロード
    	try {
    	    Class.forName("org.h2.Driver");
    	} catch (ClassNotFoundException e) {
    	    e.printStackTrace();
    	}

        try (Connection conn = DriverManager.getConnection(
                "jdbc:h2:tcp://localhost/~/stockmaster;MODE=MySQL", "sa", "")) {

            String sql = "SELECT USER_ID FROM USERS WHERE EMAIL = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // ユーザーが存在する場合 → パスワード再設定画面へ
                req.setAttribute("email", email);
                req.getRequestDispatcher("/views/newPassword.jsp").forward(req, resp);
            } else {
                // ユーザーが存在しない場合
                req.setAttribute("errorMessage", "このメールアドレスは登録されていません。");
                req.getRequestDispatcher("/views/resetPassword.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
