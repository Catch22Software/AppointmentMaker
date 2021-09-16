package appointmentmaker.controller;

import appointmentmaker.Main;
import appointmentmaker.utility.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * Controller that handles the main menu and runs the primary stage
 */
public class RootViewController extends MainScreenViewController{

    @FXML
    private Menu menugoto;
    @FXML
    private MenuItem menumain;
    @FXML
    private MenuItem menuappt;
    @FXML
    private MenuItem menucust;
    @FXML
    private MenuItem menureports;
    @FXML
    private MenuItem menulogoff;
    @FXML
    private Menu menufile;
    @FXML
    private MenuItem menuclose;
    @FXML
    private Menu menuhelp;
    @FXML
    private MenuItem menuabout;

    private static final Menu staticMenuGoTo = new Menu();
    private static final MenuItem staticMenuLogOff = new MenuItem();


    @FXML
    private void handleExitAction(ActionEvent actionEvent) {
        System.exit(0);
    } //exits program
    @FXML
    private void handleMainScreenAction(ActionEvent actionEvent) {
        Main.showMainView(Main.getLoginUserHolder());
    } // goes to main screen
    @FXML
    private void handleAppointmentScreenAction(ActionEvent actionEvent) {
        Main.showAppointmentView();
    } // goes to appointment screen
    @FXML
    private void handleCustomerScreenAction(ActionEvent actionEvent) {
        Main.showCustomerRecordView();
    } // goes to customer screen
    @FXML
    private void handleReportScreenAction(ActionEvent actionEvent) {
        Main.showGenerateReportsView();
    } // opens up report popup
    @FXML
    private void handleAboutPopUpAction(ActionEvent actionEvent) { // about screen popup
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Utility.getBundle().getString("abouttitle"));
        alert.setHeaderText(Utility.getBundle().getString("aboutheader"));
        alert.setContentText(Utility.getBundle().getString("aboutcopy"));
        alert.show();
    }
    @FXML
    private void initialize(){
        staticMenuGoTo.disableProperty().bindBidirectional(menugoto.disableProperty()); // sets static menu object to be binded to fxml
        staticMenuLogOff.disableProperty().bindBidirectional(menulogoff.disableProperty());// sets static menu object to be binded to fxml
        menufile.setText(Utility.getBundle().getString("file"));
        menuclose.setText(Utility.getBundle().getString("cls"));
        menuhelp.setText(Utility.getBundle().getString("help"));
        menuabout.setText(Utility.getBundle().getString("about"));
        menulogoff.setText(Utility.getBundle().getString("logout"));
        menulogoff.setDisable(true);
        menumain.setText(Utility.getBundle().getString("main"));
        menuappt.setText(Utility.getBundle().getString("appt"));
        menucust.setText(Utility.getBundle().getString("cust"));
        menureports.setText(Utility.getBundle().getString("report"));
        menugoto.setText(Utility.getBundle().getString("go"));
        menugoto.setDisable(true);
    }

    @FXML
    private void handleLogOutAction(ActionEvent event) { // logs off and back to log in screen
        Utility.setCurrentUserIdValue(-1);
        Main.setLoginUserHolder(null);
        menulogoff.setDisable(true);
        menugoto.setDisable(true);
        this.resetApptCheckFlag();
        Main.showLoginView();
    }

    /**
     * Enables log off and GoTo menus
     */
    public void enableMenus(){
        staticMenuLogOff.setDisable(false);
        staticMenuGoTo.setDisable(false);
    }
}
