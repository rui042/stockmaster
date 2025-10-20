<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="utf-8"/>
  <title>チャット（擬似）</title>
  <meta name="viewport" content="width=device-width,initial-scale=1"/>
  <style>
    :root{--bg:#f6fbff;--card:#fff;--accent:#1572a1;--muted:#6b7c8a}
    body{margin:0;font-family:"Yu Gothic","Segoe UI",system-ui,-apple-system,sans-serif;background:transparent;color:#213547}
    /* 左にミニメニュー分のスペースを確保 */
    .page-wrap{max-width:980px;margin:40px auto;padding:16px;margin-left:140px}
    @media (max-width:640px){ .page-wrap{margin-left:16px;margin-top:80px} }

    h2{margin:0 0 8px}
    .chat-wrap{background:var(--card);border:1px solid #e6eef6;border-radius:10px;padding:12px;box-shadow:0 8px 20px rgba(0,0,0,0.04)}
    .messages{height:380px;overflow:auto;padding:12px;background:linear-gradient(180deg,#fff,#fbfeff);border-radius:8px}
    .bubble{display:inline-block;padding:10px 12px;border-radius:12px;margin-top:8px;max-width:72%;word-break:break-word}
    .bubble.me{background:linear-gradient(90deg,#1572a1,#0e5a7e);color:#fff;margin-left:auto;border-bottom-right-radius:4px}
    .bubble.you{background:#f1f7fb;color:#213547;border-bottom-left-radius:4px}
    .input-row{display:flex;gap:8px;margin-top:10px}
    .input{flex:1;padding:10px;border-radius:10px;border:1px solid #d7e7f0}
    .btn{padding:10px 14px;border-radius:10px;border:0;background:var(--accent);color:#fff;cursor:pointer}
  </style>
</head>
<body>
  <jsp:include page="_miniMenu.jsp" />
  <div class="page-wrap">
    <h2>チャット（擬似）</h2>
    <div class="chat-wrap" role="region" aria-label="チャット">
      <div id="messages" class="messages" aria-live="polite">
        <div class="bubble you">ようこそ。これは画面サンプルです。</div>
      </div>

      <div class="input-row">
        <input id="chatInput" class="input" type="text" placeholder="メッセージを入力してEnter" autocomplete="off"/>
        <button id="chatSend" class="btn" type="button">送信</button>
      </div>
    </div>
  </div>

  <script>
    (function(){
      const input = document.getElementById('chatInput');
      const send = document.getElementById('chatSend');
      const messages = document.getElementById('messages');

      function escapeHtml(s){ return String(s).replace(/[&<>"]/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;'}[c])); }
      function append(text, cls){
        const d = document.createElement('div');
        d.className = 'bubble ' + cls;
        d.innerHTML = escapeHtml(text);
        messages.appendChild(d);
        messages.scrollTop = messages.scrollHeight;
      }

      function sendMessage(){
        const v = input.value.trim();
        if(!v) return;
        append('自分: ' + v, 'me');
        input.value = '';
        setTimeout(()=>{ append('相手: 受け取りました', 'you'); }, 600 + Math.random()*700);
      }

      send.addEventListener('click', sendMessage);
      input.addEventListener('keydown', e => { if(e.key === 'Enter'){ e.preventDefault(); sendMessage(); }});
      input.focus();
    })();
  </script>
</body>
</html>