package helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 13-01-2016.
 */
public class ServicesManager {

    private static String[] servicesArray = {"Scheduled Maintenance", "Running Maintenance", "Body and Painting", "VAS", "Others"};
    private static String loremIpsum = "Lorem ipsum dolor sit";

    private static ServicesManager mInstance;
    private List<Services> servicesp;

    public static ServicesManager getInstance() {
        if (mInstance == null) {
            mInstance = new ServicesManager();
        }

        return mInstance;
    }

    public List<Services> getServices() {
        if (servicesp == null) {
            servicesp = new ArrayList<Services>();

            for (String serviceName : servicesArray) {
                Services services = new Services();
                services.name = serviceName;
                services.description = loremIpsum;
                services.imageName = serviceName.replaceAll("\\s+","").toLowerCase();
                servicesp.add(services);
            }
        }

        return  servicesp;
    }

}
