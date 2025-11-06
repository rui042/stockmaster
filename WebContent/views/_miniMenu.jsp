<%@ page contentType="text/html; charset=UTF-8" %>
<style>
  .mini-menu {
    position: fixed;
    left: 16px;
    top: 16px;
    width: 96px;
    display: flex;
    flex-direction: column;
    align-items: center;
    background: linear-gradient(180deg, #fff, #f7fbff);
    border-radius: 12px;
    padding: 10px;
    border: 1px solid #e6eef6;
    box-shadow: 2px 6px 18px rgba(0, 0, 0, 0.06);
    z-index: 9999;
    font-family: inherit;
  }

  .mini-menu .logo {
    width: 52px;
    height: 52px;
    border-radius: 10px;
    background: #1572a1;
    color: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 700;
    margin-bottom: 10px;
  }

  .mini-menu a {
    display: block;
    width: 64px;
    height: 40px;
    border-radius: 10px;
    background: transparent;
    border: 0;
    margin: 6px 0;
    text-align: center;
    line-height: 40px;
    color: #1572a1;
    text-decoration: none;
    font-weight: 700;
  }

  .mini-menu .version {
    font-size: 0.7rem;
    color: #6b7c8a;
    margin-top: 8px;
  }

  @media (max-width: 640px) {
    .mini-menu {
      left: 8px;
      top: 8px;
      width: auto;
      padding: 6px;
      flex-direction: row;
      gap: 6px;
      border-radius: 10px;
    }

    .mini-menu a {
      width: auto;
      height: 36px;
      line-height: 36px;
      padding: 0 8px;
      font-size: 0.85rem;
    }
  }
</style>

<nav class="mini-menu" role="navigation" aria-label="ミニメニュー">
  <div class="logo">SM</div>

  <a href="${pageContext.request.contextPath}/menu"
  	title="メニュー">Menu</a>

    <a href="${pageContext.request.contextPath}/productRegister"
  	  title="商品登録">Add</a>

    <a href="${pageContext.request.contextPath}/searchProduct"
  	  title="商品検索">Search</a>

    <a href="${pageContext.request.contextPath}/receiveStock"
  	  title="入荷処理">Recv</a>

    <a href="${pageContext.request.contextPath}/shipStock"
  	  title="出荷処理">Ship</a>

    <a href="${pageContext.request.contextPath}/selectStore"
  	  title="地図">Map</a>

    <a href="${pageContext.request.contextPath}/chat"
  	  title="チャット">Chat</a>

  <a href="${pageContext.request.contextPath}/searchStore"
  	title="店舗検索">Store</a>

  <div style="flex:1"></div>
  <div class="version">v1.0</div>
</nav>