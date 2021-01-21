package com.spring.boot.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.AsyncRestTemplate;

import com.spring.boot.test.service.AsyncRestService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AsyncRestServiceTest {

	@Autowired
    AsyncRestTemplate restTemplate;
 
    @Autowired
    AsyncRestService service;
 
    private MockRestServiceServer mockServer;
    
    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }
    
    @Test
    public void testDeleteAllSuspendedUsers() {
        mockServer.expect(requestTo("http://localhost:8080/delete-all-suspended-users")).andExpect(method(HttpMethod.DELETE))
            .andRespond(withServerError());
 
        String result = service.deleteAllSuspendedUsers();
        System.out.println("testDeleteAllSuspendedUsers: " + result);
 
        mockServer.verify();
        assertEquals("{result: 'server error'}", result);
    }
    
}
