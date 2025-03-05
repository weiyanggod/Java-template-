package com.example.app.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
// 此类用于展示供应商的账号密码
public class SupplierVO {

    /**
     * 用户id
     */
    private String id;

    private String username;

    private String password;

}
