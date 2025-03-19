package com.kunlun.firmwaresystem.gatewayJson;

public class Constant {
    static final String engine="engine";
    public static final String pkt_type_scan_report = "scan_report";
    public static final String pkt_type_command = "command";
    public static final String pkt_type_response = "response";
    public static final String pkt_type_state = "state";
    ///command
    public static final String cmd_conn_addr_request = "conn_addr_request";
    public static final String cmd_conn_addr_disconn = "conn_addr_disconn";
    public static final String cmd_sys_get_ver = "sys_get_ver";

    //Response
    //获取网关状态
    public static final String response_network_status="network_status";
    //获取蓝牙版本号
    public static final String response_sys_get_ver = "sys_get_ver";
    //获取wifi版本号
    public static final String response_sys_get_wifi_ver = "sys_get_wifi_ver";
    //连接设备的响应
    public static final String response_conn_addr_request = "conn_addr_request";
    //扫描参数结果
    public static final String response_scan_filter_get = "scan_filter_get";
    //扫描过滤结果
    public static final String response_scan_params_get = "scan_params_get";
    //广播结果
    public static final String response_adv_params_get = "adv_params_get";
    //获取网关配置
    public static final String sys_app_server = "sys_app_server";
    //State
    //设备的连接状态
    public static final String state_sta_device_state = "sta_device_state";
    //心跳包
    public static final String state_sta_gw_hb = "sta_gw_hb";


    //初始下发连接指令
    public static final String state_gotoConnect =engine+ "toConnect";


    //类名   扫描上报
    public static final String Scan_report = "Scan_report";
    //类名，获取网关网络状态\
    public static final String  Network_Status="Network_Status";
    //类名 连接的状态推送
    public static final String ConnectState = "ConnectState";
    //类名  连接的执行状态
    public static final String ConnectExecute = "ConnectExecute";
    //类名 各类返回结果
    public static final String Scan_filter = "Scan_filter";
    public static final String Scan_params = "Scan_params";
    public static final String Adv_params = "Adv_params";
    public static final String WifiVersion = "WifiVersion";
    public static final String BleVersion = "BleVersion";
    public static final String App_Server = "App_Server";

    //保存上一次的位置信息
    public static final String redis_key_location =engine+ "redis_key_location";
    //Redis 保存的一些key,保持统一key,避免写错
    //下发消息时，针对消息ID
    public static final String redis_key_sendToGateway = engine+"sendToGateway_id=";
    //针对扫描上报时，存起来设备对饮网关的最强信号  一个设备最多缓存10个网关信息
    public static final String redis_key_device_gateways =engine+ "device_gateways";
    //针对扫描上报时，存起来设备对应网关的最强信号
    public static final String redis_key_gateway = engine+"gateway";
    //缓存好单个配置项对应全部网关的在线离线问题
    public static final String redis_key_gatewayConfig_onLine = engine+"redis_key_gatewayConfig_onLine";
    //public static final String redis_key_gateway_onLine = engine+"redis_key_gateway_onLine";
    //记录网关离线后首次上线时间
    public static final String redis_key_gateway_onLine_time = engine+"redis_key_gateway_onLine_time";
    //记录网关接收数据的包数量，只计算扫描上报数据包
    public static final String redis_key_gateway_revice_count = engine+"redis_key_gateway_revice_count";
    //判断网关是否同步完成
    public static final String redis_key_project_sys =engine+ "redis_key_project_sys";
    public static final String redis_key_project_heart = engine+"redis_key_project_heart";

    //缓存beacon信息.一个转发类工卡设备缓存的map
    public static final String redis_key_tag_map =engine+ "redis_key_tag_map";

    //根据地图唯一key缓存地图信息
    public static final String redis_key_map = engine+"redis_key_map";
    //根据地图唯一id缓存地图信息,此id由AOA生成
    public static final String redis_id_map = engine+"redis_id_map";
    //记录信标的在线情况
    public static final String redis_key_beacon_onLine =engine+ "redis_key_beacon_onLine";
    //缓存beacon信息.一个信标或者转发类工卡设备缓存的map
    public static final String redis_key_card_map = engine+"redis_key_card_map";
    //AOA基站
    public static final String redis_key_locator=engine+"redis_key_locator";
    //AOA基站项目对应的全部基站状态
    public static final String redis_key_locator_project=engine+"redis_key_locator_project";
    //蓝牙基站Gateway项目对应的全部基站状态
    public static final String redis_key_gateway_project=engine+"redis_key_gateway_project";
    //beacon项目对应的全部基站状态
    public static final String redis_key_beacon_project=engine+"redis_key_beacon_project";
    //人员-项目对应的全部基站状态
    public static final String redis_key_person_project=engine+"redis_key_person_project";
    //资产设备-项目对应的全部基站状态
    public static final String redis_key_device_project=engine+"redis_key_device_project";
    //保存设备的对应SOS状态
    public static final String redis_key_device_sos = engine+"redis_key_device_sos";
    //当前升级的网关mac
    public static final String redis_key_updateing_gateway =engine+ "redis_key_updateing_gateway";
    //缓存三点定位的设备的定位信息
    public static final String redis_key_location_tag =engine+ "redis_key_location_tag";
    //实时连接状态
    public static final String ConnectState_searching = engine+"searching";
    public static final String ConnectState_redy = engine+"redy";
    public static final String ConnectState_sta_conn_params_updated =engine+ "sta_conn_params_updated";
    public static final String fence_check_device=engine+"fence_check_device";
    public static final String fence_check_device_res=engine+"fence_check_device_res";
    public static final String fence_check_person=engine+"fence_check_person";
    public static final String fence_check_person_res=engine+"fence_check_person_res";
    public static final String device_check_online_status_res=engine+"device_check_online_status_res";
    public static final String person_check_online_status_res=engine+"person_check_online_status_res";
    public static final String device_check_sos_status_res=engine+"device_check_sos_status_res";
    public static final String person_check_sos_status_res=engine+"person_check_sos_status_res";
    public static final String device_check_bt_status_res=engine+"device_check_bt_status_res";
    public static final String person_check_bt_status_res=engine+"person_check_bt_status_res";
    public static final String device_check_run_status_res=engine+"device_check_run_status_res";
    public static final String person_check_run_status_res=engine+"person_check_run_status_res";
    public static final String fwordcard="fwordcard";
}
