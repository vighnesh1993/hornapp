package data;


/**
 * Created by user on 03-02-2016.
 */
public class CarData {
    private String mName;
    private String imageName;
    private String carId;
    private String carVarient;

    public CarData(String name, String imageName,String carId,String carVarient) {
        this.mName = name;
        this.imageName = imageName;
        this.carId = carId;
        this.carVarient = carVarient;
    }


    public String getName() {
        return mName;
    }
    public String getImageName() {
        return imageName;
    }
    public String getCarId() {
        return carId;
    }
    public String getVarient() {
        return carVarient;
    }

}
