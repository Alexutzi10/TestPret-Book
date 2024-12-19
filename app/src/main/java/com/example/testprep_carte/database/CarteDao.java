package com.example.testprep_carte.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.testprep_carte.data.Carte;

import java.util.List;

@Dao
public interface CarteDao {
    @Query("SELECT * FROM carti")
    List<Carte> getAll();

    @Insert
    List<Long> insertAll(List<Carte> carti);

    @Delete
    int delete(List<Carte> carti);
}
