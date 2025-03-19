package com.kunlun.firmwaresystem.sql;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kunlun.firmwaresystem.device.PageTagLog;
import com.kunlun.firmwaresystem.entity.Alarm;
import com.kunlun.firmwaresystem.entity.Tag_log;
import com.kunlun.firmwaresystem.mappers.AlarmMapper;
import com.kunlun.firmwaresystem.mappers.TagLogMapper;

import java.util.List;

import static com.kunlun.firmwaresystem.NewSystemApplication.println;

public class TagLogSql {
    public boolean addLog(TagLogMapper tagLogMapper, Tag_log tagLog) {
        tagLogMapper.insert(tagLog);
            return true;
    }
    //清除30秒
    public void deleteBy30s(TagLogMapper tagLogMapper, long time){
        QueryWrapper<Tag_log> queryWrapper = Wrappers.query();
        queryWrapper.le("create_time",time);
        // println("时间");
        tagLogMapper.delete(queryWrapper);

    }
    public void keepLatest200Records(TagLogMapper tagLogMapper) {
        // 1. 查询第 200 条记录的 id
        LambdaQueryWrapper<Tag_log> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Tag_log::getId).last("LIMIT 1 OFFSET 199");

        List<Tag_log> list = tagLogMapper.selectList(queryWrapper);
        if (list.isEmpty()) return;

        Long minId = list.get(0).getId();

        // 2. 删除旧数据
        LambdaQueryWrapper<Tag_log> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.lt(Tag_log::getId, minId);

        tagLogMapper.delete(deleteWrapper);
    }
    public PageTagLog selectPageLog(TagLogMapper tagLogMapper, int page, int limt, String address,String project_key) {

        LambdaQueryWrapper<Tag_log> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        Page<Tag_log> userPage = new Page<>(page, limt);
        IPage<Tag_log> userIPage;
            println("这里执行66666"+address+"   "+project_key);
            userLambdaQueryWrapper.like(Tag_log::getBeacon_address, address).eq(Tag_log::getProject_key, project_key).orderByDesc(true,Tag_log::getId)
                             .or().like(Tag_log::getGateway_address, address).eq(Tag_log::getProject_key, project_key).orderByDesc(true,Tag_log::getId)
                             .or().like(Tag_log::getGateway_name, address).eq(Tag_log::getProject_key, project_key).orderByDesc(true,Tag_log::getId);
        println("这里执行9999=");
      userIPage = tagLogMapper.selectPage(userPage, userLambdaQueryWrapper);
        println("userIPage="+userIPage.getPages());
        return new PageTagLog(userIPage.getRecords(), userIPage.getPages(), userIPage.getTotal());
    }

    public  void deletes(List<Integer> ids){

    }
}