package com.example.app.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.app.model.entity.SupplierEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;
@Mapper
public interface SupplierMapper extends BaseMapper<SupplierEntity> {
    List<String> getIds(String project_id);

    void deleteByIds(Set<String> ids);
}
