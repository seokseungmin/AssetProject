package com.example.assetproject.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class Hardware extends Asset {
    private Long hardwareIdx;
    private String cpu;
    private String ssd;
    private String hdd;
    private String memory;
    private LocalDate usageDuration;
    private String note;
}
