package fr.creads.midipile.objects;

import java.util.List;

/**
 * Author : Hugo Gresse
 * Date : 28/08/14
 */
public class Deals {

    private List<Deal> deals;

    public List<Deal> getDeals() {
        return deals;
    }

    @Override
    public String toString() {
        return "Deals{" +
                "deals=" + deals +
                '}';
    }
}
