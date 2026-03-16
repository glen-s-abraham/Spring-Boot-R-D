package com.example.restfull_web_services.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerStatusDto {
    private String status;
    private long totalMemory;
    private long freeMemory;
    private long maxMemory;
    private int availableProcessors;
}
