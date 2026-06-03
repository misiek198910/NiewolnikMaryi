package mivs.niewolnik_maryi;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Arrays;
import java.util.List;
import mivs.niewolnik_maryi.BuildConfig;

public class Activity_Modlitwy extends AppCompatActivity {

    LinearLayout bottom_toolbar;
    TextView toolbar_title;
    ImageButton button_back;
    public AdView mAdView;
    String[] listArray;
    ListView listView1;
    Spinner spinner1;
    ConstraintLayout main;
    String[] spinner1Array;
    ArrayAdapter<String> adapter;
    private FrameLayout adContainerView;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modlitwy);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MobileAds.initialize(this, initializationStatus -> {});

        adContainerView = findViewById(R.id.ad_view_container);
        loadAdaptiveBanner();

        listView1 = findViewById(R.id.listView1);
        spinner1 = findViewById(R.id.spinner1);
        main = findViewById(R.id.main);

        Class_App_theme app_theme = new Class_App_theme();

        int color1 = app_theme.Background_theme_change(getApplicationContext());
        main.setBackgroundResource(color1);

        int color = app_theme.Toolbar_theme_change(getApplicationContext());
        button_back = findViewById(R.id.button_back);
        button_back.setBackgroundResource(color1);
        bottom_toolbar = findViewById(R.id.bottom_toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.main_button2);
        bottom_toolbar.setBackgroundColor(color);
        button_back.setOnClickListener( v -> {
            finish();
            Intent myIntent = new Intent(this, Activity_Main.class);
            this.startActivity(myIntent);
        });

        spinner1Array = getResources().getStringArray(R.array.spinner4_items);

        adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_item, spinner1Array);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                int item = spinner1.getSelectedItemPosition();

                if (item == 0)
                {

                    listArray = getResources().getStringArray(R.array.listView1_items1);

                }
                if (item == 1)
                {
                    listArray = getResources().getStringArray(R.array.listView1_items2);
                }

                List<String> adapterList = Arrays.asList(listArray);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(),
                        R.layout.listview_item, adapterList);

                listView1.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        listView1.setOnItemClickListener((parent, view, position, id) -> {
            int spinneritem = spinner1.getSelectedItemPosition();

            String fileName = "Mo_" + spinneritem + "_" + position;

            Intent myIntent = new Intent(Activity_Modlitwy.this, Activity_Show_Text.class);
            myIntent.putExtra("file",fileName);
            myIntent.putExtra("title", "");
            myIntent.putExtra("Week", "");
            Activity_Modlitwy.this.startActivity(myIntent);
        });
    }

    private void loadAdaptiveBanner() {
        adView = new AdView(this);
        adView.setAdUnitId(BuildConfig.AD_BANNER_ID);

        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Pobranie parametrów wyświetlacza w celu określenia szerokości okna reklamy
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Zwrócenie zoptymalizowanego, adaptacyjnego rozmiaru bannera
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    @Override
    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}