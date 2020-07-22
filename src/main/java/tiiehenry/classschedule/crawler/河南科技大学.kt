package tiiehenry.classschedule.crawler

import org.jsoup.Jsoup
import tiiehenry.classschedule.json.ClassInfo
import tiiehenry.classschedule.json.ClassSchedule
import tiiehenry.classschedule.json.ClassTime
import java.io.File
import java.nio.charset.Charset

class 河南科技大学(val file: File) : Crawler() {
    //    val url = "http://jxglxt3.haust.edu.cn/wsxk/stu_zxjg.aspx"
    val 显示教师职称 = false//true

    override fun start(): ClassSchedule {
//        val doc=Jsoup.connect(url)
//            .get()

        val doc = Jsoup.parse(
            file,
            "GB18030",
            "http://jxglxt1.haust.edu.cn"
        )
        val pageRpt = doc.getElementById("pageRpt")
        val tbody = pageRpt.child(0).child(0).child(0).child(2).child(0)
        val trList = tbody.children()
        val 课程 = mutableListOf<ClassInfo>()
        trList.forEach {
            if (it.className() != "T") {
                val nameText = it.child(1).child(0).text()
                val 名称 = nameText.substringAfter("]")
                val 编号 = nameText.substringBefore("]").substringAfter("[")
                val 学分 = it.child(2).text().toDouble()
                val typeText = it.child(3).text()
                val 类别 = typeText.substringBefore("/")
                val 必修 = typeText.substringAfter("/") == "必修"
                val 任课教师 = it.child(4).child(0).text().let { teacherText ->
                    if (显示教师职称) {
                        teacherText
                    } else {
                        teacherText.substringBefore("[")
                    }
                }
                val 上课班号 = it.child(5).text()
                val 上课班级名称 = it.child(6).text()
                val scheduleText = it.children().last().toString().substringAfter(">").substringBeforeLast("<br>")
                val scheduleTextList = scheduleText.split("<br>")
                val 上课安排 = mutableListOf<ClassTime>()
                for (eachScheduleText in scheduleTextList) {
                    if (eachScheduleText.isEmpty()) {
                        continue
                    }
//                    println(eachScheduleText)
                    val splited=eachScheduleText.split("/")
                    if (splited.isEmpty())
                        continue

                    val 地点 =if (splited.size>1){splited[1]}
                                else ""

                    val timeText = splited[0]
//                    [1-12周]星期二[7-8节]/专业机房3（开元工科613，611，617）
                    val weekText=timeText.substringBefore("星期")
                    val weekDayAndSectionText=timeText.substringAfter("星期")
                    val 周次 = weekText.substringAfter("[").substringBefore("周")
                    val 星期 = week2Day(weekDayAndSectionText.first())
//                    println(星期)
                    val 节次 = weekDayAndSectionText.substringAfter("[").substringBefore("节")

                    上课安排.add(
                        ClassTime(
                            周次 = 周次.split(",").toMutableList(),
                            星期 = mutableListOf(星期),
                            节次 = mutableListOf(节次),
                            地点 = 地点,
                            教师 = 任课教师
                        )
                    )
                }
                课程.add(
                    ClassInfo(
                        名称 = 名称,
                        编号 = 编号,
                        学分 = 学分,
                        类别 = 类别,
                        必修 = 必修,
                        上课班号 = 上课班号,
                        上课班级名称 = 上课班级名称,
                        任课教师 = mutableListOf(任课教师),
                        上课安排 = 上课安排
                    )
                )
            }
        }
        val schedule = ClassSchedule(
            学校 = "河南科技大学",
            专业 = "",
            学期 = "",
            课程 = 课程
        )
        return schedule
    }

    fun week2Day(day: Char): Int {
        return when (day) {
            '一' -> 1
            '二' -> 2
            '三' -> 3
            '四' -> 4
            '五' -> 5
            '六' -> 6
            '日' -> 7
            else -> 7
        }
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
//            launch(MainApplication::class.java)
            //保存的文件夹内的html文件,文件名是一样的,改前面的路径就行
            河南科技大学(File("C:\\Users\\AnyWin\\Desktop\\河南科技大学教务网站 [网上选课--正选结果]_files", "stu_zxjg_rpt.html")).start()
        }
    }


}