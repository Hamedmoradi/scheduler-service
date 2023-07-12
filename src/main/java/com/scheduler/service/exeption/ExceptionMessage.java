package com.scheduler.service.exeption;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scheduler.service.validation.Validation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public  class ExceptionMessage{

  private String timestamp;
  @JsonIgnore
  private int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
  private String error;
  private String message ="NOT SET";
  private String path;
  private String code = "NOT SET";
  private List<Validation> details = new ArrayList<>();



}