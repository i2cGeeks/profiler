package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class Controller implements Initializable {

    @FXML //  fx:id="fruitCombo"
    private ComboBox<String> user_id; // Value injected by FXMLLoader

    @FXML //  fx:id="fruitCombo"
    private ComboBox<String> app_id; // Value injected by FXMLLoader

    @FXML //  fx:id="fruitCombo"
    private ComboBox<String> db_id; // Value injected by FXMLLoader

    @FXML //  fx:id="fruitCombo"
    private Label success_msg; // Value injected by FXMLLoader

    @FXML //  fx:id="fruitCombo"
    private Label failure_msg; // Value injected by FXMLLoader

    @FXML //  fx:id="fruitCombo"
    private Label validation_msg; // Value injected by FXMLLoader

    @FXML //  fx:id="fruitCombo"
    private ProgressIndicator progress_indicator; // Value injected by FXMLLoader

    @FXML //  fx:id="fruitCombo"
    private Button submit; // Value injected by FXMLLoader

    @FXML //  fx:id="fruitCombo"
    private Button logs; // Value injected by FXMLLoader

    @FXML //  fx:id="fruitCombo"
    private Button post; // Value injected by FXMLLoader

    @FXML //  fx:id="fruitCombo"
    private GridPane rootPane; // Value injected by FXMLLoader

    @FXML //  fx:id="fruitCombo"
    private ScrollPane logsPane; // Value injected by FXMLLoader




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy hh:mm:a");

        TimeZone etTimeZone = TimeZone.getTimeZone("Asia/Karachi"); //Target timezone
        Calendar today = Calendar.getInstance(etTimeZone);
        System.out.println(FORMATTER.format(today.getTime())); //07/14/2018 at 03:05PM EDT


        assert app_id != null : "fx:id=\"app_id\" was not injected: check your FXML file 'fruitcombo.fxml'.";
        assert db_id != null : "fx:id=\"db_id\" was not injected: check your FXML file 'fruitcombo.fxml'.";
        assert user_id != null : "fx:id=\"user_id\" was not injected: check your FXML file 'fruitcombo.fxml'.";
        assert success_msg != null : "fx:id=\"success_msg\" was not injected: check your FXML file 'fruitcombo.fxml'.";
        assert failure_msg != null : "fx:id=\"failure_msg\" was not injected: check your FXML file 'fruitcombo.fxml'.";
        assert progress_indicator != null : "fx:id=\"progressIndicator\" was not injected: check your FXML file 'fruitcombo.fxml'.";
        assert submit != null : "fx:id=\"submit\" was not injected: check your FXML file 'fruitcombo.fxml'.";
        assert logs != null : "fx:id=\"submit\" was not injected: check your FXML file 'fruitcombo.fxml'.";
        assert post != null : "fx:id=\"submit\" was not injected: check your FXML file 'fruitcombo.fxml'.";
        assert validation_msg != null : "fx:id=\"validation_msg\" was not injected: check your FXML file 'fruitcombo.fxml'.";
        assert rootPane != null : "fx:id=\"rootPane\" was not injected: check your FXML file 'fruitcombo.fxml'.";
        assert logsPane != null : "fx:id=\"logsPane\" was not injected: check your FXML file 'fruitcombo.fxml'.";
        if(readEnvironments()) {
            db_id.setItems(FXCollections.observableArrayList(envList));
        }
        else{
            failure_msg.setOpacity(1);
        }
        //Passing FileInputStream object as a parameter
        FileInputStream inputstream = null;
        File file = new File("res/config.xml");

        try {
            System.out.println(file.getAbsolutePath() +file.getPath() + "Test");
            inputstream = new FileInputStream("res/3.jpg");
            Image image = new Image(inputstream);
            //setting the fit height and width of the image view);
            Background background;
            background = new Background(new BackgroundImage(image,null,null,new BackgroundPosition(
                    Side.LEFT, 0, true, Side.TOP, 0, true),new BackgroundSize(Main.width, Main.height, false, false, false, false) ));
            rootPane.setBackground(background);
            logsPane.setBackground(Background.EMPTY);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    HashMap<String, HashMap<String,String>> envTerminalConfig;
    HashMap<String, HashMap<String,String>> envUrlList;
    HashMap<String, List<String>> envUserList;
    HashMap<String,List<String>> envAppList;
    List<String> commands;
    List<String> envList;
    private Boolean readEnvironments() {
        Boolean result = false;
        try{
            envUserList = new HashMap<>();
            envTerminalConfig = new HashMap<>();
            envAppList = new HashMap<>();
            envUrlList = new HashMap<>();
            envList = new ArrayList<>();
            commands = new ArrayList<>();
            File file = new File("res/config.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();
            NodeList ndbList = document.getElementsByTagName("db_name");
            NodeList ndbConfigList = document.getElementsByTagName("db_configs");
            NodeList nTermianlConfigList = document.getElementsByTagName("terminal_config");
            for (int temp = 0; temp < ndbList.getLength(); temp++) {
                String db_id;
                db_id = ndbList.item(temp).getTextContent();
                envList.add(db_id);
                HashMap<String, String > dbUrls = new HashMap<>();
                HashMap<String, String > terminalConfig = new HashMap<>();
                List<String> dbUsers = new ArrayList<>();
                List<String> dbApps = new ArrayList<>();
                dbUrls.put("si-url",((Element)(ndbConfigList.item(temp))).getElementsByTagName("si_url").item(0).getTextContent());
                dbUrls.put("mi-url",((Element)(ndbConfigList.item(temp))).getElementsByTagName("mi_url").item(0).getTextContent());
                terminalConfig.put("node_ip",((Element)(nTermianlConfigList.item(temp))).getElementsByTagName("node_ip").item(0).getTextContent());
                terminalConfig.put("logs_path",((Element)(nTermianlConfigList.item(temp))).getElementsByTagName("logs_path").item(0).getTextContent());
                terminalConfig.put("user_id",((Element)(nTermianlConfigList.item(temp))).getElementsByTagName("user_id").item(0).getTextContent());
                terminalConfig.put("user_password",((Element)(nTermianlConfigList.item(temp))).getElementsByTagName("user_password").item(0).getTextContent());
                envTerminalConfig.put(db_id,terminalConfig);
                envUrlList.put(db_id,dbUrls);
                NodeList db_apps = ((Element)(ndbConfigList.item(temp))).getElementsByTagName("app_id");
                for (int q = 0; q < db_apps.getLength(); q++) {
                    dbApps.add(((Element)(db_apps.item(q))).getTextContent());
                }
                envAppList.put(db_id,dbApps);
                NodeList db_users = ((Element)(ndbConfigList.item(temp))).getElementsByTagName("user_id");
                for (int q = 0; q < db_users.getLength(); q++) {
                    dbUsers.add(((Element)(db_users.item(q))).getTextContent());
                }
                envUserList.put(db_id, dbUsers);
            }
            NodeList commandList = document.getElementsByTagName("command");
            for (int q = 0; q < commandList.getLength(); q++) {
                commands.add(((Element)(commandList.item(q))).getTextContent());
            }
            result = true;
        } catch (ParserConfigurationException ex) {
        } catch (IOException th) {
        } catch (SAXException th) {
        } finally {
        }
        return result;
    }

    @FXML
    protected void handleDbChangeAction(ActionEvent event) {
        if (db_id.getValue() != null)
        {
            user_id.setItems(FXCollections.observableArrayList(envUserList.get(db_id.getValue())));
            app_id.setItems(FXCollections.observableArrayList(envAppList.get(db_id.getValue())));
        }
    }

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        success_msg.setOpacity(0);
        failure_msg.setOpacity(0);
        validation_msg.setOpacity(0);
        if(app_id.getValue()!=null && user_id.getValue() != null && db_id.getValue() != null)
        {
            submit.setOpacity(0);
            Service service = new Service();
            progress_indicator.setOpacity(1);
            String response = service.setUserConfig(app_id.getValue(),user_id.getValue(),db_id.getValue(),envUrlList.get(db_id.getValue()));
            if("00".equals(response)) {
                success_msg.setOpacity(1);
            } else {
                failure_msg.setOpacity(1);
            }
            submit.setOpacity(1);
            progress_indicator.setOpacity(0);
        }
        else {
            validation_msg.setOpacity(1);
        }
    }

    @FXML
    protected void handleLogsButtonAction(ActionEvent event) {
        success_msg.setOpacity(0);
        failure_msg.setOpacity(0);
        validation_msg.setOpacity(0);
        if(app_id.getValue()!=null && user_id.getValue() != null && db_id.getValue() != null)
        {
            Service.launchTerminal(commands,envTerminalConfig.get(db_id.getValue()));
        }
        else {
            validation_msg.setOpacity(1);
        }

    }

    @FXML
    protected void handleRequstButtonAction(ActionEvent event) {
        success_msg.setOpacity(0);
        failure_msg.setOpacity(0);
        validation_msg.setOpacity(0);
        if(app_id.getValue()!=null && user_id.getValue() != null && db_id.getValue() != null)
        {
            System.out.println(HttpConnection.sendRequest());
        }
        else {
            validation_msg.setOpacity(1);
        }

    }


}
