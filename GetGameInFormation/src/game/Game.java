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
	 * �˻���¼�ɹ�����ȡ��Ϸ����
	 */
	public Game(productElement element,WebDriver driver) {
			this.driver=driver;
			this.element=element;
	}
	/**
	 * ��ȡ������Ϸ��Ϣ
	 * @param currentPage
	 * @param num
	 * @throws IOException
	 */
	public String[] getSingelGame(String id,int currentPage,int num,String account) throws IOException {
		String[] infor=new String[6];
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//�������ڸ�ʽ
		infor[0]=df.format(new Date());
		infor[1]=account;
////////////��ȡҳ�浱ǰ��Ϸ  ����selenium
		WebElement game=element.getGameElement(id,currentPage, num);
		if (game==null) return null;	

		//ץȡ��Ϸ��Ϣ
		String gameName="";
		String description="";
		String email="";
		String waring="";
		//��������inventory0����inventory1
		int numTemp=num;
		num=(currentPage+num)%2;
//////////��ȡҳ�浱ǰ��Ϸ  ����selenium
		String itemStr=game.getAttribute("class");
		while ("".equals(itemStr)) {
			System.out.println("game.getAttribute����ץȡΪ��");
			itemStr=game.getAttribute("class");
		}
		
		if (itemStr.equals("itemHolder")) {
//			System.out.println("style>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+game.getAttribute("style"));
//			System.out.println("disable>>>>>>>>>>>>>>>>>>>>>>>"+game.isDisplayed());
			//����Ϸ���
	game.click();
			//��ȡ��Ϸ����
			gameName=element.getGameNameElement(num).getText();
			//��ʱ�򣬵����Ϸ�󣬲�������Ϸ��飬���µ��
			while ("".equals(gameName)) {
		game.click();
				gameName=element.getGameNameElement((num+1)%2).getText();
			}
			infor[2]=gameName;
			System.out.println(gameName);
			//��ȡ��Ϸ����
			int times=0;
			description=element.getGameDescriptionElement(num).getText();
			while ("".equals(description)&&times<5) {
		game.click();
				description=element.getGameDescriptionElement((num+1)%2).getText();
				times++;
			}
			infor[3]=description;
			//�ж��Ƿ��о�����Ϣ
			if (!element.getWarningElement(num).getCssValue("display").equals("none")) {
				waring=element.getWarningElement(num,true).getText();
			}else {
				waring="";
			}
			infor[4]=waring;
			//�ж��Ƿ�������
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
	 * ��ȡȫ����Ϸ��Ϣ
	 * @throws IOException
	 * @throws WriteException 
	 * @throws BiffException 
	 * @throws RowsExceededException 
	 * @throws InterruptedException 
	 */
	public void getAllGameInformation(ExcelWrite excelWrite,String account) throws IOException, RowsExceededException, BiffException, WriteException, InterruptedException {
		Thread.sleep(5000);
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
