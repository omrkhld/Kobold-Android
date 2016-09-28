package omrkhld.com.koboldfightclub.MonsterList;

import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentManager;
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
import omrkhld.com.koboldfightclub.Manager.AddPlayerDialogFragment;
import omrkhld.com.koboldfightclub.Manager.PCManagerFragment;
import omrkhld.com.koboldfightclub.Monster;
import omrkhld.com.koboldfightclub.R;

public class MonsterRecyclerViewAdapter extends RealmRecyclerViewAdapter<Monster, MonsterRecyclerViewAdapter.ViewHolder> {

    public static final String TAG = "MonsterAdapter";
    private final AppCompatActivity activity;
    private SharedPreferences xpThresholds;
    public int easy, med, hard, deadly;

    public MonsterRecyclerViewAdapter(AppCompatActivity activity, OrderedRealmCollection<Monster> data) {
        super(activity, data, true);
        this.activity = activity;
        xpThresholds = context.getSharedPreferences(context.getString(R.string.pref_party_threshold), 0);
        easy = xpThresholds.getInt("easy", 25);
        med = xpThresholds.getInt("med", 50);
        hard = xpThresholds.getInt("hard", 75);
        deadly = xpThresholds.getInt("deadly", 100);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_monster, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Monster obj = getData().get(position);
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

        GradientDrawable bgCircle = (GradientDrawable) holder.difficulty.getDrawable();
        if (obj.getExp() >= deadly) {
            bgCircle.setColor(ContextCompat.getColor(activity, R.color.colorDeadly));
        } else if (obj.getExp() >= hard) {
            bgCircle.setColor(ContextCompat.getColor(activity, R.color.colorHard));
        } else if (obj.getExp() >= med) {
            bgCircle.setColor(ContextCompat.getColor(activity, R.color.colorMed));
        } else if (obj.getExp() >= easy) {
            bgCircle.setColor(ContextCompat.getColor(activity, R.color.colorEasy));
        } else if ((obj.getExp()*3) >= med) {
            bgCircle.setColor(ContextCompat.getColor(activity, R.color.colorPair));
        } else if ((obj.getExp()*8) >= med) {
            bgCircle.setColor(ContextCompat.getColor(activity, R.color.colorGroup));
        } else {
            bgCircle.setColor(ContextCompat.getColor(activity, R.color.colorTrivial));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        @BindView(R.id.monster_name) TextView name;
        @BindView(R.id.monster_cr) TextView cr;
        @BindView(R.id.monster_difficulty) ImageView difficulty;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }
}
