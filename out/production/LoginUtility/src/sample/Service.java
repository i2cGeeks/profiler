package sample;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service {


    private final static String DEFAULT_DB_DRIVER = "com.informix.jdbc.IfxDriver";
    private static String dbUser;
    private static String dbPassword;

    private Boolean readUserCredentials() {
        Boolean result = false;
        toString();
        try{
            File file = new File("res/config.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();
            dbUser = document.getElementsByTagName("user_id").item(0).getTextContent();
            dbPassword = document.getElementsByTagName("user_password").item(0).getTextContent();
            result = true;
        } catch (ParserConfigurationException ex) {
        } catch (IOException th) {
        } catch (SAXException th) {
        } finally {
        }
        return result;
    }

    public static void execueCommandsOnTerminal(List<String> commands, Map<String, String> termainlConfig){
        try {
            //String[] command = { "gnome-terminal", "--", "sshpass", "-p", termainlConfig.get("user_password"), "ssh", termainlConfig.get("user_id") + "@" + termainlConfig.get("node_ip") };
            String[] command = { "osascript", "-e", "tell app \"Terminal\" to do script \"ssh " + termainlConfig.get("user_id") + "@" + termainlConfig.get("node_ip") + "\"" };
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            process.getOutputStream().write("Multan@123456".getBytes());
            process.getOutputStream().close();
            process.waitFor(); //
//            for (String command : commands) {
//                ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
//                Process process = processBuilder.start();
//                process.waitFor(); // Wait for the process to complete
//            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void launchTerminal(List<String> commands, Map<String, String> terminalConfig)
    {
        execueCommandsOnTerminal(commands, terminalConfig);
    }

    public String  setUserConfig(String appID, String userID, String dbID, HashMap<String, String> dbUrls)  {
        Connection siConn = null;
        Connection miConn = null;
        String serviceResponseInfo = null;

        if(null != appID && userID != null && dbID != null){
            try{
                DAO dao = new DAO();
                Class.forName(DEFAULT_DB_DRIVER);
                if(readUserCredentials())
                {
                    siConn = DriverManager.getConnection(dbUrls.get("si-url"), dbUser, dbPassword);
                    miConn = DriverManager.getConnection(dbUrls.get("mi-url"), dbUser, dbPassword);
                    serviceResponseInfo = dao.setUserConfig(appID, userID, siConn, miConn);
                }
                //TimeUnit.SECONDS.sleep(3);
            } catch (Exception ex) {
                return serviceResponseInfo;
            } catch (Throwable th) {

                return serviceResponseInfo;
            } finally {
               cleanUp(siConn, null, null);
               cleanUp(miConn, null, null);
            }
        }
        return serviceResponseInfo;
    }
    public static void cleanUp(Connection conn, Statement stmt,ResultSet rst){
        if (rst != null) {
            try {
                rst.close();
            } catch (SQLException sqle) {
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException sqle) {
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
    }

}
