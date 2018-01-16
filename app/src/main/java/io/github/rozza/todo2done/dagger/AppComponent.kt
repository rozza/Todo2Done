package io.github.rozza.todo2done.dagger

import dagger.Component
import io.github.rozza.todo2done.App
import io.github.rozza.todo2done.CompletedActivity
import io.github.rozza.todo2done.TodoCardViewActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(application: App)
    fun inject(todoCardViewActivity: TodoCardViewActivity)
    fun inject(completedActivity: CompletedActivity)
}