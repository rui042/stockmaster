<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>ログイン | すとっくますたー</title>
  <style>
    body {
      font-family: "Yu Gothic", "Segoe UI", system-ui, sans-serif;
      background: linear-gradient(135deg, #0b67c2, #17a2a8);
      margin: 0;
      padding: 0;
      display: flex;
      align-items: center;
      justify-content: center;
      height: 100vh;
    }
    .login-box {
      width: 100%;
      max-width: 420px;
      background: #fff;
      padding: 40px 32px;
      border-radius: 16px;
      box-shadow: 0 8px 24px rgba(0,0,0,0.15);
      animation: fadeIn 0.6s ease;
    }
    @keyframes fadeIn {
      from { opacity: 0; transform: translateY(20px); }
      to   { opacity: 1; transform: translateY(0); }
    }
    h2 {
      margin: 0 0 24px;
      color: #0b67c2;
      font-size: 1.8rem;
      text-align: center;
      letter-spacing: 0.05em;
    }
    label {
      display: block;
      margin-top: 16px;
      font-weight: 600;
      color: #333;
    }
    input {
      width: 100%;
      padding: 12px;
      margin-top: 6px;
      border: 1px solid #ccc;
      border-radius: 8px;
      font-size: 1rem;
      transition: border-color 0.2s, box-shadow 0.2s;
    }
    input:focus {
      border-color: #0b67c2;
      box-shadow: 0 0 0 3px rgba(11,103,194,0.2);
      outline: none;
    }
    button {
      margin-top: 28px;
      padding: 14px;
      width: 100%;
      background: linear-gradient(90deg, #17a2a8, #0b67c2);
      color: #fff;
      border: none;
      border-radius: 8px;
      font-weight: 700;
      cursor: pointer;
      font-size: 1.05rem;
      transition: transform 0.15s, opacity 0.2s;
    }
    button:hover {
      opacity: 0.9;
      transform: translateY(-2px);
    }
    .error {
      margin-top: 16px;
      color: #d93025;
      font-weight: 600;
      text-align: center;
    }
    .footer-links {
      margin-top: 20px;
      text-align: center;
      font-size: 0.9rem;
    }
    .footer-links a {
      color: #0b67c2;
      text-decoration: none;
      margin: 0 8px;
    }
    .footer-links a:hover {
      text-decoration: underline;
    }
  </style>
</head>
<body>
  <div class="login-box">
    <h2>すとっくますたー ログイン</h2>
    <form action="${pageContext.request.contextPath}/login" method="post">
      <label for="userId">ユーザーID</label>
      <input type="text" id="userId" name="userId" required autofocus>

      <label for="password">パスワード</label>
      <input type="password" id="password" name="password" required>

      <button type="submit">ログイン</button>
    </form>

    <c:if test="${not empty error}">
      <div class="error">${error}</div>
    </c:if>

    <div class="footer-links">
      <a href="register.jsp">新規登録</a> |
      <a href="#">パスワードを忘れた？</a>
    </div>
  </div>
</body>
</html>