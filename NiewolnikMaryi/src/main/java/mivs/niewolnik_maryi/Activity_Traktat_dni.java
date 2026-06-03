package mivs.niewolnik_maryi;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;
import mivs.niewolnik_maryi.BuildConfig;

public class Activity_Traktat_dni extends AppCompatActivity {

    LinearLayout bottom_toolbar;
    TextView toolbar_title;
    ImageButton button_back;
    private FrameLayout adContainerView;
    private AdView adView;
    ConstraintLayout main;
    int idx;
    List<String> Days;
    LinearLayout ll;
    LinearLayout lr;
    String head;
    Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_traktat_dni);
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
            try
            {
                idx = bundle.getInt("idx", 0);
            }
            catch (Exception ex)
            {
                idx = 0;
            }



        }

        Days = new ArrayList<>();


        switch (idx)
        {
            case 0:
                for (int i = 1; i< 13; i++) { Days.add(Integer.toString(i)); }
                head = getString(R.string.Twelfth_head);
                break;
            case 1:
                for (int i = 1; i < 8; i++) { Days.add(Integer.toString(i)); }
                head = getString(R.string.seven_head_1);
                break;
            case 2:
                for (int i = 1; i < 8; i++) { Days.add(Integer.toString(i)); }
                head = getString(R.string.seven_head_2);
                break;
            case 3:
                for (int i = 1; i < 8; i++) { Days.add(Integer.toString(i)); }
                head = getString(R.string.seven_head_3);
                break;
            case 4:
                for (int i = 1; i < 10; i++) { Days.add(Integer.toString(i)); }
                head = getString(R.string.novenna_head);
                break;
        }

        Class_App_theme app_theme = new Class_App_theme();

        main = findViewById(R.id.main);
        int color1 = app_theme.Background_theme_change(getApplicationContext());
        main.setBackgroundResource(color1);

        int color = app_theme.Toolbar_theme_change(getApplicationContext());
        button_back = findViewById(R.id.button_back);
        button_back.setBackgroundResource(color1);
        bottom_toolbar = findViewById(R.id.bottom_toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(head);
        bottom_toolbar.setBackgroundColor(color);
        button_back.setOnClickListener( v -> finish());

        ll = findViewById(R.id.ll);
        lr = findViewById(R.id.lr);
        clear = findViewById(R.id.button_clear);

        app_theme.Button_theme_change(getApplicationContext(), clear);
        clear.setOnClickListener(v ->
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //builder.setTitle("Oznaczone dni");
            builder.setMessage(getString(R.string.marked));
            builder.setPositiveButton(getString(R.string.ok), (dialog, which) ->
            {
                Class_Prefs Pref = new Class_Prefs();
                if (idx == 0)
                {
                    for (int i = 0; i < Days.size(); i++)
                    {
                        if (i < 6)
                        {
                            Button BTN = (Button)ll.getChildAt(i);
                            String alphafile = idx + (String) BTN.getTag();
                            BTN.setAlpha(1);
                            Pref.SavePrefFloat(getApplicationContext(),alphafile, 1);
                        }
                        if (i > 5)
                        {
                            int a = i - 6;
                            Button BTN = (Button)lr.getChildAt(a);
                            String alphafile = idx + (String) BTN.getTag();
                            BTN.setAlpha(1);
                            Pref.SavePrefFloat(getApplicationContext(),alphafile, 1);
                        }
                    }
                }
                else
                {
                    for (int i = 0; i < Days.size(); i++)
                    {
                        Button BTN = (Button)ll.getChildAt(i);
                        String alphafile = idx + (String) BTN.getTag();
                        BTN.setAlpha(1);
                        Pref.SavePrefFloat(getApplicationContext(),alphafile, 1);
                    }
                }
            });
            builder.setNegativeButton(getString(R.string.cancel), (dialog, which) ->
                    dialog.dismiss());

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        });

        LinearLayout.LayoutParams buttonsParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonsParams.setMargins(10, 10, 10, 10);
        buttonsParams.gravity = Gravity.CENTER;

        for (int i = 0; i < Days.size(); i++)
        {
            Class_Prefs Pref = new Class_Prefs();

            ContextThemeWrapper newContext = new ContextThemeWrapper(this, R.style.button_style);
            Button BTN = new Button(newContext);
            app_theme.Button_theme_change(getApplicationContext(), BTN);
            BTN.setTag(Integer.toString(i));
            BTN.setLayoutParams(buttonsParams);

            String buttontxt = Days.get(i);
            BTN.setText(buttontxt);

            String alphafile = idx + (String) BTN.getTag();

            float al = Pref.LoadPrefFloat(getApplicationContext(),alphafile );
            if(al == 0.0) {al = 1;}
            BTN.setAlpha(al);
            BTN.setOnClickListener(v->
            {
                BTN.setAlpha(0.5f);
                Pref.SavePrefFloat(getApplicationContext(),alphafile, 0.5f);

                String fileName = "Tr_" + idx + "_" + BTN.getTag();

                if (idx == 4)
                {
                    fileName = "Now_" + 0 + "_" + BTN.getTag();
                }

                String week;

                int dayintone = 0;

                switch (idx)
                {
                    case 0: //12 dni
                        week = getString(R.string.Twelfth_head);
                        break;
                    case 1: //1 tydzień
                        week = getString(R.string.seven_head_1);
                        dayintone =+ 12;
                        break;
                    case 2: //2 tydzień
                        week = getString(R.string.seven_head_2);
                        dayintone =+ 19;
                        break;
                    case 3: //3 tydzień
                        week = getString(R.string.seven_head_3);
                        dayintone =+ 26;
                        break;
                    case 4: //Novenna
                        week = getString(R.string.novenna_head);
                        dayintone =+ 0;
                        break;
                    default:
                        week = getString(R.string.seven_head_1);
                        break;
                }
                int dayint = Integer.parseInt((String)BTN.getTag()) + dayintone;
                dayint++;
                String day = Integer.toString(dayint);
                String title = getString(R.string.day) + day;
                Intent myIntent = new Intent(this, Activity_Show_Text.class);
                myIntent.putExtra("file",fileName);
                myIntent.putExtra("title", title);
                myIntent.putExtra("Week", week);
                this.startActivity(myIntent);

            });

            if (idx == 0)
            {
                if (i < 6)
                {
                    ll.addView(BTN);
                }
                else
                {
                    lr.addView(BTN);
                }
            }

            if (idx == 4)
            {
                if (i < 4)
                {
                    ll.addView(BTN);
                }
                else
                {
                    lr.addView(BTN);
                }
            }

            if (idx != 0 && idx != 4)
            {
                ll.addView(BTN);
            }

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