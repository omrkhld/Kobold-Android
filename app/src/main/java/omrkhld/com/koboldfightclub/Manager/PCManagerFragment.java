package omrkhld.com.koboldfightclub.Manager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import omrkhld.com.koboldfightclub.DividerItemDecoration;
import omrkhld.com.koboldfightclub.Player;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 4/8/2016.
 */
public class PCManagerFragment extends Fragment implements AddPlayerDialogFragment.AddPlayerDialogListener {

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
        selectedParty = "Party";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pcmanager, container, false);
        ButterKnife.bind(this, view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                AddPlayerDialogFragment dialog = AddPlayerDialogFragment.newInstance("Add Player", null);
                dialog.setTargetFragment(PCManagerFragment.this, 300);
                dialog.show(fm, "fragment_dialog_add_player");
                updateList();
            }
        });

        list.addItemDecoration(new DividerItemDecoration(getContext()));
        // Set the adapter
        list.setAdapter(new PCRecyclerViewAdapter((AppCompatActivity) getActivity(), results));

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                results = playersRealm.where(Player.class).findAll();
                final Player p = new Player(results.get(position));
                playersRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        results.deleteFromRealm(position);
                    }
                });
                Snackbar.make(getView(), "Undo delete?", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                playersRealm.beginTransaction();
                                playersRealm.copyToRealm(p);
                                playersRealm.commitTransaction();
                                updateList();
                            }
                        })
                        .show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(list);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
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

    public void updateList() {
        RealmQuery<Player> query = playersRealm.where(Player.class);
        results = query.findAll();
        list.setAdapter(new PCRecyclerViewAdapter((AppCompatActivity) getActivity(), results));
        list.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onFinishAddDialog(Player player) {
        playersRealm.beginTransaction();
        playersRealm.copyToRealmOrUpdate(player);
        playersRealm.commitTransaction();
        updateList();
    }
}
