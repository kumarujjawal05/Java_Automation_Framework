package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.IOException;

public class ExcelUtils {

    private static final Logger logger = LogManager.getLogger(ExcelUtils.class);
    private final String filePath;


    public ExcelUtils(String filePath){
        this.filePath = filePath;
    }

    public Object[][] getSheetData(String sheetName){
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)){

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null){
                throw new IllegalArgumentException("Sheet '" + sheetName + "' not found in " + filePath);
            }

            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getLastCellNum();

            Object[][] data = new Object[rowCount - 1][colCount];

            for (int i =1; i<rowCount; i++){
                Row row = sheet.getRow(i);
                for (int j=0; j<colCount; j++){
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    data[i - 1][j] = getCellValue(cell);
                }
            }
            return data;

        } catch (IOException e){
            logger.error("Error reading excel file: {}", filePath, e);
            throw new RuntimeException("Error reading excel file: " + filePath, e);
        }
    }

    private Object getCellValue(Cell cell){
        return switch (cell.getCellType()){
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> cell.getNumericCellValue();
            case BOOLEAN -> cell.getBooleanCellValue();
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
}
