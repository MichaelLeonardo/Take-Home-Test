package com.example.takehometest.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PropertyHelper {
    @Value("${superadmin.password}")
    private String superadminPassword;
}
