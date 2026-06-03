package mivs.niewolnik_maryi;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.Button;
import android.widget.FrameLayout;

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
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import mivs.niewolnik_maryi.BuildConfig;

public class Activity_Main extends AppCompatActivity {
    Button btn1; Button btn2; Button btn3; Button btn4; Button btn5, btn6;
    ConstraintLayout main;
    private FrameLayout adContainerView;
    private AdView adView;
    private final AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);
    ConsentInformation consentInformation;

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

        MobileAds.initialize(this, initializationStatus -> {});

        adContainerView = findViewById(R.id.ad_view_container);
        loadAdaptiveBanner();

        main = findViewById(R.id.main);
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        btn5 = findViewById(R.id.button5);
        btn6 = findViewById(R.id.button6);

        btn1.setOnClickListener(v -> {
            Intent myIntent = new Intent(Activity_Main.this, Activity_Traktat.class);
            Activity_Main.this.startActivity(myIntent);
        });
        btn2.setOnClickListener(v -> {
            Intent myIntent = new Intent(Activity_Main.this, Activity_Modlitwy.class);
            // myIntent.putExtra("key", value); //Optional parameters
            Activity_Main.this.startActivity(myIntent);
        });
        btn3.setOnClickListener(v -> {
            Intent myIntent = new Intent(Activity_Main.this, Activity_Nowenny.class);
            // myIntent.putExtra("key", value); //Optional parameters
            Activity_Main.this.startActivity(myIntent);
        });
        btn4.setOnClickListener(v -> {
            Intent myIntent = new Intent(Activity_Main.this, Activity_Oredzia.class);
            // myIntent.putExtra("key", value); //Optional parameters
            Activity_Main.this.startActivity(myIntent);
        });
        btn5.setOnClickListener(v -> {
            Intent myIntent = new Intent(Activity_Main.this, Activity_Settings.class);
            // myIntent.putExtra("key", value); //Optional parameters
            Activity_Main.this.startActivity(myIntent);
            finish();
        });
        btn6.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://buycoffee.to/mivs/MojaParafia"));
            startActivity(browserIntent);
        });


        Class_App_theme app_theme = new Class_App_theme();
        int bclr = app_theme.Background_theme_change(getApplicationContext());
        main.setBackgroundResource(bclr);

        setDeviceLocale();

        app_theme.Button_theme_change(getApplicationContext(), btn1);
        app_theme.Button_theme_change(getApplicationContext(), btn2);
        app_theme.Button_theme_change(getApplicationContext(), btn3);
        app_theme.Button_theme_change(getApplicationContext(), btn4);
        app_theme.Button_theme_change(getApplicationContext(), btn5);
        app_theme.Button_theme_change(getApplicationContext(), btn6);

        Class_Prefs pref = new Class_Prefs();
        String iso;
        int item = pref.LoadPrefInt(getApplicationContext(),"language_data");

        if (item == 0) { iso = "Pl";} else {iso = "En";}

        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(iso);  // or use "xx-YY"
        AppCompatDelegate.setApplicationLocales(appLocale);

        setupMobileAdsSdk();
        rateApp();
    }
    public void setupMobileAdsSdk() {
        // Set tag for under age of consent. false means users are not under age
        // of consent.
        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                .setTagForUnderAgeOfConsent(false)
                .build();

        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        consentInformation.requestConsentInfoUpdate(
                this,
                params,
                () -> UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                        this,
                        loadAndShowError -> {
                            if (loadAndShowError != null) {
                                // Consent gathering failed.
                                Log.w(TAG, String.format("%s: %s",
                                        loadAndShowError.getErrorCode(),
                                        loadAndShowError.getMessage()));
                            }

                            // Consent has been gathered.
                            if (consentInformation.canRequestAds()) {
                                initializeMobileAdsSdk();
                            }
                        }
                ),
                requestConsentError -> {
                    // Consent gathering failed.
                    Log.w(TAG, String.format("%s: %s",
                            requestConsentError.getErrorCode(),
                            requestConsentError.getMessage()));
                });
        // Check if you can initialize the Google Mobile Ads SDK in parallel
        // while checking for new consent information. Consent obtained in
        // the previous session can be used to request ads.
        if (consentInformation.canRequestAds()) {
            initializeMobileAdsSdk();
        }
    }
    private void initializeMobileAdsSdk() {
        isMobileAdsInitializeCalled.getAndSet(true);
    }
    public void setDeviceLocale() {
        Class_Prefs pref = new Class_Prefs();

        String firststart =  pref.LoadPrefString(getApplicationContext(),"firststart_data");

        if (Objects.equals(firststart, ""))
        {
            Locale current = getResources().getConfiguration().getLocales().get(0);

            String ll = current.toLanguageTag();

            if (ll.equals("pl-PL"))
            {
                pref.SavePrefInt(getApplicationContext(), "language_data", 0);
            }
            else
            {
                pref.SavePrefInt(getApplicationContext(), "language_data", 1);
            }

            pref.SavePrefString(getApplicationContext(),"firststart_data","true");
        }


    }

    public void rateApp()
    {
        Class_Prefs pref = new Class_Prefs();

        int count = pref.LoadPrefInt(getApplicationContext(),"rate_count");

        if (count == 2) {
            Class_Rate bottomSheet =
                    new Class_Rate();
            bottomSheet.show(getSupportFragmentManager(),
                    "ModalBottomSheet");
            bottomSheet.setCancelable(false);
        }

        count = count + 1;

        if (count == 30) { count = 0;}

        pref.SavePrefInt(getApplicationContext(), "rate_count",count);
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