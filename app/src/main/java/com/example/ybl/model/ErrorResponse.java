// ErrorResponse.java
package com.example.ybl.model;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class ErrorResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("errors")
    private Map<String, String[]> errors;

    // Getters
    public String getMessage() {
        return message;
    }

    public Map<String, String[]> getErrors() {
        return errors;
    }

    // Helper to get first error message
    public String getFirstErrorMessage() {
        if (errors != null && !errors.isEmpty()) {
            for (String[] errorMessages : errors.values()) {
                if (errorMessages != null && errorMessages.length > 0) {
                    return errorMessages[0];
                }
            }
        }
        return message != null ? message : "An error occurred";
    }
}