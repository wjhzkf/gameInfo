/**
 * 
 */
package selenium;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author wjh
 *��ȡҳ��Ԫ�� 
 */
public class getAllXpath {
	public HashMap<String,String> elmentXpath=null;
		
	/**
	 * ��ȡ����xpath
	 * @throws IOException
	 */
	public getAllXpath() throws IOException {
		elmentXpath=new HashMap<>();
		BufferedReader br=new BufferedReader(new FileReader(new File("config/AllPath.txt")));
		String string=null;
		while ((string=br.readLine())!=null) {
			String strings[]=string.split("-");
			elmentXpath.put(strings[0],strings[1]);
		}
	}
	
}
