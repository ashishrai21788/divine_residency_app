package com.app.core.dagger.coredao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.app.core.dagger.coreentity.User


@Dao
interface UserDao{

    @Query("SELECT * FROM user_table")
     fun getAll(): List<User>

    @Query("SELECT * FROM user_table WHERE uid IN (:userIds)")
     fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user_table WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
     fun findByName(first: String, last: String): User

    @Insert
     fun insertAll(vararg users: User)

    @Delete
     fun delete(user: User)
}
