package com.example.message.data.model;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.Objects;

@Entity(tableName = "sms")
public class Sms implements Comparable<Sms>{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "readState")
    private String readState;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "folder")
    private String folder;

    @ColumnInfo(name = "timeMilliseconds")
    private long time_milliseconds;

    @Ignore
    private String image;

    public Sms(){}

    public long getTime_milliseconds() {
        return time_milliseconds;
    }

    public void setTime_milliseconds(long time_milliseconds) {
        this.time_milliseconds = time_milliseconds;
    }

    @Ignore
    public Sms(@Nullable String name, String address, String message, String readState, String time, long time_milliseconds, String folder, String image) {
        this.name = name;
        this.address = address;
        this.message = message;
        this.readState = readState;
        this.time = time;
        this.time_milliseconds = time_milliseconds;
        this.folder = folder;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReadState() {
        return readState;
    }

    public void setReadState(String readState) {
        this.readState = readState;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sms sms = (Sms) o;
        return  Objects.equals(address, sms.address) && Objects.equals(message, sms.message) && Objects.equals(readState, sms.readState) && Objects.equals(time, sms.time) && Objects.equals(folder, sms.folder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, message, readState, time, folder);
    }

    @Override
    public String toString() {
        return "\nSms{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", message='" + message + '\'' +
                ", readState='" + readState + '\'' +
                ", time='" + time + '\'' +
                ", folder='" + folder + '\'' +
                ", time_milliseconds=" + time_milliseconds +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public int compareTo(Sms sms) {
        Long s1 = this.getTime_milliseconds();
        Long s2 = sms.getTime_milliseconds();
        return s1.compareTo(s2);
    }
}
