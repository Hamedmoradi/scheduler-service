package ir.baam.webClient.standingOrder.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.stereotype.Component;

@Component
public class StandingOrderDecoder extends SpringDecoder {

  public StandingOrderDecoder(
      ObjectFactory<HttpMessageConverters> messageConverters) {
    super(messageConverters);
  }

}