package com.kunlun.firmwaresystem.sql;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kunlun.firmwaresystem.entity.Roles;
import com.kunlun.firmwaresystem.mappers.RolesMapper;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Roles_Sql {
    public boolean addroles(RolesMapper rolesMapper, Roles roles) {
        boolean status = check(rolesMapper, roles);
        if (status) {
            return false;
        } else {
            rolesMapper.insert(roles);
            return true;
        }
    }
    public void delete(RolesMapper rolesMapper, int id) {
        QueryWrapper<Roles> queryWrapper = Wrappers.query();
        queryWrapper.eq("id", id);
        rolesMapper.delete(queryWrapper);
    }
    public void update(RolesMapper rolesMapper, Roles roles) {

        rolesMapper.updateById(roles);
    }


    public   List<Roles> getAllroles(RolesMapper rolesMapper,String project_key) {
        QueryWrapper<Roles> queryWrapper = Wrappers.query();
        queryWrapper.eq("project_key",project_key);
        List<Roles> roles= rolesMapper.selectList(queryWrapper);
        return roles;

    }
    public   Roles getOneRoles(RolesMapper rolesMapper,int id) {
       Roles roles= rolesMapper.selectById(id);
        return roles;

    }
    public boolean check(RolesMapper rolesMapper, Roles roles) {
        QueryWrapper<Roles> queryWrapper = Wrappers.query();
        queryWrapper.eq("name", roles.getName());
        queryWrapper.eq("project_key", roles.getProject_key());
        Roles roles1 = rolesMapper.selectOne(queryWrapper);
        if (roles1 == null) {
            return false;
        } else {
            return true;
        }
    }
}