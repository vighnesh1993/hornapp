package data;


/**
 * Created by user on 03-02-2016.
 */
public class CarData {
    private String mName;
    private String imageName;
    private String carId;

    public CarData(String name, String imageName,String carId) {
        this.mName = name;
        this.imageName = imageName;
        this.carId = carId;
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

}
