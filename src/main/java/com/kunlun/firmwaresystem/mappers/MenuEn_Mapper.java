package com.kunlun.firmwaresystem.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kunlun.firmwaresystem.entity.Menu;
import com.kunlun.firmwaresystem.entity.Menu_en;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface MenuEn_Mapper extends BaseMapper<Menu_en> {
}
