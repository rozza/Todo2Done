package io.github.rozza.todo2done.dagger

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import io.github.rozza.todo2done.room.TodoDatabase
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun providesAppDatabase(context: Context): TodoDatabase =
            Room.databaseBuilder(context, TodoDatabase::class.java, "todos.db").build()

    @Provides
    fun providesTodoDao(todoDatabase: TodoDatabase) = todoDatabase.todoDao()
}