package omrkhld.com.koboldfightclub.Manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import omrkhld.com.koboldfightclub.Helper.DividerItemDecoration;
import omrkhld.com.koboldfightclub.MonsterList.MonsterListActivity;
import omrkhld.com.koboldfightclub.POJO.Encounter;
import omrkhld.com.koboldfightclub.POJO.Monster;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 4/8/2016.
 */
public class EncManagerFragment extends android.support.v4.app.Fragment {

    public static final String TAG = "EncManagerFragment";
    @BindView(R.id.list) RecyclerView list;
    @BindView(R.id.enc_fab) FloatingActionButton fab;

    private RealmConfiguration realmConfig;
    private Realm encountersRealm;
    public RealmResults<Encounter> results;
    public ArrayList<Monster> enc;

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

        realmConfig = new RealmConfiguration.Builder()
                .name(getString(R.string.encounters_realm))
                .deleteRealmIfMigrationNeeded()
                .build();
        encountersRealm = Realm.getInstance(realmConfig);
        RealmQuery<Encounter> query = encountersRealm.where(Encounter.class);
        results = query.findAllAsync();

        list.addItemDecoration(new DividerItemDecoration(getContext()));
        list.setAdapter(new EncRealmAdapter((AppCompatActivity)getActivity(), results));

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
        RealmQuery<Encounter> query = encountersRealm.where(Encounter.class);
        results = query.findAllAsync();
        list.setAdapter(new EncRealmAdapter((AppCompatActivity)getActivity(), results));
        list.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        encountersRealm.close();
        super.onDestroy();
    }
}
