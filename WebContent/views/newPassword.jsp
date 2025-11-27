<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
	<title>新しパスワード設定</title>
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

        .logo {
		      text-align: center;
		      margin-bottom: 24px;
		    }

		    .logo img {
		      width: 240px;
		      height: auto;
		      filter: drop-shadow(0 4px 6px rgba(0,0,0,0.2));
		    }

        label {
          display: block;
          margin-top: 16px;
          font-weight: 600;
          color: #333;
        }

        h2 {
				  position: relative;
				  padding: 0.25em 0;
				  font-weight: 700;
				  color: #0b67c2;
				  text-align: center;
				}

				h2:after {
				  content: "";
				  display: block;
				  height: 4px;
				  margin-top: 8px;
				  background: linear-gradient(to right, rgb(255, 186, 115), #ffb2b2);
				  border-radius: 2px;
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
    </style>
</head>
<body>
  <div class="login-box">
    <!-- 🔹 ロゴ -->
    <div class="logo">
      <img src="${pageContext.request.contextPath}/resources/logo.png" alt="ストックマスター ロゴ">
    </div>

		<h2>パスワードの再設定</h2>

    <form action="${pageContext.request.contextPath}/newPassword" method="post">
      <input type="hidden" name="email" value="${email}">

      <label for="newPassword">新しいパスワード:</label>
      <input type="password" name="newPassword" required>

      <label for="confirmPassword">確認用パスワード:</label>
      <input type="password" name="confirmPassword" required>

      <button type="submit">確定</button>
    </form>

    <c:if test="${not empty errorMessage}">
      <p class="error">${errorMessage}</p>
    </c:if>
  </div>
</body>
</html>