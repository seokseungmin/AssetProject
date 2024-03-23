package com.example.assetproject.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Software extends Asset {
    private Long softwareIdx;
    private LocalDate expiryDate;
    private String note;

    public void update(LocalDate expiryDate, String note) {
        this.expiryDate = expiryDate;
        this.note = note;
    }
}