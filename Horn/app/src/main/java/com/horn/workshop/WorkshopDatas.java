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
    int workshopid;
    String rating;
//int id_;

    public WorkshopDatas(String name, String address,String phone,String category, String image,int workshopid,String rating) {
        this.name = name;
        this.address = address;
        this.Phone = phone;
        this.category = category;
        this.image = image;
        this.workshopid = workshopid;
        this.rating = rating;

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
public int getworkshopid(){return workshopid;}
    public String getrating(){return rating;}
  //  public int getId() {
//        return id_;
//    }

}