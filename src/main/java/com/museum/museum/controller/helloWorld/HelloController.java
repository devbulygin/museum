package com.museum.museum.controller.helloWorld;

import com.museum.generated.api.museum.HelloApi;
import com.museum.generated.model.museum.HelloResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloApi {
    @Override
    public ResponseEntity<HelloResponse> getHello() {
        HelloResponse response = new HelloResponse();
        response.setMessage("Привет");
        return ResponseEntity.ok(response);
    }
}
