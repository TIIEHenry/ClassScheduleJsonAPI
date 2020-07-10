package tiiehenry.classschedule.crawler

import tiiehenry.classschedule.json.ClassSchedule

abstract class Crawler {
    abstract fun start(): ClassSchedule

    companion object {
        val crawlerList = mutableListOf<Crawler>()
    }
}