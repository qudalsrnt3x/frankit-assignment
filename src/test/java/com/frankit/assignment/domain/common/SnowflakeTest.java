package com.frankit.assignment.domain.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

class SnowflakeTest {

    private final Snowflake snowflake = new Snowflake();

    @DisplayName("멀티스레드 환경에서도 Snowflake ID는 중복되지 않는다.")
    @Test
    void nextIdTest() throws ExecutionException, InterruptedException {
        // given
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        int repeatCount = 1000;
        int idCount = 1000;

        List<Future<List<Long>>> futures = new ArrayList<>();

        // when
        for (int i = 0; i < repeatCount; i++) {
            futures.add(executorService.submit(() -> generateIdList(snowflake, idCount)));
        }

        // then
        List<Long> result = new ArrayList<>();
        for (Future<List<Long>> future : futures) {
            List<Long> idList = future.get();
            for (int i = 1; i < idList.size(); i++) {
                assertThat(idList.get(i)).isGreaterThan(idList.get(i - 1));
            }
            result.addAll(idList);
        }

        // 전체 고유성 확인
        long total = repeatCount * idCount;
        long distinct = result.stream().distinct().count();
        assertThat(distinct).isEqualTo(total);

        executorService.shutdown();
    }

    List<Long> generateIdList(Snowflake snowflake, int count) {
        List<Long> idList = new ArrayList<>();
        while (count-- > 0) {
            idList.add(snowflake.nextId());
        }
        return idList;
    }

}