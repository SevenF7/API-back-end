package apiwork.utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class RequestMonitor {

    private ConcurrentHashMap<Long, Long> requestTimes = new ConcurrentHashMap<>();

    @Scheduled(fixedRate = 3600000) // 每个小时清理一次请求时间记录
    private void cleanExpiredRequests() {
        long now = System.currentTimeMillis();
        requestTimes.forEach((requestId, startTime) -> {
            if (now - startTime > TimeUnit.HOURS.toMillis(1)) {
                requestTimes.remove(requestId);
            }
        });
    }

    public void logRequestStart(Long requestId) {
        requestTimes.put(requestId, System.currentTimeMillis());
    }

    public long logRequestEnd(Long requestId) {
        Long startTime = requestTimes.get(requestId); // 请求开始时间
        if (startTime != null) {
            long endTime = System.currentTimeMillis(); // 请求结束时间
            long duration = endTime - startTime; // 请求持续时间
            requestTimes.remove(requestId);
            return duration;
        }
        return 0;
    }
}
