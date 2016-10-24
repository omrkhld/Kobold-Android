package omrkhld.com.koboldfightclub.MonsterList;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import omrkhld.com.koboldfightclub.Helper.SelectedSingleton;
import omrkhld.com.koboldfightclub.R;

public class MonsterListActivity extends AppCompatActivity {

    public static final String TAG = "MonsterListActivity";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.list_view_pager) ViewPager pager;
    @BindView(R.id.list_tab_layout)  TabLayout tabs;
    public FragmentPagerAdapter adapter;
    public SharedPreferences filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.fragment_encbuilder));

        SelectedSingleton.newInstance();

        adapter = new ListPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

        filters = getSharedPreferences(getString(R.string.pref_filters), 0);
        filters.edit().clear().apply();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(pager.getCurrentItem() != 0) {
            pager.setCurrentItem(0, true);
        } else {
            super.onBackPressed();
        }
    }
}
