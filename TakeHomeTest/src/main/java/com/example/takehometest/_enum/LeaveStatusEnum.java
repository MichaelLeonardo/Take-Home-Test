package com.example.takehometest._enum;

import lombok.Getter;

@Getter
public enum LeaveStatusEnum {
    PENDING(1, "Pending"),
    APPROVED(2, "Approved"),
    REJECTED(3, "Rejected"),
    ;

    private Integer id;
    private String name;

    private LeaveStatusEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static LeaveStatusEnum getById(Integer id) {
        for (LeaveStatusEnum e : values()) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }
}
