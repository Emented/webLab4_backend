package com.emented.weblab4.DTO;

import lombok.Data;

import java.time.Instant;

@Data
public class HitCheckDTO {

    private Double x;
    private Double y;
    private Double r;
    private Instant checkDate;
    private Long executionTime;
    private Boolean status;

}
