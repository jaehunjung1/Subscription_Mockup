package com.jayjung.subscription_mockup;

import android.graphics.drawable.Drawable;

public class NotiContainer {
    public Drawable smallIcon, largeIcon;
    public String contentTitle, contentText;
    public String channelName, channelDesc;

    public NotiContainer(Drawable smallI, Drawable largeI,
                         String title, String text,
                         String name, String desc) {
        this.smallIcon = smallI;
        this.largeIcon = largeI;
        this.contentTitle = title;
        this.contentText = text;
        this.channelName = name;
        this.channelDesc = desc;
    }

}
