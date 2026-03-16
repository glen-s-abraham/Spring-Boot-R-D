package com.example.restfull_web_services.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.restfull_web_services.dtos.ServerStatusDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class StatusController {
    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("server up and running");
    }

    @GetMapping("/health")
    public ResponseEntity<ServerStatusDto> getServerHealth() {
        Runtime runtime = Runtime.getRuntime();

        ServerStatusDto status = ServerStatusDto.builder()
                .status("UP")
                .totalMemory(runtime.totalMemory())
                .freeMemory(runtime.freeMemory())
                .maxMemory(runtime.maxMemory())
                .availableProcessors(runtime.availableProcessors())
                .build();

        return ResponseEntity.ok(status);
    }

    @GetMapping("/health/{metric}")
    public ResponseEntity<Object> getSpecificMetric(
            @PathVariable String metric) {
        Runtime runtime = Runtime.getRuntime();

        return switch (metric.toLowerCase()) {
            case "memory", "totalmemory" -> ResponseEntity.ok(runtime.totalMemory());
            case "freememory" -> ResponseEntity.ok(runtime.freeMemory());
            case "maxmemory" -> ResponseEntity.ok(runtime.maxMemory());
            case "processors", "availableprocessors" -> ResponseEntity.ok(runtime.availableProcessors());
            case "status" -> ResponseEntity.ok("UP");
            default -> ResponseEntity.badRequest().body("Unknown metric: " + metric);
        };
    }
}