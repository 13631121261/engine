package com.kunlun.firmwaresystem.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kunlun.firmwaresystem.entity.Moffline;
import com.kunlun.firmwaresystem.entity.Record_sos;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface MofflineMapper extends BaseMapper<Moffline> {
    @Override
    int insert(Moffline moffline);
}
