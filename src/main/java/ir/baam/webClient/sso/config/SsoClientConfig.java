package ir.baam.webClient.sso.config;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import ir.baam.webClient.sso.dto.SsoDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class SsoClientConfig {

  @Bean("SsoDecoder")
  public Decoder getDecoder() {
    HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
    ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
    return new ResponseEntityDecoder(new SsoDecoder(objectFactory));
  }

  @Bean
  Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> converters) {
    return new SpringFormEncoder(new SpringEncoder(converters));
  }
}