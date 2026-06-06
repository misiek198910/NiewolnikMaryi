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
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
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

// Importy dla In-App Updates (Java)
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import mivs.niewolnik_maryi.BuildConfig;

public class Activity_Main extends AppCompatActivity {
    Button btn1; Button btn2; Button btn3; Button btn4; Button btn5;
    ConstraintLayout main;
    private FrameLayout adContainerView;
    private AdView adView;
    private final AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);
    ConsentInformation consentInformation;
    private AppUpdateManager appUpdateManager;
    private ActivityResultLauncher<IntentSenderRequest> updateLauncher;

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

        updateLauncher = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(),
                result -> {
                    if (result.getResultCode() != RESULT_OK) {
                        Log.w("InAppUpdate", "Aktualizacja anulowana lub nie powiodła się. Kod: " + result.getResultCode());
                    }
                }
        );

        checkForUpdate();

        MobileAds.initialize(this, initializationStatus -> {});

        adContainerView = findViewById(R.id.ad_view_container);
        loadAdaptiveBanner();

        main = findViewById(R.id.main);
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        btn5 = findViewById(R.id.button5);

        btn1.setOnClickListener(v -> {
            Intent myIntent = new Intent(Activity_Main.this, Activity_Traktat.class);
            Activity_Main.this.startActivity(myIntent);
        });
        btn2.setOnClickListener(v -> {
            Intent myIntent = new Intent(Activity_Main.this, Activity_Modlitwy.class);
            Activity_Main.this.startActivity(myIntent);
        });
        btn3.setOnClickListener(v -> {
            Intent myIntent = new Intent(Activity_Main.this, Activity_Nowenny.class);
            Activity_Main.this.startActivity(myIntent);
        });
        btn4.setOnClickListener(v -> {
            Intent myIntent = new Intent(Activity_Main.this, Activity_Oredzia.class);
            Activity_Main.this.startActivity(myIntent);
        });
        btn5.setOnClickListener(v -> {
            Intent myIntent = new Intent(Activity_Main.this, Activity_Settings.class);
            Activity_Main.this.startActivity(myIntent);
            finish();
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

        Class_Prefs pref = new Class_Prefs();
        String iso;
        int item = pref.LoadPrefInt(getApplicationContext(),"language_data");

        if (item == 0) { iso = "Pl";} else {iso = "En";}

        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(iso);
        AppCompatDelegate.setApplicationLocales(appLocale);

        setupMobileAdsSdk();
        rateApp();
    }
    private void checkForUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this);

        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {

                appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        updateLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                );
            }
        });
    }

    public void setupMobileAdsSdk() {
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
                                Log.w(TAG, String.format("%s: %s",
                                        loadAndShowError.getErrorCode(),
                                        loadAndShowError.getMessage()));
                            }
                            if (consentInformation.canRequestAds()) {
                                initializeMobileAdsSdk();
                            }
                        }
                ),
                requestConsentError -> {
                    Log.w(TAG, String.format("%s: %s",
                            requestConsentError.getErrorCode(),
                            requestConsentError.getMessage()));
                });

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

        if (appUpdateManager != null) {
            appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            updateLauncher,
                            AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                    );
                }
            });
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