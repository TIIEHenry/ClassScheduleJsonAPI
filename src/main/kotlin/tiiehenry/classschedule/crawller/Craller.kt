package tiiehenry.classschedule.crawller

import tiiehenry.classschedule.json.ClassSchedule

abstract class Craller {
    abstract fun start(): ClassSchedule

    companion object {
        val carllerList = mutableListOf<Craller>()
    }
}