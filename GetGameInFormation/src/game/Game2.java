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
	//当前是第几个游戏
	int currentNum=1;
	//上次发送过的邮箱
	int lastemail=0;
	File game_email_file;
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
	public String[] getSingelGame(int currentPage,String account,List<String>youxiang) throws IOException, JSONException, InterruptedException {
		String[] infor=new String[6];
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
		infor[0]=df.format(new Date());
		infor[1]=account;
		//存储的整个游戏-邮箱信息
		emailVas=null;
		getEmailVas();
		//当前游戏存在的邮箱
		JSONObject gameEmail=null;
		//控制邮箱遍历是否完成
		boolean isover=false;
		//获取游戏界面
		 while (((JavascriptExecutor)driver).executeScript("return document.getElementById('inventories');")==null) {
			 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>在获取主页面");
			 ((JavascriptExecutor)driver).executeScript("window.location.reload();");
		}
		//利用JavaScript点击游戏元素
		String isClick;
			 isClick=((JavascriptExecutor)driver).executeScript("var inventory=document.getElementById('inventories');"
																+"var inventory_child;"
																+"var page;"
																+"var game;"
																+"for(var i=0;i<inventory.childElementCount;i++){"
																+"  if(inventory.children[i].style.display=='none'){}else{"												
																+" inventory_child=inventory.children[i]}"
																+"}"
																+ " while (inventory_child==null) {"
																	+"for(var i=0;i<inventory.childElementCount;i++){"
																	+"  if(inventory.children[i].style.display=='none'){}else{"												
																	+" inventory_child=inventory.children[i]}"
																	+"}"
																+ "}"
																+"for(var i=0;i<inventory_child.childElementCount;i++){"
																+"  if(inventory_child.children[i].style.display=='none'){}else{"
																+"  if(inventory_child.children[i].className=='inventory_page'){ page=inventory_child.children[i];}"
																+"}"
																+"}"
																+ "game=page.children['"+(currentNum-1)+"'];"		
																+"  if(game.className=='itemHolder'){game.firstElementChild.firstElementChild.click();return 'trun';}else{"									
																+"return 'false';}").toString();
			 //防止游戏信息页面没有更新，提取的是上一个游戏的信息
				Thread.sleep(3000);
		
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
																+ "if(itemc.children[5].style.display=='none'){email='';}else{"
																+ "email=itemc.children[5].lastElementChild.innerHTML;}"
																+ "return JSON.stringify({'name':itemc.children[0].innerHTML,'descriptor':itemc.children[3].children[0].innerHTML,'warning':warning,'email':email})");
			//将Json文件数据形成JSONObject对象
//			String itemstr=item.toString().replace("=",":");
			JSONObject jsonObject = new JSONObject(item.toString());
			infor[2]=jsonObject.getString("name");
			infor[3]=jsonObject.getString("descriptor");
			infor[4]=jsonObject.getString("warning");
			if (!"".equals(jsonObject.getString("email"))&&jsonObject.getString("email").contains("@")) {
				infor[5]=jsonObject.getString("email").substring(4);
			}else {
				infor[5]="";
			}
			//是否包含游戏
			boolean isContaint=false;
			//判断该游戏是否已经发送过邮箱,如果有存在邮箱则跳过邮箱发送
			if (!infor[5].equals("")) {
				//记录当前游戏以及邮箱首先判断以前是否记录过
				for (int j = 0; j < emailVas.length(); j++) {                                //已验证的游戏邮箱
					gameEmail=emailVas.getJSONObject(j);
					//如果记录过
 					if (gameEmail.has(infor[2])) {   
 							isContaint=true;
							if (!gameEmail.toString().contains(infor[5])) {                    //游戏是否已经发送相同邮件
								FileWriter fw=new FileWriter(game_email_file);
								emailVas.getJSONObject(j).append(infor[2],infor[5]);      //记录过在当前游戏后append
								fw.write(emailVas.toString());
								fw.close();
								break;
							}
						}
				}
				if (!isContaint) {
					gameEmail=new JSONObject();
					gameEmail.append(infor[2],infor[5]);
					FileWriter fw=new FileWriter(game_email_file);
//					fw.write("");
					fw.write(emailVas.put(gameEmail).toString());                   //新的则put
					fw.close();
				}
				//进行下一个游戏的判断
				currentNum++;
				return infor;
			}
			
			String emailstr = "";

			//emailVas是否存储当前游戏
			boolean hasCurrentGame=false;
			//判断邮箱是否重复
				for (int j = 0; j < emailVas.length(); j++) {                                //已验证的游戏邮箱
					if (isover) break;
					gameEmail=emailVas.getJSONObject(j);
					if (gameEmail.has(infor[2])) {                                          //判断该游戏是否存储过
						hasCurrentGame=true;
						if (lastemail!=youxiang.size()-1) {                                   //如果使用到了最后一个邮箱则返回第一个邮箱
							for (int i = lastemail; i < youxiang.size(); i++) {                        //与将要填写的邮箱进行对比
								if (!gameEmail.toString().contains(youxiang.get(i))) {
									emailstr=youxiang.get(i).toString();     
									lastemail=i;
									isover=true;
									break;
								}else {
									if (i==(youxiang.size()-1)) {                                //判断使用的邮箱是否遍历完毕，如果遍历完毕，退出，不进行下面操作
										//测试邮箱使用完
//										currentNum++;
										currentNum=0;
										lastemail=0;
										return infor;
								}
							  }
							}
						}else {
							lastemail=0;
						}
					}
				}
				if (!hasCurrentGame) {
						gameEmail=new JSONObject();
						emailstr=youxiang.get(lastemail);
				}
				System.out.println(emailstr);
				//返回库存后，currentNum设置1，从头开始
				currentNum=1;
				 //如果已经发送邮箱，则lastemail加1
				lastemail++;
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
														+ "input.value='"+emailstr+"';");
			((JavascriptExecutor)driver).executeScript("var tab=document.getElementById('gift_recipient_tab');"
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
		}else{
			return null;
		}
		Thread.sleep(4000);
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
		//用户变换是情况邮箱验证

		game_email_file=new File("config\\gameEmail\\"+account+".txt");
		if (!game_email_file.exists()) {
			game_email_file.createNewFile();
			FileWriter fw=new FileWriter(game_email_file);
			fw.write("[]");
			fw.close();
		}
		//收集已经发送的信息
		//shoujiGameInfo();
		currentNum=1;
		lastemail=0;
		//第几页
		int currentPage = 1;

		//判断是否库存不可用
		if (!element.getFalseActiveInventory().getAttribute("style").equals("display: none;")) {System.out.println("库存不可用"); return;}
		while (true) {
			System.out.println("正在收集游戏信息");
//			if (gameNum<num)break;
			if (currentNum>25) {
				currentNum=1;
				currentPage++;
				if (element.getNextButtonElement().getAttribute("class").equals("pagecontrol_element pagebtn disabled")) return;
				//element.getNextButtonElement().click();
				((JavascriptExecutor)driver).executeScript("var next=document.getElementById('pagebtn_next');"
														 + "next.click();");
				//翻页面后睡3秒
				Thread.sleep(6000);
			}
			String[] data=getSingelGame(currentPage,account,youxiang);
			//判断获取游戏是否为空
			if (data==null) return;
//			ExcelWrite.addExcel(data, "Games");
//			currentNum++;
		}
	}
	public void getEmailVas() throws IOException, JSONException {
		StringBuffer stringBuffer = new StringBuffer();
		String line = null ;
		BufferedReader br = new BufferedReader(new FileReader(game_email_file));
		while( (line = br.readLine())!= null ){
			stringBuffer.append(line);
		} 
		//获取JSONObject对象数据并打印
		emailVas= new JSONArray(stringBuffer.toString());
	}
	//初次登录收集该账户上所有已经发送的游戏及邮件
	public void shoujiGameInfo() throws InterruptedException, IOException, JSONException {
		while (true) {
			int currentPage = 1;
			if (currentNum>25) {
				currentNum=1;
				currentPage++;
				if (element.getNextButtonElement().getAttribute("class").equals("pagecontrol_element pagebtn disabled")) return;
				((JavascriptExecutor)driver).executeScript("var next=document.getElementById('pagebtn_next');"
														 + "next.click();");
				//翻页面后睡3秒
				Thread.sleep(6000);
			}
			//判断获取游戏是否为空
			if (!getemailGame()) return;
			currentNum++;
		}
	}
	/**
	 * @param currentPage
	 * @return
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws InterruptedException 
	 */
	private boolean getemailGame() throws IOException, JSONException, InterruptedException {
		//存储的整个游戏-邮箱信息
		emailVas=null;
		getEmailVas();
		//当前游戏存在的邮箱
		JSONObject gameEmail=null;
		//控制邮箱遍历是否完成
		boolean isover=false;
		//获取游戏界面
		 while (((JavascriptExecutor)driver).executeScript("return document.getElementById('inventories');")==null) {
			 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>在获取主页面");
			 ((JavascriptExecutor)driver).executeScript("window.location.reload();");
		}
		//利用JavaScript点击游戏元素
		String isClick;
			 isClick=((JavascriptExecutor)driver).executeScript("var inventory=document.getElementById('inventories');"
																+"var inventory_child;"
																+"var page;"
																+"var game;"
																+"for(var i=0;i<inventory.childElementCount;i++){"
																+"  if(inventory.children[i].style.display=='none'){}else{"												
																+" inventory_child=inventory.children[i]}"
																+"}"
																+ " while (inventory_child==null) {"
																	+"for(var i=0;i<inventory.childElementCount;i++){"
																	+"  if(inventory.children[i].style.display=='none'){}else{"												
																	+" inventory_child=inventory.children[i]}"
																	+"}"
																+ "}"
																+"for(var i=0;i<inventory_child.childElementCount;i++){"
																+"  if(inventory_child.children[i].style.display=='none'){}else{"
																+"  if(inventory_child.children[i].className=='inventory_page'){ page=inventory_child.children[i];}"
																+"}"
																+"}"
																+ "game=page.children['"+(currentNum-1)+"'];"		
																+"  if(game.className=='itemHolder'){game.firstElementChild.firstElementChild.click();return 'true';}else{"									
																+"return 'false';}").toString();

		
		if (isClick.equals("true")) {
			Thread.sleep(3000);
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
			String email="";
			JSONObject jsonObject = new JSONObject(item.toString());
			String gamename=jsonObject.getString("name");;
			if (!"".equals(jsonObject.getString("email"))) {
				email=jsonObject.getString("email").substring(4);
			}else {
				email="";
			}
			//是否包含游戏
			boolean isContaint=false;
			//判断该游戏是否已经发送过邮箱,如果有存在邮箱则跳过邮箱发送
			if (!email.equals("")) {
				//记录当前游戏以及邮箱首先判断以前是否记录过
				for (int j = 0; j < emailVas.length(); j++) {                                //已验证的游戏邮箱
					gameEmail=emailVas.getJSONObject(j);
					//如果记录过
 					if (gameEmail.has(gamename)) {   
 							isContaint=true;
							if (!gameEmail.toString().contains(email)) {                    //游戏是否已经发送相同邮件
								FileWriter fw=new FileWriter(game_email_file);
								emailVas.getJSONObject(j).append(gamename,email);      //记录过在当前游戏后append
								fw.write(emailVas.toString());
								fw.close();
								break;
							}
						}
				}
				if (!isContaint) {
					gameEmail=new JSONObject();
					gameEmail.append(gamename,email);
					FileWriter fw=new FileWriter(game_email_file);
					fw.write(emailVas.put(gameEmail).toString());                   //新的则put
					fw.close();
				}
			}
			return true;
		}
		return false;
	}
}
