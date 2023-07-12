package com.scheduler.service.webClient.paymentorder.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;

@Log4j2
public class PaymentOrderErrorDecoder implements ErrorDecoder {

  @Override
  public Exception decode(String s, Response response) {
    String errorMessage = null;
    Reader reader = null;
    try {
      reader = response.body().asReader(StandardCharsets.UTF_8);
      String result = IOUtils.toString(reader);
      ObjectMapper mapper = new ObjectMapper();
      mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      Exception exceptionMessage = mapper.readValue(result, Exception.class);
      errorMessage = exceptionMessage.getMessage();
    } catch (IOException e) {
      log.error("IO Exception on reading exception message feign client" + e);
    } finally {
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (IOException e) {
        log.error("IO Exception on reading exception message feign client" + e);
      }
    }
    switch (response.status()) {
      case 400:
        log.error(errorMessage);
        return new Exception(errorMessage);
      case 401:
        log.error(errorMessage);
        return new Exception("Unauthorized Request Through Feign");
      case 404:
        log.error(errorMessage);
        return new Exception("Unidentified Request Through Feign");
      case 500:
        log.error(errorMessage);
        return new Exception("Connection failed.");
      default:
        log.error(errorMessage);
        //handle exception
        return new Exception("Common Feign Exception");
    }
  }
}
