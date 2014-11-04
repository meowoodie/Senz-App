package com.senz.network;

import android.location.Location;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collection;
import java.io.IOException;
import com.senz.core.Senz;
import com.senz.core.Beacon;
import com.senz.core.BeaconWithSenz;
import com.senz.network.Cache;
import com.senz.network.Network;
import com.senz.utils.Asyncfied;
import com.senz.utils.L;

public class Query {
    static private <T> void addAllToHashSet(Collection<T> c, HashSet<T> hs) {
        for (T t : c)
            hs.add(t);
    }

    static public ArrayList<Senz> senzesFromBeacons(Collection<Beacon> beacons, Location lastBeen) throws IOException {
        HashSet<Senz> result = new HashSet<Senz>();
        ArrayList<Beacon> toQueryServer = new ArrayList<Beacon>();

        for (Beacon beacon : beacons) {
            BeaconWithSenz bws = Cache.lookupBeacon(beacon);
            if (bws == null)
                toQueryServer.add(beacon);
            else
                result.add(bws.getSenz());
        }

        Collection<BeaconWithSenz> bwss = Network.queryBeacons(toQueryServer, lastBeen);
        Cache.addBeaconsWithSenz(bwss);
        for (BeaconWithSenz bws : bwss)
            result.add(bws.getSenz());
        return new ArrayList<Senz>(result);
    }

    static public ArrayList<Senz> senzesFromLocation(Location location) throws IOException {
        HashSet<Senz> result = new HashSet<Senz>();
        ArrayList<BeaconWithSenz> bwss = Network.queryLocation(location);
        Cache.addBeaconsWithSenz(bwss);
        for (BeaconWithSenz bws : bwss)
            result.add(bws.getSenz());
        return new ArrayList<Senz>(result);
    }

    static public void senzesFromLocationAsync(final Location location, final SenzReadyCallback cb, final ErrorHandler eh) {
        // It's a Class that can make your defined callbacks running in an defined order.
        Asyncfied.runAsyncfiable(new Asyncfied.Asyncfiable<ArrayList<Senz>>() {
            // First, it will try runAndReturn first, and get a return value(this value will be passed to onReturn())
            // - senzesFromLocation: send location to AVOSCloud Server to get Senz info back.
            @Override
            public ArrayList<Senz> runAndReturn() throws IOException {
                L.i("running query for locations");
                return senzesFromLocation(location);
            }
            // Finally, it will handle the result which is returned by runAndReturn()
            // - onSenzReady: It is defined by user, here we can use it to get senz result.
            @Override
            public void onReturn(ArrayList<Senz> result) {
                L.i("query returnning");
                cb.onSenzReady(result);
            }
            // If it throw an error, it will run this.
            @Override
            public void onError(Exception e) {
                L.i("query location error");
                eh.onError(e);
            }
        });
    }

    // It seems like senzesFromLocationAsync. No more comment.
    // The only difference is runAndReturn(), it will run senzesFromBeacons().
    static public void senzesFromBeaconsAsync(final Collection<Beacon> beacons, final Location location, final SenzReadyCallback cb, final ErrorHandler eh) {
        Asyncfied.runAsyncfiable(new Asyncfied.Asyncfiable<ArrayList<Senz>>() {
            @Override
            public ArrayList<Senz> runAndReturn() throws IOException {
                L.i("running query for beacons");
                return senzesFromBeacons(beacons, location);
            }

            @Override
            public void onReturn(ArrayList<Senz> result) {
                L.i("query returnning");
                cb.onSenzReady(result);
            }

            @Override
            public void onError(Exception e) {
                L.i("query beacons error");
                eh.onError(e);
            }
        });
    }

    // It's an interface for user to define callback, when avoscloud server return the senz info.
    public interface SenzReadyCallback {
        public void onSenzReady(ArrayList<Senz> senzes);
    }
    // It's an interface for user to define callback, when send query request throw a error.
    public interface ErrorHandler {
        public void onError(Exception e);
    }
}
