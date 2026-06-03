package mivs.niewolnik_maryi;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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

import java.io.IOException;
import java.io.InputStream;
import mivs.niewolnik_maryi.BuildConfig;

public class Activity_Show_Text extends AppCompatActivity {

    LinearLayout bottom_toolbar;
    TextView toolbar_title;
    ImageButton button_back;
    SeekBar Sb;
    TextView textView_content;
    String content;
    String toolbarTitle;
    ConstraintLayout main;

    private FrameLayout adContainerView;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_text);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        MobileAds.initialize(this, initializationStatus -> {});

        adContainerView = findViewById(R.id.ad_view_container);
        loadAdaptiveBanner();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null)
        {
            toolbarTitle = bundle.getString("title");
            if (toolbarTitle == null) {toolbarTitle="";}
        }

        main = findViewById((R.id.main));
        Sb = findViewById(R.id.seekBar);
        textView_content = findViewById(R.id.textView_content);

        Class_Prefs pref = new Class_Prefs();
        int state = pref.LoadPrefInt(getApplicationContext(),"seekBarProgress");
        if (state != 0) { textView_content.setTextSize(state); Sb.setProgress(state);}
        else { textView_content.setTextSize(12);}

        Class_App_theme app_theme = new Class_App_theme();

        int color1 = app_theme.Background_theme_change(getApplicationContext());
        main.setBackgroundResource(color1);

        int color = app_theme.Toolbar_theme_change(getApplicationContext());
        button_back = findViewById(R.id.button_back);
        button_back.setBackgroundResource(color1);
        bottom_toolbar = findViewById(R.id.bottom_toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(toolbarTitle);
        bottom_toolbar.setBackgroundColor(color);
        button_back.setOnClickListener( v -> finish());

        Sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 12;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;

                Class_Prefs pref = new Class_Prefs();
                pref.SavePrefInt(getApplicationContext(),"seekBarProgress", progressChangedValue);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                textView_content.setTextSize(progressChangedValue);
            }
        });

        if(bundle != null)
        {
            String file1 = bundle.getString("file");
            String partEN = "_en.txt";
            String partPL = "_pl.txt";
            String file2;

            int locale = pref.LoadPrefInt(getApplicationContext(), "language_data");

            if (locale == 0) {
                file2 = file1 + partPL;
            } else {
                file2 = file1 + partEN;
            }

            content = LoadData(file2);

            textView_content.setText(Html.fromHtml(content , Html.FROM_HTML_MODE_COMPACT));
        }
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