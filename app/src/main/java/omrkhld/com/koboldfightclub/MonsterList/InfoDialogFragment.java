package omrkhld.com.koboldfightclub.MonsterList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 5/8/2016.
 */
public class InfoDialogFragment extends DialogFragment {

    public static final String TAG = "InfoDialogFragment";

    public InfoDialogFragment() {
    }

    public static InfoDialogFragment newInstance(String title) {
        InfoDialogFragment frag = new InfoDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_info, container);
        ButterKnife.bind(this, view);
        getDialog().setCanceledOnTouchOutside(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle("Difficulty");
    }
}
