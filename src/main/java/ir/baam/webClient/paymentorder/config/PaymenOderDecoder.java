package ir.baam.webClient.paymentorder.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;

public class PaymenOderDecoder extends SpringDecoder {

  public PaymenOderDecoder(
      ObjectFactory<HttpMessageConverters> messageConverters) {
    super(messageConverters);
  }
}
