package com.kunlun.firmwaresystem.sql;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kunlun.firmwaresystem.device.PageGatewayConfig;
import com.kunlun.firmwaresystem.entity.Gateway_config;
import com.kunlun.firmwaresystem.mappers.Gateway_configMapper;


import java.util.HashMap;
import java.util.List;

import static com.kunlun.firmwaresystem.NewSystemApplication.println;

public class GatewayConfig_sql {
    public boolean addGatewayConfig(Gateway_configMapper gatewayConfigMapper, Gateway_config gatewayConfig) {
        if (check(gatewayConfigMapper, gatewayConfig)) {
            gatewayConfigMapper.insert(gatewayConfig);
            return true;
        } else {
            return false;
        }
    }

    public int updateGatewayConfig(Gateway_configMapper projectMapper, Gateway_config gatewayConfig) {
        UpdateWrapper updateWrapper = new UpdateWrapper();//照搬
        updateWrapper.eq("config_key", gatewayConfig.getConfig_key());
        return projectMapper.update(gatewayConfig, updateWrapper);
    }
    public int updateGatewayConfig(Gateway_configMapper projectMapper, String config_key,int count ) {
        UpdateWrapper updateWrapper = new UpdateWrapper();//照搬
        updateWrapper.eq("config_key", config_key);
        updateWrapper.set("gateway_count",count);
        return projectMapper.update(null, updateWrapper);
    }
    public boolean bindRules(Gateway_configMapper projectMapper, Gateway_config gatewayConfig, String rulesKey, String name) {
        UpdateWrapper updateWrapper = new UpdateWrapper();//照搬
        updateWrapper.eq("config_key", gatewayConfig.getConfig_key());
        updateWrapper.set("rules_key", rulesKey);
        updateWrapper.set("rules_name", name);
        int result = projectMapper.update(null, updateWrapper);
        if (result > 0) {
            println("code=" + result);
            return true;
        } else {
            println("code=" + result + "---" + gatewayConfig.getConfig_key());
            return false;
        }
    }

    public PageGatewayConfig selectPageConfig(Gateway_configMapper projectMapper, int page, int limt, String project_name, String userKey) {
        LambdaQueryWrapper<Gateway_config> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        Page<Gateway_config> userPage = new Page<>(page, limt);
        IPage<Gateway_config> userIPage;
        userLambdaQueryWrapper.eq(Gateway_config::getUser_key, userKey);
        if (project_name != null && project_name.length() > 0) {
            userLambdaQueryWrapper.like(Gateway_config::getName, project_name);
        }
        userIPage = projectMapper.selectPage(userPage, userLambdaQueryWrapper);
        println("总页数： " + userIPage.getPages());
        println("总记录数： " + userIPage.getTotal());
        // userIPage.getRecords().forEach(System.out::println);
        PageGatewayConfig pageGatewayConfig = new PageGatewayConfig(userIPage.getRecords(), userIPage.getPages(), userIPage.getTotal());
        return pageGatewayConfig;
    }

    public List<Gateway_config> getAllConfig(Gateway_configMapper projectMapper, String userKey,String project_key) {
        QueryWrapper<Gateway_config> queryWrapper = Wrappers.query();
        queryWrapper.eq("user_key", userKey);
        queryWrapper.eq("project_key", project_key);
        return projectMapper.selectList(queryWrapper);
    }
    public List<Gateway_config> getConfigByKey(Gateway_configMapper projectMapper, String userKey, String project_Key,String config_key) {
        QueryWrapper<Gateway_config> queryWrapper = Wrappers.query();
        queryWrapper.eq("user_key", userKey);
        queryWrapper.eq("project_key", project_Key);
        queryWrapper.eq("config_key", config_key);
        return projectMapper.selectList(queryWrapper);
    }
    public List<Gateway_config> getConfigById(Gateway_configMapper projectMapper, List<Integer> id) {
        List< Gateway_config> gateway_configs=projectMapper.selectBatchIds(id);
        return gateway_configs;
    }
    public Gateway_config selectByKey(Gateway_configMapper projectMapper, String project_key, String user_key) {
        QueryWrapper<Gateway_config> queryWrapper = Wrappers.query();
        queryWrapper.eq("config_key", project_key);
        queryWrapper.eq("user_key", user_key);
        return projectMapper.selectOne(queryWrapper);
    }


    public HashMap<String, Gateway_config> getAllConfig(Gateway_configMapper gatewayConfigMapper) {

        List<Gateway_config> gatewayConfigList = gatewayConfigMapper.selectList(null);
        HashMap<String, Gateway_config> projectMap = new HashMap<>();
        for (Gateway_config gatewayConfig : gatewayConfigList) {
            projectMap.put(gatewayConfig.getConfig_key(), gatewayConfig);
        }
        return projectMap;
    }

    public int delete(Gateway_configMapper gatewayConfigMapper, String key, String userKey) {
        UpdateWrapper updateWrapper = new UpdateWrapper();//照搬
        updateWrapper.eq("config_key", key);
        updateWrapper.eq("user_key", userKey);
        return gatewayConfigMapper.delete(updateWrapper);
    }

    public List<Gateway_config> getProjectByRules(Gateway_configMapper gatewayConfigMapper, String rulesKey) {
        QueryWrapper<Gateway_config> queryWrapper = Wrappers.query();
        queryWrapper.eq("rules_key", rulesKey);
        List<Gateway_config> a = gatewayConfigMapper.selectList(queryWrapper);
        return a;
    }


    public List<Gateway_config> getLikeProject(Gateway_configMapper gatewayConfigMapper, String name, String userKey) {
        QueryWrapper<Gateway_config> queryWrapper = Wrappers.query();
        queryWrapper.like("name", name);
        queryWrapper.eq("user_key", userKey);
        List<Gateway_config> a = gatewayConfigMapper.selectList(queryWrapper);
        return a;
    }

    /**
     * 查询是否已存在此用户
     */
    private boolean check(Gateway_configMapper gatewayConfigMapper, Gateway_config gatewayConfig) {
        QueryWrapper<Gateway_config> queryWrapper = Wrappers.query();
        queryWrapper.eq("name", gatewayConfig.getName());
        queryWrapper.eq("user_key", gatewayConfig.getCustomer_key());
        List<Gateway_config> a = gatewayConfigMapper.selectList(queryWrapper);
        if (a != null && a.size() > 0) {
            return false;
        } else
            return true;
    }
}
