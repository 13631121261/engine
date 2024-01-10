package com.kunlun.firmwaresystem;

import com.kunlun.firmwaresystem.device.Gateway;
import com.kunlun.firmwaresystem.entity.*;
import com.kunlun.firmwaresystem.entity.device.DeviceModel;
import com.kunlun.firmwaresystem.entity.device.Devicep;
import com.kunlun.firmwaresystem.entity.device.Deviceptype;
import com.kunlun.firmwaresystem.interceptor.HttpServletRequestReplacedFilter;
import com.kunlun.firmwaresystem.mappers.*;
import com.kunlun.firmwaresystem.mqtt.DirectExchangeProducer;
import com.kunlun.firmwaresystem.mqtt.MyMqttClient;
import com.kunlun.firmwaresystem.sql.*;
import com.kunlun.firmwaresystem.util.RedisUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static com.kunlun.firmwaresystem.gatewayJson.Constant.redis_key_locator;

@EnableCaching // 启用缓存功能
@EnableScheduling // 开启定时任务功能
@EnableTransactionManagement
@MapperScan(basePackages = "com.kunlun.firmwaresystem.mappers")
@SpringBootApplication
public class NewSystemApplication {

    //本地参数
   //public static final String paths="E:\\蓝牙网关\\固件版本\\photo\\";
    //public static final String url="http://192.168.1.11/download";
 //    public static final String host="http://localhost:801/";
    public static int beacon_time = 6;
    public static int wordcarda_time = 60;
//昆仑云参数

   public static final String paths = "C:\\Users\\Administrator\\Desktop\\WordSpace\\Web\\file\\photo\\";
  public static final String url = "http://120.77.232.76:80/download";

    //哲凌本地
   /* public static final String paths="D:\\kunlBluetooth\\上海环境监测蓝牙项目安装文件\\Server\\log\\";
    public static final String url="http://172.17.73.62:808/download";*/



    //信号在1米时的值暂定为-51；
    public static double rssi_At_1m = 47;
    public static RedisUtils redisUtil;
    public static WordCardaMapper wordCardaMapper;
    public static WifiMapper wifiMapper;
    public static DevicepTypeMapper devicepTypeMapper;
    public static BeaconMapper beaconMapper;
    public static BleMapper bleMapper;
    private static DeviceModelMapper deviceModelMapper;
    public static Gateway_configMapper gatewayConfigMapper;
    public static RulesMapper rulesMapper;
    public static GatewayMapper gatewayMapper;
    public static RecordMapper recordMapper;
    public static MofflineMapper mofflineMapper;
    public static DirectExchangeProducer directExchangeProducer;
    public static Map<String, String> gatewayMap;
    public static Map<String, Gateway> GatewayMap;
    public static Map<String, Gateway_config> gatewayConfigMap;
    public static Map<String, Rules> rulesMap;
    public static Map<String, Beacon> beaconsMap;
    public static Map<String, Wordcard_a> wordcard_aMap;
    public static Map<String, Customer> customerMap;
    public static Map<String, Beacon_tag> beacon_tagMap;
    public static Map<Integer, Area> area_Map;
    public static List<DeviceModel> deviceModels;
    public static AreaMapper areaMapper;
    public static AlarmMapper alarmMapper;
    public static Record_SosMapper recordSosMapper;
    public static UserMapper userMapper;
    public static BTagMapper bTagMapper;
    public static PersonMapper personMapper;
    public static Map<String,Person> personMap;
    public static Map<Integer,Fence> fenceMap;
    public static DevicePMapper devicePMapper;
    public static DeviceP_recordMapper devicePRecordMapper;
    public static Map<String, Devicep> devicePMap;
    public static Map<Integer, Deviceptype> devicePtypeMap;
    public static Map<String,Check_sheet> check_sheetMap;
    public static CheckSheetMapper checkSheetMapper;
    public static CheckRecordMapper checkRecordMapper;
    public static CustomerMapper customerMapper;
    public static MapMapper mapMapper;
    public static  FenceMapper fenceMapper;
    public static  LocatorMapper locatorMapper;
    @Autowired
    public void setDataSource( LocatorMapper locatorMapper,AlarmMapper alarmMapper, FenceMapper fenceMapper,MapMapper mapMapper, DeviceP_recordMapper devicePRecordMapper,MofflineMapper mofflineMapper,CheckRecordMapper checkRecordMapper,CheckSheetMapper checkSheetMapper,DevicepTypeMapper devicepTypeMapper,DevicePMapper devicePMapper,PersonMapper personMapper,BTagMapper bTagMapper, UserMapper userMapper,  CustomerMapper customerMapper, Record_SosMapper recordSosMapper, AreaMapper areaMapper, WordCardaMapper wordCardaMapper, RecordMapper recordMapper, BeaconMapper beaconMapper, WifiMapper wifiMapper, BleMapper bleMapper, Gateway_configMapper gateway_configMapper, RedisUtils redisUtil, DeviceModelMapper deviceModelMapper, DirectExchangeProducer topicExchangeProducer, GatewayMapper gatewayMapper, RulesMapper rulesMapper) {
        NewSystemApplication.redisUtil = redisUtil;
        NewSystemApplication.devicePRecordMapper =devicePRecordMapper;
        NewSystemApplication.deviceModelMapper = deviceModelMapper;
        NewSystemApplication.directExchangeProducer = topicExchangeProducer;
        NewSystemApplication.gatewayMapper = gatewayMapper;
        NewSystemApplication.gatewayConfigMapper = gateway_configMapper;
        NewSystemApplication.rulesMapper = rulesMapper;
        NewSystemApplication.bleMapper = bleMapper;
        NewSystemApplication.wifiMapper = wifiMapper;
        NewSystemApplication.beaconMapper = beaconMapper;
        NewSystemApplication.recordMapper = recordMapper;
        NewSystemApplication.wordCardaMapper = wordCardaMapper;
        NewSystemApplication.areaMapper = areaMapper;
        NewSystemApplication.recordSosMapper = recordSosMapper;
        NewSystemApplication.userMapper = userMapper;
        NewSystemApplication.bTagMapper = bTagMapper;
        NewSystemApplication.personMapper=personMapper;
        NewSystemApplication.devicePMapper=devicePMapper;
        NewSystemApplication.devicepTypeMapper=devicepTypeMapper;
        NewSystemApplication.checkSheetMapper=checkSheetMapper;
        NewSystemApplication.checkRecordMapper=checkRecordMapper;
        NewSystemApplication.customerMapper=customerMapper;
        NewSystemApplication.mofflineMapper=mofflineMapper;
        NewSystemApplication.mapMapper=mapMapper;
        NewSystemApplication.fenceMapper=fenceMapper;
        NewSystemApplication.alarmMapper=alarmMapper;
        NewSystemApplication.locatorMapper=locatorMapper;
    }

    /* @Autowired
     private RedisUtils redisUtil;
     @Autowired
     DeviceModelMapper deviceModelMapper;*/
    public static void main(String[] args) {

        SpringApplication.run(NewSystemApplication.class, args);
        //  System.out.println("启动结果："+redisUtil+"====>"+deviceModelMapper);

        // client.addSubTopic("GwData");
 /*
        String id="178BFBFF00860F016FB55D78-B9E5-11EA-80DE-002B67BD78A6";

        try {
            File file=new File(paths+"kunlun");
            file.mkdir();
            file=new File(paths+"kunlun/kunlun.key");
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] bytes=id.getBytes();
            outputStream.write(bytes);
            outputStream.close();
        }catch (Exception e){
            return;
        }
//wmic csproduct get UUID
        //92E011EF-C31C-4DB4-869D-BE922BD1532B
       File file=new File(paths+"kunlun/kunlun.license");
        if(!file.exists()){
            System.out.println("需要激活文件，退出");
            System.exit(0);
            return;
        }else{
            try {
                FileInputStream inputStream = new FileInputStream(file);
                byte[] bytes=new byte[inputStream.available()];
                inputStream.read(bytes);
                String license=new String(bytes).replaceAll(" ","");
               System.out.println("验证吗="+license+"原来码="+(MachineCodeUtil.encode(id,"KUNLUN")+"acfc"));
                if(!(MachineCodeUtil.encode(id,"KUNLUN")+"acfc").equals(license)){
                    System.out.println("激活码不对");
                    System.exit(0);
                }
            }catch (Exception e){
                System.out.println("异常="+e);
                return;
            }
        }
*/
        System.out.println("线程=" + Thread.currentThread().getName());
        DeviceModel_sql deviceModel_sql = new DeviceModel_sql();
        deviceModels = deviceModel_sql.getAllModel(redisUtil, deviceModelMapper);
        Gateway_sql gateway_sql = new Gateway_sql();
        gatewayMap = gateway_sql.getAllGateway(redisUtil, gatewayMapper);
        GatewayMap= gateway_sql.getAllGateway( gatewayMapper);
        GatewayConfig_sql project_sql = new GatewayConfig_sql();
        gatewayConfigMap = project_sql.getAllConfig(gatewayConfigMapper);

        Rules_sql r = new Rules_sql();
        rulesMap = r.getAllRules(rulesMapper);

        Btag_Sql btag_sql = new Btag_Sql();
        beacon_tagMap = btag_sql.getAllBeacon(bTagMapper);

        Fence_Sql fence_sql=new Fence_Sql();
       fenceMap=  fence_sql.getAllFence(fenceMapper);
        Person_Sql person_sql=new Person_Sql();
        personMap=  person_sql.getAllPerson(personMapper);
        DevicePType_Sql devicePType_sql=new DevicePType_Sql();
        devicePtypeMap=devicePType_sql.getAllDeviceP(devicepTypeMapper);
        DeviceP_Sql deviceP_sql=new DeviceP_Sql();
        devicePMap=deviceP_sql.getAllDeviceP(devicePMapper);
        Beacon_Sql beacon_sql = new Beacon_Sql();
        beaconsMap = beacon_sql.getAllBeacon(beaconMapper);
        WordCarda_Sql wordCarda_sql = new WordCarda_Sql();
        wordcard_aMap = wordCarda_sql.getAllWordCarda(wordCardaMapper);
     /*   User_sql user_sql = new User_sql();
        userMap = user_sql.getAllUser(userMapper);*/
        Customer_sql customer_sql = new Customer_sql();
        customerMap = customer_sql.getAllCustomer(customerMapper);
        CheckSheet_Sql checkSheet_sql=new CheckSheet_Sql();
        check_sheetMap=checkSheet_sql.getCheckSheet(checkSheetMapper);
        Area_Sql area_sql=new Area_Sql();
        area_Map= area_sql.getAllArea(areaMapper);

        Map_Sql map_sql=new Map_Sql();
        map_sql.getAllMap(mapMapper,redisUtil);
       /*  String a="aaa";
         String[] b=a.split("1");
         System.out.println("长度="+b[0]);*/
        /*topicExchangeProducer.send("连接","connect");
        topicExchangeProducer.send("状态","state");
        topicExchangeProducer.send("扫描","scan_report");*/

        List<Locator> locators=  locatorMapper.selectList(null);
        if(locators!=null&&locators.size()>0){
            for(Locator locator:locators){
                redisUtil.setnoTimeOut(redis_key_locator+locator.getAddress(),locator);
            }
        }
        MyMqttClient client = MyMqttClient.getMyMqttClient();
        client.start();
        MyWebSocket webSocket = MyWebSocket.getWebSocket();
        webSocket.start();
        MyWebSocketTag webSockettag = MyWebSocketTag.getWebSocket();
        webSockettag.start();


    }
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();
        c.setIgnoreUnresolvablePlaceholders(true);
        return c;
    }

    @Bean
    public FilterRegistrationBean httpServletRequestReplacedRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new HttpServletRequestReplacedFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("httpServletRequestReplacedFilter");
        registration.setOrder(1);
        return registration;
    }

}
