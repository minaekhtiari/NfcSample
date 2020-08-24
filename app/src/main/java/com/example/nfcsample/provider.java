package com.example.nfcsample;


import android.nfc.tech.IsoDep;

import com.github.devnied.emvnfccard.exception.CommunicationException;
import com.github.devnied.emvnfccard.parser.IProvider;

import java.io.IOException;

public class provider implements IProvider{
    private IsoDep mTagCom;

        @Override
        public byte[] transceive(final byte[] pCommand) throws CommunicationException {

            byte[] response = null;
            try {
                // send command to emv card
                response = mTagCom.transceive(pCommand);
            } catch (IOException e) {
                throw new CommunicationException(e.getMessage());
            }

            return response;
        }

    @Override
    public byte[] getAt() {
        return new byte[0];
    }

    public void setmTagCom(final IsoDep mTagCom) {
            this.mTagCom = mTagCom;
        }
}
