package com.example.bolasepak;

import android.os.Parcel;
import android.os.Parcelable;

public class SoccerMatch implements Parcelable {
    private String name1, name2, dateMatch, score1, score2, img1, img2;

    //getter
    public String getName1(){return name1;}
    public String getName2(){return name2;}
    public String getDateMatch(){return dateMatch;}
    public String getScore1(){return score1;}
    public String getScore2(){return score2;}
    public String getImg1(){return img1;}
    public String getImg2(){return img2;}

    //setter
    public void setName1(String name){this.name1 = name;}
    public void setName2(String name){this.name2 = name;}
    public void setDateMatch(String date){this.dateMatch = date;}
    public void setScore1(String score){this.score1 = score;}
    public void setScore2(String score){this.score2 = score;}
    public void setImg1(String img){this.img1 = img;}
    public void setImg2(String img){this.img2 = img;}

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(this.name1);
        dest.writeString(this.name2);
        dest.writeString(this.dateMatch);
        dest.writeString(this.score1);
        dest.writeString(this.score2);
        dest.writeString(this.img1);
        dest.writeString(this.img2);
    }

    public SoccerMatch(){}

    protected SoccerMatch(Parcel in){
        this.name1 = in.readString();
        this.name2 = in.readString();
        this.dateMatch = in.readString();
        this.score1 = in.readString();
        this.score2 = in.readString();
        this.img1 = in.readString();
        this.img2 = in.readString();
    }

    public static final Parcelable.Creator<SoccerMatch> CREATOR = new Parcelable.Creator<SoccerMatch>(){

        @Override
        public SoccerMatch createFromParcel(Parcel source){
            return new SoccerMatch(source);
        }

        @Override
        public SoccerMatch[] newArray(int size){ return new SoccerMatch[size];}
    };

}
