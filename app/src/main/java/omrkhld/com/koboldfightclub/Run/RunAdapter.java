package omrkhld.com.koboldfightclub.Run;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import omrkhld.com.koboldfightclub.POJO.Combatant;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 22/10/2016.
 */

public class RunAdapter extends RecyclerView.Adapter<RunAdapter.ViewHolder> {

    private final AppCompatActivity activity;
    private ArrayList<Combatant> combatants;

    public RunAdapter(AppCompatActivity activity, ArrayList<Combatant> combatants) {
        this.activity = activity;
        this.combatants = combatants;
    }

    @Override
    public RunAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_combatant, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RunAdapter.ViewHolder holder, int position) {
        final Combatant combatant = combatants.get(position);
        holder.name.setText(combatant.getName());
        holder.initMod.setText(combatant.getInitMod());
        holder.init.setText(Integer.toString(combatant.getInit()));
        holder.hp.setText(Integer.toString(combatant.getHP()));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = activity.getSupportFragmentManager();
                EditCombatantDialogFragment dialog = EditCombatantDialogFragment.newInstance("Edit Combatant", combatant);
                dialog.show(fm, "dialog_edit_combatant");
            }
        });
    }

    @Override
    public int getItemCount() {
        return combatants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        @BindView(R.id.combatant_name) TextView name;
        @BindView(R.id.combatant_init_mod) TextView initMod;
        @BindView(R.id.combatant_init) TextView init;
        @BindView(R.id.combatant_hp) TextView hp;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
