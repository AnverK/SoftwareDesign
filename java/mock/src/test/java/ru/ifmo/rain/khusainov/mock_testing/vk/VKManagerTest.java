package ru.ifmo.rain.khusainov.mock_testing.vk;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.when;

public class VKManagerTest {
    private VKManager vkManager;
    private final static long SECONDS_PER_HOUR = TimeUnit.HOURS.toSeconds(1);

    @Mock
    private VKClient client;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vkManager = new VKManager(client);
    }

    @Test
    public void getPostsCountPerHours() {
        String hashtag = "котик";
        int n = 4;
        when(client.getInfo(hashtag, n))
                .thenReturn(createAnswer());

        List<Long> counts = vkManager.getPostsCountPerHours(hashtag, n);
        Assert.assertEquals(Arrays.asList(0L, 0L, 2L, 1L), counts);
    }

    private List<VKInfo> createAnswer() {
        long hour1 = Instant.now().getEpochSecond();
        long hour2 = Instant.now().getEpochSecond() - SECONDS_PER_HOUR;
        long also_hour2 = hour2 + 1;
        if (also_hour2 / SECONDS_PER_HOUR > hour2 / SECONDS_PER_HOUR) {
            also_hour2 -= 2;
        }

        return Arrays.asList(
                new VKInfo(1, 1, hour1),
                new VKInfo(2, 1, also_hour2),
                new VKInfo(2, 3, hour2)
        );
    }
}
