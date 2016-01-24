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
	 */
	public String[] getSingelGame(String id,int currentPage,int num,String account) throws IOException, JSONException {
		String[] infor=new String[6];
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//�������ڸ�ʽ
		infor[0]=df.format(new Date());
		infor[1]=account;
////////////��ȡҳ�浱ǰ��Ϸ  ����selenium
		WebElement game=element.getGameElement(id,currentPage, num);
		if (game==null) return null;	

		//ץȡ��Ϸ��Ϣ
//		String gameName="";
//		String description="";
//		String email="";
//		String waring="";
		//��������inventory0����inventory1
		int numTemp=num;
//////////��ȡҳ�浱ǰ��Ϸ  ����selenium
		String itemStr=game.getAttribute("class");
		while ("".equals(itemStr)) {
			System.out.println("game.getAttribute����ץȡΪ��");
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
			//��Json�ļ������γ�JSONObject����
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
			//���Ͱ�ť
//			((JavascriptExecutor)driver).executeScript("var item="+item+";"
//					  + "return item.children[6].children[0]");
		}else{
			return null;
		}
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
	public void getAllGameInformation(ExcelWrite excelWrite,String account) throws IOException, RowsExceededException, BiffException, WriteException, InterruptedException, JSONException {
		Thread.sleep(4000);
///////////����JavaScript��ȡ��Ϸҳ��id
		String inventory="var inventoriesDiv=document.getElementById('inventories');"
				+ "var idStr;"
				+ "for(var i=0;i<inventoriesDiv.children.length;i++){if(inventoriesDiv.children[i].style.display==''){idStr=inventoriesDiv.children[i].id}}"
				+ "return idStr";
		String id= ((JavascriptExecutor)driver).executeScript(inventory).toString();
		//�ڼ�ҳ
		int currentPage = 1;
		//����ÿҳ��Ϸ��
		int i=1;
		//��¼��Ϸ����
		int num=1;
		//�ж��Ƿ��治����
		if (!element.getFalseActiveInventory().getAttribute("style").equals("display: none;")) {System.out.println("��治����"); return;}
		while (true) {
			System.out.println("�����ռ���"+num+"����Ϸ��Ϣ");
//			if (gameNum<num)break;
			if (i>25) {
				i=1;
				currentPage++;
				if (element.getNextButtonElement().getAttribute("class").equals("pagecontrol_element pagebtn disabled")) return;
				element.getNextButtonElement().click();
				//��ҳ���˯3��
				Thread.sleep(3000);
			}
			String[] data=getSingelGame(id,currentPage, i,account);
			//�жϻ�ȡ��Ϸ�Ƿ�Ϊ��
			if (data==null) return;
			ExcelWrite.addExcel(data, "Games");
  			i++;
			num++;
		}
	}
 
}
