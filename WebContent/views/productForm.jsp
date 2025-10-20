<%@ page contentType="text/html; charset=UTF-8" language="java" %>
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
      height:100%;margin:0;
      font-family:"Yu Gothic","Segoe UI",system-ui,Arial,sans-serif;
      background:linear-gradient(90deg,var(--bg1),var(--bg2));
      color:#19324a;
    }
    .wrap{min-height:100%;display:flex;flex-direction:column;}
    header{display:flex;justify-content:flex-end;align-items:center;
      padding:10px 16px;background:transparent;}
    .user-area{font-weight:600;color:var(--primary);white-space:nowrap;font-size:0.95rem;}
    main{flex:1;display:flex;align-items:center;justify-content:center;padding:18px;margin-left:140px;}
    .form-card{
      width:100%;max-width:420px;
      background:var(--btn-bg);
      border:1px solid var(--btn-border);
      border-radius:var(--radius);
      box-shadow:var(--shadow);
      padding:24px;
    }
    h2{margin-top:0;margin-bottom:16px;color:var(--primary);}
    label{display:block;margin-top:12px;font-weight:600;}
    input[type=text], input[type=number]{
      width:100%;padding:10px;margin-top:6px;
      border:1px solid #ccc;border-radius:8px;font-size:0.95rem;
    }
    button{
      margin-top:20px;padding:12px 20px;
      background:var(--btn-hover-bg);color:#fff;
      border:none;border-radius:8px;cursor:pointer;
      font-size:1rem;font-weight:700;width:100%;
    }
    button:hover{opacity:0.9;}
  </style>
</head>
<body>
  <div class="wrap">
    <!-- 左側にミニメニューを常に表示 -->
    <jsp:include page="_miniMenu.jsp" />

    <header>
      <div class="header-right">
        <div class="user-area">${requestScope.username != null ? requestScope.username : "ゲスト"} さん</div>
      </div>
    </header>

    <main>
      <div class="form-card">
        <h2>商品登録</h2>
        <!-- サーブレットのマッピングURLに合わせる -->
        <form action="productRegister" method="post">
          <label>商品名
            <input type="text" name="name" required>
          </label>
          <label>価格
            <input type="number" name="price" min="0" required>
          </label>
          <label>棚番号
            <input type="text" name="shelf" required>
          </label>
          <button type="submit">登録する</button>
        </form>
      </div>
    </main>
  </div>
</body>
</html>