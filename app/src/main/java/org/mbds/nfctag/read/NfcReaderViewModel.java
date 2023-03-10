package org.mbds.nfctag.read;

import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Parcelable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.mbds.nfctag.model.TagContent;
import org.mbds.nfctag.model.TagType;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class NfcReaderViewModel extends ViewModel {

    private MutableLiveData<List<TagContent>> onTagRead = new MutableLiveData<>();
    private MutableLiveData<Exception> onReadFailed = new MutableLiveData<>();

    public LiveData<Exception> getReadFailed() {
        return onReadFailed;
    }

    public LiveData<List<TagContent>> getTagRead() {
        return onTagRead;
    }

    private String getTextFromNdefRecord(NdefRecord ndefRecord) {
        return null;
    }

    private TagType getTypeFromNdefRecord(NdefRecord ndefRecord) {
        //check NDEF record TNF:
        //======================

        if (ndefRecord.toUri() != null) {
            Uri uri = ndefRecord.toUri();
            switch (uri.getScheme()) {
                case "tel":
                    return TagType.PHONE;
                case "http":
                case "https":
                    return TagType.URL;
            }
        }
        return TagType.TEXT;


    }

    private String getNdefContent(NdefRecord ndefRecord) throws UnsupportedEncodingException {

        //parse NDEF record as String:
        //============================
        if (ndefRecord.toUri() != null) {
            return ndefRecord.toUri().toString();
        } else {
            byte[] payload = ndefRecord.getPayload();
            String encoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTf-8";
            int languageSize = payload[0] & 0063;
            return new String(payload, languageSize + 1,
                    payload.length - languageSize - 1, encoding);
        }
    }

    public void processNfcTag(Parcelable[] rawMsgs) {

        if (rawMsgs != null && rawMsgs.length != 0) {
            List<TagContent> tagContents = new ArrayList<>();

            // instantiate a NDEF message array to get NDEF records
            NdefMessage[] ndefMessage = new NdefMessage[rawMsgs.length];
            // loop to get the NDEF records
            for (int i = 0; i < rawMsgs.length; i++) {
                ndefMessage[i] = (NdefMessage) rawMsgs[i];
                for (int j = 0; j < ndefMessage[i].getRecords().length; j++) {
                    NdefRecord ndefRecord = ndefMessage[i].getRecords()[j];
                    try {
                        tagContents.add(new TagContent(getNdefContent(ndefRecord), getTypeFromNdefRecord(ndefRecord)));
                        onTagRead.setValue(tagContents);
                    } catch (UnsupportedEncodingException e) {
                        onReadFailed.setValue(e);
                    }
                }
            }
        } else {
            onReadFailed.setValue(new Exception("No NDEF message found!"));
        }
    }
}