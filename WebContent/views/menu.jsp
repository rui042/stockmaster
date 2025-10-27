<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
  String username = (String) session.getAttribute("username");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="utf-8" />
  <title>メニュー（2列・ラベル1行）</title>
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
      --shadow: 0 6px 18px rgba(8,40,80,0.06);
      --radius:20px;
    }
    html,body{
      height:100%;margin:0;
      font-family:"Yu Gothic","Segoe UI",system-ui,Arial,sans-serif;
      background:linear-gradient(90deg,var(--bg1),var(--bg2));
      color:#19324a;
    }
    .wrap{min-height:100%;display:flex;flex-direction:column;}

    .app-title{
      text-align:center;
      font-size:2rem;
      font-weight:700;
      color:var(--primary);
      letter-spacing:0.15em;
      margin:20px 0 10px;
    }

    header{
      display:flex;justify-content:space-between;align-items:center;
      padding:10px 16px;background:transparent;z-index:10;
    }
    .header-left{display:flex;align-items:center;}
    .header-right{display:flex;align-items:center;position:relative;}
    .notice-btn{display:flex;align-items:center;gap:8px;background:transparent;border:0;cursor:pointer;padding:2px 6px;}
    .notice-badge{display:inline-flex;align-items:center;justify-content:center;width:26px;height:26px;border-radius:50%;
      background:linear-gradient(180deg,#17a2a8,#0f8a86);color:#fff;font-weight:700;
      box-shadow:0 3px 8px rgba(16,120,118,0.18);border:2px solid #fff;font-size:0.82rem;}
    .notice-label{font-size:0.9rem;color:var(--primary);font-weight:600;}
    .user-area{font-weight:600;color:var(--primary);cursor:pointer;position:relative;}

    /* ドロップダウン */
    .user-menu{
      position:absolute;
      top:100%; right:0;
      background:#fff;
      border:1px solid #ccc;
      border-radius:8px;
      box-shadow:0 4px 12px rgba(0,0,0,0.1);
      display:none;
      min-width:160px;
      z-index:1000;
      padding:6px 0;
    }
    .user-menu form{margin:0;}
    .user-menu button{
      display:block;
      width:100%;
      padding:10px 14px;
      background:none;
      border:none;
      text-align:left;
      font-size:0.95rem;
      cursor:pointer;
      color:#333;
    }
    .user-menu button:hover{background:#f0f7ff;}

    main{flex:1;display:flex;align-items:center;justify-content:center;padding:18px;}
    .menu-grid{
      width:880px;max-width:96%;
      display:grid;
      grid-template-columns: repeat(2, minmax(0, 1fr));
      grid-gap:16px;
      align-items:center;
      justify-items:center;
    }
    .menu-card{
      display:flex;align-items:center;gap:12px;
      width:100%;max-width:360px;min-width:240px;
      background:var(--btn-bg);border:1px solid var(--btn-border);border-radius:var(--radius);
      padding:12px 16px;font-size:0.98rem;font-weight:700;color:var(--btn-text);
      box-shadow:var(--shadow);transition:transform .12s, background .16s, color .16s;cursor:pointer;
    }
    .menu-card:hover{transform:translateY(-4px);background:var(--btn-hover-bg);color:#fff;}
    .icon-circle{width:40px;height:40px;border-radius:50%;background:#f6fbfd;border:1px solid #cfe4ff;
      display:flex;align-items:center;justify-content:center;color:var(--accent);font-weight:800;font-size:1rem;}
    @media (max-width:640px){
      .menu-grid{grid-template-columns:1fr;width:92%;}
      .menu-card{max-width:100%;padding:10px;font-size:0.95rem;justify-content:center;}
      .notice-label{display:none;}
    }
  </style>
</head>
<body>
  <div class="wrap">
    <div class="app-title">すとっくますたー</div>

    <header>
      <div class="header-left">
        <button type="button" class="notice-btn">
          <span class="notice-badge">${requestScope.noticeCount != null ? requestScope.noticeCount : "2"}</span>
          <span class="notice-label">お知らせ</span>
        </button>
      </div>
      <div class="header-right">
        <div class="user-area" onclick="toggleUserMenu()">
          <%= username != null ? username + " さん" : "ゲストさん" %>
          <div id="userMenu" class="user-menu">
            <c:choose>
              <c:when test="${username == null}">
                <!-- ログイン画面へ遷移（直接JSPへ） -->
                <form action="views/login.jsp" method="get">
                  <button type="submit">ログイン</button>
                </form>
                <!-- 新規登録画面へ遷移 -->
                <form action="views/register.jsp" method="get">
                  <button type="submit">新規登録</button>
                </form>
              </c:when>
              <c:otherwise>
                <!-- ログアウト処理（POST） -->
                <form action="logout" method="post">
                  <button type="submit">ログアウト</button>
                </form>
              </c:otherwise>
            </c:choose>
          </div>
        </div>
      </div>
    </header>

    <main>
      <div class="menu-grid">
        <form action="productRegister" method="get"><button class="menu-card" type="submit"><div class="icon-circle">＋</div><div>商品登録</div></button></form>
        <form action="searchProduct" method="get"><button class="menu-card" type="submit"><div class="icon-circle">🔎</div><div>商品検索</div></button></form>
        <form action="showMap" method="get"><button class="menu-card" type="submit"><div class="icon-circle">🗺</div><div>マップ表示</div></button></form>
        <form action="chat" method="get"><button class="menu-card" type="submit"><div class="icon-circle">💬</div><div>チャット相談</div></button></form>
        <form action="receiveStock" method="get"><button class="menu-card" type="submit"><div class="icon-circle">📥</div><div>入荷処理</div></button></form>
        <form action="shipStock" method="get"><button class="menu-card" type="submit"><div class="icon-circle">📤</div><div>出荷処理</div></button></form>
      </div>
    </main>
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