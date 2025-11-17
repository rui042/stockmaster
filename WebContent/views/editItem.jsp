<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="utf-8" />
  <title>商品情報の編集</title>
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <style>
    :root {
      --primary:#0b67c2; --accent:#17a2a8;
      --bg1:#f7fbff; --bg2:#eaf3ff;
      --btn-hover-bg:linear-gradient(90deg,#17a2a8,#0b67c2);
    }
    body {
      font-family:"Yu Gothic","Segoe UI",system-ui,Arial,sans-serif;
      background:linear-gradient(90deg,var(--bg1),var(--bg2));
      margin:0;padding:0;
    }
    .container {
      max-width:500px;
      margin:60px auto;
      background:#fff;
      padding:24px;
      border-radius:12px;
      box-shadow:0 0 10px rgba(0,0,0,0.1);
    }
    h2 {
      color:var(--primary);
      margin-bottom:20px;
      text-align:center;
    }
    label {
      display:block;
      margin-bottom:12px;
      font-weight:bold;
    }
    input[type=text], input[type=number] {
      width:100%;
      padding:10px;
      border:1px solid #ccc;
      border-radius:8px;
      margin-top:4px;
    }
    button {
      margin-top:20px;
      width:100%;
      padding:12px;
      background:var(--btn-hover-bg);
      color:#fff;
      border:none;
      border-radius:8px;
      font-weight:700;
      cursor:pointer;
    }
    .message {
      margin-top:16px;
      color:green;
      text-align:center;
    }
  </style>
</head>
<body>
  <div class="container">
    <h2>商品情報の編集</h2>
    <form action="updateItem" method="post">
      <input type="hidden" name="itemId" value="${param.itemId}" />
      <label>商品名
        <input type="text" name="itemName" value="${param.itemName}" required />
      </label>
      <label>価格
        <input type="number" name="price" value="${param.price}" min="0" required />
      </label>
      <button type="submit">更新する</button>
    </form>
    <c:if test="${not empty message}">
      <div class="message">${message}</div>
    </c:if>
  </div>
</body>
</html>