package com.phuctri.shoesapi.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalysisResponse {
    private Double monthly;
    private Double yearly;
    private Integer incompleteOrder;
    private Integer completedOrder;

    public AnalysisResponse(Double monthly,
                            Double yearly,
                            Integer incompleteOrder,
                            Integer completedOrder) {
        this.monthly = monthly;
        this.yearly = yearly;
        this.incompleteOrder = incompleteOrder;
        this.completedOrder = completedOrder;
    }
}
