package com.example.takehometest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponse {

    @JsonProperty("is_admin")
    private Boolean isAdmin;

    @JsonProperty("list_menu_url")
    private List<String> listMenuUrl;
}
