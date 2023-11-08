package com.example.mulaimaneh.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mulaimaneh.model.ResponseUser

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: ResponseUser.Item)

    @Query("SELECT * FROM User")
    fun loadAll(): LiveData<MutableList<ResponseUser.Item>>

    @Query("SELECT * FROM User WHERE id LIKE :id LIMIT 1")
    fun findById(id: Int): ResponseUser.Item

    @Query("SELECT * FROM User WHERE login = :username LIMIT 1")
    fun findByUsername(username: String): ResponseUser.Item?

    @Delete
    fun delete(user: ResponseUser.Item)
}