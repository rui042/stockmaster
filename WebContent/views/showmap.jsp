<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="utf-8"/>
  <title>フロア図</title>
  <meta name="viewport" content="width=device-width,initial-scale=1"/>

  <style>
    :root{--muted:#6b7c8a;--accent:#1572a1}
    body{margin:0;font-family:"Yu Gothic","Segoe UI",system-ui,-apple-system,sans-serif;color:#213547}

    .page-wrap{max-width:1100px;margin:40px auto;padding:16px;margin-left:140px;position:relative;}
    @media (max-width:640px){.page-wrap{margin-left:16px;margin-top:88px}}

    h2{margin:0 0 8px}
    .note{color:var(--muted);margin-bottom:10px}

    /* ★★★ 画像とピンのズレを完全解消するための構造 ★★★ */
    .img-box{
      border-radius:8px;
      border:1px solid #e6eef6;
      background:#fff;
      text-align:center;
      padding:16px;
    }

    .img-wrapper{
      position:relative;
      display:inline-block; /* ← 画像サイズに合わせる */
    }

    .floor{
      display:block;
      max-width:100%; /* ← wrapper に合わせる */
      height:auto;
      cursor:pointer;
    }

    /* ピンは wrapper 内で画像と同じ座標系で配置される */
    .pin{
      position:absolute;
      transform:translate(-50%,-100%);
      background:var(--accent);
      color:#fff;
      border-radius:50%;
      width:20px;
      height:20px;
      text-align:center;
      line-height:20px;
      font-size:12px;
      cursor:pointer;
      transition:transform 0.2s ease;
      z-index:5;
    }
    .pin.red{background:red;z-index:10;}
    .pin:hover{transform:translate(-50%,-100%) scale(1.2);background:#0e4e74;}

    .pin-label{
      position:absolute;
      background:rgba(255,255,255,0.95);
      border:1px solid #ccc;
      border-radius:6px;
      padding:4px 8px;
      font-size:0.9rem;
      display:none;
      white-space:nowrap;
      box-shadow:0 2px 6px rgba(0,0,0,0.15);
      pointer-events:none;
      z-index:20;
    }
    .pin:hover + .pin-label{display:block;}

    .shelf-info{margin-top:24px;border-collapse:collapse;width:100%;background:#fff;border-radius:8px;overflow:hidden;box-shadow:0 1px 3px rgba(0,0,0,0.08);}
    .shelf-info th,.shelf-info td{padding:12px 16px;border-bottom:1px solid #e6eef6;text-align:left;}
    .shelf-info th{background:#f8fafc;color:#1572a1;font-weight:bold;}
    .shelf-info tr:last-child td{border-bottom:none;}

    .search-bar{position:absolute;top:0;right:0;margin:16px;}
    .search-bar form{display:flex;gap:6px;align-items:center;}
    .search-bar input[type=text],.search-bar select{padding:6px 10px;border:1px solid #ccc;border-radius:6px;font-size:0.95rem;}
    .search-bar button{padding:6px 12px;background:var(--accent);color:#fff;border:none;border-radius:6px;cursor:pointer;font-weight:600;}
    .search-bar button:hover{opacity:0.9;}

    .highlight{color:green;font-weight:bold;}
    .out-of-stock{color:red;font-weight:bold;}
    .low-stock{color:orange;font-weight:bold;}
    .search-note{margin-top:8px;font-size:0.9em;color:#666;}
    .shelf-link{color:#1572a1;text-decoration:underline;cursor:pointer;}
  </style>
</head>

<body>
  <jsp:include page="/views/_miniMenu.jsp" />

  <div class="page-wrap">

    <div class="search-bar">
      <form action="${pageContext.request.contextPath}/showMap" method="get">
        <input type="text" name="keyword" placeholder="商品検索" value="${keyword}">
        <select name="category">
          <option value="">すべての分類</option>
          <option value="食品" <c:if test="${category eq '食品'}">selected</c:if>>食品</option>
          <option value="飲料" <c:if test="${category eq '飲料'}">selected</c:if>>飲料</option>
          <option value="日用品" <c:if test="${category eq '日用品'}">selected</c:if>>日用品</option>
        </select>
        <button type="submit">検索</button>
      </form>
      <p class="search-note">※ キーワード検索をするとカテゴリは無視されます。</p>
    </div>

    <h2>フロア図</h2>
    <p class="note">ピンまたはマップをクリックすると棚情報を確認できます。</p>

    <!-- ★★★ 画像とピンを同じ wrapper 内に配置 ★★★ -->
    <div class="img-box">
      <div class="img-wrapper" id="imgWrapper">
        <img class="floor" id="floorImg" src="${floorImage}" alt="floor plan"/>

        <!-- ピン描画 -->
        <c:forEach var="spot" items="${hotspots}">
          <c:if test="${spot.XPct != null && spot.YPct != null}">
            <div class="pin"
                 style="left:${spot.XPct}%; top:${spot.YPct}%"
                 data-x="${spot.XPct}" data-y="${spot.YPct}">📍</div>
            <div class="pin-label"
                 style="left:${spot.XPct}%; top:${spot.YPct - 3}%;">
              棚 ${spot.shelfId} (${spot.category})
            </div>
          </c:if>
        </c:forEach>

        <!-- 選択棚（赤ピン） -->
        <c:if test="${selectedShelf != null}">
          <div class="pin red"
               style="left:${selectedShelf.XPct}%; top:${selectedShelf.YPct}%"
               data-x="${selectedShelf.XPct}" data-y="${selectedShelf.YPct}">📍</div>
          <div class="pin-label"
               style="left:${selectedShelf.XPct}%; top:${selectedShelf.YPct - 3}%;">
            棚 ${selectedShelf.shelfId} (${selectedShelf.category})
          </div>
        </c:if>

      </div>
    </div>

    <h2 style="margin-top:32px;">棚の商品情報</h2>

    <c:choose>
      <c:when test="${not empty itemList}">
        <p>検索結果：${resultCount}件</p>
        <table class="shelf-info">
          <thead>
            <tr>
              <th>棚番号</th><th>分類</th><th>商品名</th><th>在庫</th><th>操作</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="item" items="${itemList}">
              <tr>
                <td>${item.shelfId}</td>
                <td>${item.genre}</td>
                <td>${item.itemName}</td>
                <td>
                  <c:choose>
                    <c:when test="${item.stockNow > item.stockMin}"><span class="highlight">〇</span></c:when>
                    <c:when test="${item.stockNow == 0}"><span class="out-of-stock">×</span></c:when>
                    <c:otherwise><span class="low-stock">△</span></c:otherwise>
                  </c:choose>
                </td>
                <td>
                  <a class="shelf-link" href="${pageContext.request.contextPath}/showMap?shelfSeq=${item.shelfSeq}">
                    この棚を表示
                  </a>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </c:when>

      <c:otherwise>
        <c:choose>
          <c:when test="${not empty keyword or not empty category}">
            <p>該当する商品は見つかりませんでした。</p>
          </c:when>
          <c:otherwise>
            <p>検索条件を入力して「検索」を押してください。</p>
          </c:otherwise>
        </c:choose>
      </c:otherwise>
    </c:choose>

  </div>

  <!-- ★★★ JS：クリック座標は wrapper 基準で取得 ★★★ -->
  <script>
  document.addEventListener("DOMContentLoaded", () => {
    const ctx = '<%= request.getContextPath() %>';
    const floorImg = document.getElementById("floorImg");
    const wrapper = document.getElementById("imgWrapper");

    // 画像クリック
    floorImg.addEventListener("click", (e) => {
      const rect = wrapper.getBoundingClientRect();
      let xPct = ((e.clientX - rect.left) / rect.width) * 100;
      let yPct = ((e.clientY - rect.top) / rect.height) * 100;

      xPct = Math.round(xPct);
      yPct = Math.round(yPct);

      console.log("画像クリック座標:", xPct, yPct);
      window.location.href = ctx + "/showMap?xPct=" + xPct + "&yPct=" + yPct;
    });

    // ピンクリック
    document.querySelectorAll(".pin").forEach(pin => {
      pin.addEventListener("click", (e) => {
        e.stopPropagation();
        let x = Math.round(parseFloat(pin.dataset.x));
        let y = Math.round(parseFloat(pin.dataset.y));

        console.log("ピンクリック座標:", x, y);
        window.location.href = ctx + "/showMap?xPct=" + x + "&yPct=" + y;
      });
    });
  });
  </script>

</body>
</html>