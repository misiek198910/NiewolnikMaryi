package mivs.niewolnik_maryi;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import mivs.niewolnik_maryi.BuildConfig;

public class Activity_Oredzia extends AppCompatActivity {
    private FrameLayout adContainerView;
    private AdView adView;
    LinearLayout bottom_toolbar;
    TextView toolbar_title;
    ImageButton button_back;
    Spinner sp1;
    TextView textview_content;
    ConstraintLayout main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_oredzia);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MobileAds.initialize(this, initializationStatus -> {});

        adContainerView = findViewById(R.id.ad_view_container);
        loadAdaptiveBanner();

        main = findViewById(R.id.main);
        sp1 = findViewById(R.id.spinner1);

        Class_App_theme app_theme = new Class_App_theme();

        int color1 = app_theme.Background_theme_change(getApplicationContext());
        main.setBackgroundResource(color1);

        int color = app_theme.Toolbar_theme_change(getApplicationContext());
        button_back = findViewById(R.id.button_back);
        button_back.setBackgroundResource(color1);
        bottom_toolbar = findViewById(R.id.bottom_toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.main_button4);
        bottom_toolbar.setBackgroundColor(color);
        button_back.setOnClickListener( v -> finish());

        int lastpostion = sp1.getCount() -1;
        sp1.setSelection(lastpostion);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                textview_content = findViewById(R.id.textView_content);

                String itemstring = (String)sp1.getSelectedItem();

                String fileName = "Or_" + itemstring;
                String partEN = "_en.txt";
                String partPL = "_pl.txt";
                String file2;

                Locale current = getResources().getConfiguration().getLocales().get(0);
                String localeIso = current.getDisplayName();

                if (localeIso.equals("polski")) { file2 = fileName+partPL;}
                else {file2 = fileName+partEN;}

                String content = LoadData(file2);

                textview_content.setText(Html.fromHtml(content , Html.FROM_HTML_MODE_COMPACT));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }
    public String LoadData(String inFile)
    {
        String tContents = "";
        try
        {
            InputStream stream = getAssets().open(inFile);

            int size = stream.available();
            byte[] buffer = new byte[size];
            //noinspection ResultOfMethodCallIgnored
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        }
        catch (IOException e) {
            // Handle exceptions here
        }

        return tContents;

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