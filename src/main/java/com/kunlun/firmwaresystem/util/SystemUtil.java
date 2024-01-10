package com.kunlun.firmwaresystem.util;

import com.kunlun.firmwaresystem.device.Device;
import com.kunlun.firmwaresystem.entity.device.Devicep;
import com.kunlun.firmwaresystem.gatewayJson.Constant;
import com.kunlun.firmwaresystem.interface_.iConnectTimeOut;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

import static com.kunlun.firmwaresystem.NewSystemApplication.customerMap;
import static java.lang.Thread.sleep;

public class SystemUtil {


    private RedisUtils redisUtils;
    private HashMap<String, ArrayList<Device>> user_device_All = new HashMap<>();
    private HashMap<String, Device> user_device_one = new HashMap<>();
    private LinkedList<Device> deviceList = new LinkedList<>();
    private static SystemUtil util;

    public static SystemUtil getUtil() {
        if (util == null) {
            util = new SystemUtil();
            return util;
        } else {
            return util;
        }
    }

    private SystemUtil() {
        redisUtils = SpringUtil.getBean(RedisUtils.class);
    }

    public void add_user_device_one(String user_key, Device device) {
        user_device_one.put(user_key + device.getdAddress(), device);
        ArrayList<Device> deviceArrayList = user_device_All.get(user_key);
        if (deviceArrayList == null) {
            deviceArrayList = new ArrayList<>();
        }
        deviceArrayList.add(device);
        user_device_All.put(user_key, deviceArrayList);
        redisUtils.set("user_device_All", user_device_All);
    }

    public Device get_user_device_one(String user_key, String address) {
        return user_device_one.get(user_key + address);
    }

    public void addConnectDevice(Device device, iConnectTimeOut connectTimeOut) {
        redisUtils.set(device.getdAddress(), device);
        System.out.println("12112221" + device.getState());
        //deviceList.add(device);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("委曲求全群群群群群群");
                    System.out.println("*******" + device.getdAddress());
                    sleep(device.getTimeout());
                    System.out.println("*******" + device.getdAddress());
                    Device device1 = (Device) redisUtils.get(device.getdAddress());
                    System.out.println("/////");
                    System.out.println("判断" + device1.getState());
                    String state = (String) redisUtils.get(Constant.ConnectState + device.getdAddress());
                    device.setState(state);
                    //说明最新的缓存状态还是刚开始的下发状态，网关并没有上报最新的状态

                    // System.out.println("超时无动作");
                    connectTimeOut.timeout(device);
                    //  }
                } catch (Exception e) {
                    System.out.println("异常---=" + e.getMessage());
                }
            }
        }).start();
    }

    public static void writeExcel(File file, String columns[],ArrayList<HashMap<String,String>> mapList) {
        //创建Excel文件薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建工作表sheeet
        HSSFSheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        Cell cell;
        for (int i = 0; i < columns.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(columns[i]);
        }
        int i = 1;
        for (Map<String, String> map : mapList) {
            row = sheet.createRow(i);
            for (String column : map.keySet()) {
                int j = 0;
                cell = row.createCell(j);
                cell.setCellValue(map.get(column));
                j++;
            }
            i++;

        }
        try {
            FileOutputStream stream = new FileOutputStream(file);
            workbook.write(stream);
            stream.close();
        }catch (Exception e){
            System.out.println("文件异常="+e.getMessage());
        }
        }


    public static ArrayList<HashMap<String, String>> readExcel(MultipartFile file, String columns[]) {
        //String logFilePath = Environment.getExternalStorageDirectory() + File.separator + "Visitor";
        Sheet sheet = null;
        Row row = null;
        Row rowHeader = null;
        ArrayList<HashMap<String, String>> list = null;
        String cellData = null;
        Workbook wb = null;
        if (file == null) {
            return null;
        }
        System.out.println("地址=" + file.getOriginalFilename());
        InputStream is = null;
        try {
            is = file.getInputStream();
            if (file.getOriginalFilename().contains("xls")) {
                wb = new HSSFWorkbook(is);
            } else if (file.getOriginalFilename().contains("xlsx")) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = null;
            }
            if (wb != null) {
                // 用来存放表中数据
                list = new ArrayList<HashMap<String, String>>();
                // 获取第一个sheet
                sheet = wb.getSheetAt(0);

                // 获取最大行数
                int rownum = sheet.getPhysicalNumberOfRows();
                // 获取第一行
                rowHeader = sheet.getRow(0);
                row = sheet.getRow(0);
                //sheet.createRow(5).createCell(0).setCellValue("今年");


                // 获取最大列数
                int colnum = row.getPhysicalNumberOfCells();
                for (int i = 1; i < rownum; i++) {
                    HashMap<String, String> map = new LinkedHashMap<String, String>();
                    row = sheet.getRow(i);
                    if (row != null) {
                        for (int j = 0; j < colnum; j++) {
                            System.out.println("J=" + j);
                            if (columns[j].equals(getCellFormatValue(rowHeader.getCell(j)))) {
                                cellData = (String) getCellFormatValue(row
                                        .getCell(j));
                                System.out.println("读取=" + cellData + "J=" + j);
                                map.put(columns[j], cellData.replaceAll(" ", ""));
                                /*DecimalFormat df = new DecimalFormat("#");
                                System.out.println(    df.format(cellData));*/
                                // Logs.e("yy","cellData="+cellData);
                                //Logs.e("yy","map="+map);
                            }
                        }
                    } else {
                        break;
                    }
                    list.add(map);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    /**
     * 获取单个单元格数据
     *
     * @param cell
     * @return
     * @author lizixiang ,2018-05-08
     */
    public static Object getCellFormatValue(Cell cell) {
        Object cellValue = null;
        if (cell != null) {
            // 判断cell类型
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC: {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA: {
                    // 判断cell是否为日期格式
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    } else {
                        // 数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING: {
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }



public static  void  createExcelTwo(String path , String[] title, Map<String, Devicep> map_list) {
    HSSFWorkbook workbook =new  HSSFWorkbook();
    HSSFSheet sheet = workbook.createSheet();
    HSSFRow row=null;
    row = sheet.createRow(0);
    HSSFCell cell= null;
    int i=0;
    for(String t:title){
        cell=row.createCell(i);
        cell.setCellValue(t);
        i++;
    }
    i=1;
    Devicep deviceP;
    for(String sn:map_list.keySet()){
        deviceP=map_list.get(sn);
        row=sheet.createRow(i);
        i++;
        cell=row.createCell(0);
        cell.setCellValue(deviceP.getName());
        cell=row.createCell(1);
        cell.setCellValue(deviceP.getSn());
        cell=row.createCell(2);
        if(deviceP.getIsbind()==1){
            cell.setCellValue("已绑定信标");
        }else{
            cell.setCellValue("未绑定信标");
        }
        cell=row.createCell(3);
        cell.setCellValue(deviceP.getBind_mac());
        cell=row.createCell(4);
        cell.setCellValue(customerMap.get(deviceP.getCustomer_key()).getNickname());
        cell=row.createCell(5);
        cell.setCellValue(deviceP.getType_name());
        cell=row.createCell(6);
        if(deviceP.getSos()==1){
            cell.setCellValue("触发报警");
        }else{
            cell.setCellValue("正常");
        }
        cell=row.createCell(7);
        if(deviceP.getOnline()==1){
            cell.setCellValue("在线");
        }else {
            cell.setCellValue("离线");
        }
        cell=row.createCell(8);
        cell.setCellValue(deviceP.getLasttime());
        cell=row.createCell(9);
        cell.setCellValue(deviceP.getPoint_name());
        cell=row.createCell(10);
        cell.setCellValue(deviceP.getRssi());
        cell=row.createCell(11);
        cell.setCellValue(deviceP.getGateway_mac());
        cell=row.createCell(12);
        cell.setCellValue(deviceP.getBt()+"V");
        cell=row.createCell(13);
        cell.setCellValue(deviceP.getCreatetime());
    }
    File file = new File(path);
    try{
        file.createNewFile();
        FileOutputStream stream = new FileOutputStream(file);
        workbook.write(stream);
        stream.close();
    }catch (IOException e){
        System.out.println("盘点记录文件保存异常"+e.getMessage());
    }

}
}
