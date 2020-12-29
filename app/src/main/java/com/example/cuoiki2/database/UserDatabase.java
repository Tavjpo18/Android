package com.example.cuoiki2.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.cuoiki2.User;

@Database(entities = {User.class},version = 1)
public abstract class UserDatabase extends RoomDatabase {
    private static String DATABASE_NAME ="user.db";
    private static  UserDatabase intsance;

    public static synchronized UserDatabase getInstance(Context context){
      if (intsance == null){
          intsance = Room.databaseBuilder(context.getApplicationContext(),UserDatabase.class,DATABASE_NAME)
                  .allowMainThreadQueries()
                  .build();
      }

      return intsance;
    }

    public  abstract  UserDAO userDAO();
}
