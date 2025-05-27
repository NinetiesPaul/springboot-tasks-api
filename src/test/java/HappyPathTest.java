import com.tasksApi.TasksApiApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
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
public class HappyPathTest {

    /*@LocalServerPort
    private int port;*/

    @Autowired
    private TestRestTemplate restTemplate;

    private String token = "";

    private String tokenAlternative = "";

    private Integer taskId;

    private Integer assignTo;

    private Integer assignmentId;

    private Integer commentId;

    @BeforeAll
    public void setup() {
        if (token.equals("")) {
            Map<String, Object> credentials = new HashMap<>();
            credentials.put("username", "f.lamana@lcn.com");
            credentials.put("password", "123456");

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(credentials);

            ResponseEntity<Map> response = restTemplate.exchange("http://localhost:8080/login", HttpMethod.POST, request, Map.class);
            token = (String) response.getBody().get("token");
        }

        if (tokenAlternative.equals("")) {
            Map<String, Object> credentials = new HashMap<>();
            credentials.put("username", "s.dante@lcn.com");
            credentials.put("password", "123456");

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(credentials);

            ResponseEntity<Map> loginResponse = restTemplate.exchange("http://localhost:8080/login", HttpMethod.POST, request, Map.class);
            tokenAlternative = (String) loginResponse.getBody().get("token");
        }
    }

    @Test
    @Order(1)
    public void testListAllUsers() throws Exception {
        String url = "http://localhost:8080/api/users/list";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map responseBody = (Map) response.getBody().get("data");
        assertThat(responseBody.get("total")).isInstanceOf(Integer.class);

        ArrayList usersList = (ArrayList) responseBody.get("users");

        for (Object user : usersList) {

            Map<String, Object> userItem = (Map<String, Object>) user;

            if (userItem.get("email").equals("f.lamana@lcn.com")) {
                assignTo = (Integer) userItem.get("id");
            } // .getClass().getName()
        }
    }

    @Test
    @Order(2)
    public void testCreateTask() throws Exception {
        String url = "http://localhost:8080/api/task/create";

        Map<String, Object> user = new HashMap<>();
        user.put("title", "New Task Title");
        user.put("description", "This is the description of the New Task");
        user.put("type", "feature");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map responseBody = (Map) response.getBody().get("data");
        taskId = (Integer) responseBody.get("id");
        String status = (String) responseBody.get("status");
        assertThat(status).isEqualTo("open");
    }

    @Test
    @Order(3)
    public void testViewTask() throws Exception {
        String url = "http://localhost:8080/api/task/view/" + taskId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(4)
    public void testUpdateTask() throws Exception {
        String url = "http://localhost:8080/api/task/update/" + taskId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Map<String, Object> user = new HashMap<>();
        user.put("type", "hotfix");
        user.put("status", "in_dev");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PUT, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map responseBody = (Map) response.getBody().get("data");

        String status = (String) responseBody.get("status");
        assertThat(status).isEqualTo("in_dev");

        String type = (String) responseBody.get("type");
        assertThat(type).isEqualTo("hotfix");
    }

    @Test
    @Order(5)
    public void testHistoryTask() throws Exception {
        String url = "http://localhost:8080/api/task/view/" + taskId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map responseBody = (Map) response.getBody().get("data");

        ArrayList taskHistory = (ArrayList) responseBody.get("history");

        for (Object item : taskHistory) {

            Map<String, Object> historyItem = (Map<String, Object>) item;

            if (historyItem.get("field").equals("status")) {
                assertThat((String) historyItem.get("changed_from")).isEqualTo("open");
                assertThat((String) historyItem.get("changed_to")).isEqualTo("in_dev");
            }

            if (historyItem.get("field").equals("type")) {
                assertThat((String) historyItem.get("changed_from")).isEqualTo("feature");
                assertThat((String) historyItem.get("changed_to")).isEqualTo("hotfix");
            }

            /*
             * 
             * .getClass().getName()
             * 
            */
        }
    }

    @Test
    @Order(6)
    public void testTaskAssignment() throws Exception {
        String url = "http://localhost:8080/api/task/assign/" + taskId;

        Map<String, Object> user = new HashMap<>();
        user.put("assigned_to", assignTo);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenAlternative);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map responseBody = (Map) response.getBody().get("data");
        assignmentId = (Integer) responseBody.get("id");

        Map assignedTo = (Map) responseBody.get("assigned_to");
        assertThat(assignedTo.get("name")).isEqualTo("Feech La Mana");

        Map assignedBy = (Map) responseBody.get("assigned_by");
        assertThat(assignedBy.get("name")).isEqualTo("Silvio Dante");
    }
    
    @Test
    @Order(7)
    public void testHistoryTaskAssignmentEntry() throws Exception {
        String url = "http://localhost:8080/api/task/view/" + taskId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map responseBody = (Map) response.getBody().get("data");

        ArrayList taskHistory = (ArrayList) responseBody.get("history");

        for (Object item : taskHistory) {

            Map<String, Object> historyItem = (Map<String, Object>) item;

            if (historyItem.get("field").equals("added_assignee")) {
                Map changedBy = (Map) historyItem.get("changed_by");
                assertThat((String) changedBy.get("name")).isEqualTo("Silvio Dante");
                assertThat((String) historyItem.get("changed_to")).isEqualTo("Feech La Mana");
            }
        }
    }

    @Test
    @Order(8)
    public void testTaskUnassignment() throws Exception {
        String url = "http://localhost:8080/api/task/unassign/" + assignmentId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.DELETE, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    
    @Test
    @Order(9)
    public void testHistoryTaskUnassignmentEntry() throws Exception {
        String url = "http://localhost:8080/api/task/view/" + taskId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map responseBody = (Map) response.getBody().get("data");

        ArrayList taskHistory = (ArrayList) responseBody.get("history");

        for (Object item : taskHistory) {

            Map<String, Object> historyItem = (Map<String, Object>) item;

            if (historyItem.get("field").equals("removed_assignee")) {
                Map changedBy = (Map) historyItem.get("changed_by");
                assertThat((String) changedBy.get("name")).isEqualTo("Feech La Mana");
                assertThat((String) historyItem.get("changed_to")).isEqualTo("Feech La Mana");
            }
        }
    }

    @Test
    @Order(10)
    public void testCreateTaskComments() throws Exception {
        String url = "http://localhost:8080/api/task/comment/" + taskId;

        Map<String, Object> payload = new HashMap<>();
        payload.put("text", "This is the first comment");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map responseBody = (Map) response.getBody().get("data");
        commentId = (Integer) responseBody.get("id");

        Map firstCommentAssignedTo = (Map) responseBody.get("created_by");
        assertThat(firstCommentAssignedTo.get("name")).isEqualTo("Feech La Mana");

        Map<String, Object> secondCommentPayload = new HashMap<>();
        secondCommentPayload.put("text", "This is the second comment");

        HttpHeaders secondCommentHeaders = new HttpHeaders();
        secondCommentHeaders.setContentType(MediaType.APPLICATION_JSON);
        secondCommentHeaders.setBearerAuth(tokenAlternative);

        HttpEntity<Map<String, Object>> secondCommentRequest = new HttpEntity<>(secondCommentPayload, secondCommentHeaders);

        ResponseEntity<Map> secondCommentResponse = restTemplate.exchange(url, HttpMethod.POST, secondCommentRequest, Map.class);
        assertThat(secondCommentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map secondCommentResponseBody = (Map) secondCommentResponse.getBody().get("data");

        Map secondCommentAssignedTo = (Map) secondCommentResponseBody.get("created_by");
        assertThat(secondCommentAssignedTo.get("name")).isEqualTo("Silvio Dante");
    }
    
    @Test
    @Order(11)
    public void testTaskRemoveComment() throws Exception {
        String url = "http://localhost:8080/api/task/comment/" + commentId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.DELETE, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    
    @Test
    @Order(12)
    public void testTaskHasSecondCommentOnly() throws Exception {
        String url = "http://localhost:8080/api/task/view/" + taskId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map responseBody = (Map) response.getBody().get("data");

        ArrayList taskComments = (ArrayList) responseBody.get("comments");

        assertThat(taskComments.size()).isEqualTo(1);

        for (Object item : taskComments) {

            Map<String, Object> taskComment = (Map<String, Object>) item;
            assertThat((String) taskComment.get("comment_text")).isEqualTo("This is the second comment");
        }
    }
    
    @Test
    @Order(13)
    public void testCloseTask() throws Exception {
        String url = "http://localhost:8080/api/task/close/" + taskId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenAlternative);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PUT, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map responseBody = (Map) response.getBody().get("data");

        Map closedBy = (Map) responseBody.get("closed_by");
        assertThat(closedBy.get("name")).isEqualTo("Silvio Dante");
    }
}
