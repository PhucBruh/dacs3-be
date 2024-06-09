package com.phuctri.shoesapi.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UserProfile {
    private long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String username;
    private List<String> roles;
}
