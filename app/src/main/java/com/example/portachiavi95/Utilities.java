package com.example.portachiavi95;

import android.content.Context;
import android.content.SharedPreferences;

public class Utilities {

    public static final int PWD_MIN_LENGTH = 8;
    public static final int ADD_OR_EDIT_CODE = 1000; // numero arbitrario
    public static final int LOGIN_CODE = 1001;

    public static final String MAIL_COL = "mail";

    private static final String DEFAULT_EMPTY_MASTER_PWD = "";
    private static final String MASTER_PWD_OPTION_KEY = "masterPassword";
    private static final String ACCESS_KETP_OPTION_KEY = "access";

    // TODO
    //    controllare, al primo accesso, se master pwd esiste
    //    fare salvataggio master pdw (dentro utilities)
    //    sapere se dobbiamo manterene l'accesso, fare il controllo, e salvare preferenza utente
    //    useremo shared preferences

    public static boolean isMasterPasswordSet(Context context) {

        boolean exists = false;

        String masterPwd = getMasterPassword(context);

        exists = !masterPwd.equals(DEFAULT_EMPTY_MASTER_PWD);

        return exists;
    }

    public static boolean isMasterPasswordCorrect(Context context, String pass) {

        String masterPwd = getMasterPassword(context);

        return masterPwd.equals(pass);
    }

    private static String getMasterPassword(Context context) {

        // controllo nelle shared preferences
        SharedPreferences userData = getSharedPreferences(context);

        String masterPwd = userData.getString(MASTER_PWD_OPTION_KEY, DEFAULT_EMPTY_MASTER_PWD);

        return masterPwd;
    }

    public static boolean isAccessKept(Context context) {

        SharedPreferences userData = getSharedPreferences(context);

        boolean isAccessKept = false;

        isAccessKept = userData.getBoolean(ACCESS_KETP_OPTION_KEY, false);

        return isAccessKept;
    }

    public static void saveAccess(Context context, boolean access) {

        setPreference(context, ACCESS_KETP_OPTION_KEY, access);
    }

    public static void saveMasterPassword(Context context, String pass) {

        setPreference(context, MASTER_PWD_OPTION_KEY, pass);
    }

    private static SharedPreferences getSharedPreferences(Context context) {

        SharedPreferences userData = context.getSharedPreferences(context.getString(R.string.USER_DATA), Context.MODE_PRIVATE);
        return userData;
    }

    private static void setPreference(Context context, String key, boolean value) {

        SharedPreferences userData = getSharedPreferences(context);

        userData.edit().putBoolean(key, value).commit(); // come non dimenticarsi il commit?

    }

    private static void setPreference(Context context, String key, String value) {

        SharedPreferences userData = getSharedPreferences(context);

        userData.edit().putString(key, value).commit(); // come non dimenticarsi il commit?
    }
}
