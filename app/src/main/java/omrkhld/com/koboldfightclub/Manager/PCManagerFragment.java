package omrkhld.com.koboldfightclub.Manager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import omrkhld.com.koboldfightclub.Player;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 4/8/2016.
 */
public class PCManagerFragment extends Fragment {

    public static final String TAG = "PCManagerFragment";
    @BindView(R.id.pc_recyclerview) RecyclerView list;
    @BindView(R.id.pc_fab) FloatingActionButton fab;

    public SharedPreferences xpThresholds;
    public RealmConfiguration playersConfig;
    public Realm playersRealm;
    public RealmResults<Player> results;
    public String selectedParty;

    public static PCManagerFragment newInstance() {
        PCManagerFragment fragment = new PCManagerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        xpThresholds = getActivity().getSharedPreferences(getString(R.string.pref_party_threshold), 0);
        playersConfig = new RealmConfiguration.Builder(this.getContext())
                .name(getString(R.string.players_realm))
                .deleteRealmIfMigrationNeeded()
                .build();
        playersRealm = Realm.getInstance(playersConfig);
        RealmQuery<Player> query = playersRealm.where(Player.class);
        results = query.findAllAsync();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pcmanager, container, false);
        ButterKnife.bind(this, view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // Set the adapter
        list.setAdapter(new PCRecyclerViewAdapter(this.getActivity(), results));
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        int easy = 0, med = 0, hard = 0, deadly = 0;
        RealmResults<Player> selected = playersRealm.where(Player.class).equalTo("party", selectedParty).findAll();
        if (selectedParty.isEmpty()) {
            easy = 25; med = 50; hard = 75; deadly = 100;
        } else {
            for (int i = 0; i < selected.size(); i++) {
                easy += selected.get(i).getEasy();
                med += selected.get(i).getMed();
                hard += selected.get(i).getHard();
                deadly += selected.get(i).getDeadly();
            }
        }

        SharedPreferences.Editor editor = xpThresholds.edit();
        editor.putInt("easy", easy);
        editor.putInt("med", med);
        editor.putInt("hard", hard);
        editor.putInt("deadly", deadly);
        editor.apply();

        playersRealm.close();
    }
}
