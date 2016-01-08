/**
 * 
 */
package game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.jetty.html.Break;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.thoughtworks.selenium.webdriven.commands.IsElementPresent;

import dao.ExcelWrite;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import selenium.WebMethods;
import selenium.productElement;

/**
 * @author wjh
 *
 */
public class Game {
	productElement element=null;
	WebDriver driver;
//	JavascriptExecutor driver_js;
	/**
	 * 账户登录成功，获取游戏个数
	 */
	public Game(productElement element,WebDriver driver) {
			this.driver=driver;
			this.element=element;
	}
	/**
	 * 获取单个游戏信息
	 * @param currentPage
	 * @param num
	 * @throws IOException
	 */
	public String[] getSingelGame(String id,int currentPage,int num,String account) throws IOException {
		String[] infor=new String[6];
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
		infor[0]=df.format(new Date());
		infor[1]=account;
////////////获取页面当前游戏  根据selenium
		WebElement game=element.getGameElement(id,currentPage, num);
		if (game==null) return null;	

		//抓取游戏信息
		String gameName="";
		String description="";
		String email="";
		String waring="";
		//控制跳到inventory0还是inventory1
		int numTemp=num;
		num=(currentPage+num)%2;
//////////获取页面当前游戏  根据selenium
		String itemStr=game.getAttribute("class");
		while ("".equals(itemStr)) {
			System.out.println("game.getAttribute属性抓取为空");
			itemStr=game.getAttribute("class");
		}
		
		if (itemStr.equals("itemHolder")) {
//			System.out.println("style>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+game.getAttribute("style"));
//			System.out.println("disable>>>>>>>>>>>>>>>>>>>>>>>"+game.isDisplayed());
			//打开游戏简介
	game.click();
			//获取游戏名称
			gameName=element.getGameNameElement(num).getText();
			//有时候，点击游戏后，不出现游戏简介，重新点击
			while ("".equals(gameName)) {
		game.click();
				gameName=element.getGameNameElement((num+1)%2).getText();
			}
			infor[2]=gameName;
			System.out.println(gameName);
			//获取游戏描述
			int times=0;
			description=element.getGameDescriptionElement(num).getText();
			while ("".equals(description)&&times<5) {
		game.click();
				description=element.getGameDescriptionElement((num+1)%2).getText();
				times++;
			}
			infor[3]=description;
			//判断是否有警告信息
			if (!element.getWarningElement(num).getCssValue("display").equals("none")) {
				waring=element.getWarningElement(num,true).getText();
			}else {
				waring="";
			}
			infor[4]=waring;
			//判断是否有邮箱
			if (!element.getEmailElement(num).getCssValue("display").equals("none")) {
				email=element.getEmailElement(num,true).getText();
				if (!"".equals(email)) {
					email=email.substring(4);
				}
			}else {
				email="";
			}
			infor[5]=email;
		}else{
			return null;
		}
		return infor;
	}
	/**
	 * 获取全部游戏信息
	 * @throws IOException
	 * @throws WriteException 
	 * @throws BiffException 
	 * @throws RowsExceededException 
	 * @throws InterruptedException 
	 */
	public void getAllGameInformation(ExcelWrite excelWrite,String account) throws IOException, RowsExceededException, BiffException, WriteException, InterruptedException {
		Thread.sleep(5000);
///////////根据JavaScript获取游戏页面id
		String inventory="var inventoriesDiv=document.getElementById('inventories');"
				+ "var idStr;"
				+ "for(var i=0;i<inventoriesDiv.children.length;i++){if(inventoriesDiv.children[i].style.display==''){idStr=inventoriesDiv.children[i].id}}"
				+ "return idStr";
		String id= ((JavascriptExecutor)driver).executeScript(inventory).toString();
		//第几页
		int currentPage = 1;
		//控制每页游戏数
		int i=1;
		//记录游戏个数
		int num=1;
		//判断是否库存不可用
		if (!element.getFalseActiveInventory().getAttribute("style").equals("display: none;")) {System.out.println("库存不可用"); return;}
		while (true) {
			System.out.println("正在收集第"+num+"个游戏信息");
//			if (gameNum<num)break;
			if (i>25) {
				i=1;
				currentPage++;
				if (element.getNextButtonElement().getAttribute("class").equals("pagecontrol_element pagebtn disabled")) return;
				element.getNextButtonElement().click();
				//翻页面后睡3秒
				Thread.sleep(3000);
			}
			String[] data=getSingelGame(id,currentPage, i,account);
			//判断获取游戏是否为空
			if (data==null) return;
			ExcelWrite.addExcel(data, "Games");
  			i++;
			num++;
		}
	}
 
}
