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
public class BusinessRulesValidationTest {

    /*@LocalServerPort
    private int port;*/

    @Autowired
    private TestRestTemplate restTemplate;

    private String token = "";

    private Integer taskId = 0;

    private Integer taskIdToClose = 0;

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
            Map<String, Object> user = new HashMap<>();
            user.put("title", "Task For Tests");
            user.put("description", "This task was created for an integration test");
            user.put("type", "feature");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);

            ResponseEntity<Map> response = restTemplate.exchange("http://localhost:8080/api/task/create", HttpMethod.POST, request, Map.class);
            Map responseBody = (Map) response.getBody().get("data");
            taskId = (Integer) responseBody.get("id");

            Map<String, Object> assignmentPayload = new HashMap<>();
            assignmentPayload.put("assigned_to", "1");

            HttpEntity<Map<String, Object>> assignmentRequest = new HttpEntity<>(assignmentPayload, headers);
            restTemplate.exchange("http://localhost:8080/api/task/assign/" + taskId, HttpMethod.POST, assignmentRequest, Map.class);
        }

        if (taskIdToClose == 0) {
            Map<String, Object> user = new HashMap<>();
            user.put("title", "Task For Tests");
            user.put("description", "This task was created for an integration test");
            user.put("type", "feature");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);
            
            ResponseEntity<Map> response = restTemplate.exchange("http://localhost:8080/api/task/create", HttpMethod.POST, request, Map.class);
            Map responseBody = (Map) response.getBody().get("data");
            taskIdToClose = (Integer) responseBody.get("id");

            HttpEntity<Map<String, Object>> closeRequest = new HttpEntity<>(headers);
            restTemplate.exchange("http://localhost:8080/api/task/close/" + taskIdToClose, HttpMethod.PUT, closeRequest, Map.class);
        }
    }

    @Test
    public void userAlreadyAssigned() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Map<String, Object> assignmentPayload = new HashMap<>();
        assignmentPayload.put("assigned_to", "1");

        HttpEntity<Map<String, Object>> assignmentRequest = new HttpEntity<>(assignmentPayload, headers);
        ResponseEntity<Map> response = restTemplate.exchange("http://localhost:8080/api/task/assign/" + taskId, HttpMethod.POST, assignmentRequest, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        ArrayList messages = (ArrayList) response.getBody().get("message");
        assertThat(messages.contains("USER_ALREADY_ASSIGNED")).isEqualTo(true);
    }

    @Test
    public void invalidFields() throws Exception {
        String url = "http://localhost:8080/api/task/update/" + taskId;

        Map<String, Object> payload = new HashMap<>();
        payload.put("status", "closed");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PUT, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ArrayList messages = (ArrayList) response.getBody().get("message");
        assertThat(messages.contains("CAN_NOT_UPDATE_TO_CLOSE")).isEqualTo(true);
    }

    @Test
    public void taskAlreadyClosed() throws Exception {
        String url = "http://localhost:8080/api/task/close/" + taskIdToClose;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PUT, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ArrayList messages = (ArrayList) response.getBody().get("message");
        assertThat(messages.contains("TASK_ALREADY_CLOSED")).isEqualTo(true);
    }

    @Test
    public void taskNotFound() throws Exception {
        String url = "http://localhost:8080/api/task/update/" + taskIdToClose;

        System.out.println("\n**\n" + url);

        Map<String, Object> payload = new HashMap<>();
        payload.put("title", "Some title");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PUT, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ArrayList messages = (ArrayList) response.getBody().get("message");
        assertThat(messages.contains("TASK_CLOSED")).isEqualTo(true);
    }
}
