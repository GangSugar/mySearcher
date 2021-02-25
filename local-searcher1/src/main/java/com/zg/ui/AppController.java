package com.zg.ui;

import com.zg.model.FileMeta;
import com.zg.service.FileService;
import com.zg.task.FileScanner;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * ui控制器
 */
public class AppController implements Initializable {
    @FXML
    private GridPane rootPane;
    @FXML
    private Label srcDirectory;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<FileMeta> fileTable;
    @FXML
    public TableColumn<FileMeta, String> nameColumn;//名称
    @FXML
    public TableColumn<FileMeta, String> sizeColumn;//大小
    @FXML
    public TableColumn<FileMeta, String> lastModifiedColumn;//修改时间

    private final FileService fileService = new FileService();
    private final FileScanner fileScanner = new FileScanner();
    @FXML
    public void choose(MouseEvent mouseEvent) {
        DirectoryChooser chooser = new DirectoryChooser();
        File root = chooser.showDialog(rootPane.getScene().getWindow());
        if (root == null) {
            return;
        }
//定义一个线程
        Thread thread = new Thread(() -> {
            try {
                fileScanner.scan(root);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        // 会在 FXMLLoader.load 执行时，实例化好 AppController 后调用
//        StringProperty stringProperty = searchField.textProperty();
//        stringProperty.addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                System.out.println("old:|" + oldValue + "|");
//                System.out.println("new: |" + newValue + "|");
//
//                List<FileMeta> fileList = fileService.query(newValue.trim());
//                for (FileMeta file : fileList) {
//                    System.out.println(file);
//                }
//                System.out.println("===========================");
//            }
//        });
//    }
    //这个个方法实例化的时候会调用一次，之后直接就绑定事件了
@Override
public void initialize(URL location, ResourceBundle resources) {
    // 会在 FXMLLoader.load 执行时，实例化好 AppController 后调用
    StringProperty stringProperty = searchField.textProperty();
    stringProperty.addListener((observable, oldValue, newValue) -> {
        System.out.println("old:|" + oldValue + "|");
        System.out.println("new: |" + newValue + "|");

        List<FileMeta> fileList = fileService.query(newValue.trim());
//        for (FileMeta file : fileList) {
//            System.out.println(file);
//        }
//        System.out.println("===========================");

        //将查询结果显示出来
        Platform.runLater(() -> {
            fileTable.getItems().clear();//每次查询都要更新一下
            fileTable.getItems().addAll(fileList);
        });
    });
}
}
