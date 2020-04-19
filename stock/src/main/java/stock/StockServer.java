package stock;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServer;
import rx.Observable;
import stock.entities.Company;

import java.util.*;
import java.util.stream.Collectors;

public class StockServer {

    // можно хранить информацию в MongoDB или любой другой БД, но это чуть дольше и уже отработано в предыдущих ДЗ
    Map<Integer, Company> companies = new HashMap<>();
    Random random = new Random();

    public void run() {
        HttpServer.newServer(8080).start((req, resp) -> {
            Observable<String> response;
            String action = req.getDecodedPath().substring(1);
            Map<String, List<String>> queryParam = req.getQueryParameters();
            switch (action) {
                case "addCompany":
                    response = addCompany(queryParam);
                    resp.setStatus(HttpResponseStatus.OK);
                    break;
                case "getSharesInfo":
                    response = getSharesInfo(queryParam);
                    resp.setStatus(HttpResponseStatus.OK);
                    break;
                case "buyShares":
                    response = buyShares(queryParam);
                    resp.setStatus(HttpResponseStatus.OK);
                    break;
                case "sellShares":
                    response = sellShares(queryParam);
                    resp.setStatus(HttpResponseStatus.OK);
                    break;
                case "changePrice":
                    response = changePrice(queryParam);
                    resp.setStatus(HttpResponseStatus.OK);
                    break;
                default:
                    response = Observable.just("Wrong command");
                    resp.setStatus(HttpResponseStatus.BAD_REQUEST);
            }
            return resp.writeString(response);
        }).awaitShutdown();
    }

    private Observable<String> addCompany(Map<String, List<String>> queryParam) {
        ArrayList<String> required = new ArrayList<>(Arrays.asList("id", "price", "amount"));
        if (!isCompleteRequest(queryParam, required)) {
            return Observable.just(buildError(queryParam, required));
        }
        int id = Integer.parseInt(queryParam.get("id").get(0));
        if (companies.containsKey(id)) {
            return Observable.just("This company is already in stock.");
        }
        double price = Integer.parseInt(queryParam.get("price").get(0));
        int amount = Integer.parseInt(queryParam.get("amount").get(0));
        companies.put(id, new Company(id, price, amount));
        return Observable.just("ok");
    }

    private Observable<String> getSharesInfo(Map<String, List<String>> queryParam) {
        ArrayList<String> required = new ArrayList<>(Collections.singletonList("id"));
        if (!isCompleteRequest(queryParam, required)) {
            return Observable.just(buildError(queryParam, required));
        }
        int id = Integer.parseInt(queryParam.get("id").get(0));
        if (!companies.containsKey(id)) {
            return Observable.just("This company is not in stock yet.");
        }
        return Observable.just(companies.get(id).share.toString());
    }

    private Observable<String> buyShares(Map<String, List<String>> queryParam) {
        ArrayList<String> required = new ArrayList<>(Arrays.asList("id", "amount"));
        if (!isCompleteRequest(queryParam, required)) {
            return Observable.just(buildError(queryParam, required));
        }
        int id = Integer.parseInt(queryParam.get("id").get(0));
        if (!companies.containsKey(id)) {
            return Observable.just("This company is not in stock yet.");
        }
        int amount = Integer.parseInt(queryParam.get("amount").get(0));
        Company company = companies.get(id);
        if (company.share.amount < amount) {
            return Observable.just("There are only " + company.share.amount + " shares on the stock");
        }
        company.share.amount -= amount;
        return Observable.just("ok");
    }

    private Observable<String> sellShares(Map<String, List<String>> queryParam) {
        ArrayList<String> required = new ArrayList<>(Arrays.asList("id", "amount"));
        if (!isCompleteRequest(queryParam, required)) {
            return Observable.just(buildError(queryParam, required));
        }
        int id = Integer.parseInt(queryParam.get("id").get(0));
        if (!companies.containsKey(id)) {
            return Observable.just("This company is not in stock yet.");
        }
        int amount = Integer.parseInt(queryParam.get("amount").get(0));
        Company company = companies.get(id);
        company.share.amount += amount;
        return Observable.just("ok");
    }

    private Observable<String> changePrice(Map<String, List<String>> queryParam) {
        ArrayList<String> required = new ArrayList<>(Collections.singletonList("id"));
        if (!isCompleteRequest(queryParam, required)) {
            return Observable.just(buildError(queryParam, required));
        }
        int id = Integer.parseInt(queryParam.get("id").get(0));
        if (!companies.containsKey(id)) {
            return Observable.just("This company is not in stock yet.");
        }
        Company company = companies.get(id);
        double deltaChange = random.nextGaussian() * company.share.price;
        company.share.price += deltaChange;
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



