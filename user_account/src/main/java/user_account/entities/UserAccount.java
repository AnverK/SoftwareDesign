package user_account.entities;

import java.util.HashMap;
import java.util.Map;

public class UserAccount {
    public double balance;
    public Map<Integer, Integer> shares = new HashMap<>();

    public UserAccount(double balance) {
        this.balance = balance;
    }
}