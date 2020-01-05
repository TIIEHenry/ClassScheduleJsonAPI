package tiiehenry.classschedule.json

import com.google.gson.Gson
import java.io.File

fun main() {
    val text = File("C:\\Users\\AnyWin\\Desktop\\json.txt").readText()
    val gson = Gson()
    val cs = gson.fromJson<ClassSchedule>(text, ClassSchedule::class.java)
    println(cs)
    println(gson.toJson(cs))
}