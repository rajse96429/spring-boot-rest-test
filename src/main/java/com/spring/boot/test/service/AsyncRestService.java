package com.spring.boot.test.service;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.HttpServerErrorException;

@Service
public class AsyncRestService {

	@Autowired
	AsyncRestTemplate asyncRestTemplate;
	
	 public String deleteAllSuspendedUsers() {
	        ListenableFuture future = asyncRestTemplate.delete("http://localhost:8080/delete-all-suspended-users");
	        // doing some long process here...
	        Object result = null;
	        String returnValue = "";
	        try {
	            result = future.get(); //The Future will return a null result upon completion.
	            if (result == null) {
	                returnValue = "{result:'success'}";
	            } else {
	                returnValue = "{result:'fail'}";
	            }
	        } catch (InterruptedException | ExecutionException e) {
	            if (e.getCause() instanceof HttpServerErrorException) {
	                returnValue = "{result: 'server error'}";
	            }
	        }
	        System.out.println("deleteAllSuspendedUsers: " + result);
	         
	        return returnValue;
	    }
	
}
