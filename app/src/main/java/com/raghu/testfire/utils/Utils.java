package com.raghu.testfire.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

public final class Utils {

    public static void sendSMS(Context cxt, String smsBody) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"));
        intent.putExtra("sms_body", smsBody);
        cxt.startActivity(intent);
    }

    public static void forwardToDial(Activity activity, String phoneNumber) {
        if (activity != null && !TextUtils.isEmpty(phoneNumber))
            activity.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
    }

    /*   public static void callPhone(Activity activity, String phoneNumber) {
           if (activity != null && !TextUtils.isEmpty(phoneNumber)) {
               if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                   return;
               activity.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber)));
           }
       }
   */
    public static void sendMail(Context mContext, String mailID) {
        mContext.startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + mailID)));
    }

    public static void openWeb(Context context, String url) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public static void openContacts(Activity context, int requestCode) {
        Uri uri = Uri.parse("content://contacts/people");
        context.startActivityForResult(new Intent(Intent.ACTION_PICK, uri), requestCode);
    }

    public static void openSettings(Activity context, String action) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.android.settings", action);
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivityForResult(intent, 0);
    }

    public static void hideKeyBoard(Activity aty) {
        ((InputMethodManager) aty.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(aty.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void goHome(Context context) {
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(mHomeIntent);
    }

}