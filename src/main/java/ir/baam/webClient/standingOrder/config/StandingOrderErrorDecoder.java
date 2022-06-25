package ir.baam.webClient.standingOrder.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import ir.baam.exeption.BusinessException;
import ir.baam.exeption.SchedulerBusinessError;
import ir.baam.validation.Validation;
import ir.baam.webClient.standingOrder.dto.StandingOrderErrorDto;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Log4j2
@Component
public class StandingOrderErrorDecoder implements ErrorDecoder {

  @SneakyThrows
  @Override
  public Exception decode(String methodKey, Response response) throws FeignException {
    StandingOrderErrorDto errorResponse;
    try {
      Gson gson = new GsonBuilder().serializeNulls().create();
      errorResponse = gson.fromJson(response.body().asReader(Charset.defaultCharset()), StandingOrderErrorDto.class);
    } catch (Exception exp) {
      throw new BusinessException(SchedulerBusinessError.INVALID_COMMAND);
    }
    if (errorResponse == null) {
      throw new BusinessException(SchedulerBusinessError.INVALID_COMMAND,
              Validation.listOfOne("Can't convert response correctly"));
    } else {
      throw new BusinessException(String.valueOf(errorResponse.getMessage()),
              response.status(),
              errorResponse.getCode(),
              errorResponse.getDetails());
    }
  }
}