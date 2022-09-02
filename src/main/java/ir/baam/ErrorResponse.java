package ir.baam;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
public class ErrorResponse {

    private final ZonedDateTime timestamp;
    private final int status;
    private final String message;

    public ErrorResponse(HttpStatus httpStatus, String msg){
        status = httpStatus.value();
        message = msg;
        timestamp = ZonedDateTime.now();
    }
}
