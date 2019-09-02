package com.jayjung.subscription_mockup;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class NotiContainer implements Parcelable {
    public Drawable smallIcon, largeIcon;
    public String contentTitle, contentText;
    public String channelName, channelDesc;

    public NotiContainer(String title, String text, String name) {
//        this.smallIcon = smallI;
//        this.largeIcon = largeI;
        this.contentTitle = title;
        this.contentText = text;
        this.channelName = name;
    }

    protected NotiContainer(Parcel in) {
        contentTitle = in.readString();
        contentText = in.readString();
        channelName = in.readString();
//        channelDesc = in.readString();
    }

    public static final Creator<NotiContainer> CREATOR = new Creator<NotiContainer>() {
        @Override
        public NotiContainer createFromParcel(Parcel in) {
            return new NotiContainer(in);
        }

        @Override
        public NotiContainer[] newArray(int size) {
            return new NotiContainer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(contentTitle);
        parcel.writeString(contentText);
        parcel.writeString(channelName);
//        parcel.writeString(channelDesc);
    }
}
