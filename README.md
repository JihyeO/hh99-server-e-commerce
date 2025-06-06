## Server Building for E-Commerce Service

### Design

<details>
    <summary>1. Sequence Diagram</summary>
<details>
    <summary>1. 잔액 충전 또는 조회</summary>
    
```mermaid
sequenceDiagram
    actor Client
    participant API_Server
    participant Balance_Service
    participant User_DB

    %% 잔액 충전 API
    Client->>API_Server: POST /chargeBalance (userId, amount)
    API_Server->>Balance_Service: chargeBalance(userId, amount)
    Balance_Service->>User_DB: getUserBalance(userId)
    User_DB-->>Balance_Service: currentBalance
    Balance_Service->>User_DB: updateBalance(userId, currentBalance + amount)
    User_DB-->>Balance_Service: updateResult
    Balance_Service-->>API_Server: success or error
    API_Server-->>Client: 200 OK (잔액 충전 성공 메시지)

    %% 잔액 조회 API
    Client->>API_Server: GET /getBalance?userId=...
    API_Server->>Balance_Service: getBalance(userId)
    Balance_Service->>User_DB: getUserBalance(userId)
    User_DB-->>Balance_Service: currentBalance
    Balance_Service-->>API_Server: currentBalance
    API_Server-->>Client: 200 OK (잔액: currentBalance)

````
</details>

<details>
    <summary>2. 상품 조회</summary>

```mermaid
sequenceDiagram
    actor Client
    Participant API_Server
    Participant Product_Service
    Participant Product_DB

    Client -> API_Server: GET /products
    API_Server -> Product_Service: getProductList()
    Product_Service -> Product_DB: getProducts()
    Product_DB --> Product_Service: List<Product>
    Product_Service --> API_Server: List<Product>
    API_Server --> Client: 200 OK (상품 목록 반환)
````

</details>

<details>
    <summary>3. 선착순 쿠폰 기능</summary>

```mermaid
sequenceDiagram
    Participant Client
    Participant API_Server
    Participant Coupon_Service
    Participant Coupon_DB

    Client -> API_Server: POST /issueCoupon (userId)
    API_Server -> Coupon_Service: issueCoupon(userId)
    Coupon_Service -> Coupon_DB: getCoupon()
    alt 발급 가능
        Coupon_Service -> Coupon_DB: addIssuedCoupon(userId)
        Coupon_DB --> Coupon_Service: success
        Coupon_Service --> API_Server: Coupon 발급 성공
    else 발급 마감
        Coupon_Service --> API_Server: 쿠폰 소진 오류
    end
    API_Server --> Client: 결과 응답 (성공 or 실패)
```

</details>

<details>
    <summary>4. 주문 / 결제 기능</summary>

```mermaid
sequenceDiagram
    Actor Client
    Participant API_Server
    Participant Order_Service
    Participant Balance_Service
    Participant Product_Service
    Participant Order_DB
    Participant User_DB
    Participant Product_DB
    Participant External_Data_Platform

    Client -> API_Server: POST /order (userId, [productId, quantity] list)
    API_Server -> Order_Service: placeOrder(userId, productList)

    Order_Service -> Product_Service: validateAndCalculate(productList)
    Product_Service -> Product_DB: getProducts()
    Product_DB --> Product_Service: productInfo
    Product_Service --> Order_Service: totalPrice, stockStatus

    Order_Service -> Balance_Service: hasSufficientBalance(userId, totalPrice)
    Balance_Service -> User_DB: getUserBalance(userId)
    User_DB --> Balance_Service: balance
    Balance_Service --> Order_Service: true / false

    alt 잔액 충분
        Order_Service -> Balance_Service: deductBalance(userId, totalPrice)
        Balance_Service -> User_DB: updateUserBalance(userId)
        User_DB --> Balance_Service: OK

        Order_Service -> Product_Service: reduceStock(productList)
        Product_Service -> Product_DB: updateProduct()
        Product_DB --> Product_Service: OK

        Order_Service -> Order_DB: addOrder()
        Order_DB --> Order_Service: order_id

        Order_Service -> External_Data_Platform: POST /sendOrderData (orderInfo)
        External_Data_Platform --> Order_Service: 200 OK

        Order_Service --> API_Server: 주문 완료 (orderId)
    else 잔액 부족
        Order_Service --> API_Server: 400 Bad Request (잔액 부족)
    end

    API_Server --> Client: 주문 결과 응답
```

</details>

<details>
    <summary>5. 상위 상품 조회</summary>

```mermaid
sequenceDiagram
    Actor Client
    Participant API_Server
    Participant Product_Service
    Participant Order_DB
    Participant Product_DB

    Client -> API_Server: GET /topProducts
    API_Server -> Product_Service: getTopSellingProducts()

    Product_Service -> Order_DB: selectProducts()

    Order_DB --> Product_Service: [productId1, productId2, ...]

    Product_Service -> Product_DB: selectProducts()
    Product_DB --> Product_Service: productDetails

    Product_Service --> API_Server: topProducts
    API_Server --> Client: topProductsResponse
```

</details>
</details>

<details>
    <summary>2. ERD</summary>
<img src="./design_erd.png" width="400"/>
</details>

<details>
    <summary>3. Infrastructure Diagram</summary>
    
```mermaid
graph TD
  subgraph "Infrastructure"
    User[사용자] -->|HTTP 요청| API_Server[API_Server]

    subgraph Core Components
      API_Server --> Redis[(Redis Cache)]
      API_Server --> App_Service[Application Logic]
      App_Service --> DB[(MySQL)]
      App_Service --> Kafka["Kafka Producer"]
    end

    Kafka --> KafkaConsumer[Kafka Consumer]
    KafkaConsumer --> DataPlatform[데이터 플랫폼]

    subgraph Redis Use Cases
      Redis -.-> ProductCache[상품 조회 캐시]
    end

end

classDef infra fill:#D3D3D3,stroke:#333,stroke-width:1px,color:#000000;
class Kafka,KafkaConsumer,Redis infra;

```
</details>
```
