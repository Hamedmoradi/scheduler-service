package ir.baam.webClient.standingOrder.config;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import ir.baam.exeption.BusinessException;
import ir.baam.exeption.SchedulerBusinessError;
import ir.baam.webClient.standingOrder.dto.StandingOrderErrorDto;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.Charset;

@Log4j2
public class StandingOrderErrorDecoder implements ErrorDecoder {

    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        StandingOrderErrorDto errorResponse;
        try {
            Gson gson = new GsonBuilder().serializeNulls().create();
            errorResponse = gson.fromJson(response.body().asReader(Charset.defaultCharset()), StandingOrderErrorDto.class);
        } catch (FeignException exp) {
            exp.getStackTrace();
            throw new BusinessException(SchedulerBusinessError.STANDING_ORDER_INTERNAL_ERROR);
        }
        if (errorResponse == null) {
            throw new BusinessException(SchedulerBusinessError.STANDING_ORDER_INTERNAL_ERROR);
        } else {
            throw new BusinessException(String.valueOf(errorResponse.getMessage()), response.status(), errorResponse.getCode(), errorResponse.getDetails());
        }
    }
}