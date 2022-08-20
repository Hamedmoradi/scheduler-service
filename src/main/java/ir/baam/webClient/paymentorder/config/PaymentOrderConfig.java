package ir.baam.webClient.paymentorder.config;

import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class PaymentOrderConfig {

  @Bean("PaymentOrderErrorDecoder")
  public ErrorDecoder errorDecoder() {
    return new PaymentOrderErrorDecoder();
  }

  @Bean("PaymenOderDecoder")
  public Decoder getDecoder() {
    HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
    ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
    return new ResponseEntityDecoder(new PaymenOderDecoder(objectFactory));
  }

}
