package com.phuctri.shoesapi.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeeklyReport {
    private String weekStart;
    private String weekEnd;
    private long completedOrder;
    private double earn;

    public WeeklyReport(String weekStart, String weekEnd, long completedOrder, double earn) {
        this.weekStart = weekStart;
        this.weekEnd = weekEnd;
        this.completedOrder = completedOrder;
        this.earn = earn;
    }
}
