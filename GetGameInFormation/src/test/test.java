/**
 * 
 */
package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author wjh
 *
 */
public class test {

	/**
	 * @param args
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws JSONException, IOException {
		StringBuffer stringBuffer = new StringBuffer();
		String line = null ;
		BufferedReader br = new BufferedReader(new FileReader(new File("config\\youxiangValidate.txt")));
		while( (line = br.readLine())!= null ){
			stringBuffer.append(line);
		} 
		JSONArray emailVas= new JSONArray(stringBuffer.toString());
		
//		JSONObject gameEmail=new JSONObject();
//		gameEmail.append("name","wjh");
		FileWriter fw=new FileWriter(new File("config\\youxiangValidate.txt"));
		fw.write("");
		emailVas.getJSONObject(0).append("name","eee");
		fw.write(emailVas.toString());
		fw.close();
	}

}
