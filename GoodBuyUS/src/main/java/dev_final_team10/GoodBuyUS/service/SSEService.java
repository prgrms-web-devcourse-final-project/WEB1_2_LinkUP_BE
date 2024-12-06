package dev_final_team10.GoodBuyUS.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class SSEService {

    private final ConcurrentMap<String, Sinks.Many<String>> sinks = new ConcurrentHashMap<>();

    // SSE 구독 생성
    public Flux<String> subscribe(String key) {
        Sinks.Many<String> sink = sinks.computeIfAbsent(key, k -> Sinks.many().multicast().onBackpressureBuffer());
        return sink.asFlux();
    }

    // SSE 알림 전송
    public void sendEvent(String key, String message) {
        if (message == null || message.isEmpty()) {
            return;
        }
        Sinks.Many<String> sink = sinks.get(key);
        if (sink != null) {
            sink.tryEmitNext(message);
        }
    }

    // SSE 구독 삭제
    public void remove(String key) {
        sinks.remove(key);
    }
}
