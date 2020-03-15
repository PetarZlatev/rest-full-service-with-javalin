import com.revolut.moneytransfer.App;
import com.revolut.moneytransfer.HibernateUtil;
import com.revolut.moneytransfer.domain.Account;
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

public class FunctionalTestTransferRestEndpoint {

    private App app;
    private AccountController accountController;
    private AccountRepository accountRepository;

    @BeforeEach
    private void SetUpEach() {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        accountRepository = new AccountRepository(sf);
        accountController = new AccountController(accountRepository);
        MoneyTransferService moneyTransferService = new MoneyTransferService();
        MoneyTransferController transferController = new MoneyTransferController(moneyTransferService);
        app = new App(accountController, transferController);
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
    public void POST_empty_transfer_request_returns_400() {
        HttpResponse<String> response = post("http://localhost:1234/transfers")
                .body("{}")
                .asString();
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void POST_transfer_money_and_returns_transfer() {

        Account payer = accountRepository.createAccount(new Account("payer", 100));
        Account beneficiary = accountRepository.createAccount(new Account("Beneficiary", 0));

        String sb = "{\"payerAccountId\":\"" + payer.getId() + "\"," +
                "\"beneficiaryAccountId\":\"" + beneficiary.getId() + "\"," +
                "\"amount\":2}";
        HttpResponse<String> response = post("http://localhost:1234/transfers")
                .body(sb)
                .asString();

        JSONObject obj = new JSONObject(response.getBody());

        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(obj.getString("id")).isNotEmpty();
        assertThat(obj.getString("payerAccountId")).isEqualTo(payer.getId().toString());
        assertThat(obj.getString("beneficiaryAccountId")).isEqualTo(beneficiary.getId().toString());
        assertThat(obj.getInt("amount")).isEqualTo(2);

    }

    @Test
    public void GET_id_transfer_returns_transfer() {

        CreateAccountRequest accountRequest = new CreateAccountRequest();
        accountRequest.setHolder("Max");
        accountRequest.setInitialBalance(11);
        AccountResponse account = accountController.createAccount(accountRequest);

        HttpResponse<String> response = get("http://localhost:1234/accounts/{accountId}")
                .routeParam("accountId", account.getId().toString())
                .asString();

        JSONObject obj = new JSONObject(response.getBody());

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(obj.getString("id")).isEqualTo(account.getId().toString());
        assertThat(obj.getString("holder")).isEqualTo("Max");
        assertThat(obj.getString("balance")).isEqualTo("11");
    }




}