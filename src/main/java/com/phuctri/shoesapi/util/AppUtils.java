package com.phuctri.shoesapi.util;

import com.phuctri.shoesapi.exception.ShoesApiException;
import org.springframework.http.HttpStatus;

public class AppUtils {
    public static void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new ShoesApiException(HttpStatus.BAD_REQUEST, "Page number cannot be less than zero.");
        }

        if (size < 0) {
            throw new ShoesApiException(HttpStatus.BAD_REQUEST, "Size number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new ShoesApiException(HttpStatus.BAD_REQUEST, "Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public static void validatePriceFilter(double min, double max) {
        if (min > max) {
            throw new ShoesApiException(HttpStatus.BAD_REQUEST, "Min price cannot bigger than max price");
        }
    }
}
