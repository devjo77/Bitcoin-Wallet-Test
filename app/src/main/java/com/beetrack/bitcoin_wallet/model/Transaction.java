package com.beetrack.bitcoin_wallet.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.beetrack.bitcoin_wallet.utils.DateFormatUtil;

@Entity(tableName = "transaction",
        indices = {@Index(value = {"hash"}, unique = true)})
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int mId;

    @ColumnInfo(name = "hash")
    @NonNull
    private String mHash;

    @ColumnInfo(name = "date")
    private String mDate;

    @ColumnInfo(name = "value")
    private String mValue;

    public Transaction(String date, String value, String hash) {
        this.mDate = date;
        this.mValue = value;
        this.mHash = hash;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getDate() {
        return DateFormatUtil.formatDate(mDate);
    }

    public String getValue() {
        return mValue;
    }

    public String getHash() {
        return mHash;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public void setValue(String mValue) {
        this.mValue = mValue;
    }

    public void setHash(String hash) {
        this.mHash = mHash;
    }


}