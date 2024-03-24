package com.example.assetproject.entity;

import com.example.assetproject.entity.Asset;
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

    public void update(String cpu, String ssd, String hdd, String memory, String note) {
        this.cpu = cpu;
        this.ssd = ssd;
        this.hdd = hdd;
        this.memory = memory;
        this.note = note;
        // usageDuration is not updated here, assuming it's not changed in this context
    }
}
