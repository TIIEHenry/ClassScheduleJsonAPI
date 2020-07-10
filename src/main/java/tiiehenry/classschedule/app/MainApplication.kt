package tiiehenry.classschedule.app

import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Stage
import javafx.stage.StageStyle
import tiiehenry.classschedule.crawler.河南科技大学
import javax.swing.filechooser.FileSystemView
import java.io.File


class MainApplication : Application() {
    @FXML
    lateinit var button: Button
    @FXML
    lateinit var editText: TextField

    override fun start(stage: Stage) {

        stage.apply {
            title = "河南科技大学课表程序--问道"
//            icons.add(Image())
            initStyle(StageStyle.DECORATED)
            //正常显示 也是默认
            isMaximized = true
            opacity = 1.0


        }

        val root: Parent = FXMLLoader.load(javaClass.classLoader.getResource("main.fxml"))
        stage.scene = Scene(root, 1000.0, 600.0).apply {
        }
        stage.show()


        stage.setOnCloseRequest {
            Platform.exit()
        }

        button = root.lookup("#button") as Button
        editText = root.lookup("#editText") as TextField
        button.setOnMouseClicked {
            val desktopDir = FileSystemView.getFileSystemView().homeDirectory
            val cs = 河南科技大学(
                File(
                    desktopDir,
                    "stu_zxjg_rpt.html"
                )
            ).start()
            editText.text = cs.toString()
        }
//        OrderDishes().start(stage)
//        OrderDishes.main(arrayOf())
    }


    companion object {

        @JvmStatic
        fun main(args: Array<String>?) {
            launch(MainApplication::class.java)
        }
    }

}