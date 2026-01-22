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
      --primary:#0b67c2;
       --accent:#17a2a8;
      --bg1:#f7fbff;
      --bg2:#eaf3ff;
      --btn-bg:#fff;
      --btn-border:#d8eaf6;
      --btn-text:#153a57;
      --btn-hover-upbg:linear-gradient(90deg,#17a2a8,#0b67c2);
      --btn-hover-hsbg:linear-gradient(90deg, #ff9800, #f39c12);
    }

    body{
      font-family:"Yu Gothic","Segoe UI",system-ui,Arial,sans-serif;
      background:linear-gradient(90deg,var(--bg1),var(--bg2));
      margin:0;padding:0;
    }

    .wrap{
      min-height:100%;
      display:flex;
      flex-direction:column;
    }

    main{
      flex:1;
      padding:20px;
      margin-left:140px;
    }

    h2{
      color:var(--primary);
      margin-top:0;
    }

    .search-box{
      display:flex;
      gap:8px;
      margin-bottom:16px;
    }

    .search-box input[type=text]{
      flex:1;
      padding:10px;
      border:1px solid #ccc;
      border-radius:8px;
    }

    .search-box button{
      padding:10px 16px;
      background:var(--primary);
      color:#fff;
      border:none;
      border-radius:8px;
      font-weight:700;
      cursor:pointer;
    }

    .barcode-box{
      margin:20px 0;
      padding:16px;
      border:1px dashed #aaa;
      border-radius:8px;
      background:#fff;
    }

    .barcode-box h3{
      margin:0 0 8px;
      color:#0b67c2;
    }

    table{
      width:100%;
      border-collapse:collapse;
      background:#fff;
    }

    th,td{
      padding:10px 12px;
      border:1px solid #d8eaf6;
      text-align:left;
    }

    th{
      background:var(--primary);
      color:#fff;
    }

    tr:nth-child(even){
      background:#f0f7ff;
    }

    .btn-update {
      padding: 6px 12px;
      background: var(--btn-hover-upbg);
      color: #fff;
      border: none;
      border-radius: 6px;
      font-weight: 600;
      cursor: pointer;
    }

    .btn-history {
      padding: 6px 12px;
      background: var(--btn-hover-hsbg);
      color: #fff;
      border: none;
      border-radius: 6px;
      font-weight: 600;
      cursor: pointer;
   }

    .btn-update:hover,
    .btn-history:hover {
      opacity: 0.85;
    }

		/* 商品IDボタン */
		.jan-cell {
		  position: relative;
		}

		.jan-toggle {
		  padding: 4px 8px;
		  font-size: 1rem;
		  background: #fff;
		  border: 1px solid #ccc;
		  border-radius: 6px;
		  cursor: pointer;
		}

		.jan-popup {
		  display: none;
		  position: absolute;
		  top: 32px;
		  left: 0;
		  background: #ffffff;
		  color: #333;
		  padding: 8px 12px;
		  border-radius: 8px;
		  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
		  white-space: nowrap;
		  z-index: 10;
		  animation: fadeIn 0.25s ease;
		}

		@keyframes fadeIn {
		  from { opacity: 0; transform: translateY(-6px); }
		  to   { opacity: 1; transform: translateY(0); }
		}
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
                <th>棚番号</th>
    						<th>分類</th>
                <th>価格</th>
                <th>現在庫</th>
                <th>最小在庫</th>
                <th>入出荷履歴</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="p" items="${results}">
                <tr>
                	<!-- 商品ID表示ボタン -->
                  <td class="jan-cell">
									  <button type="button" class="jan-toggle" onclick="toggleJan(this)">
											🔍
									  </button>
									  <div class="jan-popup">
									    ${p.itemId}
									  </div>
									</td>
                  <td>${p.itemName}</td>
                  <td>${p.shelfId}</td>
      						<td>${p.category}</td>
                  <td>¥${p.price}</td>
                  <td>${p.stockNow}</td>
                  <td>${p.stockMin}</td>
                  <td>
	                <!-- 入出荷履歴の詳細ボタン -->
		                <form action="${pageContext.request.contextPath}/stockHistory" method="get">
		                  <input type="hidden" name="itemId" value="${p.itemId}" />
		                  <input type="hidden" name="storeId" value="${p.storeId}" />
		                  <button type="submit" class="btn-history">詳細</button>
		                </form>
		              </td>
	                  <td>
	                    <!-- 更新ボタン -->
				  					<form action="${pageContext.request.contextPath}/updateItem" method="get">
				    				  <input type="hidden" name="itemId" value="${p.itemId}" />
				    				  <input type="hidden" name="itemName" value="${p.itemName}" />
				    				  <input type="hidden" name="price" value="${p.price}" />
				    				  <button type="submit" class="btn-update">更新</button>
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

      <script>
			function toggleJan(btn) {
			  const popup = btn.nextElementSibling;
			  const isOpen = popup.style.display === "block";

			  // すべて閉じる
			  document.querySelectorAll(".jan-popup").forEach(el => el.style.display = "none");

			  // 押した行だけ開く
			  if (!isOpen) {
			    popup.style.display = "block";
			  }
			}

			// 画面のどこかを押したら閉じる
			document.addEventListener("click", function(e) {
			  if (!e.target.closest(".jan-cell")) {
			    document.querySelectorAll(".jan-popup").forEach(el => el.style.display = "none");
			  }
			});
			</script>

    </main>
  </div>
</body>
</html>