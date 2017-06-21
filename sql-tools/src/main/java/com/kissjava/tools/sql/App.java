package com.kissjava.tools.sql;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import com.kissjava.tools.sql.ui.AbstractUI;
import com.kissjava.tools.sql.ui.AllDumpUI;
import com.kissjava.tools.sql.utils.DBUtils;

/**
 * Hello world!
 *
 */
public class App extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8528265303119541504L;

	private static final String ALL_DUMP = "ALL_DUMP";
	public static final String NOT_DUMP_TABLES = "not-dump-tables";
	private CardLayout cardLayout;
	private JPanel centerPanel;
	private AbstractUI currentUI;
	private Map<String, AbstractUI> uiMap = new HashMap<String, AbstractUI>();
	private JTextArea log;
	private Set<String> noDumpTables = new HashSet<String>();
	
	public static Font defaultFont = new Font("宋体",Font.PLAIN,14);
	private static String sample = "";
	public App() {
		init();
		this.setFont(defaultFont);
		loadPropertis();
		initTestData();
	}
	
	private void initTestData() {
		userText.setText("root");
		passwordText.setText("XXXXXXXXXXX");
		urlText.setText("jdbc:mysql://XXXXXXXXXXXX");
	}
	private void loadPropertis(){
		new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				List<String> lines =  readFile("/app.properties");
				for(int index=0,size=lines.size(); index<size; index++){
					String line = lines.get(index);
					if(line!=null && line.startsWith(NOT_DUMP_TABLES)){
						addNotDump(line);
					}
				}
				sample = readFile2("/samp.html");
				return null;
			}

		}.execute();
	}
	
	private void addNotDump(String line){
		String[] sps = line.split("=");
		if(sps==null || sps.length!=2){
			return;
		}
		String[] notDumps = sps[1].split(",");
		for(int index=0,size=notDumps.length; index<size; index++){
			noDumpTables.add(notDumps[index]);
		}
	}
	private void init() {
		setLayout(new BorderLayout());

		add(topPanel(), BorderLayout.NORTH);

		centerPanel = new JPanel();
		cardLayout = new CardLayout();
		centerPanel.setLayout(cardLayout);

		addUI(new AllDumpUI(this), ALL_DUMP);

		final JSplitPane splitPane = new JSplitPane();
		HierarchyListener hierarchyListener = new HierarchyListener() {
			public void hierarchyChanged(HierarchyEvent e) {
				long flags = e.getChangeFlags();
				if ((flags & HierarchyEvent.SHOWING_CHANGED) == HierarchyEvent.SHOWING_CHANGED) {
					splitPane.setDividerLocation(.75);
				}
			}
		};
		splitPane.addHierarchyListener(hierarchyListener);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setLeftComponent(centerPanel);
		log = new JTextArea();
		log.setFont(defaultFont);
		splitPane.setRightComponent(new JScrollPane(log));

		add(splitPane, BorderLayout.CENTER);

		this.setMinimumSize(new Dimension(1024, 700));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("OpenAPI数据迁移工具--严禁上生产");
		this.setVisible(true);
	}

	private void addUI(AbstractUI uim, String name) {
		if (currentUI == null) {
			currentUI = uim;
		}
		centerPanel.add(uim, name);
		uiMap.put(name, uim);
	}

	private JTextField userText;
	private JTextField passwordText;
	private JTextField urlText;

	private JCheckBox tokenCheckBox;

	private JPanel topPanel() {
		JPanel topPanel = new JPanel();

		BoxLayout box = new BoxLayout(topPanel, BoxLayout.Y_AXIS);
		topPanel.setLayout(box);

		JToolBar toolBar = new JToolBar();
		JButton btn = getBtn("全量导出");
		btn.addActionListener(new JToolBarActionListener(ALL_DUMP));
		toolBar.add(btn);
		//////////////
		toolBar.addSeparator();
		JButton sampBtn = getBtn("规则样例");
		sampBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showSamp();
			}
		});
		toolBar.add(sampBtn);
		topPanel.add(toolBar);

		//////////////////
		JPanel sqlPanel = new JPanel();
		sqlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		sqlPanel.add(getLabel("用户名:"));
		userText = new JTextField();
		userText.setColumns(10);
		sqlPanel.add(userText);
		sqlPanel.add(getLabel("密码:"));
		passwordText = new JTextField();
		passwordText.setColumns(10);
		sqlPanel.add(passwordText);

		sqlPanel.add(getLabel("URL:"));
		urlText = new JTextField();
		urlText.setColumns(50);
		sqlPanel.add(urlText);
		topPanel.add(sqlPanel);
		////
		JPanel tokenPanel = new JPanel();
		tokenPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		// tokenPanel.add(new JLabel("是否重置token"));
		tokenCheckBox = new JCheckBox("是否重置token");
		tokenCheckBox.setFont(defaultFont);
		tokenPanel.add(tokenCheckBox);

		JButton initBtn = getBtn("初始化");
		initBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doInit();
			}
		});
		tokenPanel.add(initBtn);

		JButton dumpBtn = getBtn("导出");
		dumpBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doDump();
			}
		});
		tokenPanel.add(dumpBtn);

		topPanel.add(tokenPanel);
		//////////////

		// add(topPanel,BorderLayout.NORTH);
		return topPanel;
	}
	private JLabel getLabel(String text){
		JLabel label = new JLabel(text);
		label.setFont(defaultFont);
		return label;
	}
	private JButton getBtn(String text){
		JButton btn = new JButton(text);
		btn.setFont(defaultFont);
		return btn;
	}
	private SampleDialog dialog;
	private void showSamp(){
		//JOptionPane.showMessageDialog( this, sample);
		if(dialog==null){
			dialog = new SampleDialog(this, sample);
		}
		dialog.setVisible(true);
	}
	private void doInit() {
		if (!checkBdInit()) {
			return;
		}
		new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				log("正在获取数据库表信息，请稍后.....");
				currentUI.dbInit();
				log("获取数据库表信息，结束.....");
				return null;
			}

		}.execute();

	}

	private void doDump() {
		if (!checkBdInit()) {
			return;
		}
		new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				log.setText("");
				currentUI.dump();
				return null;
			}

		}.execute();
	}

	private boolean notEmpty(String str) {
		return str != null && !str.trim().equals("");
	}

	private boolean checkBdInit() {
		if (!notEmpty(getUser()) || !notEmpty(getPwd()) || !notEmpty(getUrl())) {
			log("输入相关用户名、密码等信息");
			return false;
		}
		DBUtils.url = getUrl();
		DBUtils.user = getUser();
		DBUtils.password = getPwd();

		return true;
	}

	public String getUser() {
		return userText.getText();
	}

	public String getPwd() {
		return passwordText.getText();
	}

	public String getUrl() {
		return urlText.getText();
	}

	public boolean isTokenCheck() {
		return tokenCheckBox.isSelected();
	}

	public void log(String msg) {
		log.append(msg + "\n");
		log.setCaretPosition(log.getDocument().getLength());
	}
	
	public Set<String> notDumpTables(){
		return noDumpTables;
	}

	class JToolBarActionListener implements ActionListener {
		private String actionName;

		public JToolBarActionListener(String actionName) {
			this.actionName = actionName;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			cardLayout.show(centerPanel, actionName);
			currentUI = uiMap.get(actionName);
		}
	}

	public static List<String> readFile(String path) throws IOException {
		List<String> lines = new ArrayList<String>();
		BufferedReader input = null;
		try {
			InputStream ips = App.class.getResourceAsStream(path);
			input = new BufferedReader(new InputStreamReader(ips));
			String str = null;
			while ((str = input.readLine()) != null) {
				lines.add(str);
			}
		} finally {
			if (input != null) {
				input.close();
			}
		}
		return lines;
	}
	public static String readFile2(String path) throws IOException {
		StringBuilder sb = new StringBuilder(300);
		BufferedReader input = null;
		try {
			InputStream ips = App.class.getResourceAsStream(path);
			input = new BufferedReader(new InputStreamReader(ips));
			String str = null;
			while ((str = input.readLine()) != null) {
				sb.append(str);
			}
		} finally {
			if (input != null) {
				input.close();
			}
		}
		return sb.toString();
	}
	class SampleDialog extends JDialog{
		private JEditorPane editorPane;
		private String msg;
		public SampleDialog(JFrame parent, String msg){
			super(parent);
			this.msg = msg;
			init();
		}
		
		void init(){
			setLayout(new BorderLayout());
			
			editorPane = new JEditorPane();
			editorPane.setEditable(false);
			editorPane.setContentType("text/html");
			editorPane.setText(msg);
			add(new JScrollPane(editorPane), BorderLayout.CENTER);
			setMinimumSize(new Dimension(600, 500));
			setTitle("规则用例");
			setLocationRelativeTo(null);
			//this.setVisible(true);
		}
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");//
				} catch (Exception e) {
					e.printStackTrace();
				}
				App app = new App();
				app.setLocationRelativeTo(null);
			}
		});
	}

}
