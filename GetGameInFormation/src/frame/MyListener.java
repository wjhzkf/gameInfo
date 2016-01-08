/**
 * 
 */
package frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import org.jdom2.JDOMException;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import selenium.WebMethods;

/**
 * @author wjh
 *
 */
public class MyListener implements ActionListener {
	AccountCheckUI frame;
	public MyListener(AccountCheckUI frame) {
		 this.frame = frame;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "≈‰÷√’Àªß"){
			try {
				Process child = Runtime.getRuntime().exec("cmd  /c  start config\\Accounts_Validate.txt");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		if(e.getActionCommand() == "’ÀªßºÏ≤È"){
			try {
			frame.getCodeAccounts();
			} catch (IOException e1) {
			frame.checkInforField.setText("’Àªß∂¡»° ß∞‹£°£°£°");
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						WebMethods webMethods=new WebMethods(frame);
						webMethods.batchStart();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (JDOMException e) {
						e.printStackTrace();
					} catch (RowsExceededException e) {
						e.printStackTrace();
					} catch (BiffException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (WriteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					
				}
			}).start();
		}
		
	}
	
}
