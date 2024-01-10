package com.kunlun.firmwaresystem.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kunlun.firmwaresystem.entity.device.DeviceBarn;
import com.kunlun.firmwaresystem.entity.device.DeviceOutBarn;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface DeviceOutBarnMapper extends BaseMapper<DeviceOutBarn> {
}
