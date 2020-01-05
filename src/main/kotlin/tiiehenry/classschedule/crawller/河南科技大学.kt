package tiiehenry.classschedule.crawller

import org.jsoup.Jsoup
import tiiehenry.classschedule.json.ClassInfo
import tiiehenry.classschedule.json.ClassSchedule
import tiiehenry.classschedule.json.ClassTime
import java.io.File

class 河南科技大学(val file: File) : Craller() {
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
                scheduleTextList.forEach { eachScheduleText ->
                    val timeText = eachScheduleText.substringBefore("/")
                    val 星期 = getWeekDay(timeText.substringBefore(" "))
                    val timeTextWithoutWeek = timeText.substringAfter(" ")
                    val 节次 = timeTextWithoutWeek.substringBefore("节")
                    val 周次 = timeTextWithoutWeek.substringAfter("[").substringBefore("周")
                    val 地点 = if (eachScheduleText.contains("/")) eachScheduleText.substringAfter("/") else ""
                    上课安排.add(
                        ClassTime(
                            周次 = mutableListOf(周次),
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
            专业 = "计算机科学与技术",
            学期 = "大二下",
            课程 = 课程
        )
        println(schedule)
        return schedule
    }

    fun getWeekDay(day: String): Int {
        return when (day) {
            "一" -> 1
            "二" -> 2
            "三" -> 3
            "四" -> 4
            "五" -> 5
            "六" -> 6
            "日" -> 7
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