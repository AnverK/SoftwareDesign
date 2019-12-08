package ru.ifmo.rain.khusainov.mock_testing.vk;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class VKManager {
    private final VKClient client;
    private final static long SECONDS_PER_HOUR = TimeUnit.HOURS.toSeconds(1);

    public VKManager(VKClient client) {
        this.client = client;
    }

    public List<Long> getPostsCountPerHours(String hashtag, int n) {
        Map<Long, Long> histogram = client.getInfo(hashtag, n)
                .stream()
                .collect(Collectors.groupingBy(
                        info -> info.date / SECONDS_PER_HOUR,
                        TreeMap::new,
                        Collectors.counting()));
        long startTime = Instant.now().getEpochSecond();
        for (int i = 0; i < n; i++) {
            histogram.putIfAbsent(startTime / SECONDS_PER_HOUR - i, 0L);
        }
        return new ArrayList<>(histogram.values());
    }
}
