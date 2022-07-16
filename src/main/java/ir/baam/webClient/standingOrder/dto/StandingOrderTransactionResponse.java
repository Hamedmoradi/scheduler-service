package ir.baam.webClient.standingOrder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.baam.enumeration.StandingOrderTransactionStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class StandingOrderTransactionResponse {
    @JsonProperty("standingOrderId")
    private Long standingOrderId;
    @JsonProperty("instructionId")
    private String instructionId;
    @JsonProperty("executionDate")
    private Date executionDate;
    @JsonProperty("hasException")
    private Boolean hasException;
    @JsonProperty("exceptionType")
    private String exceptionType;
    @JsonProperty("exceptionErrorCode")
    private String exceptionErrorCode;
    @JsonProperty("standingOrderTransactionStatus")
    private StandingOrderTransactionStatusEnum standingOrderTransactionStatus;
}
