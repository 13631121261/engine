
package com.kunlun.firmwaresystem.controller_user;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kunlun.firmwaresystem.device.*;
import com.kunlun.firmwaresystem.entity.*;
import com.kunlun.firmwaresystem.interceptor.ParamsNotNull;
import com.kunlun.firmwaresystem.mappers.*;
import com.kunlun.firmwaresystem.sql.*;
import com.kunlun.firmwaresystem.util.JsonConfig;
import com.kunlun.firmwaresystem.util.RedisUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.gatewayJson.Constant.*;
import static com.kunlun.firmwaresystem.util.JsonConfig.*;
@RestController





@Api(tags = "用户数据接口")
public class userApi {
    @Autowired
    private GatewayMapper gatewayMapper;
    private static int ExpireTime = 600;   // redis中存储的过期时间60s
    @Autowired
    private RedisUtils redisUtil;
    private static HashMap<String, Customer> customerHashMap;






    @ApiOperation(value = "获取设备告警记录", notes = "需要在header中填入登录时返回的唯一token值进行请求提交。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "登录时获取到的token值", dataType = "string", paramType = "header", required = true),
            @ApiImplicitParam(name = "quickSearch", value = "过滤设备的名称或者mac", dataType = "string", paramType = "query", required = false),
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "limit", value = "每页数量", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "alarm_object", value = "设备对象/n" +
                    "gateway:网关类型，tag：标签类型；bracelet：手环类型；all:全部类型", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "alarm_type", value = "报警类型" +
                    "sos_key:按键警报,sos_run：运动警报,sos_bt：低电量警报,sos_offline：离线警报,sos_online：上线警报，all:全部类型", dataType = "string", paramType = "query", required = true),


    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = " {\n" +
                    "      \"type\": \"KTBB835-1 信标类型\",\n" +
                    "      \"mac\": \"f0c810148238 唯一mac\",\n" +
                    "      \"bt\": \"0\" 电量或者电压值，离线时为0,\n" +
                    "      \"user_key\": \"admin\",管理员key\n" +
                    "      \"gateway_address\": null,距离最近的网关mac\n" +
                    "      \"online\": 0,是否在线\n" +
                    "      \"last_time\": 1730452161,在线时间\n" +
                    "      \"rssi\": 0,最后被网关接收到广播数据的信号值，离线为0\n" +
                    "      \"id\": 150,信标唯一id\n" +
                    "      \"n\": 0,预留\n" +
                    "      \"map_key\": \"bWFwXzE3MjY2NDYwNzg1MTY=\",当定位时，定位到对应的地图唯一key\n" +
                    "      \"sos\": -1,//-1：不支持，0：没有报警，1：按键报警\n" +
                    "      \"run\": -1,//-1：不支持，0：静止，1：运动\n" +
                    "      \"createtime\": 1728900910,添加时间\n" +
                    "      \"customer_key\": \"admin\",操作员的key,如果是超级管理员添加，则和user_key相同,\n" +
                    "      \"project_key\": \"57u05bCU6JaH5LqMXzE2OTE2Njg0MzcyMzg=\"//关联项目的key,\n" +
                    "      \"x\": 0,定位时的X轴位置\n" +
                    "      \"y\": 0,定位时的Y轴位置\n" +
                    "      \"gateway_devices\": 近期有扫描到该信标的网关列表,\n" +
                    "      \"use_datalist\": 用于定位的网关列表\n" +
                    "    }", response = Beacon.class)
    })
    @GetMapping(value = "/Api/getAlarm",produces = "application/json")
    public JSONObject getAlarm(HttpServletRequest request) {
        String quickSearch=request.getParameter("quickSearch");
        String alarm_object=request.getParameter("alarm_object");
        String alarm_type=request.getParameter("alarm_type");
        String page=request.getParameter("page");
        String limit=request.getParameter("limit");
        if(quickSearch==null||quickSearch.equals("")){
            quickSearch="";
        }
        if(alarm_object==null||alarm_object.equals("")){
            alarm_object="";
        }
        if(alarm_type==null||alarm_type.equals("")){
            alarm_type="";
        }
        if(page==null||page.equals("")){
            page="1";
        }
        if(limit==null||limit.equals("")){
            limit="10";
        }

        Customer user1 = getCustomer(request);
        Alarm_Sql alarm_sql = new Alarm_Sql();
        PageAlarm pageAlarm = alarm_sql.selectPageAlarm(alarmMapper,Integer.parseInt(page),Integer.parseInt(limit), user1.getProject_key(),alarm_object,alarm_type,quickSearch);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "ok");
        jsonObject.put("count", pageAlarm.getTotal());
        jsonObject.put("data",  pageAlarm.getAlarmList());
        return jsonObject;
    }







    @ApiOperation(value = "获取项目中的信标状态", notes = "需要在header中填入登录时返回的唯一token值进行请求提交。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "登录时获取到的token值", dataType = "string", paramType = "header", required = true),
            @ApiImplicitParam(name = "quickSearch", value = "过滤包含信标的名称或者mac", dataType = "string", paramType = "query", required = false),
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "limit", value = "每页数量", dataType = "int", paramType = "query", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = " {\n" +
                    "      \"type\": \"KTBB835-1 信标类型\",\n" +
                    "      \"mac\": \"f0c810148238 唯一mac\",\n" +
                    "      \"bt\": \"0\" 电量或者电压值，离线时为0,\n" +
                    "      \"user_key\": \"admin\",管理员key\n" +
                    "      \"gateway_address\": null,距离最近的网关mac\n" +
                    "      \"online\": 0,是否在线\n" +
                    "      \"last_time\": 1730452161,在线时间\n" +
                    "      \"rssi\": 0,最后被网关接收到广播数据的信号值，离线为0\n" +
                    "      \"id\": 150,信标唯一id\n" +
                    "      \"n\": 0,预留\n" +
                    "      \"map_key\": \"bWFwXzE3MjY2NDYwNzg1MTY=\",当定位时，定位到对应的地图唯一key\n" +
                    "      \"sos\": -1,//-1：不支持，0：没有报警，1：按键报警\n" +
                    "      \"run\": -1,//-1：不支持，0：静止，1：运动\n" +
                    "      \"createtime\": 1728900910,添加时间\n" +
                    "      \"customer_key\": \"admin\",操作员的key,如果是超级管理员添加，则和user_key相同,\n" +
                    "      \"project_key\": \"57u05bCU6JaH5LqMXzE2OTE2Njg0MzcyMzg=\"//关联项目的key,\n" +
                    "      \"x\": 0,定位时的X轴位置\n" +
                    "      \"y\": 0,定位时的Y轴位置\n" +
                    "      \"gateway_devices\": 近期有扫描到该信标的网关列表,\n" +
                    "      \"use_datalist\": 用于定位的网关列表\n" +
                    "    }", response = Beacon.class)
    })
    @GetMapping(value = "/Api/getBeacon",produces = "application/json")
    public JSONObject getAllBeacon(HttpServletRequest request) {
        Customer customer = getCustomer(request);
        if(customer==null){
            return JsonConfig.getJsonObj(CODE_OFFLINE,null,"Zn-Ch");
        }
        Beacon_Sql beacon_sql=new Beacon_Sql();
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
        //  println("1111111");
        PageBeacon pageBeacon=beacon_sql.selectPageBeacon(beaconMapper,page,limit,quickSearch,customer.getUserkey(),customer.getProject_key());
        if(pageBeacon.getBeaconList().size()>0){
            for(Beacon beacon:pageBeacon.getBeaconList()){
                Beacon beacon1=beaconsMap.get(beacon.getMac());
                beacon.setMap_key(beacon1.getMap_key());
                beacon.setOnline(beacon1.getOnline());
                if(beacon1.getOnline()==0){
                    beacon.setSos(-1);
                    beacon.setRun(-1);
                    beacon.setBt(0+"");
                }else{
                    beacon.setSos(beacon1.getSos());
                    beacon.setRun(beacon1.getRun());
                    beacon.setBt(beacon1.getBt());
                }
                beacon.setLastTime(beacon1.getLastTime());
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "ok");
        jsonObject.put("count", pageBeacon.getTotal());
        jsonObject.put("data", pageBeacon.getBeaconList());
        return jsonObject;
    }




    @ApiOperation(value = "获取该用户默认项目中关联的网关", notes = "需要在header中填入登录时返回的唯一token值进行请求提交。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "登录时获取到的token值", dataType = "string", paramType = "header", required = true),
            @ApiImplicitParam(name = "quickSearch", value = "过滤包含网关的名称或者mac", dataType = "string", paramType = "query", required = false),
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "limit", value = "每页数量", dataType = "int", paramType = "query", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "{\n" +
                    "    \"syn\": 0：未开启同步功能 ，1：开启同步功能,\n" +
                    "    \"syn_str\": 忽略,\n" +
                    "    \"id\": 网关唯一id,\n" +
                    "    \"name\": \"网关自定义名称，添加网关时填写\",\n" +
                    "    \"address\": \"网关唯一蓝牙MAC\",\n" +
                    "    \"wifi_address\": \"网关唯一Wifi MAC\",\n" +
                    "    \"wifi_version\": \"网关wifi版本\",\n" +
                    "    \"ble_version\": \"网关蓝牙版本\",\n" +
                    "    \"config_key\": \"关联的配置组，默认不关联\",\n" +
                    "    \"config_name\": \"配置组名称，默认为不关联\",\n" +
                    "    \"user_key\": \"管理员的key\",\n" +
                    "    \"create_time\": 添加到系统的时间,\n" +
                    "    \"update_time\": 编辑时间,\n" +
                    "    \"ip\": 网关在局域网的IP,\n" +
                    "    \"port\": 预留,\n" +
                    "    \"sub_topic\": \"网关订阅主题\",\n" +
                    "    \"pub_topic\": \"网关发布主题\",\n" +
                    "    \"server_type\": 预留,\n" +
                    "    \"scan_out\": 输出状态，0：上报，1：上报,\n" +
                    "    \"scan_interval\": 上报周期，单位毫秒,\n" +
                    "    \"report_type\": 1：广播包，2：输出响应包，3：仅上报mac+rssi,\n" +
                    "    \"filter_rssi\": \"信号过滤值\",\n" +
                    "    \"filter_name\": \"名称过滤值\",\n" +
                    "    \"filter_dev_mac\": \"mac过滤范围值\",\n" +
                    "    \"filter_ibeacon\": 1：仅上报ibeacon格式，0：不限制\n" +
                    "    \"filter_uuid\": \"UUID过滤值\",\n" +
                    "    \"filter_company_id\": \"Companyid过滤值\",\n" +
                    "    \"useplatform\": 预留,\n" +
                    "    \"online\": 0：离线，1：在线,\n" +
                    "    \"online_txt\": \"预留\",\n" +
                    "    \"lasttime\": 最新在线时间,\n" +
                    "    \"broadcast\": 0：不广播，1：广播,\n" +
                    "    \"x\": 地图上X轴坐标值,\n" +
                    "    \"y\": 地图上X轴坐标值,\n" +
                    "    \"z\": 地图上Z轴坐标值,\n" +
                    "    \"onlinetime\": 预留,\n" +
                    "    \"revicecount\": 预留,\n" +
                    "    \"n\": 预留,\n" +
                    "    \"area_id\": 预留,\n" +
                    "    \"area_name\": 预留,\n" +
                    "    \"customerkey\": 预留,\n" +
                    "    \"project_key\": \"关联的项目名称，一个网关仅存在于一个项目中\",\n" +
                    "    \"map_key\": \"关联的室内地图，一个网关仅存在于一份地图\",\n" +
                    "    \"map_name\": \"机场地图\",\n" +
                    "    \"reboot\": 预留,\n" +
                    "    \"isyn\": 0：未和关联配置同步参数 ，1：已经同步,\n" +
                    "    \"scan_filter_serv_data_uuid\": \"扫描过滤用户服务数据\",\n" +
                    "    \"wifi_onoff\": \"网关的wifi开启与关闭状态\",\n" +
                    "    \"wlan_ssid\": \"网关wifi SSID名称\",\n" +
                    "    \"wlan_ip\": \"网关自身IP\",\n" +
                    "    \"wlan_mask\": \"掩码\",\n" +
                    "    \"op_mode\": \"网口模式\",\n" +
                    "    \"wan_mode\": \"入网模式，有线无线或者4G\",\n" +
                    "    \"wan_ip\": \"网口或者4G IP\",\n" +
                    "    \"wan_mask\": \"对应的掩码\",\n" +
                    "    \"wan_gw\": \"对应的网关ID\",\n" +
                    "    \"up_time\": \"启动后的运行时间\",\n" +
                    "    \"arssi\": 预留,\n" +
                    "    \"crc_flag\": \"网关校验码，预留\"\n" +
                    "}", response = Gateway.class)
    })
    @GetMapping(value = "/Api/getGateway",produces = "application/json")
    public JSONObject getAllGateway(HttpServletRequest request, @RequestParam("page") @ParamsNotNull String page, @RequestParam("limit") @ParamsNotNull String limit){

        String quickSearch=request.getParameter("quickSearch");

        if(quickSearch==null||quickSearch.equals("")){
            quickSearch="";
        }
        if(page==null||page.equals("")){
            page="1";
        }
        if(limit==null||limit.equals("")){
            limit="10";
        }

        Customer customer=getCustomer(request);
        if(customer==null){
            return JsonConfig.getJsonObj(CODE_OFFLINE,null,"Zn-Ch");
        }
        Gateway_sql gateway_sql = new Gateway_sql();
        PageGateway pageGateway = gateway_sql.selectPageGateway(gatewayMapper, Integer.parseInt(page), Integer.parseInt(limit), quickSearch, customer.getUserkey(),customer.getProject_key());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", pageGateway.getTotal());
        List<Gateway> gateways=pageGateway.getGatewayList();

        for (Gateway gateway : gateways) {
            Gateway gateway1 = (Gateway) redisUtil.get(redis_key_gateway + gateway.getAddress());
            if(gateway1!=null){
                gateway.setLasttime(gateway1.getLasttime());
                gateway.setUp_time(gateway1.getUp_time());
                gateway.setCRC_FLAG(gateway1.getCRC_FLAG());
            }
            else{
                println("网关为空"+gateway.getAddress());
            }
            String syn = (String) redisUtil.get(redis_key_project_sys + gateway.getAddress());
            Integer revicecount = (Integer) redisUtil.get(redis_key_gateway_revice_count + gateway.getAddress());
            if (revicecount == null) {
                revicecount = 0;
            }
            gateway.setRevicecount(revicecount);
            if (syn != null && syn.equals("ok")) {
                gateway.setSyn(1);
            } else {
                gateway.setSyn(0);
                gateway.setSynStr(syn);
            }
        }
        jsonObject.put("data", pageGateway.getGatewayList());

        return jsonObject;
    }
    @ApiOperation(value = "获取该用户下关联的项目", notes = "一般超级管理员可以关联多个项目，子管理员只关联单个项目，需要在header中填入登录时返回的唯一token值进行请求提交。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "登录时获取到的token值", dataType = "string", paramType = "header", required = true),

    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "{\n" +
                    "  \"arssi\": A值，不对外开放接口,\n" +
                    "  \"create_time\": \"创建时间\",\n" +
                    "  \"id\": 项目唯一id,\n" +
                    "  \"info\": \"项目信息介绍\",\n" +
                    "  \"n\": N值，不对外设置,\n" +
                    "  \"project_key\": \"项目唯一key\",\n" +
                    "  \"project_name\": \"项目名称\",\n" +
                    "  \"update_time\": \"编辑更新时间\",\n" +
                    "  \"used\": 点位参数，不对外开放接口,\n" +
                    "  \"user_key\": \"主账号关联key\",\n" +
                    "  \"z\": 网关安装高度，不对外开放端口\n" +
                    "}", response = Project.class)
    })
    @GetMapping(value = "/Api/getProject",produces = "application/json")
    public com.alibaba.fastjson.JSONObject getProject(HttpServletRequest request) {
        Customer customer = getCustomer(request);
        if(customer==null){
            return JsonConfig.getJsonObj(CODE_OFFLINE,null,"Zn-Ch");
        }
        Project_Sql project_sql = new Project_Sql();
        List<Project> projects = null;
        if (customer != null && customer.getType() == 1) {
            projects = project_sql.getAllProject(projectMapper, customer.getUserkey(), "");
        } else if (customer != null && customer.getType() == 2) {
            projects = new ArrayList<>();
            Project p = project_sql.getProjectByKey(projectMapper, customer.getProject_key());
            if (p != null) {
                projects.add(p);
            }
        }
        try {
            if (customer != null) {
                return JsonConfig.getJsonObj(CODE_OK, projects, customer.getLang());
            }
            return JsonConfig.getJsonObj(CODE_SQL_ERROR, "", "zh-cn");
        }catch (Exception e){
            println("异常输出="+e);
            return JsonConfig.getJsonObj(CODE_SQL_ERROR, "", "zh-cn");
        }
    }


    @ApiOperation(value = "系统登录", notes = "根据子管理员的用户名和密码去登录,返回的token值，用于后续的接口，附带在header中进行请求提交。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "string", paramType = "query", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "{\n" +
                    "  \"msg\": \"执行成功\",\n" +
                    "  \"code\": 1  仅当返回数据中code==1登录成功，有唯一token返回,\n" +
                    "  \"data\": {\n" +
                    "    \"token\": \"YWRtaW5fMTczMTkxOTI4MzA4NQ==\"\n" +
                    "  }\n" +
                    "}", response = ExampleResponse.class)
    })
    @PostMapping(value = "/Api/login",produces = "application/json")

    public com.alibaba.fastjson.JSONObject login(String username,String password) {

        String response = null;
        if (customerHashMap == null) {
            customerHashMap = new HashMap<>();
        }
        JSONObject jsonObject1 = new JSONObject();
        //账号密码固定超过5个字符，后续在优化
        Customer customer = new Customer(username, password);
        String lang = customer.getLang();
        Customer_sql customer_sql = new Customer_sql();
        List<Customer> customerList = customer_sql.getCustomer(customerMapper, customer);
        if (customerList == null || customerList.isEmpty()) {
            jsonObject1 = JsonConfig.getJsonObj(JsonConfig.CODE_RESPONSE_NULL, null, lang);
        } else if (customerList.size() > 1) {
            jsonObject1 = JsonConfig.getJsonObj(JsonConfig.CODE_RESPONSE_MORE, null, lang);
        } else {
            customer = customerList.get(0);

            println("账号=" + customer);
            String toketn = "";
            if (customer.getType() == 1) {
                toketn = Base64.getEncoder().encodeToString((customer.getUserkey() + "_" + System.currentTimeMillis()).getBytes()).replaceAll("\\+", "");
            } else {
                toketn = Base64.getEncoder().encodeToString((customer.getCustomerkey() + "_" + System.currentTimeMillis()).getBytes()).replaceAll("\\+", "");
            }
            customerHashMap.put(toketn, customer);
            customer.setToken(toketn);
            customer.setRefresh_token("");
            customer_sql.updateCustomer(customerMapper, customer);
            // redisUtil.set("tokenId:"+customer.getCustomerkey() , "", ExpireTime);
            //设置新的token
            redisUtil.set("api_tokenId_", toketn, ExpireTime);
            //设置token对应内容
            //  redisUtil.set(toketn, customer, ExpireTime);
            jsonObject1.put("code", CODE_OK);
            jsonObject1.put("msg", CODE_OK_txt);
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("token", toketn);
            jsonObject1.put("data", jsonObject2);
            return jsonObject1;
        }
        return jsonObject1;
    }

    class ExampleResponse{
        /** token 登录返回的token，后续请求中，请放在请求header中*/
        String token="123456";

        public ExampleResponse(){
            setToken("121212");
        }
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    //专门给码石做的一个兼容，只上传单个项目固定的key，前提是需要第一个项目必须有一个子管理员账号。
    private Customer getCustomer(HttpServletRequest request) {
        if (customerHashMap == null) {
            customerHashMap = new HashMap<>();
        }
        String token = request.getHeader("token");
        Customer customer = null;
        if (!token.isEmpty()) {
            try {
                println("token=" + token);
                String old_token=(String) redisUtil.get("api_tokenId_");
                customer = customerHashMap.get(token);
                if (customer == null) {
                    Project_Sql projectSql = new Project_Sql();
                    Project p = projectSql.getProjectByKey(projectMapper, token);
                    println("项目="+p);
                    if (p != null) {
                        Customer_sql customer_sql = new Customer_sql();
                        customer = customer_sql.getCustomerByProjcetKey(customerMapper, token);
                        if (customer != null) {
                            customerHashMap.put(token, customer);
                        }
                    }
                }
                println("账户信息="+customer);
                return customer;
            } catch (Exception e) {
                println("异常设置" + e);
            }


        }
        return null;
    }
}
