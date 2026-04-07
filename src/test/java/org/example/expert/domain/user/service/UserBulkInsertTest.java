package org.example.expert.domain.user.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
@Transactional
@Rollback
class UserBulkInsertTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final int TOTAL_USERS = 5_000_000;
    private static final int BATCH_SIZE = 1_000;

    @Test
    void bulkInsertUsers() {
        log.info("=== Starting bulk insert of {} users ===", TOTAL_USERS);
        long startTime = System.currentTimeMillis();

        String sql = "INSERT INTO users (email, password, user_role, nickname) VALUES (?, ?, ?, ?)";

        for (int i = 0; i < TOTAL_USERS; i += BATCH_SIZE) {
            final int batchStart = i;
            int batchEnd = Math.min(i + BATCH_SIZE, TOTAL_USERS);
            final int currentBatchSize = batchEnd - batchStart;

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@NonNull PreparedStatement ps, int j) throws SQLException {
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

        assertTrue(elapsed > 0, "Bulk insert should have taken some time");
    }
}
