package reactive_mongo_driver;

import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class Good {
    private static final String RUB = "rub";
    private static final String USD = "usd";
    private static final String EUR = "eur";
    private int id;
    private String name;
    private Map<String, String> currencyMap;

    public Good(Document doc) {
        this(doc.getInteger("id"), doc.getString("name"), doc.getString(RUB), doc.getString(USD), doc.getString(EUR));
    }

    public Good(int id, String name, String rub, String usd, String eur) {
        this.id = id;
        this.name = name;
        this.currencyMap = new HashMap<>();
        currencyMap.put(RUB, rub);
        currencyMap.put(USD, usd);
        currencyMap.put(EUR, eur);
    }

    public Document getDocument() {
        return new Document("id", id)
                .append("name", name)
                .append(RUB, currencyMap.get(RUB))
                .append(USD, currencyMap.get(USD))
                .append(EUR, currencyMap.get(EUR));
    }

    public String priceString(String currency) {
        return currencyMap.get(currency);
    }

    public String priceString() {
        StringBuilder pricesBuilder = new StringBuilder();
        for (Map.Entry<String, String> price : currencyMap.entrySet()) {
            pricesBuilder.append(price.getKey()).append(": ").append(price.getValue()).append(", ");
        }
        String prices = pricesBuilder.toString();
        return prices.substring(0, prices.length() - 2);
    }

    public String genericString(String priceString) {
        return "Good{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + priceString +
                '}';
    }

    public String toString(String currency) {
        return genericString(priceString(currency));
    }

    @Override
    public String toString() {
        return genericString(priceString());
    }
}
