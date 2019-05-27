package com.fantasy.app.core.util.excelutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 公众号：18岁fantasy
 * @Description: excel导出工具类
 * @date 2017年8月21日 下午2:53:04
 */
public class ExcelUtils {


    /**
     * 对外提供读取excel 的方法  File
     */
    public static List<List<List<Object>>> readExcel(File file) throws IOException {

        if ("xls".equals(getExcelExtension(file))) {
            return read2003Excel(file);
        } else if ("xlsx".equals(getExcelExtension(file))) {
            return read2007Excel(file);
        } else {
            throw new IOException("不支持的文件类型");
        }
    }


    /**
     * 对外提供读取excel 的方法    MultipartFile
     */
    public static List<List<List<Object>>> readExcel(MultipartFile file) throws IOException {

        if (file.getOriginalFilename().toLowerCase().endsWith("xls")) {
            return read2003Excel(file);
        } else if (file.getOriginalFilename().toLowerCase().endsWith("xlsx")) {
            return read2007Excel(file);
        } else {
            throw new IOException("不支持的文件类型");
        }
    }


    /**
     * 对外提供读取excel 的方法    MultipartFile
     *
     * @return LinkedHashMap<String,List<List<Object>>>    Map<工作表名称，工作表内容（《行集合《列集合）>
     */
    public static Map<String, List<List<Object>>> readExcelGetMap(MultipartFile file) throws IOException {

        if (file.getOriginalFilename().toLowerCase().endsWith("xls")) {
            return getMapTo2003Excel(file);
        } else if (file.getOriginalFilename().toLowerCase().endsWith("xlsx")) {
            return getMapTo2007Excel(file);
        } else {
            throw new IOException("不支持的文件类型");
        }
    }

    /**
     * 获取文件后缀名(扩展名)
     */
    public static String getExcelExtension(File file) {
        String fileName = file.getName();
        String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
                .substring(fileName.lastIndexOf(".") + 1);
        return extension;
    }


    /**
     * 获取sheet列表  List<String>
     */
    public List<String> getSheetListString(String filePath) {
        List<String> sheetList = new ArrayList<String>();
        try {
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(filePath));
            int i = 0;
            while (true) {
                try {
                    String name = wb.getSheetName(i);
                    sheetList.add(name);
                    i++;
                } catch (Exception e) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sheetList;
    }

    /**
     * 获取sheet列表数 int
     */
    public static int getSheetListInt(File file) {
        int i = 0;
        try {

            if ("xls".equals(getExcelExtension(file))) {
                HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
                while (true) {
                    try {
                        String name = wb.getSheetName(i);
                        i++;
                    } catch (Exception e) {
                        i--;
                        break;
                    }
                }
            } else if ("xlsx".equals(getExcelExtension(file))) {
                XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
                while (true) {
                    try {
                        String name = wb.getSheetName(i);
                        i++;
                    } catch (Exception e) {
                        break;
                    }
                }
            } else {
                throw new IOException("不支持的文件类型");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }


    /**
     * 在磁盘生成一个还有内容的excel,路径为path的属性。
     *
     * @param sheetName  导出的sheet名称
     * @param fieldNames 列名数组
     * @param datas      数据组
     * @throws IOException
     */
    public void makeExcel(String sheetName, String[] fieldNames,
                          List<String[]> datas, String path) throws IOException {
        // 在内存中生成工作薄
        HSSFWorkbook workbook = makeWorkBook(sheetName, fieldNames, datas);

        // 截取文件件路径
        String filePath = path.substring(0, path.lastIndexOf("\\"));

        // 如果路径不存在,创建路径
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fileOut = new FileOutputStream(path);
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

    /**
     * 在输出流中导出excel
     *
     * @param excelName 导出的excel名称 包括扩展名
     * @param sheetName 导出的sheet名称
     * @param fieldName 列名数组
     * @param data      数据组
     * @param response  response
     */
    public void makeStreamExcel(String excelName, String sheetName,
                                String[] fieldName, List<String[]> data,
                                HttpServletResponse response) {
        OutputStream os = null;
        try {
            response.reset(); // 清空输出流,此方法受限制，response的任何打开的流关闭之后不在reset
            os = response.getOutputStream(); // 取得输出流
            response.setHeader("Content-disposition", "attachment; filename="
                    + new String(excelName.getBytes(), "ISO-8859-1")); // 设定输出文件头
            response.setContentType("application/msexcel"); // 定义输出类型
        } catch (IOException ex) {// 捕捉异常
            System.out.println("流操作错误:" + ex.getMessage());
        }
        // 在内存中生成工作薄
        HSSFWorkbook workbook = makeWorkBook(sheetName, fieldName, data);
        try {
            os.flush();
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Output is closed");
        }
    }

    /**
     * 根据条件生成工作薄对象到内存。
     *
     * @param sheetName  工作表对象名称
     * @param fieldNames 首行列名称
     * @param datas      数据
     * @return HSSFWorkbook
     */
    private HSSFWorkbook makeWorkBook(String sheetName, String[] fieldNames,
                                      List<String[]> datas) {
        // 产生工作薄对象
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 产生工作表对象
        HSSFSheet sheet = workbook.createSheet();

        // 设置每列宽度
        sheet.setColumnWidth(0, 3500);
        sheet.setColumnWidth(1, 5000);
        // 设置表单名称
        workbook.setSheetName(0, sheetName);
        // 产生一行
        HSSFRow row = sheet.createRow(0);
        // 产生单元格
        HSSFCell cell = null;
        // 写入各字段的名称
        for (int i = 0; i < fieldNames.length; i++) {
            cell = row.createCell((short) i);
            // 设置单元格的内容为字符串
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);

            // 为了能够在单元格内输入中文，设置字符集为UTF-16
            // cell.setEndcoding(HSSFCell.ENCODING_UTF_16);
            cell.setCellValue(fieldNames[i]);
        }

        // 写入各记录，每条记录对应excel表中一行记录
        for (int i = 0; i < datas.size(); i++) {
            String[] tmp = datas.get(i);
            // 生成一行
            row = sheet.createRow(i + 1);
            for (int j = 0; j < tmp.length; j++) {
                cell = row.createCell((short) j);
                // 设置单元格的字符类型为String
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(new HSSFRichTextString((tmp[j] == null) ? ""
                        : tmp[j]));
                // 创建一个超链接
                HSSFHyperlink link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
                link.setAddress("http://zhidao.baidu.com");
                // 给单元格添加样式
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                HSSFFont font = workbook.createFont();
                font.setColor(HSSFFont.COLOR_RED);
                cellStyle.setFont(font);

                if (j == 1) {
                    cell.setHyperlink(link);
                    cell.setCellStyle(cellStyle);
                }
            }
        }
        return workbook;
    }


    /**
     * 导出数据到  excel（2003及以下xls版本）
     *
     * @param fileName   文件名   （不带后缀）
     * @param titles     List<String>   标题数组    每个sheet  一个
     * @param sheetNames List<String>   sheet表单名称    数组
     * @param fieldNames List<List<List<Object>>>  标题列数组    每个sheet一个List   每行列 一个数组，多行多个
     * @param dataLists  List<List<List<Object>>>   数据组  每个sheet一个list  每行一个List<Object>
     *                   每列一个Object   可以直接为值，
     *                   如果需要合并单元格或自定义样式，则为ExcelColumn对象
     * @param path       导出路径   如果该值不为null，则导出到指定路径，如果为null，则导出到输出流（浏览器下载）
     * @throws Exception
     * @author 王明盛
     */
    public static void export2003XLS(String fileName, List<String> titles, List<String> sheetNames,
                                     List<List<List<Object>>> fieldNames, List<List<List<Object>>> dataLists, String path, HttpServletResponse response) throws Exception {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();                     // 创建工作簿对象  
            for (int sheetI = 0; sheetI < sheetNames.size(); sheetI++) {
                String sheetName = sheetNames.get(sheetI);
                List<List<Object>> fieldNameList = fieldNames.get(sheetI);
                List<List<Object>> dataList = dataLists.get(sheetI);
                if (fieldNameList == null || fieldNameList.size() == 0) {
                    continue;
                }

                HSSFSheet sheet = workbook.createSheet(sheetName);                  // 创建工作表

                int rowStar = 0;//下次创建行的起始索引
                int cellStar = 0;//下次创建单元格列的起始索引

                //sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】  
                HSSFCellStyle columnTopStyle = getColumnTopStyleTo2003(workbook);//获取列头样式对象  
                HSSFCellStyle style = getStyleTo2003(workbook);                  //单元格样式对象  

                //定义表格标题需要合并多少单元格 
                int titleMergedNum = 0;
                for (List<Object> ss : fieldNameList) {
                    int sum = 0;
                    for (Object s : ss) {
                        if (s instanceof ExcelColumn) {
                            sum += ((ExcelColumn) s).getMergedRight();
                        } else {
                            sum++;
                        }
                    }
                    if (titleMergedNum < sum) {
                        titleMergedNum = sum;
                    }
                }

                if (titles != null && titles.size() > 0) {
                    // 产生表格标题行
                    HSSFRow rowm = sheet.createRow(rowStar++);
                    HSSFCell cellTiltle = rowm.createCell(cellStar);
                    sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, titleMergedNum - 1));
                    cellTiltle.setCellStyle(columnTopStyle);
                    cellTiltle.setCellValue(titles.get(sheetI));
                    rowStar = 2;
                }

                for (List<Object> fieldName : fieldNameList) {
                    // 定义所需列数
                    int columnNum = fieldName.size();
                    HSSFRow rowRowName = sheet.createRow(rowStar);                // 在索引rowStar的位置创建行(最顶端的行开始的第rowStar行)  
                    rowRowName.setHeight((short) 800);
                    // 将列头设置到sheet的单元格中  
                    for (int n = 0; n < columnNum; n++) {  //创建列头对应个数的单元格
                        HSSFCell cellRowName = rowRowName.createCell(cellStar);  //从该行的cellStar处开始创建列
                        cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING);             //设置列头单元格的数据类型  
                        Object field = fieldName.get(n);
                        if (field instanceof ExcelColumn) {
                            ExcelColumn column = (ExcelColumn) field;

                            sheet.addMergedRegion(new CellRangeAddress(rowStar, rowStar = rowStar + column.getMergedDown(), cellStar, cellStar = cellStar + column.getMergedRight()));
                            HSSFRichTextString text = new HSSFRichTextString(((ExcelColumn) field).getValue() + "");
                            cellRowName.setCellValue(text);                                 //设置列头单元格的值  
                            cellRowName.setCellStyle(columnTopStyle);   //设置列头单元格样式     

                        } else {
                            HSSFRichTextString text = new HSSFRichTextString(field + "");
                            cellRowName.setCellValue(text);                                 //设置列头单元格的值  
                            cellRowName.setCellStyle(columnTopStyle);   //设置列头单元格样式     
                        }
                        cellStar++;
                    }
                    rowStar++;
                    cellStar = 0;  //重置创建列的开始索引
                }


                if (dataList == null || dataList.size() == 0) {
                    continue;
                }
                //将查询出的数据设置到sheet对应的单元格中  
                for (int i = 0; i < dataList.size(); i++) {

                    List<Object> obj = dataList.get(i);//遍历每个对象  
                    HSSFRow row = sheet.createRow(rowStar++);//创建所需的行数  
                    row.setHeight((short) 800);
                    for (int j = 0; j < obj.size(); j++) {
                        HSSFCell cell = null;   //设置单元格的数据类型
                        if (j == 0) {
                            cell = row.createCell(j, HSSFCell.CELL_TYPE_NUMERIC);
                            cell.setCellValue(i + 1);
                        } else {
                            cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                            if (!"".equals(obj.get(j)) && obj.get(j) != null) {
                                cell.setCellValue(obj.get(j).toString());                       //设置单元格的值  
                            }
                        }
                        cell.setCellStyle(style);                                   //设置单元格样式  
                    }
                }
                //让列宽随着导出的列长自动适应  
                for (int colNum = 0; colNum < titleMergedNum; colNum++) {
                    int columnWidth = sheet.getColumnWidth(colNum) / 256;
                    for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                        HSSFRow currentRow;
                        //当前行未被使用过  
                        if (sheet.getRow(rowNum) == null) {
                            currentRow = sheet.createRow(rowNum);
                        } else {
                            currentRow = sheet.getRow(rowNum);
                        }
                        if (currentRow.getCell(colNum) != null) {
                            HSSFCell currentCell = currentRow.getCell(colNum);
                            if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                                int length = currentCell.getStringCellValue().getBytes().length;
                                if (columnWidth < length) {
                                    columnWidth = length;
                                }
                            }
                        }
                    }
                    if (colNum == 0) {
                        sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
                    } else {
                        sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
                    }
                }
            }


            if (workbook != null) {
                try {

                    if (path != null && !"".equals(path)) {
                        File file = new File(path);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        FileOutputStream fileOut = new FileOutputStream(path + "\\" + fileName + ".xls");
                        workbook.write(fileOut);
                        fileOut.flush();
                        fileOut.close();
                    } else {
                        String headStr = fileName + ".xls";
                        response.reset();
                        response.setContentType("APPLICATION/msexcel");
                        response.setHeader("Content-disposition", "attachment; filename="
                                + new String(headStr.getBytes(), "ISO-8859-1")); // 设定输出文件头
                        OutputStream os = response.getOutputStream();
                        workbook.write(os);
                        os.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /*
     * 列头单元格样式 
     */
    public static HSSFCellStyle getColumnTopStyleTo2003(HSSFWorkbook workbook) {

        // 设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short) 11);
        //字体加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        //设置左边框;
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        //设置右边框;
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        //设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        //设置顶边框;
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return style;

    }

    /*   
     * 列数据信息单元格样式 
     */
    public static HSSFCellStyle getStyleTo2003(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        //font.setFontHeightInPoints((short)10);
        //字体加粗
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        //设置左边框;
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        //设置右边框;
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        //设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        //设置顶边框;
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return style;

    }


    /**
     * 导出数据到  excel（2007及以上xlsx版本）
     *
     * @param fileName   文件名   （不带后缀）
     * @param titles     List<String>   标题数组    每个sheet  一个
     * @param sheetNames List<String>   sheet表单名称    数组
     * @param fieldNames List<List<List<Object>>>  标题列数组    每个sheet一个List   每行列 一个List<Object>，多行多个
     * @param dataLists  List<List<List<Object>>>   数据组  每个sheet一个list  每行一个List<Object>
     *                   每列一个Object   可以直接为值，
     *                   如果需要合并单元格或自定义样式，则为ExcelColumn对象
     * @param path       导出路径   如果该值不为null，则导出到指定路径，如果为null，则导出到输出流（浏览器下载）
     * @throws Exception
     * @author 王明盛
     */
    public static Object export2007XLSX(String fileName, List<String> titles, List<String> sheetNames,
                                        List<List<List<Object>>> fieldNames, List<List<List<Object>>> dataLists, String path, HttpServletResponse response) throws Exception {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();                     // 创建工作簿对象
            for (int sheetI = 0; sheetI < sheetNames.size(); sheetI++) {
                String sheetName = sheetNames.get(sheetI);
                List<List<Object>> fieldNameList = fieldNames.get(sheetI);
                List<List<Object>> dataList = dataLists.get(sheetI);
                if (fieldNameList == null || fieldNameList.size() == 0) {
                    continue;
                }

                XSSFSheet sheet = workbook.createSheet(sheetName);                  // 创建工作表

                int rowStar = 0;//下次创建行的起始索引
                int cellStar = 0;//下次创建单元格列的起始索引

                //sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】
                XSSFCellStyle columnTopStyle = getColumnTopStyleTo2007(workbook);//获取列头样式对象
                XSSFCellStyle style = getStyleTo2007(workbook);                  //单元格样式对象

                //定义表格标题需要合并多少单元格
                int titleMergedNum = 0;
                for (List<Object> ss : fieldNameList) {
                    int sum = 0;
                    for (Object s : ss) {
                        if (s instanceof ExcelColumn) {
                            sum += ((ExcelColumn) s).getMergedRight();
                        } else {
                            sum++;
                        }
                    }
                    if (titleMergedNum < sum) {
                        titleMergedNum = sum;
                    }
                }

                if (titles != null && titles.size() > 0) {
                    // 产生表格标题行
                    XSSFRow rowm = sheet.createRow(rowStar++);
                    XSSFCell cellTiltle = rowm.createCell(cellStar);
                    sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, titleMergedNum - 1));
                    cellTiltle.setCellStyle(columnTopStyle);
                    cellTiltle.setCellValue(titles.get(sheetI));
                    rowStar = 2;
                }

                for (List<Object> fieldName : fieldNameList) {
                    // 定义所需列数
                    int columnNum = fieldName.size();
                    XSSFRow rowRowName = sheet.createRow(rowStar);                // 在索引rowStar的位置创建行(最顶端的行开始的第rowStar行)
                    rowRowName.setHeight((short) 800);
                    // 将列头设置到sheet的单元格中
                    for (int n = 0; n < columnNum; n++) {  //创建列头对应个数的单元格
                        XSSFCell cellRowName = rowRowName.createCell(cellStar);  //从该行的cellStar处开始创建列
                        cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING);             //设置列头单元格的数据类型
                        Object field = fieldName.get(n);
                        if (field instanceof ExcelColumn) {
                            ExcelColumn column = (ExcelColumn) field;

                            sheet.addMergedRegion(new CellRangeAddress(rowStar, rowStar = rowStar + column.getMergedDown(), cellStar, cellStar = cellStar + column.getMergedRight()));
                            XSSFRichTextString text = new XSSFRichTextString(((ExcelColumn) field).getValue() + "");
                            cellRowName.setCellValue(text);                                 //设置列头单元格的值
                            cellRowName.setCellStyle(columnTopStyle);   //设置列头单元格样式

                        } else {
                            XSSFRichTextString text = new XSSFRichTextString(field + "");
                            cellRowName.setCellValue(text);                                 //设置列头单元格的值
                            cellRowName.setCellStyle(columnTopStyle);   //设置列头单元格样式
                        }
                        cellStar++;
                    }
                    rowStar++;
                    cellStar = 0;  //重置创建列的开始索引
                }


                if (dataList == null || dataList.size() == 0) {
                    continue;
                }
                //将查询出的数据设置到sheet对应的单元格中
                for (int i = 0; i < dataList.size(); i++) {

                    List<Object> obj = dataList.get(i);//遍历每个对象
                    XSSFRow row = sheet.createRow(rowStar++);//创建所需的行数
                    row.setHeight((short) 700);
                    for (int j = 0; j < obj.size(); j++) {
                        XSSFCell cell = null;   //设置单元格的数据类型
                       /*if(j == 0){  
                           cell = row.createCell(j,XSSFCell.CELL_TYPE_NUMERIC);  
                           cell.setCellValue(obj.get(j).toString());   
                       }else{  */
                        Object o = obj.get(j);
                        if (o == null) {
                            cell = row.createCell(j, XSSFCell.CELL_TYPE_NUMERIC);
                            if (!"".equals(o) && o != null) {
                                cell.setCellValue(((Number) 0).doubleValue());                       //设置单元格的值
                            }
                        } else {
                            if (o instanceof String) {
                                cell = row.createCell(j, XSSFCell.CELL_TYPE_STRING);
                                if (!"".equals(o) && o != null) {
                                    cell.setCellValue(o.toString());                       //设置单元格的值
                                }
                            } else if (o instanceof Number) {
                                cell = row.createCell(j, XSSFCell.CELL_TYPE_NUMERIC);
                                if (!"".equals(o) && o != null) {
                                    cell.setCellValue(((Number) o).doubleValue());                       //设置单元格的值
                                }
                            }
                        }

                        //}
                        cell.setCellStyle(style);                                   //设置单元格样式
                    }
                }
                //让列宽随着导出的列长自动适应
                for (int colNum = 0; colNum < titleMergedNum; colNum++) {
                    int columnWidth = sheet.getColumnWidth(colNum) / 256;
                    for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                        XSSFRow currentRow;
                        //当前行未被使用过
                        if (sheet.getRow(rowNum) == null) {
                            currentRow = sheet.createRow(rowNum);
                        } else {
                            currentRow = sheet.getRow(rowNum);
                        }
                        if (currentRow.getCell(colNum) != null) {
                            XSSFCell currentCell = currentRow.getCell(colNum);
                            if (currentCell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                                int length = currentCell.getStringCellValue().getBytes().length;
                                if (columnWidth < length) {
                                    columnWidth = length;
                                }
                            }
                        }
                    }
                    if (colNum == 0) {
                        sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
                    } else {
                        sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
                    }
                }
            }


            if (workbook != null) {
                try {

                    if (path != null && !"".equals(path)) {
                        File file = new File(path);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        FileOutputStream fileOut = new FileOutputStream(path + "\\" + fileName + ".xlsx");
                        workbook.write(fileOut);
                        fileOut.flush();
                        fileOut.close();
                    } else {
                        String headStr = fileName + ".xlsx";
                        response.reset();
                        response.setContentType("APPLICATION/msexcel");
                        response.setHeader("Content-disposition", "attachment; filename="
                                + new String(headStr.getBytes(), "ISO-8859-1")); // 设定输出文件头
                        OutputStream os = response.getOutputStream();
                        workbook.write(os);
                        os.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return null;

    }


    /*
     * 列头单元格样式 
     */
    public static XSSFCellStyle getColumnTopStyleTo2007(XSSFWorkbook workbook) {

        // 设置字体
        XSSFFont font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short) 11);
        //字体加粗
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        //设置底边框;
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        //设置左边框;
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        //设置右边框;
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        //设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        //设置顶边框;
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        return style;

    }

    /*   
     * 列数据信息单元格样式 
     */
    public static XSSFCellStyle getStyleTo2007(XSSFWorkbook workbook) {
        // 设置字体
        XSSFFont font = workbook.createFont();
        //设置字体大小
        //font.setFontHeightInPoints((short)10);
        //字体加粗
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        XSSFCellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        //设置左边框;
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        //设置右边框;
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        //设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        //设置顶边框;
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        return style;

    }


    /**
     * 修改表单中指定的单元格的内容
     *
     * @param sheetOrder
     * @param column
     * @param row
     * @param content
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void write(File file, int sheetOrder, int column, int row, String content)
            throws FileNotFoundException, IOException {
        Workbook workbook = new HSSFWorkbook(new POIFSFileSystem(
                new FileInputStream(file)));
        Sheet sheet = workbook.getSheetAt(sheetOrder);
        Row rows = sheet.createRow(row);
        Cell cell = rows.createCell(column);
        cell.setCellValue(content);
        OutputStream fileOut = new FileOutputStream(file);
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

    /**
     * 获取一个工作表最后一条记录的序号
     *
     * @param sheetOrder 工作表序号
     * @return int
     * @throws IOException
     */
    public int getSheetLastRowNum(File file, int sheetOrder) throws IOException {
        Workbook workbook = new HSSFWorkbook(new POIFSFileSystem(
                new FileInputStream(file)));
        Sheet sheet = workbook.getSheetAt(sheetOrder);
        return sheet.getLastRowNum();

    }

    /**
     * 根据path属性在磁盘生成一个新的Excel
     *
     * @throws IOException
     */
    public void makeEmptyExcel(String path) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        // 创建一个名称为new sheet的表单
        workbook.createSheet("new sheet");
        // 截取文件夹路径
        String subPath = path.substring(0, path.lastIndexOf("\\"));
        // 如果文件不存在创建文件
        File file = new File(subPath);
        if (file.exists()) {
            file.mkdirs();
        }
        OutputStream fileOut = new FileOutputStream(subPath + "\\"
                + path.substring(path.lastIndexOf("\\")));
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

    /**
     * 根据path属性，给已经存在的excel工作薄中添加工作表单。
     *
     * @param sheetName 表单名称
     * @throws IOException
     */
    public void makeEmptySheetInExistExcel(String path, String sheetName) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(
                path)));
        // 在已经存在的工作薄中新建一个空工作表单
        Sheet sheet = workbook.createSheet(sheetName);
        workbook.setSheetOrder(sheet.getSheetName(),
                workbook.getNumberOfSheets() - 1);
        // 截取文件夹路径
        String subPath = path.substring(0, path.lastIndexOf("\\"));
        // 如果文件不存在创建文件
        File file = new File(subPath);
        if (file.exists()) {
            file.mkdirs();
        }
        OutputStream fileOut = new FileOutputStream(subPath + "\\"
                + path.substring(path.lastIndexOf("\\")));
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

    /**
     * 根据工作区的序号，读取该工作区下的所有记录，每一条记录是一个List<Object><br/>
     * 注意如果单元格中的数据时数字将会被自动转换成字符串<br/>
     * 如果单元格中存在除数字、字符串以外的其他类型数据，将会产生错误。
     *
     * @param sheetOrder 　工作区序号
     * @return List<String[]>
     * @throws IOException
     */
    public static List<List<Object>> getDataFromSheetIndex(File file, int sheetOrder) throws IOException {
        Workbook workbook = new HSSFWorkbook(new POIFSFileSystem(
                new FileInputStream(file)));
        Sheet sheet = workbook.getSheetAt(sheetOrder);
        List<List<Object>> strArrays = new ArrayList<List<Object>>();
        // 得到的行数是基于0的索引，遍历所有的行
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            List<Object> strs = new ArrayList<>();
            ;
            for (int k = 0; k < row.getLastCellNum(); k++) {
                Cell cell = row.getCell(k);
                if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                    // 用于格式化数字，只保留数字的整数部分
                    DecimalFormat format = new DecimalFormat("########");
                    strs.add(format.format(cell.getNumericCellValue()));
                } else {
                    strs.add(cell.getStringCellValue());
                }
            }
            strArrays.add(strs);
        }
        return strArrays;
    }


    /**
     * 读取 office 2003 excel 读取File
     *
     * @return List<List<List<Object>>>    工作表集合《行集合《列集合
     * @throws IOException
     * @throws FileNotFoundException
     */
    private static List<List<List<Object>>> read2003Excel(File file)
            throws IOException {
        List<List<List<Object>>> dataList = new ArrayList<List<List<Object>>>();
        HSSFWorkbook hwb = new HSSFWorkbook(new FileInputStream(file));
        int sheetNum = getSheetListInt(file);
        for (int sheetI = 0; sheetI < sheetNum; sheetI++) {

            List<List<Object>> list = new ArrayList<List<Object>>();
            HSSFSheet sheet = hwb.getSheetAt(sheetI);
            Object value = null;
            HSSFRow row = null;
            HSSFCell cell = null;
            int counter = 0;


            //空工作表跳过，不需要跳过可以添加空集合
            if (sheet.getFirstRowNum() < 0)
                continue;


            for (int i = sheet.getFirstRowNum(); counter < sheet.getPhysicalNumberOfRows(); i++) {
                row = sheet.getRow(i);
                if (row == null) {  //空行跳过，不需要跳过可以添加空集合
                    continue;
                } else if (row.getFirstCellNum() < 0) { //伪空行跳过，不需要跳过可以添加空集合
                    counter++;
                    continue;
                } else {
                    counter++;
                }
                List<Object> linked = new ArrayList<Object>();
                for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    if (cell == null) {
                        linked.add(null);
                        continue;
                    }
                    DecimalFormat df = new DecimalFormat("0");// 格式化 number String 字符  
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串  
                    DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字  
                    switch (cell.getCellType()) {
                        case XSSFCell.CELL_TYPE_STRING:
                            System.out.println(i + "行" + j + " 列 is String type");
                            value = cell.getStringCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_NUMERIC:
                            System.out.println(i + "行" + j
                                    + " 列 is Number type ; DateFormt:"
                                    + cell.getCellStyle().getDataFormatString());
                            if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                                value = df.format(cell.getNumericCellValue());
                            } else if ("General".equals(cell.getCellStyle()
                                    .getDataFormatString())) {
                                value = nf.format(cell.getNumericCellValue());
                            } else {
                                value = sdf.format(HSSFDateUtil.getJavaDate(cell
                                        .getNumericCellValue()));
                            }
                            break;
                        case XSSFCell.CELL_TYPE_BOOLEAN:
                            System.out.println(i + "行" + j + " 列 is Boolean type");
                            value = cell.getBooleanCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_BLANK:
                            System.out.println(i + "行" + j + " 列 is Blank type");
                            value = "";
                            break;
                        default:
                            System.out.println(i + "行" + j + " 列 is default type");
                            value = cell.toString();
                    }
                    if (value == null || "".equals(value)) {
                        linked.add(null);
                        continue;
                    }
                    linked.add(value);
                }
                list.add(linked);
            }
            dataList.add(list);
        }


        return dataList;
    }

    /**
     * 读取Office 2007 excel 读取File
     */
    private static List<List<List<Object>>> read2007Excel(File file)
            throws IOException {
        List<List<List<Object>>> dataList = new ArrayList<List<List<Object>>>();
        // 构造 XSSFWorkbook 对象，strPath 传入文件路径  
        XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));

        int sheetNum = getSheetListInt(file);

        for (int sheetI = 0; sheetI < sheetNum; sheetI++) {
            List<List<Object>> list = new ArrayList<List<Object>>();
            // 读取第sheetI张工作表内容
            XSSFSheet sheet = xwb.getSheetAt(sheetI);
            Object value = null;
            XSSFRow row = null;
            XSSFCell cell = null;
            int counter = 0;


            //空工作表跳过，不需要跳过可以添加空集合
            if (sheet.getFirstRowNum() < 0)
                continue;


            for (int i = sheet.getFirstRowNum(); counter < sheet.getPhysicalNumberOfRows(); i++) {
                row = sheet.getRow(i);
                if (row == null) {  //空行跳过，不需要跳过可以添加空集合
                    continue;
                } else if (row.getFirstCellNum() < 0) { //伪空行跳过，不需要跳过可以添加空集合
                    counter++;
                    continue;
                } else {
                    counter++;
                }
                List<Object> linked = new ArrayList<Object>();
                for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    if (cell == null) {
                        linked.add(null);
                        continue;
                    }
                    DecimalFormat df = new DecimalFormat("0");// 格式化 number String 字符
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
                    DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
                    switch (cell.getCellType()) {
                        case XSSFCell.CELL_TYPE_STRING:
                            value = cell.getStringCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_NUMERIC:
                            if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                                value = df.format(cell.getNumericCellValue());
                            } else if ("General".equals(cell.getCellStyle()
                                    .getDataFormatString())) {
                                value = nf.format(cell.getNumericCellValue());
                            } else {
                                value = sdf.format(HSSFDateUtil.getJavaDate(cell
                                        .getNumericCellValue()));
                            }
                            break;
                        case XSSFCell.CELL_TYPE_BOOLEAN:
                            value = cell.getBooleanCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_BLANK:
                            value = "";
                            break;
                        default:
                            value = cell.toString();
                    }
                    if (value == null || "".equals(value)) {
                        linked.add(null);
                        continue;
                    }
                    linked.add(value);
                }
                list.add(linked);
            }
            dataList.add(list);
        }
        return dataList;
    }


    /**
     * 读取 office 2003 excel   读取 MultipartFile
     *
     * @return List<List<List<Object>>>    工作表集合《行集合《列集合
     * @throws IOException
     * @throws FileNotFoundException
     */
    private static List<List<List<Object>>> read2003Excel(MultipartFile file)
            throws IOException {
        List<List<List<Object>>> dataList = new ArrayList<List<List<Object>>>();

        InputStream inputStream = file.getInputStream();
        POIFSFileSystem fs = new POIFSFileSystem(inputStream);
        HSSFWorkbook hwb = new HSSFWorkbook(fs);

        int sheetNum = 0;
        while (true) {
            try {
                String name = hwb.getSheetName(sheetNum);
                sheetNum++;
            } catch (Exception e) {
                break;
            }
        }

        for (int sheetI = 0; sheetI < sheetNum; sheetI++) {
            List<List<Object>> list = new ArrayList<List<Object>>();
            HSSFSheet sheet = hwb.getSheetAt(sheetI);
            Object value = null;
            HSSFRow row = null;
            HSSFCell cell = null;
            int counter = 0;


            //空工作表跳过，不需要跳过可以添加空集合
            if (sheet.getFirstRowNum() < 0)
                continue;


            for (int i = sheet.getFirstRowNum(); counter < sheet.getPhysicalNumberOfRows(); i++) {
                row = sheet.getRow(i);
                if (row == null) {  //空行跳过，不需要跳过可以添加空集合
                    continue;
                } else if (row.getFirstCellNum() < 0) { //伪空行跳过，不需要跳过可以添加空集合
                    counter++;
                    continue;
                } else {
                    counter++;
                }
                List<Object> linked = new ArrayList<Object>();
                for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    if (cell == null) {
                        linked.add(null);
                        continue;
                    }
                    DecimalFormat df = new DecimalFormat("0");// 格式化 number String 字符  
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串  
                    DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字  
                    switch (cell.getCellType()) {
                        case XSSFCell.CELL_TYPE_STRING:
                            System.out.println(i + "行" + j + " 列 is String type");
                            value = cell.getStringCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_NUMERIC:
                            System.out.println(i + "行" + j
                                    + " 列 is Number type ; DateFormt:"
                                    + cell.getCellStyle().getDataFormatString());
                            if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                                value = df.format(cell.getNumericCellValue());
                            } else if ("General".equals(cell.getCellStyle()
                                    .getDataFormatString())) {
                                value = nf.format(cell.getNumericCellValue());
                            } else {
                                value = sdf.format(HSSFDateUtil.getJavaDate(cell
                                        .getNumericCellValue()));
                            }
                            break;
                        case XSSFCell.CELL_TYPE_BOOLEAN:
                            System.out.println(i + "行" + j + " 列 is Boolean type");
                            value = cell.getBooleanCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_BLANK:
                            System.out.println(i + "行" + j + " 列 is Blank type");
                            value = "";
                            break;
                        default:
                            System.out.println(i + "行" + j + " 列 is default type");
                            value = cell.toString();
                    }
                    if (value == null || "".equals(value)) {
                        linked.add(null);
                        continue;
                    }
                    linked.add(value);
                }
                list.add(linked);
            }
            dataList.add(list);
        }


        return dataList;
    }

    /**
     * 读取Office 2007 excel   读取 MultipartFile
     *
     * @return List<List<List<Object>>>    工作表集合《行集合《列集合
     */
    private static List<List<List<Object>>> read2007Excel(MultipartFile file)
            throws IOException {
        List<List<List<Object>>> dataList = new ArrayList<List<List<Object>>>();


        // 构造 XSSFWorkbook 对象，strPath 传入文件路径  
        XSSFWorkbook xwb = new XSSFWorkbook(file.getInputStream());

        int sheetNum = 0;
        while (true) {
            try {
                String name = xwb.getSheetName(sheetNum);
                sheetNum++;
            } catch (Exception e) {
                break;
            }
        }

        for (int sheetI = 0; sheetI < sheetNum; sheetI++) {
            List<List<Object>> list = new ArrayList<List<Object>>();
            // 读取第sheetI张工作表内容
            XSSFSheet sheet = xwb.getSheetAt(sheetI);
            Object value = null;
            XSSFRow row = null;
            XSSFCell cell = null;
            int counter = 0;


            //空工作表跳过，不需要跳过可以添加空集合
            if (sheet.getFirstRowNum() < 0)
                continue;


            for (int i = sheet.getFirstRowNum(); counter < sheet.getPhysicalNumberOfRows(); i++) {
                row = sheet.getRow(i);
                if (row == null) {  //空行跳过，不需要跳过可以添加空集合
                    continue;
                } else if (row.getFirstCellNum() < 0) { //伪空行跳过，不需要跳过可以添加空集合
                    counter++;
                    continue;
                } else {
                    counter++;
                }
                List<Object> linked = new ArrayList<Object>();
                for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    if (cell == null) {
                        linked.add(null);
                        continue;
                    }
                    DecimalFormat df = new DecimalFormat("0");// 格式化 number String 字符
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
                    DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
                    switch (cell.getCellType()) {
                        case XSSFCell.CELL_TYPE_STRING:
                            value = cell.getStringCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_NUMERIC:
                            if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                                value = df.format(cell.getNumericCellValue());
                            } else if ("General".equals(cell.getCellStyle()
                                    .getDataFormatString())) {
                                value = nf.format(cell.getNumericCellValue());
                            } else {
                                value = sdf.format(HSSFDateUtil.getJavaDate(cell
                                        .getNumericCellValue()));
                            }
                            break;
                        case XSSFCell.CELL_TYPE_BOOLEAN:
                            value = cell.getBooleanCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_BLANK:
                            value = "";
                            break;
                        default:
                            value = cell.toString();
                    }
                    if (value == null || "".equals(value)) {
                        linked.add(null);
                        continue;
                    }
                    linked.add(value);
                }
                list.add(linked);
            }
            dataList.add(list);
        }
        return dataList;
    }


    /**
     * 读取 office 2003 excel   读取 MultipartFile
     *
     * @return LinkedHashMap<String,List<List<Object>>>    Map<工作表名称，工作表内容（《行集合《列集合）>
     * @throws IOException
     * @throws FileNotFoundException
     */
    private static Map<String, List<List<Object>>> getMapTo2003Excel(MultipartFile file)
            throws IOException {
        Map<String, List<List<Object>>> dataMap = new LinkedHashMap<String, List<List<Object>>>();

        InputStream inputStream = file.getInputStream();
        POIFSFileSystem fs = new POIFSFileSystem(inputStream);
        HSSFWorkbook hwb = new HSSFWorkbook(fs);

        int sheetNum = 0;
        while (true) {
            try {
                String name = hwb.getSheetName(sheetNum);
                sheetNum++;
            } catch (Exception e) {
                break;
            }
        }

        for (int sheetI = 0; sheetI < sheetNum; sheetI++) {
            List<List<Object>> list = new ArrayList<List<Object>>();
            HSSFSheet sheet = hwb.getSheetAt(sheetI);
            String sheetName = sheet.getSheetName();
            Object value = null;
            HSSFRow row = null;
            HSSFCell cell = null;
            int counter = 0;


            //空工作表跳过，不需要跳过可以添加空集合
            if (sheet.getFirstRowNum() < 0)
                continue;


            for (int i = sheet.getFirstRowNum(); counter < sheet.getPhysicalNumberOfRows(); i++) {
                row = sheet.getRow(i);
                if (row == null) {  //空行跳过，不需要跳过可以添加空集合
                    continue;
                } else if (row.getFirstCellNum() < 0) { //伪空行跳过，不需要跳过可以添加空集合
                    counter++;
                    continue;
                } else {
                    counter++;
                }
                List<Object> linked = new ArrayList<Object>();
                for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {

                    cell = row.getCell(j);
                    if (cell == null) {
                        linked.add(null);
                        continue;
                    }
                    DecimalFormat df = new DecimalFormat("0");// 格式化 number String 字符  
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串  
                    DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字  
                    switch (cell.getCellType()) {
                        case XSSFCell.CELL_TYPE_STRING:
                        /*System.out.println(i + "行" + j + " 列 is String type");  */
                            value = cell.getStringCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_NUMERIC:
                            System.out.println(i + "行" + j
                                    + " 列 is Number type ; DateFormt:"
                                    + cell.getCellStyle().getDataFormatString());
                            if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                                value = df.format(cell.getNumericCellValue());
                            } else if ("General".equals(cell.getCellStyle()
                                    .getDataFormatString())) {
                                value = nf.format(cell.getNumericCellValue());
                            } else {
                                value = sdf.format(HSSFDateUtil.getJavaDate(cell
                                        .getNumericCellValue()));
                            }
                            break;
                        case XSSFCell.CELL_TYPE_BOOLEAN:
                       /* System.out.println(i + "行" + j + " 列 is Boolean type");*/
                            value = cell.getBooleanCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_BLANK:
                       /* System.out.println(i + "行" + j + " 列 is Blank type");  */
                            value = "";
                            break;
                        default:
                        /*System.out.println(i + "行" + j + " 列 is default type");  */
                            value = cell.toString();
                    }
                    if (value == null || "".equals(value)) {
                        linked.add(null);
                        continue;
                    }
                    linked.add(value);
                }
                list.add(linked);
            }
            dataMap.put(sheetName, list);
        }

        return dataMap;
    }

    /**
     * 读取Office 2007 excel   读取 MultipartFile
     *
     * @return LinkedHashMap<String,List<List<Object>>>    Map<工作表名称，工作表内容（《行集合《列集合）>
     */
    private static Map<String, List<List<Object>>> getMapTo2007Excel(MultipartFile file)
            throws IOException {
        Map<String, List<List<Object>>> dataMap = new LinkedHashMap<String, List<List<Object>>>();


        // 构造 XSSFWorkbook 对象，strPath 传入文件路径  
        XSSFWorkbook xwb = new XSSFWorkbook(file.getInputStream());

        int sheetNum = 0;
        while (true) {
            try {
                String name = xwb.getSheetName(sheetNum);
                sheetNum++;
            } catch (Exception e) {
                break;
            }
        }
        for (int sheetI = 0; sheetI < sheetNum; sheetI++) {
            List<List<Object>> list = new ArrayList<List<Object>>();
            // 读取第sheetI张工作表内容
            XSSFSheet sheet = xwb.getSheetAt(sheetI);
            String sheetName = sheet.getSheetName();
            Object value = null;
            XSSFRow row = null;
            XSSFCell cell = null;
            int counter = 0;


            //空工作表跳过，不需要跳过可以添加空集合
            if (sheet.getFirstRowNum() < 0)
                continue;

            for (int i = sheet.getFirstRowNum(); counter < sheet
                    .getPhysicalNumberOfRows(); i++) {
                row = sheet.getRow(i);
                if (row == null) {  //空行跳过，不需要跳过可以添加空集合
                    continue;
                } else if (row.getFirstCellNum() < 0) { //伪空行跳过，不需要跳过可以添加空集合
                    counter++;
                    continue;
                } else {
                    counter++;
                }
                List<Object> linked = new ArrayList<Object>();
                for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {

                    cell = row.getCell(j);
                    if (cell == null) {
                        linked.add(null);
                        continue;
                    }
                    DecimalFormat df = new DecimalFormat("0");// 格式化 number String 字符
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
                    DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
                    switch (cell.getCellType()) {
                        case XSSFCell.CELL_TYPE_STRING:
                            value = cell.getStringCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_NUMERIC:
                            if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                                value = df.format(cell.getNumericCellValue());
                            } else if ("General".equals(cell.getCellStyle()
                                    .getDataFormatString())) {
                                value = nf.format(cell.getNumericCellValue());
                            } else {
                                value = sdf.format(HSSFDateUtil.getJavaDate(cell
                                        .getNumericCellValue()));
                            }
                            break;
                        case XSSFCell.CELL_TYPE_BOOLEAN:
                            value = cell.getBooleanCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_BLANK:
                            value = "";
                            break;
                        default:
                            value = cell.toString();
                    }
                    if (value == null || "".equals(value)) {
                        linked.add(null);
                        continue;
                    }
                    linked.add(value);
                }
                list.add(linked);
            }
            dataMap.put(sheetName, list);
        }
        return dataMap;
    }

    
    /**
     * 创建工作簿对象
     * 工作表名称，工作表标题，工作表数据最好能够对应起来
     * 比如三个不同或相同的工作表名称，三组不同或相同的工作表标题，三组不同或相同的工作表数据
     * 注意：
     * 需要为每个工作表指定工作表名称，工作表标题，工作表数据
     * 如果工作表的数目大于工作表数据的集合，那么首先会根据顺序一一创建对应的工作表名称和数据集合，然后创建的工作表里面是没有数据的
     * 如果工作表的数目小于工作表数据的集合，那么多余的数据将不会写入工作表中
     * @param sheetName 工作表名称的数组
     * @param title 每个工作表名称的数组集合
     * @param data 每个工作表数据的集合的集合
     * @param sheets 每个工作表数据的需要合并的行列   [] 0:开始 1:结束 2:行／列[0/1]
     * @return Workbook工作簿
     * @throws FileNotFoundException 文件不存在异常 
     * @throws IOException IO异常
     * @author wangruixin
     */
    public static Workbook getWorkBook(String[] sheetName,List<? extends Object[]> title,
    		List<? extends List<? extends Object[]>> data, boolean merge, Map<String, List<Integer[]>> sheets) throws FileNotFoundException, IOException{
        //创建工作簿，支持2007及以后的文档格式
	    	XSSFWorkbook wb = new XSSFWorkbook();
	        //创建一个工作表sheet
	    	XSSFSheet sheet = null;
	        //申明行
	    	XSSFRow row = null;
	        //申明单元格
	    	XSSFCell cell = null;
        //单元格样式
        XSSFCellStyle titleStyle = getColumnTopStyleTo2007(wb);
        XSSFCellStyle cellStyle = getStyleTo2007(wb);
        
        // 设置字体
        XSSFFont titlefont = wb.createFont();
        //设置字体大小
        titlefont.setFontHeightInPoints((short) 14);
        //字体加粗
        titlefont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        //设置字体名字
        titlefont.setFontName("Courier New");
        //设置样式;
        XSSFCellStyle style = wb.createCellStyle();
        //在样式用应用设置的字体;
        style.setFont(titlefont);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        
        //字体样式
        Font font = wb.createFont();
        //粗体
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
       /* titleStyle.setFont(font);
        //水平居中  
        titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
        //垂直居中  
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
         
        //水平居中  
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        //垂直居中  
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);*/
         
        //标题数据
        Object[] title_temp=null;
         
        //行数据
        Object[] rowData=null;
         
        //工作表数据
        List<? extends Object[]> sheetData = null;
         
        //遍历sheet
        for(int sheetNumber=0;sheetNumber<sheetName.length;sheetNumber++){
            //创建工作表
            sheet = wb.createSheet();
            //设置列标题
            title_temp = title.get(sheetNumber);
            
            if(merge) {
            		List<Integer[]> merges = sheets.get(sheetName[sheetNumber]);
            		for (Integer[] integers : merges) {
            			org.apache.poi.ss.util.CellRangeAddress region = null;
						if(integers[2] == 1) {
							region = new org.apache.poi.ss.util.CellRangeAddress(integers[0]+1, // first row
									integers[1], // last row
			                        0, // first column
			                        0 // last column
			                );
						}else {
							region = new org.apache.poi.ss.util.CellRangeAddress(0, // first row
									0, // last row
									integers[0], // first column
			                        integers[1] // last column
			                );
						}
						sheet.addMergedRegion(region);
					}
            }
            //设置工作表名称
            wb.setSheetName(sheetNumber, sheetName[sheetNumber]);
            
            //设置表格标题
            XSSFRow titleRow = sheet.createRow(0);
            XSSFCell titleCell = titleRow.createCell(0);
            titleCell.setCellStyle(style);
            //DateUtil.formatDate(new Date(), "yyyy年MM月dd日  ") + 
            titleCell.setCellValue(sheetName[sheetNumber]);
            org.apache.poi.ss.util.CellRangeAddress re = new org.apache.poi.ss.util.CellRangeAddress(0, // first row
					0, // last row
					0, // first column
					title_temp.length-1 // last column
            );
            sheet.addMergedRegion(re);
            
            
            row = sheet.createRow(1);
            
            //写入标题
            for(int i=0;i<title_temp.length;i++){
                cell=row.createCell(i);
                cell.setCellStyle(titleStyle);
                cell.setCellValue(title_temp[i].toString());
            }
             
            try {
                sheetData=data.get(sheetNumber);
            } catch (Exception e) {
                continue;
            }
            //写入行数据
            for(int rowNumber=0;rowNumber<sheetData.size();rowNumber++){
                //如果没有标题栏，起始行就是0，如果有标题栏，行号就应该为1
                row=sheet.createRow(title_temp == null ? (rowNumber+1): (rowNumber+2));
                rowData=sheetData.get(rowNumber);
                for(int columnNumber=0; columnNumber < rowData.length; columnNumber ++){
                    cell=row.createCell(columnNumber);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(rowData[columnNumber].toString());
                }
            }
        }
        return wb;
    }
    
    

    public static void main(String[] args) throws Exception {

		/*
        // 创建Excel
		List<String[]> ss = new ArrayList<String[]>();
		ss.add(new String[] { "你撒地方", "知道百度" });
		ss.add(new String[] { "瓦尔", "zhidao.baidu.com" });
		eu.makeExcel("smsLog", new String[] { "色粉", "的是否" }, ss);
		// 给工作薄中新增指定索引的表单
		eu.makeEmptySheetInExistExcel("ee");
		eu.makeEmptySheetInExistExcel("ff");
		eu.makeEmptySheetInExistExcel("hh");
		eu.makeEmptySheetInExistExcel("ii");
		for (int i = 0; i < eu.workbook.getNumberOfSheets(); i++) {
			System.out.println(eu.workbook.getSheetAt(i).getSheetName());
		}*/
        // 打印表格中的数据
        /*
		  List<List<List<Object>>> strs = ExcelUtils.readExcel(new File("D:\\文章分类汇总表.xlsx")); 
		  for (List<List<Object>> str :strs) { 
			  System.out.println("------------------------------------------------------------------------------------");
			  for (List<Object> s : str) {
				  for(Object a : s){
					  System.out.print(a+"\t\t"); 
				  }
				  System.out.println();
			  }
		   }*/


        List<String> list = new ArrayList<>();
        list.add("ssss1");
        list.add("ssss2");
        list.add("ssss3");
        list.add("ssss4");
        list.add("ssss5");
        list.add("ssss6");
        list.add("ssss7");
        list.add("ssss8");
        list.add("ssss9");
        list.add("ssss10");
        list.add("ssss11");
        list.add("ssss12");
        list.add("ssss13");
        list.add("ssss14");
        list.add("ssss15");
        list.add("ssss16");
        list.add("ssss17");
        list.add("ssss18");
        list.add("ssss19");
        list.add("ssss20");
        list.add("ssss21");
        list.add("ssss22");
        list.add("ssss23");
        list.add("ssss24");

        int starIndex = 0;

        for (boolean isContinue = true; isContinue; ) {

            int endIndex = 0;
            StringBuffer sb = new StringBuffer("");
            for (int i = starIndex; i < list.size(); i++) {
				/*if("".equals(sb.toString())){
					starIndex = i;
				}*/
                endIndex = i;


                sb.append(list.get(i) + "------");


                if (5 == (endIndex - starIndex + 1)) {
                    starIndex = i + 1;
                    break;
                }
                if (endIndex == list.size() - 1) {
                    isContinue = false;
                }

            }

            System.out.println(sb.toString() + "\n");

        }


        // 给表单中指定的单元格写数据
		/*
		 * String content = "Hello Worlds"; eu.write(0,2, 3, content);
		 */

        // 构造一个空Excel
        // eu.makeEmptyExcel();

    }

}
