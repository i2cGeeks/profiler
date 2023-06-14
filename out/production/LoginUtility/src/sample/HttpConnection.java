package sample;


import org.omg.CORBA.NameValuePair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class HttpConnection {
    public static URL serverUrl;

    public HttpConnection()
    {
        try {
            serverUrl = new URL("https://mcpqa.mycardplace.com/mcpmobile/signinUser.action?mobileApplicationId=i2cdemo&IMEI=fb1348356db84f3899&mobileApplicationVersion=3.0.4&mobileOSVersion=Android 9&deviceId=52c5dcf7079b2465129a213d4df8277027140a0467aff73ac56fdc9590b207c5&userId=bqaisar1&password=test1234&deviceToken=APA91bHoEiCx4yK-zIWvb2Vk9Jz1t5OrQ4qT4BK4zq_NklNV6SIymoI1CgPF-1YJNWuQWuYMX6X-JZJTfjNdBEUAGCOnoUhS4CWZmuYtf0aSVj18rK-vKIYdJuNXgbhQT5MRtYYk3kf_&deviceOsId=2&deviceModel=Android SDK built for x86&deviceVendorId=vivo23&deviceName=Android SDK built for x86&loginAuthMode=P");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    public static String sendRequest() {
        String respone = "Fail to fetch data or server connection issue";
        try {
            HttpURLConnection urlConnection = (HttpURLConnection)serverUrl.openConnection();
            // Indicate that we want to write to the HTTP request body
            //urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");

            // Writing the post data to the HTTP request body
            //BufferedWriter httpRequestBodyWriter = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            //httpRequestBodyWriter.write("visitorName=Johnny+Jacobs&luckyNumber=1234");
            //httpRequestBodyWriter.close();

            // Reading from the HTTP response body
            Scanner httpResponseScanner = new Scanner(urlConnection.getInputStream());
            while(httpResponseScanner.hasNextLine()) {
                respone = httpResponseScanner.nextLine();
            }
            httpResponseScanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return respone;
    }

    public static URL getServerUrl() {
        return serverUrl;
    }

    public static void setServerUrl(URL serverUrl) {
        HttpConnection.serverUrl = serverUrl;
    }
}
