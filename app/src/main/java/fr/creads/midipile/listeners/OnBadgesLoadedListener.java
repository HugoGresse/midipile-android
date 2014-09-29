package fr.creads.midipile.listeners;

import java.util.List;

import fr.creads.midipile.objects.Badge;

/**
 * Author : Hugo Gresse
 * Date : 17/09/14
 */
public interface OnBadgesLoadedListener {

    public void onBadgeLoaded(List<Badge> badges);

}
