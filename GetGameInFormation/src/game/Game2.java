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
	//��ǰ�ǵڼ�����Ϸ
	int currentNum=1;
	//�ϴη��͹�������
	int lastemail=0;
	File game_email_file;
//	JavascriptExecutor driver_js;
	/**
	 * �˻���¼�ɹ�����ȡ��Ϸ����
	 */
	public Game2(productElement element,WebDriver driver) {
			this.driver=driver;
			this.element=element;
	}
	/**
	 * ��ȡ������Ϸ��Ϣ
	 * @param currentPage
	 * @param num
	 * @throws IOException
	 * @throws JSONException 
	 * @throws InterruptedException 
	 */
	public String[] getSingelGame(int currentPage,String account,List<String>youxiang) throws IOException, JSONException, InterruptedException {
		String[] infor=new String[6];
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//�������ڸ�ʽ
		infor[0]=df.format(new Date());
		infor[1]=account;
		//�洢��������Ϸ-������Ϣ
		emailVas=null;
		getEmailVas();
		//��ǰ��Ϸ���ڵ�����
		JSONObject gameEmail=null;
		//������������Ƿ����
		boolean isover=false;
		//��ȡ��Ϸ����
		 while (((JavascriptExecutor)driver).executeScript("return document.getElementById('inventories');")==null) {
			 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>�ڻ�ȡ��ҳ��");
			 ((JavascriptExecutor)driver).executeScript("window.location.reload();");
		}
		//����JavaScript�����ϷԪ��
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
			 //��ֹ��Ϸ��Ϣҳ��û�и��£���ȡ������һ����Ϸ����Ϣ
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
			//��Json�ļ������γ�JSONObject����
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
			//�Ƿ������Ϸ
			boolean isContaint=false;
			//�жϸ���Ϸ�Ƿ��Ѿ����͹�����,����д����������������䷢��
			if (!infor[5].equals("")) {
				//��¼��ǰ��Ϸ�Լ����������ж���ǰ�Ƿ��¼��
				for (int j = 0; j < emailVas.length(); j++) {                                //����֤����Ϸ����
					gameEmail=emailVas.getJSONObject(j);
					//�����¼��
 					if (gameEmail.has(infor[2])) {   
 							isContaint=true;
							if (!gameEmail.toString().contains(infor[5])) {                    //��Ϸ�Ƿ��Ѿ�������ͬ�ʼ�
								FileWriter fw=new FileWriter(game_email_file);
								emailVas.getJSONObject(j).append(infor[2],infor[5]);      //��¼���ڵ�ǰ��Ϸ��append
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
					fw.write(emailVas.put(gameEmail).toString());                   //�µ���put
					fw.close();
				}
				//������һ����Ϸ���ж�
				currentNum++;
				return infor;
			}
			
			String emailstr = "";

			//emailVas�Ƿ�洢��ǰ��Ϸ
			boolean hasCurrentGame=false;
			//�ж������Ƿ��ظ�
				for (int j = 0; j < emailVas.length(); j++) {                                //����֤����Ϸ����
					if (isover) break;
					gameEmail=emailVas.getJSONObject(j);
					if (gameEmail.has(infor[2])) {                                          //�жϸ���Ϸ�Ƿ�洢��
						hasCurrentGame=true;
						if (lastemail!=youxiang.size()-1) {                                   //���ʹ�õ������һ�������򷵻ص�һ������
							for (int i = lastemail; i < youxiang.size(); i++) {                        //�뽫Ҫ��д��������жԱ�
								if (!gameEmail.toString().contains(youxiang.get(i))) {
									emailstr=youxiang.get(i).toString();     
									lastemail=i;
									isover=true;
									break;
								}else {
									if (i==(youxiang.size()-1)) {                                //�ж�ʹ�õ������Ƿ������ϣ����������ϣ��˳����������������
										//��������ʹ����
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
				//���ؿ���currentNum����1����ͷ��ʼ
				currentNum=1;
				 //����Ѿ��������䣬��lastemail��1
				lastemail++;
			//���Ͱ�ť
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
			
			//��д����
			((JavascriptExecutor)driver).executeScript("var input=document.getElementById('email_input');"
														+ "input.value='"+emailstr+"';");
			((JavascriptExecutor)driver).executeScript("var tab=document.getElementById('gift_recipient_tab');"
														+ "var next=tab.lastElementChild.lastElementChild.firstElementChild;"
														+ "next.click();");
			//��д��������Ϣ
			((JavascriptExecutor)driver).executeScript("var input1=document.getElementById('gift_recipient_name');"
														+ "input1.value='1';"
														+"var input3=document.getElementById('gift_signature');"
														+"input3.value='1';"
														+"var input2=document.getElementById('gift_message_text');"
														+"input2.value='accounts:"+account+"';"
														+ "var next=document.getElementById('submit_gift_note_btn').getElementsByTagName('a')[1];"
														+ "next.click();");
			//���ؿ��
			((JavascriptExecutor)driver).executeScript("var kucun=document.getElementById('cancel_button_bottom');"
														+ "kucun.click();");
		}else{
			return null;
		}
		Thread.sleep(4000);
		return infor;
	}
	/**
	 * ��ȡȫ����Ϸ��Ϣ
	 * @throws IOException
	 * @throws WriteException 
	 * @throws BiffException 
	 * @throws RowsExceededException 
	 * @throws InterruptedException 
	 * @throws JSONException 
	 */
	public void getAllGameInformation(ExcelWrite excelWrite,String account,List<String> youxiang) throws IOException, RowsExceededException, BiffException, WriteException, InterruptedException, JSONException {
		Thread.sleep(4000);
		//�û��任�����������֤

		game_email_file=new File("config\\gameEmail\\"+account+".txt");
		if (!game_email_file.exists()) {
			game_email_file.createNewFile();
			FileWriter fw=new FileWriter(game_email_file);
			fw.write("[]");
			fw.close();
		}
		//�ռ��Ѿ����͵���Ϣ
		//shoujiGameInfo();
		currentNum=1;
		lastemail=0;
		//�ڼ�ҳ
		int currentPage = 1;

		//�ж��Ƿ��治����
		if (!element.getFalseActiveInventory().getAttribute("style").equals("display: none;")) {System.out.println("��治����"); return;}
		while (true) {
			System.out.println("�����ռ���Ϸ��Ϣ");
//			if (gameNum<num)break;
			if (currentNum>25) {
				currentNum=1;
				currentPage++;
				if (element.getNextButtonElement().getAttribute("class").equals("pagecontrol_element pagebtn disabled")) return;
				//element.getNextButtonElement().click();
				((JavascriptExecutor)driver).executeScript("var next=document.getElementById('pagebtn_next');"
														 + "next.click();");
				//��ҳ���˯3��
				Thread.sleep(6000);
			}
			String[] data=getSingelGame(currentPage,account,youxiang);
			//�жϻ�ȡ��Ϸ�Ƿ�Ϊ��
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
		//��ȡJSONObject�������ݲ���ӡ
		emailVas= new JSONArray(stringBuffer.toString());
	}
	//���ε�¼�ռ����˻��������Ѿ����͵���Ϸ���ʼ�
	public void shoujiGameInfo() throws InterruptedException, IOException, JSONException {
		while (true) {
			int currentPage = 1;
			if (currentNum>25) {
				currentNum=1;
				currentPage++;
				if (element.getNextButtonElement().getAttribute("class").equals("pagecontrol_element pagebtn disabled")) return;
				((JavascriptExecutor)driver).executeScript("var next=document.getElementById('pagebtn_next');"
														 + "next.click();");
				//��ҳ���˯3��
				Thread.sleep(6000);
			}
			//�жϻ�ȡ��Ϸ�Ƿ�Ϊ��
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
		//�洢��������Ϸ-������Ϣ
		emailVas=null;
		getEmailVas();
		//��ǰ��Ϸ���ڵ�����
		JSONObject gameEmail=null;
		//������������Ƿ����
		boolean isover=false;
		//��ȡ��Ϸ����
		 while (((JavascriptExecutor)driver).executeScript("return document.getElementById('inventories');")==null) {
			 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>�ڻ�ȡ��ҳ��");
			 ((JavascriptExecutor)driver).executeScript("window.location.reload();");
		}
		//����JavaScript�����ϷԪ��
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
			//��Json�ļ������γ�JSONObject����
//			String itemstr=item.toString().replace("=",":");
			String email="";
			JSONObject jsonObject = new JSONObject(item.toString());
			String gamename=jsonObject.getString("name");;
			if (!"".equals(jsonObject.getString("email"))) {
				email=jsonObject.getString("email").substring(4);
			}else {
				email="";
			}
			//�Ƿ������Ϸ
			boolean isContaint=false;
			//�жϸ���Ϸ�Ƿ��Ѿ����͹�����,����д����������������䷢��
			if (!email.equals("")) {
				//��¼��ǰ��Ϸ�Լ����������ж���ǰ�Ƿ��¼��
				for (int j = 0; j < emailVas.length(); j++) {                                //����֤����Ϸ����
					gameEmail=emailVas.getJSONObject(j);
					//�����¼��
 					if (gameEmail.has(gamename)) {   
 							isContaint=true;
							if (!gameEmail.toString().contains(email)) {                    //��Ϸ�Ƿ��Ѿ�������ͬ�ʼ�
								FileWriter fw=new FileWriter(game_email_file);
								emailVas.getJSONObject(j).append(gamename,email);      //��¼���ڵ�ǰ��Ϸ��append
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
					fw.write(emailVas.put(gameEmail).toString());                   //�µ���put
					fw.close();
				}
			}
			return true;
		}
		return false;
	}
}
