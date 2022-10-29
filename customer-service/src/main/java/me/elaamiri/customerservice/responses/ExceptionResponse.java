package me.elaamiri.customerservice.responses;

import lombok.Data;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Data
@ToString
public class ExceptionResponse {
    private HttpStatus httpStatus;
    private String message;
}
