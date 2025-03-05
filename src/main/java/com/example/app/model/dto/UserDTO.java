package com.example.app.model.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    /**
     * 主键
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

}
