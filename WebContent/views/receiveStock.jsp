<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="utf-8" />
  <title>入荷処理</title>
  <style>
    body{font-family:"Yu Gothic","Segoe UI",system-ui,Arial,sans-serif;background:#f7fbff;margin:0;padding:20px;}
    .form-card{max-width:420px;margin:auto;background:#fff;padding:20px;border-radius:12px;box-shadow:0 4px 12px rgba(0,0,0,0.1);}
    h2{margin-top:0;color:#0b67c2;}
    label{display:block;margin-top:12px;font-weight:600;}
    input{width:100%;padding:10px;margin-top:6px;border:1px solid #ccc;border-radius:8px;}
    button{margin-top:20px;padding:12px;width:100%;background:#0b67c2;color:#fff;border:none;border-radius:8px;font-weight:700;cursor:pointer;}
    button:hover{opacity:0.9;}
    .note{font-size:0.85rem;color:#666;margin-top:4px;}
  </style>
</head>
<body>
  <jsp:include page="_miniMenu.jsp" />
  <div class="form-card">
    <h2>入荷処理</h2>
    <form action="receiveStock" method="post">
      <!-- 商品IDはバーコードリーダー入力OK -->
      <label>商品ID（バーコード入力可）
        <input type="text" name="productId" placeholder="商品IDをスキャンまたは入力" autofocus required>
        <div class="note">※バーコードリーダーで読み取った値がそのまま商品IDになります</div>
      </label>

      <!-- 入荷数は手入力専用 -->
      <label>入荷数
        <input type="number" name="quantity" min="1" required>
        <div class="note">※入荷数は必ず手入力してください</div>
      </label>

      <button type="submit">在庫を更新</button>
    </form>
  </div>
</body>
</html>