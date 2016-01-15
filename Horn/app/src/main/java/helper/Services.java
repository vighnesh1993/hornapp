package helper;

import android.content.Context;

/**
 * Created by user on 13-01-2016.
 */
public class Services {

    public String name;
    public String description;
    public String imageName;

    public static String[] countArray = {"30", "26", "15", "20", "30"};

    public int getImageResourceId(Context context)
    {
        try {
            return context.getResources().getIdentifier(this.imageName, "drawable", context.getPackageName());

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public String getservicesCount() {
        try {
            String serviceName = this.name;

            if(serviceName.equals("Scheduled Maintenance")){
                return  countArray[0];
            }else if(serviceName.equals("Running Maintenance")){
                return  countArray[1];
            }else if(serviceName.equals("Body and Painting")){
                return  countArray[2];
            }else if(serviceName.equals("VAS")){
                return  countArray[3];
            }else if(serviceName.equals("Others")){
                return  countArray[4];
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
