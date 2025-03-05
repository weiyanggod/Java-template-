package com.example.app.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 项目
 */
@TableName("project")
public class ProjectEntity {
    /**
     * 主键
     */
    private String id;
    /**
     * 编号
     */
    private String code;
    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目开始竞价的时间
     */
    private String startTime;

    /**
     * 项目结束竞价的时间
     */
    private LocalDateTime endTime;

    /**
     * 拦标价
     */
    private Integer bidPrice;
    /**
     * 采购内容
     */
    private String value;
    /**
     * 采购内容明细
     */
    private String valueDetails;

    /**
     * 当前轮次
     */

    private Integer nowRound = 1;

    /**
     * 总轮次
     */
    private Integer round;
    /**
     * 最终拦标价
     */
    private Integer finalBidPrice;
    /**
     * 状态
     */
    private String status;

    /**
     * 每轮竞价时长
     */
    private Integer duration;

    /**
     * 创建人
     */
    private String createName;

    /**
     * 创建人id
     */
    private String creatId;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")
    private LocalDateTime createdTime;

    @TableField(exist = false)
    private List<SupplierEntity> supplierList;

    @TableField(exist = false)
    private List<QuoteEntity> quoteList;

}
