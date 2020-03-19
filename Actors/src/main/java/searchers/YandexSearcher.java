package searchers;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import responses.SingleResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;


public class YandexSearcher extends AbstractSearcher {
    private static final String SEARCHER_NAME = "Yandex";
    private static final String YANDEX_QUERY = "https://www.yandex.ru/search/?text=";
    private String query;

    public YandexSearcher(String query) {
        this.query = query;
    }

    @Override
    public List<SingleResponse> search() throws IOException {
        Elements links = Jsoup.connect(getUrlForSearch(query))
                .userAgent(USER_AGENT)
                .get().select("div h2");
        return extractLinks(links);
    }

    private String getUrlForSearch(String query) throws UnsupportedEncodingException {
        return YANDEX_QUERY + URLEncoder.encode(query, "UTF-8") + String.format("&numdoc=%d", RESULTS_NUM);
    }

    @Override
    public String getSearcherName() {
        return SEARCHER_NAME;
    }
}