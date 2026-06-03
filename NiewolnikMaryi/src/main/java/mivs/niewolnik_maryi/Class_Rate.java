package mivs.niewolnik_maryi;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Class_Rate extends BottomSheetDialogFragment {

    /** @noinspection DataFlowIssue*/
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.window_rate,
                container, false);

        Button button_rate = v.findViewById(R.id.button_rate);
        Button button_cancel = v.findViewById(R.id.button_cancel);


        button_rate.setOnClickListener(v1 -> {

            String error = getString(R.string.error);
            Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(myAppLinkToMarket);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }

            dismiss();
        });

        button_cancel.setOnClickListener(v2 -> dismiss());

        return v;
    }
}
