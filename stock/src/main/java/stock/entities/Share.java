package stock.entities;

public class Share {
    public int amount;
    public double price;

    public Share(int amount, double price) {
        this.amount = amount;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Share{" +
                "amount=" + amount +
                ", price=" + price +
                '}';
    }
}
