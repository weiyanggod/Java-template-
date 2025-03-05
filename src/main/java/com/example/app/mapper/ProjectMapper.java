package com.example.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.app.model.entity.ProjectEntity;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface ProjectMapper extends BaseMapper<ProjectEntity> {
}
