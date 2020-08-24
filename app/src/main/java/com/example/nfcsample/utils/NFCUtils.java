package com.example.nfcsample.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;

public  class NFCUtils {

    public static boolean isNfcAvailable(final Context context) {
        boolean nfcAvailable = true;
        try {
            NfcAdapter adapter = NfcAdapter.getDefaultAdapter(context);
            if (adapter == null) {
                nfcAvailable = false;
            }
        } catch (UnsupportedOperationException e) {
            nfcAvailable = false;
        }
        return nfcAvailable;
    }


    public static boolean isNfcEnabled(final Context pContext) {
        boolean nfcEnabled;
        try {
            NfcAdapter adapter = NfcAdapter.getDefaultAdapter(pContext);
            nfcEnabled = adapter.isEnabled();
        } catch (UnsupportedOperationException e) {
            nfcEnabled = false;
        }
        return nfcEnabled;
    }


    private final NfcAdapter mNfcAdapter;

    private final PendingIntent mPendingIntent;

    private final Activity mActivity;


    private static final IntentFilter[] INTENT_FILTER = new IntentFilter[]
            { new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED) };


    private static final String[][] TECH_LIST = new String[][]
            { { IsoDep.class.getName() } };


    public NFCUtils(final Activity pActivity) {
        mActivity = pActivity;
        mNfcAdapter = NfcAdapter.getDefaultAdapter(mActivity);
        mPendingIntent = PendingIntent.getActivity(mActivity, 0,
                new Intent(mActivity, mActivity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }


    void disableDispatch() {
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(mActivity);
        }
    }


    public void enableDispatch() {
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(mActivity, mPendingIntent, INTENT_FILTER, TECH_LIST);
        }
    }


    public NfcAdapter getmNfcAdapter() {
        return mNfcAdapter;
    }
}
