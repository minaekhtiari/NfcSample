package com.example.nfcsample

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nfcsample.ReadCardFeatures.DumpTagData
import com.example.nfcsample.ReadCardFeatures.NdefMessageParser
import com.example.nfcsample.model.Card
import com.example.nfcsample.utils.NFCUtils

class MainActivity2 : AppCompatActivity() {

    private var pendingIntent: PendingIntent?=null
    var nfcAdapter: NfcAdapter? = null
    private var text: TextView? = null
    var card: Card? = null
    var nfcUtils: NFCUtils?=null
    private lateinit var cardViewModel: CardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = CardListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        text = findViewById<View>(R.id.text) as TextView
        nfcUtils = NFCUtils(this)
//         nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//
//        if (nfcAdapter == null) {
//            Toast.makeText(this, "No NFC", Toast.LENGTH_SHORT).show()
//
//            return
//        }
//
//         pendingIntent = PendingIntent.getActivity(this, 0,
//                 Intent(this, this.javaClass)
//                         .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        cardViewModel = ViewModelProvider(this).get(CardViewModel::class.java)
       cardViewModel.allCards.observe(this, Observer { cards ->

           cards?.let {
               adapter.setCards(it)
               adapter.notifyDataSetChanged()
           }
       })
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (intent != null) {
            resolveIntent(intent)
        }
    }

    private fun resolveIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action || NfcAdapter.ACTION_TECH_DISCOVERED == action || NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val msgs: Array<NdefMessage?>
            if (rawMsgs != null) {
                msgs = arrayOfNulls(rawMsgs.size)
                for (i in rawMsgs.indices) {
                    msgs[i] = rawMsgs[i] as NdefMessage
                }
            } else {
                val empty = ByteArray(0)
                val id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)
                val tag = intent.getParcelableExtra<Parcelable>(NfcAdapter.EXTRA_TAG) as Tag?
                val payload = DumpTagData.dumpTagData(tag).toByteArray()
                val record = NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload)
                val msg = NdefMessage(arrayOf(record))
                msgs = arrayOf(msg)
            }
            displayMsgs(msgs)
        }
    }

    private fun displayMsgs(msgs: Array<NdefMessage?>) {

        if (msgs == null || msgs.size == 0) return
        val builder = StringBuilder()
        val records = NdefMessageParser.parse(msgs[0])
        val size = records.size
        for (i in 0 until size) {
            val record = records[i]
            val str = record.str()
            builder.append(str).append("\n")
        }
        text?.setText(builder.toString())


        card = Card(builder.toString())
      cardViewModel.insert(card!!)
    }
    override fun onResume() {
        super.onResume()

        if (nfcAdapter != null) {
            if (!nfcAdapter!!.isEnabled())

            nfcAdapter!!.enableForegroundDispatch(this, pendingIntent, null, null);

        }
    }

}