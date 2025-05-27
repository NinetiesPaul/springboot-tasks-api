import com.tasksApi.TasksApiApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TasksApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskValidationsTest {

    /*@LocalServerPort
    private int port;*/

    @Autowired
    private TestRestTemplate restTemplate;

    private String token = "";

    @BeforeAll
    public void setup() {
        if (token.equals("")) {
            Map<String, Object> credentials = new HashMap<>();
            credentials.put("username", "s.dante@lcn.com");
            credentials.put("password", "123456");

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(credentials);

            ResponseEntity<Map> loginResponse = restTemplate.exchange("http://localhost:8080/login", HttpMethod.POST, request, Map.class);
            token = (String) loginResponse.getBody().get("token");
        }
    }

    @Test
    public void missingFields() throws Exception {
        String url = "http://localhost:8080/api/task/create";

        Map<String, Object> payload = new HashMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ArrayList messages = (ArrayList) response.getBody().get("message");
        assertThat(messages.contains("MISSING_TITLE")).isEqualTo(true);
        assertThat(messages.contains("MISSING_DESCRIPTION")).isEqualTo(true);
        assertThat(messages.contains("MISSING_TYPE")).isEqualTo(true);
    }

    @Test
    public void invalidFields() throws Exception {
        String url = "http://localhost:8080/api/task/create";

        Map<String, Object> payload = new HashMap<>();
        payload.put("title", 99);
        payload.put("description", 99);
        payload.put("type", 99);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ArrayList messages = (ArrayList) response.getBody().get("message");
        assertThat(messages.contains("TITLE_NOT_STRING")).isEqualTo(true);
        assertThat(messages.contains("DESCRIPTION_NOT_STRING")).isEqualTo(true);
        assertThat(messages.contains("TYPE_NOT_STRING")).isEqualTo(true);
        assertThat(messages.contains("INVALID_TYPE")).isEqualTo(true);
    }

    @Test
    public void emptyFields() throws Exception {
        String url = "http://localhost:8080/api/task/create";

        Map<String, Object> payload = new HashMap<>();
        payload.put("title", "");
        payload.put("description", "");
        payload.put("type", "");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ArrayList messages = (ArrayList) response.getBody().get("message");
        assertThat(messages.contains("EMPTY_TITLE")).isEqualTo(true);
        assertThat(messages.contains("EMPTY_DESCRIPTION")).isEqualTo(true);
        assertThat(messages.contains("EMPTY_TYPE")).isEqualTo(true);
    }
}
