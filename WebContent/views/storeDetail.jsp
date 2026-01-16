<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
  String username = (String) session.getAttribute("username");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <title>店舗情報</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <style>
    :root {
      --primary: #0b67c2;
      --accent: #17a2a8;
      --bg1: #f7fbff;
      --bg2: #eaf3ff;
      --btn-bg: #fff;
      --btn-border: #d8eaf6;
      --btn-text: #153a57;
      --btn-hover-bg: linear-gradient(90deg, #17a2a8, #0b67c2);
      --shadow: 0 6px 18px rgba(8, 40, 80, 0.06);
      --radius: 20px;
    }

    html, body {
      margin: 0;
      padding: 0;
      font-family: "Yu Gothic", "Segoe UI", system-ui, Arial, sans-serif;
      background: linear-gradient(90deg, var(--bg1), var(--bg2));
      color: #19324a;
      /* 任意（なくても可） */
      height: 100vh;
    }

    .wrap {
      min-height:100%;
      display:flex;
      flex-direction:column;
    }

    main {
      flex:1;
      margin-left:120px;
      padding-top:80p;
      padding:20px;
      max-width:960px;
    }

    header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 10px 0;
    }

    .user-area {
      font-weight: 600;
      color: var(--primary);
      cursor: pointer;
      position: relative;
    }

    .user-menu {
      position: absolute;
      top: 100%;
      right: 0;
      background: #fff;
      border: 1px solid #ccc;
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      display: none;
      min-width: 160px;
      z-index: 1000;
      padding: 6px 0;
    }

    .user-menu button {
      display: block;
      width: 100%;
      padding: 10px 14px;
      background: none;
      border: none;
      text-align: left;
      font-size: 0.95rem;
      cursor: pointer;
      color: #333;
    }

    .user-menu button:hover {
      background: #f0f7ff;
    }

    .search-form {
      background: var(--btn-bg);
      border: 1px solid var(--btn-border);
      border-radius: var(--radius);
      padding: 20px;
      box-shadow: var(--shadow);
      margin-bottom: 30px;
    }

    .search-form label {
      display: block;
      margin-bottom: 12px;
      font-weight: 600;
    }

    .search-form input,
    .search-form select {
      padding: 8px;
      font-size: 1rem;
      width: 100%;
      max-width: 400px;
      border: 1px solid #ccc;
      border-radius: 6px;
    }

    .search-form button {
      margin-top: 12px;
      padding: 10px 20px;
      background: var(--btn-hover-bg);
      color: #fff;
      border: none;
      border-radius: var(--radius);
      cursor: pointer;
      font-weight: 700;
    }

    .store-list .store-item {
      background: #fff;
      border: 1px solid #ccc;
      border-radius: var(--radius);
      padding: 16px;
      margin-bottom: 16px;
      box-shadow: var(--shadow);
    }

    .store-item strong {
      font-size: 1.1rem;
      color: var(--primary);
    }

    .store-item a {
      display: inline-block;
      margin-top: 8px;
      color: var(--accent);
      text-decoration: none;
    }

    .store-item a:hover {
      text-decoration: underline;
    }

    table.shelf-info {
      width: 100%;
      margin: 30px auto;
      border-collapse: collapse;
      background: #fff;
      border-radius: var(--radius);
      box-shadow: var(--shadow);
      overflow: hidden;
    }

    table.shelf-info th,
    table.shelf-info td {
      border: 1px solid #ccc;
      padding: 12px 16px;
      text-align: left;
      font-size: 1rem;
    }

    table.shelf-info th {
      background-color: var(--bg2);
      color: var(--primary);
      width: 30%;
      font-weight: 600;
    }

    .status-open {
      color: green;
      font-weight: bold;
    }

    .status-closed {
      color: red;
      font-weight: bold;
    }

    .map-link {
      text-align: ;
      margin-top: 20px;
    }

    h2 {
      text-align: center;
      margin-top: 30px;
      font-family: inherit;
      color: var(--primary);
    }

    .note {
      display: block;
      font-size: 0.9em;
      color: #555;
      margin-top: 4px;
    }

    /* 戻るリンクを中央寄せ */
	  .map-link {
	    text-align: center !important;
	    margin-top: 24px !important;
	  }

	  .map-link a {
	    display: inline-block;
	    padding: 10px 20px;
	    background: var(--btn-hover-bg);
	    color: #fff;
	    border-radius: 12px;
	    text-decoration: none;
	    font-weight: 600;
	  }

	  .return-left {
		  bottom: 20px;
		  left: 20px;
		  z-index: 999;
		}

		.return-left a {
		  display: inline-block;
		  padding: 10px 14px;
		  background: var(--btn-hover-bg);
		  color: #fff;
		  border-radius: 10px;
		  text-decoration: none;
		  font-weight: 600;
		}

    /* ▼ スマホ最適化（幅640px以下） ▼ */
		@media (max-width: 640px) {

		  /* main を中央寄せに */
		  main {
		    margin-left: 0 !important;
		    padding: 16px !important;
		    max-width: 100% !important;
		  }

		  /* テーブルをカード型に変換 */
		  table.shelf-info {
		    width: 100%;
		    border: none;
		    box-shadow: none;
		  }

		  table.shelf-info tr {
		    display: block;
		    margin-bottom: 16px;
		    border: 1px solid #ccc;
		    border-radius: 12px;
		    padding: 12px;
		    background: #fff;
		  }

		  table.shelf-info th {
		    display: block;
		    width: 100% !important;
		    background: none;
		    border: none;
		    padding: 4px 0;
		    font-size: 0.9rem;
		    color: #666;
		  }

		  table.shelf-info td {
		    display: block;
		    width: 100%;
		    border: none;
		    padding: 6px 0 10px;
		    font-size: 1.1rem;
		    font-weight: 600;
		    color: #19324a;
		  }
		}
  </style>
</head>
<body>
  <c:if test="${not empty sessionScope.username}">
    <jsp:include page="_miniMenu.jsp" />
	</c:if>
  <div class="wrap">

  	<main>
	    <h2>店舗詳細情報</h2>
	    <table class="shelf-info">
	      <tbody>
	        <tr>
	          <th>店舗名</th>
	          <td>${store.storeName}</td>
	        </tr>
	        <tr>
	          <th>住所</th>
	          <td>${store.storeAddress}</td>
	        </tr>
	        <tr>
	          <th>電話番号</th>
	          <td>${store.phone}</td>
	        </tr>
	        <tr>
	          <th>営業時間</th>
	          <td>${store.openTime} ～ ${store.closeTime}
	          <span class="note">
			    ※一部サービスは店舗営業時間と異なる場合がございます。
			  </span>
	          </td>
	        </tr>
	        <tr>
	          <th>営業状況</th>
	          <td>
	            <c:choose>
	              <c:when test="${store.openNow}">
	                <span class="status-open">営業中</span>
	              </c:when>
	              <c:otherwise>
	                <span class="status-closed">営業時間外</span>
	              </c:otherwise>
	            </c:choose>
	          </td>
	        </tr>
	      </tbody>
	    </table>

			<div class="map-link">
			  <a href="${pageContext.request.contextPath}/showMap?storeId=${store.storeId}">
			    マップを表示する
			  </a>
			</div>

	    <div class="return-left">
			  <a href="searchStore">←</a>
			</div>
		</main>
  </div>
</body>
</html>