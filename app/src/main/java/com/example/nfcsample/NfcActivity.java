package com.example.nfcsample;

import android.os.Bundle;

import com.example.nfcsample.utils.NFCUtils;
import com.github.devnied.emvnfccard.exception.CommunicationException;
import com.github.devnied.emvnfccard.model.EmvCard;
import com.github.devnied.emvnfccard.parser.EmvTemplate;
import com.github.devnied.emvnfccard.parser.IProvider;

import androidx.appcompat.app.AppCompatActivity;

public class NfcActivity extends AppCompatActivity {

    private NFCUtils nfcUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
     ///   nfcUtils=new NFCUtils(this);
        // Create provider
        IProvider provider = new provider();
// Define config
        EmvTemplate.Config config = EmvTemplate.Config()
                .setContactLess(true) // Enable contact less reading (default: true)
                .setReadAllAids(true) // Read all aids in card (default: true)
                .setReadTransactions(true) // Read all transactions (default: true)
                .setRemoveDefaultParsers(false) // Remove default parsers for GeldKarte and EmvCard (default: false)
                .setReadAt(true) // Read and extract ATR/ATS and description
                ;
// Create Parser
        EmvTemplate parser = EmvTemplate.Builder() //
                .setProvider(provider) // Define provider
                .setConfig(config) // Define config
                //.setTerminal(terminal) (optional) you can define a custom terminal implementation to create APDU
                .build();

// Read card
        try {
            EmvCard card = parser.readEmvCard();
        } catch (CommunicationException e) {
            e.printStackTrace();
        }
    }
}