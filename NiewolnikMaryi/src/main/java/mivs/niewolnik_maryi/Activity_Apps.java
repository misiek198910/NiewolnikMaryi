package mivs.niewolnik_maryi;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class Activity_Apps extends AppCompatActivity {
    ImageButton button_back;
    private FrameLayout adContainerView;
    private AdView adView;
    ConstraintLayout main;
    TextView toolbar_title;
    LinearLayout bottom_toolbar;

    private static class AppItem {
        final int iconResId;
        final int titleResId;
        final String appLink;

        AppItem(int iconResId, int titleResId, String appLink) {
            this.iconResId = iconResId;
            this.titleResId = titleResId;
            this.appLink = appLink;
        }
    }

    private final AppItem[] appItems = new AppItem[]{
            new AppItem(R.drawable.droga_krzyzowa_logo, R.string.app_droga_krzyzowa, "droga_krzyzowa.droga_krzyzowa"),
            new AppItem(R.drawable.droga_krzyzowa_plus_logo, R.string.app_droga_krzyzowa_plus, "mivs.droga_krzyzowa_plus"),
            new AppItem(R.drawable.kalendarz_liturgiczny_logo, R.string.app_kalendarz_liturgiczny, "mivs.kalendarz_liturgiczny"),
            new AppItem(R.drawable.kalendarz_liturgiczny_plus_logo, R.string.app_kalendarz_liturgiczny_plus, "mivs.kalendarz_liturgiczny_plus"),
            new AppItem(R.drawable.kalkulator_cnc_logo, R.string.app_kalkulator_cnc, "kalkulator.cnc"),
            new AppItem(R.drawable.kalkulator_cnc_plus_logo, R.string.app_kalkulator_cnc_plus, "kalkulator.cnc.plus"),
            new AppItem(R.drawable.ktoz_jak_bog_logo, R.string.app_ktoz_jak_bog, "mivs.ktozjakbog"),
            new AppItem(R.drawable.ktoz_jak_bog_plus_logo, R.string.app_ktoz_jak_bog_plus, "mivs.ktozjakbog_plus"),
            new AppItem(R.drawable.moj_rozaniec_logo, R.string.app_moj_rozaniec, "mivs.m_j_r_aniec"),
            new AppItem(R.drawable.moj_rozaniec_plus_logo, R.string.app_moj_rozaniec_plus, "mivs.m_j_r_aniec_plus"),
            new AppItem(R.drawable.niewolnik_maryi_logo, R.string.app_niewolnik_maryi, "mivs.niewolnik_maryi"),
            new AppItem(R.drawable.niewolnik_maryi_plus_logo, R.string.app_niewolnik_maryi_plus, "mivs.niewolnik_maryi_plus"),
            new AppItem(R.drawable.objawienia_logo, R.string.app_objawienia, "mivs.objawienia"),
            new AppItem(R.drawable.objawienia_plus_logo, R.string.app_objawienia_plus, "mivs.objawienia_plus"),
            new AppItem(R.drawable.rachunek_sumienia_logo, R.string.app_rachunek_sumienia, "pakiet.rachuneksumienia"),
            new AppItem(R.drawable.rachunek_sumienia_plus_logo, R.string.app_rachunek_sumienia_plus, "pakiet.rachuneksumienia_plus")

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_apps);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MobileAds.initialize(this, initializationStatus -> {});

        adContainerView = findViewById(R.id.ad_view_container);
        loadAdaptiveBanner();

        Class_App_theme app_theme = new Class_App_theme();

        int color1 = app_theme.Background_theme_change(getApplicationContext());
        main = findViewById(R.id.main);
        main.setBackgroundResource(color1);

        int color = app_theme.Toolbar_theme_change(getApplicationContext());
        button_back = findViewById(R.id.button_back);
        button_back.setBackgroundResource(color1);
        bottom_toolbar = findViewById(R.id.bottom_toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.settings_button2);
        bottom_toolbar.setBackgroundColor(color);
        button_back.setOnClickListener( v -> {
            finish();
        });

        LinearLayout container = findViewById(R.id.appListContainer);
        container.setGravity(Gravity.CENTER);

        for (AppItem item : appItems) {
            LinearLayout panel = new LinearLayout(this);
            panel.setOrientation(LinearLayout.HORIZONTAL);
            panel.setPadding(10, 10, 10, 10);
            panel.setClickable(true);
            TypedValue outValue = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            panel.setBackgroundResource(outValue.resourceId);

            LinearLayout.LayoutParams panelParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            panelParams.setMargins(5, 5, 5, 5);
            panel.setLayoutParams(panelParams);

            ImageView icon = new ImageView(this);
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(100, 100);
            icon.setLayoutParams(iconParams);
            icon.setImageResource(item.iconResId);

            TextView label = new TextView(this);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            label.setLayoutParams(textParams);
            label.setText(item.titleResId);
            label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
            label.setGravity(Gravity.CENTER_VERTICAL);
            label.setPadding(16, 0, 0, 0);
            label.setTypeface(null, Typeface.BOLD); // pogrubienie
            label.setTextColor(Color.WHITE);
            //label.setBackgroundColor(Color.parseColor("#4A000000"));

            panel.addView(icon);
            panel.addView(label);

            panel.setOnClickListener(v -> {
                if (item.appLink != null) {
                    OpenStore(item.appLink);
                }
            });

            container.addView(panel);
        }
    }


    public void OpenStore(String packageName)
    {
        try
        {
            startActivity(new Intent(Intent.ACTION_VIEW, android.net.Uri.parse("market://details?id=" + packageName)));
        }
        catch (Exception e) //Jeżeli sklep jest niedostepny
        {
            startActivity(new Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
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