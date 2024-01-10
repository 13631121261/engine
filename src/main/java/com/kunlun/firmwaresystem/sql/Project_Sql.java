package com.kunlun.firmwaresystem.sql;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kunlun.firmwaresystem.device.PageMap;
import com.kunlun.firmwaresystem.entity.Map;
import com.kunlun.firmwaresystem.entity.Project;
import com.kunlun.firmwaresystem.mappers.MapMapper;
import com.kunlun.firmwaresystem.mappers.ProjectMapper;

import java.util.List;

public class Project_Sql {
    public boolean addProject(ProjectMapper projectMapper, Project project) {
        boolean status = check(projectMapper, project.getProject_key());
        if (status) {
            return false;
        } else {
            projectMapper.insert(project);
            return true;
        }

    }

    public List<Project> getAllProject(ProjectMapper projectMapper, String key,String name) {
        QueryWrapper<Project> queryWrapper = Wrappers.query();
        queryWrapper.eq("user_key", key);
        queryWrapper.like("project_name", name);
        List<Project> mapList = projectMapper.selectList(queryWrapper);
        return mapList;
    }
    public int update(ProjectMapper projectMapper, String project_key,String user_key,String info,String name,String time) {
        UpdateWrapper<Project> queryWrapper = Wrappers.update();
        queryWrapper.eq("project_key", project_key);
        queryWrapper.eq("user_key", user_key);
        queryWrapper.set("project_name",name);
        queryWrapper.set("info",info);
        queryWrapper.set("update_time",time);
        return  projectMapper.update(null,queryWrapper);
    }
    public int delete(ProjectMapper projectMapper, String project_key,String user_key) {
        QueryWrapper<Project> queryWrapper = Wrappers.query();
        queryWrapper.eq("project_key", project_key);
        queryWrapper.eq("user_key", user_key);
        return projectMapper.delete(queryWrapper);
    }



    public boolean check(ProjectMapper projectMapper, String project_key) {
        QueryWrapper<Project> queryWrapper = Wrappers.query();
        queryWrapper.eq("project_key", project_key);
        Project project = projectMapper.selectOne(queryWrapper);
        if (project == null) {
            return false;
        } else {
            return true;
        }
    }
}