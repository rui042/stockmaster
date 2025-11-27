<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
	<title>Insert title here</title>
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
          text-align: center;
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

        h3 {
          color: #333;
          margin-bottom: 24px;
        }

        a {
          display: inline-block;
          margin-top: 20px;
          color: #0b67c2;
          text-decoration: underline;
          font-weight: 600;
          font-size: 1rem;
        }

        a:hover {
          opacity: 0.8;
        }
    </style>
</head>
<body>
	<div class="login-box">
		<!-- 🔹 ロゴ -->
	  <div class="logo">
	    <img src="${pageContext.request.contextPath}/resources/logo.png" alt="ストックマスター ロゴ">
	  </div>

	  <h2>変更完了！</h2>

	  <h3>${message}</h3>

	  <p><a href="${pageContext.request.contextPath}/login">ログイン画面へ戻る</a></p>
  </div>
</body>
</html>