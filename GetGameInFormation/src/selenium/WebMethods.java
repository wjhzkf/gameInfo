/**
 * 
 */
package selenium;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.jdom2.JDOMException;
import org.json.JSONException;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import dao.ExcelWrite;
import frame.AccountCheckUI;
import game.Game2;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


/**
 * @author wjh
 *
 */
public class WebMethods {
	AccountCheckUI frame=null;
	/**
	 * ��ֵ�����˻�
	 */
	public Map<String,String> accounts = new HashMap<String,String>();
	/**
	 * ��ֵ�����˻�ID
	 */
	public List<String> accountIDs = new ArrayList<String>();
	WebDriver driver=null;
	WebDriverWait wait=null;
	productElement element=null;
	public Game2 steamGame=null;
	ExcelWrite excelWrite=null;
	/**
	 * ��Ϸ����
	 */
	int gameNum=0;
	/**
	 * ��ʼ��driver,��ʼ��Game,��ʼ��productElement
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * 
	 */
	public WebMethods(AccountCheckUI frame) throws IOException{
		this.frame=frame;
		this.accounts = this.frame.accounts;
		this.accountIDs = this.frame.accountIDs;
		// Define Chrome Option, create web Driver
		ChromeOptions options = new ChromeOptions();
		options.addExtensions(new File("driver/Block-image_v1.1.crx"), new File("driver/StopFlash-Flash-Blocker_v0.1.5.crx"));
			System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
			this.driver = new ChromeDriver(options);
		    this.wait = new WebDriverWait(driver, 10);	   
			this.driver.manage().window().setPosition(new Point(0,0));
			this.driver.manage().window().setSize(new Dimension(600, 400));
			getAllXpath xpath=new getAllXpath();
			element=new productElement(xpath, wait);
			steamGame=new Game2(element,driver);
			
	}
	
	
	public synchronized void batchStart() throws InterruptedException, JDOMException, IOException, RowsExceededException, BiffException, WriteException, JSONException{
		this.frame.checkInforField.setText("����������������������������ĵȴ����벻Ҫ�ظ������������");		
		//��ʱ�洢�˻�i
		int temp=-2;
		for(int i=0;i<accounts.size();i++){
			//�洢�����˻���Ϣ
			String[] accountCheck=new String[4];
			//����˻��û���������󣬽���������һ���˻�,jump=false�����������
			boolean jump=false;
			String account = accountIDs.get(i);
			String password = accounts.get(accountIDs.get(i));
			accountCheck[1]=account;
			accountCheck[2]=password;
			/**
			 * loginOrNot[0]�����Ƿ��¼�ɹ���loginOrNot[1]�˻����
			 */
			String[] loginOrNot = {"false","0"};
			
			this.frame.checkInforField.setText("��������������"+account+"���ڵ�½��������������");
			//�����˻��滻����������5��
			for(int x=5;x>0;x--){
				loginOrNot = this.login("https://store.steampowered.com/login/?cc=ru", account, password);
				//��ȡ��¼ʱ��
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
				accountCheck[0]=df.format(new Date());
				//�ж��Ƿ��¼�ɹ�
				if(loginOrNot[0]=="true"){
					accountCheck[3]=loginOrNot[1];
					break;
				}else if (loginOrNot[1].equals("pwderr")) {//�û������������ֱ��������һ���˻�
					if ((temp+1)==i) {
						ProgramQuit();
						this.frame.checkInforField.setText("���������������û���������󣡳����˳�������");
						return;
					}else {
					temp=i;
					accountCheck[3]="�û������������";
					this.frame.checkInforField.setText("�������û�����������󣡣���");
					jump=true;
					break;}
				}else if (loginOrNot[1].equals("lingpai")) {//��Ҫ������֤��ֱ��������һ���˻�
					accountCheck[3]="��ҪSteam����";
					this.frame.checkInforField.setText("��������Ҫ������֤�룡����");
					jump=true;
					break;
				}else if (loginOrNot[1].equals("yanzhengma")) {//��Ҫ������֤��ֱ��������һ���˻�
						accountCheck[3]="��Ҫ��֤��";
						this.frame.checkInforField.setText("��������Ҫ��֤�룡����");
						jump=true;
						break;
				}else if(x==1){
					this.frame.checkInforField.setText("��������½5�δ��������¼Ԫ��xpath�����ԣ�����");
					jump=true;
				}else{
					accountCheck[3]="";
					System.out.println(loginOrNot[0]);
					this.frame.checkInforField.setText("��������¼�쳣���������ԣ�����");
				}
			}
			ExcelWrite.addExcel(accountCheck, "Accounts");
			if (jump) {
				continue;
			}else {
			this.frame.checkInforField.setText("��������������"+account+"��½�ɹ������ڻ�ȡgame��Ϣ������������");
			//���������λ��
			((JavascriptExecutor)driver).executeScript("window.scrollTo(0,0)","");
			//��ȡ���
			String string=element.getStockElement().getAttribute("href");
			//��ת���ҳ��
			driver.get(string);
			this.driver.manage().window().setSize(new Dimension(1000, 1200));
			((JavascriptExecutor)driver).executeScript("window.scrollTo(0,400)","");
			//������
			String openGifts="var contextOption=document.getElementById('contextselect_options');"
							+ "contextOption.style.display='block';"
							+ "var contextSelect=document.getElementById('contextselect_options_contexts');"
							+ "var gifts=contextSelect.getElementsByTagName('div');"
							+ "gifts[1].click();";
			((JavascriptExecutor)driver).executeScript(openGifts);
 			steamGame.getAllGameInformation(excelWrite,account,frame.youxiang);
			}
		}
		ProgramQuit();
		return;
	}


	/**
	 * @throws IOException
	 */
	public void ProgramQuit() throws IOException {
		this.frame.checkInforField.setText("���������������������˳���������");
		this.driver.quit();
		Runtime.getRuntime().exec("config\\kill_chromedriver.bat");
		System.out.println("ȫ�����ɹ�");
	}
	
	/**
	 * ��¼������ȡ
	 * �����������¼ҳ��ַ���û��������룬�Լ���¼ҳ�������xpath
	 */
	public String[] login(String loginPageLink, String account, String password){
		System.out.println("��ǰ�˻���"+account);
//		this.driver.manage().deleteAllCookies();
		//
		WebElement cancle=element.getCancellation();
		if (cancle!=null) {
			((JavascriptExecutor)driver).executeScript(cancle.getAttribute("href"));
		}
		this.driver.get(loginPageLink);
		//�ж��Ƿ���ҵ���¼��ť��û�ҵ����׳��쳣������ִ�����沽�裬��¼������ֹͣ
		WebElement login_userName = null;
		WebElement login_password = null;
		WebElement login_button = null; 
		WebElement account_balance = null;
		String account_balance_value = null;
		String login_status[] = new String[2];
		int ii = 0;
		login_status[0] = "false";
		login_status[1] = account_balance_value;
		
		login_userName =element.getLoginIdField();
		login_password = element.getLoginPwField();
		login_button = element.getLoginButton();
			
		login_userName.clear();
		login_password.clear();
		login_userName.sendKeys(account);
		login_password.sendKeys(password);	
		login_button.click();
		try{
			this.driver.manage().window().setSize(new Dimension(1000, 400));	
			//ǿ��˯5��
//			Thread.sleep(15000);
			//ץȡ���Ͻ��˻�
			element.getLoginAccountElement();		
		}catch(Exception e1){
			try {
				//�������
				String pwderr=wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='error_display']"))).getText();
				if (!"".equals(pwderr)) {
					login_status[1]="pwderr";
				}else {
					//����
					String lingpai=wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class='ellipsis']"))).getText();
					if (!"".equals(lingpai)) {
						login_status[1]="lingpai";
					}
				}
				return login_status;
			} catch (Exception e) {
				login_status[1]="error";
				return login_status;
			}
		}
		try {
			account_balance = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("header_wallet_balance")));
			this.driver.manage().window().setSize(new Dimension(1000, 400));
			account_balance_value = account_balance.getText();
			System.out.println("balance = "+account_balance_value);
		} catch (Exception e) {
			account_balance_value="0 py6.";
		}
		login_status[0] = "true";
		login_status[1] = account_balance_value;
		return login_status;
	}
	
}
