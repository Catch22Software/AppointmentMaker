package appointmentmaker.controller;

import appointmentmaker.Main;
import appointmentmaker.model.Appointments;
import appointmentmaker.modeldao.AppointmentsDAO;
import appointmentmaker.utility.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Controller that handles the main screen
 */
public class MainScreenViewController {

    @FXML
    private ComboBox<String> filterbycombobox;
    @FXML
    private Button userscreenbutton;
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
    private TableColumn<Appointments,LocalDateTime> apptstartcol;
    @FXML
    private TableColumn<Appointments, LocalDateTime> apptendcol;
    @FXML
    private TableColumn<Appointments, Integer> apptcxidcol;
    @FXML
    private TableColumn<Appointments,Integer> apptusercol;
    @FXML
    private Label welcometextlabel;
    @FXML
    private ComboBox<String> yearviewcombobox;
    @FXML
    private ComboBox<String> monthviewcombobox;
    @FXML
    private ComboBox<String> weekviewcombobox;
    private final AppointmentsDAO dbappt = new AppointmentsDAO();
    private static ObservableList<Appointments> masterTableData = null;
    private static FilteredList<Appointments> filteredList = null;
    private static SortedList<Appointments> sortedList = null;
    private static boolean apptCheckCalled = false;


    @FXML
    private void initialize(){
        var loginUserHolder = Main.getLoginUserHolder();
        welcometextlabel.setText("WELCOME : "+ loginUserHolder.toUpperCase());
        welcometextlabel.setFont(Font.font(24));
        setTableColData();
        if(Main.getLoginUserHolder().equals("admin"))
            userscreenbutton.setDisable(false);
        List<String> years = List.of("2021","2022","2023","2024","2025");
        yearviewcombobox.getItems().addAll(years);
        List<String> months = List.of("JANUARY","FEBRUARY","MARCH","APRIL","MAY","JUNE","JULY"
                ,"AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER");
        monthviewcombobox.getItems().addAll(months);
        weekviewcombobox.getItems().addAll(Utility.LocalizedWeek.createWeekComboBoxBasedOnYear("2021"));
        yearviewcombobox.valueProperty().addListener((observableValue, string, t1) -> {
            weekviewcombobox.getItems().clear();
            weekviewcombobox.getItems().addAll(Utility.LocalizedWeek.createWeekComboBoxBasedOnYear(t1));
        });
        filterbycombobox.getItems().addAll("YEAR", "YEAR and MONTH", "WEEK");
        setTableData();
        if(!apptCheckCalled)
            checkForUpcomingAppointments(); // if first time seeing main screen, check for upcoming appointments.
    }

    @FXML
    private void setTableData() {
        masterTableData = FXCollections.observableArrayList();
        masterTableData.addAll(dbappt.displayAllAppointments());
        filteredList = new FilteredList<>(masterTableData, p -> true);
        sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(apptviewtable.comparatorProperty());
        apptviewtable.setItems(sortedList);
    }
    @FXML
    private void checkForUpcomingAppointments(){
        // shows custom popup if user has appointments within 15 minutes of login time relative to user's system default time
        final var userIdFilter = Utility.getCurrentUserIdValue();
        ObservableList<Appointments> newObservableList = FXCollections.observableArrayList(sortedList);
        FilteredList<Appointments> newFilteredList = new FilteredList<>(newObservableList, p -> true);
        newFilteredList.setPredicate(appointments -> {
            // filters appointment list to only assigned to user and within 15 minutes

            long diff = ChronoUnit.MINUTES.between(LocalDateTime.now(),appointments.getApptStartTime());
            return ((diff<=15 && diff >=0) && appointments.getUserId()==userIdFilter);
        });
        SortedList<Appointments> newSortedList = new SortedList<>(newFilteredList);
        if(!newSortedList.isEmpty())
            Main.showPopUpReportView(newSortedList);
        else{
            //shows other prompt if no appointments are within 15 minutes for the current user
            final var info = "You do not have any upcoming appointments.\n Thanks for being such an excellent worker!!";
            final var information="APPOINTMENT ALERT!!";
            Alert alert = new Alert(Alert.AlertType.INFORMATION,info);
            alert.setTitle(information);
            alert.setHeaderText("");
            alert.setResizable(false);
            alert.show();
        }
        apptCheckCalled=true; // changes flag so check is only called once during log in.
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


        apptidcol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.05));
        appttitlecol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.1));
        apptdesccol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.1));
        apptloccol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.1));
        apptcontactcol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.1));
        appttypecol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.1));
        apptstartcol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.15));
        apptendcol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.15));
        apptcxidcol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.05));
        apptusercol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.05));

        apptidcol.setStyle("-fx-alignment: CENTER;");
        apptcxidcol.setStyle("-fx-alignment: CENTER;");
        apptusercol.setStyle("-fx-alignment: CENTER;");

    }

    @FXML
    private void handleCustomerScreenAction(ActionEvent actionEvent) { //opens customer screen
        Main.showCustomerRecordView();
    }
    @FXML
    private void handleAppointmentScreenAction(ActionEvent actionEvent) { // opens appointment screen
        Main.showAppointmentView();
    }
    @FXML
    private void handleReportsScreenAction(ActionEvent actionEvent) { // opens report choosing screen
        Main.showGenerateReportsView();
    }
    @FXML
    private void handleUserScreenAction(ActionEvent actionEvent) { // opens user maintenance screen (if applicable)
        Main.showUserView();
    }
    @FXML
    private void handleFilterTableAction(ActionEvent actionEvent) {
        //filters appointments based on year, year and month, week or all
        String choice = filterbycombobox.getSelectionModel().getSelectedItem();
        if(choice==null || choice.isBlank() || choice.isEmpty()){
            handleResetFilterViewAction(actionEvent);
            return;
        }
        if(choice.contains("and")){
            if((yearviewcombobox.getSelectionModel().getSelectedItem()==null || monthviewcombobox.getSelectionModel().getSelectedItem() == null) ||
                    (yearviewcombobox.getSelectionModel().getSelectedItem().isBlank() || monthviewcombobox.getSelectionModel().getSelectedItem().isBlank()) ||
                    (yearviewcombobox.getSelectionModel().getSelectedItem().isEmpty() || monthviewcombobox.getSelectionModel().getSelectedItem().isEmpty())){
                handleResetFilterViewAction(actionEvent);
                return;
            }
            String temp = yearviewcombobox.getSelectionModel().getSelectedItem();
            String selectedItem = monthviewcombobox.getSelectionModel().getSelectedItem();
            filteredList.setPredicate(appointments -> {
                long diff = Math.abs(ChronoUnit.MONTHS.between((YearMonth.of(Integer.parseInt(temp),Month.valueOf(selectedItem).getValue())),appointments.getApptStartTime()));
                long diff2 = Math.abs(ChronoUnit.MONTHS.between((YearMonth.of(Integer.parseInt(temp),Month.valueOf(selectedItem).getValue())),appointments.getApptEndTime()));
                return (diff+diff2)<=1;
            });
        }
        else if(choice.contains("YEAR")){
            String temp = yearviewcombobox.getSelectionModel().getSelectedItem();
            if(temp==null || temp.isEmpty() || temp.isBlank()){
                handleResetFilterViewAction(actionEvent);
                return;
            }
            filteredList.setPredicate(appointments -> {
                long diff = Math.abs(ChronoUnit.YEARS.between(Year.parse(temp),appointments.getApptStartTime()));
                long diff2 = Math.abs(ChronoUnit.YEARS.between(Year.parse(temp),appointments.getApptEndTime()));
                return (diff+diff2)<=1;
            });
        }
        else if(choice.contains("WEEK")){
            String temp = weekviewcombobox.getSelectionModel().getSelectedItem();
            if(temp==null || temp.isEmpty() || temp.isBlank()){
                handleResetFilterViewAction(actionEvent);
                return;
            }
            String[] trash;
            LocalDate start, end;
            trash = temp.split(" ");
            start = LocalDate.parse(trash[2]);
            end = LocalDate.parse(trash[4]);
            filteredList.setPredicate(appointments ->
                    (appointments.getApptStartTime().toLocalDate().isAfter(start) ||
                            appointments.getApptStartTime().toLocalDate().isEqual(start))
                            &&
                            (appointments.getApptEndTime().toLocalDate().isBefore(end) ||
                                    appointments.getApptEndTime().toLocalDate().isEqual(end)));
        }
        else
            handleResetFilterViewAction(actionEvent);
    }

    @FXML
    private void handleResetFilterViewAction(ActionEvent actionEvent) { //resets filter to all appointments
        filteredList.setPredicate(p->true);
    }

    /**
     * resets appointment check flag
     */
    public void resetApptCheckFlag(){
        apptCheckCalled=false;
    }
}
