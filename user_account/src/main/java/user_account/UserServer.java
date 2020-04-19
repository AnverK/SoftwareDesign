package user_account;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServer;
import rx.Observable;
import user_account.entities.Share;
import user_account.entities.UserAccount;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class UserServer {
    StockClient stockClient = new StockClient();
    Map<Integer, UserAccount> users = new HashMap<>();

    public void run() {
        HttpServer.newServer(8081).start((req, resp) -> {
            Observable<String> response;
            String action = req.getDecodedPath().substring(1);
            Map<String, List<String>> queryParam = req.getQueryParameters();
            try {
                switch (action) {
                    case "addUser":
                        response = addUser(queryParam);
                        resp.setStatus(HttpResponseStatus.OK);
                        break;
                    case "depositMoney":
                        response = depositMoney(queryParam);
                        resp.setStatus(HttpResponseStatus.OK);
                        break;
                    case "getSharesInfo":
                        response = getSharesInfo(queryParam);
                        resp.setStatus(HttpResponseStatus.OK);
                        break;
                    case "getBalance":
                        response = getBalance(queryParam);
                        resp.setStatus(HttpResponseStatus.OK);
                        break;
                    case "buyShare":
                        response = buyShares(queryParam);
                        resp.setStatus(HttpResponseStatus.OK);
                        break;
                    case "sellShare":
                        response = sellShares(queryParam);
                        resp.setStatus(HttpResponseStatus.OK);
                        break;
                    default:
                        response = Observable.just("Wrong command");
                        resp.setStatus(HttpResponseStatus.BAD_REQUEST);
                }
            } catch (Exception e) {
                response = Observable.just("Error occurred");
                resp.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
            }
            return resp.writeString(response);
        }).awaitShutdown();
    }

    public Observable<String> addUser(Map<String, List<String>> queryParam) {
        ArrayList<String> required = new ArrayList<>(Arrays.asList("id", "balance"));
        if (!isCompleteRequest(queryParam, required)) {
            return Observable.just(buildError(queryParam, required));
        }
        int id = Integer.parseInt(queryParam.get("id").get(0));
        if (users.containsKey(id)) {
            return Observable.just("This user is already exists.");
        }
        double balance = Double.parseDouble(queryParam.get("balance").get(0));
        users.put(id, new UserAccount(balance));
        return Observable.just("ok");
    }

    public Observable<String> depositMoney(Map<String, List<String>> queryParam) {
        ArrayList<String> required = new ArrayList<>(Arrays.asList("id", "deposit"));
        if (!isCompleteRequest(queryParam, required)) {
            return Observable.just(buildError(queryParam, required));
        }
        int id = Integer.parseInt(queryParam.get("id").get(0));
        if (!users.containsKey(id)) {
            return Observable.just("This user doesn't exist.");
        }
        double money = Double.parseDouble(queryParam.get("deposit").get(0));
        UserAccount user = users.get(id);
        user.balance += money;
        return Observable.just(Double.toString(user.balance));
    }

    public Observable<String> getSharesInfo(Map<String, List<String>> queryParam) throws IOException, InterruptedException {
        ArrayList<String> required = new ArrayList<>(Collections.singletonList("id"));
        if (!isCompleteRequest(queryParam, required)) {
            return Observable.just(buildError(queryParam, required));
        }
        int id = Integer.parseInt(queryParam.get("id").get(0));
        if (!users.containsKey(id)) {
            return Observable.just("This user doesn't exist.");
        }
        UserAccount user = users.get(id);
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Integer, Integer> kv : user.shares.entrySet()) {
            stringBuilder.append(stockClient.getShareInfo(kv.getKey())).append("\n");
        }
        return Observable.just(stringBuilder.toString());
    }

    public Observable<String> getBalance(Map<String, List<String>> queryParam) throws IOException, InterruptedException {
        ArrayList<String> required = new ArrayList<>(Collections.singletonList("id"));
        if (!isCompleteRequest(queryParam, required)) {
            return Observable.just(buildError(queryParam, required));
        }
        int id = Integer.parseInt(queryParam.get("id").get(0));
        if (!users.containsKey(id)) {
            return Observable.just("This user doesn't exist.");
        }
        UserAccount user = users.get(id);
        double total = user.balance;
        for (Map.Entry<Integer, Integer> kv : user.shares.entrySet()) {
            total += kv.getValue() * stockClient.getShareInfo(kv.getKey()).price;
        }
        return Observable.just(Double.toString(total));
    }

    public Observable<String> buyShares(Map<String, List<String>> queryParam) throws IOException, InterruptedException {
        ArrayList<String> required = new ArrayList<>(Arrays.asList("userId", "companyId", "amount"));
        if (!isCompleteRequest(queryParam, required)) {
            return Observable.just(buildError(queryParam, required));
        }
        int userId = Integer.parseInt(queryParam.get("userId").get(0));
        if (!users.containsKey(userId)) {
            return Observable.just("This user doesn't exist.");
        }
        int companyId = Integer.parseInt(queryParam.get("companyId").get(0));
        int amount = Integer.parseInt(queryParam.get("amount").get(0));
        Share shareInfo = stockClient.getShareInfo(companyId);
        if (shareInfo == null) {
            return Observable.just("This company is not in stock yet");
        }
        UserAccount user = users.get(userId);
        if (shareInfo.price * amount > user.balance) {
            return Observable.just("User doesn't have enough money for purchase");
        }
        if (amount > shareInfo.amount) {
            return Observable.just("Company doesn't have this amount of shares");
        }
        stockClient.buyShares(companyId, amount);
        user.balance -= shareInfo.price * amount;
        user.shares.put(companyId, user.shares.getOrDefault(companyId, 0) + amount);
        return Observable.just("ok");
    }

    public Observable<String> sellShares(Map<String, List<String>> queryParam) throws IOException, InterruptedException {
        ArrayList<String> required = new ArrayList<>(Arrays.asList("userId", "companyId", "amount"));
        if (!isCompleteRequest(queryParam, required)) {
            return Observable.just(buildError(queryParam, required));
        }
        int userId = Integer.parseInt(queryParam.get("userId").get(0));
        if (!users.containsKey(userId)) {
            return Observable.just("This user doesn't exist.");
        }
        int companyId = Integer.parseInt(queryParam.get("companyId").get(0));
        int amount = Integer.parseInt(queryParam.get("amount").get(0));
        UserAccount user = users.get(userId);
        if (amount > user.shares.getOrDefault(companyId, 0)) {
            return Observable.just("User doesn't have this amount of shares");
        }
        double price = stockClient.getShareInfo(companyId).price;
        stockClient.sellShares(companyId, amount);
        user.balance += price * amount;
        user.shares.put(companyId, user.shares.getOrDefault(companyId, 0) - amount);
        return Observable.just("ok");
    }

    private static boolean isCompleteRequest(Map<String, List<String>> queryParam, List<String> required) {
        for (String value : required) {
            if (!queryParam.containsKey(value)) {
                return false;
            }
        }
        return true;
    }

    private static String buildError(Map<String, List<String>> queryParam, List<String> required) {
        List<String> missedAttributes = required.stream().filter(val -> !queryParam.containsKey(val))
                .collect(Collectors.toList());
        return "Missed attributes: " + String.join(", ", missedAttributes);
    }
}