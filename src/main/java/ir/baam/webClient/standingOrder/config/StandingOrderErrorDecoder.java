package ir.baam.webClient.standingOrder.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import feign.Response;
import feign.codec.ErrorDecoder;
import ir.baam.exeption.BusinessException;
import ir.baam.webClient.standingOrder.dto.StandingOrderErrorDto;
import java.io.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class StandingOrderErrorDecoder implements ErrorDecoder {

  @Override
  public BusinessException decode(String methodKey, Response response) {

    String message = null;
    Reader reader = null;
    StandingOrderErrorDto standingOrderErrorDto = null;

    try {
      reader = response.body().asReader();
      String result = CharStreams.toString(reader);
      ObjectMapper mapper = new ObjectMapper();
      mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      standingOrderErrorDto = mapper.readValue(result, StandingOrderErrorDto.class);
      message = standingOrderErrorDto.getMessage();

      return new BusinessException(result,response.status(),response.request().url(),standingOrderErrorDto.getDetails());

//          standingOrderErrorDto.getPath()
//        standingOrderErrorDto.getTimestamp()
//          (List<Validation>) Validation
//              .builder()
//              .message(standingOrderErrorDto.getPath())
//              .item("path")
//              .message(standingOrderErrorDto.getError())
//              .item("error")
//              .message(standingOrderErrorDto.getTimestamp())
//              .item("timestamp")
//              .build());
//          Validation.listOfOne(response.request().url(),"path"));
    } catch (IOException e) {

    } finally {

      try {
        if (reader != null) {
          reader.close();
        }
      } catch (IOException e) {
        return new BusinessException(standingOrderErrorDto.getMessage(),response.status(),response.request().url(),standingOrderErrorDto.getDetails());
      }
    }

    switch (response.status()) {

      case 404:
        return new BusinessException(message == null ? "File no found" : message,400);
      case 403:
        return new BusinessException(message == null ? "Forbidden access" : message, response.status());
    }
    return new BusinessException(standingOrderErrorDto.getMessage(),response.status(),response.request().url(),standingOrderErrorDto.getDetails());
  }
}