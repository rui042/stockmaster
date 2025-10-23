<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="utf-8" />
  <title>商品検索</title>
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <style>
    :root{
      --primary:#0b67c2; --accent:#17a2a8;
      --bg1:#f7fbff; --bg2:#eaf3ff;
      --btn-bg:#fff; --btn-border:#d8eaf6; --btn-text:#153a57;
      --btn-hover-bg:linear-gradient(90deg,#17a2a8,#0b67c2);
    }
    body{font-family:"Yu Gothic","Segoe UI",system-ui,Arial,sans-serif;background:linear-gradient(90deg,var(--bg1),var(--bg2));margin:0;padding:0;}
    .wrap{min-height:100%;display:flex;flex-direction:column;}
    main{flex:1;padding:20px;margin-left:140px;}
    h2{color:var(--primary);margin-top:0;}
    .search-box{display:flex;gap:8px;margin-bottom:16px;}
    .search-box input[type=text]{flex:1;padding:10px;border:1px solid #ccc;border-radius:8px;}
    .search-box button{padding:10px 16px;background:var(--btn-hover-bg);color:#fff;border:none;border-radius:8px;font-weight:700;cursor:pointer;}
    .genre-list{display:flex;flex-wrap:wrap;gap:10px;margin-bottom:20px;}
    .genre-btn{padding:8px 14px;border:1px solid var(--btn-border);border-radius:20px;background:var(--btn-bg);color:var(--btn-text);font-weight:600;cursor:pointer;}
    .genre-btn:hover{background:var(--btn-hover-bg);color:#fff;}
    .barcode-box{margin:20px 0;padding:16px;border:1px dashed #aaa;border-radius:8px;background:#fff;}
    .barcode-box h3{margin:0 0 8px;color:#0b67c2;}
    table{width:100%;border-collapse:collapse;background:#fff;}
    th,td{padding:10px 12px;border:1px solid #d8eaf6;text-align:left;}
    th{background:var(--primary);color:#fff;}
    tr:nth-child(even){background:#f0f7ff;}
  </style>
</head>
<body>
  <jsp:include page="_miniMenu.jsp" />
  <div class="wrap">
    <main>
      <h2>商品検索</h2>

      <!-- キーワード検索 -->
      <form action="searchProduct" method="get" class="search-box">
        <input type="text" name="keyword" placeholder="商品名で検索">
        <button type="submit">検索</button>
      </form>

      <!-- ジャンル検索 -->
      <div class="genre-list">
        <form action="searchProduct" method="get"><button type="submit" name="genre" value="fruits" class="genre-btn">果物</button></form>
        <form action="searchProduct" method="get"><button type="submit" name="genre" value="vegetables" class="genre-btn">野菜</button></form>
        <form action="searchProduct" method="get"><button type="submit" name="genre" value="drinks" class="genre-btn">飲料</button></form>
        <form action="searchProduct" method="get"><button type="submit" name="genre" value="snacks" class="genre-btn">お菓子</button></form>
        <form action="searchProduct" method="get"><button type="submit" name="genre" value="daily" class="genre-btn">日用品</button></form>
        <form action="searchProduct" method="get"><button type="submit" name="genre" value="others" class="genre-btn">その他</button></form>
      </div>

      <!-- バーコード検索 -->
      <div class="barcode-box">
        <h3>バーコード検索</h3>
        <form action="searchProduct" method="get">
          <input type="text" name="productId" placeholder="バーコードをスキャンしてください" autofocus required>
          <button type="submit">検索</button>
        </form>
        <p style="font-size:0.85rem;color:#666;">※バーコードリーダーで読み取った値がそのまま商品IDになります</p>
      </div>

      <!-- 検索結果 -->
      <c:if test="${not empty results}">
        <table>
          <thead>
            <tr><th>商品ID</th><th>商品名</th><th>ジャンル</th><th>在庫数</th></tr>
          </thead>
          <tbody>
            <c:forEach var="p" items="${results}">
              <tr>
                <td>${p.id}</td>
                <td>${p.name}</td>
                <td>${p.genre}</td>
                <td>${p.stock}</td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </c:if>
    </main>
  </div>
</body>
</html>