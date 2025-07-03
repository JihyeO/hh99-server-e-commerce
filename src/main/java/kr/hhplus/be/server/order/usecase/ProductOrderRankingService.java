package kr.hhplus.be.server.order.usecase;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * 레디스를 사용해 가장 많이 주문된 제품의 순위를 일/주/월 단위로 매기는 서비스입니다.
 */
@Service
@RequiredArgsConstructor
public class ProductOrderRankingService {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 제품 주문 점수를 증가시키고, 일/주/월 단위로 순위를 매깁니다.
     * 
     * @param productId 제품 ID
     */
    public void increaseProductScore(String productId) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();

        // 일/주/월별로 각각 증가
        zSetOps.incrementScore(getTodayKey(), productId, 1.0);
        zSetOps.incrementScore(getCurrentWeekKey(), productId, 1.0);
        zSetOps.incrementScore(getCurrentMonthKey(), productId, 1.0);

        // 키별 TTL 설정: 일간은 30일, 주간은 12주, 월간은 12개월 유지
        setTTL(getTodayKey(), Duration.ofDays(30));
        setTTL(getCurrentWeekKey(), Duration.ofDays(90));
        setTTL(getCurrentMonthKey(), Duration.ofDays(365));
    }

    /**
     * 오늘의 상위 N개 제품을 조회합니다.
     * 
     * @param key Redis 키
     * @param topN 상위 N개
     * @return 상위 N개 제품의 Set
     */
    public Set<ZSetOperations.TypedTuple<String>> getTopProducts(String key, int topN) {
        return redisTemplate.opsForZSet()
            .reverseRangeWithScores(key, 0, topN - 1);
    }

    /**
     * 오늘의 상위 N개 제품을 조회하기 위한 Redis 키를 생성합니다.
     * 
     * @param topN 상위 N개
     * @return Redis 키
     */
    public String getTodayKey() {
        return "product:order:daily:" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); // yyyyMMdd
    }

    /**
     * 이번주의 상위 N개 제품을 조회하기 위한 Redis 키를 생성합니다.
     * 
     * @return Redis 키
     */
    public String getCurrentWeekKey() {
        LocalDate now = LocalDate.now();
        WeekFields weekFields = WeekFields.ISO;
        int weekNumber = now.get(weekFields.weekOfWeekBasedYear());
        int year = now.get(weekFields.weekBasedYear());
        return "product:order:weekly:" + String.format("%d%02d", year, weekNumber);
    }

    /**
     * 이번 달의 상위 N개 제품을 조회하기 위한 Redis 키를 생성합니다.
     * 
     * @return Redis 키
     */
    public String getCurrentMonthKey() {
        return "product:order:monthly:" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")); // yyyyMM
    }

    /**
     * Redis 키에 TTL을 설정합니다.
     * 
     * @param key Redis 키
     * @param ttl TTL 기간
     */
    private void setTTL(String key, Duration ttl) {
        redisTemplate.expire(key, ttl);
    }
}
