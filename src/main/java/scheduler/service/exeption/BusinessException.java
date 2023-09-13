package scheduler.service.exeption;

import scheduler.service.enumeration.SchedulerBusinessError;
import scheduler.service.validation.Validation;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {
  private  String code = "Without code";
  private int status = 500;
  private List<Validation> params = new ArrayList<>();

  public BusinessException(String message, int status) {
    super(message);
    this.status = status;
  }

  public BusinessException(SchedulerBusinessError schedulerBusinessError) {
    super(schedulerBusinessError.getMessage());
    this.status = schedulerBusinessError.getStatus();
    this.code = schedulerBusinessError.getErrorCode();
  }

  public BusinessException(SchedulerBusinessError schedulerBusinessError, List<Validation> params) {
    super(schedulerBusinessError.getMessage());
    this.status = schedulerBusinessError.getStatus();
    this.code = schedulerBusinessError.getErrorCode();
    this.params = params;
  }

  public BusinessException(String message, int status, String code) {
    super(message);
    this.status = status;
    this.code = code;
  }

  public BusinessException(String message, int status, List<Validation> params) {
    super(message);
    this.status = status;
    this.code = "E" + status + "SO";
    this.params = params;
  }

  public BusinessException(String message, int status, String code, List<Validation> params) {
    super(message);
    this.status = status;
    this.code = code;
    this.params = params;
  }
}