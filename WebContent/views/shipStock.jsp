<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="utf-8" />
  <title>出荷処理</title>
  <style>
    body {
      font-family: "Yu Gothic", "Segoe UI", system-ui, Arial, sans-serif;
      background: #f7fbff;
      margin: 0;
      padding: 20px;
    }

    .form-card {
      max-width: 420px;
      margin: auto;
      background: #fff;
      padding: 20px;
      border-radius: 12px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }

    h2 {
      margin-top: 0;
      color: #0b67c2;
    }

    label {
      display: block;
      margin-top: 12px;
      font-weight: 600;
    }

    input {
      width: 100%;
      padding: 10px;
      margin-top: 6px;
      border: 1px solid #ccc;
      border-radius: 8px;
    }

    button {
      margin-top: 20px;
      padding: 12px;
      width: 100%;
      background: #0b67c2;
      color: #fff;
      border: none;
      border-radius: 8px;
      font-weight: 700;
      cursor: pointer;
    }

    button:hover {
      opacity: 0.9;
    }

    .note {
      font-size: 0.85rem;
      color: #666;
      margin-top: 4px;
    }

    /* 🔽 通知トースト（上から降りてくる） */
    #toast {
      position: fixed;
      top: -60px;
      left: 50%;
      transform: translateX(-50%);
      background-color: #4caf50;
      color: white;
      padding: 14px 28px;
      border-radius: 8px;
      box-shadow: 0 3px 8px rgba(0, 0, 0, 0.2);
      opacity: 0;
      transition: top 0.5s ease, opacity 0.5s ease;
      z-index: 9999;
      font-weight: 600;
    }

    #toast.show {
      top: 20px;
      opacity: 1;
    }
  </style>
</head>
<body>
  <jsp:include page="_miniMenu.jsp" />

  <div class="form-card">
    <h2>出荷処理</h2>
    <form id="shipForm">
      <!-- 商品ID -->
      <label>商品ID（バーコード入力可）
        <input type="text" id="productId" name="productId" placeholder="商品IDをスキャンまたは入力" autofocus required>
        <div class="note">※バーコードリーダーで読み取った値がそのまま商品IDになります</div>
      </label>

      <!-- 出荷数 -->
      <label>出荷数
        <input type="number" id="quantity" name="quantity" min="1" required>
        <div class="note">※出荷数は必ず手入力してください</div>
      </label>

      <button type="submit">在庫を更新</button>
    </form>
  </div>

  <!-- 🔽 通知エリア -->
  <div id="toast"></div>

  <script>
  document.getElementById("shipForm").addEventListener("submit", async (e) => {
      e.preventDefault(); // ページ遷移防止

      const productId = document.getElementById("productId").value.trim();
      const quantity = document.getElementById("quantity").value.trim();

      if (!productId || !quantity) {
          showToast("商品IDと数量を入力してください", "error");
          return;
      }

      try {
          const res = await fetch("shipStock", {
              method: "POST",
              headers: { "Content-Type": "application/x-www-form-urlencoded" },
              body: `productId=\${encodeURIComponent(productId)}&quantity=\${encodeURIComponent(quantity)}`
          });

          const data = await res.json();
          console.log("レスポンス:", data);
          showToast(data.message, data.status);
      } catch (err) {
          console.error(err);
          showToast("通信エラーが発生しました", "error");
      }
  });

  // 🔽 トースト表示関数
  function showToast(message, status) {
      const toast = document.getElementById("toast");
      toast.textContent = message;

      // ステータスに応じて色変更
      if (status === "error") toast.style.backgroundColor = "#e53935";
      else if (status === "warning") toast.style.backgroundColor = "#fbc02d";
      else toast.style.backgroundColor = "#43a047";

      toast.classList.add("show");

      // 3秒後にフェードアウト
      setTimeout(() => {
          toast.classList.remove("show");
      }, 3000);
  }
  </script>
</body>
</html>
