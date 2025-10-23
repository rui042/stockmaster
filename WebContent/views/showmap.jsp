<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="utf-8"/>
  <title>フロア図</title>
  <meta name="viewport" content="width=device-width,initial-scale=1"/>
  <style>
    :root{--muted:#6b7c8a;--accent:#1572a1}
    body{
      margin:0;
      font-family:"Yu Gothic","Segoe UI",system-ui,-apple-system,sans-serif;
      background:transparent;
      color:#213547
    }
    .page-wrap{
      max-width:1100px;
      margin:40px auto;
      padding:16px;
      margin-left:140px;
      position:relative;
    }
    @media (max-width:640px){
      .page-wrap{margin-left:16px;margin-top:88px}
    }

    h2{margin:0 0 8px}
    .note{color:var(--muted);margin-bottom:10px}
    .img-box{
      border-radius:8px;
      border:1px solid #e6eef6;
      overflow:hidden;
      background:#fff;
      text-align:center;
    }
    .floor{
      display:block;
      max-width:80%;
      height:auto;
      margin:0 auto;
    }

    /* 商品情報テーブル */
    .shelf-info{
      margin-top:24px;
      border-collapse:collapse;
      width:100%;
      background:#fff;
      border-radius:8px;
      overflow:hidden;
      box-shadow:0 1px 3px rgba(0,0,0,0.08);
    }
    .shelf-info th, .shelf-info td{
      padding:12px 16px;
      border-bottom:1px solid #e6eef6;
      text-align:left;
    }
    .shelf-info th{
      background:#f8fafc;
      color:#1572a1;
      font-weight:bold;
    }
    .shelf-info tr:last-child td{border-bottom:none;}

    /* 検索バー */
    .search-bar{
      position:absolute;
      top:0;
      right:0;
      margin:16px;
    }
    .search-bar form{
      display:flex;gap:6px;
    }
    .search-bar input[type=text]{
      padding:6px 10px;
      border:1px solid #ccc;
      border-radius:6px;
      font-size:0.95rem;
    }
    .search-bar button{
      padding:6px 12px;
      background:var(--accent);
      color:#fff;
      border:none;
      border-radius:6px;
      cursor:pointer;
      font-weight:600;
    }
    .search-bar button:hover{opacity:0.9;}
  </style>
</head>
<body>
  <jsp:include page="_miniMenu.jsp" />
  <div class="page-wrap">
    <!-- 右上検索バー -->
    <div class="search-bar">
      <form action="searchProduct" method="get">
        <input type="text" name="keyword" placeholder="商品検索">
        <button type="submit">検索</button>
      </form>
    </div>

    <h2>フロア図</h2>
    <p class="note">表示する画像: /resources/floorplan.png</p>

    <div class="img-box" role="img" aria-label="フロア図画像">
      <img id="floorImg" class="floor"
           src="${floorImage != null ? floorImage : pageContext.request.contextPath + '/resources/floorplan.png'}"
           alt="floor plan"/>
    </div>

    <!-- 将来ホットスポットを重ねる領域 -->
    <div id="hotspotLayer" style="position:relative;margin-top:12px;display:none;"></div>

    <!-- 商品情報サンプル -->
    <h2 style="margin-top:32px;">棚の商品情報</h2>
    <table class="shelf-info">
      <thead>
        <tr>
          <th>棚番号</th>
          <th>商品名</th>
          <th>価格</th>
        </tr>
      </thead>
      <tbody>
        <tr><td>A-01</td><td>ミネラルウォーター 500ml</td><td>¥120</td></tr>
        <tr><td>A-02</td><td>ポテトチップス（うすしお）</td><td>¥150</td></tr>
        <tr><td>B-05</td><td>チョコレートバー</td><td>¥100</td></tr>
        <tr><td>C-10</td><td>インスタントラーメン（しょうゆ味）</td><td>¥180</td></tr>
      </tbody>
    </table>
  </div>
</body>
</html>