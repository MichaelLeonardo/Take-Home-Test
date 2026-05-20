package com.example.takehometest.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseFilterCriteria {
    private String key;
    private String operation;
    private Object value;
    private String mode;
}
