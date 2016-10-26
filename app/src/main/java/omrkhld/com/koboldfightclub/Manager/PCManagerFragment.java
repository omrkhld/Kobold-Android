package omrkhld.com.koboldfightclub.Manager;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import omrkhld.com.koboldfightclub.Helper.DividerItemDecoration;
import omrkhld.com.koboldfightclub.POJO.Player;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 4/8/2016.
 */
public class PCManagerFragment extends Fragment implements AddPlayerDialogFragment.AddPlayerDialogListener {

    public static final String TAG = "PCManagerFragment";
    @BindView(R.id.player_recycler_view) RecyclerView list;
    @BindView(R.id.pc_fab) FloatingActionButton fab;

    private SharedPreferences xpThresholds;
    private RealmConfiguration playersConfig;
    private Realm playersRealm;
    private RealmResults<Player> results;
    private RealmChangeListener changeListener;

    public static PCManagerFragment newInstance() {
        return new PCManagerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        xpThresholds = getActivity().getSharedPreferences(getString(R.string.pref_party_threshold), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pcmanager, container, false);
        ButterKnife.bind(this, view);

        playersConfig = new RealmConfiguration.Builder()
                .name(getString(R.string.players_realm))
                .deleteRealmIfMigrationNeeded()
                .build();
        playersRealm = Realm.getInstance(playersConfig);

        RealmQuery<Player> query = playersRealm.where(Player.class);
        results = query.findAll();
        list.addItemDecoration(new DividerItemDecoration(getContext()));
        list.setAdapter(new PCRealmAdapter((AppCompatActivity) getActivity(), results));

        changeListener = new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm playersRealm) {
                list.getAdapter().notifyDataSetChanged();
                updateList();
            }
        };
        playersRealm.addChangeListener(changeListener);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            Drawable background;
            Drawable deleteIcon;
            int deleteIconMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                deleteIcon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_delete);
                deleteIconMargin = (int) getActivity().getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                PCRealmAdapter adapter = (PCRealmAdapter) list.getAdapter();
                adapter.pendingRemoval(position);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;

                    if (!initiated) {
                        init();
                    }

                    if (dX < 0) {
                        // draw red background
                        background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                        background.draw(c);

                        // draw delete icon
                        int itemHeight = itemView.getBottom() - itemView.getTop();
                        int intrinsicWidth = deleteIcon.getIntrinsicWidth();
                        int intrinsicHeight = deleteIcon.getIntrinsicWidth();

                        int xMarkLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
                        int xMarkRight = itemView.getRight() - deleteIconMargin;
                        int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                        int xMarkBottom = xMarkTop + intrinsicHeight;
                        deleteIcon.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);
                        deleteIcon.draw(c);
                    } else {
                        // draw red background
                        background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + (int) dX, itemView.getBottom());
                        background.draw(c);

                        // draw delete icon
                        int itemHeight = itemView.getBottom() - itemView.getTop();
                        int intrinsicWidth = deleteIcon.getIntrinsicWidth();
                        int intrinsicHeight = deleteIcon.getIntrinsicWidth();

                        int xMarkLeft = itemView.getLeft() + deleteIconMargin;
                        int xMarkRight = itemView.getLeft() + deleteIconMargin + intrinsicWidth;
                        int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                        int xMarkBottom = xMarkTop + intrinsicHeight;
                        deleteIcon.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);
                        deleteIcon.draw(c);
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(list);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                AddPlayerDialogFragment dialog = AddPlayerDialogFragment.newInstance("Add Player", null);
                dialog.setTargetFragment(PCManagerFragment.this, 300);
                dialog.show(fm, "dialog_add_player");
            }
        });

        return view;
    }

    @Subscribe
    public void deletePlayer(final PCRealmAdapter.DeleteEvent event) {
        playersRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteFromRealm(event.pos);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        playersRealm.removeAllChangeListeners();
        playersRealm.close();
    }

    public void updateList() {
        int numPlayers = 1, easy = 0, med = 0, hard = 0, deadly = 0;

        if (results.isEmpty()) {
            easy = 25; med = 50; hard = 75; deadly = 100;
        } else {
            numPlayers = results.size();
            for (int i = 0; i < numPlayers; i++) {
                easy += results.get(i).getEasy();
                med += results.get(i).getMed();
                hard += results.get(i).getHard();
                deadly += results.get(i).getDeadly();
            }
        }

        SharedPreferences.Editor editor = xpThresholds.edit();
        editor.putInt("numPlayers", numPlayers);
        editor.putInt("easy", easy);
        editor.putInt("med", med);
        editor.putInt("hard", hard);
        editor.putInt("deadly", deadly);
        editor.apply();
        EventBus.getDefault().post(new UpdateEvent());
    }

    @Override
    public void onFinishAddDialog(Player player, Player oldPlayer) {
        if (oldPlayer != null) {
            Player p = playersRealm.where(Player.class).equalTo("name", oldPlayer.getName()).findFirst();
            playersRealm.beginTransaction();
            p.deleteFromRealm();
            playersRealm.commitTransaction();
        }
        playersRealm.beginTransaction();
        playersRealm.copyToRealmOrUpdate(player);
        playersRealm.commitTransaction();
    }

    //This is to update the Difficulty color in Encounter Manager page
    public class UpdateEvent {
        public UpdateEvent() {
        }
    }
}
