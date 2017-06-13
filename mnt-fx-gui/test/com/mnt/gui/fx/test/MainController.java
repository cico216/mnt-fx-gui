package com.mnt.gui.fx.test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.mnt.gui.fx.base.BaseController;
import com.mnt.gui.fx.controls.dialog.DialogFactory;
import com.mnt.gui.fx.controls.searchbox.SearchBoxFactory;
import com.mnt.gui.fx.controls.searchbox.action.SelectAction;
import com.mnt.gui.fx.table.TabelCellFactory;
import com.mnt.gui.fx.table.TableViewSupport;
import com.mnt.gui.fx.test.data.TestTableData;
import com.mnt.gui.fx.view.anno.MainView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * 
 * <p>
 * 测试基础界面
 * </p>
 * @author    mnt.cico
 * @version  2016年5月14日 下午11:18:30 mnt.cico .
 * @since   FX8.0
 */
@MainView(appName = "测试界面" )
public class MainController extends BaseController {
	

    @FXML
    private TableView<TestTableData> tvwTest;
    
    //表格支持
    private TableViewSupport<TestTableData> tvwSupport;
    
    @FXML
    private TextField txtTest;
    
    private List<String> testStrs = new ArrayList<>();
    
	@Override
	public void init() {
		tvwSupport = TabelCellFactory.createTableSupport(true, tvwTest, TestTableData.class);
		TestTableData s = new TestTableData();
		s.setButton("test1");
		s.setLabel("test1");
		s.setChoice(false);
		tvwSupport.addItem(s);
		s = new TestTableData();
		s.setButton("test2");
		s.setLabel("test2");
		s.setChoice(true);
		tvwSupport.addItem(s);
		s.setLabel("newlabel");
		
		testStrs.add("test1");
		testStrs.add("test2");
		testStrs.add("test3");
		testStrs.add("测试");
		testStrs.add("1测试1");
		testStrs.add("我的");
		
		SearchBoxFactory.getInstance().<String>buildTextFiled(txtTest, testStrs, new SelectAction<String>() {
			@Override
			public void action(String t) {
				
				txtTest.setText(t);
			}
		}, new Predicate<String>() {
			@Override
			public boolean test(String t) {
				if(t.contains(txtTest.getText()))
				{
					return true;
				}
				return false;
			}
		});
	}
	
    @FXML
    void processTest(ActionEvent event) {
//    	DialogFactory.getInstance().showSuccessMsg("test", "info", null);
//    	DialogFactory.getInstance().showWindow("test", "info", null);
    	DialogFactory.getInstance().showConfirm("test", "info", ()->{
    		System.err.println("--");
    	});
    }
	
	@Override
	public void shutdown() {
		tvwSupport.shutdown();
	}
}
