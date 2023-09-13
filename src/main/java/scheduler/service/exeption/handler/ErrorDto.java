package scheduler.service.exeption.handler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import scheduler.service.validation.Validation;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {

  private Object message = "NOT SET";
  @JsonIgnore
  private int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
  private String code = "NOT SET";
  private List<Validation> details = new ArrayList<>();

    public ErrorDto(Object message, int status, List<Validation> details) {
    this.message = message;
    this.status = status;
    this.details = details;
  }

}