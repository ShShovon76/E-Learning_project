package com.example.E_Learning_CRUD.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileRequestDTO {
    private String address;
    private String city;
    private String country;
    private String timezone;
    private String language;
    private boolean emailNotifications;
    private boolean pushNotifications;
    private String title;
    private String expertise;
    private String website;
    private String linkedin;
    private boolean verified;
}
