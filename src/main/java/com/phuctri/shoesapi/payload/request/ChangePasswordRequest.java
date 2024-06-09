package com.phuctri.shoesapi.payload.request;

import lombok.Getter;

@Getter
public class ChangePasswordRequest {
    public String oldPassword;
    public String newPassword;
}
