/**
 * 
 */
package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
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
	JSONArray emailVas;
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
	 * @throws InterruptedException 
	 */
	public String[] getSingelGame(String id,int currentPage,int num,String account,List<String>youxiang) throws IOException, JSONException, InterruptedException {
		String[] infor=new String[6];
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
		infor[0]=df.format(new Date());
		infor[1]=account;
//////////////获取页面当前游戏  根据selenium
//		WebElement game=element.getGameElement(id,currentPage, num);
//		if (game==null) return null;	
//
//		//控制跳到inventory0还是inventory1
//		int numTemp=num;
////////////获取页面当前游戏  根据selenium
//		String itemStr=game.getAttribute("class");
//		while ("".equals(itemStr)) {
//			System.out.println("game.getAttribute属性抓取为空");
//			itemStr=game.getAttribute("class");
//		}
		//利用JavaScript点击游戏元素
		String isClick=((JavascriptExecutor)driver).executeScript("var inventory=document.getElementById('inventories');"
													+"var inventory_child;"
													+"var page;"
													+"var game;"
													+"for(var i=0;i<inventory.childElementCount;i++){"
													+"  if(inventory.children[i].style.display=='none'){}else{"												
													+" inventory_child=inventory.children[i]}"
													+"}"
													+"for(var i=0;i<inventory_child.childElementCount;i++){"
													+"  if(inventory_child.children[i].style.display=='none'){}else{"
													+"  if(inventory_child.children[i].className=='inventory_page'){ page=inventory_child.children[i];}"
													+"}"
													+"}"
													+ "game=page.children['"+num+"'];"		
													+"  if(game.className=='itemHolder'){game.click();return 'trun';}else{"									
													+"return 'false';}").toString();
		
		if (isClick.equals("trun")) {
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
			
			
			
			//判断邮箱是否该游戏使用过
			String emailstr = "";
			boolean isover=false;
			JSONObject gameEmail=null;
			int copynum=-1;
			emailVas=null;
			getEmailVas();
//			if (emailVas.length()==0) {
//				gameEmail=new JSONObject();
//				emailstr=youxiang.get(0);
//			}
				for (int j = 0; j < emailVas.length(); j++) {                                //已验证的游戏邮箱
					if (isover) break;
					gameEmail=emailVas.getJSONObject(j);
					if (gameEmail.has(infor[2])) {                           //判断该游戏是否存储过
						copynum=j;
						for (int i = 0; i < youxiang.size(); i++) {                      //与将要填写的邮箱进行对比
							if (!gameEmail.toString().contains(youxiang.get(i))) {
								emailstr=youxiang.get(i).toString();
								isover=true;
								break;
							}else {
							if (i==(youxiang.size()-1)) {                                //判断使用的邮箱是否遍历完毕，如果遍历完毕，退出，不进行下面操作
							return infor;
							}
						  }
						}
					}else {
						gameEmail=null;
					}
				}
				if (gameEmail==null) {
						gameEmail=new JSONObject();
						emailstr=youxiang.get(0);
				}
			//发送按钮
			((JavascriptExecutor)driver).executeScript("var info=new Array();"
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
														+ "itemc.children[6].getElementsByTagName('a')[0].click();");
			
			//填写邮箱
			((JavascriptExecutor)driver).executeScript("var input=document.getElementById('email_input');"
													+ "input.value='"+emailstr+"';"
													+ "var tab=document.getElementById('gift_recipient_tab');"
													+ "var next=tab.lastElementChild.lastElementChild.firstElementChild;"
													+ "next.click();");
			//填写接收人信息
			((JavascriptExecutor)driver).executeScript("var input1=document.getElementById('gift_recipient_name');"
														+ "input1.value='1';"
														+"var input3=document.getElementById('gift_signature');"
														+"input3.value='1';"
														+"var input2=document.getElementById('gift_message_text');"
														+"input2.value='accounts:"+account+"';"
														+ "var next=document.getElementById('submit_gift_note_btn').getElementsByTagName('a')[1];"
														+ "next.click();");
			//返回库存
			((JavascriptExecutor)driver).executeScript("var kucun=document.getElementById('cancel_button_bottom');"
														+ "kucun.click();");
			//将游戏跟邮件信息存储
			if (copynum==-1) {
				gameEmail.append(infor[2],emailstr);
				FileWriter fw=new FileWriter(new File("config\\youxiangValidate.txt"));
				fw.write("");
				fw.write(emailVas.put(gameEmail).toString());
				fw.close();
			}else {
				FileWriter fw=new FileWriter(new File("config\\youxiangValidate.txt"));
				fw.write("");
				emailVas.getJSONObject(copynum).append(infor[2],emailstr);
				fw.write(emailVas.toString());
				fw.close();
			}
			Thread.sleep(4000);
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
	public void getAllGameInformation(ExcelWrite excelWrite,String account,List<String> youxiang) throws IOException, RowsExceededException, BiffException, WriteException, InterruptedException, JSONException {
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
				//element.getNextButtonElement().click();
				((JavascriptExecutor)driver).executeScript("var next=document.getElementById('pagebtn_next');"
														 + "next.click();");
				//翻页面后睡3秒
				Thread.sleep(3000);
			}
			String[] data=getSingelGame(id,currentPage, i,account,youxiang);
			//判断获取游戏是否为空
			if (data==null) return;
			ExcelWrite.addExcel(data, "Games");
  			i++;
			num++;
		}
	}
	public void getEmailVas() throws IOException, JSONException {
		StringBuffer stringBuffer = new StringBuffer();
		String line = null ;
		BufferedReader br = new BufferedReader(new FileReader(new File("config\\youxiangValidate.txt")));
		while( (line = br.readLine())!= null ){
			stringBuffer.append(line);
		} 
		//获取JSONObject对象数据并打印
		emailVas= new JSONArray(stringBuffer.toString());
	}
 
}
