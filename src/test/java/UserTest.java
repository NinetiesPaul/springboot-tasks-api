import com.tasksApi.TasksApiApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
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
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TasksApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

    /*@LocalServerPort
    private int port;*/

    @Autowired
    private TestRestTemplate restTemplate;

    private String generatedUserName = "";

    private String generatedUserEmail = "";

    @Test
    @Order(2)
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
            userItem.get("name").equals(generatedUserName);
            userItem.get("email").equals(generatedUserEmail);
        }
    }

    @Test
    @Order(1)
    public void postUser() throws Exception {
        String url = "http://localhost:8080/register";

        String alphanumericCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuv";
 
        StringBuffer randomString = new StringBuffer(10);
        Random random = new Random();
    
        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(alphanumericCharacters.length());
            char randomChar = alphanumericCharacters.charAt(randomIndex);
            randomString.append(randomChar);
        }
 
        String randomStringToString = (String) randomString.toString();
        
        generatedUserName = randomStringToString;
        generatedUserEmail = randomStringToString + "@mail.com";

        Map<String, Object> credentials = new HashMap<>();
        credentials.put("name", generatedUserName);
        credentials.put("email", generatedUserEmail);
        credentials.put("password", "123456");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(credentials);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
