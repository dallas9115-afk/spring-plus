package org.example.expert.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("bulk")
@RequiredArgsConstructor
public class UserBulkInsertRunner implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    private static final int TOTAL_USERS = 5_000_000;
    private static final int BATCH_SIZE = 1_000;

    @Override
    public void run(ApplicationArguments args) {
        log.info("=== Starting bulk insert of {} users ===", TOTAL_USERS);
        long startTime = System.currentTimeMillis();

        String sql = "INSERT INTO users (email, password, user_role, nickname) VALUES (?, ?, ?, ?)";

        for (int i = 0; i < TOTAL_USERS; i += BATCH_SIZE) {
            final int batchStart = i;
            int batchEnd = Math.min(i + BATCH_SIZE, TOTAL_USERS);
            final int currentBatchSize = batchEnd - batchStart;

            jdbcTemplate.batchUpdate(sql, new org.springframework.jdbc.core.BatchPreparedStatementSetter() {
                @Override
                public void setValues(@org.springframework.lang.NonNull java.sql.PreparedStatement ps, int j) throws java.sql.SQLException {
                    int index = batchStart + j;
                    ps.setString(1, "user" + index + "@bulk.com");
                    ps.setString(2, "BulkPass1!");
                    ps.setString(3, "USER");
                    ps.setString(4, "user_" + index);
                }

                @Override
                public int getBatchSize() {
                    return currentBatchSize;
                }
            });

            if ((i + BATCH_SIZE) % 100_000 == 0) {
                log.info("Inserted {} / {} users...", Math.min(i + BATCH_SIZE, TOTAL_USERS), TOTAL_USERS);
            }
        }

        long elapsed = System.currentTimeMillis() - startTime;
        log.info("=== Bulk insert completed in {} ms ({} seconds) ===", elapsed, elapsed / 1000);
    }
}
