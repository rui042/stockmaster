package stockmaster.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import stockmaster.bean.UserBean;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

	  	// 文字コード指定
	    req.setCharacterEncoding("UTF-8");
	    resp.setContentType("text/html; charset=UTF-8");

	  // ログインチェック
	  HttpSession session = req.getSession(false);
	  if (session == null || session.getAttribute("loginUser") == null) {
	      resp.sendRedirect(req.getContextPath() + "/login");
	      return;
	  }

	  // 履歴の取得
	  UserBean user = (UserBean) session.getAttribute("loginUser");
	    List<String[]> history = loadChatHistory(user.getUserId());
	    session.setAttribute("chatHistory", history);

    req.getRequestDispatcher("/views/chat.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

	  	// 文字コード指定
	    req.setCharacterEncoding("UTF-8");
	    resp.setContentType("text/html; charset=UTF-8");

	  // ログインチェック
	  HttpSession session = req.getSession(false);
	  if (session == null || session.getAttribute("loginUser") == null) {
	      resp.sendRedirect(req.getContextPath() + "/login");
	      return;
	  }

	// JDBCドライバのロード
	try {
	    Class.forName("org.h2.Driver");
	  } catch (ClassNotFoundException e) {
	   System.out.println("H2ドライバのロードに失敗しました");
	    e.printStackTrace();
	  }

	// 呼び出し
	UserBean user = (UserBean) session.getAttribute("loginUser");

    String userInput = req.getParameter("message");
    String currentStepKey = req.getParameter("currentStepKey");

    // デバッグログ確認用
//     System.out.println("DEBUG: userInput = " + userInput);
//     System.out.println("DEBUG: currentStepKey = " + currentStepKey);

    if (currentStepKey == null || currentStepKey.isEmpty()) {
      currentStepKey = "START";
    }

    String nextStepKey = findNextStepKey(currentStepKey, userInput);
    String responseMessage = findMessageByStepKey(nextStepKey);

    // 履歴の保存
    saveChatEntry(user.getUserId(), "me", userInput);
    saveChatEntry(user.getUserId(), "you", responseMessage);

    List<String[]> history = loadChatHistory(user.getUserId());
    session.setAttribute("chatHistory", history);

    String deleteFlag = req.getParameter("delete");
    if ("true".equals(deleteFlag)) {
        HttpSession currentSession = req.getSession(false);
        if (currentSession == null || currentSession.getAttribute("loginUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        UserBean loginUser = (UserBean) currentSession.getAttribute("loginUser");
        deleteChatHistory(loginUser.getUserId());

        currentSession.setAttribute("chatHistory", new ArrayList<String[]>());

        req.getRequestDispatcher("/views/chat.jsp").forward(req, resp);
        return;
    }

    req.setAttribute("responseMessage", responseMessage);
    req.setAttribute("currentStepKey", nextStepKey);
    req.setAttribute("userMessage", userInput);
    req.getRequestDispatcher("/views/chat.jsp").forward(req, resp);
  }

  private String findNextStepKey(String currentStepKey, String inputValue) {
    String nextStepKey = null;
    String sql = "SELECT c.NEXT_STEP_KEY FROM CHAT_CHOICES c " +
            "JOIN CHAT_STEPS s ON c.STEP_ID = s.STEP_ID " +
            "WHERE s.STEP_KEY = ? AND ? LIKE CONCAT('%', c.INPUT_VALUE, '%')";

    // デバッグログ確認用
//     System.out.println("DEBUG: Executing SQL for STEP_KEY=" + currentStepKey + " and INPUT_VALUE=" + inputValue);

    try (Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/stockmaster;MODE=MySQL", "sa", "");
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, currentStepKey);
      stmt.setString(2, inputValue);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        nextStepKey = rs.getString("NEXT_STEP_KEY");
      }
      // デバッグログ確認用
//      if (rs.next()) {
//    	  nextStepKey = rs.getString("NEXT_STEP_KEY");
//    	  System.out.println("DEBUG: nextStepKey = " + nextStepKey);
//    	} else {
//    	  System.out.println("DEBUG: No match found in CHAT_CHOICES.");
//      }
//      if (!rs.next()) {
//    	  System.out.println("DEBUG: No match found for STEP_KEY=" + currentStepKey + " and INPUT_VALUE=" + inputValue);
//    	} else {
//    	  nextStepKey = rs.getString("NEXT_STEP_KEY");
//    	  System.out.println("DEBUG: nextStepKey = " + nextStepKey);
//      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return nextStepKey;
  }

  private String findMessageByStepKey(String stepKey) {
    String message = "入力が正しくありません。もう一度お願いします。";
    String sql = "SELECT MESSAGE FROM CHAT_STEPS WHERE STEP_KEY = ?";

    // デバッグログ確認用
//    System.out.println("DEBUG: Looking up message for stepKey = " + stepKey);
//    System.out.println("DEBUG: Retrieved message = " + message);

    try (Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/stockmaster;MODE=MySQL", "sa", "");
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, stepKey);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        message = rs.getString("MESSAGE");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return message;
  }

  // 履歴保存メソッド
  private void saveChatEntry(String userId, String senderType, String message) {
	    String sql = "INSERT INTO CHAT_HISTORY (USER_ID, SENDER_TYPE, MESSAGE) VALUES (?, ?, ?)";

	    try (Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/stockmaster;MODE=MySQL", "sa", "");
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, userId);
	        stmt.setString(2, senderType);
	        stmt.setString(3, message);
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

  // 履歴取得メソッド
  private List<String[]> loadChatHistory(String userId) {
	    List<String[]> history = new ArrayList<>();
	    String sql = "SELECT SENDER_TYPE, MESSAGE FROM CHAT_HISTORY WHERE USER_ID = ? ORDER BY CREATED_AT";

	    try (Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/stockmaster;MODE=MySQL", "sa", "");
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, userId);
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            history.add(new String[]{rs.getString("SENDER_TYPE"), rs.getString("MESSAGE")});
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return history;
	}

  // 履歴削除メソッド
  private void deleteChatHistory(String userId) {
	    String sql = "DELETE FROM CHAT_HISTORY WHERE USER_ID = ?";

	    try (Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/stockmaster;MODE=MySQL", "sa", "");
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, userId);
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
}