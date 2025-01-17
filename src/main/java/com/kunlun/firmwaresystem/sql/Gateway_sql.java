package com.kunlun.firmwaresystem.sql;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kunlun.firmwaresystem.NewSystemApplication;
import com.kunlun.firmwaresystem.device.Gateway;
import com.kunlun.firmwaresystem.device.PageGateway;
import com.kunlun.firmwaresystem.entity.Project;
import com.kunlun.firmwaresystem.entity.web_Structure.GatewayTree;
import com.kunlun.firmwaresystem.mappers.GatewayMapper;
import com.kunlun.firmwaresystem.util.RedisUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.gatewayJson.Constant.*;

public class Gateway_sql {
    public boolean addGateway(GatewayMapper gatewayMapper, Gateway gateway) {
        if (!checkGateway(gatewayMapper, gateway)) {
            println("输出="+gateway.toString());
            gatewayMapper.insert(gateway);
            return true;
        } else {
            return false;
        }
    }

    public int updateGateway(GatewayMapper gatewayMapper, Gateway gateway) {
      //  println("更新网关3="+gateway);
        UpdateWrapper updateWrapper = new UpdateWrapper();//照搬
        updateWrapper.eq("address", gateway.getAddress());
        return gatewayMapper.update(gateway, updateWrapper);
    }
    public int updateGateway(GatewayMapper gatewayMapper,int id,int isyn) {
        println("更新网关2=");
        UpdateWrapper updateWrapper = new UpdateWrapper();//照搬
        updateWrapper.set("isyn", isyn);
        updateWrapper.eq("id", id);
        return gatewayMapper.update(null,updateWrapper);
    }
    public int updateGateway(GatewayMapper gatewayMapper,int id,String config_key,String config_name) {
        UpdateWrapper updateWrapper = new UpdateWrapper();//照搬
        updateWrapper.eq("id", id);
        updateWrapper.set("config_key", config_key);
        updateWrapper.set("config_name", config_name);
        return gatewayMapper.update(null,updateWrapper);
    }
    public int delete(GatewayMapper gatewayMapper, String address) {

        UpdateWrapper updateWrapper = new UpdateWrapper();//照搬
        updateWrapper.eq("address", address);
        return gatewayMapper.delete(updateWrapper);
    }
    public int deletes(GatewayMapper gatewayMapper, List<Integer> id) {
        return gatewayMapper.deleteBatchIds(id);
    }
    public int updateGateway(GatewayMapper gatewayMapper, String address, String name, String wifiAddress, String projectKey, String config_name, double x, double y) {
        UpdateWrapper updateWrapper = new UpdateWrapper();//照搬
        updateWrapper.eq("address", address);
        updateWrapper.set("config_key", projectKey);
        updateWrapper.set("name", name);
        updateWrapper.set("wifi_address", wifiAddress);
        updateWrapper.set("project_name", config_name);
        updateWrapper.set("x", x);
        updateWrapper.set("y", y);
        Gateway gateway = (Gateway) redisUtil.get(redis_key_gateway + address);
        gateway.setConfig_key(projectKey);
        gateway.setWifi_address(wifiAddress);
        gateway.setConfig_name(config_name);
        gateway.setName(name);
        redisUtil.set(redis_key_gateway + address, gateway);
        return gatewayMapper.update(null, updateWrapper);
    }

    public int updateGateway(GatewayMapper gatewayMapper, String address, String projectKey) {
        UpdateWrapper updateWrapper = new UpdateWrapper();//照搬
        updateWrapper.eq("address", address);
        updateWrapper.set("project_key", projectKey);
        Gateway gateway = (Gateway) redisUtil.get(redis_key_gateway + address);
        gateway.setConfig_key(projectKey);
        redisUtil.set(redis_key_gateway + address, gateway);
        return gatewayMapper.update(null, updateWrapper);
    }
    /**
     * 查询是否已c存在此网关设备
     */
    private boolean checkGateway(GatewayMapper gatewayMapper, Gateway gateway) {
        QueryWrapper<Gateway> queryWrapper = Wrappers.query();
        queryWrapper.eq("address", gateway.getAddress());
        // queryWrapper.eq("username", user.getCustomername());
//若是数据库中符合传入的条件的记录有多条，那就不能用这个方法，会报错
        List<Gateway> a = gatewayMapper.selectList(queryWrapper);
        if (a != null && a.size() > 0) {
            return true;
        } else
            return false;
    }
    public Gateway getGatewayById(GatewayMapper gatewayMapper, int id) {
       Gateway gateway= gatewayMapper.selectById(id);
       return  gateway;
    }
    public Gateway getGatewayByMac(GatewayMapper gatewayMapper, String mac) {
        QueryWrapper<Gateway> queryWrapper = Wrappers.query();
        queryWrapper.eq("address",mac);
        List<Gateway> list=gatewayMapper.selectList(queryWrapper);
        if(list!=null&&list.size()==1){
            return list.get(0);
        }
        return  null;
    }

    public Map<String, String> getAllGateway(RedisUtils redisUtil, GatewayMapper gatewayMapper) {
        println("执行一次获取全部数据");
        List<Gateway> gatewayList = gatewayMapper.selectList(null);
        HashMap<String, String> gatewayMap = new HashMap<>();
        Project_Sql projectSql=new Project_Sql();
        List<Project> projects=   projectSql.getAllProject(projectMapper);
        for (Gateway gateway : gatewayList) {
            for(Project project:projects){
                if(gateway.getProject_key().equals(project.getProject_key())){
                    if(project.getArssi()==0){
                        project.setArssi(NewSystemApplication.rssi_At_1m);
                        projectMapper.updateById(project);
                    }
                    if(project.getN()==0){
                        project.setN(NewSystemApplication.N);
                        projectMapper.updateById(project);
                    }
                    if(gateway.getArssi()==0||gateway.getN()==0){
                        gateway.setArssi(project.getArssi());
                        gateway.setN(project.getN());
                    }
                    if(gateway.getZ()==0){
                        gateway.setZ(project.getZ());
                    }
                }
            }
            //println("初始化"+gateway.getSub_topic()+"==="+gateway.getPub_topic());
            redisUtil.set(redis_key_gateway + gateway.getAddress(), gateway);
            gatewayMap.put(gateway.getAddress(), gateway.getAddress());
            redisUtil.set(redis_key_gateway_onLine_time + gateway.getAddress(), null);
            redisUtil.set(redis_key_gateway_revice_count + gateway.getAddress(), 0);
        }
        return gatewayMap;
    }


    public Map<String, String> updateGateway(RedisUtils redisUtil, GatewayMapper gatewayMapper,String config_key) {
        println("更新一次取全部数据");
        QueryWrapper<Gateway> queryWrapper = Wrappers.query();
        queryWrapper.eq("config_key",config_key);
        List<Gateway> gatewayList = gatewayMapper.selectList(queryWrapper);
        HashMap<String, String> gatewayMap = new HashMap<>();

        for (Gateway gateway : gatewayList) {
            //println("初始化"+gateway.getSub_topic()+"==="+gateway.getPub_topic());
            gateway.setConfig_name("不关联配置");
            gateway.setConfig_key("noconfig_key");
            updateGateway(gatewayMapper,gateway);
            redisUtil.set(redis_key_gateway + gateway.getAddress(), gateway);
        }
        return gatewayMap;
    }

    public Map<String, Gateway> getAllGateway( GatewayMapper gatewayMapper) {
        println("执行11一次获取全部数据");
        List<Gateway> gatewayList = gatewayMapper.selectList(null);
        HashMap<String, Gateway> gatewayMap = new HashMap<>();
        Project_Sql projectSql=new Project_Sql();
        List<Project> projects=   projectSql.getAllProject(projectMapper);
        for (Gateway gateway : gatewayList) {
            for(Project project:projects){
                if(gateway.getProject_key().equals(project.getProject_key())){
                    if(project.getArssi()==0){
                        project.setArssi(NewSystemApplication.rssi_At_1m);

                        projectMapper.updateById(project);
                    }
                    if(project.getN()==0){
                        project.setN(NewSystemApplication.N);
                        projectMapper.updateById(project);
                    }
                    if(gateway.getArssi()==0||gateway.getN()==0){
                        gateway.setArssi(project.getArssi());
                        gateway.setN(project.getN());
                    }
                    if(gateway.getZ()==0){
                        gateway.setZ(project.getZ());
                    }
                }
            }
            gatewayMap.put(gateway.getAddress(), gateway);
        }
        return gatewayMap;
    }
    public   List<GatewayTree> getAllGateway(GatewayMapper gatewayMapper, String user_key, String project_key,String config_key) {
        println("执行333一次获取全部数据");
        QueryWrapper<Gateway> queryWrapper = Wrappers.query();
        queryWrapper.eq("user_key",user_key);
        queryWrapper.eq("project_key",project_key);
        queryWrapper.ne("config_key",config_key);
        List<Gateway> gatewayList = gatewayMapper.selectList(queryWrapper);
        HashMap<String, List<GatewayTree>> gatewayMap = new HashMap<>();
        for (Gateway gateway : gatewayList) {
            GatewayTree gatewayTree=new GatewayTree();
            gatewayTree.setId(gateway.getId());
            gatewayTree.setDisabled(false);
            gatewayTree.setLabel(gateway.getName());
            gatewayTree.setAddress(gateway.getAddress());

            List<GatewayTree> gatewayList1= gatewayMap.get(gateway.getConfig_name());
            if(gatewayList1==null){
                gatewayList1=new ArrayList<>();
                gatewayMap.put(gateway.getConfig_name(),gatewayList1);
            }
            gatewayList1.add(gatewayTree);
        }
        List<GatewayTree> only=new ArrayList<>();
        int i=-100;
        for(String key:gatewayMap.keySet()){
            List<GatewayTree> trees=gatewayMap.get(key);
            GatewayTree gatewayTree=new GatewayTree();
            gatewayTree.setLabel(key);
            gatewayTree.setId(i);
            gatewayTree.setChildren(trees);
            gatewayTree.setAddress("网关MAC");
            only.add(gatewayTree);
            i++;
        }
        return only;
    }

    public   List<Gateway> getAllGateways(GatewayMapper gatewayMapper, String user_key, String project_key,String config_key) {
        println("执行44一次获取全部数据");
        QueryWrapper<Gateway> queryWrapper = Wrappers.query();
        queryWrapper.eq("user_key",user_key);
        queryWrapper.eq("project_key",project_key);
        queryWrapper.eq("config_key",config_key);
        List<Gateway> gatewayList = gatewayMapper.selectList(queryWrapper);
        return gatewayList;
    }
    public   List<Gateway> getGatewayByMapKey(GatewayMapper gatewayMapper, String user_key, String project_key,String map_key) {
        QueryWrapper<Gateway> queryWrapper = Wrappers.query();
        queryWrapper.eq("user_key",user_key);
        queryWrapper.eq("project_key",project_key);
        queryWrapper.eq("map_key",map_key);
        List<Gateway> gatewayList = gatewayMapper.selectList(queryWrapper);
        return gatewayList;
    }

    public   List<GatewayTree> getGatewayByconfig_key(GatewayMapper gatewayMapper, String user_key, String project_key,String config_key) {
        QueryWrapper<Gateway> queryWrapper = Wrappers.query();
        queryWrapper.eq("user_key",user_key);
        queryWrapper.eq("project_key",project_key);
        queryWrapper.eq("config_key",config_key);
        List<Gateway> gatewayList = gatewayMapper.selectList(queryWrapper);
        HashMap<String, List<GatewayTree>> gatewayMap = new HashMap<>();
        for (Gateway gateway : gatewayList) {
            GatewayTree gatewayTree=new GatewayTree();
            gatewayTree.setId(gateway.getId());
            gatewayTree.setDisabled(false);
            gatewayTree.setLabel(gateway.getName());
            gatewayTree.setAddress(gateway.getAddress());

            List<GatewayTree> gatewayList1= gatewayMap.get(gateway.getConfig_name());
            if(gatewayList1==null){
                gatewayList1=new ArrayList<>();
                gatewayMap.put(gateway.getConfig_name(),gatewayList1);
            }
            gatewayList1.add(gatewayTree);
        }
        List<GatewayTree> only=new ArrayList<>();
        int i=0;
        for(String key:gatewayMap.keySet()){
            List<GatewayTree> trees=gatewayMap.get(key);
            GatewayTree gatewayTree=new GatewayTree();
            gatewayTree.setLabel(key);
            gatewayTree.setId(i);
            gatewayTree.setChildren(trees);
            gatewayTree.setAddress("网关MAC");
            only.add(gatewayTree);
        }


        return only;
    }
    public PageGateway selectPageGateway(GatewayMapper gatewayMapper, int page, int limt, String quickSearch, String userKey,String project_key) {
       // println("执行55一次获取全部数据"+page+"  "+limt);
        LambdaQueryWrapper<Gateway> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        Page<Gateway> userPage = new Page<>(page, limt);
        IPage<Gateway> userIPage;
        //println("user_key="+userKey+" project_key="+project_key);
        userLambdaQueryWrapper.eq(Gateway::getUser_key, userKey).eq(Gateway::getProject_key,project_key).like(Gateway::getAddress, quickSearch).or().eq(Gateway::getUser_key, userKey).eq(Gateway::getProject_key,project_key).like(Gateway::getName,quickSearch);

       /* if (project_name != null && project_name.length() > 0) {
            GatewayConfig_sql project_sql = new GatewayConfig_sql();
            List<Gateway_config> gatewayConfigList = project_sql.getLikeProject(projectMapper, project_name, userKey);
            if (gatewayConfigList == null || gatewayConfigList.size() == 0) {
                PageGateway pageGateway = new PageGateway(null, 0, 0);
                return pageGateway;
            } else {
                for (int i = 0; i < gatewayConfigList.size(); i++) {
                    userLambdaQueryWrapper.eq(Gateway::getConfig_key, gatewayConfigList.get(i).getConfig_key());
                }
            }
        }*/
        userIPage = gatewayMapper.selectPage(userPage, userLambdaQueryWrapper);
        //    println("总页数： "+userIPage.getPages());
        //println("总记录数： "+userIPage.getTotal());
        // userIPage.getRecords().forEach(System.out::println);
        PageGateway pageGateway = new PageGateway(userIPage.getRecords(), userIPage.getPages(), userIPage.getTotal());
        return pageGateway;
    }



}