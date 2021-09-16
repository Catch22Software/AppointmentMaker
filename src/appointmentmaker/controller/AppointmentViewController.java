package appointmentmaker.controller;

import appointmentmaker.Main;
import appointmentmaker.model.Appointments;
import appointmentmaker.model.Contacts;
import appointmentmaker.model.Customers;
import appointmentmaker.model.Users;
import appointmentmaker.modeldao.AppointmentsDAO;
import appointmentmaker.modeldao.ContactsDAO;
import appointmentmaker.modeldao.CustomersDAO;
import appointmentmaker.modeldao.UsersDAO;
import appointmentmaker.utility.Utility;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.text.DecimalFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller class for appointment setting screen
 */
public class AppointmentViewController {


    @FXML
    private TextField apptsearchfield;
    @FXML
    private ComboBox<String> searchbycombobox;
    @FXML
    private ComboBox<String> filterbycombobox;
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
    @FXML
    private TableColumn<Appointments, LocalDateTime> apptcreatecol;
    @FXML
    private TableColumn<Appointments, String> apptcreatedbycol;
    @FXML
    private TableColumn<Appointments, LocalDateTime> apptlastupdatecol;
    @FXML
    private TableColumn<Appointments, String > apptlastupdatedbycol;
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
    private final List<String> apptTypes = List.of("Planning Session","De-Briefing","Meal","War Room");

    @FXML
    private void initialize(){
        setTableColData();
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

        searchbycombobox.getItems().addAll("Appt ID","Appt Title","Appt Type","Cust ID","User ID","Contact Name");
        searchbycombobox.getSelectionModel().selectFirst();
        apptsearchfield.textProperty().addListener(((observableValue, s, t1) -> { // searches dynamically based on combo box selection for which column.
            String value = searchbycombobox.getSelectionModel().getSelectedItem();
            if(t1.trim().isEmpty()){
                filteredList.setPredicate(customers -> true);
            }
            else{
                switch (value){
                    case "Appt ID":{
                        filteredList.setPredicate(p-> Integer.valueOf(p.getApptId()).toString().equals(t1.trim().toLowerCase()));
                        break;
                    }
                    case "Appt Title":{
                        filteredList.setPredicate(p->p.getApptTitle().trim().toLowerCase().contains(t1.trim().toLowerCase()));
                        break;
                    }
                    case "Appt Type":{
                        filteredList.setPredicate(p->p.getApptType().trim().toLowerCase().contains(t1.trim().toLowerCase()));
                        break;
                    }
                    case "Cust ID":{
                        filteredList.setPredicate(p-> Integer.valueOf(p.getCxId()).toString().equals(t1.trim().toLowerCase()));
                        break;
                    }
                    case "User ID":{
                        filteredList.setPredicate(p-> Integer.valueOf(p.getUserId()).toString().equals(t1.trim().toLowerCase()));
                        break;
                    }
                    case "Contact Name":{
                        filteredList.setPredicate(p->p.getContactName().trim().toLowerCase().contains(t1.trim().toLowerCase()));
                        break;
                    }
                    default:{
                        filteredList.setPredicate(p-> true);
                        break;
                    }
                }
            }
        }));
        setTableData();
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
        apptcreatecol.setCellValueFactory(c -> c.getValue().apptCreateTimeProperty());
        apptcreatedbycol.setCellValueFactory(c-> c.getValue().apptCreatedByProperty());
        apptlastupdatecol.setCellValueFactory(c-> c.getValue().apptLastUpdateTimeProperty());
        apptlastupdatedbycol.setCellValueFactory(c-> c.getValue().apptLastUpdatedByProperty());


        apptidcol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.05));
        appttitlecol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.1));
        apptdesccol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.1));
        apptloccol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.1));
        apptcontactcol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.1));
        appttypecol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.1));
        apptstartcol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.1));
        apptendcol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.1));
        apptcxidcol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.05));
        apptusercol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.05));
        apptcreatecol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.1));
        apptcreatedbycol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.05));
        apptlastupdatecol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.1));
        apptlastupdatedbycol.prefWidthProperty().bind(apptviewtable.widthProperty().multiply(.08));

        apptidcol.setStyle("-fx-alignment: CENTER;");
        apptcxidcol.setStyle("-fx-alignment: CENTER;");
        apptusercol.setStyle("-fx-alignment: CENTER;");

    }

    @FXML
    private void handleNewAppointmentAction(ActionEvent actionEvent) { // custom new appointment popup
        Dialog<Appointments> dialog = new Dialog<>();
        var title = "NEW APPOINTMENT FORM";
        var header = "Please carefully enter all fields. Times are user based. ";
        dialog.setTitle(title);
        dialog.setHeaderText(header);

        ButtonType createButtonType = new ButtonType("CREATE", ButtonBar.ButtonData.OK_DONE);
        ButtonType selectButtonType = new ButtonType("SELECT", ButtonBar.ButtonData.NEXT_FORWARD);
        dialog.getDialogPane().getButtonTypes().addAll(selectButtonType,createButtonType,ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();

        col1.setPercentWidth(50.0);
        col2.setPercentWidth(50.0);
        grid.getColumnConstraints().addAll(col1,col2);

        TextField apptId = new TextField("FIXED");
        apptId.setDisable(true);
        TextField apptTitle = new TextField();
        apptTitle.setPromptText("Enter Appointment Title Please: ");
        TextField apptDesc = new TextField();
        apptDesc.setPromptText("Enter Appointment Description Summary Please: ");
        TextField apptLoc = new TextField();
        apptLoc.setPromptText("Enter Appointment Location Please: ");
        ComboBox<String> apptContact  = new ComboBox<>();
        ComboBox<String> apptType = new ComboBox<>();
        DatePicker apptStart = new DatePicker();
        ComboBox<String> startHours = new ComboBox<>();
        ComboBox<String> startMins = new ComboBox<>();
        HBox start = new HBox(startHours,startMins);
        start.setSpacing(10);
        ComboBox<String> endHours = new ComboBox<>();
        ComboBox<String> endMins = new ComboBox<>();
        HBox end = new HBox(endHours,endMins);
        end.setSpacing(10);
        apptStart.setEditable(false);
        DatePicker apptEnd = new DatePicker();
        apptEnd.setEditable(false);
        TextField apptCustId = new TextField();
        apptCustId.setPromptText("Please Click Button To Right To Select Customer: ");
        apptCustId.setEditable(false);
        ComboBox<String> apptUserId = new ComboBox<>();
        Label errLabel = new Label("Start Date and Time must be before End Date and Time!!!");
        errLabel.setStyle("-fx-text-fill: #ff0000;");
        // creates custom label with Business hours adjusted for system default time.
        LocalDateTime busStart = LocalDateTime.parse("2021-01-01T08:00:00");
        LocalDateTime busEnd = LocalDateTime.parse("2021-01-01T22:00:00");
        ZoneId oldZone = ZoneId.of("America/New_York");
        ZoneId newZone = ZoneId.systemDefault();
        LocalTime adjBusStart = busStart.atZone(oldZone).withZoneSameInstant(newZone).toLocalTime();
        LocalTime adjBusEnd = busEnd.atZone(oldZone).withZoneSameInstant(newZone).toLocalTime();
        Label hourLabel = new Label("Business hours adjusted to \n User's local time is Monday-Sunday "+adjBusStart+" to "+adjBusEnd+" : ");

        grid.add(new Label("Appointment ID: "),0,0);
        grid.add(apptId,1,0);
        grid.add(new Label(" Title: "),0,1);
        grid.add(apptTitle,1,1);
        grid.add(new Label("Description: "),0,2);
        grid.add(apptDesc,1,2);
        grid.add(new Label("Location: "),0,3);
        grid.add(apptLoc,1,3);
        grid.add(new Label("Contact Name: "),0,4);
        grid.add(apptContact,1,4);
        grid.add(new Label("Type: "),0,5);
        grid.add(apptType,1,5);
        grid.add(new Label("Start Time: "),0,6);
        grid.add(apptStart,1,6);
        grid.add(start,2,6);
        grid.add(new Label("End Time: "),0,7);
        grid.add(apptEnd,1,7);
        grid.add(end,2,7);
        grid.add(new Label("Customer ID: Use SELECT Button: "),0,8);
        grid.add(apptCustId,1,8);
        grid.add(new Label("User ID: "),0,9);
        grid.add(apptUserId,1,9);
        grid.add(hourLabel,0,10);
        grid.add(errLabel,0,11);

        apptStart.setValue(LocalDate.now()); // code below restricts user to be unable to select an end date that is
        // before the chosen start date and also sets the start date to the current date.
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(apptStart.getValue())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };

        final Callback<DatePicker,DateCell> dayCellFactoryStart = new Callback<>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };

        apptEnd.setDayCellFactory(dayCellFactory);
        apptEnd.setValue(apptStart.getValue());

        apptStart.setDayCellFactory(dayCellFactoryStart);

        dialog.getDialogPane().setContent(grid);

        dialog.getDialogPane().lookupButton(createButtonType) // disables create button until all fields are not NULL
                .disableProperty().bind(Bindings.createBooleanBinding(()->
                                apptTitle.getText().trim().isEmpty()
                                        || apptDesc.getText().trim().isEmpty()
                                        || apptLoc.getText().trim().isEmpty()
                                        || apptCustId.getText().trim().isEmpty()
                                        || errLabel.isVisible()
                        ,apptTitle.textProperty()
                        ,apptDesc.textProperty()
                        ,apptLoc.textProperty()
                        ,apptCustId.textProperty()
                        ,errLabel.visibleProperty()
                ));
        DecimalFormat df = new DecimalFormat(); // populates the hour and minute combo boxes for start and end times.
        df.setMinimumIntegerDigits(2);
        List<Integer> hours = new ArrayList<>();
        for(int i=0;i<25;i++)
            hours.add(i);
        List<Integer> minutes = new ArrayList<>();
        for(int i=0;i<60;i++)
            minutes.add(i);
        for (Integer hour : hours) {
            startHours.getItems().add(df.format(hour));
            endHours.getItems().add(df.format(hour));
        }
        for(Integer minute : minutes){
            startMins.getItems().add(df.format(minute));
            endMins.getItems().add(df.format(minute));
        }
        startHours.getSelectionModel().selectFirst();
        startMins.getSelectionModel().selectFirst();
        endHours.getSelectionModel().selectFirst();
        endMins.getSelectionModel().select(1);
        startHours.setVisibleRowCount(6);
        startMins.setVisibleRowCount(10);
        endHours.setVisibleRowCount(6);
        endMins.setVisibleRowCount(10);

        List<String> names = new ContactsDAO()
                .displayAllContacts()
                .stream()
                .map(Contacts::getContactName)
                .collect(Collectors.toList());
        apptContact.getItems().addAll(names);
        apptContact.getSelectionModel().selectFirst();

        apptType.getItems().addAll(apptTypes);
        apptType.getSelectionModel().selectFirst();

        List<String> unames = new UsersDAO()
                .displayAllUsers()
                .stream()
                .map(Users::getUserName)
                .collect(Collectors.toList());
        apptUserId.getItems().addAll(unames);
        apptUserId.getSelectionModel().selectFirst();
        final int[] cxId = new int[1]; // holds the customer id value since user sees the customer name chosen

        Button getCustomer = (Button)dialog.getDialogPane().lookupButton(selectButtonType);

        errLabel.visibleProperty().bind(Bindings.createBooleanBinding(()-> // customer error message shows if times chosen are outside the business hours
             (LocalDateTime.of(apptStart.getValue()
                             ,LocalTime.parse(startHours.getSelectionModel().getSelectedItem()
                    .concat(":")
                    .concat(startMins.getSelectionModel().getSelectedItem())))
                     .isAfter(LocalDateTime.of(apptEnd.getValue(),
                             LocalTime.parse(endHours.getSelectionModel().getSelectedItem()
                    .concat(":")
                    .concat(endMins.getSelectionModel().getSelectedItem())))))
                    || (LocalDateTime.of(apptStart.getValue(),
                     LocalTime.parse(startHours.getSelectionModel().getSelectedItem()
                    .concat(":")
                    .concat(startMins.getSelectionModel().getSelectedItem())))
                     .isEqual(LocalDateTime.of(apptEnd.getValue(),
                             LocalTime.parse(endHours.getSelectionModel().getSelectedItem()
                    .concat(":")
                    .concat(endMins.getSelectionModel().getSelectedItem())))))
            ,startHours.valueProperty()
                    ,startMins.valueProperty()
                    ,endHours.valueProperty()
                    ,endMins.valueProperty()
                    ,apptStart.valueProperty()
                    ,apptEnd.valueProperty()
        ));

            EventHandler<ActionEvent> eventHandler = (event) -> { // event handler to kill the window close event and open the appropriate customer selection screen for the appointment
            event.consume();
            Dialog<String> custDialog = new Dialog<>();
            var cxtitle = "CHOOSE CUSTOMER FOR APPT";
            custDialog.setTitle(cxtitle);
            custDialog.setHeaderText(null);
            custDialog.setResizable(true);
            custDialog.setHeight(400);
            custDialog.setWidth(800);
            custDialog.getDialogPane().setMinHeight(400);
            custDialog.getDialogPane().setMinWidth(800);

            ButtonType selectButtonType1 = new ButtonType("SELECT", ButtonBar.ButtonData.OK_DONE);
            custDialog.getDialogPane().getButtonTypes().addAll(selectButtonType1,ButtonType.CANCEL);

            TableView<Customers> customersTableView = new TableView<>();
            ObservableList<Customers> tableData;

            TableColumn<Customers,Integer> custidcol = new TableColumn<>("Cust ID:");
            TableColumn<Customers,String> custnamecol = new TableColumn<>("Cust Name:");
            TableColumn<Customers,String> custaddresscol = new TableColumn<>("Cust Address:");
            TableColumn<Customers,String> custpostalcol = new TableColumn<>("Cust Postal:");
            TableColumn<Customers,String> custphonecol = new TableColumn<>("Cust Phone:");
            TableColumn<Customers,String> custcountrynamecol = new TableColumn<>("Cust Country:");
            TableColumn<Customers,String> custdivisionnamecol = new TableColumn<>("Cust Division:");

            custidcol.setCellValueFactory(cellData -> cellData.getValue().custIdProperty().asObject());
            custnamecol.setCellValueFactory(cellData -> cellData.getValue().custNameProperty());
            custaddresscol.setCellValueFactory(cellData -> cellData.getValue().custAddressProperty());
            custpostalcol.setCellValueFactory(cellData -> cellData.getValue().custPostalCodeProperty());
            custphonecol.setCellValueFactory(cellData -> cellData.getValue().custPhoneNumberProperty());
            custcountrynamecol.setCellValueFactory(c-> c.getValue().getCountries().countryNameProperty());
            custdivisionnamecol.setCellValueFactory(c->c.getValue().getFirstLevelDivision().divisionNameProperty());

            custidcol.setStyle("-fx-alignment: CENTER;");


            customersTableView.getColumns().add(custidcol);
            customersTableView.getColumns().add(custnamecol);
            customersTableView.getColumns().add(custaddresscol);
            customersTableView.getColumns().add(custpostalcol);
            customersTableView.getColumns().add(custphonecol);
            customersTableView.getColumns().add(custcountrynamecol);
            customersTableView.getColumns().add(custdivisionnamecol);


            custidcol.prefWidthProperty().bind(customersTableView.widthProperty().multiply(.05));
            custnamecol.prefWidthProperty().bind(customersTableView.widthProperty().multiply(.1));
            custaddresscol.prefWidthProperty().bind(customersTableView.widthProperty().multiply(.1));
            custpostalcol.prefWidthProperty().bind(customersTableView.widthProperty().multiply(.1));
            custphonecol.prefWidthProperty().bind(customersTableView.widthProperty().multiply(.1));
            custcountrynamecol.prefWidthProperty().bind(customersTableView.widthProperty().multiply(.1));
            custdivisionnamecol.prefWidthProperty().bind(customersTableView.widthProperty().multiply(.1));

            custDialog.getDialogPane().setContent(customersTableView);


            tableData = new CustomersDAO().displayAllCustomers();
            List<Customers> holder = tableData
                    .stream()
                    .map(new CustomersDAO()::getCustomerDivNameAndCountryName)
                    .collect(Collectors.toList());

            tableData = FXCollections.observableArrayList(holder);
            FilteredList<Customers> filteredList1 = new FilteredList<>(tableData,p->true);
            SortedList<Customers> sortedList1 = new SortedList<>(filteredList1);
            sortedList1.comparatorProperty().bind(customersTableView.comparatorProperty());
            customersTableView.setItems(sortedList1);
            customersTableView.getSelectionModel().selectFirst();

            custDialog.setResultConverter(b -> {
                if(b==selectButtonType1){
                    cxId[0] = customersTableView.getSelectionModel().getSelectedItem().getCustId();
                    return customersTableView.getSelectionModel().getSelectedItem().getCustName();
                }
                return null;
            });

            Optional<String> result = custDialog.showAndWait();

            result.ifPresent(apptCustId::setText);
        };

            getCustomer.addEventFilter(ActionEvent.ACTION,eventHandler);



        dialog.setResultConverter(b -> {
            if(b==createButtonType){
                var appt = new Appointments();
                appt.setApptTitle(apptTitle.getText());
                appt.setApptDesc(apptDesc.getText());
                appt.setApptLocation(apptLoc.getText());
                appt.setApptType(apptType.getSelectionModel().getSelectedItem());
                String startString = startHours.getSelectionModel().getSelectedItem()
                        .concat(":")
                        .concat(startMins.getSelectionModel().getSelectedItem());
                LocalTime startTime = LocalTime.parse(startString);
                LocalDate startDate = apptStart.getValue();
                String endString = endHours.getSelectionModel().getSelectedItem()
                        .concat(":")
                        .concat(endMins.getSelectionModel().getSelectedItem());
                LocalTime endTime = LocalTime.parse(endString);
                LocalDate endDate = apptEnd.getValue();
                appt.setApptStartTime(LocalDateTime.of(startDate,startTime));
                appt.setApptEndTime(LocalDateTime.of(endDate,endTime));
                appt.setApptCreateTime(LocalDateTime.now());
                appt.setApptCreatedBy(Main.getLoginUserHolder());
                appt.setApptLastUpdateTime(LocalDateTime.now());
                appt.setApptLastUpdatedBy(Main.getLoginUserHolder());
                appt.setCxId(cxId[0]);
                appt.setUserId(new UsersDAO().displayOneUser(apptUserId.getSelectionModel().getSelectedItem()).getUserId());
                appt.setContactId(new ContactsDAO().displayOneContact(apptContact.getSelectionModel().getSelectedItem()).getContactId());
                return appt;
            }
            else if(b==selectButtonType){
                getCustomer.fire();
            }
            return null;
        });
        Optional<Appointments> result = dialog.showAndWait(); // holds the appointment object if create is chosen

        result.ifPresent(appointments -> {
            if(Appointments.isApptTimesAllowedAgainstList(masterTableData,appointments,null)){ // checks new appt against database list for the selected customer
                if(isApptBetweenBusinessHours(appointments)){ // checks new appt against the business hours
                    int didItWork = dbappt.createAppointment(appointments);
                    if (didItWork != 1) {
                        final var info1 = "Something strange happened.. Please try again."; // should never see this
                        Alert alert1 = new Alert(Alert.AlertType.ERROR, info1);
                        alert1.setTitle("");
                        alert1.setHeaderText("");
                        alert1.setResizable(false);
                        alert1.show();
                    } else {
                        final var info1 = "Appointment : " + appointments.getApptTitle() + " successfully created. ";
                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION, info1);
                        alert1.setTitle("");
                        alert1.setHeaderText("");
                        alert1.setResizable(false);
                        alert1.show();
                    }
                }
            else{
                final var info1 = "You must set appointment between business hours!!";
                Alert alert1 = new Alert(Alert.AlertType.ERROR, info1);
                alert1.setTitle("");
                alert1.setHeaderText("Business hours are Monday-Sunday 8:00am EST - 10:00pm EST");
                alert1.setResizable(false);
                alert1.show();
            }
            }
            else{
                final var info1 = "You cannot put the same customer in two different places at the same time!!!"; // error if try to overlap appointments with same customer
                Alert alert1 = new Alert(Alert.AlertType.ERROR, info1);
                alert1.setTitle(null);
                alert1.setHeaderText(null);
                alert1.setResizable(false);
                alert1.show();
            }
        });
        setTableData();
    }

    @FXML
    private void handleEditAppointmentAction(ActionEvent actionEvent) { // customer dialog box for editing appointments. code is similar to create appointment
        if(apptviewtable.getSelectionModel().getSelectedItem()==null){ // checks if appointment was selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            var title = "ERROR ALERT!!!";
            var header = "You cannot edit an appointment that isn't selected!!";
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(header);
            alert.setResizable(false);
            alert.show();
            return;
        }
        ObservableList<Customers> mCustList = new CustomersDAO().displayAllCustomers(); // master cust list for editing appointment
        var custHolder = new Customers();
        var editAppointment = apptviewtable.getSelectionModel().getSelectedItem(); // appointment to potentially edit
        Dialog<Appointments> dialog = new Dialog<>();
        var title = "EDIT APPOINTMENT FORM";
        var header = "Please carefully change all fields. Times are user based. ";
        dialog.setTitle(title);
        dialog.setHeaderText(header);

        ButtonType editButtonType = new ButtonType("EDIT", ButtonBar.ButtonData.OK_DONE);
        ButtonType selectButtonType = new ButtonType("SELECT", ButtonBar.ButtonData.NEXT_FORWARD);
        dialog.getDialogPane().getButtonTypes().addAll(selectButtonType,editButtonType,ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();

        col1.setPercentWidth(50.0);
        col2.setPercentWidth(50.0);
        grid.getColumnConstraints().addAll(col1,col2);

        TextField apptId = new TextField("FIXED");
        apptId.setDisable(true);
        TextField apptTitle = new TextField(editAppointment.getApptTitle());
        apptTitle.setPromptText("Enter Appointment Title Please: ");
        TextField apptDesc = new TextField(editAppointment.getApptDesc());
        apptDesc.setPromptText("Enter Appointment Description Summary Please: ");
        TextField apptLoc = new TextField(editAppointment.getApptLocation());
        apptLoc.setPromptText("Enter Appointment Location Please: ");
        ComboBox<String> apptContact  = new ComboBox<>();
        ComboBox<String> apptType = new ComboBox<>();
        DatePicker apptStart = new DatePicker();
        ComboBox<String> startHours = new ComboBox<>();
        ComboBox<String> startMins = new ComboBox<>();
        HBox start = new HBox(startHours,startMins);
        start.setSpacing(10);
        ComboBox<String> endHours = new ComboBox<>();
        ComboBox<String> endMins = new ComboBox<>();
        HBox end = new HBox(endHours,endMins);
        end.setSpacing(10);
        apptStart.setEditable(false);
        DatePicker apptEnd = new DatePicker();
        apptEnd.setEditable(false);
        TextField apptCustId = new TextField();
        custHolder
                .copyCustomerObject(mCustList
                        .stream()
                        .filter(customers
                                -> editAppointment.getCxId()==customers.getCustId())
                        .limit(1)
                        .collect(Collectors.toList())
                        .get(0));
        apptCustId.setText(custHolder.getCustName());
        apptCustId.setEditable(false);
        ComboBox<String> apptUserId = new ComboBox<>();
        Label errLabel = new Label("Start Date and Time must be before End Date and Time!!!");
        errLabel.setStyle("-fx-text-fill: #ff0000;");
        LocalDateTime busStart = LocalDateTime.parse("2021-01-01T08:00:00");
        LocalDateTime busEnd = LocalDateTime.parse("2021-01-01T22:00:00");
        ZoneId oldZone = ZoneId.of("America/New_York");
        ZoneId newZone = ZoneId.systemDefault();
        LocalTime adjBusStart = busStart.atZone(oldZone).withZoneSameInstant(newZone).toLocalTime();
        LocalTime adjBusEnd = busEnd.atZone(oldZone).withZoneSameInstant(newZone).toLocalTime();
        Label hourLabel = new Label("Business hours adjusted to \n User's local time is Monday-Sunday "+adjBusStart+" to "+adjBusEnd+" : ");

        grid.add(new Label("Appointment ID: "),0,0);
        grid.add(apptId,1,0);
        grid.add(new Label(" Title: "),0,1);
        grid.add(apptTitle,1,1);
        grid.add(new Label("Description: "),0,2);
        grid.add(apptDesc,1,2);
        grid.add(new Label("Location: "),0,3);
        grid.add(apptLoc,1,3);
        grid.add(new Label("Contact Name: "),0,4);
        grid.add(apptContact,1,4);
        grid.add(new Label("Type: "),0,5);
        grid.add(apptType,1,5);
        grid.add(new Label("Start Time: "),0,6);
        grid.add(apptStart,1,6);
        grid.add(start,2,6);
        grid.add(new Label("End Time: "),0,7);
        grid.add(apptEnd,1,7);
        grid.add(end,2,7);
        grid.add(new Label("Customer ID: Use SELECT Button: "),0,8);
        grid.add(apptCustId,1,8);
        grid.add(new Label("User ID: "),0,9);
        grid.add(apptUserId,1,9);
        grid.add(hourLabel,0,10);
        grid.add(errLabel,0,11);

        apptStart.setValue(editAppointment.getApptStartTime().toLocalDate());
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(apptStart.getValue())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };

        final Callback<DatePicker,DateCell> dayCellFactoryStart = new Callback<>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };

        apptEnd.setDayCellFactory(dayCellFactory);
        apptEnd.setValue(editAppointment.getApptEndTime().toLocalDate());

        apptStart.setDayCellFactory(dayCellFactoryStart);

        dialog.getDialogPane().setContent(grid);

        dialog.getDialogPane().lookupButton(editButtonType)
                .disableProperty().bind(Bindings.createBooleanBinding(()->
                                apptTitle.getText().trim().isEmpty()
                                        || apptDesc.getText().trim().isEmpty()
                                        || apptLoc.getText().trim().isEmpty()
                                        || apptCustId.getText().trim().isEmpty()
                                        || errLabel.isVisible()
                        ,apptTitle.textProperty()
                        ,apptDesc.textProperty()
                        ,apptLoc.textProperty()
                        ,apptCustId.textProperty()
                        ,errLabel.visibleProperty()
                ));
        DecimalFormat df = new DecimalFormat();
        df.setMinimumIntegerDigits(2);
        List<Integer> hours = new ArrayList<>();
        for(int i=0;i<25;i++)
            hours.add(i);
        List<Integer> minutes = new ArrayList<>();
        for(int i=0;i<60;i++)
            minutes.add(i);
        for (Integer hour : hours) {
            startHours.getItems().add(df.format(hour));
            endHours.getItems().add(df.format(hour));
        }
        for(Integer minute : minutes){
            startMins.getItems().add(df.format(minute));
            endMins.getItems().add(df.format(minute));
        }
        String begins = editAppointment.getApptStartTime().toLocalTime().toString();
        String ends = editAppointment.getApptEndTime().toLocalTime().toString();
        startHours.getSelectionModel().select(begins.substring(0,2));
        startMins.getSelectionModel().select(begins.substring(3));
        endHours.getSelectionModel().select(ends.substring(0,2));
        endMins.getSelectionModel().select(ends.substring(3));
        startHours.setVisibleRowCount(4);
        startMins.setVisibleRowCount(10);
        endHours.setVisibleRowCount(4);
        endMins.setVisibleRowCount(10);

        ObservableList<Contacts> mContactList = new ContactsDAO().displayAllContacts(); // mlist gets set to customer object that matches the edit appointment customer id
        List<String> names = mContactList
                .stream()
                .map(Contacts::getContactName)
                .collect(Collectors.toList());
        apptContact.getItems().addAll(names);
        String contactName = mContactList
                .stream()
                .filter(u -> u.getContactId()==editAppointment.getContactId())
                .map(Contacts::getContactName)
                .collect(Collectors.toList())
                .get(0);
        apptContact.getSelectionModel().select(contactName);

        apptType.getItems().addAll(apptTypes);
        apptType.getSelectionModel().select(editAppointment.getApptType());

        ObservableList<Users> mUserList = new UsersDAO().displayAllUsers();
        List<String> unames = mUserList
                .stream()
                .map(Users::getUserName)
                .collect(Collectors.toList());
        apptUserId.getItems().addAll(unames);
        String userName = mUserList
                .stream()
                .filter(u -> u.getUserId()==editAppointment.getUserId())
                .map(Users::getUserName)
                .collect(Collectors.toList())
                .get(0);
       apptUserId.getSelectionModel().select(userName);

        Button getCustomer = (Button)dialog.getDialogPane().lookupButton(selectButtonType);

        errLabel.visibleProperty().bind(Bindings.createBooleanBinding(()->
                        (LocalDateTime.of(apptStart.getValue()
                                        ,LocalTime.parse(startHours.getSelectionModel().getSelectedItem()
                                                .concat(":")
                                                .concat(startMins.getSelectionModel().getSelectedItem())))
                                .isAfter(LocalDateTime.of(apptEnd.getValue(),
                                        LocalTime.parse(endHours.getSelectionModel().getSelectedItem()
                                                .concat(":")
                                                .concat(endMins.getSelectionModel().getSelectedItem())))))
                                || (LocalDateTime.of(apptStart.getValue(),
                                        LocalTime.parse(startHours.getSelectionModel().getSelectedItem()
                                                .concat(":")
                                                .concat(startMins.getSelectionModel().getSelectedItem())))
                                .isEqual(LocalDateTime.of(apptEnd.getValue(),
                                        LocalTime.parse(endHours.getSelectionModel().getSelectedItem()
                                                .concat(":")
                                                .concat(endMins.getSelectionModel().getSelectedItem())))))
                ,startHours.valueProperty()
                ,startMins.valueProperty()
                ,endHours.valueProperty()
                ,endMins.valueProperty()
                ,apptStart.valueProperty()
                ,apptEnd.valueProperty()
        ));

        EventHandler<ActionEvent> eventHandler = (event) -> {
            event.consume();
            Dialog<String> custDialog = new Dialog<>();
            var cxtitle = "CHOOSE CUSTOMER FOR APPT";
            custDialog.setTitle(cxtitle);
            custDialog.setHeaderText(null);
            custDialog.setResizable(true);
            custDialog.setHeight(400);
            custDialog.setWidth(800);
            custDialog.getDialogPane().setMinHeight(400);
            custDialog.getDialogPane().setMinWidth(800);

            ButtonType selectButtonType1 = new ButtonType("SELECT", ButtonBar.ButtonData.OK_DONE);
            custDialog.getDialogPane().getButtonTypes().addAll(selectButtonType1,ButtonType.CANCEL);

            TableView<Customers> customersTableView = new TableView<>();
            ObservableList<Customers> tableData;

            TableColumn<Customers,Integer> custidcol = new TableColumn<>("Cust ID:");
            TableColumn<Customers,String> custnamecol = new TableColumn<>("Cust Name:");
            TableColumn<Customers,String> custaddresscol = new TableColumn<>("Cust Address:");
            TableColumn<Customers,String> custpostalcol = new TableColumn<>("Cust Postal:");
            TableColumn<Customers,String> custphonecol = new TableColumn<>("Cust Phone:");
            TableColumn<Customers,String> custcountrynamecol = new TableColumn<>("Cust Country:");
            TableColumn<Customers,String> custdivisionnamecol = new TableColumn<>("Cust Division:");

            custidcol.setCellValueFactory(cellData -> cellData.getValue().custIdProperty().asObject());
            custnamecol.setCellValueFactory(cellData -> cellData.getValue().custNameProperty());
            custaddresscol.setCellValueFactory(cellData -> cellData.getValue().custAddressProperty());
            custpostalcol.setCellValueFactory(cellData -> cellData.getValue().custPostalCodeProperty());
            custphonecol.setCellValueFactory(cellData -> cellData.getValue().custPhoneNumberProperty());
            custcountrynamecol.setCellValueFactory(c-> c.getValue().getCountries().countryNameProperty());
            custdivisionnamecol.setCellValueFactory(c->c.getValue().getFirstLevelDivision().divisionNameProperty());

            custidcol.setStyle("-fx-alignment: CENTER;");


            customersTableView.getColumns().add(custidcol);
            customersTableView.getColumns().add(custnamecol);
            customersTableView.getColumns().add(custaddresscol);
            customersTableView.getColumns().add(custpostalcol);
            customersTableView.getColumns().add(custphonecol);
            customersTableView.getColumns().add(custdivisionnamecol);
            customersTableView.getColumns().add(custcountrynamecol);


            custidcol.prefWidthProperty().bind(customersTableView.widthProperty().multiply(.05));
            custnamecol.prefWidthProperty().bind(customersTableView.widthProperty().multiply(.1));
            custaddresscol.prefWidthProperty().bind(customersTableView.widthProperty().multiply(.1));
            custpostalcol.prefWidthProperty().bind(customersTableView.widthProperty().multiply(.1));
            custphonecol.prefWidthProperty().bind(customersTableView.widthProperty().multiply(.1));
            custcountrynamecol.prefWidthProperty().bind(customersTableView.widthProperty().multiply(.1));
            custdivisionnamecol.prefWidthProperty().bind(customersTableView.widthProperty().multiply(.1));

            custDialog.getDialogPane().setContent(customersTableView);


            tableData = new CustomersDAO().displayAllCustomers();
            List<Customers> holder = tableData
                    .stream()
                    .map(new CustomersDAO()::getCustomerDivNameAndCountryName)
                    .collect(Collectors.toList());

            tableData = FXCollections.observableArrayList(holder);
            FilteredList<Customers> filteredList1 = new FilteredList<>(tableData,p->true);
            SortedList<Customers> sortedList1 = new SortedList<>(filteredList1);
            sortedList1.comparatorProperty().bind(customersTableView.comparatorProperty());
            customersTableView.setItems(sortedList1);

            custDialog.setResultConverter(b -> {
                if(b==selectButtonType1){
                     custHolder.copyCustomerObject(customersTableView.getSelectionModel().getSelectedItem());
                     // copies newly selected customer object to holder object to populate new customer name and id
                     return custHolder.getCustName();
                }
                return null;
            });

            Optional<String> result = custDialog.showAndWait();

            result.ifPresent(apptCustId::setText);
        };

        getCustomer.addEventFilter(ActionEvent.ACTION,eventHandler);


        dialog.setResultConverter(b -> { // creates new appointment object to check against edited and others.
            if(b==editButtonType){
                var appt = new Appointments();
                appt.setApptId(editAppointment.getApptId());
                appt.setApptTitle(apptTitle.getText());
                appt.setApptDesc(apptDesc.getText());
                appt.setApptLocation(apptLoc.getText());
                appt.setApptType(apptType.getSelectionModel().getSelectedItem());
                String startString = startHours.getSelectionModel().getSelectedItem()
                        .concat(":")
                        .concat(startMins.getSelectionModel().getSelectedItem());
                LocalTime startTime = LocalTime.parse(startString);
                LocalDate startDate = apptStart.getValue();
                String endString = endHours.getSelectionModel().getSelectedItem()
                        .concat(":")
                        .concat(endMins.getSelectionModel().getSelectedItem());
                LocalTime endTime = LocalTime.parse(endString);
                LocalDate endDate = apptEnd.getValue();
                appt.setApptStartTime(LocalDateTime.of(startDate,startTime));
                appt.setApptEndTime(LocalDateTime.of(endDate,endTime));
                appt.setApptLastUpdateTime(LocalDateTime.now());
                appt.setApptLastUpdatedBy(Main.getLoginUserHolder());
                appt.setCxId(custHolder.getCustId());
                appt.setUserId(new UsersDAO().displayOneUser(apptUserId.getSelectionModel().getSelectedItem()).getUserId());
                appt.setContactId(new ContactsDAO().displayOneContact(apptContact.getSelectionModel().getSelectedItem()).getContactId());
                return appt;
            }
            else if(b==selectButtonType){
                getCustomer.fire();
            }
            return null;
        });
        Optional<Appointments> result = dialog.showAndWait();

        result.ifPresent(appointments -> {
            if(Appointments.isApptTimesAllowedAgainstList(masterTableData,appointments,editAppointment)){
                // checks new appointment against both edited appointment and list for time conflicts with the same customer ID.
                if(isApptBetweenBusinessHours(appointments)){ // checks against business hours
                        int didItWork = dbappt.updateAppointment(editAppointment.getApptId(), appointments);
                        if (didItWork != 1) {
                            final var info1 = "Something strange happened.. Please try again."; // should never happen
                            Alert alert1 = new Alert(Alert.AlertType.ERROR, info1);
                            alert1.setTitle("");
                            alert1.setHeaderText("");
                            alert1.setResizable(false);
                            alert1.show();
                        } else {
                            final var info1 = "Appointment : " + editAppointment.getApptTitle() + " successfully edited. ";
                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION, info1);
                            alert1.setTitle("");
                            alert1.setHeaderText("");
                            alert1.setResizable(false);
                            alert1.show();
                        }
                }
                else{
                    final var info1 = "You must set appointment between business hours!!";
                    Alert alert1 = new Alert(Alert.AlertType.ERROR, info1);
                    alert1.setTitle("");
                    alert1.setHeaderText("Business hours are Monday-Sunday 8:00am EST - 10:00pm EST");
                    alert1.setResizable(false);
                    alert1.show();
                }
            }
            else{
                final var info1 = "You cannot put the same customer in two different places at the same time!!!";
                Alert alert1 = new Alert(Alert.AlertType.ERROR, info1);
                alert1.setTitle(null);
                alert1.setHeaderText(null);
                alert1.setResizable(false);
                alert1.show();
            }
        });
        setTableData();
    }

    @FXML
    private void handleDeleteAppointmentAction(ActionEvent actionEvent) { // custom dialog to confirm deletion of appointment
        if(apptviewtable.getSelectionModel().getSelectedItem()==null){ // checks to see if anything selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            var title = "ERROR ALERT!!! ";
            var content = "You cannot delete a appointment which you have not selected!!";
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.show();
            return;
        }
        var appt = apptviewtable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            var title = "Consider your options. ";
            var content = "Are you sure you want to delete appointment :" +
                    " "+appt.getApptTitle()+" Type: "+appt.getApptType()+" ?" ;
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(b -> {
                if(b==ButtonType.OK){
                    int didItWork = dbappt.deleteAppointment(appt.getApptId());
                    Alert alert1;
                    String title1;
                    String content1;
                    if(didItWork==1){
                        alert1 = new Alert(Alert.AlertType.INFORMATION);
                        title1 = "CONFIRMATION";
                        content1 = "Appointment: " + appt.getApptTitle()
                                +" Type: "+appt.getApptType()+ " has been successfully deleted. ";
                    }
                    else{
                        alert1 = new Alert(Alert.AlertType.ERROR);
                        title1 = "ERROR ALERT!!!";
                        content1 = "Something happened that shouldn't have happened. "; // should never happen
                    }
                    alert1.setTitle(title1);
                    alert1.setHeaderText(null);
                    alert1.setContentText(content1);
                    alert1.show();
                }
            });
        setTableData();
    }

    @FXML
    private void handleBackToMainScreenAction(ActionEvent actionEvent) { // back to main screen
        Main.showMainView(Main.getLoginUserHolder());
    }

    @FXML
    private void handleFilterTableAction(ActionEvent actionEvent) { // filters appointments just as in main screen
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
                long diff = Math.abs(ChronoUnit.MONTHS.between((YearMonth.of(Integer.parseInt(temp), Month.valueOf(selectedItem).getValue())),appointments.getApptStartTime()));
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
    private void handleResetFilterViewAction(ActionEvent actionEvent) { // resets view to all appointments
        filteredList.setPredicate(p->true);
    }

    private static boolean isApptBetweenBusinessHours(Appointments appointments){ // checks if appointment is between business hours
        LocalDateTime busStart = LocalDateTime.parse("2021-01-01T08:00:00");
        LocalDateTime busEnd = LocalDateTime.parse("2021-01-01T22:00:00");
        ZoneId oldZone = ZoneId.of("America/New_York");
        ZoneId newZone = ZoneId.systemDefault();
        LocalTime adjBusStart = busStart.atZone(oldZone).withZoneSameInstant(newZone).toLocalTime();
        LocalTime adjBusEnd = busEnd.atZone(oldZone).withZoneSameInstant(newZone).toLocalTime();

        return (((appointments.getApptStartTime().toLocalTime().isAfter(adjBusStart)
                || appointments.getApptStartTime().toLocalTime().equals(adjBusStart))
                && ((appointments.getApptEndTime().toLocalTime().isBefore(adjBusEnd)
                || (appointments.getApptEndTime().toLocalTime().equals(adjBusEnd))))));
    }
}
