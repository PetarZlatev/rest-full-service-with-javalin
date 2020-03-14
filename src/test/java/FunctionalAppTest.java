import com.revolut.moneytransfer.App;
import com.revolut.moneytransfer.HibernateUtil;
import com.revolut.moneytransfer.domain.AccountRepository;
import com.revolut.moneytransfer.rest.AccountController;
import com.revolut.moneytransfer.rest.AccountResponse;
import com.revolut.moneytransfer.rest.CreateAccountRequest;
import kong.unirest.HttpResponse;
import kong.unirest.json.JSONObject;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static kong.unirest.Unirest.get;
import static kong.unirest.Unirest.post;
import static org.assertj.core.api.Assertions.assertThat;

public class FunctionalAppTest {

    private App app;
    private AccountController accountController;

    @BeforeEach
    private void SetUpEach() {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        AccountRepository repository = new AccountRepository(sf);
        accountController = new AccountController(repository);
        app = new App(accountController);
        app.start(1234);

        try { // the server can't start quickly, and the tests fail
            // That's why this
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    private void CleanUpEach() {
        app.stop();
    }

    @Test
    public void POST_accounts_empty_holder_returns_bad_request() {
        HttpResponse<String> response = post("http://localhost:1234/accounts")
                .body("{\"holder\":\"\":\"initialBalance\":0}")
                .asString();
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void POST_accounts_negative_balance_returns_bad_request() {
        HttpResponse<String> response = post("http://localhost:1234/accounts")
                .body("{\"holder\":\"Max\":\"initialBalance\":-10}")
                .asString();
        assertThat(response.getStatus()).isEqualTo(400);
    }


    @Test
    public void POST_accounts_create_account() {
        HttpResponse<String> response = post("http://localhost:1234/accounts")
                .body("{\"holder\":\"Max\",\"initialBalance\":10}")
                .asString();
        assertThat(response.getStatus()).isEqualTo(201);
        String body = response.getBody();
        JSONObject obj = new JSONObject(body);
        assertThat(obj.getString("id")).isNotEmpty();
        assertThat(obj.getString("holder")).isEqualTo("Max");
        assertThat(obj.getString("balance")).isEqualTo("10");
    }


    @Test
    public void GET_accounts() {
        CreateAccountRequest accountRequest = new CreateAccountRequest();
        accountRequest.setHolder("Max");
        accountRequest.setInitialBalance(11);
        AccountResponse account = accountController.createAccount(accountRequest);

        HttpResponse<String> response = get("http://localhost:1234/accounts/" + account.getId())
                .asString();
        assertThat(response.getStatus()).isEqualTo(200);
        String body = response.getBody();
        JSONObject obj = new JSONObject(body);
        assertThat(obj.getString("id")).isEqualTo(account.getId().toString());
        assertThat(obj.getString("holder")).isEqualTo("Max");
        assertThat(obj.getString("balance")).isEqualTo("11");
    }


}