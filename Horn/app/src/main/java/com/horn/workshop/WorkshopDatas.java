package com.horn.workshop;

/**
 * Created by Sariga on 1/8/2016.
 */
public class WorkshopDatas {


    String name;
    String address;
    String Phone;
    String category;
    String image;
    String offer;
    int workshopid;
    String rating;
    String d;
    String offday;
//int id_;

    public WorkshopDatas(String name, String address, String phone, String category, String image, int workshopid, String rating,String distance,String offer,String offday) {
        this.name = name;
        this.address = address;
        this.Phone = phone;
        this.category = category;
        this.image = image;
        this.workshopid = workshopid;
        this.rating = rating;
        this.d = distance;
        this.offer = offer;
        this.offday = offday;

    }


    public String getName() {
        return name;
    }


    public String getAddress() {
        return address;
    }

    public String getCategory() {
        return category;
    }

    public String getPhone() {
        return Phone;
    }

    public String getProfilepic() {
        return image;
    }

    public int getworkshopid() {
        return workshopid;
    }

    public String getrating() {
        return rating;
    }
    public String getOffer() {
        return offer;
    }
    public String getOffday() {
        return offday;
    }
    //  public int getId() {
//        return id_;
//    }
    public String getDistance() {
        return d;

    }

}