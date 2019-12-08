package ru.ifmo.rain.khusainov.mock_testing.vk;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import ru.ifmo.rain.khusainov.mock_testing.rule.HostReachableRule;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@HostReachableRule.HostReachable(VKClientIntegrationTest.HOST)
public class VKClientIntegrationTest {
    public static final String HOST = "api.vk.com";

    @ClassRule
    public static final HostReachableRule rule = new HostReachableRule();


    @Test
    public void getInfo() {
        VKClient client = new VKClient(HOST);
        int n = 10;
        List<VKInfo> infos = client.getInfo("котик", n);
        long endTime = Instant.now().getEpochSecond();
        long startTime = endTime - n * TimeUnit.HOURS.toSeconds(n);

        for (VKInfo info : infos) {
            Assert.assertTrue(info.date >= startTime && info.date <= endTime);
        }
    }
}