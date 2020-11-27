package fr.dev.majdi.netgem.giphy

import android.app.Application
import fr.dev.majdi.netgem.giphy.module.mainModule
import org.koin.android.ext.android.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        //Init Koin
        startKoin(
            this,
            listOf(mainModule),
            loadPropertiesFromFile = true
        )
    }

}