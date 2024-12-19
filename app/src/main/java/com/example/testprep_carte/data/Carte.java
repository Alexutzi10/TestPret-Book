package com.example.testprep_carte.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = "carti")
public class Carte {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private int nrPages;
    private Date date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNrPages() {
        return nrPages;
    }

    public void setNrPages(int nrPages) {
        this.nrPages = nrPages;
    }

    @Ignore
    public Carte(String name, int nrPages, Date date) {
        this.name = name;
        this.nrPages = nrPages;
        this.date = date;
    }

    public Carte(long id, String name, Date date, int nrPages) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.nrPages = nrPages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carte carte = (Carte) o;
        return nrPages == carte.nrPages && Objects.equals(name, carte.name) && Objects.equals(date, carte.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, nrPages, date);
    }

    @Override
    public String toString() {
        return "Carte{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nrPages=" + nrPages +
                ", date=" + date +
                '}';
    }
}
