package com.example.app.service.impl;

import com.example.app.mapper.PersonMapper;
import com.example.app.model.dto.Person;
import com.example.app.service.PersonService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonMapper personMapper;

    @Override
    public void getList() {
        PageHelper.startPage(1, 10);  // Start pagination
        PageInfo<Person> List = new PageInfo<>(personMapper.getList());
        System.out.println("Total records: " + List.getTotal());
        System.out.println("Results: " + List.getList());
    }
}
