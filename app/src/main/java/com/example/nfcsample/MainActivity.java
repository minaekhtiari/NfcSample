package com.example.nfcsample;


import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nfcsample.ReadCardFeatures.NdefMessageParser;
import com.example.nfcsample.ReadCardFeatures.ParsedNdefRecord;
import com.example.nfcsample.model.Card;
import com.example.nfcsample.utils.NFCUtils;


import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.nfcsample.ReadCardFeatures.DumpTagData.dumpTagData;

public class MainActivity extends AppCompatActivity
       {

    private TextView text;

    NFCUtils nfcUtils;
    Application application;
    Card card;
           CardListAdapter cardListAdapter;

    private Toolbar mToolbar;
    private LinearLayout mCardReadyContent;
    private TextView mPutCardContent;
    private TextView mCardNumberText;
    private TextView mExpireDateText;
    private ImageView mCardLogoIcon;
    private NfcAdapter mNfcAdapter;
    private AlertDialog mTurnNfcDialog;
    private ProgressDialog mProgressDialog;
    private String mDoNotMoveCardMessage;
    private String mUnknownEmvCardMessage;
    private String mCardWithLockedNfcMessage;
    private boolean mIsScanNow;
    private boolean mIntentFromCreate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.text);

        //RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()
                , LinearLayoutManager.VERTICAL, false));

   //   new com.example.nfcsample.CardListAdapter(this)

       // final CardListAdapter adapter = new CardListAdapter(this);
    //    recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mCardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
     //   mCardViewModel.getAllCards().observe(this, new Observer<List<Card>>() {
//            @Override
//            public void onChanged(List<Card> cards) {
//            ///    adapter.setCards(cards);
//           //     adapter.notifyDataSetChanged();
//
//            }
//        });

        nfcUtils=new NFCUtils(this);
      //  nfcAdapter = NfcAdapter.getDefaultAdapter(this);

//        if (nfcAdapter == null) {
//            Toast.makeText(this, "No NFC", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        pendingIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, this.getClass())
//                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;

            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }

            } else {
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
                msgs = new NdefMessage[] {msg};

            }

            displayMsgs(msgs);

        }
    }

    private void displayMsgs(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0)
            return;

        StringBuilder builder = new StringBuilder();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();

        for (int i = 0; i < size; i++) {
            ParsedNdefRecord record = records.get(i);
            String str = record.str();
            builder.append(str).append("\n");
        }

       // text.setText(builder.toString());

        //todo //Inject instance   ???
        card=new Card(builder.toString());
       // mCardViewModel.insert(card);
    }

    @Override
    protected void onResume() {
        super.onResume();
      if(!NFCUtils.isNfcEnabled(MainActivity.this)
                && (NFCUtils.isNfcAvailable(this)));
        {
            nfcUtils.enableDispatch();
//            nfcAdapter.enableForegroundDispatch(
//                    this, pendingIntent, null, null);
        }
//        if (nfcAdapter != null) {
//            if (!nfcAdapter.isEnabled())
//                showWirelessSettings();
//  nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
//
//        }
    }


//    @Override
//    public void startNfcReadCard() {
//        mIsScanNow = true;
//     //   mProgressDialog.show();
//    }
//
//    @Override
//    public void cardIsReadyToRead() {
//    //    mPutCardContent.setVisibility(View.GONE);
//    //    mCardReadyContent.setVisibility(View.VISIBLE);
//        String card = mCardNfcAsyncTask.getCardNumber();
//        card = getPrettyCardNumber(card);
//
//        String expiredDate = mCardNfcAsyncTask.getCardExpireDate();
//        String cardType = mCardNfcAsyncTask.getCardType();
//        text.setText(cardType);
//      //  mCardNumberText.setText(card);
//      //  mExpireDateText.setText(expiredDate);
//        parseCardType(cardType);
//    }
//
//    @Override
//    public void doNotMoveCardSoFast() {
//
//    }
//
//    @Override
//    public void unknownEmvCard() {
//
//    }
//
//    @Override
//    public void cardWithLockedNfc() {
//
//    }
//
//    @Override
//    public void finishNfcReadCard() {
//
//    }
//    private void parseCardType(String cardType){
//        if (cardType.equals(CardNfcAsyncTask.CARD_UNKNOWN)){
////            Snackbar.make(mToolbar, getString(R.string.snack_unknown_bank_card), Snackbar.LENGTH_LONG)
////                    .setAction("GO", new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            goToRepo();
////                        }
////                    });
//        } else if (cardType.equals(CardNfcAsyncTask.CARD_VISA)){
//            mCardLogoIcon.setImageResource(R.drawable.visa_logo);
//        } else if (cardType.equals(CardNfcAsyncTask.CARD_MASTER_CARD)){
//            mCardLogoIcon.setImageResource(R.drawable.master_logo);
//        }
//    }
//
//    private String getPrettyCardNumber(String card){
//        String div = " - ";
//        return  card.substring(0,4) + div + card.substring(4,8) + div + card.substring(8,12)
//                +div + card.substring(12,16);
//    }


}