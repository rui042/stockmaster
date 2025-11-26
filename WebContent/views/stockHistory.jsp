<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>入出荷履歴</title>
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <style>
    :root {
      --primary: #0b67c2;
      --accent: #17a2a8;
      --bg1: #f7fbff;
      --bg2: #eaf3ff;
      --btn-bg: #fff;
      --btn-border: #d8eaf6;
      --btn-text: #153a57;
      --btn-hover-bg: linear-gradient(90deg, #17a2a8, #0b67c2);
      --shadow: 0 6px 18px rgba(8, 40, 80, 0.06);
      --radius: 20px;
    }

    html, body {
      margin: 0;
      padding: 0;
      font-family: "Yu Gothic", "Segoe UI", system-ui, Arial, sans-serif;
      background: linear-gradient(90deg, var(--bg1), var(--bg2));
      color: #19324a;
      /* 追加推奨 */
      height: 100vh;
    }

    .wrap {
      min-height:100%;
      display:flex;
      flex-direction:column;
    }

    main {
      flex:1;
      margin-left:120px;
      padding-top:80p;
      padding:20px;
      max-width:960px;
    }

    h2 {
      text-align: center;
      margin-top: 30px;
      font-family: inherit;
      color: var(--primary);
    }

    table.history-info {
      width: 400px;
      border-collapse: collapse;
      background: #fff;
      border-radius: var(--radius);
      box-shadow: var(--shadow);
      overflow: hidden;
    }

    table.history-info th,
    table.history-info td {
      border: 1px solid #ccc;
      padding: 10px 14px;
      font-size: 0.95rem;
    }

    table.history-info th {
      background-color: var(--bg2);
      color: var(--primary);
      font-weight: 600;
      text-align: left;
      width: 40%;
    }

    table.history-info td {
      text-align: left;
      width: 60%;
    }

    .history-container {
      display: flex;
      gap: 40px;
      justify-content: center;
      flex-wrap: wrap;
    }

    .back-link {
      text-align: center;
      margin-top: 20px;
    }
  </style>
</head>
<body>
  <jsp:include page="_miniMenu.jsp" />
  <div class="wrap">

	  <main>
	     <h2>入出荷履歴</h2>

	     <div class="history-container">
	       <!-- 入荷処理テーブル -->
	       <table class="history-info">
	         <thead>
	           <tr>
	             <th colspan="2">入荷処理</th>
	           </tr>
	         </thead>
	         <tbody>
	           <c:choose>
	             <c:when test="${inbound != null}">
	               <tr>
	                 <th>商品番号</th>
	                 <td>${inbound.itemId}</td>
	               </tr>
	               <tr>
	                 <th>商品名</th>
	                 <td>${inbound.itemName}</td>
	               </tr>
	               <tr>
	                 <th>入荷数量</th>
	                 <td>${inbound.quantity}</td>
	               </tr>
	               <tr>
	                 <th>最新入荷日付</th>
	                 <td><fmt:formatDate value="${inbound.actionAt}" pattern="yyyy/MM/dd HH:mm" /></td>
	               </tr>
	               <tr>
	                 <th>入荷担当者</th>
	                 <td>${inbound.userName}</td>
	               </tr>
	             </c:when>
	             <c:otherwise>
	               <tr>
	                 <td colspan="2">履歴はまだありません。</td>
	               </tr>
	             </c:otherwise>
	           </c:choose>
	         </tbody>
	       </table>

	       <!-- 出荷処理テーブル -->
	       <table class="history-info">
	         <thead>
	           <tr>
	             <th colspan="2">出荷処理</th>
	           </tr>
	         </thead>
	         <tbody>
	           <c:choose>
	             <c:when test="${outbound != null}">
	               <tr>
	                 <th>商品番号</th>
	                 <td>${outbound.itemId}</td>
	               </tr>
	               <tr>
	                 <th>商品名</th>
	                 <td>${outbound.itemName}</td>
	               </tr>
	               <tr>
	                 <th>出荷数量</th>
	                 <td>${outbound.quantity}</td>
	               </tr>
	               <tr>
	                 <th>最新出荷日付</th>
	                 <td><fmt:formatDate value="${outbound.actionAt}" pattern="yyyy/MM/dd HH:mm" /></td>
	               </tr>
	               <tr>
	                 <th>出荷担当者</th>
	                 <td>${outbound.userName}</td>
	               </tr>
	             </c:when>
	             <c:otherwise>
	               <tr>
	                 <td colspan="2">履歴はまだありません。</td>
	               </tr>
	             </c:otherwise>
	           </c:choose>
	         </tbody>
	       </table>
	     </div>

	  </main>
  </div>
</body>
</html>