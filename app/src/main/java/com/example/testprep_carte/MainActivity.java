package com.example.testprep_carte;

import static android.opengl.ETC1.isValid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testprep_carte.data.Carte;
import com.example.testprep_carte.database.CarteService;
import com.example.testprep_carte.network.AsyncTaskRunner;
import com.example.testprep_carte.network.Callback;
import com.example.testprep_carte.network.CarteParser;
import com.example.testprep_carte.network.HttpManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private static final String url = "https://api.npoint.io/efcea1d46c36fa555d04";
    public static final String SHARED_PREFERENCES = "shared_preferences";
    public static final String SPINNER_KEY = "spinner_key";
    public static final String ET_KEY = "et_key";
    private FloatingActionButton fab;
    private TextView tv;
    private Spinner spinner;
    private EditText et;
    private Button bttn;
    private AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
    private List<Carte> carti = new ArrayList<>();
    private CarteService carteService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initComponents();

        fab.setOnClickListener(click -> {
            Callable<String> httpManager = new HttpManager(url);
            Callback<String> httpManagerCallback = onMainThreadOperation();
            asyncTaskRunner.executeAsync(httpManager, httpManagerCallback);
        });

        bttn.setOnClickListener(click -> {
            if (!isValid()) {
                Toast.makeText(getApplicationContext(), R.string.page_number_not_inserted, Toast.LENGTH_SHORT).show();
            }

            String comp = spinner.getSelectedItem().toString();
            int pageNr = Integer.parseInt(et.getText().toString());

            List<Carte> deleted;
            if (comp.equals("are valoarea mai mare decat")) {
                deleted = carti.stream().filter(c -> c.getNrPages() > pageNr).collect(Collectors.toList());
            } else if (comp.equals("are valoarea mai mica decat")) {
                deleted = carti.stream().filter(c -> c.getNrPages() < pageNr).collect(Collectors.toList());
            } else {
                deleted = carti.stream().filter(c -> c.getNrPages() == pageNr).collect(Collectors.toList());
            }

            carteService.delete(deleted, callbackDelete(pageNr, comp));

        });
    }

    private Callback<List<Carte>> callbackDelete(int pageNr, String comp) {
        return result -> {
            if (result != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(SPINNER_KEY, comp);
                editor.putInt(ET_KEY, pageNr);
                editor.apply();
                carti.removeAll(result);
                Toast.makeText(getApplicationContext(), R.string.deleted, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private boolean isValid() {
        if (et.getText() == null || et.getText().toString().isBlank()) {
            return false;
        }
        return true;
    }

    private Callback<List<Carte>> callbackGetAll() {
        return result -> {
          if (result != null) {
              carti.clear();
              carti.addAll(result);
              Toast.makeText(getApplicationContext(), R.string.loaded, Toast.LENGTH_SHORT).show();
          }
        };
    }

    private Callback<String> onMainThreadOperation() {
        return result -> {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            List<Carte> cartiParsate = CarteParser.getFromJSON(result);
            List<Carte> cartiToInsert = cartiParsate.stream().filter(c -> !carti.contains(c)).collect(Collectors.toList());

            if (!cartiToInsert.isEmpty()) {
                carteService.insertAll(cartiToInsert, callbackInsert());
            }
        };
    }

    private Callback<List<Carte>> callbackInsert() {
        return result -> {
            if (result != null) {
                carti.addAll(result);
                Toast.makeText(getApplicationContext(), R.string.inserted, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void initComponents() {
        fab = findViewById(R.id.surugiu_george_alexandru_fab);
        tv = findViewById(R.id.surugiu_george_alexandru_tv);
        spinner = findViewById(R.id.surugiu_george_alexandru_spinner);
        et = findViewById(R.id.surugiu_george_alexandru_et);
        bttn = findViewById(R.id.surugiu_george_alexandru_bttn);

        carteService = new CarteService(getApplicationContext());

        sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String comp = sharedPreferences.getString(SPINNER_KEY, "");
        int pageNr = sharedPreferences.getInt(ET_KEY, 0);

        if (comp.equals("are valoarea mai mare decat")) {
            spinner.setSelection(0);
        } else if (comp.equals("are valoarea mai mica decat")) {
            spinner.setSelection(1);
        } else {
            spinner.setSelection(2);
        }

        if (pageNr != 0) {
            et.setText(String.valueOf(pageNr));
        }

        carteService.getAll(callbackGetAll());
    }
}