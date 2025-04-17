package com.kunlun.firmwaresystem;

import com.kunlun.firmwaresystem.Tcp.NettyTcpServer;
import com.kunlun.firmwaresystem.device.Gateway;
import com.kunlun.firmwaresystem.entity.*;
import com.kunlun.firmwaresystem.entity.device.DeviceModel;
import com.kunlun.firmwaresystem.interceptor.HttpServletRequestReplacedFilter;
import com.kunlun.firmwaresystem.mappers.*;
import com.kunlun.firmwaresystem.mqtt.DirectExchangeProducer;
import com.kunlun.firmwaresystem.mqtt.MyMqttClient;
import com.kunlun.firmwaresystem.mqtt.TMyMqttClient;
import com.kunlun.firmwaresystem.sql.*;
import com.kunlun.firmwaresystem.util.RedisUtils;
import io.netty.channel.ChannelFuture;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kunlun.firmwaresystem.gatewayJson.Constant.redis_key_locator;

@EnableCaching // 启用缓存功能
@EnableScheduling // 开启定时任务功能
@EnableTransactionManagement
@MapperScan(basePackages = "com.kunlun.firmwaresystem.mappers")
@SpringBootApplication
public class NewSystemApplication {
    @Autowired

    //本地参数
  // public static final String paths="E:\\蓝牙网关\\固件版本\\photo\\";
   // public static final String url="http://192.168.1.14/download";
 //    public static final String host="http://localhost:801/";
    public static int beacon_time = 6;
    public static int wordcarda_time = 60;
//昆仑云参数

/*
   public static final String paths = "C:\\Users\\Administrator\\Desktop\\WordSpace\\Web\\file\\photo\\";
  public static final String rooturl = "http://192.168.1.14:7001/download?";
*/

    //哲凌本地
   /* public static final String paths="D:\\kunlBluetooth\\上海环境监测蓝牙项目安装文件\\Server\\log\\";
    public static final String url="http://172.17.73.62:808/download";*/


    public static MyMqttClient  client;
    //信号在1米时的值暂定为-51；
    public static int rssi_At_1m = -47;
    public static double N=2.67;
    public static RedisUtils redisUtil;
    public static WordCardaMapper wordCardaMapper;
    public static WifiMapper wifiMapper;
    public static BeaconMapper beaconMapper;
    public static BraceletMapper braceletMapper;
    public static BleMapper bleMapper;
    private static DeviceModelMapper deviceModelMapper;
    public static Gateway_configMapper gatewayConfigMapper;
    public static RulesMapper rulesMapper;
    public static GatewayMapper gatewayMapper;
    public static TagLogMapper tagLogMapper;
    public static RecordMapper recordMapper;
    public static MofflineMapper mofflineMapper;
    public static DirectExchangeProducer directExchangeProducer;
    public static Map<String, String> gatewayMap;
    public static Map<String, Integer> usedMap;
    public static Map<String, Gateway> GatewayMap;
    public static Map<String, Gateway_config> gatewayConfigMap;
    public static Map<String, Rules> rulesMap;
    public static Map<String, Beacon> beaconsMap;

    public static Map<String, Bracelet> braceletsMap;
 //   public static Map<String, MyMqttClient> myMqttClientMap;
   // public static Map<String, TMyMqttClient> t_myMqttClientMap;
    public static Map<String, Wordcard_a> wordcard_aMap;
    public static Map<String, FWordcard> fWordcardMap;
    public static Map<String, Customer> customerMap;
    public static Map<String, Beacon_tag> beacon_tagMap;
    public static Map<String, byte[]> file_cache;
    public static List<DeviceModel> deviceModels;
    public static HistoryMapper historyMapper;
    public static LogsMapper logsMapper;
    public static AlarmMapper alarmMapper;
    public static Record_SosMapper recordSosMapper;
    public static UserMapper userMapper;
    public static BTagMapper bTagMapper;
    public static ConfigMapper configMapper;
    public static HashMap<String,Firmware_task> firmwareTaskHashMap;

    public  static ProjectMapper projectMapper;

    public static DeviceP_recordMapper devicePRecordMapper;


    //public static Map<String,Check_sheet> check_sheetMap;
    public static CheckSheetMapper checkSheetMapper;
    public static CheckRecordMapper checkRecordMapper;
    public static CustomerMapper customerMapper;
    public static MapMapper mapMapper;
    public static  FenceMapper fenceMapper;
    public static NettyTcpServer nettyTcpServer;
    public static FWordcardMapper fWordcardMapper;

    @Autowired
    public void setDataSource(  ConfigMapper configMapper,ProjectMapper projectMapper,LogsMapper logsMapper, TagLogMapper tagLogMapper, FWordcardMapper fWordcardMapper,  NettyTcpServer nettyTcpServer,HistoryMapper historyMapper,AlarmMapper alarmMapper, FenceMapper fenceMapper,MapMapper mapMapper, DeviceP_recordMapper devicePRecordMapper,MofflineMapper mofflineMapper,CheckRecordMapper checkRecordMapper,CheckSheetMapper checkSheetMapper,BTagMapper bTagMapper, UserMapper userMapper,  CustomerMapper customerMapper, Record_SosMapper recordSosMapper, WordCardaMapper wordCardaMapper, RecordMapper recordMapper, BeaconMapper beaconMapper,BraceletMapper braceletMapper,  WifiMapper wifiMapper, BleMapper bleMapper, Gateway_configMapper gateway_configMapper, RedisUtils redisUtil, DeviceModelMapper deviceModelMapper, DirectExchangeProducer topicExchangeProducer, GatewayMapper gatewayMapper, RulesMapper rulesMapper) {
        NewSystemApplication.configMapper=configMapper;
        NewSystemApplication.tagLogMapper=tagLogMapper;
        NewSystemApplication.projectMapper=projectMapper;
        NewSystemApplication.redisUtil = redisUtil;
        NewSystemApplication.fWordcardMapper=fWordcardMapper;
        NewSystemApplication.devicePRecordMapper =devicePRecordMapper;
        NewSystemApplication.deviceModelMapper = deviceModelMapper;
        NewSystemApplication.directExchangeProducer = topicExchangeProducer;
        NewSystemApplication.gatewayMapper = gatewayMapper;
        NewSystemApplication.gatewayConfigMapper = gateway_configMapper;
        NewSystemApplication.rulesMapper = rulesMapper;
        NewSystemApplication.bleMapper = bleMapper;
        NewSystemApplication.nettyTcpServer=nettyTcpServer;
        NewSystemApplication.wifiMapper = wifiMapper;
        NewSystemApplication.beaconMapper = beaconMapper;
        NewSystemApplication.braceletMapper = braceletMapper;
        NewSystemApplication.recordMapper = recordMapper;
        NewSystemApplication.wordCardaMapper = wordCardaMapper;
        NewSystemApplication.recordSosMapper = recordSosMapper;
        NewSystemApplication.userMapper = userMapper;
        NewSystemApplication.bTagMapper = bTagMapper;
        NewSystemApplication.logsMapper=logsMapper;

        NewSystemApplication.checkSheetMapper=checkSheetMapper;
        NewSystemApplication.checkRecordMapper=checkRecordMapper;
        NewSystemApplication.customerMapper=customerMapper;
        NewSystemApplication.mofflineMapper=mofflineMapper;
        NewSystemApplication.mapMapper=mapMapper;
        NewSystemApplication.fenceMapper=fenceMapper;
        NewSystemApplication.alarmMapper=alarmMapper;
        NewSystemApplication.historyMapper=historyMapper;
    }

    /* @Autowired
     private RedisUtils redisUtil;
     @Autowired
     DeviceModelMapper deviceModelMapper;*/
    public static void main(String[] args) {

        SpringApplication.run(NewSystemApplication.class, args);
        //  println("启动结果："+redisUtil+"====>"+deviceModelMapper);

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
            println("需要激活文件，退出");
            System.exit(0);
            return;
        }else{
            try {
                FileInputStream inputStream = new FileInputStream(file);
                byte[] bytes=new byte[inputStream.available()];
                inputStream.read(bytes);
                String license=new String(bytes).replaceAll(" ","");
               println("验证吗="+license+"原来码="+(MachineCodeUtil.encode(id,"KUNLUN")+"acfc"));
                if(!(MachineCodeUtil.encode(id,"KUNLUN")+"acfc").equals(license)){
                    println("激活码不对");
                    System.exit(0);
                }
            }catch (Exception e){
                println("异常="+e);
                return;
            }
        }
*/
        boolean result = redisUtil.deleteAll();

        if (result) {
            println("成功删除所有的 key");
        } else {
            println("未能删除所有的 key");
        }
        firmwareTaskHashMap=new HashMap<>();
        file_cache=new HashMap<>();
        println("线程=" + Thread.currentThread().getName());
        DeviceModel_sql deviceModel_sql = new DeviceModel_sql();
        deviceModels = deviceModel_sql.getAllModel(redisUtil, deviceModelMapper);
        Gateway_sql gateway_sql = new Gateway_sql();
        gatewayMap = gateway_sql.getAllGateway(redisUtil, gatewayMapper);
        GatewayMap= gateway_sql.getAllGateway( gatewayMapper);
        GatewayConfig_sql project_sql = new GatewayConfig_sql();
        gatewayConfigMap = project_sql.getAllConfig(gatewayConfigMapper);

        Project_Sql projectSql=new Project_Sql();
        usedMap=projectSql.getUsed(projectMapper);
        Rules_sql r = new Rules_sql();
        rulesMap = r.getAllRules(rulesMapper);

        Btag_Sql btag_sql = new Btag_Sql();
        beacon_tagMap = btag_sql.getAllBeacon(bTagMapper);
        Beacon_Sql beacon_sql = new Beacon_Sql();
        beaconsMap = beacon_sql.getAllBeacon(beaconMapper);
        Bracelet_Sql braceletSql=new Bracelet_Sql();
       braceletsMap= braceletSql.getAllBracelet(braceletMapper);
        WordCarda_Sql wordCarda_sql = new WordCarda_Sql();
        wordcard_aMap = wordCarda_sql.getAllWordCarda(wordCardaMapper);
        Customer_sql customer_sql = new Customer_sql();
        customerMap = customer_sql.getAllCustomer(customerMapper);
        CheckSheet_Sql checkSheet_sql=new CheckSheet_Sql();
       // check_sheetMap=checkSheet_sql.getCheckSheet(checkSheetMapper);



        //println("有链接="+value.getR_host());
        //    t_myMqttClientMap.put(key,t_client);
      new Thread(new Runnable() {
          @Override
          public void run() {
              Map_Sql map_sql=new Map_Sql();
              map_sql.getAllMap(mapMapper,redisUtil);
              client = new MyMqttClient("120.77.232.76",1883,"asd","location_engine",0,"","","");
              client.start();
           //   myMqttClientMap=new HashMap<>();
         //     t_myMqttClientMap=new HashMap<>();
          /*= new MyMqttClient("120.77.232.76",1883,"GwData,Gwdata5","SrvData",1,"kunlun_server","","KunLun");
              MyMqttClient t_client=null;
              client.start();
              myMqttClientMap.put("allmqtt",client);*/

              /*for (Map.Entry<String, Check_sheet> entry : check_sheetMap.entrySet()) {
                  String key = entry.getKey();
                  Check_sheet value = entry.getValue();
                  println("循环"+value.toString());
                 if(value!=null&&value.getRelay_type()==1&& !value.getR_host().isEmpty()&&!value.getR_host().equals("None")&&value.getR_port()>0){
                      println("有链接="+value.getR_host());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            TMyMqttClient t_client=null;
                            t_client = new TMyMqttClient(value.getR_host(),value.getR_port(),value.getR_sub(),value.getR_pub(),value.getR_qos(),value.getR_user(),value.getR_password(),value.getProject_key());
                            t_client.start();
                            println("有链接="+value.getR_host());
                            t_myMqttClientMap.put(key,t_client);
                        }
                    }).start();

                  }
                *//*  if(value!=null&& !value.getHost().isEmpty()&&!value.getHost().equals("None")){

                      new Thread(new Runnable() {
                          @Override
                          public void run() {
                              MyMqttClient client =null;
                              client = new MyMqttClient(value.getHost(),value.getPort(),value.getSub(),value.getPub(),value.getQos(),value.getUser(),value.getPassword(),value.getProject_key());
                              client.start();
                              myMqttClientMap.put(key,client);
                          }
                      }).start();

                  }*//*
              }*/
          }
      }).start();

        MyWebSocket webSocket = MyWebSocket.getWebSocket();
        webSocket.start();
        MyWebSocketTag webSockettag = MyWebSocketTag.getWebSocket();
        webSockettag.start();
        MyWebSocket_debug webSocketDebug = MyWebSocket_debug.getWebSocket();
        webSocketDebug.start();
        ChannelFuture start = nettyTcpServer.start();
        start.channel().closeFuture().syncUninterruptibly();

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
    public static void println(String log){
        System.out.println(new Date()+"输出log----="+log);
    }
}
