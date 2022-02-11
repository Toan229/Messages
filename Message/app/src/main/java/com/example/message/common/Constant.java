package com.example.message.common;

public class Constant {
    public static  final String DATABASE_NAME ="tapbi_sample.realm";
    public static  final String DB_NAME ="Messages";
    public static  final int DB_VERSION = 1;
    public static  final String BASE_URL ="https://dataservice.accuweather.com/";
    public static final String API_KEY = "kFElmaWjtFptSPRPI8udMhgb3wfCpbLW";
    public static  final int CONNECT_S =30;
    public static  final int READ_S =30;
    public static  final int WRITE_S =30;

    public static  final String  LANGUAGE_EN ="en";
    public static  final String  LANGUAGE_VN ="vi";

    /*Param fragment*/
    public static final String ARGUMENT_FRAGMENT_MESSAGE_ID="ARGUMENT_FRAGMENT_MESSAGE_ID";
    public static final String ARGUMENT_FRAGMENT_MESSAGE="ARGUMENT_FRAGMENT_MESSAGE";
    public static final String ARGUMENT_FRAGMENT_MESSAGE_TITLE="ARGUMENT_FRAGMENT_MESSAGE_TITLE";
    /*SharePreference constant*/
    public static final String USER_NAME="USER_NAME";
    public static final String LOAD_ALL_SMS_FIRST_TIME="LOAD_ALL_SMS_FIRST_TIME";
    public static final String PREF_SETTING_LANGUAGE="pref_setting_language";

   /*switch type db, this only use in this sample*/
    public static  final int  DB_TYPE_ROOM =1;
    public static  final int  DB_TYPE_REALM =2;
    public static  final int  DB_TYPE =DB_TYPE_ROOM;

    /*event constant*/
    public static  final int  EVENT_BACK_PREVIOUS_SCREEN =1;
    public static  final int  EVENT_SHOW_TOAST =2;
    public static  final int  EVENT_CHANGE_CONFIG =3;

    /** type contact*/
    public static final String CONTACT_NAME = "name";
    public static final String CONTACT_PHONE_NUMBER = "phoneNumber";

    /** backstack fragment name */
    public static final String FRAGMENT_DETAIL = "detail";
    public static final String FRAGMENT_POLICY = "policy";
    public static final String FRAGMENT_CHAT = "chat";
    public static final String FRAGMENT_EDIT_CONTACT = "edit_contact";

    /** message column*/
    public static final String MESSAGE_CONTENT = "content://sms";
    public static final String DEFAULT_MESSAGE_ID_COLUMN = "_id";
    public static final String DEFAULT_MESSAGE_ADDRESS_COLUMN = "address";
    public static final String DEFAULT_MESSAGE_BODY_COLUMN = "body";
    public static final String DEFAULT_MESSAGE_READ_COLUMN = "read";
    public static final String DEFAULT_MESSAGE_TYPE_COLUMN = "type";
    public static final String DEFAULT_MESSAGE_DATE_COLUMN = "date";

    /** pattern date format */
    public static final String HOUR_PATTERN = "HH:mm";
    public static final String HOUR_PATTERN_1 = "h:mm a";

    /** type sms*/
    public static final String BUNDLE_SMS_LIST = "smsList";
    public static final String SENT_SMS = "sent";
    public static final String INBOX_SMS = "inbox";
    public static final String READ_STATE = "1";
    public static final String UNREAD_STATE = "0";
    public static final String ACTION_RECEIVE_SMS = "android.provider.Telephony.SMS_RECEIVED";

    /**Share reference*/
    public static final String IS_FIRST_LOAD =  "IS_FIRST_LOAD";
    public static final String IS_FIRST_LOAD_SMS =  "IS_FIRST_LOAD_SMS";
    public static final String IS_FIRST_LOAD_CONTACT =  "IS_FIRST_LOAD_CONTACT";

    /**Setting reference*/
    public static final String IS_BACKGROUND_A_COLOR = "is_color";
    public static final String BACKGROUND_IMAGE = "backgroundImage";
    public static final String BACKGROUND_COLOR = "backgroundColor";
    public static final String FONT = "font";
    public static final String DEFAULT_CONTACT_IMAGE = "custom";

    //Setting item
    public static String CHAT_WALLPAPER_SETTING = "Chat Wallpaper";
    public static String FONT_SETTING = "Font";
    public static String PRIVACY_POLICY_SETTING = "Privacy Policy";
    public static String RATE_APP_SETTING = "Rate App";
    public static String FEED_BACK_SETTING = "Feedback";

    /**Regex pattern*/
    public static final String REGEX_EXCLUSIVE_SPECIAL_CHARACTER = "(\\.|,|-| )+";
    public static final String REGEX_IS_NOT_NUMBER = "\\D+";
//    public static final String REGEX_VIETNAMESE_PHONE_NUMBER = "[(\\+84)|(84)|(0084)][1-9]*";

    public static final String LINK_SHARE_APP = "https://play.google.com/store/apps/details?id=";

}
