package omrkhld.com.koboldfightclub.MonsterList;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import omrkhld.com.koboldfightclub.Helper.DividerItemDecoration;
import omrkhld.com.koboldfightclub.Helper.SelectedSingleton;
import omrkhld.com.koboldfightclub.POJO.Monster;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 5/10/2016.
 */

public class SelectedListFragment extends Fragment {

    public static final String TAG = "SelectedListFragment";
    private RealmConfiguration monstersConfig;
    private Realm monstersRealm;
    public ArrayList<String> names;
    public RealmResults<Monster> results;

    @BindView(R.id.selected_list_view) RecyclerView list;
    @BindView(R.id.list_fab) FloatingActionButton fab;

    public static SelectedListFragment newInstance() {
        SelectedListFragment fragment = new SelectedListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selected_list, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        monstersConfig = new RealmConfiguration.Builder()
                .name(getString(R.string.monsters_realm))
                .deleteRealmIfMigrationNeeded()
                .build();
        monstersRealm = Realm.getInstance(monstersConfig);
        list.addItemDecoration(new DividerItemDecoration(getContext()));
        list.setAdapter(new MonsterRealmAdapter((AppCompatActivity) getActivity(), results));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (results != null) {
                    RealmList<Monster> monsters = new RealmList<Monster>();
                    HashMap<String, Integer> hm = SelectedSingleton.getInstance().getQtyMap();
                    for (Monster m : results) {
                        for (int i = 0; i < hm.get(m.getName()); i++) {
                            monsters.add(m);
                        }
                    }
                    Log.e(TAG, "Size: " + monsters.size());
                    EventBus.getDefault().post(new SubmitEvent(monsters));
                    getActivity().finish();
                }
            }
        });

        return view;
    }

    @Subscribe
    public void updateList(MonsterRealmAdapter.UpdateEvent event) {
        RealmQuery<Monster> query = monstersRealm.where(Monster.class);
        names = SelectedSingleton.getInstance().getNames();

        if (names.size() > 0) {
            query.equalTo("name", names.get(0));
            for (int i = 1; i < names.size(); i++) {
                query.or().equalTo("name", names.get(i));
            }
            results = query.findAll();
        } else {
            results = null;
        }

        list.setAdapter(new SelectedRealmAdapter((AppCompatActivity) getActivity(), results));
        list.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        monstersRealm.close();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_selected_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.info:
                showInfoDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showInfoDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        InfoDialogFragment infoDialogFragment = InfoDialogFragment.newInstance("Difficulty");
        infoDialogFragment.show(fm, "fragment_dialog_info");
    }

    public class SubmitEvent {
        public final RealmList<Monster> monsters;

        public SubmitEvent(RealmList<Monster> monsters) {
            this.monsters = monsters;
        }
    }
}