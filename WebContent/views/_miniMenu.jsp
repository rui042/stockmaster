<%@ page contentType="text/html; charset=UTF-8" %>
<nav class="mini-menu" role="navigation" aria-label="ミニメニュー" style="
  position:fixed;left:16px;top:16px;width:96px;display:flex;flex-direction:column;align-items:center;
  background:linear-gradient(180deg,#fff,#f7fbff);border-radius:12px;padding:10px;border:1px solid #e6eef6;
  box-shadow:2px 6px 18px rgba(0,0,0,0.06);z-index:9999;font-family:inherit;">

  <div style="width:52px;height:52px;border-radius:10px;background:#1572a1;color:#fff;
              display:flex;align-items:center;justify-content:center;font-weight:700;margin-bottom:10px">
    SM
  </div>

  <a href="${pageContext.request.contextPath}/menu" title="メニュー"
     style="display:block;width:64px;height:40px;border-radius:10px;background:transparent;border:0;
            margin:6px 0;text-align:center;line-height:40px;color:#1572a1;text-decoration:none;font-weight:700">
    Menu
  </a>

  <a href="${pageContext.request.contextPath}/showMap" title="地図"
     style="display:block;width:64px;height:40px;border-radius:10px;background:transparent;border:0;
            margin:6px 0;text-align:center;line-height:40px;color:#1572a1;text-decoration:none;font-weight:700">
    Map
  </a>

  <a href="${pageContext.request.contextPath}/chat" title="チャット"
     style="display:block;width:64px;height:40px;border-radius:10px;background:transparent;border:0;
            margin:6px 0;text-align:center;line-height:40px;color:#1572a1;text-decoration:none;font-weight:700">
    Chat
  </a>



  <!-- 修正ポイント: /addproduct → /productRegister -->
  <a href="${pageContext.request.contextPath}/productRegister" title="商品登録"
     style="display:block;width:64px;height:40px;border-radius:10px;background:transparent;border:0;
            margin:6px 0;text-align:center;line-height:40px;color:#1572a1;text-decoration:none;font-weight:700">
    Add
  </a>

  <a href="${pageContext.request.contextPath}/searchProduct" title="商品検索"
     style="display:block;width:64px;height:40px;border-radius:10px;background:transparent;border:0;
            margin:6px 0;text-align:center;line-height:40px;color:#1572a1;text-decoration:none;font-weight:700">
    Search
  </a>

  <a href="${pageContext.request.contextPath}/receiveStock" title="入荷処理"
     style="display:block;width:64px;height:40px;border-radius:10px;background:transparent;border:0;
            margin:6px 0;text-align:center;line-height:40px;color:#1572a1;text-decoration:none;font-weight:700">
    Recv
  </a>

  <a href="${pageContext.request.contextPath}/shipStock" title="出荷処理"
     style="display:block;width:64px;height:40px;border-radius:10px;background:transparent;border:0;
            margin:6px 0;text-align:center;line-height:40px;color:#1572a1;text-decoration:none;font-weight:700">
    Ship
  </a>

  <div style="flex:1"></div>
  <div style="font-size:.7rem;color:#6b7c8a;margin-top:8px">v1.0</div>

  <style>
    @media (max-width:640px){
      .mini-menu{
        position:fixed;left:8px;top:8px;width:auto;padding:6px;
        display:flex;flex-direction:row;gap:6px;border-radius:10px;
      }
      .mini-menu a{
        width:auto;height:36px;line-height:36px;padding:0 8px;font-size:0.85rem;
      }
    }
  </style>
</nav>