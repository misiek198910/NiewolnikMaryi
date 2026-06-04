package mivs.niewolnik_maryi;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

import mivs.niewolnik_maryi.BuildConfig;

public class Activity_Oredzia extends AppCompatActivity {
    private FrameLayout adContainerView;
    private AdView adView;
    LinearLayout bottom_toolbar;
    TextView toolbar_title;
    ImageButton button_back;
    Spinner sp1;
    WebView webview_content;
    ConstraintLayout main;

    @SuppressLint("SetJavaScriptEnabled")
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
        webview_content = findViewById(R.id.webView_content);

        android.webkit.WebSettings webSettings = webview_content.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webview_content.setWebViewClient(new android.webkit.WebViewClient());

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

        String[] spinnerYearsArray = getResources().getStringArray(R.array.spinner5_items);

        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                spinnerYearsArray
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp1.setAdapter(adapter);

        int lastpostion = sp1.getCount() - 1;
        sp1.setSelection(lastpostion);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedYear = sp1.getSelectedItem().toString();
                
                String targetUrl = "https://www.medjugorje.ws/pl/messages/" + selectedYear + "/";

                webview_content.loadUrl(targetUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

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