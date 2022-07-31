package ir.baam.webClient.standingOrder.config;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import ir.baam.exeption.BusinessException;
import ir.baam.exeption.SchedulerBusinessError;
import ir.baam.webClient.standingOrder.dto.StandingOrderErrorDto;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.Charset;

@Log4j2
public class StandingOrderErrorDecoder implements ErrorDecoder {

    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {

        String requestUrl = response.request().url();
        HttpStatus responseStatus = HttpStatus.valueOf(response.status());
        try {
            String bodyStr = Util.toString(response.body().asReader(Util.UTF_8));
            log.debug("Exception in: " + requestUrl + "--- status: " + responseStatus + " --- body: " + bodyStr);
            throw new BusinessException(String.valueOf(response.request()), response.status(), response.headers().toString());
        } catch (IOException e) {
            throw new BusinessException(String.valueOf(response.request()), response.status(), response.headers().toString());
        }
    }
}