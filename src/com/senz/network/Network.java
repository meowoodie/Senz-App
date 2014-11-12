package com.senz.network;

import android.location.Location;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Pair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import com.senz.utils.L;
import com.senz.core.Senz;
import com.senz.core.Utils;
import com.senz.core.Beacon;
import com.senz.core.BeaconWithSenz;

/***********************************************************************************************************************
 * @ClassName:   Network
 * @Author:      zhzhzoo
 * @CommentBy:   Woodie
 * @CommentAt:   Tue, Oct 27, 2014
 * @Description: This class is similar to a LruCache's subclass. It packages the LruCache's put() and get() for BeaconWithSenz.
 ***********************************************************************************************************************/

public class Network {
    private static String queryUrl = "https://cn.avoscloud.com/1.1/functions/";
    private static int timeout = (int) TimeUnit.SECONDS.toMillis(10);
    private static String AVOS_ID = "vigxpgtjk8w6ruxcfaw4kju3ssyttgcqz38y6y6uablqivjd";
    private static String AVOS_KEY = "dxbawm2hh0338hb37wap59gticgr92dpajd80tzekrgv1ptw";

    // Write Location info into JsonWriter
    private static void writeLocation(JsonWriter writer, Location location) throws IOException {
        writer.beginObject();
        writer.name("latitude").value(location.getLatitude());
        writer.name("longitude").value(location.getLongitude());
        writer.name("accuracy").value(location.getAccuracy());
        writer.name("time").value(location.getTime());
        writer.endObject();
    }

    // Write Beacons info into JsonWriter and ready to send request.
    private static void writeBeaconsQueryPost(JsonWriter writer, Collection<Beacon> toQuery, Location lastBeen) throws IOException {
        writer.beginObject();

        writer.name("meta");
        writer.beginObject();
        if (lastBeen != null) {
            writer.name("location");
            writeLocation(writer, lastBeen);
        }
        writer.endObject();

        writer.name("beacons");
        Utils.writeToJsonArray(writer, toQuery);

        writer.endObject();
        writer.close();
    }
    // Write Beacons info into JsonWriter and ready to send request.
    private static void writeLocationQueryPost(JsonWriter writer, Location location) throws IOException {
        writer.beginObject();

        writer.name("location");
        writeLocation(writer, location);

        writer.endObject();
        writer.close();
    }

    //
    private static HashMap<String, Senz> readSenzHashMapFromJsonObject(JsonReader reader) throws IOException {
        HashMap<String, Senz> msenz = new HashMap<String, Senz>();
        reader.beginObject();
        while (reader.hasNext())
            msenz.put(reader.nextName(), Senz.fromJson(reader));
        reader.endObject();
        return msenz;
    }

    private static ArrayList<BeaconWithSenz> readBeaconWithSenzArrayListFromJsonArrayAndSenzById(JsonReader reader, Map<String, Senz> senzesById) throws IOException {
        ArrayList<BeaconWithSenz> bwss = new ArrayList<BeaconWithSenz>();
        reader.beginArray();
        while (reader.hasNext())
            bwss.add(BeaconWithSenz.fromJsonAndSenzById(reader, senzesById));
        reader.endArray();
        return bwss;
    }

    private static ArrayList<Pair<Beacon, String>> readBeaconSenzIdPairArrayListFromJsonArray(JsonReader reader) throws IOException {
        ArrayList<Pair<Beacon, String>> tmp = new ArrayList<Pair<Beacon, String>>();
        reader.beginArray();
        while (reader.hasNext()) {
            tmp.add(BeaconWithSenz.beaconSenzIdPairFromJson(reader));
        }
        reader.endArray();
        return tmp;
    }

    // Read result from a JsonReader and Transform the result to a BeaconWithSenz object.
    private static ArrayList<BeaconWithSenz> readResult(JsonReader reader) throws IOException {
        String name, result = null;
        HashMap<String, Senz> senzesById = null;
        ArrayList<BeaconWithSenz> bwss = null;
        ArrayList<Pair<Beacon, String>> tmp = null;

        //L.i("[Network] The receiving message is: " + reader.toString());
        // read result from reader
        reader.beginObject();
        while (reader.hasNext()) {
            name = reader.nextName();
            // Get result's item.
            if (name.equals("result")) {
                result = reader.nextString();
                L.i("[Network] The 'result' is: " + result);
            }
            else {
                reader.skipValue();
            }
        }
        reader.endObject();
        reader.close();

        // If there is no result, It will throw an exception.
        if (result == null) {
            L.e("[Network] Analysis result error");
            throw new ResultNotPresentException();
        }

        // Analysis the result, and
        // Pick up BeaconWithSenz object from result.
        reader = new JsonReader(new StringReader(result));
        reader.beginObject();
        while (reader.hasNext()) {
            name = reader.nextName();
            if (name.equals("senzes")) {
                senzesById = readSenzHashMapFromJsonObject(reader);
            }
            else if (name.equals("beacons")) {
                if (senzesById != null)
                    bwss = readBeaconWithSenzArrayListFromJsonArrayAndSenzById(reader, senzesById);
                else
                    tmp = readBeaconSenzIdPairArrayListFromJsonArray(reader);
            }
            else {
                reader.skipValue();
            }
        }
        reader.endObject();
        reader.close();

        return bwss;
    }

    private interface QueryWriter {
        public void write(OutputStream os) throws IOException;
    }

    private interface ResultReader<T> {
        public T read(InputStream is) throws IOException;
    }

    // It's the main function for sending http request.
    public static <T> T doQuery(URL url, QueryWriter w, ResultReader<T> r) throws IOException {
        // According to url's type, url.openConnection will return different object of URLConnection's subclass.
        // Here it will return a object of HttpURLConnection, because of url's head is "http".
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        // Set http's header.
        urlConnection.setConnectTimeout(timeout);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("charset", "utf-8");
        urlConnection.setRequestProperty("X-AVOSCloud-Application-Id", AVOS_ID);
        urlConnection.setRequestProperty("X-AVOSCloud-Application-Key", AVOS_KEY);
        urlConnection.setRequestProperty("X-AVOSCloud-Application-Production", "0");
        T t = null;

        try {
            // writer is not allowed be null.
            if (w != null) {
                // Url's connection can be used to output(or input), if you want the connection output, then set it true.
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);
            }
            // Write the sending message.
            w.write(urlConnection.getOutputStream());
            // Read the receiving message.
            t = r.read(urlConnection.getInputStream());
        }
        finally {
            urlConnection.disconnect();
        }

        return t;
    }

    // Query with Beacons info.
    public static ArrayList<BeaconWithSenz> queryBeacons(final Collection<Beacon> toQuery, final Location lastBeen) throws IOException {
        return doQuery(
                new URL(queryUrl + "beacons"),
                new QueryWriter() {
                    @Override
                    // This callback will write the location and beacons info into os.
                    public void write(OutputStream os) throws IOException {
                        // Init the StringWriter sized fo 100
                        StringWriter sw = new StringWriter(100);
                        // Write the beacons info and location into StringWriter.
                        writeBeaconsQueryPost(new JsonWriter(sw), toQuery, lastBeen);
                        L.i("[Network] The sending message is: " + sw.toString());
                        // Write location and beacons info into a JsonWriter,
                        // which Creates a new instance that writes a JSON-encoded stream to os.
                        // The os will return to be the post's para
                        writeBeaconsQueryPost(new JsonWriter(new OutputStreamWriter(os)), toQuery, lastBeen);
                    }
                },
                new ResultReader<ArrayList<BeaconWithSenz>>() {
                    @Override
                    public ArrayList<BeaconWithSenz> read(InputStream is) throws IOException {
                        return readResult(new JsonReader(new InputStreamReader(is)));
                    }
                });
    }

    // Query with Location info.
    public static ArrayList<BeaconWithSenz> queryLocation(final Location location) throws IOException {
        return doQuery(
                new URL(queryUrl + "beacons"),
                new QueryWriter() {
                    @Override
                    public void write(OutputStream os) throws IOException {
                        // Init the StringWriter sized fo 100
                        StringWriter sw = new StringWriter(100);
                        // Write the beacons info and location into StringWriter.
                        writeLocationQueryPost(new JsonWriter(sw), location);
                        L.i("[Network] The 'message' is: " + sw.toString());
                        // Write location info into a JsonWriter,
                        // which Creates a new instance that writes a JSON-encoded stream to os.
                        // The os will return to be the post's para
                        writeLocationQueryPost(new JsonWriter(new OutputStreamWriter(os)), location);
                    }
                },
                new ResultReader<ArrayList<BeaconWithSenz>>() {
                    @Override
                    public ArrayList<BeaconWithSenz> read(InputStream is) throws IOException {
                        return readResult(new JsonReader(new InputStreamReader(is)));
                    }
                });
    }

    public static class ResultNotPresentException extends IOException {
    }
}
