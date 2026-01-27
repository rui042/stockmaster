<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
  String username = (String) session.getAttribute("username");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="utf-8" />
  <title>メニュー | すとっくますたー</title>
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <style>
    :root {
      --primary:#0b67c2;
      --accent:#17a2a8;
      --bg:#f8fafc;
      --card-bg:#fff;
      --radius:16px;
      --shadow:0 4px 12px rgba(0,0,0,0.08);
    }

    body {
      margin:0;
      font-family:"Segoe UI","Yu Gothic",system-ui,sans-serif;
      background:var(--bg);
      color:#223;
    }

    /* ===== ヘッダー全体 ===== */
    header {
      display:grid;
      grid-template-columns:1fr auto 1fr; /* 左中央右 */
      align-items:center;
      padding:14px 24px;
      background:#fff;
      box-shadow:0 2px 8px rgba(0,0,0,0.05);
      position:sticky; top:0; z-index:10;
    }

    /* 左の「お知らせ」ボタン */
    .notice-area {
      justify-self:start;
      display:flex;
      align-items:center;
    }

    .notice-btn {
      background:transparent;
      border:none;
      display:flex;
      align-items:center;
      gap:8px;
      cursor:pointer;
      font-size:1rem;
    }
    .notice-badge {
      background:linear-gradient(180deg,#17a2a8,#0f8a86);
      color:#fff;
      font-weight:700;
      border-radius:50%;
      width:26px; height:26px;
      display:flex; align-items:center; justify-content:center;
    }
    .notice-label {
      font-weight:600;
      color:var(--primary);
    }

    /* 中央タイトル */
    .app-title {
      justify-self:center;
      font-size:1.6rem;
      font-weight:700;
      color:var(--primary);
      letter-spacing:0.05em;
      text-align:center;
    }

    /* 右のユーザー名とメニュー */
    .user-area {
      justify-self:end;
      position:relative;
      font-weight:600;
      color:var(--primary);
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
    .user-menu button:hover {
    	background:#f0f7ff;
    }

    /* ===== 未ログインメッセージ ===== */
		.login-warning {
		  width: 100%;
		  display: flex;
		  flex-direction: column;
		  align-items: center;
		  justify-content: center;
		  padding: 60px 20px;
		  text-align: center;
		  font-size: 1.2rem;
		  color: #333;
		}

		.login-warning p {
		  margin-bottom: 24px;
		  font-weight: 600;
		  white-space: nowrap;
		}

		.login-warning button {
		  padding: 12px 28px;
		  font-size: 1rem;
		  background: var(--primary);
		  color: #fff;
		  border: none;
		  border-radius: 8px;
		  cursor: pointer;
		  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
		  transition: background 0.2s;
		}

		.login-warning button:hover {
		  background: #084f96;
		}

    /* ===== メイン部分 ===== */
    main {
      padding:50px 20px 70px;
      display:flex;
      justify-content:center;
    }

    .menu-grid {
      display:grid;
      grid-template-columns:repeat(auto-fit,minmax(220px,220px));
      gap:24px;
      justify-content:center;
      width:100%;
      max-width:950px;
    }

    .menu-card {
      background:var(--card-bg);
      border-radius:var(--radius);
      box-shadow:var(--shadow);
      display:flex;
      flex-direction:column;
      align-items:center;
      justify-content:center;
      text-align:center;
      padding:30px 20px;
      transition:transform 0.2s, box-shadow 0.2s;
      cursor:pointer;
      border:1px solid #e4ebf5;
      width:220px;
      height:160px;
    }
    .menu-card:hover {
      transform:translateY(-6px);
      box-shadow:0 8px 20px rgba(0,0,0,0.12);
    }

    .icon {
      font-size:2.2rem;
      margin-bottom:12px;
    }
    .menu-card div:last-child {
      font-weight:600;
      color:#333;
      font-size:1rem;
    }

    /* ===== モーダル ===== */
    .modal-overlay {
      position: fixed;
      top: 0; left: 0;
      width: 100%; height: 100%;
      background: rgba(0,0,0,0.4);
      display: none;
      justify-content: center;
      align-items: center;
      z-index: 9999;
    }
    .modal-content {
      background: #fff;
      padding: 24px;
      border-radius: 12px;
      box-shadow: 0 8px 24px rgba(0,0,0,0.2);
      max-width: 480px;
      width: 90%;
    }
    .modal-content h2 { margin-top: 0; color: var(--primary); }
    .modal-content ul { padding-left: 20px; }
    .modal-content li { margin-bottom: 8px; }
    .modal-close { margin-top: 20px; text-align: right; }
    .modal-close button {
      padding: 8px 16px;
      border: 1px solid #ddd;
      border-radius: 8px;
      cursor:pointer;
    }
  </style>
</head>
<body>
  <header>
    <!-- 左 -->
    <div class="notice-area">

    </div>

    <!-- 中央 -->
    <div class="app-title">すとっくますたー</div>

    <!-- 右 -->
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
    <div class="menu-grid">
    <!-- 	未ログイン -->
	    <c:if test="${username == null}">
			  <div class="login-warning">
			    <p>ログイン状態が確認できません。ログインをしてください。</p>

			    <form action="login" method="get">
			      <button type="submit">ログイン画面へ</button>
			    </form>
			  </div>

			  <!-- 未ログイン時はメニューを表示しない -->
			  <c:remove var="sessionScope.isStaff" />
			  <c:remove var="sessionScope.isAdmin" />
			</c:if>

      <!-- スタッフ専用 -->
      <c:if test="${sessionScope.isStaff}">
      <form action="showMap" method="get">
        <button class="menu-card" type="submit">
          <div class="icon">🗺️</div><div>マップ表示</div>
        </button>
      </form>

      <form action="chat" method="get">
        <button class="menu-card" type="submit">
          <div class="icon">💬</div><div>チャット相談</div>
        </button>
      </form>

      <form action="searchStore" method="get">
        <button class="menu-card" type="submit">
          <div class="icon">🏬</div><div>店舗検索</div>
        </button>
      </form>

        <form action="productRegister" method="get">
          <button class="menu-card" type="submit">
            <div class="icon">🛒</div><div>商品登録</div>
          </button>
        </form>

        <form action="searchProduct" method="get">
          <button class="menu-card" type="submit">
            <div class="icon">🔍</div><div>商品検索</div>
          </button>
        </form>

        <form action="receiveStock" method="get">
          <button class="menu-card" type="submit">
            <div class="icon">📦</div><div>入荷処理</div>
          </button>
        </form>

        <form action="shipStock" method="get">
          <button class="menu-card" type="submit">
            <div class="icon">🚚</div><div>出荷処理</div>
          </button>
        </form>
      </c:if>

      <!-- 管理者専用 -->
      <c:if test="${sessionScope.isAdmin}">
        <form action="register" method="get">
          <button class="menu-card" type="submit">
            <div class="icon">🫂</div><div>新規登録</div>
          </button>
        </form>
      </c:if>
    </div>
  </main>

  <!-- モーダル -->
  <div id="modal" class="modal-overlay">
    <div class="modal-content">
      <h2>📢 お知らせ</h2>
      <ul>
        <li>2025年10月27日：在庫管理画面のUIを一部改善しました。</li>
        <li>2025年10月25日：新しいスタッフ権限が追加されました。</li>
        <li>2025年10月20日：メンテナンスのお知らせ（10月30日 22:00〜）</li>
      </ul>
      <div class="modal-close">
        <button onclick="closeModal()">閉じる</button>
      </div>
    </div>
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
    function openModal() { document.getElementById("modal").style.display = "flex"; }
    function closeModal() { document.getElementById("modal").style.display = "none"; }
  </script>
</body>
</html>
