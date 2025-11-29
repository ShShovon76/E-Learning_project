package com.example.E_Learning_CRUD.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponseDTO {
    private Long id;
    private Long userId;
    private String address;
    private String city;
    private String country;
    private String timezone;
    private String language;
    private String title;
    private String expertise;
    private boolean verified;
}
