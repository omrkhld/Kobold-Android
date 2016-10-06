package omrkhld.com.koboldfightclub.MonsterList;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import omrkhld.com.koboldfightclub.R;

public class MonsterListActivity extends AppCompatActivity {

    public static final String TAG = "MonsterListActivity";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.list_view_pager) ViewPager pager;
    @BindView(R.id.list_tab_layout)  TabLayout tabs;
    public FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.fragment_encbuilder));

        adapter = new ListPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
