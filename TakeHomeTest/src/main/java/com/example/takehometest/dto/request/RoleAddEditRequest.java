package com.example.takehometest.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.List;

@Data
public class RoleAddEditRequest {
    @JsonProperty("role_name")
    private String roleName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("is_admin")
    private Boolean isAdmin;

    @JsonProperty("feature_list")
    private List<Long> featureList;
}
