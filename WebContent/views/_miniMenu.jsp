<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <style>
    body {
      margin: 0;
      font-family: "Yu Gothic", "Segoe UI", system-ui, Arial, sans-serif;
    }

    /* サイドメニュー */
    .side-menu {
      position: fixed;
      left: 0;
      top: 0;
      width: 120px;
      height: 100vh;
      background: #0b67c2;
      color: #fff;
      display: flex;
      flex-direction: column;
      align-items: center;
      padding-top: 20px;
      box-shadow: 2px 0 8px rgba(0,0,0,0.1);
      z-index: 999;
    }

    .side-menu .logo {
      font-weight: 700;
      font-size: 1.4rem;
      margin-bottom: 24px;
      letter-spacing: 1px;
    }

    .side-menu a {
      color: #fff;
      text-decoration: none;
      margin: 8px 0;
      padding: 8px 12px;
      border-radius: 8px;
      width: 80%;
      text-align: center;
      transition: background 0.3s;
      font-weight: 600;
    }

    .side-menu a:hover {
      background: rgba(255,255,255,0.2);
    }

    .side-menu .bottom {
      margin-top: auto;
      font-size: 0.8rem;
      color: #cdddf5;
      margin-bottom: 16px;
    }

    /* ヘッダー */
    header.user-header {
      position: fixed;
      top: 0;
      right: 0;
      height: 60px;
      width: calc(100% - 120px);
      display: flex;
      align-items: center;
      justify-content: flex-end;
      padding: 0 24px;
      background: #fff;
      border-bottom: 1px solid #e5e9f2;
      z-index: 998;
    }

    .user-info {
      position: relative;
      cursor: pointer;
      color: #0b67c2;
      font-weight: 600;
      user-select: none;
    }

    .user-info:hover {
      opacity: 0.8;
    }

    /* ドロップダウンメニュー */
    .logout-dropdown {
      display: none;
      position: absolute;
      top: 36px;
      right: 0;
      background: #fff;
      border: 1px solid #e0e6ef;
      border-radius: 6px;
      box-shadow: 0 4px 8px rgba(0,0,0,0.1);
      padding: 8px 0;
      min-width: 120px;
      text-align: right;
      z-index: 1000;
    }

    .logout-dropdown a {
      display: block;
      color: #e53935;
      text-decoration: none;
      padding: 8px 16px;
      font-weight: 600;
    }

    .logout-dropdown a:hover {
      background: #ffe6e6;
    }

    .user-info.active .logout-dropdown {
      display: block;
    }

    .logout-link-btn {
	  display: block;
	  width: 100%;
	  text-align: right;
	  background: none;
	  border: none;
	  color: #e53935;
	  text-decoration: none;
	  padding: 8px 16px;
	  font-weight: 600;
	  font-family: inherit;
	  font-size: inherit;
	  cursor: pointer;
	}

	.logout-link-btn:hover {
	  background: #ffe6e6;
	}

    /* メインコンテンツ用 */
    main {
      margin-left: 120px;
      padding-top: 80px;
    }
  </style>
</head>
<body>
  <!-- サイドメニュー -->
  <nav class="side-menu" role="navigation" aria-label="サイドメニュー">
    <div class="logo">SM</div>

    <a href="${pageContext.request.contextPath}/menu" title="メニュー">Menu</a>

    <c:if test="${sessionScope.isStaff}">
      <a href="${pageContext.request.contextPath}/productRegister" title="商品登録">Add</a>
      <a href="${pageContext.request.contextPath}/searchProduct" title="商品検索">Search</a>
      <a href="${pageContext.request.contextPath}/receiveStock" title="入荷処理">Recv</a>
      <a href="${pageContext.request.contextPath}/shipStock" title="出荷処理">Ship</a>
    </c:if>

    <a href="${pageContext.request.contextPath}/selectStore" title="地図">Map</a>
    <a href="${pageContext.request.contextPath}/chat" title="チャット">Chat</a>
    <a href="${pageContext.request.contextPath}/searchStore" title="店舗検索">Store</a>

    <div class="bottom">v1.2</div>
  </nav>

  <!-- 右上ユーザー情報 -->
  <header class="user-header">
    <div class="user-info">
      <c:choose>
        <c:when test="${not empty sessionScope.username}">
          ${sessionScope.username} さん ▼
          <div class="logout-dropdown">
            <form action="${pageContext.request.contextPath}/logout" method="post">
		      <button type="submit" class="logout-link-btn">ログアウト</button>
		    </form>
          </div>
        </c:when>
        <c:otherwise>
          ゲストさん
          <!-- <div class="logout-dropdown">
            <form action="${pageContext.request.contextPath}/logout" method="post">
		      <button type="submit" class="logout-link-btn">ログイン</button>
		    </form>
		    <form action="${pageContext.request.contextPath}/register" method="post">
		      <button type="submit" class="logout-link-btn">新規登録</button>
		    </form>
          </div>  -->
        </c:otherwise>
      </c:choose>
    </div>
  </header>

  <script>
    // ユーザー名クリックでログアウトメニューを表示・非表示
    document.addEventListener('DOMContentLoaded', () => {
      const userInfo = document.querySelector('.user-info');
      if (userInfo) {
        userInfo.addEventListener('click', (e) => {
          e.stopPropagation();
          userInfo.classList.toggle('active');
        });

        document.addEventListener('click', () => {
          userInfo.classList.remove('active');
        });
      }
    });
  </script>
</body>
</html>
