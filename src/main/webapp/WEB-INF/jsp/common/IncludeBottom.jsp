<%@ page language="java"
		 contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8" %>

<%@ include file="../common/IncludeTop.jsp"%>

</div>

<div id="Footer">

<div id="PoweredBy">&nbsp;<a href="http://www.mybatis.org">www.mybatis.org</a>
</div>

<div id="Banner"><c:if test="${sessionScope.accountBean != null }">
	<c:if test="${sessionScope.accountBean.authenticated}">
		<c:if test="${sessionScope.accountBean.account.bannerOption}">
          ${sessionScope.accountBean.account.bannerName}
        </c:if>
	</c:if>
</c:if></div>

</div>

<!-- AI Chatbot Widget -->
<div id="chatbot-container" style="display: none;">
	<div id="chatbot-header">
		<span>🤖 AI 고객 지원</span>
		<div>
			<button id="chatbot-reset" title="대화 기록 초기화">🔄</button>
			<button id="chatbot-close">&times;</button>
		</div>
	</div>
	<div id="chatbot-messages"></div>
	<div id="chatbot-input-container">
		<input type="text" id="chatbot-input" placeholder="질문을 입력하세요..." />
		<button id="chatbot-send">전송</button>
	</div>
</div>

<button id="chatbot-button">💬</button>

<style>
/* Chatbot Button */
#chatbot-button {
	position: fixed;
	bottom: 20px;
	right: 20px;
	width: 60px;
	height: 60px;
	border-radius: 50%;
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	color: white;
	border: none;
	font-size: 28px;
	cursor: pointer;
	box-shadow: 0 4px 12px rgba(0,0,0,0.3);
	z-index: 9998;
	transition: transform 0.3s ease;
}

#chatbot-button:hover {
	transform: scale(1.1);
}

/* Chatbot Container */
#chatbot-container {
	position: fixed;
	bottom: 90px;
	right: 20px;
	width: 350px;
	height: 500px;
	background: white;
	border-radius: 12px;
	box-shadow: 0 8px 24px rgba(0,0,0,0.2);
	z-index: 9999;
	display: flex;
	flex-direction: column;
	overflow: hidden;
}

/* Chatbot Header */
#chatbot-header {
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	color: white;
	padding: 15px;
	font-weight: bold;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

#chatbot-reset, #chatbot-close {
	background: none;
	border: none;
	color: white;
	font-size: 20px;
	cursor: pointer;
	padding: 0;
	width: 30px;
	height: 30px;
	margin-left: 5px;
}

#chatbot-close {
	font-size: 24px;
}

#chatbot-reset:hover, #chatbot-close:hover {
	opacity: 0.8;
}

/* Messages Area */
#chatbot-messages {
	flex: 1;
	overflow-y: auto;
	padding: 15px;
	background: #f5f5f5;
}

.chatbot-message {
	margin-bottom: 12px;
	display: flex;
	animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
	from { opacity: 0; transform: translateY(10px); }
	to { opacity: 1; transform: translateY(0); }
}

.chatbot-message.user {
	justify-content: flex-end;
}

.chatbot-message-content {
	max-width: 70%;
	padding: 10px 14px;
	border-radius: 18px;
	word-wrap: break-word;
}

.chatbot-message.user .chatbot-message-content {
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	color: white;
}

.chatbot-message.bot .chatbot-message-content {
	background: white;
	color: #333;
	box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

/* Input Area */
#chatbot-input-container {
	display: flex;
	padding: 15px;
	background: white;
	border-top: 1px solid #e0e0e0;
}

#chatbot-input {
	flex: 1;
	border: 1px solid #ddd;
	border-radius: 20px;
	padding: 10px 15px;
	font-size: 14px;
	outline: none;
}

#chatbot-input:focus {
	border-color: #667eea;
}

#chatbot-send {
	margin-left: 10px;
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	color: white;
	border: none;
	border-radius: 20px;
	padding: 10px 20px;
	cursor: pointer;
	font-weight: bold;
	transition: opacity 0.3s ease;
}

#chatbot-send:hover {
	opacity: 0.9;
}

#chatbot-send:disabled {
	opacity: 0.5;
	cursor: not-allowed;
}

/* Loading Animation */
.chatbot-typing {
	display: inline-block;
	padding: 10px 14px;
	background: white;
	border-radius: 18px;
	box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.chatbot-typing span {
	display: inline-block;
	width: 8px;
	height: 8px;
	border-radius: 50%;
	background: #999;
	margin: 0 2px;
	animation: typing 1.4s infinite;
}

.chatbot-typing span:nth-child(2) {
	animation-delay: 0.2s;
}

.chatbot-typing span:nth-child(3) {
	animation-delay: 0.4s;
}

@keyframes typing {
	0%, 60%, 100% { transform: translateY(0); }
	30% { transform: translateY(-10px); }
}
</style>

<script>
(function() {
	var chatbotButton = document.getElementById('chatbot-button');
	var chatbotContainer = document.getElementById('chatbot-container');
	var chatbotClose = document.getElementById('chatbot-close');
	var chatbotReset = document.getElementById('chatbot-reset');
	var chatbotInput = document.getElementById('chatbot-input');
	var chatbotSend = document.getElementById('chatbot-send');
	var chatbotMessages = document.getElementById('chatbot-messages');
	var isOpen = false;

	// Load chat history from localStorage
	function loadChatHistory() {
		var history = localStorage.getItem('jpetstore_chat_history');
		if (history) {
			try {
				var messages = JSON.parse(history);
				messages.forEach(function(msg) {
					if (msg.type === 'user') {
						addUserMessage(msg.text, false);
					} else {
						addBotMessage(msg.text, false);
					}
				});
			} catch (e) {
				console.error('Failed to load chat history:', e);
			}
		}
		// Add welcome message if no history
		if (chatbotMessages.children.length === 0) {
			addBotMessage('안녕하세요! JPetStore AI 고객 지원입니다. 무엇을 도와드릴까요? 😊', true);
		}
	}

	// Save chat history to localStorage (최근 20개만 유지)
	function saveChatHistory() {
		var messages = [];
		var messageElements = chatbotMessages.querySelectorAll('.chatbot-message');
		messageElements.forEach(function(elem) {
			var type = elem.classList.contains('user') ? 'user' : 'bot';
			var text = elem.querySelector('.chatbot-message-content').textContent;
			messages.push({ type: type, text: text });
		});

		// 최근 20개 메시지만 유지 (너무 많은 히스토리가 쌓이지 않도록)
		if (messages.length > 20) {
			messages = messages.slice(messages.length - 20);
		}

		localStorage.setItem('jpetstore_chat_history', JSON.stringify(messages));
	}

	// Load history on page load
	loadChatHistory();

	// Toggle chatbot
	chatbotButton.addEventListener('click', function() {
		isOpen = !isOpen;
		chatbotContainer.style.display = isOpen ? 'flex' : 'none';
	});

	chatbotClose.addEventListener('click', function() {
		isOpen = false;
		chatbotContainer.style.display = 'none';
	});

	// Reset chat history
	chatbotReset.addEventListener('click', function() {
		if (confirm('대화 기록을 모두 삭제하시겠습니까?')) {
			localStorage.removeItem('jpetstore_chat_history');
			chatbotMessages.innerHTML = '';
			addBotMessage('안녕하세요! JPetStore AI 고객 지원입니다. 무엇을 도와드릴까요? 😊', true);
		}
	});

	// Send message
	function sendMessage() {
		var message = chatbotInput.value.trim();
		if (!message) return;

		addUserMessage(message, true);
		chatbotInput.value = '';
		chatbotSend.disabled = true;

		// Show typing indicator
		var typingId = addTypingIndicator();

		// Call backend
		fetch('/jpetstore/actions/Chatbot.action?message=' + encodeURIComponent(message))
			.then(function(response) { return response.json(); })
			.then(function(data) {
				removeTypingIndicator(typingId);
				if (data.success) {
					addBotMessage(data.response, true);
				} else {
					addBotMessage('죄송합니다. 일시적인 오류가 발생했습니다.', true);
				}
				chatbotSend.disabled = false;
			})
			.catch(function(error) {
				removeTypingIndicator(typingId);
				addBotMessage('죄송합니다. 연결에 문제가 발생했습니다.', true);
				chatbotSend.disabled = false;
			});
	}

	chatbotSend.addEventListener('click', sendMessage);
	chatbotInput.addEventListener('keypress', function(e) {
		if (e.key === 'Enter') {
			sendMessage();
		}
	});

	function addUserMessage(text, save) {
		var messageDiv = document.createElement('div');
		messageDiv.className = 'chatbot-message user';
		messageDiv.innerHTML = '<div class="chatbot-message-content">' + escapeHtml(text) + '</div>';
		chatbotMessages.appendChild(messageDiv);
		chatbotMessages.scrollTop = chatbotMessages.scrollHeight;
		if (save) saveChatHistory();
	}

	function addBotMessage(text, save) {
		var messageDiv = document.createElement('div');
		messageDiv.className = 'chatbot-message bot';
		messageDiv.innerHTML = '<div class="chatbot-message-content">' + escapeHtml(text) + '</div>';
		chatbotMessages.appendChild(messageDiv);
		chatbotMessages.scrollTop = chatbotMessages.scrollHeight;
		if (save) saveChatHistory();
	}

	function addTypingIndicator() {
		var typingDiv = document.createElement('div');
		typingDiv.className = 'chatbot-message bot';
		typingDiv.id = 'typing-indicator';
		typingDiv.innerHTML = '<div class="chatbot-typing"><span></span><span></span><span></span></div>';
		chatbotMessages.appendChild(typingDiv);
		chatbotMessages.scrollTop = chatbotMessages.scrollHeight;
		return 'typing-indicator';
	}

	function removeTypingIndicator(id) {
		var typing = document.getElementById(id);
		if (typing) {
			typing.remove();
		}
	}

	function escapeHtml(text) {
		var map = {
			'&': '&amp;',
			'<': '&lt;',
			'>': '&gt;',
			'"': '&quot;',
			"'": '&#039;'
		};
		return text.replace(/[&<>"']/g, function(m) { return map[m]; });
	}
})();
</script>

</body>
</html>
