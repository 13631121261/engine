package com.kunlun.firmwaresystem.sql;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kunlun.firmwaresystem.device.PageBeacon;
import com.kunlun.firmwaresystem.entity.Area;
import com.kunlun.firmwaresystem.entity.Beacon;
import com.kunlun.firmwaresystem.entity.Check_record;
import com.kunlun.firmwaresystem.entity.Check_sheet;
import com.kunlun.firmwaresystem.mappers.AreaMapper;
import com.kunlun.firmwaresystem.mappers.BeaconMapper;
import com.kunlun.firmwaresystem.mappers.CheckSheetMapper;

import java.util.HashMap;
import java.util.List;

public class CheckSheet_Sql {

        public void addCheck_sheet(CheckSheetMapper checkSheetMapper,Check_sheet check_sheet){
            checkSheetMapper.insert(check_sheet);
        }

    public HashMap<String,Check_sheet> getCheckSheet(CheckSheetMapper checkSheetMapper){
        LambdaQueryWrapper<Check_sheet> userLambdaQueryWrapper = Wrappers.lambdaQuery();

        List<Check_sheet> check_sheets=checkSheetMapper.selectList(userLambdaQueryWrapper);

        HashMap<String,Check_sheet> map=new HashMap<String,Check_sheet>();
        for(Check_sheet check_sheet:check_sheets ){
            map.put(check_sheet.getUserkey(),check_sheet);
        }

        return map;
    }
 public Check_sheet getCheckSheet(CheckSheetMapper checkSheetMapper,String userkey){
     LambdaQueryWrapper<Check_sheet> userLambdaQueryWrapper = Wrappers.lambdaQuery();
     userLambdaQueryWrapper.eq(Check_sheet::getUserkey,userkey);
     List<Check_sheet> check_sheets=checkSheetMapper.selectList(userLambdaQueryWrapper);
     Check_sheet check_sheet=null;
     if(check_sheets==null||check_sheets.size()==0){
         check_sheet=new    Check_sheet("系统定义",0,6,5,userkey);
         checkSheetMapper.insert(check_sheet);
         check_sheets= checkSheetMapper.selectList(null);
         check_sheet=check_sheets.get(0);
     }else{
         check_sheet=check_sheets.get(0);
     }

     return check_sheet;
 }
    public void update(CheckSheetMapper checkSheetMapper,Check_sheet check_sheet){
        checkSheetMapper.updateById(check_sheet);
    }

}