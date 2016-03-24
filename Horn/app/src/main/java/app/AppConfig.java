package app;

/**
 * Created by vighnu on 12/11/2015.
 */
public class AppConfig {
    // Server user login url
    public static String URL_LOGIN = "http://blueripples.org/horn/app_server/login.php";
    public static String UPLOAD_URL="http://blueripples.org/horn/app_server/test.php";

    // Server user register url
    public static String URL_REGISTER = "http://blueripples.org/horn/app_server/register.php";

    public static final String URL_VERIFY_OTP = "http://blueripples.org/horn/app_server/verify_otp.php";

    public static final String URL_FORGOT_PASSWORD = "http://blueripples.org/horn/app_server/forgot_password.php";

    public static final String URL_CHANGE_PASSWORD = "http://blueripples.org/horn/app_server/changePassword.php";

    public static final String SMS_ORIGIN = "MSGIND";

    // special character to prefix the otp. Make sure this character appears only once in the sms
    public static final String OTP_DELIMITER = ":";
    public static String URL_SM_SERVICES = "http://blueripples.org/horn/app_server/SM.php";
    public static String URL_SM_WORKSHOPDATA = "http://blueripples.org/horn/app_server/SM_WorkshopDatalist.php";
    public static String URL_CARLIST = "http://blueripples.org/horn/app_server/cars.php";
    public static String URL_ADDCARDETAIL = "http://blueripples.org/horn/app_server/add_cars.php";
    public static String URL_RM_DATA = "http://blueripples.org/horn/app_server/RM_data.php";
    public static String URL_RM_WORKSHOPDATA = "http://blueripples.org/horn/app_server/RM_Workshopdata.php";
    public static String URL_RM_SERVICES = "http://blueripples.org/horn/app_server/RM_appointment.php";
}

