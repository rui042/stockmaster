package stockmaster.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/newPassword")
public class NewPasswordServlet extends HttpServlet {

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/views/newPassword.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        // 🔹 パスワードチェック
        if (newPassword == null || confirmPassword == null) {
            req.setAttribute("errorMessage", "パスワードを入力してください。");
            req.setAttribute("email", email);
            req.getRequestDispatcher("/views/newPassword.jsp").forward(req, resp);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            req.setAttribute("errorMessage", "パスワードが一致しません。");
            req.setAttribute("email", email);
            req.getRequestDispatcher("/views/newPassword.jsp").forward(req, resp);
            return;
        }

        // 文字数チェック（8〜16文字）
        if (newPassword.length() < 8 || newPassword.length() > 16) {
            req.setAttribute("errorMessage", "パスワードは8〜16文字で入力してください。");
            req.setAttribute("email", email);
            req.getRequestDispatcher("/views/newPassword.jsp").forward(req, resp);
            return;
        }

        // 英字と数字を必ず含むチェック
        if (!newPassword.matches(".*[A-Za-z].*") || !newPassword.matches(".*[0-9].*")) {
            req.setAttribute("errorMessage", "パスワードには英字と数字を必ず含めてください。");
            req.setAttribute("email", email);
            req.getRequestDispatcher("/views/newPassword.jsp").forward(req, resp);
            return;
        }

        // パスワードは必ずハッシュ化して保存（例: BCrypt）
//        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

     // JDBCドライバのロード
    	try {
    	    Class.forName("org.h2.Driver");
    	} catch (ClassNotFoundException e) {
    	    e.printStackTrace();
    	}

        // DB更新処理
        try (Connection conn = DriverManager.getConnection(
                "jdbc:h2:tcp://localhost/~/stockmaster;MODE=MySQL", "sa", "")) {

            String sql = "UPDATE USERS SET PASSWORD = ? WHERE EMAIL = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newPassword); // 平文保存
            stmt.setString(2, email);

            int updated = stmt.executeUpdate();

            if (updated > 0) {
                req.setAttribute("message", "パスワードが更新されました。ログイン画面から再度ログインしてください。");
                req.getRequestDispatcher("/views/resetComplete.jsp").forward(req, resp);
            } else {
                req.setAttribute("errorMessage", "パスワード更新に失敗しました。");
                req.setAttribute("email", email);
                req.getRequestDispatcher("/views/newPassword.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
