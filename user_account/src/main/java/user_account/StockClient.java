package user_account;

import user_account.entities.Share;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class StockClient {

    private final static HttpClient HTTP_CLIENT = HttpClient.newBuilder().build();
    private final static String HOST_URL = "http://localhost:8080/";

    private String sendRequest(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(path)).build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public Share getShareInfo(int companyId) throws IOException, InterruptedException {
        String response = sendRequest(HOST_URL + "getSharesInfo" + "?id=" + companyId);
        if (!response.contains("{")) {
            return null;
        }
        response = response.substring(response.indexOf("{") + 1, response.lastIndexOf("}"));
        String[] parts = response.split(",");
        int amount = Integer.parseInt(parts[0].substring(parts[0].indexOf('=') + 1));
        double price = Double.parseDouble(parts[1].substring(parts[1].indexOf('=') + 1));
        return new Share(amount, price);
    }

    public void buyShares(int shareId, int count) throws IOException, InterruptedException {
        sendRequest(HOST_URL + "buyShares?id=" + shareId + "&shares=" + count);
    }

    public void sellShares(int shareId, int count) throws IOException, InterruptedException {
        sendRequest(HOST_URL + "sellShares?id=" + shareId + "&shares=" + count);
    }
}
