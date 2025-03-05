package com.example.app.config;
import com.example.app.model.dto.UserDTO;
public class GlobalProperties {
    private static final ThreadLocal<UserDTO> userEntity = new ThreadLocal<>();
    
    public static UserDTO getUserEntity() {
        return userEntity.get();
    }
    public static void setUserEntity(UserDTO user) {
        GlobalProperties.userEntity.set(user);
    }
    public static void clear() {
        userEntity.remove();
    }
}
