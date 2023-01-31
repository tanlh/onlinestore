package com.onlinestore.shared.errorhandling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

    Date timestamp;
    String errorCode;
    String message;

    public static ErrorMessage of(String errorCode, String message) {
        return new ErrorMessage(new Date(), errorCode, message);
    }

}
