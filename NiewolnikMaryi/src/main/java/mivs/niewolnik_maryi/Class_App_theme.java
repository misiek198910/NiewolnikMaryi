package mivs.niewolnik_maryi;

import android.content.Context;
import android.graphics.Color;
import android.widget.Button;

/** @noinspection UnusedReturnValue*/
public class Class_App_theme
{
    Context CX;
    Button BTN;
    int res1;
    int res2;
    public int Background_theme_change(Context cx)
    {
        this.CX = cx;
        Class_Prefs pref = new Class_Prefs();
        int themeValue = pref.LoadPrefInt(CX,"theme_data");

        switch (themeValue)
        {
            case 0:
                res1 = R.color.dark_blue;
                break;
            case 1:
                res1 = R.color.light_blue;
                break;
            case 2:
                res1 = R.color.dark_gray;
                break;
            case 3:
                res1 = R.color.light_gray;
                break;
            case 4:
                res1 = R.color.lavender;
                break;
            case 5:
                res1 = R.color.orange;
                break;
            case 6:
                res1 = R.color.pink;
                break;
            case 7:
                res1 = R.color.rose;
                break;
        }

        return res1;
    }
    public Button Button_theme_change(Context cx, Button btn)
    {
        this.CX = cx;
        this.BTN = btn;
        Class_Prefs pref = new Class_Prefs();
        int themeValue = pref.LoadPrefInt(CX,"theme_data");
        switch (themeValue)
        {
            case 0:
                btn.setBackgroundResource(R.drawable.button_light_blue_style);
                btn.setTextColor(cx.getColorStateList(R.color.white));
                break;
            case 1:
                btn.setBackgroundResource(R.drawable.button_dark_blue_style);
                btn.setTextColor(cx.getColorStateList( R.color.white));
                break;
            case 2:
                btn.setBackgroundResource(R.drawable.button_light_gray_style);
                btn.setTextColor(cx.getColorStateList( R.color.white));
                break;
            case 3:
                btn.setBackgroundResource(R.drawable.button_dark_gray_style);
                btn.setTextColor(cx.getColorStateList( R.color.white));
                break;
            case 4:
                btn.setBackgroundResource(R.drawable.button_lavender_style);
                btn.setTextColor(cx.getColorStateList( R.color.white));
                break;
            case 5:
                btn.setBackgroundResource(R.drawable.button_orange_style);
                btn.setTextColor(cx.getColorStateList( R.color.white));
                break;
            case 6:
                btn.setBackgroundResource(R.drawable.button_pink_style);
                btn.setTextColor(cx.getColorStateList( R.color.white));
                break;
            case 7:
                btn.setBackgroundResource(R.drawable.button_rose_style);
                btn.setTextColor(cx.getColorStateList( R.color.white));
                break;
        }
        return BTN;
    }

    public int Toolbar_theme_change(Context cx)
    {
        this.CX = cx;
        Class_Prefs pref = new Class_Prefs();
        int themeValue = pref.LoadPrefInt(CX,"theme_data");

        switch (themeValue)
        {
            case 0:
                res2 = Color.rgb(0,162,232);
                break;
            case 1:
                res2 = Color.rgb(63,72,204);
                break;
            case 2:
                res2 = Color.rgb(170,170,170);
                break;
            case 3:
                res2 = Color.rgb(68,68,68);
                break;
            case 4:
                res2 = Color.rgb(121,83,114);
                break;
            case 5:
                res2 = Color.rgb(205,83,0);
                break;
            case 6:
                res2 = Color.rgb(165,0,165);
                break;
            case 7:
                res2 = Color.rgb(230,92,137);
                break;
        }

        return res2;
    }
}
