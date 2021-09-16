package appointmentmaker.controller;

import appointmentmaker.Main;
import appointmentmaker.model.Appointments;
import appointmentmaker.model.Countries;
import appointmentmaker.model.Customers;
import appointmentmaker.model.FirstLevelDivision;
import appointmentmaker.modeldao.AppointmentsDAO;
import appointmentmaker.modeldao.CountriesDAO;
import appointmentmaker.modeldao.CustomersDAO;
import appointmentmaker.modeldao.FirstLevelDivisionDAO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller that handles the customer screen
 */
public class CustomerViewController {

    @FXML
    private TableView<Customers> custviewtable;
    @FXML
    private TableColumn<Customers,Integer> custidcol;
    @FXML
    private TableColumn<Customers,String> custnamecol;
    @FXML
    private TableColumn<Customers,String > custaddresscol;
    @FXML
    private TableColumn<Customers,String > custpostalcol;
    @FXML
    private TableColumn<Customers,String> custphonecol;
    @FXML
    private TableColumn<Customers, LocalDateTime> custcreatecol;
    @FXML
    private TableColumn<Customers,LocalDateTime> custlastupdatecol;
    @FXML
    private TableColumn<Customers,String> custlastupdatedbycol;
    @FXML
    private TableColumn<Customers,String> custcountrynamecol;
    @FXML
    private TableColumn<Customers,String> custdivisionnamecol;
    @FXML
    private TextField searchtextfield;
    private final CustomersDAO dbcust = new CustomersDAO();
    private static ObservableList<Customers> masterTableData = null;
    private static FilteredList<Customers> filteredList = null;
    private static SortedList<Customers> sortedList = null;


    @FXML
    private void initialize(){

        setTableColData();
        setTableData();
        searchtextfield.textProperty().addListener(((observableValue, s, t1)
                //search dynamically filters based on either customer id and or customer name.
                -> {
            if(t1.trim().isEmpty()){
                filteredList.setPredicate(customers -> true);
            }
            else{
                filteredList.setPredicate(customers
                        -> ((customers.getCustName().toLowerCase().contains(t1.toLowerCase()))
                                 || ( Integer.valueOf(customers.getCustId()).toString().equals(t1.toLowerCase())))
                );}
        }));
    }

    @FXML
    private void setTableData(){

        masterTableData = FXCollections.observableArrayList();
        masterTableData.addAll(dbcust.displayAllCustomers());
        List<Customers> holder = masterTableData
                .stream()
                .map(dbcust::getCustomerDivNameAndCountryName)
                .collect(Collectors.toList());
        masterTableData = FXCollections.observableArrayList(holder);
        filteredList = new FilteredList<>(masterTableData,p->true);
        sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(custviewtable.comparatorProperty());
        custviewtable.setItems(sortedList);
    }
    @FXML
    private void setTableColData(){

        custidcol.setCellValueFactory(cellData -> cellData.getValue().custIdProperty().asObject());
        custnamecol.setCellValueFactory(cellData -> cellData.getValue().custNameProperty());
        custaddresscol.setCellValueFactory(cellData -> cellData.getValue().custAddressProperty());
        custpostalcol.setCellValueFactory(cellData -> cellData.getValue().custPostalCodeProperty());
        custphonecol.setCellValueFactory(cellData -> cellData.getValue().custPhoneNumberProperty());
        custcreatecol.setCellValueFactory(cellData -> cellData.getValue().custCreateDateProperty());
        custlastupdatecol.setCellValueFactory(cellData-> cellData.getValue().custLastUpdateProperty());
        custlastupdatedbycol.setCellValueFactory(cellData -> cellData.getValue().custLastUpdatedByProperty());
        custcountrynamecol.setCellValueFactory(c-> c.getValue().getCountries().countryNameProperty());
        custdivisionnamecol.setCellValueFactory(c->c.getValue().getFirstLevelDivision().divisionNameProperty());

        custidcol.setStyle("-fx-alignment: CENTER;");

    }
    @FXML
    private void handleNewCustomerAction(ActionEvent actionEvent) { // new customer custom dialog box
        Dialog<Customers> dialog = new Dialog<>();
        var title = "NEW CUSTOMER FORM";
        var header = "Please carefully enter all editable fields.";
        dialog.setTitle(title);
        dialog.setHeaderText(header);

        ButtonType createButtonType = new ButtonType("CREATE", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType,ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();

        col1.setPercentWidth(50.0);
        col2.setPercentWidth(50.0);
        grid.getColumnConstraints().addAll(col1,col2);

        TextField custId = new TextField("FIXED");
        custId.setDisable(true);
        TextField custName = new TextField();
        custName.setPromptText("Enter Full Name Please.");
        TextField custAddress = new TextField();
        custAddress.setPromptText("Enter Street Address Including City Please.  ");
        TextField custPostal = new TextField();
        custPostal.setPromptText("Enter Postal Or Zip Code Please.");
        TextField custPhone = new TextField();
        custPhone.setPromptText("Enter Phone Number Please.");
        ComboBox<String> custCountry = new ComboBox<>();
        ComboBox<String> custDivision = new ComboBox<>();

        grid.add(new Label("Customer ID: "),0,0);
        grid.add(custId,1,0);
        grid.add(new Label("Customer Name: "),0,1);
        grid.add(custName,1,1);
        grid.add(new Label("Customer Address: "),0,2);
        grid.add(custAddress,1,2);
        grid.add(new Label("Customer Postal: "),0,3);
        grid.add(custPostal,1,3);
        grid.add(new Label("Customer Phone: "),0,4);
        grid.add(custPhone,1,4);
        grid.add(new Label("Customer Country.\n Please Choose First: "),0,5);
        grid.add(custCountry,1,5);
        grid.add(new Label("Customer Division: "),0,6);
        grid.add(custDivision,1,6);

        dialog.getDialogPane().lookupButton(createButtonType) // create button disabled until all fields are populated
                .disableProperty().bind(Bindings.createBooleanBinding(()->
            custName.getText().trim().isEmpty()
                    || custAddress.getText().trim().isEmpty()
                    || custPostal.getText().trim().isEmpty()
                    || custPhone.getText().trim().isEmpty()
                    ,custName.textProperty()
                    ,custAddress.textProperty()
                    ,custPostal.textProperty()
                    ,custPhone.textProperty()
        ));

        ObservableList<Countries> list = FXCollections.observableArrayList(new CountriesDAO().displayAllCountries()); // list of all countries
        List<String> nameList = list
                .stream()
                .map(Countries::getCountryName)
                .collect(Collectors.toList());
        custCountry.getItems().addAll(nameList); // populate into country combo box

        custCountry.valueProperty().addListener(((observableValue, s, t1) -> {
            // add listener to auto-populate the division combo box based on the country selection

            List<String> list1 = FXCollections.observableArrayList(new FirstLevelDivisionDAO()
                    .displayAllFirstLevelDivisionAssosicatedWithCountry(new CountriesDAO()
                            .displayOneCountries(t1).getCountryId()))
                    .stream()
                    .map(FirstLevelDivision::getDivisionName)
                    .collect(Collectors.toList());
            if(custDivision.getItems().size()>0)
                custDivision.getItems().clear();
            custDivision.getItems().addAll(list1);
            custDivision.getSelectionModel().selectFirst();
        }));

        custCountry.getSelectionModel().selectFirst();
        dialog.getDialogPane().setContent(grid);

        Platform.runLater(custName::requestFocus);

        dialog.setResultConverter(b -> { // creates customer object when create is selected
            if(b==createButtonType){
                var cx = new Customers();
                cx.setCustName(custName.getText());
                cx.setCustAddress(custAddress.getText());
                cx.setCustPostalCode(custPostal.getText());
                cx.setCustPhoneNumber(custPhone.getText());
                cx.setCustCreateDate(LocalDateTime.now());
                cx.setCustCreatedBy(Main.getLoginUserHolder());
                cx.setCustLastUpdatedBy(Main.getLoginUserHolder());
                cx.setCustLastUpdate(LocalDateTime.now());
                int divId = new FirstLevelDivisionDAO()
                        .displayOneFirstLevelDivision(custDivision.getSelectionModel()
                                .getSelectedItem()).getDivisionId();
                cx.setCustDivId(divId);
                cx.getCountries().setCountryName(custCountry.getSelectionModel().getSelectedItem());
                cx.getFirstLevelDivision().setDivisionName(custDivision.getSelectionModel().getSelectedItem());
                return cx;
            }
            return null;
        });
        Optional<Customers> result = dialog.showAndWait();

        result.ifPresent(customers -> {
            if(Customers.isListContainTestItem(sortedList,result.get())){ // checks to see if new customer is duplicate
                final var info1 = "You cannot create a duplicate customer. ";
                Alert alert1 = new Alert(Alert.AlertType.ERROR, info1);
                alert1.setTitle("");
                alert1.setHeaderText("");
                alert1.setResizable(false);
                alert1.show();
            }
            else{
                int didItWork = dbcust.createCustomer(customers);
                if (didItWork != 1) {
                    final var info1 = "Something strange happened.. Please try again."; // should never happen
                    Alert alert1 = new Alert(Alert.AlertType.ERROR, info1);
                    alert1.setTitle("");
                    alert1.setHeaderText("");
                    alert1.setResizable(false);
                    alert1.show();
                } else {
                    final var info1 = "Customer : " + customers.getCustName() + " successfully created. ";
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION, info1);
                    alert1.setTitle("");
                    alert1.setHeaderText("");
                    alert1.setResizable(false);
                    alert1.show();
                }
            }
        });
        setTableData();
    }

    @FXML
    private void handleEditCustomerAction(ActionEvent actionEvent){
        // custom dialog box to edit customer. Similar to new customer
        if(custviewtable.getSelectionModel().getSelectedItem()==null){
            // checks to see if customer has been selected first
            Alert alert = new Alert(Alert.AlertType.ERROR);
            var title = "ERROR ALERT!!!";
            var header = "You cannot edit a customer that isn't selected!!";
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(header);
            alert.setResizable(false);
            alert.show();
            return;
        }
        var editCustomer = custviewtable.getSelectionModel().getSelectedItem();
        Dialog<Customers> dialog = new Dialog<>();
        var title = "EDIT CUSTOMER FORM";
        var header = "Please carefully change all editable fields.";
        dialog.setTitle(title);
        dialog.setHeaderText(header);

        ButtonType editButtonType = new ButtonType("EDIT", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(editButtonType,ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();

        col1.setPercentWidth(50.0);
        col2.setPercentWidth(50.0);
        grid.getColumnConstraints().addAll(col1,col2);

        TextField custId = new TextField("FIXED");
        custId.setDisable(true);
        TextField custName = new TextField(editCustomer.getCustName());
        TextField custAddress = new TextField(editCustomer.getCustAddress());
        TextField custPostal = new TextField(editCustomer.getCustomerPostalCode());
        TextField custPhone = new TextField(editCustomer.getCustPhoneNumber());
        ComboBox<String> custCountry = new ComboBox<>();
        ComboBox<String> custDivision = new ComboBox<>();

        grid.add(new Label("Customer ID: "),0,0);
        grid.add(custId,1,0);
        grid.add(new Label("Customer Name: "),0,1);
        grid.add(custName,1,1);
        grid.add(new Label("Customer Address: "),0,2);
        grid.add(custAddress,1,2);
        grid.add(new Label("Customer Postal: "),0,3);
        grid.add(custPostal,1,3);
        grid.add(new Label("Customer Phone: "),0,4);
        grid.add(custPhone,1,4);
        grid.add(new Label("Customer Country: "),0,5);
        grid.add(custCountry,1,5);
        grid.add(new Label("Customer Division: "),0,6);
        grid.add(custDivision,1,6);

        dialog.getDialogPane().lookupButton(editButtonType)
                .disableProperty().bind(Bindings.createBooleanBinding(()->
                                custName.getText().trim().isEmpty()
                                        || custAddress.getText().trim().isEmpty()
                                        || custPostal.getText().trim().isEmpty()
                                        || custPhone.getText().trim().isEmpty()
                        ,custName.textProperty()
                        ,custAddress.textProperty()
                        ,custPostal.textProperty()
                        ,custPhone.textProperty()
                ));

        ObservableList<Countries> list = FXCollections.observableArrayList(new CountriesDAO().displayAllCountries());
        List<String> nameList = list.stream().map(Countries::getCountryName).collect(Collectors.toList());
        custCountry.getItems().addAll(nameList);

        custCountry.valueProperty().addListener(((observableValue, s, t1) -> {

            List<String> list1 = FXCollections.observableArrayList(new FirstLevelDivisionDAO()
                            .displayAllFirstLevelDivisionAssosicatedWithCountry(new CountriesDAO()
                                    .displayOneCountries(t1).getCountryId()))
                    .stream()
                    .map(FirstLevelDivision::getDivisionName)
                    .collect(Collectors.toList());
            if(custDivision.getItems().size()>0)
                custDivision.getItems().clear();
            custDivision.getItems().addAll(list1);
            custDivision.getSelectionModel().selectFirst();
        }));

        custCountry.getSelectionModel().select(editCustomer.getCountries().getCountryName());
        custDivision.getSelectionModel().select(editCustomer.getFirstLevelDivision().getDivisionName());

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(custCountry::requestFocus);

        dialog.setResultConverter(b -> {
            if(b==editButtonType){
                var cx = new Customers();
                cx.setCustName(custName.getText());
                cx.setCustAddress(custAddress.getText());
                cx.setCustPostalCode(custPostal.getText());
                cx.setCustPhoneNumber(custPhone.getText());
                cx.setCustCreateDate(editCustomer.getCustCreateDate());
                cx.setCustCreatedBy(editCustomer.getCustCreatedBy());
                cx.setCustLastUpdatedBy(Main.getLoginUserHolder());
                cx.setCustLastUpdate(LocalDateTime.now());
                int divId = new FirstLevelDivisionDAO()
                        .displayOneFirstLevelDivision(custDivision.getSelectionModel()
                                .getSelectedItem()).getDivisionId();
                cx.setCustDivId(divId);
                cx.getCountries().setCountryName(custCountry.getSelectionModel().getSelectedItem());
                cx.getFirstLevelDivision().setDivisionName(custDivision.getSelectionModel().getSelectedItem());
                return cx;
            }
            return null;
        });
        Optional<Customers> result = dialog.showAndWait();

        result.ifPresent(customers -> {
            if(Customers.isListContainTestItem(sortedList,result.get())){ // checks for duplicate customer
                final var info1 = "You cannot create a duplicate customer. ";
                Alert alert1 = new Alert(Alert.AlertType.ERROR, info1);
                alert1.setTitle("");
                alert1.setHeaderText("");
                alert1.setResizable(false);
                alert1.show();
            }
            else{
                int didItWork = dbcust.updateCustomer(editCustomer.getCustId(), result.get());
                if (didItWork != 1) {
                    final var info1 = "Something strange happened.. Please try again."; // should never happen
                    Alert alert1 = new Alert(Alert.AlertType.ERROR, info1);
                    alert1.setTitle("");
                    alert1.setHeaderText("");
                    alert1.setResizable(false);
                    alert1.show();
                } else {
                    final var info1 = "Customer : " + editCustomer.getCustName() + " successfully edited. ";
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION, info1);
                    alert1.setTitle("");
                    alert1.setHeaderText("");
                    alert1.setResizable(false);
                    alert1.show();
                }
            }
        });
        setTableData();
}

    @FXML
    private void handleDeleteCustomerAction(ActionEvent actionEvent) { // custom dialog box to confirm customer deletion
        if(custviewtable.getSelectionModel().getSelectedItem()==null){//checks if customer is selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            var title = "ERROR ALERT!!! ";
            var content = "You cannot delete a customer which you have not selected!!";
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.show();
            return;
        }
        var customer = custviewtable.getSelectionModel().getSelectedItem();
        ObservableList<Appointments> exists
                = FXCollections.observableArrayList(new AppointmentsDAO()
                .displayAllAppointmentsUnderCustomerId(customer.getCustId()));
        // checks if there are appointments assigned to the selected customer
        if(exists.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            var title = "Consider your options. ";
            var content = "Are you sure you want to delete customer : "+customer.getCustName()+" ?" ;
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(b -> {
                if(b==ButtonType.OK){
                    int didItWork = dbcust.deleteCustomer(customer.getCustId());
                    Alert alert1;
                    String title1;
                    String content1;
                    if(didItWork==1){
                        alert1 = new Alert(Alert.AlertType.INFORMATION);
                        title1 = "CONFIRMATION";
                        content1 = "Customer: " + customer.getCustName() + " has been successfully deleted. ";
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
                    setTableData();
                }
            });
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            var title = "You can't do this!!";
            var content = "You can't delete a customer who still has appointments scheduled. " ;
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.show();
        }
        setTableData();
    }

    @FXML
    private void handleMainScreenAction(ActionEvent actionEvent) {
        Main.showMainView(Main.getLoginUserHolder());
    } //goes back to main screen

    @FXML
    private void handleResetViewAction(ActionEvent actionEvent) {
        filteredList.setPredicate(customers -> true);
    }
    //resets filter to display all customers
}
