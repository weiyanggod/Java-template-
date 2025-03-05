package com.example.app.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
// 报价表
@TableName("price")
public class PriceEntity {
    private String id;

    private Integer round;

    private Integer number;

    private String supplierId;

    private String supplierName;

    private LocalDateTime editTime;

}
