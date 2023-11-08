package com.example.mulaimaneh.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mulaimaneh.model.ResponseUser

@Database(entities = [ResponseUser.Item::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun userDao(): UserDao
}