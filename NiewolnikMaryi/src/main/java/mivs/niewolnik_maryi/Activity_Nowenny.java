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
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Arrays;
import java.util.List;

import mivs.niewolnik_maryi.BuildConfig;

public class Activity_Nowenny extends AppCompatActivity {
    private FrameLayout adContainerView;
    private AdView adView;
    LinearLayout bottom_toolbar;
    TextView toolbar_title;
    ImageButton button_back;
    ConstraintLayout main;
    ListView listview1;

    String[] listArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nowenny);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MobileAds.initialize(this, initializationStatus -> {});

        adContainerView = findViewById(R.id.ad_view_container);
        loadAdaptiveBanner();

        main = findViewById(R.id.main);

        Class_App_theme app_theme = new Class_App_theme();

        int color1 = app_theme.Background_theme_change(getApplicationContext());
        main.setBackgroundResource(color1);

        int color = app_theme.Toolbar_theme_change(getApplicationContext());
        button_back = findViewById(R.id.button_back);
        button_back.setBackgroundResource(color1);
        bottom_toolbar = findViewById(R.id.bottom_toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.main_button3);
        bottom_toolbar.setBackgroundColor(color);
        button_back.setOnClickListener( v -> {
            finish();
            Intent myIntent = new Intent(this, Activity_Main.class);
            this.startActivity(myIntent);
        });

        listview1 = findViewById(R.id.listView1);

        listview1.setOnItemClickListener((parent, view, position, id) -> {
            Intent myIntent = new Intent(Activity_Nowenny.this, Activity_Traktat_dni.class);
            myIntent.putExtra("idx", 4);
            Activity_Nowenny.this.startActivity(myIntent);
        });

        listArray = getResources().getStringArray(R.array.listView2_items1);

        List<String> adapterList = Arrays.asList(listArray);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(),
                R.layout.listview_item, adapterList);

        listview1.setAdapter(adapter);

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