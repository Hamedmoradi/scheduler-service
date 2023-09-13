package scheduler.service.validation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class Validation implements Serializable {

  private String message;
  @JsonIgnore
  private boolean valid;
  private String item;

  public Validation(String message, boolean isValid) {
    this.message = message;
    this.valid = isValid;
  }

  public Validation(String message) {
    this.message = message;
    this.valid = false;
  }

  public Validation(String message, String item) {
    this.message = message;
    this.item = item;
    this.valid = false;
  }

  @Override
  public String toString() {
    return this.message;
  }

  public static Validation from(String message) {
    return new Validation(message);
  }

  public static List<Validation> listOfOne(String message, String item) {
    return List.of(new Validation(message, item));
  }

  public static List<Validation> listOfOne(String message) {
    return listOfOne(message, null);
  }
}
