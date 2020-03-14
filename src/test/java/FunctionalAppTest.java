import com.revolut.moneytransfer.App;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FunctionalAppTest {

    private App app = new App();

    @BeforeEach
    private void SetUpEach() {
        app.start(1234);
    }

    @AfterEach
    private void CleanUpEach() {
        app.stop();
    }

    @Test
    public void GET_hello_response_with_hello_world() {
        HttpResponse<String> response = Unirest.get("http://localhost:1234/hello").asString();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Hello World");
    }

}