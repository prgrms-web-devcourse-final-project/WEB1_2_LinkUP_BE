package dev_final_team10.GoodBuyUS.controller.api;

import dev_final_team10.GoodBuyUS.service.SSEService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/v1/sse")
public class SSEController {

    private final SSEService sseService;

    public SSEController(SSEService sseService) {
        this.sseService = sseService;
    }

    // SSE 구독 엔드포인트
    @GetMapping(value = "/events/{key}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> subscribe(@PathVariable String key) {
        return sseService.subscribe(key);
    }
}
