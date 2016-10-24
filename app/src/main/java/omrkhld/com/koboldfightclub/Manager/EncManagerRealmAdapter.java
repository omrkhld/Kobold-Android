package omrkhld.com.koboldfightclub.Manager;

import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import omrkhld.com.koboldfightclub.POJO.Monster;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 6/10/2016.
 */

public class EncManagerRealmAdapter extends RealmRecyclerViewAdapter<Monster, EncManagerRealmAdapter.ViewHolder> {

    public static final String TAG = "EncounterAdapter";
    private final AppCompatActivity activity;
    private SharedPreferences xpThresholds;
    public int numPlayers, easy, med, hard, deadly;

    public EncManagerRealmAdapter(AppCompatActivity activity, OrderedRealmCollection<Monster> data) {
        super(activity, data, true);
        this.activity = activity;
        xpThresholds = context.getSharedPreferences(context.getString(R.string.pref_party_threshold), 0);
        numPlayers = xpThresholds.getInt("numPlayers", 4);
        easy = xpThresholds.getInt("easy", 25);
        med = xpThresholds.getInt("med", 50);
        hard = xpThresholds.getInt("hard", 75);
        deadly = xpThresholds.getInt("deadly", 100);
    }

    @Override
    public EncManagerRealmAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_monster_alt, parent, false);
        return new EncManagerRealmAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EncManagerRealmAdapter.ViewHolder holder, int position) {
        final Monster obj = getData().get(position);
        holder.name.setText(obj.getName());
        float cr = obj.getCR();
        String crText = "";
        if (cr == 0.125f) {
            crText = "1/8";
        } else if (cr == 0.25f) {
            crText = "1/4";
        } else if (cr == 0.5f) {
            crText = "1/2";
        } else {
            crText = String.format("%d", (int) cr);
        }
        holder.cr.setText(crText);
        holder.alignment.setText(obj.getAlignment());
        holder.type.setText(obj.getType());
        holder.src.setText(obj.getSource());

        double tempExp = obj.getExp();
        GradientDrawable bgCircle = (GradientDrawable) holder.difficulty.getDrawable();
        if (tempExp >= deadly) {
            bgCircle.setColor(ContextCompat.getColor(activity, R.color.colorDeadly));
        } else if (tempExp >= hard) {
            bgCircle.setColor(ContextCompat.getColor(activity, R.color.colorHard));
        } else if (tempExp >= med) {
            bgCircle.setColor(ContextCompat.getColor(activity, R.color.colorMed));
        } else if (tempExp >= easy) {
            bgCircle.setColor(ContextCompat.getColor(activity, R.color.colorEasy));
        } else if (numPlayers < 3) {
            if (tempExp*2*2 >= med) {
                bgCircle.setColor(ContextCompat.getColor(activity, R.color.colorPair));
            } else if (tempExp*4*2.5 >= med) {
                bgCircle.setColor(ContextCompat.getColor(activity, R.color.colorGroup));
            } else {
                bgCircle.setColor(ContextCompat.getColor(activity, R.color.colorTrivial));
            }
        } else if (numPlayers > 6) {
            if (tempExp*2 >= med) {
                bgCircle.setColor(ContextCompat.getColor(activity, R.color.colorPair));
            } else if (tempExp*4*1.5 >= med) {
                bgCircle.setColor(ContextCompat.getColor(activity, R.color.colorGroup));
            } else {
                bgCircle.setColor(ContextCompat.getColor(activity, R.color.colorTrivial));
            }
        } else {
            if (tempExp*2*1.5 >= med) {
                bgCircle.setColor(ContextCompat.getColor(activity, R.color.colorPair));
            } else if (tempExp*4*2 >= med) {
                bgCircle.setColor(ContextCompat.getColor(activity, R.color.colorGroup));
            } else {
                bgCircle.setColor(ContextCompat.getColor(activity, R.color.colorTrivial));
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        @BindView(R.id.monster_name) TextView name;
        @BindView(R.id.monster_cr) TextView cr;
        @BindView(R.id.monster_type) TextView type;
        @BindView(R.id.monster_alignment) TextView alignment;
        @BindView(R.id.monster_src) TextView src;
        @BindView(R.id.monster_difficulty) ImageView difficulty;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }
}
