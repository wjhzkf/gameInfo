/**
 * 
 */
package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * @author wjh
 *
 */
public class AccountCheckUI extends JFrame{

	JLabel checkLabel;
	public JTextField checkInforField;
	JButton checkBtn;
	JButton configBtn;
	JButton configEmail;
	/**
	 * 主面板
	 */
	public JPanel MainPanel;
	public JPanel panel;
	/**
	 * 所有充值者账户
	 */
	public Map<String,String> accounts =null;
	/**
	 * 所有充值者账户ID
	 */
	public List<String> accountIDs =null;
	/**
	 *邮箱
	 */
	public List<String> youxiang =null;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AccountCheckUI frame = new AccountCheckUI();
					frame.setLocation(200,400);
					frame.setResizable(false);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * @throws IOException 
	 * @throws HeadlessException
	 */
	public AccountCheckUI() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800,220); 
		MainPanel = new JPanel();
		MainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		MainPanel.setLayout(new BorderLayout(0, 0));
		setContentPane(MainPanel);
		
		panel= new JPanel();
		MainPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		checkLabel=new JLabel("信息状态");
		checkLabel.setBounds(20, 20, 60, 20);
		panel.add(checkLabel);
		
		checkInforField = new JTextField();
		checkInforField.setForeground(Color.RED);
		checkInforField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		checkInforField.setEditable(false);
		checkInforField.setColumns(10);
		checkInforField.setBounds(90,20,650,60);
		panel.add(checkInforField);
		
		checkBtn = new JButton("账户检查");
		checkBtn.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		checkBtn.setBounds(86, 110, 160, 48);
		panel.add(checkBtn);
		checkBtn.addActionListener(new MyListener(this));
		
		configBtn= new JButton("配置账户");
		configBtn.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		configBtn.setBounds(306,110, 160, 48);
		panel.add(configBtn);
		configBtn.addActionListener(new MyListener(this));
		
		configEmail= new JButton("配置邮箱");
		configEmail.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		configEmail.setBounds(536,110, 160, 48);
		panel.add(configEmail);
		configEmail.addActionListener(new MyListener(this));
		
	}
	/**
	 * 初始化充值用户accounts，充值用户ID:accountIDs
	 */
	public void getCodeAccounts() throws IOException{
		BufferedReader bf = null;
		
		try {
			bf = new BufferedReader(new FileReader(new File("config\\Accounts_Validate.txt")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bf.readLine();
		String temp;
		String defaultId = null;
		String defaultPassword = null;
		accounts=new HashMap<String,String>();
		accountIDs= new ArrayList<String>();
		
		while((temp=bf.readLine())!=null){
			defaultId = temp.split("-")[0];
			System.out.println(defaultId);
			defaultPassword = temp.split("-")[1];
			System.out.println(defaultPassword);
			accounts.put(defaultId,defaultPassword);
			accountIDs.add(defaultId);
		}
		
	}
	public void getYouXiang() throws IOException{
		BufferedReader bf = null;
		
		try {
			bf = new BufferedReader(new FileReader(new File("config\\youxiang.txt")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String temp;
		youxiang= new ArrayList<String>();
		while((temp=bf.readLine())!=null){
			youxiang.add(temp);
		}
		
	}
	
}
