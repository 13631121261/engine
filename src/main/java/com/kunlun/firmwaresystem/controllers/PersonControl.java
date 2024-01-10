package com.kunlun.firmwaresystem.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunlun.firmwaresystem.device.PagePerson;
import com.kunlun.firmwaresystem.entity.Beacon;
import com.kunlun.firmwaresystem.entity.Customer;
import com.kunlun.firmwaresystem.entity.Department;
import com.kunlun.firmwaresystem.entity.Person;
import com.kunlun.firmwaresystem.entity.device.Devicep;
import com.kunlun.firmwaresystem.interceptor.ParamsNotNull;
import com.kunlun.firmwaresystem.mappers.DepartmentMapper;
import com.kunlun.firmwaresystem.mappers.PersonMapper;
import com.kunlun.firmwaresystem.sql.Beacon_Sql;
import com.kunlun.firmwaresystem.sql.Department_Sql;
import com.kunlun.firmwaresystem.sql.Person_Sql;
import com.kunlun.firmwaresystem.util.JsonConfig;
import com.kunlun.firmwaresystem.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.util.JsonConfig.*;

@RestController
public class PersonControl {
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    private PersonMapper personMapper;
    @Autowired
    private RedisUtils redisUtil;
    @RequestMapping(value = "/userApi/Person/index", method = RequestMethod.GET, produces = "application/json")
    public JSONObject selectPerson(HttpServletRequest request){
        String quickSearch=request.getParameter("quickSearch");
        String pages=request.getParameter("page");
        String limits=request.getParameter("limit");
        int page=1;
        int limit=10;
        if (!StringUtils.isBlank(pages)) {
            page=Integer.parseInt(pages);
        }
        if (!StringUtils.isBlank(limits)) {
            limit=Integer.parseInt(limits);
        }
        if (StringUtils.isBlank(quickSearch)) {
            quickSearch="";
        }
        Customer customer=getCustomer(request);
        Person_Sql person_sql=new Person_Sql();
        int p_id=-1;

        PagePerson personList=person_sql.selectPagePerson(personMapper,Integer.valueOf(page),Integer.valueOf(limit),quickSearch,customer.getUserkey(),customer.getProject_key());
        for(Person person:personList.getPersonList()){
            Person person1=personMap.get(person.getIdcard());
            person.setGateway_name(person1.getGateway_name());
            person.setGateway_mac(person1.getGateway_mac());
            person.setB_area_name(person1.getB_area_name());
            person.setMap_name(person1.getMap_name());
           person.setOnline(person1.getOnline());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", personList.getTotal());
        jsonObject.put("data", personList.getPersonList());
        return jsonObject;
    }
    @RequestMapping(value = "userApi/Person/add", method = RequestMethod.POST, produces = "application/json")
    public JSONObject addPerson(HttpServletRequest request, @RequestBody JSONObject json) {
        Customer customer=getCustomer(request);
        Person_Sql person_sql=new Person_Sql();
        Person person=new Gson().fromJson(json.toString(),new TypeToken<Person>(){}.getType());
        person.setUser_key(customer.getUserkey());
        person.setCustomer_key(customer.getCustomerkey());
        person.setProject_key(customer.getProject_key());
        Department department= departmentMapper.selectById(person.getDepartment_id());
        if(department!=null){
            person.setDepartment_name(department.getName());
        }else{
            person.setDepartment_id(0);
        }
        if(person.getBind_mac()!=null&&person.getBind_mac().length()>0){
            person.setIsbind(1);
            Beacon beacon= beaconsMap.get(person.getBind_mac());
            beacon.setIsbind(1);
            beacon.setBind_type(2);
            beacon.setDevice_sn(person.getIdcard());
            beacon.setDevice_name(person.getName());
            Beacon_Sql beacon_sql=new Beacon_Sql();
            beacon_sql.update(beaconMapper,beacon);
        }
        person.setCreate_time(System.currentTimeMillis()/1000);
        boolean status=person_sql.addPerson(personMapper,person);
        if(status){
            personMap.put(person.getIdcard(),person);
            return JsonConfig.getJsonObj(CODE_OK,null);
        }
        else{
            return JsonConfig.getJsonObj(CODE_REPEAT,null);
        }
    }
    @RequestMapping(value = "userApi/Person/edit", method = RequestMethod.POST, produces = "application/json")
    public JSONObject editPerson(HttpServletRequest request, @RequestBody JSONObject json) {
        try {
            Customer customer = getCustomer(request);
            System.out.println("111");
            Person_Sql person_sql = new Person_Sql();
            Person person = new Gson().fromJson(json.toString(), new TypeToken<Person>() {
            }.getType());
            System.out.println(person);
            person.setUser_key(customer.getUserkey());
            person.setCustomer_key(customer.getCustomerkey());
            person.setProject_key(customer.getProject_key());
            person.setUpdate_time(System.currentTimeMillis() / 1000);
            Department department = departmentMapper.selectById(person.getDepartment_id());
            if (department != null) {
                person.setDepartment_name(department.getName());
            } else {
                person.setDepartment_id(0);
            }
            System.out.println("222");
            Person person1 = person_sql.getPersonById(personMapper, person.getId() + "");
            Beacon_Sql beacon_sql = new Beacon_Sql();
            System.out.println("222"+person1);
            //原来设备有绑定，有变更
            //处理旧的信标
            if (person1.getBind_mac() != null && person1.getBind_mac().length() > 0 && !person1.getBind_mac().equals(person.getBind_mac())) {
                List<Beacon> beacons = beacon_sql.getBeaconByMac(beaconMapper, person.getUser_key(), person.getProject_key(), person1.getBind_mac());
                if (beacons == null || beacons.size() != 1) {
                    return JsonConfig.getJsonObj(CODE_SQL_ERROR, null);
                }
                Beacon beacon1 = beacons.get(0);
                beacon1.setBind_type(0);
                beacon1.setIsbind(0);
                beacon1.setDevice_sn("");
                beacon1.setDevice_name("");
                beacon_sql.update(beaconMapper, beacon1);
                beaconsMap.put(beacon1.getMac(), beacon1);
            }
            //有绑定，处理新的信标
            System.out.println("3333"+person.getBind_mac());
            if (person.getBind_mac().length() > 0 && !person.getBind_mac().equals("不绑定标签")) {
                System.out.println("4444");
                person.setIsbind(1);
                List<Beacon> beacons = beacon_sql.getBeaconByMac(beaconMapper, person.getUser_key(), person.getProject_key(), person.getBind_mac());
                System.out.println("555"+person.getBind_mac());
                if (beacons == null || beacons.size() != 1) {
                    return JsonConfig.getJsonObj(CODE_SQL_ERROR, null);
                }
                Beacon beacon1 = beacons.get(0);
                beacon1.setIsbind(1);
                beacon1.setBind_type(2);
                beacon1.setDevice_sn(person.getIdcard());
                beacon1.setDevice_name(person.getName());
                beacon_sql.update(beaconMapper, beacon1);
                beaconsMap.put(beacon1.getMac(), beacon1);
            }
            System.out.println("666");
            if (person.getBind_mac().length() == 0 || person.getBind_mac() == null || person.getBind_mac().equals("不绑定标签")) {
                person.setIsbind(0);
                person.setBind_mac("");
            }
            personMap.put(person.getIdcard(), person);


            boolean status = person_sql.update(personMapper, person);
            if (status) {
                personMap.put(person.getIdcard(), person);
                return JsonConfig.getJsonObj(CODE_OK, null);
            } else {
                return JsonConfig.getJsonObj(CODE_REPEAT, null);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
  /*  //其实和编辑是一样的,c传递的参数
    @RequestMapping(value = "userApi/Person/bind", method = RequestMethod.POST, produces = "application/json")
    public JSONObject bindPerson(HttpServletRequest request, @RequestBody JSONObject json) {
        Customer customer=getCustomer(request);
        Person_Sql person_sql=new Person_Sql();
        Person person=new Gson().fromJson(json.toString(),new TypeToken<Person>(){}.getType());
        person.setUser_key(customer.getUserkey());
        person.setCustomer_key(customer.getCustomerkey());
        person.setProject_key(customer.getProject_key());

        boolean status=person_sql.update(personMapper,person);
        if(status){
            personMap.put(person.getIdcard(),person);
            return JsonConfig.getJsonObj(CODE_OK,null);
        }
        else{
            return JsonConfig.getJsonObj(CODE_REPEAT,null);
        }
    }*/
    //解绑
    @RequestMapping(value = "userApi/Person/unbind", method = RequestMethod.GET, produces = "application/json")
    public JSONObject unbindPerson(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "idcard") String idcard) {
        System.out.println("返回状态"+idcard);
        Person person=personMap.get(idcard);
        for(String key:personMap.keySet()){
            System.out.println("身份证="+key);
        }
        if(person==null){
            System.out.println("返回状态1");
            return JsonConfig.getJsonObj(CODE_SQL_ERROR,null);
        }
        System.out.println("返回状态2");
        if(person!=null&&person.getIsbind()==1){
            System.out.println("返回状态3");
            String mac=person.getBind_mac();
            if(mac!=null&&mac.length()>0){
                System.out.println("返回状态44");
                Beacon beacon=beaconsMap.get(mac);
                if(beacon!=null){
                    beacon.setBind_type(0);
                    beacon.setDevice_name("");
                    beacon.setDevice_sn("");
                    beacon.setIsbind(0);
                    Beacon_Sql beacon_sql=new Beacon_Sql();
                    beacon_sql.update(beaconMapper,beacon);
                }else{
                    return JsonConfig.getJsonObj(CODE_SQL_ERROR,null);
                }


            }
        }
        person.setIsbind(0);
        person.setBind_mac("");
        Person_Sql person_sql=new Person_Sql();
        System.out.println("返回状态55");
       boolean status=  person_sql.update(personMapper,person);
        if(status){
            personMap.put(person.getIdcard(),person);
            System.out.println("返回状态666");
            return JsonConfig.getJsonObj(CODE_OK,null);
        }
        else{
            System.out.println("返回状态77");
            return JsonConfig.getJsonObj(CODE_SQL_ERROR,null);
        }
    }
    @RequestMapping(value = "userApi/Person/edit", method = RequestMethod.GET, produces = "application/json")
    public JSONObject editPerson(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "id") String id) {
        Customer customer=getCustomer(request);
        Person_Sql person_sql=new Person_Sql();
       Person person=person_sql.getPersonById(personMapper,id);
        if(person!=null){
            return JsonConfig.getJsonObj(CODE_OK,person);
        }else{
            return JsonConfig.getJsonObj(CODE_RESPONSE_NULL,null);
        }
    }
    @RequestMapping(value = "userApi/Person/del", method = RequestMethod.POST, produces = "application/json")
    public JSONObject deleteBeacon(HttpServletRequest request, @RequestBody JSONArray jsonArray) {
        Customer customer = getCustomer(request);
        Person_Sql person_sql=new Person_Sql();
        List<Integer> id=new ArrayList<Integer>();
        for(Object ids:jsonArray){
            if(ids!=null&&ids.toString().length()>0){
                id.add(Integer.parseInt(ids.toString()));
                for(String key:personMap.keySet()){
                    Person person=personMap.get(key);
                    if(person!=null&&person.getId()==Integer.parseInt(ids.toString())&&person.getIsbind()==1){
                        return JsonConfig.getJsonObj(CODE_10,null);
                    }
                    for(String sn:devicePMap.keySet()){
                        Devicep devicep=devicePMap.get(sn);
                        if(devicep!=null&&devicep.getIdcard()!=null&&devicep.getIdcard().equals(key)){
                            return JsonConfig.getJsonObj(CODE_10,null);
                        }
                    }
                }

            }
        }


        if(id.size()>0){

            int status = person_sql.deletes(personMapper, id);

            if(status!=-1){
                personMap=person_sql.getAllPerson(personMapper);
                return JsonConfig.getJsonObj(CODE_OK,null);
            }else{
                return JsonConfig.getJsonObj(CODE_SQL_ERROR,null);
            }
        }else{
            return JsonConfig.getJsonObj(CODE_PARAMETER_NULL,null);
        }
    }
    @RequestMapping(value = "/userApi/Person/index1", method = RequestMethod.GET, produces = "application/json")
    public JSONObject selectAllPerson( HttpServletRequest request){
        Customer customer=getCustomer(request);
        Person_Sql person_sql=new Person_Sql();
        List<Person> personList=person_sql.getAllPerson(personMapper,customer.getUserkey(),customer.getProject_key());
        JSONObject jsonObject = new JSONObject();
        personList.add(0,new Person("-1","不绑定人员"));
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", personList.size());
        jsonObject.put("data", personList);
        return jsonObject;
    }

    private Customer getCustomer(HttpServletRequest request) {
        String  token=request.getHeader("batoken");
        Customer customer = (Customer) redisUtil.get(token);
        return customer;
    }
}
