package com.google.android.mms.pdu;

import android.content.Intent;
import android.os.Bundle;
import android.test.AndroidTestCase;

public class PduParserAndroidTest extends AndroidTestCase {

    public static final byte[] PDU_DATA = new byte[]{-116, -126, -104, 109, 97, 118, 111, 100, 105, 45, 55, 45, 56, 57, 45, 49, 55, 57, 45, 50, 45, 99, 98, 45, 55, 45, 53, 51, 45, 51, 45, 99, 98, 45, 50, 54, 56, 51, 99, 56, 57, 0, -115, -110, -119, 18, -128, 16, -125, 111, 64, 111, 99, 97, 114, 108, 115, 101, 110, 46, 99, 111, 109, 0, -106, 82, 101, 58, 32, 77, 109, 115, 32, 83, 117, 98, 106, 101, 99, 116, 0, -118, -128, -114, 2, 1, 17, -120, 5, -127, 3, 3, -12, -128, -125, 104, 116, 116, 112, 58, 47, 47, 97, 116, 108, 50, 109, 111, 115, 103, 101, 116, 46, 109, 115, 103, 46, 101, 110, 103, 46, 116, 45, 109, 111, 98, 105, 108, 101, 46, 99, 111, 109, 47, 109, 109, 115, 47, 119, 97, 112, 101, 110, 99, 63, 84, 61, 109, 97, 118, 111, 100, 105, 45, 55, 45, 49, 51, 98, 45, 53, 51, 45, 51, 45, 99, 98, 45, 50, 54, 56, 51, 99, 56, 57, 0};
    public static final String KEY = "data";

    private Intent mmsIntent = new Intent();

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Bundle extras = new Bundle();
        extras.putByteArray(KEY, PDU_DATA);

        // Simulate how a BroadcastReceiver would receive data.
        mmsIntent.setType("application/vnd.wap.mms-message");
        mmsIntent.putExtras(extras);
    }

    public void testParse() throws Exception {

        Bundle extras = mmsIntent.getExtras();
        byte[] data = extras.getByteArray(KEY);

        GenericPdu mms = new PduParser(data).parse();
        String mmsFrom = new String(mms.getFrom().getTextString());
        int mmsMessageType = mms.getMessageType();
        int mmsMmsVersion = mms.getMmsVersion();

        assertTrue(mmsFrom.equals("o@ocarlsen.com"));
        assertTrue(mmsMessageType == 130);
        assertTrue(mmsMmsVersion == 18);

        NotificationInd ni = (NotificationInd) mms;
        String niTransactionId = new String(ni.getTransactionId());
        String niSubject = new String(ni.getSubject().getTextString());
        int niContentClass = ni.getContentClass();
        String niContentLocation = new String(ni.getContentLocation());
        int niDeliveryReport = ni.getDeliveryReport();

        assertTrue(niTransactionId.equals("mavodi-7-89-179-2-cb-7-53-3-cb-2683c89"));  // Use this to look up MMS in database.
        assertTrue(niSubject.equals("Re: Mms Subject"));
        assertTrue(niContentClass == 0);
        assertTrue(niContentLocation.equals("http://atl2mosget.msg.eng.t-mobile.com/mms/wapenc?T=mavodi-7-13b-53-3-cb-2683c89"));
        assertTrue(niDeliveryReport == 0);
    }
}