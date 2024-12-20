package com.mensinator.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class App : Application() {

    // Koin dependency injection definitions
    private val appModule = module {
        singleOf(::PeriodDatabaseHelper) { bind<IPeriodDatabaseHelper>() }
        singleOf(::CalculationsHelper) { bind<ICalculationsHelper>() }
        singleOf(::OvulationPrediction) { bind<IOvulationPrediction>() }
        singleOf(::PeriodPrediction) { bind<IPeriodPrediction>() }
        singleOf(::ExportImport) { bind<IExportImport>() }
        singleOf(::NotificationScheduler) { bind<INotificationScheduler>() }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }
}