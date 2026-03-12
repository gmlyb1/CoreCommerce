# 🛒 CoreCommerce

Spring Boot 기반의 **E-Commerce 웹 애플리케이션**입니다.  
회원 인증, 상품 주문, 고객 문의, 관리자 응대 기능과 **WebSocket 기반 실시간 채팅 기능**을 제공합니다.

---

# 📌 프로젝트 소개

CoreCommerce는 Spring Boot 기반으로 구현한 쇼핑몰 서비스입니다.

사용자는 상품을 조회하고 주문할 수 있으며, 고객 문의 기능을 통해 관리자와 소통할 수 있습니다.  
또한 **WebSocket + STOMP 기반 실시간 채팅 기능**을 통해 관리자와 고객 간 실시간 상담이 가능합니다.

---

# 🛠 Tech Stack

### Backend
- Java 17
- Spring Boot
- Spring Security
- MyBatis
- WebSocket (STOMP)

### Frontend
- Thymeleaf
- JavaScript
- Bootstrap

### Database
- MySQL

### DevOps
- GitHub

---

# 📚 주요 기능

## 👤 회원 기능

- 회원가입
- 로그인 / 로그아웃
- 카카오 로그인 (OAuth2)
- Spring Security 기반 인증 처리
- BCrypt 비밀번호 암호화

---

## 🛍 상품 기능

- 상품 목록 조회
- 상품 상세 조회
- 상품 구매 기능

---

## 📦 주문 기능

- 주문 생성
- 주문 상태 관리

| 상태 | 설명 |
|---|---|
| READY | 주문 준비 |
| PAID | 결제 완료 |
| SHIPPED | 배송 중 |

---

## 💬 고객 문의

- 고객 문의 등록
- 관리자 답변

---

## ⚡ 실시간 채팅

WebSocket + STOMP 기반 관리자 / 고객 채팅 기능

기능

- 채팅방 생성
- 실시간 메시지 전송
- typing indicator
- 관리자 / 고객 구분

---

---

# ⚙️ 주요 기술 구현

## Spring Security 인증 처리

- 세션 기반 인증
- BCrypt 비밀번호 암호화
- Role 기반 접근 제어

---

## WebSocket 실시간 채팅

STOMP 프로토콜을 사용한 실시간 채팅 기능 구현

```javascript
stompClient.subscribe('/topic/room/' + roomId + '/typing', function(message) {
    var data = JSON.parse(message.body);
});
