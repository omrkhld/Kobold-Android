package omrkhld.com.koboldfightclub.Manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 4/8/2016.
 */
public class PCManagerFragment extends Fragment {

    public static final String TAG = "PCManagerFragment";
    @BindView(R.id.pc_recyclerview) RecyclerView list;
    @BindView(R.id.pc_fab) FloatingActionButton fab;

    SharedPreferences pclevels;

    public static PCManagerFragment newInstance() {
        PCManagerFragment fragment = new PCManagerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pclevels = getActivity().getSharedPreferences(getString(R.string.pref_pc_levels), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pcmanager, container, false);
        ButterKnife.bind(this, view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return view;
    }
}
