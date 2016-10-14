package omrkhld.com.koboldfightclub.ExpandableRecyclerView.Listeners;

import omrkhld.com.koboldfightclub.ExpandableRecyclerView.Models.ExpandableGroup;

/**
 * Created by Omar on 10/10/2016.
 */

public interface GroupExpandCollapseListener {

    /**
     * Called when a group is expanded
     * @param group the {@link ExpandableGroup} being expanded
     */
    void onGroupExpanded(ExpandableGroup group);

    /**
     * Called when a group is collapsed
     * @param group the {@link ExpandableGroup} being collapsed
     */
    void onGroupCollapsed(ExpandableGroup group);
}