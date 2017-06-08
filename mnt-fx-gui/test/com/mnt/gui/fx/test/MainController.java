package com.mnt.gui.fx.test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import com.mnt.gui.fx.base.BaseController;
import com.mnt.gui.fx.controls.dialog.DialogFactory;
import com.mnt.gui.fx.table.TabelCellFactory;
import com.mnt.gui.fx.table.TableViewSupport;
import com.mnt.gui.fx.test.data.TestTableData;
import com.mnt.gui.fx.view.anno.MainView;

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
