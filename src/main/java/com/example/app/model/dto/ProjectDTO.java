package com.example.app.model.dto;
import com.baomidou.mybatisplus.annotation.TableField;
import com.example.app.model.entity.SupplierEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
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
    private String endTime;

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
     * 轮次
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
     * 供应商列表
     */
    @TableField(exist = false)
    private List<SupplierEntity> supplierList;

    /**
     * 每轮竞价时长
     */
    private Integer duration;

}
