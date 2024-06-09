package com.phuctri.shoesapi.payload.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FilterRequest {
    public String search;
    public double rating;
    public long fromPrice;
    public long toPrice;
    public long brandId;
    public FilterSortBy sortBy;
}

enum FilterSortBy {
    POPULAR,
    MOST_RECENT,
    PRICE_HIGH,
    PRICE_LOW
}
