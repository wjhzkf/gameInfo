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
	 * 充值所有账户
	 */
	public Map<String,String> accounts = new HashMap<String,String>();
	/**
	 * 充值所有账户ID
	 */
	public List<String> accountIDs = new ArrayList<String>();
	WebDriver driver=null;
	WebDriverWait wait=null;
	productElement element=null;
	public Game2 steamGame=null;
	ExcelWrite excelWrite=null;
	/**
	 * 游戏个数
	 */
	int gameNum=0;
	/**
	 * 初始化driver,初始化Game,初始化productElement
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
		this.frame.checkInforField.setText("！！！！正在启动浏览器，请耐心等待，请不要重复点击！！！！");		
		//临时存储账户i
		int temp=-2;
		for(int i=0;i<accounts.size();i++){
			//存储检查后账户信息
			String[] accountCheck=new String[4];
			//如果账户用户名密码错误，进行跳到下一个账户,jump=false代表继续进行
			boolean jump=false;
			String account = accountIDs.get(i);
			String password = accounts.get(accountIDs.get(i));
			accountCheck[1]=account;
			accountCheck[2]=password;
			/**
			 * loginOrNot[0]代表是否登录成功，loginOrNot[1]账户余额
			 */
			String[] loginOrNot = {"false","0"};
			
			this.frame.checkInforField.setText("！！！！！！！"+account+"正在登陆！！！！！！！");
			//调用账户替换函数，重试5次
			for(int x=5;x>0;x--){
				loginOrNot = this.login("https://store.steampowered.com/login/?cc=ru", account, password);
				//获取登录时间
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
				accountCheck[0]=df.format(new Date());
				//判断是否登录成功
				if(loginOrNot[0]=="true"){
					accountCheck[3]=loginOrNot[1];
					break;
				}else if (loginOrNot[1].equals("pwderr")) {//用户名或密码错误，直接跳入下一个账户
					if ((temp+1)==i) {
						ProgramQuit();
						this.frame.checkInforField.setText("！！！连续两次用户名密码错误！程序退出！！！");
						return;
					}else {
					temp=i;
					accountCheck[3]="用户名或密码错误";
					this.frame.checkInforField.setText("！！！用户名或密码错误！！！");
					jump=true;
					break;}
				}else if (loginOrNot[1].equals("lingpai")) {//需要邮箱验证，直接跳到下一个账户
					accountCheck[3]="需要Steam令牌";
					this.frame.checkInforField.setText("！！！需要邮箱验证码！！！");
					jump=true;
					break;
				}else if (loginOrNot[1].equals("yanzhengma")) {//需要邮箱验证，直接跳到下一个账户
						accountCheck[3]="需要验证码";
						this.frame.checkInforField.setText("！！！需要验证码！！！");
						jump=true;
						break;
				}else if(x==1){
					this.frame.checkInforField.setText("！！！登陆5次错误，请检查登录元素xpath后再试！！！");
					jump=true;
				}else{
					accountCheck[3]="";
					System.out.println(loginOrNot[0]);
					this.frame.checkInforField.setText("！！！登录异常，正在重试！！！");
				}
			}
			ExcelWrite.addExcel(accountCheck, "Accounts");
			if (jump) {
				continue;
			}else {
			this.frame.checkInforField.setText("！！！！！！！"+account+"登陆成功！正在获取game信息！！！！！！");
			//调整浏览器位置
			((JavascriptExecutor)driver).executeScript("window.scrollTo(0,0)","");
			//获取库存
			String string=element.getStockElement().getAttribute("href");
			//跳转库存页面
			driver.get(string);
			this.driver.manage().window().setSize(new Dimension(1000, 1200));
			((JavascriptExecutor)driver).executeScript("window.scrollTo(0,400)","");
			//打开礼物
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
		this.frame.checkInforField.setText("！！！！检查结束，程序退出！！！！");
		this.driver.quit();
		Runtime.getRuntime().exec("config\\kill_chromedriver.bat");
		System.out.println("全部检查成功");
	}
	
	/**
	 * 登录函数提取
	 * 传入参数：登录页地址，用户名，密码，以及登录页面的三个xpath
	 */
	public String[] login(String loginPageLink, String account, String password){
		System.out.println("当前账户："+account);
//		this.driver.manage().deleteAllCookies();
		//
		WebElement cancle=element.getCancellation();
		if (cancle!=null) {
			((JavascriptExecutor)driver).executeScript(cancle.getAttribute("href"));
		}
		this.driver.get(loginPageLink);
		//判断是否查找到登录按钮，没找到则抛出异常，继续执行下面步骤，登录错误则停止
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
			//强制睡5秒
//			Thread.sleep(15000);
			//抓取右上角账户
			element.getLoginAccountElement();		
		}catch(Exception e1){
			try {
				//密码错误
				String pwderr=wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='error_display']"))).getText();
				if (!"".equals(pwderr)) {
					login_status[1]="pwderr";
				}else {
					//令牌
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
