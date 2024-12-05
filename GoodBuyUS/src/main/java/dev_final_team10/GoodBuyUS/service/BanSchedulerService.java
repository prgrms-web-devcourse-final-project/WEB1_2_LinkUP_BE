package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BanSchedulerService {

    private final AdminUserService adminUserService;

    /**
     * 1분마다 정지 해제 프로세스 실행
     */
    @Scheduled(cron = "0 */1 * * * *") // 매 1분마다 실행
    public void scheduleAutoUnban() {
        log.info("정지 해제 스케줄러 실행 시작");
        adminUserService.autoUnbanExpiredUsers();
        log.info("정지 해제 스케줄러 실행 완료");
    }
}
