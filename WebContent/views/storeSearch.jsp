<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
  String username = (String) session.getAttribute("username");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8" />
  <title>店舗検索</title>
  <meta name="viewport" content="width=device-width,initial-scale=1" />
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
  </style>
</head>
<body>
  <div class="wrap">
    <div class="app-title">店舗検索</div>

    <header>
      <div></div>
      <div class="user-area" onclick="toggleUserMenu()">
        <%= username != null ? username + " さん" : "ゲストさん" %>
        <div id="userMenu" class="user-menu">
          <c:choose>
            <c:when test="${username == null}">
              <form action="<%= request.getContextPath() %>/views/login.jsp" method="get"><button type="submit">ログイン</button></form>
              <form action="<%= request.getContextPath() %>/views/register.jsp" method="get"><button type="submit">新規登録</button></form>
            </c:when>
            <c:otherwise>
              <form action="<%= request.getContextPath() %>/logout" method="post"><button type="submit">ログアウト</button></form>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
    </header>

    <form action="searchStore" method="get" class="search-form">
      <label>店舗名：<input type="text" name="name" /></label>
      <label>地域：<input type="text" name="area" /></label>
      <label>カテゴリ：
        <select name="category">
          <option value="">--選択--</option>
          <option value="飲食">飲食</option>
          <option value="アパレル">アパレル</option>
          <option value="サービス">サービス</option>
        </select>
      </label>
      <label><input type="checkbox" name="openOnly" value="true" /> 営業中のみ</label>
      <button type="submit">検索する</button>
    </form>

    <div class="store-list">
      <c:if test="${not empty storeList}">
        <c:forEach var="store" items="${storeList}">
          <div class="store-item">
            <strong>${store.name}</strong><br/>
            住所：${store.address}<br/>
            電話：${store.phone}<br/>
            <a href="storeDetail?id=${store.id}">詳細を見る</a>
          </div>
        </c:forEach>
      </c:if>
    </div>
  </div>

  <script>
    function toggleUserMenu() {
      const menu = document.getElementById("userMenu");
      menu.style.display = (menu.style.display === "block") ? "none" : "block";
    }

    document.addEventListener("click", function(e) {
      const menu = document.getElementById("userMenu");
      if (!e.target.closest(".user-area")) {
        menu.style.display = "none";
      }
    });
  </script>
</body>
</html>