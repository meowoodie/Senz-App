package com.senz.network;

import java.util.Collection;
import android.util.LruCache;
import com.senz.core.Beacon;
import com.senz.core.BeaconWithSenz;

public class Cacher {
    private LruCache<Beacon, BeaconWithSenz> mBeaconCache;

    public Cacher(int cacheSize) {
        mBeaconCache = new LruCache<Beacon, BeaconWithSenz>(cacheSize);
    }

    public BeaconWithSenz lookupBeacon(Beacon beacon) {
        return mBeaconCache.get(beacon);
    }

    public void addBeaconWithSenz(BeaconWithSenz bws) {
        mBeaconCache.put(bws, bws);
    }
}
