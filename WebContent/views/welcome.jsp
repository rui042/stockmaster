<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
  <title>ようこそ！</title>
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

    .welcome-box {
      width: 100%;
      max-width: 420px;
      background: #fff;
      padding: 40px 32px;
      border-radius: 16px;
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
      animation: fadeIn 0.6s ease;
      text-align: center;
    }

    @keyframes fadeIn {
      from { opacity: 0; transform: translateY(20px); }
      to   { opacity: 1; transform: translateY(0); }
    }

    .welcome-box h1 {
      font-size: 2rem;
      font-weight: 700;
      color: #0b67c2;
      margin-bottom: 32px;
      letter-spacing: 0.1em;
    }

    .welcome-box form {
      margin-bottom: 20px;
    }

    .welcome-box button {
      width: 100%;
      padding: 14px;
      font-size: 1.05rem;
      font-weight: 700;
      border: none;
      border-radius: 16px;
      cursor: pointer;
      background: linear-gradient(90deg, #17a2a8, #0b67c2);
      color: #fff;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      transition: transform 0.15s, opacity 0.2s;
    }

    .welcome-box button:hover {
      opacity: 0.9;
      transform: translateY(-2px);
    }

    .footer-links {
      margin-top: 20px;
      text-align: center;
      font-size: 0.9rem;
    }

    .guest-btn {
      margin-top: 12px;
      background: none;
      border: none;
      color: #0b67c2;
      text-decoration: underline;
      cursor: pointer;
      font-size: 0.95rem;
    }

    .guest-btn:hover {
      opacity: 0.8;
    }
  </style>
<body>
  <div class="welcome-box">
    <h1>すとっくますたー</h1>

    <!-- ログイン -->
    <form action="${pageContext.request.contextPath}/login" method="get">
      <button type="submit">ログインをする</button>
    </form>

    <!-- 新規登録 -->
    <form action="${pageContext.request.contextPath}/register" method="get">
      <button type="submit">新規登録をする</button>
    </form>

    <!-- ログインせずに使用 -->
    <div class="footer-links">
      <a href="${pageContext.request.contextPath}/menu" class="guest-btn">ログインせずに使用する</a>
    </div>
  </div>
</body>
</html>