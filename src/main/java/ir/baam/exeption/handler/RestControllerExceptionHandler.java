package ir.baam.exeption.handler;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import feign.FeignException;
import ir.baam.exeption.BusinessException;
import ir.baam.exeption.SchedulerBusinessError;
import ir.baam.validation.Validation;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Locale;
import java.util.Objects;
import javax.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.quartz.SchedulerException;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Log4j2
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class RestControllerExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;
    private final MessageSource messages;



    @ResponseBody
    @ExceptionHandler({SchedulerException.class,BusinessException.class})
    public ResponseEntity<?> handleBusinessException(BusinessException businessException) {

        log.error(businessException.getMessage());
        ErrorDto errorDto = ErrorDto.builder()
                .message(messages.getMessage(businessException.getMessage(), null, new Locale("fa")))
                .status(businessException.getStatus())
                .code(businessException.getCode())
                .details(businessException.getParams())
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(errorDto.getStatus()));
    }

    @ResponseBody
    @ExceptionHandler({IOException.class, FeignException.class})
    public ResponseEntity<?> handleNonRunException(IOException be) {
        log.error(be.getMessage());
        ErrorDto errorDto = ErrorDto.builder()
                .message(messages.getMessage(be.getMessage(), null, new Locale("fa")))
                .status(400)
                .code("400000")
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(errorDto.getStatus()));
    }

    @ResponseBody
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleConstraintViolation(ValidationException validationException) {
        ErrorDto errorDto = ErrorDto.builder()
                .message(validationException.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .code(SchedulerBusinessError.FILL_MANDATORY_PARAMETERS.getErrorCode())
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(errorDto.getStatus()));
    }


    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<?> handleConverterErrors(JacksonException jacksonException) {
        return handleNonRunException(jacksonException);
    }

    @ResponseBody
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MismatchedInputException.class})
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
        BusinessException businessException =
                new BusinessException(SchedulerBusinessError.GENERAL_MISMATCH_INPUT,
                    Validation.listOfOne(methodArgumentTypeMismatchException.getCause().toString(),
                                         methodArgumentTypeMismatchException.getName()));
        return handleBusinessException(businessException);
    }

    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException missingServletRequestParameterException) {

        BusinessException exception =
                new BusinessException(SchedulerBusinessError.FILL_MANDATORY_PARAMETERS,
                Validation.listOfOne(missingServletRequestParameterException.getMessage(),
                                     missingServletRequestParameterException.getParameterName()));
        return handleBusinessException(exception);
    }

    @ResponseBody
    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<?> handleConnectionException(ConnectException connectException) {

        BusinessException exception = new BusinessException(SchedulerBusinessError.RESOURCE_NOT_FOUND);
        connectException.getCause();
        return handleBusinessException(exception);
    }

    @SneakyThrows
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatus());
        }
        if (ex instanceof ServerWebInputException) {
            return response
                    .writeWith(Mono.fromSupplier(() -> {
                        DataBufferFactory bufferFactory = response.bufferFactory();
                        try {
                            return bufferFactory.wrap(objectMapper.writeValueAsBytes(
                                    new ErrorDto("مقادیر لازم یا وراد نشده، یا صحیح نمی باشد.",
                                            400,
                                        Validation.listOfOne(((ServerWebInputException) ex).getMessage(), null))));
                        } catch (JsonProcessingException e) {
                            log.error("Error writing response", ex);
                            return bufferFactory.wrap(new byte[0]);
                        }
                    }));
        }

        return Mono.error(ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorDto> processValidationIllegalError(HttpMessageNotReadableException ex) {

        Throwable throwable = ex.getMostSpecificCause();
        ErrorDto errorDTO = new ErrorDto();
        if (throwable instanceof BusinessException) {
            BusinessException exception = (BusinessException) ex.getMostSpecificCause();
            errorDTO.setStatus(exception.getStatus());
            errorDTO.setCode(exception.getCode());
            errorDTO.setMessage(exception.getMessage() + " is an invalid " + exception.getParams());
        } else {
            errorDTO.setStatus(400);
            errorDTO.setCode(SchedulerBusinessError.SCHEDULER_JSON_STRUCTURE_IS_INVALID.getErrorCode());
            errorDTO.setMessage(SchedulerBusinessError.SCHEDULER_JSON_STRUCTURE_IS_INVALID.getMessage());
            errorDTO.setDetails(Validation.listOfOne(ex.getMessage()));
        }
        return new ResponseEntity<ErrorDto>(errorDTO, HttpStatus.BAD_REQUEST);
    }
    @ResponseBody
    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public ResponseEntity<?> handleSqlException(InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {

        log.error(invalidDataAccessResourceUsageException.getMessage());
        ErrorDto errorDto = ErrorDto.builder()
                .message(messages.getMessage(Objects.requireNonNull(invalidDataAccessResourceUsageException.getMessage()), null, new Locale("fa")))
                .status(500)
                .code("55555555")
//                .details()
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(errorDto.getStatus()));
    }
}