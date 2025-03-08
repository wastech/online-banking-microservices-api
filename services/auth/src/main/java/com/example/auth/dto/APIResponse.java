package com.example.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class APIResponse {
    public String message;
    public boolean status;

    public APIResponse(String message, boolean status) {
        this.message = message;
        this.status = status;
    }
}