package com.kissjava.tools.sql.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import com.kissjava.tools.sql.App;
import com.kissjava.tools.sql.utils.DBDumpCallback;
import com.kissjava.tools.sql.utils.DBUtils;
import com.kissjava.tools.sql.utils.callbak.ReplaceCallBack;
import com.kissjava.tools.sql.utils.callbak.TokenReplaceCallBack;
import com.kissjava.tools.sql.vo.DumpRule;
import com.kissjava.tools.sql.vo.SelectVo;

public abstract class AbstractUI extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2204411418543185070L;
	protected App app;

	private Vector<SelectVo> allSelect = new Vector<SelectVo>();

	private JPanel selectList;
	private JTextArea ruleText;

	public AbstractUI(App app) {
		this.app = app;
		init();
	}

	protected void init() {
		setLayout(new BorderLayout());
		JComponent ctrPanel = createCtrPanel();
		add(ctrPanel);
	}
	
	/***
	 * 
	 */
	public void dump() {
		List<String> names = selectedTableName();
		if(names.isEmpty()){
			app.log("请选择需要导出的表....");
			return;
		}
		app.log(names.toString());
		//Map<String, DumpRule> ruleMap = ruleMap();
		List<DumpRule> drList = ruleList();
		FileWriter fw = null;
		
		try {
			String fileName = fileName();
			fw = writer(fileName);
			for(int index=0,size=names.size(); index<size; index++){
				String tbName = names.get(index);
				List<DBDumpCallback> callList = callList(tbName, drList);
				app.log("正在导出"+tbName+".......");
				String ss = DBUtils.instance().dump(tbName,callList.toArray(new DBDumpCallback[callList.size()]));
				if(ss!=null){
					fw.write(ss);
					fw.write("\n\n");
				}
			}
			app.log("导出文件至"+fileName);
		} catch (IOException e) {
			app.log("导出异常");
		}finally{
			if(fw!=null){
				try {
					fw.flush();
					fw.close();
				} catch (IOException e) {
					app.log("导出异常——finally");
				}
				
			}
		}
	}
	
	private FileWriter writer(String path) throws IOException{
		FileWriter fw = new FileWriter(path, false);  
		return fw;
	}
	private String fileName(){
		String path = App.class.getResource("/").getPath().substring(1);
		path += "sql-"+formatDate()+".sql";
		return path;
	}
	public static String formatDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return formatter.format(new Date());
	}

	private List<DBDumpCallback> callList(String tbName, List<DumpRule> drList){
		List<DBDumpCallback> callList = new ArrayList<DBDumpCallback>();
		if(tbName.equals("api_app") && app.isTokenCheck()){
			callList.add(new TokenReplaceCallBack("app_secret"));
			callList.add(new TokenReplaceCallBack("token"));
			callList.add(new TokenReplaceCallBack("sandbox_app_secret"));
			callList.add(new TokenReplaceCallBack("sandbox_token"));
		}
		
		if(drList.isEmpty()){
			return callList;
		}
		for(int index=0,size=drList.size(); index<size; index++){
			DumpRule dr = drList.get(index);
			if(find(dr.getTableNames(), tbName)==null){
				continue;
			}
			String[] colNames = dr.getColNames();
			if(colNames==null || colNames.length==0){
				continue;
			}
			for(int cindex=0,csize=colNames.length; cindex<csize; cindex++){
				ReplaceCallBack replaceBack = new ReplaceCallBack(colNames[cindex]);
				replaceBack.setRepalceMap(dr.getRuleMap());
				callList.add(replaceBack);
			}
		}
		
		return callList;
	}
	
	private String find(String[] arr, String find){
		if(arr!=null && arr.length>0){
			for(int tIndex=0,tSize=arr.length; tIndex<tSize; tIndex++){
				String tempTbName = arr[tIndex];
				if(find.equals(tempTbName.trim())){
					return find;
				}
			}
		}
		return null;
	}
	private DumpRule findDr(String tableName,List<DumpRule> drList){
		if(drList.isEmpty()){
			return null;
		}
		for(int index=0,size=drList.size(); index<size; index++){
			DumpRule dr = drList.get(index);
			String[] names = dr.getTableNames();
			
		}
		return null;
	}
	
	private List<String> selectedTableName(){
		List<String> names = new ArrayList<String>();
		Enumeration<SelectVo> enumer = allSelect.elements();
		while (enumer.hasMoreElements()) {
			SelectVo vo = enumer.nextElement();
			if(vo.isSelected()){
				names.add(vo.getName());
			}
		}
		return names;
	}
	public void dbInit() {
		selectList.removeAll();
		allSelect.removeAllElements();
		List<String> names = DBUtils.instance().tableNames();
		Set<String> notDumps = app.notDumpTables();
		for (int index = 0, size = names.size(); index < size; index++) {
			String name = names.get(index);
			if(!notDumps.contains(name)){
				addSelected(new SelectVo(name, false));
			}
		}
		selectList.updateUI();
	}

	private JComponent createCtrPanel() {
		final JSplitPane splitPane = new JSplitPane();
		HierarchyListener hierarchyListener = new HierarchyListener() {
			public void hierarchyChanged(HierarchyEvent e) {
				long flags = e.getChangeFlags();
				if ((flags & HierarchyEvent.SHOWING_CHANGED) == HierarchyEvent.SHOWING_CHANGED) {
					splitPane.setDividerLocation(.35);
				}
			}
		};
		splitPane.addHierarchyListener(hierarchyListener);
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerSize(1);
		JPanel selectPanel = createSelectPane();
		splitPane.setLeftComponent(selectPanel);

		JPanel rulPanel = createRulePane();
		splitPane.setRightComponent(rulPanel);

		return splitPane;
	}

	protected JPanel createRulePane() {
		JPanel rulPanel = new JPanel();
		rulPanel.setLayout(new BoxLayout(rulPanel, BoxLayout.Y_AXIS));
		//rulPanel.add(new JLabel("选择需要的表"));
		rulPanel.setBorder(createBorder("规则："));
		ruleText = new JTextArea();
		ruleText.setFont(new Font("宋体",Font.PLAIN,18));
		rulPanel.add(new JScrollPane(ruleText));
		return rulPanel;
	}
	private static String TB_COL_SPlit = "==>";
	private static String RULE_COL_SPlit = "-->";
	
	protected List<DumpRule> ruleList(){
		//Map<String, DumpRule> ruleMap = new HashMap<String,DumpRule>();
		List<DumpRule> drList = new ArrayList<DumpRule>();
		String lineStrs = ruleText.getText();
		try{
			String[] lines = lineStrs.split("\n");
			DumpRule currentDr = null;
			for(int index=0,size=lines.length; index<size; index++){
				String line=lines[index];
				if(line.indexOf(TB_COL_SPlit)!=-1){
					currentDr = addTbColRule(line);
					drList.add(currentDr);
				}else if(line.indexOf(RULE_COL_SPlit)!=-1){
					String[] kvs = line.split(RULE_COL_SPlit);
					if(kvs!=null && kvs.length==2){
						currentDr.addRule(kvs[0].trim(), kvs[1].trim());
					}
				}
			}
		}catch(Exception e){
			app.log("规则输入错误..");
		}
		return drList;
	}
	private DumpRule addTbColRule(String line){
		DumpRule dr = new DumpRule();
		String[] tbCols = line.split(TB_COL_SPlit);
		String[] tbNames = tbCols[0].split(",");
		
		String[] cols = tbCols[1].split(",");
		dr.setTableNames(tbNames);
		dr.setColNames(cols);
		return dr;
	}
	
	
	private TitledBorder createBorder(String title){
		TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(getBackground(), 1), title);
		Font font = new Font("宋体",Font.PLAIN,18);  
		border.setTitleFont(font);
		return border;
	}
	protected JPanel createSelectPane() {
		JPanel selectPanel = new JPanel();
		selectPanel.setBorder(createBorder("选择需要的表："));
		selectPanel.setLayout(new BoxLayout(selectPanel, BoxLayout.Y_AXIS));
		//selectPanel.add(new JLabel("选择需要的表"));

		selectList = new JPanel();
		selectList.setLayout(new BoxLayout(selectList, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(selectList);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		selectPanel.add(scrollPane);
		return selectPanel;
	}

	private SelectAction selectAction;

	private void addSelected(SelectVo vo) {
		if (selectAction == null) {
			selectAction = new SelectAction();
		}
		JCheckBox cb = (JCheckBox) selectList.add(new JCheckBox(vo.getName()));
		cb.setFont(new Font("宋体",Font.PLAIN,18));
		allSelect.add(vo);
		cb.setSelected(vo.isSelected());
		cb.addActionListener(selectAction);
	}

	private SelectVo find(String name) {
		Enumeration<SelectVo> enumer = allSelect.elements();
		while (enumer.hasMoreElements()) {
			SelectVo vo = enumer.nextElement();
			if (vo.getName().equals(name)) {
				return vo;
			}
		}
		return null;
	}

	class SelectAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			JCheckBox cb = (JCheckBox) e.getSource();
			SelectVo vo = find(cb.getText());
			if (vo != null) {
				vo.setSelected(cb.isSelected());
			}
		}
	}
}
