package ir.baam.webClient.standingOrder.config;


import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import ir.baam.exeption.BusinessException;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Log4j2
public class StandingOrderErrorDecoder implements ErrorDecoder {

    @SneakyThrows
    @Override
    public BusinessException decode(String methodKey, Response response) {

        try {
            String requestUrl = response.request().url();
            HttpStatus responseStatus = HttpStatus.valueOf(response.status());
            if (response.body()!=null){
                String bodyStr = Util.toString(response.body().asReader(Util.UTF_8));
                log.debug("Exception in: " + requestUrl + "--- status: " + responseStatus + " --- body: " + bodyStr);
            }
            throw new BusinessException(String.valueOf(response.request()), response.status(), response.headers().toString());
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(response.request()), response.status(), response.headers().toString());
        }
    }
}