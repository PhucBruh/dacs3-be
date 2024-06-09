package com.phuctri.shoesapi.payload;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ColorPayload {
    private String name;
    private String value;
}
