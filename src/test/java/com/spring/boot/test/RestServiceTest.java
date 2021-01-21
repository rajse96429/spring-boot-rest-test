package com.spring.boot.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.spring.boot.test.service.RestService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestServiceTest {

	@Autowired
    RestTemplate restTemplate;
 
    @Autowired
    RestService service;
 
    private MockRestServiceServer mockServer;
    
    @BeforeEach
    public void onSetUp() {
    	mockServer = MockRestServiceServer.createServer(restTemplate);
    }
    
    /**
     * In the testGetRootResource method, if the expected count
     * (e.g., once()) is not specified then by default it expects a single HTTP request.
     */
    @Test
    public void testGetRootResource() {
    	mockServer.expect(requestTo("http://localhost:8080"))
    			.andRespond(withSuccess("hello",MediaType.TEXT_PLAIN));
    	String result = service.getRootResource();
        System.out.println("testGetRootResource: " + result);
 
        mockServer.verify();
        assertEquals("hello", result);
    }
    
    @Test
    public void testGetRootResourceOnce() {
    	mockServer.expect(once(),requestTo("http://localhost:8080"))
    			.andRespond(withSuccess("{message : Under construction}",MediaType.APPLICATION_JSON));
    	
    	String result = service.getRootResource();
        System.out.println("testGetRootResource: " + result);
 
        mockServer.verify();
        assertEquals("{message : Under construction}", result);
    }
    
    /**
     * The testGetRootResourceTimes will fail because it expects two HTTP requests to the REST server 
     * but RestService only invokes RestTemplate’s getForObject method once.
     */
   // @Test
    public void testGetRootResourceTimes() {
        mockServer.expect(times(2), requestTo("http://localhost:8080"))
            .andRespond(withSuccess("{message : 'under construction'}", MediaType.APPLICATION_JSON));
 
        String result = service.getRootResource();
        System.out.println("testGetRootResourceTimes: " + result);
     // should fail because this test expects RestTemplate.getForObject to be called twice
        mockServer.verify();  
        assertEquals("{message : 'under construction'}", result);
    }
    
    @Test
    public void testAddComment() {
    	mockServer.expect(requestTo("http://localhost:8080/add-comment"))
    	.andExpect(method(HttpMethod.POST))
    	.andRespond(withSuccess("{Post : Success}",MediaType.APPLICATION_JSON));
    	
    	String result = service.addComment("hello");
    	System.out.println("testAddComment: " + result);
    	
    	mockServer.verify();
    	assertEquals("{Post : Success}", result);
    }
    
    /**
     * In testAddCommentClientError, a client error is simulated. 
     * The mock server returns an HTTP status code signifying an HTTP client error 
     * (e.g., malformed request).
     */
    @Test
    public void testAddCommentClientError() {
        mockServer.expect(requestTo("http://localhost:8080/add-comment")).andExpect(method(HttpMethod.POST))
            .andRespond(withBadRequest().body("error"));
 
        String result = service.addComment("hello");
        System.out.println("testAddCommentClientError: " + result);
 
        mockServer.verify();
        assertEquals("400 Bad Request: [error]", result);
    }
    
    /**
     * In the testReset method, the service is called twice. 
     * The mock server didn’t fail the test because a reset is 
     * made (i.e., mockServer.reset() is invoked between each single call). 
     * The MockRestServiceServer reset operation removes all expectations and recorded requests.
     */
    @Test
    public void testReset() {
        mockServer.expect(requestTo("http://localhost:8080/add-comment"))
        	.andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess("{post : 'success'}", MediaType.APPLICATION_JSON));
 
        String result = service.addComment("hello");
        System.out.println("testReset 1st: " + result);
 
        mockServer.verify();
        assertEquals("{post : 'success'}", result);
         
        mockServer.reset();
         
        mockServer.expect(requestTo("http://localhost:8080"))
        	.andRespond(withSuccess("hello", MediaType.TEXT_PLAIN));
 
        result = service.getRootResource();
        System.out.println("testReset 2nd: " + result);
 
        mockServer.verify();
        assertEquals("hello", result);
    }
     
}
