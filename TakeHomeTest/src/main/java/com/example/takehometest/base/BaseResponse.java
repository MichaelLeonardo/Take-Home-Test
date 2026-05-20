package com.example.takehometest.base;

import com.example.takehometest._enum.ResponseCodeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse<T> {
    @JsonProperty("status_code")
    private String statusCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("error_messages")
    private List<BaseErrorMessage> errorMessages;
    @JsonProperty("warning_messages")
    private List<BaseWarningMessage> warningMessages;
    @JsonProperty("data")
    private T data;

    public BaseResponse() {
        this.errorMessages = new ArrayList<>();
        this.warningMessages = new ArrayList<>();
    }

    public void setStatusCodeByEnum(ResponseCodeEnum responseCodeEnum) {
        this.statusCode = responseCodeEnum.getCode();
        this.message = responseCodeEnum.getDescription();
    }

}
