import com.tasksApi.TasksApiApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
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
public class UserValidationsTest {

    /*@LocalServerPort
    private int port;*/

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void missingFields() throws Exception {
        String url = "http://localhost:8080/register";

        Map<String, Object> credentials = new HashMap<>();

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(credentials);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ArrayList messages = (ArrayList) response.getBody().get("message");
        assertThat(messages.contains("MISSING_NAME")).isEqualTo(true);
        assertThat(messages.contains("MISSING_EMAIL")).isEqualTo(true);
        assertThat(messages.contains("MISSING_PASSWORD")).isEqualTo(true);
    }

    @Test
    public void invalidFields() throws Exception {
        String url = "http://localhost:8080/register";

        Map<String, Object> credentials = new HashMap<>();
        credentials.put("name", 99);
        credentials.put("email", 99);
        credentials.put("password", "password");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(credentials);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ArrayList messages = (ArrayList) response.getBody().get("message");
        assertThat(messages.contains("NAME_NOT_STRING")).isEqualTo(true);
        assertThat(messages.contains("EMAIL_NOT_STRING")).isEqualTo(true);
    }

    @Test
    public void emptyFields() throws Exception {
        String url = "http://localhost:8080/register";

        Map<String, Object> credentials = new HashMap<>();
        credentials.put("name", "");
        credentials.put("email", "");
        credentials.put("password", "");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(credentials);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ArrayList messages = (ArrayList) response.getBody().get("message");

        assertThat(messages.contains("EMPTY_NAME")).isEqualTo(true);
        assertThat(messages.contains("EMPTY_EMAIL")).isEqualTo(true);
        assertThat(messages.contains("EMPTY_PASSWORD")).isEqualTo(true);
    }

    @Test
    public void invalidEmail() throws Exception {
        String url = "http://localhost:8080/register";

        Map<String, Object> credentials = new HashMap<>();
        credentials.put("name", "Some Name");
        credentials.put("email", "somemailcom");
        credentials.put("password", "password");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(credentials);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ArrayList messages = (ArrayList) response.getBody().get("message");
        assertThat(messages.contains("INVALID_EMAIL")).isEqualTo(true);
    }

    @Test
    public void emailAlreadyTaken() throws Exception {
        String url = "http://localhost:8080/register";

        Map<String, Object> credentials = new HashMap<>();
        credentials.put("name", "Some Name");
        credentials.put("email", "s.dante@lcn.com");
        credentials.put("password", "password");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(credentials);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ArrayList messages = (ArrayList) response.getBody().get("message");
        assertThat(messages.contains("EMAIL_ALREADY_TAKEN")).isEqualTo(true);
    }
}
