package me.huynhducphu.PingMe_Backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Admin 8/3/2025
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiResponse<T> {

    private String errorCode;
    private T data;

    public ApiResponse(String errorCode) {
        this.errorCode = errorCode;
        this.data = null;
    }

    public ApiResponse(T data) {
        this.data = data;
        this.errorCode = null;
    }
}
