package kr.hhplus.be.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
    "kr.hhplus.be.server.user",
    "kr.hhplus.be.server.balance",
    "kr.hhplus.be.server.coupon",
    "kr.hhplus.be.server.usercoupon",
    "kr.hhplus.be.server.order",
    "kr.hhplus.be.server.product"
})
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
