import http from "k6/http";
import { check, sleep } from "k6";

export let options = {
  vus: 10, // 동시에 10명 요청
  duration: "10s",
};

export default function () {
  const url = "http://localhost:8080/order";
  const payload = JSON.stringify({
    userId: 1,
    userCouponId: null,
    status: "ORDERED",
    orderDate: "2024-06-12T15:00:00",
    items: [{ productId: 1, quantity: 2 }],
  });

  const params = { headers: { "Content-Type": "application/json" } };

  let res = http.post(url, payload, params);
  check(res, { "status is 200": (r) => r.status === 200 });
  sleep(0.1);
}
