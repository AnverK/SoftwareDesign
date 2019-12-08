package ru.ifmo.rain.khusainov.mock_testing.vk;

import ru.ifmo.rain.khusainov.mock_testing.http.UrlReader;
import ru.ifmo.rain.khusainov.mock_testing.vk.VKInfo;
import ru.ifmo.rain.khusainov.mock_testing.vk.VKResponseParser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VKClient {
    private final String host;
    private final VKResponseParser parser;
    private final UrlReader reader;

    public VKClient(String host) {
        this.host = host;
        this.parser = new VKResponseParser();
        this.reader = new UrlReader();
    }

    public List<VKInfo> getInfo(String hashtag, int n) {
        String response = reader.readAsText(createUrl(hashtag, n));
        return parser.parseResponse(response);
    }

    private String createUrl(String hashtag, int n) {
        String encoded_hashtag = hashtag;
        try {
            encoded_hashtag = URLEncoder.encode("#" + hashtag, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException ignored) {
        }
        long startTime = Instant.now().getEpochSecond() - n * TimeUnit.HOURS.toSeconds(n);
        return "https://" + host + "/method/newsfeed.search?" +
                "start_time=" + startTime +
                "&count=200&q=" + encoded_hashtag +
                "&access_token=5e4212b15e4212b15e4212b1ab5e2f934555e425e4212b103e91d12e06aa9981c689c60&v=5.102";
    }
}
