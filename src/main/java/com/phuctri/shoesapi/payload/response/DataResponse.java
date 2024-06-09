package com.phuctri.shoesapi.payload.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonPropertyOrder({
        "success",
        "message",
        "data"
})
public class DataResponse extends ApiResponse implements Serializable {
    private Object data;

    public DataResponse(Boolean success, Object data) {
        super(success);
        this.data = data;
    }

    public DataResponse(Boolean success, String message, Object data) {
        super(success, message);
        this.data = data;
    }
}
