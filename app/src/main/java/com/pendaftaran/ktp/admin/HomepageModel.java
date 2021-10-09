package com.pendaftaran.ktp.admin;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class HomepageModel implements Parcelable {

    private String nik;
    private String noKK;
    private String name;
    private String age;
    private String email;
    private String phone;
    private String pos;
    private String address;
    private String dp;
    private String sign;
    private ArrayList<String> document;
    private String status;
    private String uid;

    public HomepageModel(){}

    protected HomepageModel(Parcel in) {
        nik = in.readString();
        noKK = in.readString();
        name = in.readString();
        age = in.readString();
        email = in.readString();
        phone = in.readString();
        pos = in.readString();
        address = in.readString();
        dp = in.readString();
        sign = in.readString();
        document = in.createStringArrayList();
        status = in.readString();
        uid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nik);
        dest.writeString(noKK);
        dest.writeString(name);
        dest.writeString(age);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(pos);
        dest.writeString(address);
        dest.writeString(dp);
        dest.writeString(sign);
        dest.writeStringList(document);
        dest.writeString(status);
        dest.writeString(uid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HomepageModel> CREATOR = new Creator<HomepageModel>() {
        @Override
        public HomepageModel createFromParcel(Parcel in) {
            return new HomepageModel(in);
        }

        @Override
        public HomepageModel[] newArray(int size) {
            return new HomepageModel[size];
        }
    };

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNoKK() {
        return noKK;
    }

    public void setNoKK(String noKK) {
        this.noKK = noKK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public ArrayList<String> getDocument() {
        return document;
    }

    public void setDocument(ArrayList<String> document) {
        this.document = document;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
