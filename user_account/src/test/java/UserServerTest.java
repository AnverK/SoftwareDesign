import org.junit.Test;
import user_account.UserServer;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServerTest {
    private final static UserServer USER_SERVER = new UserServer();

    @Test
    public void addUser() {
        Map<String, List<String>> params = new HashMap<>();
        params.put("id", Collections.singletonList("1"));
        params.put("balance", Collections.singletonList("0"));
        USER_SERVER.addUser(params).test().assertValue("ok");
        USER_SERVER.addUser(params).test().assertValue("This user is already exists.");
    }

    @Test
    public void depositMoney() {
        Map<String, List<String>> params = new HashMap<>();
        params.put("id", Collections.singletonList("1"));
        params.put("balance", Collections.singletonList("0"));
        USER_SERVER.addUser(params);
        params.put("deposit", Collections.singletonList("100"));
        params.remove("balance");
        USER_SERVER.depositMoney(params).test().assertValue(Double.toString(100));
    }
}
