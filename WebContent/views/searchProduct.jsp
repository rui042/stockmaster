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
    body{font-family:"Yu Gothic","Segoe UI",system-ui,Arial,sans-serif;
         background:linear-gradient(90deg,var(--bg1),var(--bg2));
         margin:0;padding:0;}
    .wrap{min-height:100%;display:flex;flex-direction:column;}
    main{flex:1;padding:20px;margin-left:140px;}
    h2{color:var(--primary);margin-top:0;}
    .search-box{display:flex;gap:8px;margin-bottom:16px;}
    .search-box input[type=text]{flex:1;padding:10px;border:1px solid #ccc;border-radius:8px;}
    .search-box button{padding:10px 16px;background:var(--btn-hover-bg);color:#fff;
                       border:none;border-radius:8px;font-weight:700;cursor:pointer;}
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
        <input type="text" name="keyword" value="${param.keyword}" placeholder="商品名で検索">
        <button type="submit">検索</button>
      </form>

      <!-- バーコード検索 -->
      <div class="barcode-box">
        <h3>バーコード検索</h3>
        <form action="searchProduct" method="get">
          <input type="text" name="productId" value="${param.productId}" placeholder="バーコードをスキャンしてください" autofocus>
          <button type="submit">検索</button>
        </form>
        <p style="font-size:0.85rem;color:#666;">※バーコードリーダーで読み取った値がそのまま商品IDになります</p>
      </div>

      <!-- 検索結果 -->
      <c:choose>
        <c:when test="${not empty results}">
          <table>
            <thead>
              <tr>
                <th>商品ID</th>
                <th>商品名</th>
                <th>価格</th>
                <th>現在庫</th>
                <th>最小在庫</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="p" items="${results}">
                <tr>
                  <td>${p.itemId}</td>
                  <td>${p.itemName}</td>
                  <td>¥${p.price}</td>
                  <td>${p.stockNow}</td>
                  <td>${p.stockMin}</td>
                  <td>
  						<form action="${pageContext.request.contextPath}/updateItem" method="get">
    						<input type="hidden" name="itemId" value="${p.itemId}" />
    						<input type="hidden" name="itemName" value="${p.itemName}" />
    						<input type="hidden" name="price" value="${p.price}" />
    						<button type="submit" style="padding:6px 12px;background:var(--btn-hover-bg);color:#fff;
            				border:none;border-radius:6px;font-weight:600;cursor:pointer;">更新</button>
  						</form>
				   </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </c:when>
        <c:otherwise>
          <p>検索結果はありません。</p>
        </c:otherwise>
      </c:choose>
    </main>
  </div>
</body>
</html>