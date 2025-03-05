package com.example.app.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.app.model.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
    void deleteByIds(Set<String> ids);
}
