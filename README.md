## Server Building for E-Commerce Service

### Sequence Diagram

1. 잔액 충전 또는 조회
```mermaid
sequenceDiagram
    participant User
    participant Server

    User->>Server: 조회 요청
    Server-->>User: 응답
    User->>Server: 잔액 충전 요청
    Server-->>User: 성공 시, 업데이트된 잔액 응답
    Server-->>User: 실패 시, 에러 출력
