package omrkhld.com.koboldfightclub.MonsterList;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import omrkhld.com.koboldfightclub.DividerItemDecoration;
import omrkhld.com.koboldfightclub.Monster;
import omrkhld.com.koboldfightclub.R;

public class MonsterListActivity extends AppCompatActivity {

    public static final String TAG = "MonsterListActivity";
    private RealmConfiguration monstersConfig;
    private Realm monstersRealm;
    public RealmResults<Monster> results;
    public String[] conditions = {"", "", "", "0", "30"};

    @BindView(R.id.list_view) RecyclerView list;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.fragment_encbuilder));
        list.addItemDecoration(new DividerItemDecoration(this));

        monstersConfig = new RealmConfiguration.Builder(this)
                .name(getString(R.string.monsters_realm))
                .deleteRealmIfMigrationNeeded()
                .build();
        monstersRealm = Realm.getInstance(monstersConfig);

        RealmQuery<Monster> query = monstersRealm.where(Monster.class);
        results = query.findAllAsync();

        // Set the adapter
        list.setAdapter(new MonsterRecyclerViewAdapter(this, results));
    }

    @Override
    public void onResume() {
        super.onResume();
        RealmQuery<Monster> query = monstersRealm.where(Monster.class);
        results = query.findAllAsync();
        list.setAdapter(new MonsterRecyclerViewAdapter(this, results));
        list.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        monstersRealm.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_monsterlist, menu);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.info:
                showInfoDialog();
                return true;
            case R.id.filter:
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
        list.setAdapter(new MonsterRecyclerViewAdapter(this, results));
        list.getAdapter().notifyDataSetChanged();
    }

    private void showInfoDialog() {
        FragmentManager fm = getSupportFragmentManager();
        InfoDialogFragment infoDialogFragment = InfoDialogFragment.newInstance("Group Info");
        infoDialogFragment.show(fm, "fragment_dialog_info");
    }
}
