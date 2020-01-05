package tiiehenry.classschedule.crawller

import tiiehenry.classschedule.json.ClassSchedule

open class Craller {
    open fun start(): ClassSchedule {
    }
    companion object{
        val carllerList= mutableListOf<Craller>()
    }
}