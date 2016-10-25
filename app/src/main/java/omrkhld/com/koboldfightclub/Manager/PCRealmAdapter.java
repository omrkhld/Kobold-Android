package omrkhld.com.koboldfightclub.Manager;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;
import omrkhld.com.koboldfightclub.POJO.Player;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 27/9/2016.
 */

public class PCRealmAdapter extends RealmRecyclerViewAdapter<Player, PCRealmAdapter.ViewHolder> {
    private final String TAG = "PCAdapter";
    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec
    private Handler handler = new Handler(); // handler for running delayed runnables
    HashMap<Player, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be
    private final AppCompatActivity activity;
    private RealmList<Player> itemsPendingRemoval;

    public PCRealmAdapter(AppCompatActivity activity, OrderedRealmCollection<Player> data) {
        super(activity, data, true);
        this.activity = activity;
        itemsPendingRemoval = new RealmList<>();
    }

    @Override
    public PCRealmAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        return new PCRealmAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Player obj = getData().get(position);

        if (itemsPendingRemoval.contains(obj)) {
            // we need to show the "undo" state of the row
            holder.itemView.setBackgroundColor(Color.RED);
            holder.layout.setVisibility(View.GONE);
            holder.undoButton.setVisibility(View.VISIBLE);
            holder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(obj);
                    pendingRunnables.remove(obj);
                    if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
                    itemsPendingRemoval.remove(obj);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(getData().indexOf(obj));
                }
            });
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.layout.setVisibility(View.VISIBLE);
            holder.undoButton.setVisibility(View.GONE);
            holder.undoButton.setOnClickListener(null);

            holder.name.setText(obj.getName());
            holder.lvl.setText(Integer.toString(obj.getLevel()));
            holder.hp.setText(Integer.toString(obj.getHP()));
            holder.init.setText(obj.getInitMod());
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = activity.getSupportFragmentManager();
                    AddPlayerDialogFragment dialog = AddPlayerDialogFragment.newInstance("Add Player", obj);
                    dialog.setTargetFragment(fm.findFragmentById(R.id.view_pager), 300);
                    dialog.show(fm, "dialog_add_player");
                }
            });
        }
    }

    public void pendingRemoval(int position) {
        final Player obj = getData().get(position);
        if (!itemsPendingRemoval.contains(obj)) {
            itemsPendingRemoval.add(obj);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(getData().indexOf(obj));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(obj, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        Player obj = getData().get(position);
        if (itemsPendingRemoval.contains(obj)) {
            itemsPendingRemoval.remove(obj);
        }
        if (getData().contains(obj)) {
            EventBus.getDefault().post(new DeleteEvent(position));
        }
    }

    public class DeleteEvent {
        public final int pos;

        public DeleteEvent(int pos) {
            this.pos = pos;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        @BindView(R.id.linear_layout) LinearLayout layout;
        @BindView(R.id.undo_button) Button undoButton;
        @BindView(R.id.player_name) TextView name;
        @BindView(R.id.player_lvl) TextView lvl;
        @BindView(R.id.player_hp) TextView hp;
        @BindView(R.id.player_init) TextView init;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }
}
