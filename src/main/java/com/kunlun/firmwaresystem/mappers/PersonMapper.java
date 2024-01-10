package com.kunlun.firmwaresystem.mappers;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kunlun.firmwaresystem.entity.Department;
import com.kunlun.firmwaresystem.entity.Person;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Mapper
@Component
public interface PersonMapper extends BaseMapper<Person> {

}
