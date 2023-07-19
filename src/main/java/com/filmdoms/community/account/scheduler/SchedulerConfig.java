package com.filmdoms.community.account.scheduler;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@EnableScheduling
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @Scheduled(cron = "0 0 3 * * *")
    public void accountDeleteScheduler() {
        Optional<List<Account>> deletedAccounts = accountRepository.findDeletedAccounts();

        if (deletedAccounts.isPresent()) {
            List<Account> deletedList = deletedAccounts.get();
            deletedList.stream().filter(account -> {
                long daysBetween = ChronoUnit.DAYS.between(account.getDeleteRegisteredDate(), LocalDateTime.now());
                return daysBetween >= 7;
            }).forEach(account -> accountService.deleteExpiredAccount(account));
        }


    }
}
