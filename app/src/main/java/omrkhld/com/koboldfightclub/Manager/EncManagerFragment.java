package omrkhld.com.koboldfightclub.Manager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import omrkhld.com.koboldfightclub.Helper.DividerItemDecoration;
import omrkhld.com.koboldfightclub.MonsterList.MonsterListActivity;
import omrkhld.com.koboldfightclub.MonsterList.SelectedListFragment;
import omrkhld.com.koboldfightclub.POJO.Monster;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 4/8/2016.
 */
public class EncManagerFragment extends android.support.v4.app.Fragment {

    public static final String TAG = "EncManagerFragment";
    @BindView(R.id.list) RecyclerView list;
    @BindView(R.id.enc_fab) FloatingActionButton fab;
    @BindView(R.id.text_view) TextView text;
    @BindView(R.id.linear_layout) LinearLayout linearLayout;
    @BindView(R.id.total_xp) TextView totalText;
    @BindView(R.id.adjusted_xp) TextView adjustedText;

    private RealmConfiguration encConfig;
    private Realm realm;
    public RealmResults<Monster> results;
    private SharedPreferences xpThresholds;
    private int numPlayers;

    public static EncManagerFragment newInstance() {
        EncManagerFragment fragment = new EncManagerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        xpThresholds = getActivity().getSharedPreferences(getString(R.string.pref_party_threshold), 0);
        numPlayers = xpThresholds.getInt("numPlayers", 4);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_encmanager, container, false);
        ButterKnife.bind(this, view);
        initRealm();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MonsterListActivity.class);
                startActivity(intent);
            }
        });

        list.addItemDecoration(new DividerItemDecoration(getContext()));
        list.setAdapter(new EncManagerRealmAdapter((AppCompatActivity) getActivity(), results));

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
                results = realm.where(Monster.class).findAll().sort("name");
                final Monster p = new Monster(results.get(position));
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        results.deleteFromRealm(position);
                    }
                });
                list.getAdapter().notifyItemRemoved(position);
                Snackbar.make(getView(), "Undo delete?", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                realm.beginTransaction();
                                realm.copyToRealm(p);
                                realm.commitTransaction();
                                list.getAdapter().notifyItemInserted(position);
                                results = realm.where(Monster.class).findAll().sort("name");
                                checkEmpty(results);
                            }
                        })
                        .show();

                checkEmpty(results);
                adjustExp(results);
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

        return view;
    }

    public void initRealm() {
        encConfig = new RealmConfiguration.Builder()
                .name(getString(R.string.encbuild_realm))
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(encConfig);
    }

    @Subscribe
    public void addToRealm(SelectedListFragment.SubmitEvent event) {
        RealmList<Monster> monsters = event.monsters;
        Log.e(TAG, "Size: " + monsters.size());
        realm.beginTransaction();
        for (Monster m : monsters) {
            realm.copyToRealm(m);
        }
        realm.commitTransaction();
    }

    @Subscribe
    public void updateDifficulty(PCManagerFragment.UpdateEvent event) {
        updateList();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    @Override
    public void onDestroy() {
        realm.close();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void updateList() {
        RealmQuery<Monster> query = realm.where(Monster.class);
        results = query.findAll().sort("name");
        list.setAdapter(new EncManagerRealmAdapter((AppCompatActivity)getActivity(), results));
        list.getAdapter().notifyDataSetChanged();
        checkEmpty(results);
        adjustExp(results);
    }

    public void checkEmpty(RealmResults<Monster> r) {
        if (r.isEmpty()) {
            text.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        } else {
            text.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    public void adjustExp(RealmResults<Monster> r) {
        int totalExp = 0;
        double adjustedExp = 0;
        for (Monster m : r) {
            totalExp += m.getExp();
        }
        if (r.size() == 2) {
            adjustedExp = totalExp * 1.5;
        } else if (r.size() <= 6) {
            adjustedExp = totalExp * 2;
        } else if (r.size() <= 10) {
            adjustedExp = totalExp * 2.5;
        } else if (r.size() <= 14) {
            adjustedExp = totalExp * 3;
        } else if (r.size() > 14) {
            adjustedExp = totalExp * 4;
        } else {
            adjustedExp = totalExp;
        }

        if (numPlayers < 3) {
            if (r.size() == 1) {
                adjustedExp = totalExp * 1.5;
            } else if (r.size() == 2) {
                adjustedExp = totalExp * 2;
            } else if (r.size() <= 6) {
                adjustedExp = totalExp * 2.5;
            } else if (r.size() <= 10) {
                adjustedExp = totalExp * 3;
            } else if (r.size() <= 14) {
                adjustedExp = totalExp * 4;
            } else if (r.size() > 14) {
                adjustedExp = totalExp * 5;
            }
        } else if (numPlayers > 5) {
            if (r.size() == 1) {
                adjustedExp = totalExp * 0.5;
            } else if (r.size() == 2) {
                adjustedExp = totalExp;
            } else if (r.size() <= 6) {
                adjustedExp = totalExp * 1.5;
            } else if (r.size() <= 10) {
                adjustedExp = totalExp * 2;
            } else if (r.size() <= 14) {
                adjustedExp = totalExp * 2.5;
            } else if (r.size() > 14) {
                adjustedExp = totalExp * 3;
            }
        } else {
            if (r.size() == 2) {
                adjustedExp = totalExp * 1.5;
            } else if (r.size() <= 6) {
                adjustedExp = totalExp * 2;
            } else if (r.size() <= 10) {
                adjustedExp = totalExp * 2.5;
            } else if (r.size() <= 14) {
                adjustedExp = totalExp * 3;
            } else if (r.size() > 14) {
                adjustedExp = totalExp * 4;
            } else {
                adjustedExp = totalExp;
            }
        }

        totalText.setText(Integer.toString(totalExp));
        adjustedText.setText(Integer.toString((int) adjustedExp));
    }
}
