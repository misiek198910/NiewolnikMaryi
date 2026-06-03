package mivs.niewolnik_maryi;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import mivs.niewolnik_maryi.BuildConfig;

public class Activity_Traktat extends AppCompatActivity {

    LinearLayout bottom_toolbar;
    TextView toolbar_title;
    ImageButton button_back;
    private FrameLayout adContainerView;
    private AdView adView;
    Button btn1; Button btn2; Button btn3; Button btn4; Button btn5; Button btn6; Button btn7;
    ConstraintLayout main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_traktat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MobileAds.initialize(this, initializationStatus -> {});

        adContainerView = findViewById(R.id.ad_view_container);
        loadAdaptiveBanner();

        main = findViewById((R.id.main));
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        btn5 = findViewById(R.id.button5);
        btn6 = findViewById(R.id.button6);
        btn7 = findViewById(R.id.button7);

        Class_App_theme app_theme = new Class_App_theme();

        int color1 = app_theme.Background_theme_change(getApplicationContext());
        main.setBackgroundResource(color1);

        int color = app_theme.Toolbar_theme_change(getApplicationContext());
        button_back = findViewById(R.id.button_back);
        button_back.setBackgroundResource(color1);
        bottom_toolbar = findViewById(R.id.bottom_toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.main_button1);
        bottom_toolbar.setBackgroundColor(color);
        button_back.setOnClickListener( v -> finish());

        app_theme.Button_theme_change(getApplicationContext(), btn1);
        app_theme.Button_theme_change(getApplicationContext(), btn2);
        app_theme.Button_theme_change(getApplicationContext(), btn3);
        app_theme.Button_theme_change(getApplicationContext(), btn4);
        app_theme.Button_theme_change(getApplicationContext(), btn5);
        app_theme.Button_theme_change(getApplicationContext(), btn6);
        app_theme.Button_theme_change(getApplicationContext(), btn7);

        btn1.setOnClickListener(v ->
        {
            String file = "wprowadzenie";
            String title = getString(R.string.traktat_button1);

            Intent myIntent = new Intent(this, Activity_Show_Text.class);
            myIntent.putExtra("file", file);
            myIntent.putExtra("title", title);
            this.startActivity(myIntent);
        });
        btn2.setOnClickListener(v ->
        {
            Intent myIntent = new Intent(this, Activity_Traktat_dni.class);
            myIntent.putExtra("idx", 0);
            this.startActivity(myIntent);
        });
        btn3.setOnClickListener(v ->
        {
            Intent myIntent = new Intent(this, Activity_Traktat_dni.class);
            myIntent.putExtra("idx", 1);
            this.startActivity(myIntent);
        });
        btn4.setOnClickListener(v ->
        {
            Intent myIntent = new Intent(this, Activity_Traktat_dni.class);
            myIntent.putExtra("idx", 2);
            this.startActivity(myIntent);
        });
        btn5.setOnClickListener(v ->
        {
            Intent myIntent = new Intent(this, Activity_Traktat_dni.class);
            myIntent.putExtra("idx", 3);
            this.startActivity(myIntent);
        });
        btn6.setOnClickListener(v ->
        {
            String file = "Akt";
            String title = getString(R.string.traktat_button6);

            Intent myIntent = new Intent(this, Activity_Show_Text.class);
            myIntent.putExtra("file", file);
            myIntent.putExtra("title", title);
            this.startActivity(myIntent);
        });
        btn7.setOnClickListener(v ->
        {
            String file = "terminy";
            String title = getString(R.string.traktat_button7);

            Intent myIntent = new Intent(this, Activity_Show_Text.class);
            myIntent.putExtra("file", file);
            myIntent.putExtra("title", title);
            this.startActivity(myIntent);
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