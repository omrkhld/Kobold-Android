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
import io.realm.RealmQuery;
import io.realm.RealmResults;
import omrkhld.com.koboldfightclub.Helper.DividerItemDecoration;
import omrkhld.com.koboldfightclub.POJO.Monster;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 5/10/2016.
 */

public class SelectedListFragment extends Fragment {

    public static final String TAG = "SelectedListFragment";
    private RealmConfiguration selectedConfig;
    private Realm selectedRealm;
    public RealmResults<Monster> results;
    public static HashMap<String, Integer> quantity = new HashMap<>();
    public static ArrayList<String> names = new ArrayList<>();

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

    @Subscribe
    public void addMonster(MonsterRealmAdapter.AddEvent event) {
        if (!quantity.containsKey(event.name)) {
            quantity.put(event.name, 1);
        } else {
            quantity.put(event.name, quantity.get(event.name)+1);
        }
        names.add(event.name);
    }

    @Subscribe
    public void removeMonster(MonsterRealmAdapter.AddEvent event) {
        if (quantity.containsKey(event.name)) {
            quantity.remove(event.name);
        }
        names.remove(event.name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selected_list, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selectedConfig = new RealmConfiguration.Builder()
                .name(getString(R.string.monsters_realm))
                .deleteRealmIfMigrationNeeded()
                .build();
        selectedRealm = Realm.getInstance(selectedConfig);
        list.addItemDecoration(new DividerItemDecoration(getContext()));
        list.setAdapter(new MonsterRealmAdapter((AppCompatActivity) getActivity(), results));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        selectedRealm.close();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            RealmQuery<Monster> query = selectedRealm.where(Monster.class);

            Log.e(TAG, "Size: " + names.size());
            if (names.size() > 0) {
                query.equalTo("name", names.get(0));
                Log.e(TAG, "Name: " + names.get(0));
                for (int i = 1; i < names.size(); i++) {
                    Log.e(TAG, "Name: " + names.get(i));
                    query.or().equalTo("name", names.get(i));
                }
                results = query.findAll();
            }

            list.setAdapter(new SelectedRealmAdapter((AppCompatActivity) getActivity(), results, quantity));
            list.getAdapter().notifyDataSetChanged();
        }
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
}