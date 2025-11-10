<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="utf-8" />
  <title>商品登録</title>
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
      --shadow:0 6px 18px rgba(8,40,80,0.06);
      --radius:20px;
    }

    html,body{
      height:100%;
      margin:0;
      font-family:"Yu Gothic","Segoe UI",system-ui,Arial,sans-serif;
      background:linear-gradient(90deg,var(--bg1),var(--bg2));
      color:#19324a;
    }

    .wrap{min-height:100%;
      display:flex;
      flex-direction:column;
    }

    header{
      display:flex;
      justify-content:flex-end;align-items:center;
      padding:10px 16px;
      background:transparent;
    }

    .user-area{
      font-weight:600;
      color:var(--primary);
      white-space:nowrap;
      font-size:0.95rem;
    }

    main{
      flex:1;display:
      flex;align-items:center;
      justify-content:center;
      padding:18px;
      margin-left:140px;
    }

    .form-card{
      width:100%;
      max-width:420px;
      background:var(--btn-bg);
      border:1px solid var(--btn-border);
      border-radius:var(--radius);
      box-shadow:var(--shadow);
      padding:24px;
    }

    h2{
      margin-top:0;
      margin-bottom:16px;
      color:var(--primary);
    }

    label{
      display:block;
      margin-top:12px;
      font-weight:600;
    }

    input[type=text], input[type=number]{
      width:100%;
      padding:10px;
      margin-top:6px;
      border:1px solid #ccc;
      border-radius:8px;
      font-size:0.95rem;
    }

    select {
	  width: 100%;
	  padding: 10px;
	  margin-top: 6px;
	  border: 1px solid #ccc;
	  border-radius: 8px;
	  font-size: 0.95rem;
	  background-color: #fff;
	}

    button{
      margin-top:20px;padding:12px 20px;
      background:var(--btn-hover-bg);color:#fff;
      border:none;border-radius:8px;cursor:pointer;
      font-size:1rem;font-weight:700;width:100%;
    }

    button:hover{opacity:0.9;}

    #toast {
	  position: fixed;
	  bottom: 20px;
	  left: 50%;
	  transform: translateX(-50%);
	  background: #333;
	  color: #fff;
	  padding: 12px 24px;
	  border-radius: 4px;
	  opacity: 0;
	  transition: opacity 0.3s ease;
	  z-index: 9999;
	}
	#toast.show {
	  opacity: 1;
	}
  </style>
</head>
<body>
  <!-- 左側にミニメニューを常に表示 -->
  <jsp:include page="_miniMenu.jsp" />
  <div class="wrap">

    <main>
      <div class="form-card">
        <h2>商品登録</h2>

        <form id="productForm" action="productRegister" method="post">
		  <label>商品番号(バーコード入力可)
		    <input type="text" name="itemId" pattern="\d{13}" maxlength="13" required autofocus>
		  </label>

		  <label>商品名
		    <input type="text" name="name" required>
		  </label>

		  <label>棚番号（例：A-01）
		    <input type="text" name="shelf" required>
		  </label>

		  <label>分類
		    <input type="text" name="category" required>
		  </label>

		  <label>価格
		    <input type="number" name="price" min="0" required>
		  </label>

		  <label>入荷数（在庫数）
		    <input type="number" name="stockNow" min="1" required>
		  </label>

		  <label>ストック数
		    <input type="number" name="stockMin" min="0" required>
		  </label>

		  <label>店舗選択
			<select name="storeId" required>
			<option value="">--選択--</option>
			<c:forEach var="store" items="${storeList}">
			<option value="${store.storeId}">${store.storeName}</option>
			</c:forEach>
			</select>
		  </label>

		  <button type="submit">登録する</button>
		</form>
	  </div>
	</main>

	<div id="toast"></div>
	<script>
	document.addEventListener("DOMContentLoaded", () => {
	  const form = document.getElementById("productForm");
	  const itemIdInput = form.querySelector('input[name="itemId"]');
	  const stockNowInput = form.querySelector('input[name="stockNow"]');
	  const stockMinInput = form.querySelector('input[name="stockMin"]');

	  // 入荷数が入力されたら最小在庫数を自動計算(1/3)
	  stockNowInput.addEventListener("input", () => {
	    const stockNow = parseInt(stockNowInput.value, 10);
	    if (!isNaN(stockNow)) {
	      const stockMin = Math.floor(stockNow / 3);
	      stockMinInput.value = stockMin;
	    } else {
	      stockMinInput.value = "";
	    }
	  });

	  // 商品番号入力後に商品名へフォーカス（バーコード読み取り対応）
	  itemIdInput.addEventListener("change", () => {
	    form.querySelector('input[name="name"]').focus();
	  });

	  // Enterキーでフォーム送信（バーコードリーダー対応）
	  itemIdInput.addEventListener("keypress", (e) => {
	    if (e.key === "Enter") {
	      e.preventDefault();
	      form.requestSubmit();
	    }
	  });

	  // 登録処理
	  form.addEventListener("submit", async (e) => {
	    e.preventDefault();

	    const formData = new FormData(form);
	    const params = new URLSearchParams();
	    for (const [key, value] of formData.entries()) {
	      params.append(key, value.trim());
	    }

	    try {
	      const res = await fetch("productRegister", {
	        method: "POST",
	        headers: { "Content-Type": "application/x-www-form-urlencoded" },
	        body: params.toString()
	      });

	      const text = await res.text();
	      console.log("Response:", text);
	      const data = JSON.parse(text);
	      showToast(data.message, data.status);
	      if (data.status === "success") form.reset();
	    } catch (err) {
	      console.error(err);
	      showToast("通信エラーが発生しました", "error");
	    }
	  });

	  function showToast(message, status) {
	    const toast = document.getElementById("toast");
	    toast.textContent = message;

	    if (status === "error") toast.style.backgroundColor = "#e53935";
	    else if (status === "warning") toast.style.backgroundColor = "#fbc02d";
	    else toast.style.backgroundColor = "#43a047";

	    toast.classList.add("show");
	    setTimeout(() => toast.classList.remove("show"), 3000);
	  }
	});
	</script>
  </div>
</body>
</html>