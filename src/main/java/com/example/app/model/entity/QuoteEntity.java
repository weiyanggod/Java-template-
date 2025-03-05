package com.example.app.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
// 轮次表
@TableName("quote")
public class QuoteEntity {

    private String id;

    private Integer round;

    private String projectId;

    private String status;

    /**
     * 剩余时间
     */
    private Integer leftTime;

    // 报价列表
    @TableField(exist = false)
    private List<PriceEntity> priceList;

    /**
     * 当前排名
     */
    @TableField(exist = false)
    private Integer rank;

}
