package com.example.cuoiki2.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cuoiki2.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    void insertUSer(User user);

    @Query("SELECT * FROM user")
    List<User> getListUser();

    @Query("SELECT * FROM user where username= :username")
    List<User> checkUser(String username);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("Select * from user where username like '%' || :name || '%'")
    List<User> searchUser(String name);
}
