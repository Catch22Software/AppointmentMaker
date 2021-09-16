package appointmentmaker.controller;

import appointmentmaker.Main;
import appointmentmaker.utility.PassWrdHash;
import appointmentmaker.utility.Utility;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.ZoneId;
import java.util.Optional;

/**
 * Controller that handles login screen and extends the root menu screen
 */
public class LoginViewController extends RootViewController{


    @FXML
    private Label userloginlabel;
    @FXML
    private Label usernamelabel;
    @FXML
    private Label enterpasswordlabel;
    @FXML
    private Button loginbutton;
    @FXML
    private Button clearbutton;
    @FXML
    private TextField userfield;
    @FXML
    private PasswordField passwrdfield;
    @FXML
    private Label locationlabel;
    @FXML
    private Label errorlabelfield;

    @FXML
    private void handleLoginAction(ActionEvent actionEvent) {
        //when user presses login button, custom prompt displayed
        ButtonType ok = new ButtonType(Utility.getBundle().getString("ButtonText"), ButtonBar.ButtonData.OK_DONE);
        errorlabelfield.setOpacity(0.0);
        if(userfield.getText().isBlank() ||
        passwrdfield.getText().isBlank()){
            String s = Utility.getBundle().getString("errfield");
           errorlabelfield.setOpacity(100.0);
           errorlabelfield.setText(s);
           return;
        }
        final var text = (passwrdfield.getText()).toCharArray(); //password stored as char array for hashing check
       String valid = PassWrdHash.validateUser(userfield.getText(), text);// validates user/pass against DB value for login process
        var information = Utility.getBundle().getString("popInformation");
        if(valid.contains("THANK") || valid.contains("MERCI")) {
           Alert alert = new Alert(Alert.AlertType.INFORMATION,valid,ok);
           alert.setTitle(information);
           alert.setHeaderText("");
           alert.setResizable(false);
           Optional<ButtonType> result = alert.showAndWait();
           if (result.isPresent()) {
               passwrdfield.clear();
               errorlabelfield.setOpacity(0.0);
               Platform.runLater(this::enableMenus); // enables logoff and the GoTo menus in the root menu
               Main.showMainView(userfield.getText());
           }
       }
           else if(valid.contains("INCORRECT")){
               Alert alert2 = new Alert(Alert.AlertType.ERROR,valid,ok);
               alert2.setTitle(information);
               alert2.setHeaderText("");
               alert2.setResizable(false);
               alert2.show();
           }
           else if(valid.contains("INVALID") || valid.contains("INVALIDE")){
               Alert alert3 = new Alert(Alert.AlertType.ERROR,valid,ok);
               alert3.setTitle(information);
               alert3.setHeaderText("");
               alert3.setResizable(false);
               alert3.show();
           }
    }

    @FXML
    private void handleClearFieldsAction(ActionEvent actionEvent) { // clears out user/pass fields
        userfield.clear();
        passwrdfield.clear();
    }

    @FXML
    private void initialize(){
        userloginlabel.setText(Utility.getBundle().getString("UserLoginLabel"));
        usernamelabel.setText(Utility.getBundle().getString("UserNameLabel"));
        enterpasswordlabel.setText(Utility.getBundle().getString("EnterPassWrdLabel"));
        loginbutton.setText(Utility.getBundle().getString("LoginButton"));
        clearbutton.setText(Utility.getBundle().getString("ClearButton"));
        errorlabelfield.setOpacity(0.0);
        var s = String.valueOf(ZoneId.systemDefault());
        locationlabel.setText(s);
    }
}
