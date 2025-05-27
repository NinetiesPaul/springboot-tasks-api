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
public class TaskValidationsOnUpdateTest {

    /*@LocalServerPort
    private int port;*/

    @Autowired
    private TestRestTemplate restTemplate;

    private String token = "";

    private Integer taskId = 0;

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

        if (taskId == 0) {
            String url = "http://localhost:8080/api/task/create";

            Map<String, Object> user = new HashMap<>();
            user.put("title", "Task For Tests");
            user.put("description", "This task was created for an integration test");
            user.put("type", "feature");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
            Map responseBody = (Map) response.getBody().get("data");
            taskId = (Integer) responseBody.get("id");
        }
    }

    @Test
    public void invalidFields() throws Exception {
        String url = "http://localhost:8080/api/task/update/" + taskId;

        Map<String, Object> payload = new HashMap<>();
        payload.put("title", 99);
        payload.put("description", 99);
        payload.put("type", 99);
        payload.put("status", 99);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PUT, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ArrayList messages = (ArrayList) response.getBody().get("message");
        assertThat(messages.contains("TITLE_NOT_STRING")).isEqualTo(true);
        assertThat(messages.contains("DESCRIPTION_NOT_STRING")).isEqualTo(true);
        assertThat(messages.contains("TYPE_NOT_STRING")).isEqualTo(true);
        assertThat(messages.contains("STATUS_NOT_STRING")).isEqualTo(true);
    }

    @Test
    public void emptyFields() throws Exception {
        String url = "http://localhost:8080/api/task/update/" + taskId;

        Map<String, Object> payload = new HashMap<>();
        payload.put("title", "");
        payload.put("description", "");
        payload.put("type", "");
        payload.put("status", "");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PUT, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ArrayList messages = (ArrayList) response.getBody().get("message");
        assertThat(messages.contains("EMPTY_TITLE")).isEqualTo(true);
        assertThat(messages.contains("EMPTY_DESCRIPTION")).isEqualTo(true);
        assertThat(messages.contains("EMPTY_TYPE")).isEqualTo(true);
        assertThat(messages.contains("EMPTY_STATUS")).isEqualTo(true);
    }

    @Test
    public void taskNotFound() throws Exception {
        String url = "http://localhost:8080/api/task/update/999";

        Map<String, Object> payload = new HashMap<>();
        payload.put("title", "Some title");
        payload.put("description", "Some description");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PUT, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        ArrayList messages = (ArrayList) response.getBody().get("message");
        assertThat(messages.contains("TASK_NOT_FOUND")).isEqualTo(true);
    }

    @Test
    public void taskNotFoundOnClose() throws Exception {
        String url = "http://localhost:8080/api/task/close/999";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PUT, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        ArrayList messages = (ArrayList) response.getBody().get("message");
        assertThat(messages.contains("TASK_NOT_FOUND")).isEqualTo(true);
    }
}
