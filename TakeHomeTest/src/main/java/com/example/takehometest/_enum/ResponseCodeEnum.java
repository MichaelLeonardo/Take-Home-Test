package com.example.takehometest._enum;

import lombok.Getter;

@Getter
public enum ResponseCodeEnum {

    OK("200", "Sukses"), OK_PARTIAL("206", "Success, partial content"),
    ERROR("503", "Maaf, terjadi kesalahan pada server"), BAD_REQUEST("400", "Tolong cek kembali data yang dimasukkan"),
    UNAUTHORIZED("401", "Anda tidak diperbolehkan mengakses menu ini"),
    FORBIDDEN("403", "Anda tidak memiliki akses ke menu ini"), NOT_FOUND("404", "Data tidak ditemukan"),
    CREATED("200", "Sukses, data berhasil ditambahkan"), UPDATED("200", "Sukses, data berhasil diperbaharui"),
    DELETED("200", "Data berhasil dihapus"), REQUEST_ENTITY_TOO_LARGE("413", "Ukuran file terlalu besar");

    private String code;
    private String description;

    private ResponseCodeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
