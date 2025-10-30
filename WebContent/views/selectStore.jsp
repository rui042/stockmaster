<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
  String username = (String) session.getAttribute("username");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>店舗選択</title>
<meta name="viewport" content="width=device-width,initial-scale=1"/>
<style>
    :root {
      --primary:#0b67c2;
      --accent:#17a2a8;
      --bg1:#f7fbff;
      --bg2:#eaf3ff;
      --btn-bg:#fff;
      --btn-border:#d8eaf6;
      --btn-text:#153a57;
      --btn-hover-bg:linear-gradient(90deg,#17a2a8,#0b67c2);
      --shadow: 0 6px 18px rgba(8,40,80,0.06);
      --radius:20px;
    }
    html,body {
      margin:0; padding:0;
      font-family:"Yu Gothic","Segoe UI",system-ui,Arial,sans-serif;
      background:linear-gradient(90deg,var(--bg1),var(--bg2));
      color:#19324a;
    }
    .wrap { padding:20px; max-width:960px; margin:auto; }

    .app-title {
      text-align:center;
      font-size:2rem;
      font-weight:700;
      color:var(--primary);
      letter-spacing:0.15em;
      margin:20px 0;
    }

    header {
      display:flex; justify-content:space-between; align-items:center;
      padding:10px 0;
    }
    .user-area {
      font-weight:600; color:var(--primary); cursor:pointer; position:relative;
    }
    .user-menu {
      position:absolute; top:100%; right:0;
      background:#fff; border:1px solid #ccc; border-radius:8px;
      box-shadow:0 4px 12px rgba(0,0,0,0.1); display:none;
      min-width:160px; z-index:1000; padding:6px 0;
    }
    .user-menu button {
      display:block; width:100%; padding:10px 14px;
      background:none; border:none; text-align:left;
      font-size:0.95rem; cursor:pointer; color:#333;
    }
    .user-menu button:hover { background:#f0f7ff; }

    .search-form {
      background:var(--btn-bg); border:1px solid var(--btn-border);
      border-radius:var(--radius); padding:20px; box-shadow:var(--shadow);
      margin-bottom:30px;
    }
    .search-form label { display:block; margin-bottom:12px; font-weight:600; }
    .search-form input, .search-form select {
      padding:8px; font-size:1rem; width:100%; max-width:400px;
      border:1px solid #ccc; border-radius:6px;
    }
    .search-form button {
      margin-top:12px; padding:10px 20px;
      background:var(--btn-hover-bg); color:#fff;
      border:none; border-radius:var(--radius); cursor:pointer;
      font-weight:700;
    }

    .store-list .store-item {
      background:#fff; border:1px solid #ccc; border-radius:var(--radius);
      padding:16px; margin-bottom:16px; box-shadow:var(--shadow);
    }
    .store-item strong { font-size:1.1rem; color:var(--primary); }
    .store-item a {
      display:inline-block; margin-top:8px;
      color:var(--accent); text-decoration:none;
    }
    .store-item a:hover { text-decoration:underline; }


    .modal-content {
	  background: #fff;
	  padding: 24px;
	  border-radius: var(--radius);
	  box-shadow: var(--shadow);
	  max-width: 600px;
	  margin: 40px auto;
	}

    ul {
	  list-style: none;
	  padding: 0;
	}

	li {
	  margin-bottom: 12px;
	}

	.modal-close button {
	  padding: 10px 20px;
	  background: var(--btn-hover-bg);
	  color: #fff;
	  border: none;
	  border-radius: var(--radius);
	  cursor: pointer;
	  font-weight: 700;
	}

  </style>
</head>
<body>
<div class="modal-content">
  <h2>店舗を選択してください</h2>
  <form action="showMap" method="get">
    <ul>
      <c:forEach var="store" items="${storeList}">
        <li>
          <label>
            <input type="radio" name="storeId" value="${store.storeId}" required>
            ${store.storeName}
          </label>
        </li>
      </c:forEach>
    </ul>
    <div class="modal-close">
      <button type="submit">表示する</button>
    </div>
  </form>
</div>

</body>
</html>