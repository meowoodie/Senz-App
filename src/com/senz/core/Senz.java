package com.senz.core;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import android.os.Parcelable;
import android.os.Parcel;
import android.util.JsonReader;
import android.util.JsonWriter;
import com.senz.utils.L;
import com.senz.utils.Jsonable;
import com.senz.core.Utils;

public class Senz implements Parcelable, Jsonable {
    private String mId;
    private String mDoing;
    private String mWhile;
    private String mWhen;
    private String mWhere;
    private String mWith_somebody;
    private String mWith_a_mood;
    private HashMap<String, String> mEntities;


    @Override
    public int hashCode() {
        return this._id().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof Senz)
            return this._id().equals(((Senz) obj)._id());

        return false;
    }

    public String _id() {
        return mId;
    }

    public String _doing() {
        return mDoing;
    }

    public String _when() {
        return mWhen;
    }

    public String _while() {
        return mWhile;
    }

    public String _where() {
        return mWhere;
    }

    public String _somebody() {
        return mWith_somebody;
    }

    public String _mood() {
        return mWith_a_mood;
    }

    public Map<String, String> entities() {
        return mEntities;
    }

    public int describeContents() {
        return 0;
    }

    // Parcelable for serialization

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this._id());
        out.writeString(this._doing());
        out.writeString(this._while());
        out.writeString(this._when());
        out.writeString(this._where());
        out.writeString(this._somebody());
        out.writeString(this._mood());
        Utils.writeParcelStringMap(out, this.entities());
    }

    public Senz(Parcel in) {
        String what;

        this.mId = in.readString();
        this.mDoing = in.readString();
        this.mWhile = in.readString();
        this.mWhen = in.readString();
        this.mWhere = in.readString();
        this.mWith_somebody = in.readString();
        this.mWith_a_mood = in.readString();
        this.mEntities = new HashMap<String, String>();
        Utils.readParcelStringMap(this.mEntities, in);
    }

    public static final Parcelable.Creator<Senz> CREATOR
            = new Parcelable.Creator<Senz> () {
        public Senz createFromParcel(Parcel in) {
            return new Senz(in);
        }

        public Senz[] newArray(int size) {
            return new Senz[size];
        }
    };

    // Json for serialization

    @Override
    public void writeToJson(JsonWriter writer) throws IOException {
        writer.name("id").value(this._id());
        writer.name("doing").value(this._doing());
        writer.name("while").value(this._while());
        writer.name("when").value(this._when());
        writer.name("where").value(this._where());
        writer.name("with_somebody").value(this._somebody());
        writer.name("with_a_mood").value(this._mood());
        Utils.writeStringMapAsJsonObject(writer, this.entities());
    }

    public Senz(JsonReader reader) throws IOException {
        String what = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String s = reader.nextName();
            if (s.equals("id")) {
                this.mId = reader.nextString();

            } else if (s.equals("doing")) {
                this.mDoing = reader.nextString();

            } else if (s.equals("while")) {
                this.mWhile = reader.nextString();

            } else if (s.equals("when")) {
                this.mWhen = reader.nextString();

            } else if (s.equals("where")) {
                this.mWhere = reader.nextString();

            } else if (s.equals("with_somebody")) {
                this.mWith_somebody = reader.nextString();

            } else if (s.equals("with_a_mood")) {
                this.mWith_a_mood = reader.nextString();

            }  else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }

    public static Senz fromJson(JsonReader reader) throws IOException {
        return new Senz(reader);
    }

    public static final Jsonable.Creator<Senz> JsonCREATOR
            = new Jsonable.Creator<Senz>() {
        @Override
        public Senz createFromJson(JsonReader in) throws IOException {
            return Senz.fromJson(in);
        }
    };


    public static void writeSenzIdArray(JsonWriter writer, List<Senz> senzes) throws IOException{
        writer.beginArray();
        for (Senz senz : senzes) {
            writer.value(senz._id());
        }
        writer.endArray();
    }

    public static ArrayList<Senz> senzesFromJsonIdArray(JsonReader reader, Map<String, Senz> senzesById) throws IOException {
        ArrayList<Senz> senzes = new ArrayList<Senz>();
        reader.beginArray();
        while (reader.hasNext()) {
            String id = reader.nextString();
            if (senzesById.containsKey(id))
                senzes.add(senzesById.get(id));
        }
        reader.endArray();
        return senzes;
    }
}

/*
public class Senz implements Parcelable, Jsonable {
    private String mId;
    private String mType;
    private String mSubType;
    private HashMap<String, String> mEntities;


    @Override
    public int hashCode() {
        return this.id().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof Senz)
            return this.id().equals(((Senz) obj).id());

        return false;
    }

    public String id() {
        return mId;
    }

    public String type() {
        return mType;
    }

    public String subType() {
        return mSubType;
    }

    public Map<String, String> entities() {
        return mEntities;
    }

    public int describeContents() {
        return 0;
    }

    // Parcelable for serialization

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.id());
        out.writeString(this.type());
        out.writeString(this.subType());
        Utils.writeParcelStringMap(out, this.entities());
    }

    public Senz(Parcel in) {
        String what;

        this.mId = in.readString();
        this.mType = in.readString();
        this.mSubType = in.readString();
        this.mEntities = new HashMap<String, String>();
        Utils.readParcelStringMap(this.mEntities, in);
    }

    public static final Parcelable.Creator<Senz> CREATOR
            = new Parcelable.Creator<Senz> () {
        public Senz createFromParcel(Parcel in) {
            return new Senz(in);
        }

        public Senz[] newArray(int size) {
            return new Senz[size];
        }
    };

    // Json for serialization

    @Override
    public void writeToJson(JsonWriter writer) throws IOException {
        writer.name("id").value(this.id());
        writer.name("type").value(this.type());
        writer.name("subType").value(this.subType());
        writer.name("content");
        Utils.writeStringMapAsJsonObject(writer, this.entities());
    }

    public Senz(JsonReader reader) throws IOException {
        String what = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String s = reader.nextName();
            if (s.equals("objectId")) {
                this.mId = reader.nextString();

            } else if (s.equals("type")) {
                this.mType = reader.nextString();

            } else if (s.equals("subType")) {
                this.mSubType = reader.nextString();

            } else if (s.equals("entities")) {
                this.mEntities = new HashMap<String, String>();
                Utils.readJsonStringMap(reader, this.mEntities);

            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }

    public static Senz fromJson(JsonReader reader) throws IOException {
        return new Senz(reader);
    }

    public static final Jsonable.Creator<Senz> JsonCREATOR
        = new Jsonable.Creator<Senz>() {
            @Override
            public Senz createFromJson(JsonReader in) throws IOException {
                return Senz.fromJson(in);
            }
        };


    public static void writeSenzIdArray(JsonWriter writer, List<Senz> senzes) throws IOException{
        writer.beginArray();
        for (Senz senz : senzes) {
            writer.value(senz.id());
        }
        writer.endArray();
    }

    public static ArrayList<Senz> senzesFromJsonIdArray(JsonReader reader, Map<String, Senz> senzesById) throws IOException {
        ArrayList<Senz> senzes = new ArrayList<Senz>();
        reader.beginArray();
        while (reader.hasNext()) {
            String id = reader.nextString();
            if (senzesById.containsKey(id))
                senzes.add(senzesById.get(id));
        }
        reader.endArray();
        return senzes;
    }
}
*/
