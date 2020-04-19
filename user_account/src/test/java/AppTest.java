import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import user_account.UserServer;
import user_account.entities.Share;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppTest {

    @ClassRule
    public static GenericContainer simpleWebServer
            = new FixedHostPortGenericContainer("stock:1.0-SNAPSHOT")
            .withFixedExposedPort(8080, 8080)
            .withExposedPorts(8080);

    private final static String STOCK_SOCKET = "http://localhost:8080/";

    private final static HttpClient HTTP_CLIENT = HttpClient.newBuilder().build();

    private final static UserServer USER_SERVER = new UserServer();


    private String sendRequest(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(path)).build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    @Before
    public void initiate() throws InterruptedException, IOException {
        sendRequest(STOCK_SOCKET + "addCompany?id=1&price=10&amount=10");
        sendRequest(STOCK_SOCKET + "addCompany?id=2&price=100&amount=100");
        Map<String, List<String>> params = new HashMap<>();
        params.put("id", Collections.singletonList("1"));
        params.put("balance", Collections.singletonList("50"));
        USER_SERVER.addUser(params);
        params.replace("id", Collections.singletonList("2"));
        params.replace("balance", Collections.singletonList("100000000"));
        USER_SERVER.addUser(params);
        params.remove("balance");
        params.remove("id");
        params.put("userId", Collections.singletonList("2"));
        params.put("companyId", Collections.singletonList("2"));
        params.put("amount", Collections.singletonList("100"));
        USER_SERVER.buyShares(params);
    }

    @Test
    public void testAddingCompany() throws InterruptedException, IOException {
        Assert.assertEquals("ok",
                sendRequest(STOCK_SOCKET + "addCompany?id=3&price=10&amount=0"));
        Assert.assertEquals("This company is already in stock.",
                sendRequest(STOCK_SOCKET + "addCompany?id=3&price=10&amount=0"));
    }

    @Test
    public void testGettingSharesInfo() throws InterruptedException, IOException {
        Share share = new Share(10, 10);
        Assert.assertEquals(share.toString(),
                sendRequest(STOCK_SOCKET + "getSharesInfo?id=1"));
        Assert.assertEquals("This company is not in stock yet.",
                sendRequest(STOCK_SOCKET + "getSharesInfo?id=10"));
    }

    @Test
    public void testBuyingShares() throws InterruptedException, IOException {
        Map<String, List<String>> params = new HashMap<>();
        params.put("userId", Collections.singletonList("1"));
        params.put("companyId", Collections.singletonList("1"));
        params.put("amount", Collections.singletonList("5"));
        USER_SERVER.buyShares(params).test().assertValue("ok");
        USER_SERVER.buyShares(params).test().assertValue("User doesn't have enough money for purchase");
        params.replace("userId", Collections.singletonList("2"));
        params.replace("amount", Collections.singletonList("100"));
        USER_SERVER.buyShares(params).test().assertValue("Company doesn't have this amount of shares");
    }

    @Test
    public void testSellingShares() throws InterruptedException, IOException {
        Map<String, List<String>> params = new HashMap<>();
        params.put("userId", Collections.singletonList("2"));
        params.put("companyId", Collections.singletonList("2"));
        params.put("amount", Collections.singletonList("100"));
        USER_SERVER.sellShares(params).test().assertValue("ok");
        USER_SERVER.sellShares(params).test().assertValue("User doesn't have this amount of shares");
    }

    @Test
    public void testBalance() throws InterruptedException, IOException {
        Map<String, List<String>> params = new HashMap<>();
        params.put("id", Collections.singletonList("1"));
        USER_SERVER.getBalance(params).test().assertValue(Double.toString(50));
        params.put("id", Collections.singletonList("2"));
        USER_SERVER.getBalance(params).test().assertValue(Double.toString(100000000));
    }

}