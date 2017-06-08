package com.mnt.gui.fx.test._3d;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import com.mnt.gui.fx.base.Base3DController;

/**
 * <p>
 *  3d controller
 * </p>
 * @author    mnt.cico
 * @version  2016年5月22日 下午2:43:10 mnt.cico .
 * @since   FX8.0
 */
public class Main3dController extends Base3DController {

	
    @FXML
    private AnchorPane acPane;

    @FXML
    private PerspectiveCamera perCamera;
    
    @FXML
    private Box boxTest;

    @FXML
    private VBox vbParent;
    
    @FXML
    private MeshView meshTest;
	
    @Override
	public void init()
	{
    	boxTest.setMaterial(new PhongMaterial(Color.YELLOW));
    	meshTest.setMesh(new TriangleMesh());
    	meshTest.setMaterial(new PhongMaterial(Color.YELLOW));
//		acPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
//
//			@Override
//			public void handle(MouseEvent event)
//			{
////				perCamera.setLayoutX(event.getX());
////				perCamera.setLayoutY(event.getY());
//				System.err.println(event.getX() + " - " + event.getY());
//				System.err.println("camera x = " + perCamera.getLayoutX() + ", y = " + perCamera.getLayoutY());
//			}
//		});
	}
    
    private static int moveSpeed = 10;
    
    @Override
    public void afterInit()
    {
    	getMainStage().getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event)
			{
				if(!event.isAltDown())
				{
					if(event.getCode() == KeyCode.UP)
					{
						perCamera.setLayoutY(perCamera.getLayoutY() + moveSpeed);
					}
					else if(event.getCode() == KeyCode.DOWN)
					{
						perCamera.setLayoutY(perCamera.getLayoutY() - moveSpeed);
					}
					else if(event.getCode() == KeyCode.LEFT)
					{
						perCamera.setLayoutX(perCamera.getLayoutX() + moveSpeed);
					}
					else if(event.getCode() == KeyCode.RIGHT)
					{
						perCamera.setLayoutX(perCamera.getLayoutX() - moveSpeed);
					}
				}
				else 
				{
					if(event.getCode() == KeyCode.UP)
					{
						perCamera.setRotationAxis(new Point3D(0, 1, 0));
						perCamera.setRotate(moveSpeed + perCamera.getRotate());
					}
					else if(event.getCode() == KeyCode.DOWN)
					{
						perCamera.setRotationAxis(new Point3D(0, 1, 0));
						perCamera.setRotate(perCamera.getRotate() - moveSpeed);
					}
					else if(event.getCode() == KeyCode.LEFT)
					{
						perCamera.setRotationAxis(new Point3D(1, 0, 0));
						perCamera.setRotate(moveSpeed + perCamera.getRotate());
					}
					else if(event.getCode() == KeyCode.RIGHT)
					{
						perCamera.setRotationAxis(new Point3D(1, 0, 0));
						perCamera.setRotate(perCamera.getRotate() - moveSpeed);
					}
				}
			}
		});
    }
    
	
	@Override
	public Camera getCamera() {
		return perCamera;
	}

}
