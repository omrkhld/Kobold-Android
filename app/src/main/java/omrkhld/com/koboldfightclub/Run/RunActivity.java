package omrkhld.com.koboldfightclub.Run;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import omrkhld.com.koboldfightclub.POJO.Combatant;
import omrkhld.com.koboldfightclub.POJO.Monster;
import omrkhld.com.koboldfightclub.POJO.Player;
import omrkhld.com.koboldfightclub.R;

public class RunActivity extends AppCompatActivity {

    public static final String TAG = "RunActivity";
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.recycler_view) RecyclerView list;

    public RealmConfiguration playersConfig, monstersConfig;
    public Realm playersRealm, monstersRealm;
    public RealmResults<Player> playerResults;
    public RealmResults<Monster> monsterResults;
    public HashMap<String, Integer> monsterCounts;
    public ArrayList<Combatant> combatants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.app_name));
        combatants = new ArrayList<>();
        monsterCounts = new HashMap<>();
        initCombatants();
        list.setAdapter(new RunAdapter(this, combatants));
        rollInit();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_run, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.reroll:
                rollInit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initCombatants() {
        playersConfig = new RealmConfiguration.Builder()
                .name(getString(R.string.players_realm))
                .deleteRealmIfMigrationNeeded()
                .build();
        playersRealm = Realm.getInstance(playersConfig);

        monstersConfig = new RealmConfiguration.Builder()
                .name(getString(R.string.encbuild_realm))
                .deleteRealmIfMigrationNeeded()
                .build();
        monstersRealm = Realm.getInstance(monstersConfig);

        playerResults = playersRealm.where(Player.class).findAll();
        monsterResults = monstersRealm.where(Monster.class).findAll();

        for (Player p : playerResults) {
            Combatant c = new Combatant();
            c.setName(p.getName());
            c.setHP(p.getHP());
            c.setInitMod(p.getInitMod());
            combatants.add(c);
        }

        for (Monster m : monsterResults) {
            if (monsterCounts.containsKey(m.getName())) {
                int count = monsterCounts.get(m.getName());
                monsterCounts.put(m.getName(), count+1);
            } else {
                monsterCounts.put(m.getName(), 1);
            }
        }

        int count = 1;
        String prevName = monsterResults.get(0).getName();

        for (Monster m : monsterResults) {
            Combatant c = new Combatant();
            if (monsterCounts.get(m.getName()) > 1) {
                if (!m.getName().equals(prevName)) {
                    count = 1;
                    prevName = m.getName();
                }
                c.setName(m.getName() + " " + String.valueOf(count));
                count++;
            } else {
                c.setName(m.getName());
                prevName = m.getName();
            }
            c.setHP(m.getHP());
            c.setInitMod(m.getInit());
            combatants.add(c);
        }
    }

    public void rollInit() {
        Random random = new Random();
        for (Combatant c : combatants) {
            int init = random.nextInt(20) + 1;
            Log.e(TAG, "Roll: " + init);
            init += Integer.valueOf(c.getInitMod());
            c.setInit(init);
        }

        Collections.sort(combatants, new Comparator<Combatant>() {
            @Override
            public int compare(Combatant c1, Combatant c2) {
                if (c2.getInit() - c1.getInit() == 0) {
                    return Integer.valueOf(c2.getInitMod()) - Integer.valueOf(c1.getInitMod());
                }
                return c2.getInit() - c1.getInit();
            }
        });
        list.getAdapter().notifyDataSetChanged();
    }
}
