<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

    /* 在庫の有無の色 */
    .highlight {
	  color: red;
	  font-weight: bold;
	}
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
      <th>分類</th>
      <th>商品名</th>
      <th>価格</th>
      <th>在庫</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="item" items="${itemList}">
      <tr>
        <td>${item.shelfId}</td>
        <td>${item.category}
        <td>${item.itemName}</td>
        <td>¥${item.price}</td>
        <td>
		  <c:choose>
		    <c:when test="${item.stockNow > item.stockMin}">
		      <span class="highlight">〇
		    </c:when>
		    <c:otherwise>
		      ×
		    </c:otherwise>
		  </c:choose>
		</td>

      </tr>
    </c:forEach>
  </tbody>

    </table>
  </div>
</body>
</html>