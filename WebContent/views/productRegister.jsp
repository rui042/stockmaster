<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
  String username = (String) session.getAttribute("username");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="utf-8" />
  <title>商品登録</title>
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <style>
    :root{
      --primary:#0b67c2;
      --accent:#17a2a8;
      --bg1:#f7fbff;
      --bg2:#eaf3ff;
      --btn-bg:#fff;
      --btn-border:#d8eaf6;
      --btn-text:#153a57;
      --btn-hover-bg:linear-gradient(90deg,#17a2a8,#0b67c2);
      --shadow:0 6px 18px rgba(8,40,80,0.06);
      --radius:20px;
    }

    html,body{
      height:100%;
      margin:0;
      font-family:"Yu Gothic","Segoe UI",system-ui,Arial,sans-serif;
      background:linear-gradient(90deg,var(--bg1),var(--bg2));
      color:#19324a;
    }

    .wrap{min-height:100%;
      display:flex;
      flex-direction:column;
    }

    /* ==== ヘッダー部分 ==== */
    header{
      display:flex;
      justify-content:space-between;
      align-items:center;
      padding:10px 16px;
      background:#fff;
      box-shadow:0 2px 6px rgba(0,0,0,0.08);
      position:sticky; top:0; z-index:10;
    }

    .header-title{
      font-weight:700;
      font-size:1.2rem;
      color:var(--primary);
    }

    .user-area{
      font-weight:600;
      color:var(--primary);
      white-space:nowrap;
      font-size:0.95rem;
      position:relative;
      cursor:pointer;
    }

    .user-menu {
      display:none;
      position:absolute; right:0; top:120%;
      background:#fff;
      border:1px solid #ddd;
      border-radius:10px;
      box-shadow:0 4px 16px rgba(0,0,0,0.1);
      min-width:160px;
      overflow:hidden;
      z-index:100;
    }
    .user-menu button {
      width:100%;
      padding:10px 16px;
      border:none;
      background:none;
      text-align:left;
      font-size:0.95rem;
      cursor:pointer;
    }
    .user-menu button:hover { background:#f0f7ff; }

    main{
      flex:1;
      display:flex;
      align-items:center;
      justify-content:center;
      padding:18px;
      margin-left:140px;
    }

    .form-card{
      width:100%;
      max-width:420px;
      background:var(--btn-bg);
      border:1px solid var(--btn-border);
      border-radius:var(--radius);
      box-shadow:var(--shadow);
      padding:24px;
    }

    h2{
      margin-top:0;
      margin-bottom:16px;
      color:var(--primary);
    }

    label{
      display:block;
      margin-top:12px;
      font-weight:600;
    }

    input[type=text], input[type=number], select{
      width:100%;
      padding:10px;
      margin-top:6px;
      border:1px solid #ccc;
      border-radius:8px;
      font-size:0.95rem;
    }

    button{
      margin-top:20px;
      padding:12px 20px;
      background:var(--btn-hover-bg);
      color:#fff;
      border:none;
      border-radius:8px;
      cursor:pointer;
      font-size:1rem;
      font-weight:700;
      width:100%;
    }
    button:hover{opacity:0.9;}

    #toast {
      position: fixed;
      bottom: 20px;
      left: 50%;
      transform: translateX(-50%);
      background: #333;
      color: #fff;
      padding: 12px 24px;
      border-radius: 4px;
      opacity: 0;
      transition: opacity 0.3s ease;
      z-index: 9999;
    }
    #toast.show { opacity: 1; }
  </style>
</head>
<body>
  <!-- 左側のミニメニュー -->
  <jsp:include page="_miniMenu.jsp" />

  <div class="wrap">
    <!-- ヘッダー -->
    <header>
      <div class="header-title">商品登録</div>
      <div class="user-area" onclick="toggleUserMenu()">
        <%= username != null ? username + " さん" : "ゲストさん" %>
        <div id="userMenu" class="user-menu">
          <c:choose>
            <c:when test="${username == null}">
              <form action="login" method="get"><button type="submit">ログイン</button></form>
              <form action="register" method="get"><button type="submit">新規登録</button></form>
            </c:when>
            <c:otherwise>
              <form action="logout" method="post"><button type="submit">ログアウト</button></form>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
    </header>

    <main>
      <div class="form-card">
        <h2>商品登録フォーム</h2>

        <form id="productForm" action="productRegister" method="post">
          <label>商品番号(バーコード入力可)
            <input type="text" name="itemId" pattern="\d{13}" maxlength="13" required autofocus>
          </label>

          <label>商品名
            <input type="text" name="name" required>
          </label>

          <label>分類
            <input type="text" name="category" required>
          </label>

          <label>棚番号（例：A-01）
            <input type="text" name="shelf" required>
          </label>

          <label>価格
            <input type="number" name="price" min="0" required>
          </label>

          <label>入荷数（在庫数）
            <input type="number" name="stockNow" min="1" required>
          </label>

          <label>ストック数
            <input type="number" name="stockMin" min="0" required>
          </label>

          <label>店舗選択
            <select name="storeId" required>
              <option value="">--選択--</option>
              <c:forEach var="store" items="${storeList}">
                <option value="${store.storeId}">${store.storeName}</option>
              </c:forEach>
            </select>
          </label>

          <button type="submit">登録する</button>
        </form>
      </div>
    </main>

    <div id="toast"></div>
  </div>

  <script>
    function toggleUserMenu() {
      const menu = document.getElementById("userMenu");
      menu.style.display = (menu.style.display === "block") ? "none" : "block";
    }
    document.addEventListener("click", e => {
      if (!e.target.closest(".user-area")) {
        document.getElementById("userMenu").style.display = "none";
      }
    });

    document.addEventListener("DOMContentLoaded", () => {
      const form = document.getElementById("productForm");
      const itemIdInput = form.querySelector('input[name="itemId"]');
      const stockNowInput = form.querySelector('input[name="stockNow"]');
