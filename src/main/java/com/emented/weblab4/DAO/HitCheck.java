package com.emented.weblab4.DAO;

import lombok.Data;

import java.time.Instant;


@Data
public class HitCheck {

    private Integer id;
    private Integer userId;
    private Double x;
    private Double y;
    private Double r;
    private Instant checkDate;
    private Long executionTime;
    private Boolean status;

}
