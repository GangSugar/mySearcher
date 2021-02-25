package com.zg;

import com.zg.service.DBService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    public void start(Stage primaryStage) throws Exception {
        URL url = Main.class.getClassLoader().getResource("app.fxml");//在类下面获取resource下面的文件app.fxml
        if (url == null){//如果获取的url是空，意思就是app.fxml这个文件找不到
            throw new RuntimeException("app,fxml文件没有找到");//抛出异常
        }
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);//一个场景对象
/**
 * Stage：舞台，就桌面显示出来的那个大框
 * Scene：场景：就是大框里面的文件显示的那个小框，搜索的那个文本框这些，都是舞台上的场景
 */
        primaryStage.setTitle("本地文件搜索工具");//设置舞台的标题
        primaryStage.setWidth(1000);//设置舞台的大小
        primaryStage.setHeight(900);//设置高度

        primaryStage.setScene(scene);//将场景放到舞台里面
        primaryStage.show();//舞台的大幕拉开，将舞台显示出来
    }
    public static void main(String[] args) {
        DBService service = new DBService();//对数据库进行初始化
        service.init();

        launch(args);//启动
    }
}
