package stock.entities;

public class Company {
    int id;
    public Share share;

    public Company(int id, double price, int amount) {
        this.id = id;
        this.share = new Share(amount, price);
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", share=" + share +
                '}';
    }
}