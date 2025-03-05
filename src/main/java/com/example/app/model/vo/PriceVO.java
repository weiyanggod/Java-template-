package com.example.app.model.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceVO {

    private String id;

    private Integer round;

    private Integer number;

    private String supplierId;

    private String supplierName;

    private Integer rank;
}
