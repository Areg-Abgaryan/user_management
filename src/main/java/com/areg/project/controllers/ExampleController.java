package com.areg.project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
class ExampleController {

    @GetMapping("/example")
    public ResponseEntity<String> exampleEndpoint() {
        return ResponseEntity.ok("some example");
    }
}
