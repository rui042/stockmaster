<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="stockmaster.bean.StoreBean" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="utf-8" />
  <title>出荷処理|すとっくますたー</title>
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

    input, select {
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

    .form-card {
	  width: 100%;
	  max-width: 600px;
	  padding: 20px;
	  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
	  background-color: #fff;
	  border-radius: 8px;
	}


    .note {
      font-size: 0.85rem;
      color: #666;
      margin-top: 4px;
    }

    /* トースト通知 */
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

    /* テンキー配置 */
    .keypad {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 8px;
      margin-top: 20px;
    }

    .keypad button {
      font-size: 1.2rem;
      padding: 14px 0;
      border-radius: 8px;
      border: none;
      background: #e3f2fd;
      color: #0b67c2;
      font-weight: bold;
      cursor: pointer;
    }

    .keypad button:hover {
      background: #bbdefb;
    }
  </style>
</head>
<body>
  <jsp:include page="_miniMenu.jsp" />

  <div class="form-card">
    <h2>出荷処理</h2>
    <form id="shipForm">

      <!-- 店舗選択 -->
      <label>店舗を選択
        <select id="storeId" name="storeId" required>
          <option value="">選択してください</option>
          <%
            List<StoreBean> stores = (List<StoreBean>) request.getAttribute("storeList");
            if (stores != null) {
              for (StoreBean s : stores) {
          %>
                <option value="<%= s.getStoreId() %>"><%= s.getStoreName() %></option>
          <%
              }
            }
          %>
        </select>
      </label>

      <!-- 商品ID（バーコード入力のみ） -->
      <label>商品バーコード
        <input type="text" id="productId" name="productId" placeholder="バーコードをスキャン" readonly required>
        <div class="note">※バーコードリーダー専用（手入力不可）</div>
      </label>

      <!-- 出荷数（テンキー入力） -->
      <label>出荷数
        <input type="text" id="quantity" name="quantity" readonly required>
      </label>

      <!-- テンキー -->
      <div class="keypad">
        <button type="button" onclick="addNumber(1)">1</button>
        <button type="button" onclick="addNumber(2)">2</button>
        <button type="button" onclick="addNumber(3)">3</button>
        <button type="button" onclick="addNumber(4)">4</button>
        <button type="button" onclick="addNumber(5)">5</button>
        <button type="button" onclick="addNumber(6)">6</button>
        <button type="button" onclick="addNumber(7)">7</button>
        <button type="button" onclick="addNumber(8)">8</button>
        <button type="button" onclick="addNumber(9)">9</button>
        <button type="button" onclick="clearInput()">クリア</button>
        <button type="button" onclick="addNumber(0)">0</button>
        <button type="submit">出荷確定</button>
      </div>
    </form>
  </div>

  <div id="toast"></div>

  <script>
    // テンキー入力制御
    function addNumber(num) {
      document.getElementById("quantity").value += num;
    }
    function clearInput() {
      document.getElementById("quantity").value = "";
    }

    // 出荷確定処理
    document.getElementById("shipForm").addEventListener("submit", async (e) => {
      e.preventDefault();

      const storeId = document.getElementById("storeId").value;
      const productId = document.getElementById("productId").value.trim();
      const quantity = document.getElementById("quantity").value.trim();

      if (!storeId || !productId || !quantity) {
        showToast("すべての項目を入力してください", "error");
        return;
      }

      try {
        const res = await fetch("shipStock", {
          method: "POST",
          headers: { "Content-Type": "application/x-www-form-urlencoded" },
          // 👇 JSP誤認防止のため「\${}」でエスケープ！
          body: `storeId=\${encodeURIComponent(storeId)}&productId=\${encodeURIComponent(productId)}&quantity=\${encodeURIComponent(quantity)}`
        });

        const data = await res.json();
        showToast(data.message, data.status);

        if (data.status === "success") {
          clearInput();
          document.getElementById("productId").value = "";
        }

      } catch (err) {
        console.error(err);
        showToast("通信エラーが発生しました", "error");
      }
    });

    // トースト通知
    function showToast(message, status) {
      const toast = document.getElementById("toast");
      toast.textContent = message;

      if (status === "error") toast.style.backgroundColor = "#e53935";
      else if (status === "warning") toast.style.backgroundColor = "#fbc02d";
      else toast.style.backgroundColor = "#43a047";

      toast.classList.add("show");
      setTimeout(() => toast.classList.remove("show"), 3000);
    }
  </script>
</body>
</html>
