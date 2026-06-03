package mivs.niewolnik_maryi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity_kofi extends AppCompatActivity {

    LinearLayout bottom_toolbar;
    ImageButton button_back;
    ConstraintLayout main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_kofi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_kofi_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        main = findViewById(R.id.activity_kofi_main);

        Class_App_theme app_theme = new Class_App_theme();

        int color1 = app_theme.Background_theme_change(getApplicationContext());
        main.setBackgroundResource(color1);

        int color = app_theme.Toolbar_theme_change(getApplicationContext());
        button_back = findViewById(R.id.button_back);
        button_back.setBackgroundResource(color1);
        bottom_toolbar = findViewById(R.id.bottom_toolbar);
        bottom_toolbar.setBackgroundColor(color);
        button_back.setOnClickListener( v -> {
            finish();
            Intent myIntent = new Intent(this, Activity_Main.class);
            this.startActivity(myIntent);
        });

        ImageView btn = findViewById(R.id.kofi_button);
        btn.setOnClickListener(v ->{
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ko-fi.com/michals"));
            startActivity(browserIntent);
        });

        ImageButton button_back = findViewById(R.id.button_back);
        button_back.setOnClickListener( v -> {
            Intent myIntent = new Intent(this, Activity_Settings.class);
            this.startActivity(myIntent);
            finish();
        });
    }
}