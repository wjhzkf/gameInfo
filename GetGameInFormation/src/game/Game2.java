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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
public class Game2 {
	productElement element=null;
	WebDriver driver;
//	JavascriptExecutor driver_js;
	/**
	 * 账户登录成功，获取游戏个数
	 */
	public Game2(productElement element,WebDriver driver) {
			this.driver=driver;
			this.element=element;
	}
	/**
	 * 获取单个游戏信息
	 * @param currentPage
	 * @param num
	 * @throws IOException
	 * @throws JSONException 
	 */
	public String[] getSingelGame(String id,int currentPage,int num,String account) throws IOException, JSONException {
		String[] infor=new String[6];
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
		infor[0]=df.format(new Date());
		infor[1]=account;
////////////获取页面当前游戏  根据selenium
		WebElement game=element.getGameElement(id,currentPage, num);
		if (game==null) return null;	

		//抓取游戏信息
//		String gameName="";
//		String description="";
//		String email="";
//		String waring="";
		//控制跳到inventory0还是inventory1
		int numTemp=num;
//////////获取页面当前游戏  根据selenium
		String itemStr=game.getAttribute("class");
		while ("".equals(itemStr)) {
			System.out.println("game.getAttribute属性抓取为空");
			itemStr=game.getAttribute("class");
		}
		
		if (itemStr.equals("itemHolder")) {
			game.click();
			
			
			Object item=((JavascriptExecutor)driver).executeScript("var info=new Array();"
																+ "var item0=document.getElementById('iteminfo0');"
															   	+ "var item1=document.getElementById('iteminfo1');"
															   	+ "var item;"
															   	+ "var itemc;"
															   	+ "var warning;"
															   	+ "var email;"
																+ "if(item0.style.display=='none'){item=item1;}"
																+ "else{item=item0;}"
																+ "for(var i=0;i<item.children[0].childElementCount;i++){"
																+ "if(item.children[0].children[i].className=='item_desc_description')"
																+ "{itemc=item.children[0].children[i];}"
																+ "}"
																+ "if(itemc.children[1].style.display=='none'){warning='';}else{"
																+ "warning=itemc.children[1].getElementsByTagName('span')[0].innerHTML;}"
																+ "if(itemc.children[5].style.display=='none'||!itemc.children[5].children[0].childElementCount==0){email='';}else{"
																+ "email=itemc.children[5].children[0].innerHTML;}"
																+ "return JSON.stringify({'name':itemc.children[0].innerHTML,'descriptor':itemc.children[3].children[0].innerHTML,'warning':warning,'email':email})");
			//将Json文件数据形成JSONObject对象
//			String itemstr=item.toString().replace("=",":");
			JSONObject jsonObject = new JSONObject(item.toString());
			infor[2]=jsonObject.getString("name");
			infor[3]=jsonObject.getString("descriptor");
			infor[4]=jsonObject.getString("warning");
			if (!"".equals(jsonObject.getString("email"))) {
				infor[5]=jsonObject.getString("email").substring(4);
			}else {
				infor[5]="";
			}
			//发送按钮
//			((JavascriptExecutor)driver).executeScript("var item="+item+";"
//					  + "return item.children[6].children[0]");
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
	 * @throws JSONException 
	 */
	public void getAllGameInformation(ExcelWrite excelWrite,String account) throws IOException, RowsExceededException, BiffException, WriteException, InterruptedException, JSONException {
		Thread.sleep(4000);
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
