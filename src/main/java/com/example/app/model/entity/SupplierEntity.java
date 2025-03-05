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
/**
 * 供应商
 */
@TableName("supplier")
public class SupplierEntity {
    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 税号
     */
    private String taxId;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系电话
     */
    private String contactNumber;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 账号类型
     */
    private String accountType;

    @TableField(exist = false)
    private List<QuoteEntity> quoteList;

}
