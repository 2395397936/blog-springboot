package com.example.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private String nickname;
    private String password;
    private String email;
    private String code;
    private String inviteCode;
}
