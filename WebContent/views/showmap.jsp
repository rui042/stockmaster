<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
  String username = (String) session.getAttribute("username");
%>
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
	.out-of-stock {
	  color: black;
	  font-weight: bold;
	}
	.low-stock {
	  color: green;
	  font-weight: bold;
	}

	.search-note {
	  margin-top: 8px;
	  font-size: 0.9em;
	  color: #666;
	}
  </style>
</head>
<body>
  <jsp:include page="_miniMenu.jsp" />
  <div class="page-wrap">
    <!-- 右上検索バー -->
    <div class="search-bar">
      <form id="searchForm" action="${pageContext.request.contextPath}/showMap" method="get">
        <input type="hidden" name="storeId" value="${param.storeId}">
        <input type="text" name="keyword" placeholder="商品検索" value="${param.keyword}">

        <select name="category">
	      <option value="">すべての分類</option>
	      <option value="食品" ${param.category == '食品' ? 'selected' : ''}>食品</option>
	      <option value="飲料" ${param.category == '飲料' ? 'selected' : ''}>飲料</option>
	      <option value="日用品" ${param.category == '日用品' ? 'selected' : ''}>日用品</option>
	      <option value="文房具" ${param.category == '文房具' ? 'selected' : ''}>文房具</option>
	      <option value="季節商品" ${param.category == '季節商品' ? 'selected' : ''}>季節商品</option>
	      <option value="家電" ${param.category == '家電' ? 'selected' : ''}>家電</option>
	      <option value="衣料品" ${param.category == '衣料品' ? 'selected' : ''}>衣料品</option>
	      <option value="ペット用品" ${param.category == 'ペット用品' ? 'selected' : ''}>ペット用品</option>
	      <option value="書籍" ${param.category == '書籍' ? 'selected' : ''}>書籍</option>
	      <option value="健康用品" ${param.category == '健康用品' ? 'selected' : ''}>健康用品</option>
	    </select>

	    <button type="submit">検索</button>
      </form>
      <p class="search-note">
	    ※ 商品の検索を行うと、カテゴリは無視され商品の検索が優先されます。<br>
	    ※ カテゴリの検索を行いたい場合は商品検索を削除してください。
	  </p>
    </div>

    <!-- 商品検索が行われた場合カテゴリはリセット -->
    <script>
	  document.addEventListener("DOMContentLoaded", function () {
	    const form = document.getElementById("searchForm");
	    const keywordInput = form.querySelector("input[name='keyword']");
	    const categorySelect = form.querySelector("select[name='category']");

	    form.addEventListener("submit", function (e) {
	      if (keywordInput.value.trim() !== "") {
	        // キーワードが入力されている場合、カテゴリを「すべての分類」にリセット
	        categorySelect.value = "";
	      }
	    });
	  });
	</script>


    <h2>フロア図</h2>
    <p class="note">表示する画像: /resources/floorplan.png</p>

    <div class="img-box" role="img" aria-label="フロア図画像">
      <img id="floorImg" class="floor"
           src="${floorImage != null ? floorImage : pageContext.request.contextPath + '/resources/floorplan.png'}"
           alt="floor plan"/>
    </div>

    <!-- 将来ホットスポットを重ねる領域 -->
    <div id="hotspotLayer" style="position:relative;margin-top:12px;display:none;"></div>

    <!-- 商品情報 -->
    <h2 style="margin-top:32px;">棚の商品情報</h2>
    <table class="shelf-info">
	  <c:if test="${not empty itemList}">
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
		  <p>検索結果：${resultCount}件</p>
			<c:forEach var="item" items="${itemList}">
			<tr>
			  <td>${item.shelfId}</td>
			  <td>${item.category}</td>
			  <td>${item.itemName}</td>
			  <td>¥${item.price}</td>
			  <td>
			    <c:choose>
				  <c:when test="${item.stockNow > item.stockMin}">
				    <span class="highlight">〇</span>
				  </c:when>
				  <c:when test="${item.stockNow == 0}">
				    <span class="out-of-stock">×</span>
				  </c:when>
				  <c:otherwise>
				    <span class="low-stock">△</span>
				  </c:otherwise>
				</c:choose>
			  </td>
			</tr>
		  </c:forEach>
		</c:if>

		<c:if test="${empty itemList}">
		  <p>該当する商品は見つかりませんでした。</p>
		</c:if>

	  </tbody>
    </table>
  </div>
</body>
</html>