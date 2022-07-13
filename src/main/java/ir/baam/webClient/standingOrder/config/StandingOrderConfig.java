package ir.baam.webClient.standingOrder.config;

import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class StandingOrderConfig {

  @Bean("StandingOrderErrorDecoder")
  public ErrorDecoder errorDecoder() {
    return new StandingOrderErrorDecoder();
  }

  @Bean("StandingOrderDecoder")
  public Decoder getDecoder() {
    HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
    ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
    return new ResponseEntityDecoder(new StandingOrderDecoder(objectFactory));
  }
}