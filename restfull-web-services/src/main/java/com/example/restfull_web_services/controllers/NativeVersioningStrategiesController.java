package com.example.restfull_web_services.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/native")
public class NativeVersioningStrategiesController {

    // URI Versioning (Twitter/Localytics style)
    @GetMapping("/v1/person")
    public ResponseEntity<String> getPersonV1() {
        return ResponseEntity.ok("Name: John Doe (V1: Simple String)");
    }

    @GetMapping("/v2/person")
    public ResponseEntity<PersonV2> getPersonV2() {
        return ResponseEntity.ok(new PersonV2("John", "Doe"));
    }

    // Request Parameter Versioning (Amazon style)
    // Path: /api/person?version=1
    @GetMapping(path = "/person", params = "version=1")
    public ResponseEntity<String> getPersonParamV1() {
        return ResponseEntity.ok("Name: John Doe (V1: Simple String from Param)");
    }

    // Path: /api/person?version=2
    @GetMapping(path = "/person", params = "version=2")
    public ResponseEntity<PersonV2> getPersonParamV2() {
        return ResponseEntity.ok(new PersonV2("John", "Doe"));
    }

    // Request Header Versioning (Microsoft style)
    // Header: X-API-VERSION=1
    @GetMapping(path = "/person", headers = "X-API-VERSION=1")
    public ResponseEntity<String> getPersonHeaderV1() {
        return ResponseEntity.ok("Name: John Doe (V1: Simple String from Header)");
    }

    // Header: X-API-VERSION=2
    @GetMapping(path = "/person", headers = "X-API-VERSION=2")
    public ResponseEntity<PersonV2> getPersonHeaderV2() {
        return ResponseEntity.ok(new PersonV2("John", "Doe"));
    }

    // Media Type Versioning (a.k.a "content negotiation" or "accept header", GitHub
    // style)
    // Header: Accept=application/vnd.company.app-v1+json
    @GetMapping(path = "/person", produces = "application/vnd.company.app-v1+json")
    public ResponseEntity<String> getPersonProducesV1() {
        return ResponseEntity.ok("Name: John Doe (V1: Simple String from Media Type)");
    }

    // Header: Accept=application/vnd.company.app-v2+json
    @GetMapping(path = "/person", produces = "application/vnd.company.app-v2+json")
    public ResponseEntity<PersonV2> getPersonProducesV2() {
        return ResponseEntity.ok(new PersonV2("John", "Doe"));
    }

    // Helper classes for the V2 response
    record PersonV2(String firstName, String lastName) {
    }
}
