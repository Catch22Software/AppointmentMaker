package appointmentmaker;

import appointmentmaker.controller.PopUpViewController;
import appointmentmaker.model.Appointments;
import appointmentmaker.model.Contacts;
import appointmentmaker.modeldao.AppointmentsDAO;
import appointmentmaker.modeldao.ContactsDAO;
import appointmentmaker.utility.DBUtility;
import appointmentmaker.utility.Utility;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/***
 * Main class runs the Appointment Maker Program.
 * @author James Mills , email: codeguru721@gmail.com
 */
public class Main extends Application {

    /**
     * Stage variable to hold the main stage for access.
     */
    public static Stage primaryStage;
    private static Stage popUpStage;
    private static BorderPane rootLayout;
    private static String userHolder=null;
    private static Stage reportAlertStageHolder = null;

    /**
     * Main method that launches the GUI and sets the ResourceBundle
     * @param args Command line argument (optional)
     */
    public static void main(String[] args) {
        //Lambda Usage #1 in appointmentmaker.utility.Utility.LocalizedWeek.java
        //Lambda Usage #2 in appointmentmaker.model.Appointments.java
        // JavaDocs located in JavaDocs folder outside of root project folder
        //Locale.setDefault(Locale.FRENCH); // test for french
        DBUtility.dbConnect();
        Utility.setBundleOnce(); // Sets Resource Bundle
        launch(args);
        DBUtility.dbDisconnect();
    }

    /**
     * Stage that holds appointment reminder table if applicable
     * @return Returns the reminder table stage
     */
    public static Stage getPopUpStage() {
        return popUpStage;
    }


    /**
     * Overridden method to launch GUI
     * @param stage Primary stage
     */
    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle(Utility.getBundle().getString("title"));
        primaryStage.getIcons()
                .add(new Image(Objects.requireNonNull(Main.class
                        .getResource("view/images/zelda_icon.png"))
                        .toString()));
        initRootLayout();
        showLoginView();
    }

    /**
     * Sets scene for primary stage containing Menu Bar
     */
    public static void initRootLayout(){

        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/rootview.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets scene for login screen at center of root stage
     */
    public static void showLoginView(){

        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/loginview.fxml"));
            AnchorPane loginView = loader.load();
            rootLayout.setCenter(loginView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets scene for main screen at center of root stage
     * @param text Sets the username for the welcome text
     */
    public static void showMainView(String text){
        if(Main.getLoginUserHolder()==null)
            setLoginUserHolder(text);
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/mainscreenview.fxml"));
            AnchorPane mainView = loader.load();
            rootLayout.setCenter(mainView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets scene for customer screen at center of root stage
     */
    public static void showCustomerRecordView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/customerview.fxml"));
            AnchorPane custView = loader.load();
            rootLayout.setCenter(custView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets scene for appointment setting screen at center of root stage
     */
    public static void showAppointmentView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/appointmentview.fxml"));
            AnchorPane apptView = loader.load();
            rootLayout.setCenter(apptView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets scene for user screen at center of root stage
     */
    public static void showUserView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/userview.fxml"));
            AnchorPane userView = loader.load();
            rootLayout.setCenter(userView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens dialog box for report choosing
     */
    public static void showGenerateReportsView(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY); // no extra window options
        alert.setTitle("Choose Your Report Type. ");
        alert.setHeaderText(null);
        alert.setContentText(null);
        alert.setResizable(false);

        ButtonType buttonTypeApptType = new ButtonType("Appointment Types");
        ButtonType buttonTypeContactType = new ButtonType("Appointments by Contact");
        ButtonType buttonTypeUserAlteredType = new ButtonType("Appointment Alterations");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes()
                .setAll(buttonTypeApptType,buttonTypeContactType,buttonTypeUserAlteredType,buttonTypeCancel);
        reportAlertStageHolder = (Stage) alert.getDialogPane().getScene().getWindow();
        reportAlertStageHolder.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST,(Event::consume)); // stops the 'x' from closing the window

        Button apptTypeButton = (Button)alert.getDialogPane().lookupButton(buttonTypeApptType);
        Button apptContactButton = (Button)alert.getDialogPane().lookupButton(buttonTypeContactType);
        Button apptUserButton = (Button)alert.getDialogPane().lookupButton(buttonTypeUserAlteredType);
        apptTypeButton.addEventFilter(ActionEvent.ACTION,event -> { // stops the window close process and opens the correct dialog box
            event.consume();
            apptTypeReport();
        });
        apptContactButton.addEventFilter(ActionEvent.ACTION,event -> { // stops the window close process and opens the correct dialog box
            event.consume();
            apptContactReport();
        });
        apptUserButton.addEventFilter(ActionEvent.ACTION,event -> { // stops the window close process and opens the correct dialog box
            event.consume();
            apptUserAlterReport();
        });

        Optional<ButtonType> result = alert.showAndWait(); // takes the button type used and fires the appropriate event
        result.ifPresent(p -> {
            if(p == buttonTypeApptType)
                apptTypeButton.fire();
            else if(p == buttonTypeContactType)
                apptContactButton.fire();
            else if(p == buttonTypeUserAlteredType)
                apptUserButton.fire();
            else if(p == buttonTypeCancel)
                reportAlertStageHolder.close();
        });
    }

    private static void apptUserAlterReport() { // shows report for appointments altered by another user
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Appointments created or altered " +
                "by another user that are assigned to : "+Main.getLoginUserHolder());
        alert.setHeaderText(null);
        alert.setContentText(null);
        alert.setResizable(true);

        TableView<Appointments> tableView = new TableView<>();
        tableView.setMinHeight(200);
        tableView.setMinWidth(1200);

        TableColumn<Appointments, Integer> apptidcol = new TableColumn<>("Appt ID");
        TableColumn<Appointments, String> appttitlecol = new TableColumn<>("Title");
        TableColumn<Appointments, String> apptdesccol = new TableColumn<>("Desc");
        TableColumn<Appointments,String> apptloccol = new TableColumn<>("Location");
        TableColumn<Appointments,String> apptcontactcol = new TableColumn<>("Contact");
        TableColumn<Appointments,String> appttypecol = new TableColumn<>("Type");
        TableColumn<Appointments, LocalDateTime> apptstartcol = new TableColumn<>("Begins");
        TableColumn<Appointments, LocalDateTime> apptendcol = new TableColumn<>("Ends");
        TableColumn<Appointments, Integer> apptcxidcol = new TableColumn<>("Cust ID");
        TableColumn<Appointments,Integer> apptusercol = new TableColumn<>("User ID");
        TableColumn<Appointments, LocalDateTime> apptcreatecol = new TableColumn<>("Created");
        TableColumn<Appointments, String> apptcreatedbycol = new TableColumn<>("Created By");
        TableColumn<Appointments, LocalDateTime> apptlastupdatecol = new TableColumn<>("Last Updated");
        TableColumn<Appointments, String > apptlastupdatedbycol = new TableColumn<>("Updated By");

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
        apptcreatecol.setCellValueFactory(c -> c.getValue().apptCreateTimeProperty());
        apptcreatedbycol.setCellValueFactory(c-> c.getValue().apptCreatedByProperty());
        apptlastupdatecol.setCellValueFactory(c-> c.getValue().apptLastUpdateTimeProperty());
        apptlastupdatedbycol.setCellValueFactory(c-> c.getValue().apptLastUpdatedByProperty());

        //adjusting the column width
        apptidcol.prefWidthProperty().bind(tableView.widthProperty().multiply(.05));
        appttitlecol.prefWidthProperty().bind(tableView.widthProperty().multiply(.1));
        apptdesccol.prefWidthProperty().bind(tableView.widthProperty().multiply(.1));
        apptloccol.prefWidthProperty().bind(tableView.widthProperty().multiply(.1));
        apptcontactcol.prefWidthProperty().bind(tableView.widthProperty().multiply(.1));
        appttypecol.prefWidthProperty().bind(tableView.widthProperty().multiply(.1));
        apptstartcol.prefWidthProperty().bind(tableView.widthProperty().multiply(.1));
        apptendcol.prefWidthProperty().bind(tableView.widthProperty().multiply(.1));
        apptcxidcol.prefWidthProperty().bind(tableView.widthProperty().multiply(.05));
        apptusercol.prefWidthProperty().bind(tableView.widthProperty().multiply(.05));
        apptcreatecol.prefWidthProperty().bind(tableView.widthProperty().multiply(.1));
        apptcreatedbycol.prefWidthProperty().bind(tableView.widthProperty().multiply(.05));
        apptlastupdatecol.prefWidthProperty().bind(tableView.widthProperty().multiply(.1));
        apptlastupdatedbycol.prefWidthProperty().bind(tableView.widthProperty().multiply(.08));


        tableView.getColumns().add(apptidcol);
        tableView.getColumns().add(appttitlecol);
        tableView.getColumns().add(apptdesccol);
        tableView.getColumns().add(apptloccol);
        tableView.getColumns().add(apptcontactcol);
        tableView.getColumns().add(appttypecol);
        tableView.getColumns().add(apptstartcol);
        tableView.getColumns().add(apptendcol);
        tableView.getColumns().add(apptcxidcol);
        tableView.getColumns().add(apptusercol);
        tableView.getColumns().add(apptcreatecol);
        tableView.getColumns().add(apptcreatedbycol);
        tableView.getColumns().add(apptlastupdatecol);
        tableView.getColumns().add(apptlastupdatedbycol);


        apptidcol.setStyle("-fx-alignment: CENTER;");
        apptcxidcol.setStyle("-fx-alignment: CENTER;");
        apptusercol.setStyle("-fx-alignment: CENTER;");

        alert.getDialogPane().setContent(tableView);

        ObservableList<Appointments> masterData = new AppointmentsDAO()
                .displayAllAppointmentsUnderUserId(Utility.getCurrentUserIdValue());
        FilteredList<Appointments> filteredList = new FilteredList<>(masterData,
                p -> (!((p.getApptCreatedBy()
                        .equals(Main.getLoginUserHolder())))
                        || (!(p.getApptLastUpdatedBy()
                        .equals(Main.getLoginUserHolder()))))); // sets filtered list to appointments created by and/ or updated by another user than the current user
        SortedList<Appointments> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        if(sortedList.isEmpty()){
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("FOR YOUR INFORMATION");
            alert1.setHeaderText("No appointments exist that were created and/or updated by another user.");
            alert1.show();
        }
        else{
            tableView.setItems(sortedList);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(reportAlertStageHolder);
            alert.show();
        }
    }

    private static void apptContactReport() { // generates report based on contact chosen
        List<String> choices = new ContactsDAO()
                .displayAllContacts()
                .stream()
                .map(Contacts::getContactName)
                .collect(Collectors.toList());
        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0),choices);
        dialog.setTitle("Choose the Contact");
        dialog.setContentText("Generate appointment report for contact: ");
        dialog.setHeaderText(null);
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(reportAlertStageHolder);
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(Main::generateReportForContact);
    }

    private static void generateReportForContact(String s) { // displays TableView for appointments assigned to chosen contact
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Appointments for Contact: "+s);
        alert.setHeaderText(null);
        alert.setContentText(null);
        alert.setResizable(true);

        TableView<Appointments> tableView = new TableView<>();
        tableView.setMinHeight(100);
        tableView.setMinWidth(800);

        TableColumn<Appointments, Integer> apptidcol = new TableColumn<>("Appt ID");
        TableColumn<Appointments, String> appttitlecol = new TableColumn<>("Title");
        TableColumn<Appointments, String> apptdesccol = new TableColumn<>("Desc");
        TableColumn<Appointments,String> apptcontactcol = new TableColumn<>("Contact");
        TableColumn<Appointments,String> appttypecol = new TableColumn<>("Type");
        TableColumn<Appointments, LocalDateTime> apptstartcol = new TableColumn<>("Begins");
        TableColumn<Appointments, LocalDateTime> apptendcol = new TableColumn<>("Ends");
        TableColumn<Appointments, Integer> apptcxidcol = new TableColumn<>("Cust ID");

        apptidcol.setCellValueFactory(cellData -> cellData.getValue().apptIdProperty().asObject());
        appttitlecol.setCellValueFactory(cellData -> cellData.getValue().apptTitleProperty());
        apptdesccol.setCellValueFactory(cellData -> cellData.getValue().apptDescProperty());
        apptcontactcol.setCellValueFactory(cellData -> cellData.getValue().contactNameProperty());
        appttypecol.setCellValueFactory(cellData -> cellData.getValue().apptTypeProperty());
        apptstartcol.setCellValueFactory(cellData -> cellData.getValue().apptStartTimeProperty());
        apptendcol.setCellValueFactory(cellData -> cellData.getValue().apptEndTimeProperty());
        apptcxidcol.setCellValueFactory(cellData -> cellData.getValue().cxIdProperty().asObject());

        tableView.getColumns().add(apptidcol);
        tableView.getColumns().add(appttitlecol);
        tableView.getColumns().add(apptdesccol);
        tableView.getColumns().add(apptcontactcol);
        tableView.getColumns().add(appttypecol);
        tableView.getColumns().add(apptstartcol);
        tableView.getColumns().add(apptendcol);
        tableView.getColumns().add(apptcxidcol);


        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        apptidcol.setStyle("-fx-alignment: CENTER;");
        apptcxidcol.setStyle("-fx-alignment: CENTER;");

        alert.getDialogPane().setContent(tableView);

        ObservableList<Appointments> masterData = new AppointmentsDAO().displayAllAppointments();
        FilteredList<Appointments> filteredList = new FilteredList<>(masterData,
                p -> p.getContactName().equals(s));
        SortedList<Appointments> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        if(sortedList.isEmpty()){
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("FOR YOUR INFORMATION");
            alert1.setHeaderText("No appointments exist for Contact : "+s);
            alert1.show();
        }
        else{
            tableView.setItems(sortedList);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(reportAlertStageHolder);
            alert.show();
        }
    }

    private static void apptTypeReport() { // generates report showing all appointments by type
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Appointments by Type and Filtered by Date.");
        alert.setHeaderText(null);
        alert.setContentText(null);
        alert.setResizable(false);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(reportAlertStageHolder);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        final List<String> apptTypes = List.of("Planning Session","De-Briefing","Meal","War Room");

        Label allAppts = new Label("Total appointments: ");
        Label allApptsHolder = new Label();
        Label type1Appts = new Label(apptTypes.get(0)+" Total #");
        Label type1ApptsHolder = new Label();
        Label type2Appts = new Label(apptTypes.get(1)+" Total #");
        Label type2ApptsHolder = new Label();
        Label type3Appts = new Label(apptTypes.get(2)+" Total #");
        Label type3ApptsHolder = new Label();
        Label type4Appts = new Label(apptTypes.get(3)+" Total #");
        Label type4ApptsHolder = new Label();
        ComboBox<String> yearviewcombobox = new ComboBox<>();
        ComboBox<String> monthviewcombobox = new ComboBox<>();
        ComboBox<String> weekviewcombobox = new ComboBox<>();
        ComboBox<String> filterbycombobox = new ComboBox<>();
        HBox h1 = new HBox(yearviewcombobox, monthviewcombobox);
        h1.setSpacing(15);
        Button filterButton = new Button("FILTER");
        HBox h2 = new HBox(filterbycombobox,filterButton);
        h2.setSpacing(15);

        grid.add(allAppts,0,0);
        grid.add(allApptsHolder,1,0);
        grid.add(type1Appts,0,1);
        grid.add(type1ApptsHolder,1,1);
        grid.add(type2Appts,0,2);
        grid.add(type2ApptsHolder,1,2);
        grid.add(type3Appts,0,3);
        grid.add(type3ApptsHolder,1,3);
        grid.add(type4Appts,0,4);
        grid.add(type4ApptsHolder,1,4);
        grid.add(h1,0,5);
        grid.add(weekviewcombobox,0,6);
        grid.add(h2,0,7);

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col1.setPercentWidth(75);
        col2.setPercentWidth(25);
        grid.getColumnConstraints().addAll(col1,col2);

        //sets observable list to be observed when changes occur to populate data automatically
        ObservableList<Appointments> masterData = FXCollections.observableArrayList(appointments -> new Observable[]{
                appointments.apptIdProperty(),
                appointments.apptTypeProperty(),
                appointments.apptDescProperty(),
                appointments.contactNameProperty(),
                appointments.apptTypeProperty(),
                appointments.apptStartTimeProperty(),
                appointments.apptEndTimeProperty(),
                appointments.cxIdProperty()
        });
        List<Appointments> list = List.copyOf(new AppointmentsDAO().displayAllAppointments());
        masterData.addAll(list);

        FilteredList<Appointments> filteredList = new FilteredList<>(masterData, p -> false );
        filteredList.addListener((ListChangeListener<Appointments>) change -> {// listener runs code to update TextFields each time list changes
            while(change.next()){
                if(change.wasPermutated()){
                    int type1 = 0, type2 =0 , type3 = 0, type4 = 0, all = 0;
                    for (Appointments appointments : filteredList) {
                        all++;
                        if (appointments.getApptType().equals(apptTypes.get(0)))
                            type1++;
                        else if (appointments.getApptType().equals(apptTypes.get(1)))
                            type2++;
                        else if (appointments.getApptType().equals(apptTypes.get(2)))
                            type3++;
                        else
                            type4++;
                    }
                    allApptsHolder.setText(String.valueOf(all));
                    type1ApptsHolder.setText(String.valueOf(type1));
                    type2ApptsHolder.setText(String.valueOf(type2));
                    type3ApptsHolder.setText(String.valueOf(type3));
                    type4ApptsHolder.setText(String.valueOf(type4));
                }
                else if(change.wasUpdated()){
                    int type1 = 0, type2 =0 , type3 = 0, type4 = 0, all = 0;
                    for (Appointments appointments : filteredList) {
                        all++;
                        if (appointments.getApptType().equals(apptTypes.get(0)))
                            type1++;
                        else if (appointments.getApptType().equals(apptTypes.get(1)))
                            type2++;
                        else if (appointments.getApptType().equals(apptTypes.get(2)))
                            type3++;
                        else
                            type4++;
                    }
                    allApptsHolder.setText(String.valueOf(all));
                    type1ApptsHolder.setText(String.valueOf(type1));
                    type2ApptsHolder.setText(String.valueOf(type2));
                    type3ApptsHolder.setText(String.valueOf(type3));
                    type4ApptsHolder.setText(String.valueOf(type4));
            }
                else{
                    int type1 = 0, type2 =0 , type3 = 0, type4 = 0, all = 0;
                    for (Appointments appointments : change.getAddedSubList()) {
                        all++;
                        if (appointments.getApptType().equals(apptTypes.get(0)))
                            type1++;
                        else if (appointments.getApptType().equals(apptTypes.get(1)))
                            type2++;
                        else if (appointments.getApptType().equals(apptTypes.get(2)))
                            type3++;
                        else
                            type4++;
                    }
                    allApptsHolder.setText(String.valueOf(all));
                    type1ApptsHolder.setText(String.valueOf(type1));
                    type2ApptsHolder.setText(String.valueOf(type2));
                    type3ApptsHolder.setText(String.valueOf(type3));
                    type4ApptsHolder.setText(String.valueOf(type4));
                }
        }
    });
        filteredList.setPredicate(p->true);
        filterButton.setOnAction(event -> { //filters list based on chosen filter
            String value = filterbycombobox.getSelectionModel().getSelectedItem();
            if(value==null){
                filteredList.setPredicate(customers -> true);
            }
            else{
                switch (value){
                    case "YEAR":{
                        filteredList.setPredicate(appointments -> {
                            String temp = yearviewcombobox.getSelectionModel().getSelectedItem();
                            long diff = Math.abs(ChronoUnit.YEARS
                                    .between(Year.parse(temp),appointments.getApptStartTime()));
                            long diff2 = Math.abs(ChronoUnit.YEARS
                                    .between(Year.parse(temp),appointments.getApptEndTime()));
                            return (diff+diff2)<=1;
                        });
                        break;
                    }
                    case "YEAR and MONTH":{
                        filteredList.setPredicate(appointments -> {
                            String temp = yearviewcombobox.getSelectionModel().getSelectedItem();
                            String selectedItem = monthviewcombobox.getSelectionModel().getSelectedItem();
                            long diff = Math.abs(ChronoUnit.MONTHS
                                    .between((YearMonth.of(Integer.parseInt(temp),
                                            Month.valueOf(selectedItem).getValue())),appointments.getApptStartTime()));
                            long diff2 = Math.abs(ChronoUnit.MONTHS
                                    .between((YearMonth.of(Integer.parseInt(temp),
                                            Month.valueOf(selectedItem).getValue())),appointments.getApptEndTime()));
                            return (diff+diff2)<=1;
                        });
                        break;
                    }
                    case "WEEK":{
                        String temp = weekviewcombobox.getSelectionModel().getSelectedItem();
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
                        break;
                    }
                    default:{
                        filteredList.setPredicate(p-> true);
                        break;
                    }
                }
            }
        });


        List<String> years = List.of("2021","2022","2023","2024","2025");
        yearviewcombobox.getItems().addAll(years);
        List<String> months = List.of("JANUARY","FEBRUARY","MARCH","APRIL","MAY","JUNE","JULY"
                ,"AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER");
        monthviewcombobox.getItems().addAll(months);
        weekviewcombobox.getItems().addAll(Utility.LocalizedWeek.createWeekComboBoxBasedOnYear("2021"));
        yearviewcombobox.valueProperty().addListener((observableValue, string, t1) -> {
            weekviewcombobox.getItems().clear();
            weekviewcombobox.getItems().addAll(Utility.LocalizedWeek.createWeekComboBoxBasedOnYear(t1));
            weekviewcombobox.getSelectionModel().selectFirst();
        });
        filterbycombobox.getItems().addAll("YEAR", "YEAR and MONTH", "WEEK", "ALL");

        yearviewcombobox.getSelectionModel().selectFirst();
        monthviewcombobox.getSelectionModel().selectFirst();
        filterbycombobox.getSelectionModel().select("ALL");
        alert.getDialogPane().setContent(grid);
        alert.show();
    }

    /**
     * Opens pop up scene for appointment reminder based on time launched
     * @param list takes list of appointments for the current user
     */
    public static void showPopUpReportView(ObservableList<Appointments> list){
        try{
            PopUpViewController.setFilteredList(list);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/popupreportview.fxml"));
            AnchorPane popUpView = loader.load();
            Scene scene = new Scene(popUpView);
            popUpStage = new Stage();
            popUpStage.setScene(scene);
            popUpStage.initModality(Modality.APPLICATION_MODAL);
            popUpStage.setTitle(getLoginUserHolder()+" (s)'s Custom Generated Appointment Reminders ");
            popUpStage.initOwner(primaryStage);
            popUpStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets current username that has logged in
     * @param text current username
     */
    public static void setLoginUserHolder(String text){userHolder=text;}

    /**
     * Gets current username that has loggin in
     * @return current username
     */
    public static String getLoginUserHolder(){return userHolder;}
}

//8-10-21 work on Countries and FirstLevelDivision next then Customers and Appointments :)

//08-11-21 work on DAO classes next :) Utility and afterwards GUI and controllers :)

//8-13-21 finish up Utility before starting GUI stuffz..
// SQL is case insensitive.... :)
// login.activity file and properties as well :)

//8-14-21 add locales to login page :)

// 8/16/21 add loginviewcontroller labels to properties file and complete fr properties then test :)

// 8/17/21 continue working on mainscreencontroller :)

//crashes when going to main screen from login.. username passed through incorrectly??

//crashes when trying to format LocalDateTimes in appointment table in main screen :) debug?

//fix year and month filtering on mainscreen controller.. all else works :)

// 08/22/21 crashes when opening up mainscreen during checking for upcoming appointments..
// added breakpoint for NullpointerException...maybe from list being floated around :)

// 8/24/21 userview.fxml broken.. will not even open the actual fxml doc

// 8/25/21 I broke it.... passwordhash generate hash, bytes to string change :)

// 8/26/21 Got password hash stuffz to work... now to fix add new user and double check delete user :)

// 8/27/21 User screen completed... work on customer screen and get stuff going :)

// 8/28/21 Customer screen...add data for country and first level division next :) search bar complete.

// 8/28/21 Customer table fixed.. add, edit and delete customer next :)

// 8/31/21 yep... times not working right in new appt screen.

// 9/2/21 new and edit fixed :) add columns for appointment screen for create date, created by, last update, last updated by ..

// 9/5/21 writing up JavaDocs and final review. updated for local DB usage as well for later.