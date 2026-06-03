package mivs.niewolnik_maryi;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.os.LocaleListCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import mivs.niewolnik_maryi.BuildConfig;

public class Activity_Settings extends AppCompatActivity {
    ArrayAdapter<String> adapter1, adapter2;

    LinearLayout bottom_toolbar;
    TextView toolbar_title;
    ImageButton button_back;
    private FrameLayout adContainerView;
    private AdView adView;
    Button btn1, btn2, btn3, btn4, btn5;
    Spinner sp1, sp2;
    ConstraintLayout main;
    private boolean mSpinnerInitialized1 = true;
    private boolean mSpinnerInitialized2 = true;

    String[] spinner1Array;
    String[] spinner2Array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MobileAds.initialize(this, initializationStatus -> {});

        adContainerView = findViewById(R.id.ad_view_container);
        loadAdaptiveBanner();

        main = findViewById((R.id.main));
        sp1 = findViewById(R.id.spinner1);
        sp2 = findViewById(R.id.spinner2);
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        btn5 = findViewById(R.id.button5);

        Class_App_theme app_theme = new Class_App_theme();
        
        int color1 = app_theme.Background_theme_change(getApplicationContext());
        main.setBackgroundResource(color1);

        app_theme.Button_theme_change(getApplicationContext(), btn1);
        app_theme.Button_theme_change(getApplicationContext(), btn2);
        app_theme.Button_theme_change(getApplicationContext(), btn3);
        app_theme.Button_theme_change(getApplicationContext(), btn4);
        app_theme.Button_theme_change(getApplicationContext(), btn5);

        int color = app_theme.Toolbar_theme_change(getApplicationContext());
        button_back = findViewById(R.id.button_back);
        button_back.setBackgroundResource(color1);
        bottom_toolbar = findViewById(R.id.bottom_toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.main_button5);
        bottom_toolbar.setBackgroundColor(color);
        button_back.setOnClickListener( v -> {
            Intent myIntent = new Intent(Activity_Settings.this, Activity_Main.class);
            Activity_Settings.this.startActivity(myIntent);
            finish();
        });

        spinner1Array = getResources().getStringArray(R.array.spinner1_items);
        spinner2Array = getResources().getStringArray(R.array.spinner3_items);

        adapter1 = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, spinner1Array);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp1.setAdapter(adapter1);

        adapter2 = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_item, spinner2Array);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp2.setAdapter(adapter2);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    if (mSpinnerInitialized1) {
                        mSpinnerInitialized1 = false;
                    }
                    else
                    {
                        int item = sp1.getSelectedItemPosition();
                        Class_Prefs pref = new Class_Prefs();
                        pref.SavePrefInt(getApplicationContext(), "theme_data", item);

                        Class_App_theme app_theme = new Class_App_theme();

                        int color = app_theme.Background_theme_change(getApplicationContext());
                        main.setBackgroundResource(color);

                        app_theme.Button_theme_change(getApplicationContext(), btn1);
                        app_theme.Button_theme_change(getApplicationContext(), btn2);
                        app_theme.Button_theme_change(getApplicationContext(), btn3);
                        app_theme.Button_theme_change(getApplicationContext(), btn4);
                        app_theme.Button_theme_change(getApplicationContext(), btn5);

                        int tclr = app_theme.Toolbar_theme_change(getApplicationContext());
                        bottom_toolbar = findViewById(R.id.bottom_toolbar);
                        bottom_toolbar.setBackgroundColor(tclr);
                        button_back.setBackgroundResource(color);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (mSpinnerInitialized2) {
                        mSpinnerInitialized2 = false;
                    }
                    else
                    {
                        int item = sp2.getSelectedItemPosition();
                        Class_Prefs pref = new Class_Prefs();
                        String iso = "pl";

                        switch (item) {
                            case 0:
                                pref.SavePrefInt(getApplicationContext(), "language_data", 0);
                                iso = "Pl";
                                break;
                            case 1:
                                pref.SavePrefInt(getApplicationContext(), "language_data", 1);
                                iso = "En";
                                break;

                        }

                        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(iso);  // or use "xx-YY"
                        AppCompatDelegate.setApplicationLocales(appLocale);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        btn1.setOnClickListener(v ->
        {
            Intent myIntent = new Intent(Activity_Settings.this, Activity_Information.class);
            Activity_Settings.this.startActivity(myIntent);
        });
        btn2.setOnClickListener(v ->
        {
            Intent myIntent = new Intent(Activity_Settings.this, Activity_Apps.class);
            Activity_Settings.this.startActivity(myIntent);
        });
        btn3.setOnClickListener(v ->
        {
            String error = getString(R.string.error);
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(myAppLinkToMarket);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
        btn4.setOnClickListener(v ->
        {
            Intent myIntent = new Intent(this, Activity_kofi.class);
            this.startActivity(myIntent);
        });

        Class_Prefs pref = new Class_Prefs();
        int sp1_idx = pref.LoadPrefInt(getApplicationContext(), "theme_data");
        int sp3_idx = pref.LoadPrefInt(getApplicationContext(), "language_data");

        sp1.setSelection(sp1_idx);
        sp2.setSelection(sp3_idx);

        getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent myIntent = new Intent(Activity_Settings.this, Activity_Main.class);
                Activity_Settings.this.startActivity(myIntent);
                finish();
            }
        });
    }

    public void button5_clicked(View view){
        String error = getString(R.string.error);
        Uri uri = Uri.parse("market://details?id=mivs.niewolnik_maryi_plus");
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try { startActivity(myAppLinkToMarket);}
        catch (ActivityNotFoundException e) { Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
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
