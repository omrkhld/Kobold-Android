package omrkhld.com.koboldfightclub.MonsterList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import omrkhld.com.koboldfightclub.DividerItemDecoration;
import omrkhld.com.koboldfightclub.Monster;
import omrkhld.com.koboldfightclub.R;

public class SelectedListFragment extends Fragment {

    public static final String TAG = "SelectedListFragment";
    @BindView(R.id.selected_list_view) RecyclerView list;

    private RealmConfiguration realmConfig;
    private Realm selectedRealm;
    public RealmResults<Monster> results;

    public static SelectedListFragment newInstance() {
        SelectedListFragment fragment = new SelectedListFragment();
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
        View view = inflater.inflate(R.layout.fragment_selected_list, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        realmConfig = new RealmConfiguration.Builder(getContext())
                .name(getString(R.string.selected_realm))
                .deleteRealmIfMigrationNeeded()
                .build();
        selectedRealm = Realm.getInstance(realmConfig);
        RealmQuery<Monster> query = selectedRealm.where(Monster.class);
        results = query.findAllAsync();

        list.addItemDecoration(new DividerItemDecoration(getContext()));
        list.setAdapter(new MonsterRecyclerViewAdapter((AppCompatActivity) getActivity(), results));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        RealmQuery<Monster> query = selectedRealm.where(Monster.class);
        results = query.findAllAsync();
        list.setAdapter(new MonsterRecyclerViewAdapter((AppCompatActivity) getActivity(), results));
        list.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        selectedRealm.close();
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
}
