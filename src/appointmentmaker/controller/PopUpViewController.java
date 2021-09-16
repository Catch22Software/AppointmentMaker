package appointmentmaker.controller;

import appointmentmaker.Main;
import appointmentmaker.model.Appointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDateTime;

/**
 * Controller that handles the pop up screen for appointment reminder check
 */
public class PopUpViewController {

    @FXML
    private TableView<Appointments> apptviewtable;
    @FXML
    private TableColumn<Appointments, Integer> apptidcol;
    @FXML
    private TableColumn<Appointments, String> appttitlecol;
    @FXML
    private TableColumn<Appointments, String> apptdesccol;
    @FXML
    private TableColumn<Appointments,String> apptloccol;
    @FXML
    private TableColumn<Appointments,String> apptcontactcol;
    @FXML
    private TableColumn<Appointments,String> appttypecol;
    @FXML
    private TableColumn<Appointments, LocalDateTime> apptstartcol;
    @FXML
    private TableColumn<Appointments, LocalDateTime> apptendcol;
    @FXML
    private TableColumn<Appointments, Integer> apptcxidcol;
    @FXML
    private TableColumn<Appointments,Integer> apptusercol;
    private static ObservableList<Appointments> observableList = FXCollections.observableArrayList();
    private static FilteredList<Appointments> filteredList = new FilteredList<>(observableList);
    private static SortedList<Appointments> sortedList = new SortedList<>(filteredList);

    /**
     * Populates list that contains appointments for current user within 15 minutes
     * @param list the list
     */
    public static void setFilteredList(ObservableList<Appointments> list) {
            observableList = FXCollections.observableArrayList(list);
    }

    @FXML
    private void initialize(){

        setTableColData();
        setTableData();
    }
    @FXML
    private void setTableColData() {
        apptidcol.setCellValueFactory(cellData -> cellData.getValue().apptIdProperty().asObject());
        appttitlecol.setCellValueFactory(cellData -> cellData.getValue().apptTitleProperty());
        apptdesccol.setCellValueFactory(cellData -> cellData.getValue().apptDescProperty());
        apptloccol.setCellValueFactory(cellData -> cellData.getValue().apptLocationProperty());
        apptcontactcol.setCellValueFactory(cellData -> cellData.getValue().contactNameProperty());
        appttypecol.setCellValueFactory(cellData -> cellData.getValue().apptTypeProperty());
        apptstartcol.setCellValueFactory(cellData -> cellData.getValue().apptStartTimeProperty());
        apptendcol.setCellValueFactory(cellData -> cellData.getValue().apptEndTimeProperty());
        apptcxidcol.setCellValueFactory(cellData -> cellData.getValue().cxIdProperty().asObject());
        apptusercol.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asObject());
    }
    @FXML
    private void setTableData(){
        filteredList = new FilteredList<>(observableList,p->true);
        sortedList =  new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(apptviewtable.comparatorProperty());
        apptviewtable.setItems(sortedList);

    }
    @FXML
    private void handleConfirmAction(ActionEvent actionEvent) { // closes popup window
        Main.getPopUpStage().close();
    }
}
