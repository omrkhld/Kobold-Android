package omrkhld.com.koboldfightclub.Manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import omrkhld.com.koboldfightclub.Helper.DividerItemDecoration;
import omrkhld.com.koboldfightclub.MonsterList.MonsterListActivity;
import omrkhld.com.koboldfightclub.MonsterList.SelectedListFragment;
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

    private RealmConfiguration encConfig;
    private Realm encRealm;
    public RealmResults<Encounter> encResults;

    public static EncManagerFragment newInstance() {
        EncManagerFragment fragment = new EncManagerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_encmanager, container, false);
        ButterKnife.bind(this, view);
        initRealm();
        list.addItemDecoration(new DividerItemDecoration(getContext()));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MonsterListActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void initRealm() {
        encConfig = new RealmConfiguration.Builder()
                .name(getString(R.string.encounters_realm))
                .deleteRealmIfMigrationNeeded()
                .build();
        encRealm = Realm.getInstance(encConfig);
    }

    @Subscribe
    public void addToRealm(SelectedListFragment.SubmitEvent event) {
        RealmList<Monster> monsters = event.monsters;
        Encounter e = new Encounter();
        RealmQuery<Encounter> query = encRealm.where(Encounter.class);
        encResults = query.findAll();
        int count = encResults.size() + 1;
        e.setID(count);
        e.setEnc(monsters);

        encRealm.beginTransaction();
        encRealm.copyToRealmOrUpdate(e);
        encRealm.commitTransaction();
    }

    @Override
    public void onResume() {
        super.onResume();
        RealmQuery<Encounter> query = encRealm.where(Encounter.class);
        encResults = query.findAll();
        Log.e(TAG, "Enc size: " + encResults.size());
        list.setAdapter(new EncRealmAdapter((AppCompatActivity)getActivity(), encResults));
        list.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        encRealm.close();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
