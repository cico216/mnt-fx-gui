package com.mnt.gui.fx.controls.file;

import java.io.File;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * file chooser control factory
 * <p>
 * 1.support file chooser
 * 2.support director chooser
 * 3.support file save
 * 4.support director save
 * </p>
 * 
 * @author 2014-5-14 mnt.cico
 *
 */
public final class FileChooserFacotry {

	/**
	 * file chooser dialog 
	 * 2014-5-16 mnt.cico
	 * @param stage
	 * @param displayType display name
	 * @param defaultPath default path by choose
	 * @param filterFile filter file suffix
	 * @return
	 */
	public static File chooserFileControl(Stage stage, String displayType, String defaultPath, String... filterFile) {
		FileChooser chooser = new FileChooser();
		if(null != defaultPath) {
			File file = new File(defaultPath); 
			if(file.isDirectory()) {
				chooser.setInitialDirectory(file);
			}
		}
		if(filterFile.length != 0) {
			chooser.getExtensionFilters().add(new ExtensionFilter(displayType, filterFile));
		}
		return chooser.showOpenDialog(stage);
	}
	
	/**
	 * 
	 * @param stage
	 * @param displaytype
	 * @return
	 */
	public static File chooserFileControl(Stage stage, String displaytype) {
		return chooserFileControl(stage, displaytype, null);
	}
	
	/**
	 * 
	 * @param stage
	 * @return
	 */
	public static File chooserFileControl(Stage stage) {
		return chooserFileControl(stage, "chooers file", null);
	}
	
	/**
	 * choice director and set default path
	 * 2014-5-18 mnt.cico
	 * @return
	 */
	public static File chooserDirectorControl(Stage stage, String defaultPath) {
		DirectoryChooser chooser = new DirectoryChooser();
		if(null != defaultPath) {
			File file = new File(defaultPath); 
			
			if(file.isDirectory()) {
				chooser.setInitialDirectory(file);
			}
		}
		return chooser.showDialog(stage);
	}
	
	/**
	 *  chooser the firector
	 * 2014-5-18 mnt.cico
	 * @param stage
	 * @return
	 */
	public static File chooserDirectorControl(Stage stage) {
		return chooserDirectorControl(stage, null);
	}
	
	/**
	 * get save file 
	 * 2014-7-22 mnt.cico
	 * @param stage
	 * @param defaultPath
	 * @return
	 */
	public static File saveFileInDirector(Stage stage, String defaultPath, String type, String... suffix) {
		FileChooser save = new FileChooser();
		if(suffix.length != 0) {
			save.getExtensionFilters().add(new ExtensionFilter(type, suffix));
		}
		if(null != defaultPath) {
			File deffile = new File(defaultPath); 
			if(deffile.isDirectory()) {
				save.setInitialDirectory(deffile);
			}
		}
		return save.showSaveDialog(stage);
	}
	
	/**
	 * get save file 
	 * 2014-7-22 mnt.cico
	 * @param stage
	 * @param defaultPath
	 * @return
	 */
	public static File saveFileInDirector(Stage stage, String type, String... suffix) {
		return saveFileInDirector(stage, null, type, suffix);
	}
}
