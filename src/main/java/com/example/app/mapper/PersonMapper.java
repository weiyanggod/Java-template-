package com.example.app.mapper;
import com.example.app.model.dto.Person;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PersonMapper {

    List<Person> getList();
}
