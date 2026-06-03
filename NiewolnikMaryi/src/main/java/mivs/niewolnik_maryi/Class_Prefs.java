package mivs.niewolnik_maryi;

import android.content.Context;
import android.content.SharedPreferences;

/** @noinspection unused*/
public class Class_Prefs
{
    Context context;
    public void SavePrefString(Context context, String filename, String content)
    {
        this.context = context;
        SharedPreferences preferences = context.getSharedPreferences("def_shared", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(filename , content);
        editor.apply();
    }
    public String LoadPrefString(Context context, String filename)
    {
        SharedPreferences sp = context.getSharedPreferences("def_shared", Context.MODE_PRIVATE);

        return sp.getString(filename, "");
    }

    public void SavePrefInt(Context context, String filename, int content)
    {
        this.context = context;
        SharedPreferences preferences = context.getSharedPreferences("def_shared", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(filename , content);
        editor.apply();
    }

    public void SavePrefFloat(Context context, String filename, float content)
    {
        this.context = context;
        SharedPreferences preferences = context.getSharedPreferences("def_shared", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(filename , content);
        editor.apply();
    }

    public void SavePrefBoolean(Context context, String filename, Boolean content)
    {
        this.context = context;
        SharedPreferences preferences = context.getSharedPreferences("def_shared", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(filename , content);
        editor.apply();
    }

    public int LoadPrefInt(Context context, String filename)
    {
        SharedPreferences sp = context.getSharedPreferences("def_shared", Context.MODE_PRIVATE);

        return sp.getInt(filename, 0);
    }

    public float LoadPrefFloat(Context context, String filename)
    {
        SharedPreferences sp = context.getSharedPreferences("def_shared", Context.MODE_PRIVATE);

        return sp.getFloat(filename, 0);
    }

    public boolean LoadPrefBoolean(Context context, String filename)
    {
        SharedPreferences sp = context.getSharedPreferences("def_shared", Context.MODE_PRIVATE);

        return sp.getBoolean(filename, false);
    }
}
