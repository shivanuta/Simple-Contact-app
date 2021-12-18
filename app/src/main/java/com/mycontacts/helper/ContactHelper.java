package com.mycontacts.helper;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class ContactHelper {

    /**
     * Getting contacts from device
     * @param contactHelper - to fetch the contacts from device
     * @return
     */
    public static Cursor getContactCursor(ContentResolver contactHelper) {
        String[] projection = new String[]{Phone._ID, Phone.DISPLAY_NAME, Phone.NUMBER};
        Cursor cur = null;
        try {
            cur = contactHelper.query( Phone.CONTENT_URI, projection, null, null,
                        Phone.DISPLAY_NAME
                                + " ASC");
            cur.moveToFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cur;
    }
}
