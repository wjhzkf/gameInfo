/**
 * 
 */
package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Iterator;


import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;



/**
 * @author wjh
 *
 */
public class ExcelWrite {


    private static File hasFile;  
    
    /** 
     * ͬ����������ֹ������ 
     *  
     * @param args 
     * @return 
     * @throws IOException 
     * @throws RowsExceededException 
     * @throws WriteException 
     */  
    public synchronized static String[] write()  
            throws IOException, RowsExceededException, WriteException {  

        // �ļ�·��  
        // �ж��ļ��Ƿ���ڣ�������ھͲ�������׷�ӣ�����������򴴽��ļ���׷�ӡ�  
        WritableWorkbook book = Workbook.createWorkbook(getHasFile());  
        book.setProtected(true);  
        // -- ��һ��������Sheet�����ڶ���������Sheet�±�  
        // -- �±���������ֻ���ʶ���ã�������ʱ�����create˳�������������ɵ�EXCEL�ļ���һ��Sheet��sheet1  
        WritableSheet sheet = book.createSheet("Games", 1);  
        sheet.setColumnView(0, 25);  
        sheet.setColumnView(1, 15);  
        sheet.setColumnView(2, 30);  
        sheet.setColumnView(3, 40);  
        sheet.setColumnView(4, 40);  
        sheet.setColumnView(5, 30);  
        sheet.getSettings().setProtected(false);  
        
        String[] title = { "��ȡʱ��","�˻�", "��Ϸ��", "��Ϸ����", "������Ϣ", "��������"};  
        for (int i = 0; i < title.length; i++) {  
            Label lable = new Label(i, 0, title[i]);  
            sheet.addCell(lable);  
        }  

        WritableSheet sheet2 = book.createSheet("Accounts", 2);  
        sheet2.setColumnView(0, 25);  
        sheet2.setColumnView(1, 15);  
        sheet2.setColumnView(2, 15);  
        sheet2.setColumnView(3, 20);  
        sheet2.getSettings().setProtected(false);  
//        sheet2.getSettings().setPassword("xxxx");//��������  
        
        String[] title2 = {"��¼ʱ��", "�˻�", "����", "״̬"};  
        for (int i = 0; i < title2.length; i++) {  
            Label lable = new Label(i, 0, title2[i]);  
            sheet2.addCell(lable);  
        }  
        
        book.write();  
        book.close();  
        System.out.println("д��ɹ�");  
        return null;  
    }  
  
    /** 
     * ׷��excel 
     *  
     * @param args 
     * @throws IOException 
     * @throws BiffException 
     * @throws WriteException 
     * @throws RowsExceededException 
     */  
    public static void addExcel(String[] args,String sheetStr) throws BiffException,  
            IOException, RowsExceededException, WriteException {  
    	File file1 = new File("config/accounts.xls");  
        if (!file1.exists()) {  
        	filecheck("config/accounts.xls");
            write();
         }  
    	setHasFile(file1);
        Workbook book = Workbook.getWorkbook(getHasFile());  
        Sheet sheet = book.getSheet(sheetStr);
        // ��ȡ��  
        int length = sheet.getRows();  
 
        System.out.println(length);  
        WritableWorkbook wbook = Workbook.createWorkbook(getHasFile(), book); // ����book����һ����������  
        WritableSheet sh = wbook.getSheet(sheetStr);// �õ�һ����������  
        // �����һ�п�ʼ��  
        for (int i = 0; i < args.length; i++) {  
            Label label = new Label(i, length, args[i]);  
            sh.addCell(label);  
        }  
        wbook.write();  
        wbook.close();  
    }  
  
    /** 
     * �ж��ļ��Ƿ��Ѿ�д�� 
     *  
     * @param filename 
     * @return 
     */  
    public static boolean filecheck(String filename) {  
        boolean flag = false;  
        File file = new File(filename);  
        if (file.exists()) {  
            flag = true;  
        }  
        setHasFile(file);  
        return flag;  
    }  
    /** 
     * @return the hasFile 
     */  
    public static File getHasFile() {  
        return hasFile;  
    }  
  
    /** 
     * @param hasFile 
     *            the hasFile to set 
     */  
    public static void setHasFile(File hasFile) {  
    	ExcelWrite.hasFile = hasFile;  
    } 
	
	
}
