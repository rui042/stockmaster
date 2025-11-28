<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="utf-8"/>
  <title>チャット</title>
  <meta name="viewport" content="width=device-width,initial-scale=1"/>
  <style>
    :root{
      --bg:#f6fbff;
      --card:#fff;
      --accent:#1572a1;
      --muted:#6b7c8a
    }

    body{
      margin:0;
      font-family:"Yu Gothic","Segoe UI",system-ui,-apple-system,sans-serif;
      background:transparent;
      color:#213547
    }

    /* 左にミニメニュー分のスペースを確保 */
    .page-wrap{
      max-width:980px;
      margin:40px auto;
      padding:16px;
      margin-left:140px
    }

    @media (max-width:640px){
     .page-wrap{
       margin-left:16px;
       margin-top:80px
     }
   }

    h2{
      margin:0 0 8px
    }
    .chat-wrap{
      background:var(--card);
      border:1px solid #e6eef6;
      border-radius:10px;
      padding:12px;
      box-shadow:0 8px 20px rgba(0,0,0,0.04);
    }

		.messages {
		  height: 380px;
		  overflow: auto;
		  padding: 12px;
		  background: linear-gradient(180deg,#fff,#fbfeff);
		  border-radius: 8px;
		  display: flex;
		  flex-direction: column;
		  gap: 6px;
		}

		.bubble {
		  display: inline-block;
		  padding: 10px 12px;
		  border-radius: 12px;
		  margin-top: 8px;
		  max-width: 72%;
		  word-break: break-word;
		}

		.bubble.me {
		  background: linear-gradient(90deg,#1572a1,#0e5a7e);
		  color: #fff;
		  margin-left: auto;
		  border-bottom-right-radius: 4px;
		  text-align: right;
		}

		.bubble.you {
		  background: #f1f7fb;
		  color: #213547;
		  margin-right: auto;
		  border-bottom-left-radius: 4px;
		  text-align: left;
		}

    .input-row{
      display:flex;
      gap:8px;
      margin-top:10px:
    }

    .input{
      flex:1;
      padding:10px;
      border-radius:10px;
      border:1px solid #d7e7f0:
    }

    .btn{
      padding:10px 14px;
      border-radius:10px;
      border:0;
      background:var(--accent);
      color:#fff;
      cursor:pointer:
    }

    .delete-btn {
		  background: #6b7c8a;
		  color: #fff;
		  padding: 10px 14px;
		  border-radius: 10px;
		  border: 0;
		  cursor: pointer;
		  transition: background 0.3s ease;
		}

		.delete-btn:hover {
		  background: #4e5d6a;
		}
  </style>
</head>
<body>
  <jsp:include page="_miniMenu.jsp" />
  <div class="page-wrap">
    <h2>チャット</h2>
    <div class="chat-wrap" role="region" aria-label="チャット">
      <div id="messages" class="messages" aria-live="polite">
        <div class="bubble you">
          こんにちは！こちらはチャット相談ページです。<br/>
				  現在次のことに対応しております。該当する番号を入力してください。<br/>
				  1:商品に関する案内<br/>
				  2:サポート
				</div>
		      <c:forEach var="entry" items="${sessionScope.chatHistory}">
			    <div class="bubble ${entry[0]}">
			      <c:out value="${entry[0] == 'me' ? '自分: ' : '相手: '}" />
			      <c:out value="${entry[1]}" />
			    </div>
			  </c:forEach>
      </div>

      <form method="post" action="chat" class="input-row">
        <input id="chatInput" name="message" class="input" type="text" placeholder="メッセージを入力してEnter" autocomplete="off"/>
        <input type="hidden" name="currentStepKey" value="${currentStepKey != null ? currentStepKey : 'START'}" />
        <button id="chatSend" class="btn" type="submit">送信</button>
        <button name="delete" value="true" class="btn delete-btn" type="submit">履歴削除</button>
      </form>

    </div>
  </div>

	<script>
	  document.getElementById('chatInput').focus();

	  // 最新メッセージまでスクロール
	  function scrollToBottom() {
	    const messages = document.querySelector('.messages');
	    if (messages) {
	      messages.scrollTop = messages.scrollHeight;
	    }
	  }

	  // ページロード時にスクロール
	  window.addEventListener('load', scrollToBottom);

	  // DOM構築完了時にもスクロール
	  window.addEventListener('DOMContentLoaded', scrollToBottom);
	</script>
</body>
</html>