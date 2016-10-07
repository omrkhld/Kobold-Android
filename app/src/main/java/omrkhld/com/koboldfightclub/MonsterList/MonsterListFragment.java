package omrkhld.com.koboldfightclub.MonsterList;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import omrkhld.com.koboldfightclub.Helper.DividerItemDecoration;
import omrkhld.com.koboldfightclub.POJO.Monster;
import omrkhld.com.koboldfightclub.R;
import omrkhld.com.koboldfightclub.Helper.RecyclerViewFastScroller;

/**
 * Created by Omar on 5/10/2016.
 */

public class MonsterListFragment extends Fragment {

    public static final String TAG = "MonsterListFragment";
    private RealmConfiguration monstersConfig;
    private Realm monstersRealm;
    public RealmResults<Monster> results;
    public String[] conditions = {"", "", "", "0", "30"};

    @BindView(R.id.list_view) RecyclerView list;
    @BindView(R.id.fast_scroller) RecyclerViewFastScroller fastScroller;

    public static MonsterListFragment newInstance() {
        MonsterListFragment fragment = new MonsterListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monster_list, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        monstersConfig = new RealmConfiguration.Builder()
                .name(getString(R.string.monsters_realm))
                .deleteRealmIfMigrationNeeded()
                .build();
        monstersRealm = Realm.getInstance(monstersConfig);
        RealmQuery<Monster> query = monstersRealm.where(Monster.class);
        results = query.findAllAsync();

        list.addItemDecoration(new DividerItemDecoration(getContext()));
        list.setAdapter(new MonsterRealmAdapter((AppCompatActivity) getActivity(), results));
        fastScroller.setRecyclerView(list);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        RealmQuery<Monster> query = monstersRealm.where(Monster.class);
        results = query.findAllAsync();
        list.setAdapter(new MonsterRealmAdapter((AppCompatActivity) getActivity(), results));
        list.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        monstersRealm.close();
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_monster_list, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                RealmQuery<Monster> query = monstersRealm.where(Monster.class);
                try {
                    query.equalTo("cr", Float.parseFloat(newText));
                } catch(NumberFormatException e) {
                    query.contains("name", newText, Case.INSENSITIVE);
                    query.or().contains("size", newText, Case.INSENSITIVE);
                    query.or().contains("type", newText, Case.INSENSITIVE);
                    query.or().contains("tag", newText, Case.INSENSITIVE);
                }

                updateRecyclerView(query);
                return true;
            }
        });
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
            case R.id.filter:
                return true;
            case R.id.search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private RealmResults<Monster> buildQuery(RealmQuery<Monster> query) {
        query.contains("size", conditions[0], Case.INSENSITIVE)
                .contains("type", conditions[1], Case.INSENSITIVE)
                .contains("alignment", conditions[2], Case.INSENSITIVE)
                .greaterThanOrEqualTo("cr", Float.parseFloat(conditions[3]))
                .lessThanOrEqualTo("cr", Float.parseFloat(conditions[4]));

        return query.findAllAsync();
    }

    private void updateRecyclerView(RealmQuery<Monster> query) {
        results = buildQuery(query);
        list.setAdapter(new MonsterRealmAdapter((AppCompatActivity) getActivity(), results));
        list.getAdapter().notifyDataSetChanged();
    }

    private void showInfoDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        InfoDialogFragment infoDialogFragment = InfoDialogFragment.newInstance("Difficulty");
        infoDialogFragment.show(fm, "fragment_dialog_info");
    }
}