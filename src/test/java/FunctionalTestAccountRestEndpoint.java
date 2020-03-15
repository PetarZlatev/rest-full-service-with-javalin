import com.revolut.moneytransfer.App;
import com.revolut.moneytransfer.HibernateUtil;
import com.revolut.moneytransfer.domain.AccountRepository;
import com.revolut.moneytransfer.rest.AccountController;
import com.revolut.moneytransfer.rest.MoneyTransferController;
import com.revolut.moneytransfer.rest.dto.AccountResponse;
import com.revolut.moneytransfer.rest.dto.CreateAccountRequest;
import com.revolut.moneytransfer.service.MoneyTransferService;
import kong.unirest.HttpResponse;
import kong.unirest.json.JSONObject;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static kong.unirest.Unirest.get;
import static kong.unirest.Unirest.post;
import static org.assertj.core.api.Assertions.assertThat;

public class FunctionalTestAccountRestEndpoint {

    private App app;
    private AccountController accountController;
    private MoneyTransferService moneyTransferService;
    private MoneyTransferController moneyTransferController;
    private AccountRepository accountRepository;

    @BeforeEach
    private void SetUpEach() {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        accountRepository = new AccountRepository(sf);
        accountController = new AccountController(accountRepository);
        moneyTransferService = new MoneyTransferService();
        moneyTransferController = new MoneyTransferController(moneyTransferService);
        app = new App(accountController, moneyTransferController);
        app.start(1234);

        try { // FIXME the server can't start quick for the next test and
            // that's why this
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
    public void POST_accounts_empty_request_returns_400() {
        HttpResponse<String> response = post("http://localhost:1234/accounts")
                .body("{}")
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
    public void GET_accounts_unknown_returns_404() {

        HttpResponse<String> response = get("http://localhost:1234/accounts/{transferId}")
                .routeParam("transferId", "7c8e239e-a79a-4b70-916a-4e28e41e997e")
                .asString();
        assertThat(response.getStatus()).isEqualTo(404);
        String body = response.getBody();
        JSONObject obj = new JSONObject(body);
        assertThat(obj.getString("title")).isNotEmpty();

    }

    @Test
    public void GET_accounts_id() {
        CreateAccountRequest accountRequest = new CreateAccountRequest();
        accountRequest.setHolder("Max");
        accountRequest.setInitialBalance(11);
        AccountResponse account = accountController.createAccount(accountRequest);

        HttpResponse<String> response = get("http://localhost:1234/accounts/{accountId}")
                .routeParam("accountId", account.getId().toString())
                .asString();
        assertThat(response.getStatus()).isEqualTo(200);
        String body = response.getBody();
        JSONObject obj = new JSONObject(body);
        assertThat(obj.getString("id")).isEqualTo(account.getId().toString());
        assertThat(obj.getString("holder")).isEqualTo("Max");
        assertThat(obj.getString("balance")).isEqualTo("11");
    }

}