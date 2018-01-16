package io.github.rozza.todo2done

import android.app.Application
import io.github.rozza.todo2done.dagger.AppComponent
import io.github.rozza.todo2done.dagger.AppModule
import io.github.rozza.todo2done.dagger.DaggerAppComponent
import io.github.rozza.todo2done.room.TodoDatabase
import javax.inject.Inject

class App : Application() {

    companion object {
        lateinit var component: AppComponent
    }

    @Inject
    lateinit var todoDatabase: TodoDatabase

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        component.inject(this)
    }
}