package data;


/**
 * Created by user on 03-02-2016.
 */
public class CarData {
    private String mName;
    private String imageName;

    public CarData(String name, String imageName) {
        this.mName = name;
        this.imageName = imageName;
    }


    public String getName() {
        return mName;
    }
    public String getImageName() {
        return imageName;
    }

}
