/**
 * 
 */
package selenium;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;



/**
 * @author wjh
 *
 */
public class productElement {
	public HashMap< String,String> xpathMap=null;
	public WebDriverWait wait;
	
	/**
	 * 生成所需元素工厂
	 * @param xpath
	 * @param wait
	 */
	public productElement(getAllXpath xpath,WebDriverWait wait) {
			xpathMap=xpath.elmentXpath;
			this.wait=wait;
	}

	/**
	 * @return the userAccountElement
	 */
	public WebElement getUserAccountElement() {
		try{
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("userAccount"))));
		}catch(Exception e){
			return null;
		}
	}


	/**
	 * @return the accountDetailsElement
	 */
	public WebElement getAccountDetailsElement() {
		try{
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("accountDetails"))));
		}catch(Exception e){
			return null;
		}
	}
	/**
	 * @return the accountDetailsElement
	 */
	public WebElement getLoginAccountElement() {			
			try{
				return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("loginaccount"))));
			}catch(Exception e){
				return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("loginaccount"))));
			}
	}

	/**
	 * 获取验证码
	 * @return
	 */
	public WebElement getCaptchaElement() {
		try{
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("captcha"))));
		}catch(Exception e){
			return null;
		}
	}
	public WebElement getAccountElement() {
		try{
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("account"))));
		}catch(Exception e){
			return null;
		}
	}

	/**
	 * @return the stockElement
	 */
	public WebElement getStockElement() {
		try{
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("stock"))));
		}catch(Exception e){
			return null;
		}
	}


	/**
	 * @return the gameNumElement
	 */
	public WebElement getGameNumElement() {
		try{
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("gameNum"))));
		}catch(Exception e){
			return null;
		}
	}


	/**
	 * @return the gameElement
	 */
//	public WebElement getGameElement(int page,int num) {
//		try{
//			WebElement inventories0=wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='inventories']/div[1]")));
//			WebElement inventories1=wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='inventories']/div[2]")));
//			WebElement inventories2=wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='inventories']/div[3]")));
//			String string0=inventories0.getAttribute("style");
//			String string1=inventories1.getAttribute("style");
//			String string2=inventories2.getAttribute("style");
//			if (!inventories0.getAttribute("style").equals("display: none;")) {
//				return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='inventories']/div[1]/div["+page+"]/div["+num+"]")));
//			}else if (!inventories1.getAttribute("style").equals("display: none;")) {
//				return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='inventories']/div[2]/div["+page+"]/div["+num+"]")));
//			}else{
//				return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='inventories']/div[3]/div["+page+"]/div["+num+"]")));
//			}
//		}catch(Exception e){
//			return null;
//		}
//	}
	public WebElement getGameElement(String id,int page,int num) {
		try{
				return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='"+id+"']/div["+page+"]/div["+num+"]")));
		}catch(Exception e){
			return null;
		}
	}
	public WebElement getFalseActiveInventory() {
		try{
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='failed_inventory_page']")));
		}catch(Exception e){
			return null;
		}
	}

	public WebElement getGameLinkElement(int page,int num) {
		try{
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='inventories']/div[2]/div["+page+"]/div["+num+"]/div[1]/a")));
		}catch(Exception e){
			return null;
		}
	}
	
	public WebElement getInventory0() {
		try{
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("inventory0"))));
		}catch(Exception e){
			return null;
		}
	}
	public WebElement getInventory1() {
		try{
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("inventory1"))));
		}catch(Exception e){
			return null;
		}
	}

	/**
	 * @return the gameNameElement
	 */
	public WebElement getGameNameElement(int i) {
		try{
			if (i==0) {
				return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("inventory0")+xpathMap.get("gameName"))));
			}else {
				return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("inventory1")+xpathMap.get("gameName"))));
			}
			
		}catch(Exception e){
			return null;
		}
	}


	/**
	 * @return the warningElement
	 */
	public WebElement getWarningElement(int i) {
		try{
			if (i==0) {
				return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("inventory0")+xpathMap.get("warning"))));
			}else {
				return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("inventory1")+xpathMap.get("warning"))));
			}
			
		}catch(Exception e){
			return null;
		}
	}
	public WebElement getWarningElement(int i,boolean f) {
		try{
			if (i==0) {
				return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("inventory0")+xpathMap.get("haswarning"))));
			}else {
				return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("inventory1")+xpathMap.get("haswarning"))));
			}
			
		}catch(Exception e){
			return null;
		}
	}

	/**
	 * @return the gameDescriptionElement
	 */
	public WebElement getGameDescriptionElement(int i) {
		try{
			if (i==0) {
				return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("inventory0")+xpathMap.get("gameDescription"))));
			}else {
				return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("inventory1")+xpathMap.get("gameDescription"))));
			}
			
		}catch(Exception e){
			return null;
		}
	}




	/**
	 * @return the emailElement
	 */
	public WebElement getEmailElement(int i) {
		try{
			if (i==0) {
				return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("inventory0")+xpathMap.get("email"))));
			}else {
				return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("inventory1")+xpathMap.get("email"))));
			}
			
		}catch(Exception e){
			return null;
		}
	}
	public WebElement getEmailElement(int i,boolean f) {
		try{
			if (i==0) {
				return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("inventory0")+xpathMap.get("hasemail"))));
			}else {
				return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("inventory1")+xpathMap.get("hasemail"))));
			}
			
		}catch(Exception e){
			return null;
		}
	}


	/**
	 * @return the maxPageElement
	 */
	public WebElement getMaxPageElement() {
		try{
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("maxPage"))));
		}catch(Exception e){
			return null;
		}
	}


	/**
	 * @return the currentPageElement
	 */
	public WebElement getCurrentPageElement() {
		try{
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("currentPage"))));
		}catch(Exception e){
			return null;
		}
	}
	public WebElement getNextButtonElement() {
		try{
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("nextButton"))));
		}catch(Exception e){
			return null;
		}
	}
	/**
	 * @return the currentPageElement
	 */
	public WebElement getLoginIdField() {
		try{
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("loginIdField"))));
		}catch(Exception e){
			return null;
		}
	}
	/**
	 * @return the currentPageElement
	 */
	public WebElement getLoginPwField() {
		try{
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("loginPwField"))));
		}catch(Exception e){
			return null;
		}
	}
	/**
	 * @return the currentPageElement
	 */
	public WebElement getLoginButton() {
		try{
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("loginButton"))));
		}catch(Exception e){
			return null;
		}
	}
	public WebElement getCancellation() {
		try{
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("cancellation"))));
		}catch(Exception e){
			return null;
		}
	}
	/**
	 * 获取礼物
	 * @return
	 */
	public WebElement getGift() {
		try{
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("gift"))));
		}catch(Exception e){
			return null;
		}
	}
	/**
	 * 获取礼物下拉框
	 * @return
	 */
	public WebElement getGifts() {
		try{
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathMap.get("gifts"))));
		}catch(Exception e){
			return null;
		}
	}
}
