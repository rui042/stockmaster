<%@ page contentType="text/html; charset=UTF-8" language="java" %>
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
    html,body{height:100%;margin:0;font-family:"Yu Gothic","Segoe UI",system-ui,Arial,sans-serif;
      background:linear-gradient(90deg,var(--bg1),var(--bg2));color:#19324a;-webkit-font-smoothing:antialiased;}
    .wrap{min-height:100%;display:flex;flex-direction:column;}

    /* HEADER */
    header{display:flex;justify-content:space-between;align-items:center;padding:10px 16px;background:transparent;z-index:10;}
    .header-left{display:flex;align-items:center;min-width:0;}
    .header-right{display:flex;align-items:center;justify-content:flex-end;min-width:0;}
    .notice-btn{display:flex;align-items:center;gap:8px;background:transparent;border:0;cursor:pointer;padding:2px 6px;line-height:1;}
    .notice-badge{display:inline-flex;align-items:center;justify-content:center;width:26px;height:26px;border-radius:50%;
      background:linear-gradient(180deg,#17a2a8,#0f8a86);color:#fff;font-weight:700;box-shadow:0 3px 8px rgba(16,120,118,0.18);border:2px solid #fff;flex:0 0 26px;font-size:0.82rem;}
    .notice-label{font-size:0.9rem;color:var(--primary);white-space:nowrap;font-weight:600;}
    .user-area{font-weight:600;color:var(--primary);white-space:nowrap;font-size:0.95rem;}

    /* MAIN / GRID - 常に2列表示（狭くなれば1列に） */
    main{flex:1;display:flex;align-items:center;justify-content:center;padding:18px;}
    .menu-grid{
      width:880px;max-width:96%;
      display:grid;
      grid-template-columns: repeat(2, minmax(0, 1fr)); /* 常に2列 */
      grid-auto-rows: minmax(84px, auto);
      grid-gap:16px;
      align-items:center;
      justify-items:center;
    }

    /* メニューカード（アイコン左、ラベル1行） */
    .menu-card{
      display:flex;align-items:center;gap:12px;
      width:100%;max-width:360px;min-width:240px;
      background:var(--btn-bg);border:1px solid var(--btn-border);border-radius:var(--radius);
      padding:12px 16px;font-size:0.98rem;font-weight:700;color:var(--btn-text);
      box-shadow:var(--shadow);transition:transform .12s, background .16s, color .16s;cursor:pointer;
      text-align:left;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;
    }
    .menu-card:hover{transform:translateY(-4px);background:var(--btn-hover-bg);color:#fff;border-color:rgba(0,0,0,0.06);}

    .icon-circle{width:40px;height:40px;border-radius:50%;background:#f6fbfd;border:1px solid #cfe4ff;
      display:flex;align-items:center;justify-content:center;color:var(--accent);font-weight:800;font-size:1rem;flex:0 0 40px;}

    .menu-label{display:inline-block;vertical-align:middle;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;}

    /* レスポンシブ：非常に狭い画面は1列表示 */
    @media (max-width:640px){
      .menu-grid{grid-template-columns:1fr;grid-gap:12px;width:92%;}
      .menu-card{max-width:100%;min-width:0;padding:10px;font-size:0.95rem;justify-content:center;}
      .menu-card .icon-circle{margin-right:8px;}
      .notice-label{display:none;}
    }
  </style>
</head>
<body>
  <div class="wrap">
    <header>
      <div class="header-left">
        <form action="notifications" method="get" style="margin:0;">
          <button type="submit" class="notice-btn" aria-label="お知らせへ移動" title="お知らせ">
            <span class="notice-badge">${requestScope.noticeCount != null ? requestScope.noticeCount : "0"}</span>
            <span class="notice-label">お知らせ</span>
          </button>
        </form>
      </div>
      <div class="header-right">
        <div class="user-area">${requestScope.username != null ? requestScope.username : "ゲスト"} さん</div>
      </div>
    </header>

    <main>
      <div class="menu-grid">
        <form action="registerProduct" method="get" style="width:100%;margin:0;">
          <button class="menu-card" type="submit">
            <div class="icon-circle">＋</div>
            <div class="menu-label">商品登録</div>
          </button>
        </form>

        <form action="deleteProduct" method="get" style="width:100%;margin:0;">
          <button class="menu-card" type="submit">
            <div class="icon-circle">−</div>
            <div class="menu-label">商品削除</div>
          </button>
        </form>

        <form action="inventory" method="get" style="width:100%;margin:0;">
          <button class="menu-card" type="submit">
            <div class="icon-circle">📚</div>
            <div class="menu-label">棚卸一覧</div>
          </button>
        </form>

        <form action="searchProduct" method="get" style="width:100%;margin:0;">
          <button class="menu-card" type="submit">
            <div class="icon-circle">🔎</div>
            <div class="menu-label">商品検索</div>
          </button>
        </form>

        <form action="showMap" method="get" style="width:100%;margin:0;">
          <button class="menu-card" type="submit">
            <div class="icon-circle">🗺</div>
            <div class="menu-label">マップ表示</div>
          </button>
        </form>

        <form action="chat" method="get" style="width:100%;margin:0;">
          <button class="menu-card" type="submit">
            <div class="icon-circle">💬</div>
            <div class="menu-label">チャット相談</div>
          </button>
        </form>

        <form action="receiveStock" method="get" style="width:100%;margin:0;">
          <button class="menu-card" type="submit">
            <div class="icon-circle">📥</div>
            <div class="menu-label">入荷処理</div>
          </button>
        </form>

        <form action="shipStock" method="get" style="width:100%;margin:0;">
          <button class="menu-card" type="submit">
            <div class="icon-circle">📤</div>
            <div class="menu-label">出荷処理</div>
          </button>
        </form>
      </div>
    </main>
  </div>
</body>
</html>