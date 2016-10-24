package omrkhld.com.koboldfightclub.MonsterList;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import omrkhld.com.koboldfightclub.Helper.DividerItemDecoration;
import omrkhld.com.koboldfightclub.POJO.Monster;
import omrkhld.com.koboldfightclub.R;
import omrkhld.com.koboldfightclub.Helper.RecyclerViewFastScroller;

/**
 * Created by Omar on 5/10/2016.
 */

public class MonsterListFragment extends Fragment implements FilterDialogFragment.FilterDialogListener {

    public static final String TAG = "MonsterListFragment";
    public String UP = "\u2191";
    public String DOWN = "\u2193";
    private RealmConfiguration monstersConfig;
    private Realm monstersRealm;
    public RealmResults<Monster> results;
    public String[] conditions = {"", "", "", "0", "30"};

    @BindView(R.id.list_view) RecyclerView list;
    @BindView(R.id.fast_scroller) RecyclerViewFastScroller fastScroller;
    @BindView(R.id.list_fab) FloatingActionButton fab;
    @BindView(R.id.sort_by_name) TextView sortNameButton;
    @BindView(R.id.sort_by_cr) TextView sortCRButton;
    @BindView(R.id.sort_by_type) TextView sortTypeButton;
    @BindView(R.id.sort_by_alignment) TextView sortAlignmentButton;
    @BindView(R.id.sort_by_src) TextView sortSrcButton;

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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
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
        results = query.findAll();

        list.addItemDecoration(new DividerItemDecoration(getContext()));
        list.setAdapter(new MonsterRealmAdapter((AppCompatActivity) getActivity(), results));
        fastScroller.setRecyclerView(list);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPager vp = (ViewPager) container;
                vp.setCurrentItem(1);
            }
        });

        setUpSortFunctions();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
                showFilterDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpSortFunctions() {
        sortNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortCRButton.setText("CR");
                sortTypeButton.setText("Type");
                sortAlignmentButton.setText("Alignment");
                sortSrcButton.setText("Source");
                if (sortNameButton.getText().toString().contains(DOWN)) {
                    sortNameButton.setText("Name " + UP);
                    results = results.sort("name", Sort.DESCENDING);
                } else {
                    sortNameButton.setText("Name " + DOWN);
                    results = results.sort("name", Sort.ASCENDING);
                }
                Log.e(TAG, "First: " + results.get(0).getName());
                list.setAdapter(new MonsterRealmAdapter((AppCompatActivity) getActivity(), results));
                list.getAdapter().notifyDataSetChanged();
            }
        });

        sortCRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortNameButton.setText("Name");
                sortTypeButton.setText("Type");
                sortAlignmentButton.setText("Alignment");
                sortSrcButton.setText("Source");
                if (sortCRButton.getText().toString().contains(DOWN)) {
                    sortCRButton.setText("CR " + UP);
                    results = results.sort("cr", Sort.ASCENDING);
                } else {
                    sortCRButton.setText("CR " + DOWN);
                    results = results.sort("cr", Sort.DESCENDING);
                }
                Log.e(TAG, "First: " + results.get(0).getCR());
                list.setAdapter(new MonsterRealmAdapter((AppCompatActivity) getActivity(), results));
                list.getAdapter().notifyDataSetChanged();
            }
        });

        sortTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortCRButton.setText("CR");
                sortNameButton.setText("Name");
                sortAlignmentButton.setText("Alignment");
                sortSrcButton.setText("Source");
                if (sortTypeButton.getText().toString().contains(DOWN)) {
                    sortTypeButton.setText("Type " + UP);
                    results = results.sort("type", Sort.DESCENDING);
                } else {
                    sortTypeButton.setText("Type " + DOWN);
                    results = results.sort("type", Sort.ASCENDING);
                }
                Log.e(TAG, "First: " + results.get(0).getType());
                list.setAdapter(new MonsterRealmAdapter((AppCompatActivity) getActivity(), results));
                list.getAdapter().notifyDataSetChanged();
            }
        });

        sortAlignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortCRButton.setText("CR");
                sortTypeButton.setText("Type");
                sortNameButton.setText("Name");
                sortSrcButton.setText("Source");
                if (sortAlignmentButton.getText().toString().contains(DOWN)) {
                    sortAlignmentButton.setText("Alignment " + UP);
                    results = results.sort("alignment", Sort.DESCENDING);
                } else {
                    sortAlignmentButton.setText("Alignment " + DOWN);
                    results = results.sort("alignment", Sort.ASCENDING);
                }
                Log.e(TAG, "First: " + results.get(0).getAlignment());
                list.setAdapter(new MonsterRealmAdapter((AppCompatActivity) getActivity(), results));
                list.getAdapter().notifyDataSetChanged();
            }
        });

        sortSrcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortCRButton.setText("CR");
                sortTypeButton.setText("Type");
                sortAlignmentButton.setText("Alignment");
                sortNameButton.setText("Name");
                if (sortSrcButton.getText().toString().contains(DOWN)) {
                    sortSrcButton.setText("Source " + UP);
                    results = results.sort("src", Sort.DESCENDING);
                } else {
                    sortSrcButton.setText("Source " + DOWN);
                    results = results.sort("src", Sort.ASCENDING);
                }
                Log.e(TAG, "First: " + results.get(0).getSource());
                list.setAdapter(new MonsterRealmAdapter((AppCompatActivity) getActivity(), results));
                list.getAdapter().notifyDataSetChanged();
            }
        });
    }

    private RealmResults<Monster> buildQuery(RealmQuery<Monster> query) {
        query.contains("size", conditions[0], Case.INSENSITIVE)
                .contains("type", conditions[1], Case.INSENSITIVE);

        float minCR = 0, maxCR = 0;
        if (conditions[3].equals("1/8")) {
            minCR = 0.125f;
        } else if (conditions[3].equals("1/4")) {
            minCR = 0.25f;
        } else if (conditions[3].equals("1/2")) {
            minCR = 0.5f;
        } else {
            minCR = Integer.parseInt(conditions[3]);
        }

        if (conditions[4].equals("1/8")) {
            maxCR = 0.125f;
        } else if (conditions[4].equals("1/4")) {
            maxCR = 0.25f;
        } else if (conditions[4].equals("1/2")) {
            maxCR = 0.5f;
        } else {
            maxCR = Integer.parseInt(conditions[4]);
        }
        query.greaterThanOrEqualTo("cr", minCR)
                .lessThanOrEqualTo("cr", maxCR);

        if (conditions[2].isEmpty()) {
            query.contains("alignment", conditions[2], Case.INSENSITIVE);
        } else if (conditions[2].equals("*N")) {
            query.equalTo("alignment", "N", Case.INSENSITIVE)
                    .or()
                    .contains("alignment", "any", Case.INSENSITIVE);
        } else {
            query.contains("alignment", conditions[2], Case.INSENSITIVE)
                    .or()
                    .contains("alignment", "any", Case.INSENSITIVE);
        }
        return query.findAllAsync();
    }

    private void updateRecyclerView(RealmQuery<Monster> query) {
        results = buildQuery(query);
        list.setAdapter(new MonsterRealmAdapter((AppCompatActivity) getActivity(), results));
        list.getAdapter().notifyDataSetChanged();
    }

    private void showInfoDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        InfoDialogFragment dialog = InfoDialogFragment.newInstance("Difficulty");
        dialog.show(fm, "dialog_info");
    }

    private void showFilterDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FilterDialogFragment dialog = FilterDialogFragment.newInstance("Filters");
        dialog.setTargetFragment(MonsterListFragment.this, 300);
        dialog.show(fm, "dialog_filter");
    }

    @Override
    public void onFiltered(String size, String type, String alignment, String min, String max) {
        if (size.equals("All")) {
            conditions[0] = "";
        } else {
            conditions[0] = size;
        }
        if (type.equals("All")) {
            conditions[1] = "";
        } else {
            conditions[1] = type;
        }
        if (alignment.equals("All")) {
            conditions[2] = "";
        } else {
            conditions[2] = alignment;
        }
        conditions[3] = min;
        conditions[4] = max;

        if (!sortNameButton.getText().toString().contains(DOWN)) {
            sortNameButton.performClick();
        }
        RealmQuery<Monster> query = monstersRealm.where(Monster.class);
        updateRecyclerView(query);
    }
}