package com.kunlun.firmwaresystem.util;

import com.kunlun.firmwaresystem.device.Device;
import com.kunlun.firmwaresystem.entity.Beacon;
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
import static com.kunlun.firmwaresystem.NewSystemApplication.println;
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
        println("12112221" + device.getState());
        //deviceList.add(device);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    println("委曲求全群群群群群群");
                    println("*******" + device.getdAddress());
                    sleep(device.getTimeout());
                    println("*******" + device.getdAddress());
                    Device device1 = (Device) redisUtils.get(device.getdAddress());
                    println("/////");
                    println("判断" + device1.getState());
                    String state = (String) redisUtils.get(Constant.ConnectState + device.getdAddress());
                    device.setState(state);
                    connectTimeOut.timeout(device);
                    //  }
                } catch (Exception e) {
                    println("异常---=" + e.getMessage());
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
            int j = 0;
            for (String column : map.keySet()) {
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
            println("文件异常="+e.getMessage());
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
        println("地址=" + file.getOriginalFilename());
        InputStream is = null;
        try {
            println("1111");
            is =file.getInputStream();
            println("12222"+is.available());
            if (file.getOriginalFilename().contains("xlsx")) {
                println("类型在此5");
                try {
                    println("类型在8"+is);
                    wb = new XSSFWorkbook(is);
                    println("类型在此66"+wb);
                }catch (IOException e){
                    println("大大的异常="+e.toString());
                }

            } else if (file.getOriginalFilename().contains("xls")) {

                wb = new HSSFWorkbook(is);

            } else {
                wb = null;
            }
            if (wb != null) {
                println("65656");
                // 用来存放表中数据
                list = new ArrayList<HashMap<String, String>>();
                // 获取第一个sheet
                sheet = wb.getSheetAt(0);

                // 获取最大行数
                int rownum = sheet.getPhysicalNumberOfRows();
                println("输出行数="+rownum);
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
                        for (int j = 0; j < columns.length; j++) {
                            println("J=" + j);
                            if (columns[j].equals(getCellFormatValue(rowHeader.getCell(j)))) {
                                cellData = (String) getCellFormatValue(row
                                        .getCell(j));
                                println("读取=" + cellData + "J=" + j);
                                map.put(columns[j], cellData.replaceAll(" ", ""));
                                /*DecimalFormat df = new DecimalFormat("#");
                                println(    df.format(cellData));*/
                                // Logs.e("yy","cellData="+cellData);
                                //Logs.e("yy","map="+map);
                            }
                        }
                    } else {
                        break;
                    }
                    list.add(map);
                }
            }else{
                println("7878");
            }
        } catch (FileNotFoundException e) {

            println("异常"+e.getMessage());
            return null;
        } catch (IOException e) {
            println("异常22="+e.getMessage());


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



    public static  void  createExcelBeacon(String path , String[] title,    List<Beacon> beaconList) {
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

        for(Beacon beacon :beaconList){
        //  String[] titles = {"MAC", "在线状态", "绑定状态", "资产编码/身份证","资产/人员", "电压", "创建时间", "在线时间"};
            row=sheet.createRow(i);
            i++;
            cell=row.createCell(0);
            cell.setCellValue(beacon.getMac());

            cell=row.createCell(1);
            cell.setCellValue(beacon.getOnline()==1?"OnLine":"OffLine");
            cell=row.createCell(5);
            cell.setCellValue(beacon.getBt());

            cell=row.createCell(6);
            cell.setCellValue(beacon.getCreatetime());

            cell=row.createCell(7);
            cell.setCellValue(beacon.getLastTime());
        }
        File file = new File(path);
        try{
            file.createNewFile();
            FileOutputStream stream = new FileOutputStream(file);
            workbook.write(stream);
            stream.close();
        }catch (IOException e){
            println("盘点记录文件保存异常"+e.getMessage());
        }

    }
}
