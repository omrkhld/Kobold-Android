package omrkhld.com.koboldfightclub.Manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import omrkhld.com.koboldfightclub.MonsterList.MonsterListActivity;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 4/8/2016.
 */
public class EncManagerFragment extends android.support.v4.app.Fragment {

    public static final String TAG = "EncManagerFragment";
    @BindView(R.id.list) RecyclerView list;
    @BindView(R.id.enc_fab) FloatingActionButton fab;

    public static EncManagerFragment newInstance() {
        EncManagerFragment fragment = new EncManagerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_encmanager, container, false);
        ButterKnife.bind(this, view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MonsterListActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
