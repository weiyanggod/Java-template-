package com.example.app.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class UserEntity {

    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 账号类型
     */
    private String accountType;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

}
