package stockmaster.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import stockmaster.bean.UserBean;
import stockmaster.dao.UserDao;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * 新規登録ページを表示（GETアクセス対応）
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 🔹 ログインチェック
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 🔹 管理者権限チェック
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (isAdmin == null || !isAdmin) {
            request.setAttribute("error", "管理者のみ新規登録が可能です。");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

    /**
     * 新規登録処理（POSTアクセス対応）
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // System.out.println("[RegisterServlet] doPost START");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (isAdmin == null || !isAdmin) {
            request.setAttribute("error", "管理者のみ新規登録が可能です。");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String userId = request.getParameter("userId");
        String name = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // System.out.println("[RegisterServlet] 入力値 userId=" + userId + ", name=" + name + ", email=" + email);

        // 店舗はログインユーザーに固定
        UserBean loginUser = (UserBean) session.getAttribute("loginUser");
        Integer storeId = loginUser.getStoreId();

        try {
            // 🔹 入力チェック
            if (userId == null || userId.isEmpty() ||
                name == null || name.isEmpty() ||
                email == null || email.isEmpty() ||
                password == null || password.isEmpty() ||
                confirmPassword == null || confirmPassword.isEmpty()) {
                sendJson(response, "error", "すべての項目を入力してください。");
                return;
            }

            if (!password.equals(confirmPassword)) {
                sendJson(response, "error", "パスワードが一致しません。");
                return;
            }

            // 🔹 UserBean に詰める（スタッフ=true, 管理者=false）
            UserBean user = new UserBean(userId, password, name, email, true, false);
            user.setStoreId(storeId);

            UserDao userDao = new UserDao();
            boolean success = userDao.insert(user);

	        // System.out.println("[RegisterServlet] insert結果=" + success);

	        if (success) {
	            // 🔹 登録成功 → JSONで成功メッセージを返す
	            sendJson(response, "success", "登録が完了しました。");
	        } else {
	            // 🔹 登録失敗 → JSONでエラーメッセージを返す
	            sendJson(response, "error", "登録に失敗しました。ユーザーIDが既に存在する可能性があります。");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        // 🔹 例外発生時も JSON で返す
	        sendJson(response, "error", "登録に失敗しました: " + e.getMessage());
	    }
	}
	/**
	 * JSONレスポンスを返す共通メソッド
	 * @param response HttpServletResponse
	 * @param status "success" または "error"
	 * @param message 表示するメッセージ
	 */
	private void sendJson(HttpServletResponse response, String status, String message) throws IOException {
	    response.setContentType("application/json; charset=UTF-8");
	    PrintWriter out = response.getWriter();
	    // 🔹 シンプルな JSON 形式で返却
	    out.print("{\"status\":\"" + status + "\", \"message\":\"" + message + "\"}");
	    out.flush();
	}
}
