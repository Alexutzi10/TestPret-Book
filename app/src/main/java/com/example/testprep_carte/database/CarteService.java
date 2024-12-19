package com.example.testprep_carte.database;

import android.content.Context;

import com.example.testprep_carte.data.Carte;
import com.example.testprep_carte.network.AsyncTaskRunner;
import com.example.testprep_carte.network.Callback;

import java.util.List;
import java.util.concurrent.Callable;

public class CarteService {
    private AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
    private CarteDao carteDao;

    public CarteService(Context context) {
        carteDao = DatabaseManager.getInstance(context).getCarteDao();
    }

    public void insertAll(List<Carte> list, Callback<List<Carte>> callback) {
        Callable<List<Carte>> callable = () -> {
            List<Long> ids = carteDao.insertAll(list);

            for (int i = 0; i < list.size(); i++) {
                list.get(i).setId(ids.get(i));
            }

            return list;
        };
        asyncTaskRunner.executeAsync(callable, callback);
    }

    public void getAll(Callback<List<Carte>> callback) {
        Callable<List<Carte>> callable = () -> {
          List<Carte> carti = carteDao.getAll();
          return carti;
        };
        asyncTaskRunner.executeAsync(callable, callback);
    }

    public void delete(List<Carte> carti, Callback<List<Carte>> callback) {
        Callable<List<Carte>> callable = () -> {
          int count = carteDao.delete(carti);
          if (count <= 0) {
              return null;
          }
          return carti;
        };
        asyncTaskRunner.executeAsync(callable, callback);
    }
}
