package appointmentmaker.controller;

import appointmentmaker.Main;
import appointmentmaker.model.Appointments;
import appointmentmaker.model.Users;
import appointmentmaker.modeldao.AppointmentsDAO;
import appointmentmaker.modeldao.UsersDAO;
import appointmentmaker.utility.PassWrdHash;
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
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller that handles the user maintenance screen
 */
public class UserViewController {

    @FXML
    private TableView<Users> userviewtable;
    @FXML
    private TableColumn<Users,Integer> useridcol;
    @FXML
    private TableColumn<Users,String> usernamecol;
    @FXML
    private TableColumn<Users, LocalDateTime> lastupdatecol;
    @FXML
    private TableColumn<Users,String> lastupdatedbycol;
    private final UsersDAO dbUsers = new UsersDAO();
    private static ObservableList<Users> masterTableData = null;
    private static FilteredList<Users> filteredList = null;
    private static SortedList<Users> sortedList = null;


    @FXML
    private void initialize(){
        setColData();
        setTableDate();
    }

    @FXML
    private void setColData() {
        useridcol.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asObject());
        usernamecol.setCellValueFactory(cellData -> cellData.getValue().userNameProperty());
        lastupdatecol.setCellValueFactory(cellData -> cellData.getValue().lastUpdatedProperty());
        lastupdatedbycol.setCellValueFactory(cellData -> cellData.getValue().lastUpdatedByProperty());

        useridcol.setStyle("-fx-alignment: CENTER;");
        usernamecol.setStyle("-fx-alignment: CENTER;");
        lastupdatecol.setStyle("-fx-alignment: CENTER;");
        lastupdatedbycol.setStyle("-fx-alignment: CENTER;");
    }

    @FXML
    private void setTableDate() {
        masterTableData = FXCollections.observableArrayList();
        masterTableData.addAll(dbUsers.displayAllUsers());
        filteredList = new FilteredList<>(masterTableData, p -> true);
        sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(userviewtable.comparatorProperty());
        userviewtable.setItems(sortedList);
    }

    @FXML
    private void handleGoToMainScreenAction(ActionEvent actionEvent) { // back to main screen
        Main.showMainView(Main.getLoginUserHolder());
    }

    @FXML
    private void handleAddNewUserAction(ActionEvent actionEvent) { // custom dialog to create new user
        Dialog<Pair<String,String>> dialog = new Dialog<>();
        dialog.setGraphic(new Circle(15, Color.GREEN));
        dialog.setTitle("New User input.");
        dialog.setHeaderText("Please enter new password twice to confirm. \n" +
                "Password must contain at least four characters\n and is case sensitive.");

        ButtonType createButtonType = new ButtonType("CREATE", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType,ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10,10));

        Label userNameLabel = new Label("Enter UserName: ");
        TextField userText = new TextField();

        Label passwordLabel = new Label("Enter Password: ");
        PasswordField pwd1 = new PasswordField();
        pwd1.setPromptText("Password");

        Label confirmLabel = new Label("Confirm Password: ");
        PasswordField pwd2 = new PasswordField();
        pwd2.setPromptText("Password Confirm");

        grid.add(passwordLabel,0,1);
        grid.add(pwd1,1,1);
        grid.add(confirmLabel,0,2);
        grid.add(pwd2,1,2);
        grid.add(userNameLabel,0,0);
        grid.add(userText,1,0);

        dialog.getDialogPane().lookupButton(createButtonType).disableProperty().bind(Bindings.createBooleanBinding(()->
                        // create button is disabled until all fields are populated
                userText.getText().trim().isEmpty()
                        || pwd1.getText().trim().isEmpty()
                        || pwd2.getText().trim().isEmpty()
                ,userText.textProperty()
                ,pwd1.textProperty()
                ,pwd2.textProperty()));


        dialog.getDialogPane().setContent(grid);

        Platform.runLater(userText::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                if (!((pwd1.getText().equals(pwd2.getText()))
                        && (pwd1.getText().length() >= 4))) {
                    final var info = "Your passwords must match and be at least four characters!!!";
                    Alert alert = new Alert(Alert.AlertType.ERROR, info);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("");
                    alert.setResizable(false);
                    alert.show();
                } else {
                    return new Pair<>(userText.getText(),pwd1.getText());
                }
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(userPass -> { // checks new user object for duplicte username
                    var newUser = new Users();
                    newUser.setUserId(0);
                    newUser.setUserName(userPass.getKey());
                    String[] strings;
                    strings = PassWrdHash.doPrivateStuffWithPlainTextPassword((userPass.getValue()).toCharArray());
                    newUser.setPassHash(strings[0]);
                    newUser.setSaltValue(strings[1], this);
                    newUser.setLastUpdated(LocalDateTime.now());
                    newUser.setLastUpdatedBy("admin");
                    List<String> exists = new ArrayList<>();
                    for (Users masterTableDatum : masterTableData) {
                        exists.add(masterTableDatum.getUserName());
                    }
                    Optional<String> itExists = exists
                            .stream()
                            .filter(s -> s.equalsIgnoreCase(newUser.getUserName()))
                            .findAny();
                    if (itExists.isPresent()) {
                        final var info1 = "You cannot have duplicate UserNames. Please try another UserName. ";
                        Alert alert1 = new Alert(Alert.AlertType.ERROR, info1);
                        alert1.setTitle("");
                        alert1.setHeaderText("");
                        alert1.setResizable(false);
                        alert1.show();
                    } else {
                        int didItWork = dbUsers.createUser(newUser);
                        if (didItWork != 1) {
                            final var info1 = "Something strange happened.. Please try again.";// should never happen
                            Alert alert1 = new Alert(Alert.AlertType.ERROR, info1);
                            alert1.setTitle("");
                            alert1.setHeaderText("");
                            alert1.setResizable(false);
                            alert1.show();
                        } else {
                            final var info1 = "User : " + newUser.getUserName() + " successfully created. ";
                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION, info1);
                            alert1.setTitle("");
                            alert1.setHeaderText("");
                            alert1.setResizable(false);
                            alert1.show();
                        }
                    }
                });
        masterTableData.clear();
        masterTableData.addAll(dbUsers.displayAllUsers()); //repopulates table
    }

    @FXML
    private void handleChangeUserPasswordAction(ActionEvent actionEvent) { // change user password
        if (userviewtable.getSelectionModel().getSelectedItem() == null) { // checks if user is selected
            final var info = "You cannot change a password for a user that is not selected!!";
            final var information = "ERROR ALERT!!";
            Alert alert = new Alert(Alert.AlertType.ERROR, info);
            alert.setTitle(information);
            alert.setHeaderText("");
            alert.setResizable(false);
            alert.show();
            return;
        }
        final var userName = userviewtable.getSelectionModel().getSelectedItem().getUserName();
            if(userName.equals("test")){ // cannot change password for test user
                final var info = "You cannot change the password for user: test !!";
                final var information = "ERROR ALERT!!";
                Alert alert = new Alert(Alert.AlertType.ERROR, info);
                alert.setTitle(information);
                alert.setHeaderText("");
                alert.setResizable(false);
                alert.show();
                return;
            }
            else {
                final var user = userviewtable.getSelectionModel().getSelectedItem();
                Dialog<String> dialog = new Dialog<>();
                dialog.setGraphic(new Circle(15, Color.YELLOW));
                dialog.setTitle("Change password for: "+ user.getUserName());
                dialog.setHeaderText("Please enter new password twice to confirm. \n" +
                        "Password must contain at least four characters\n and is case sensitive.");

                ButtonType changeButtonType = new ButtonType("CHANGE", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(changeButtonType,ButtonType.CANCEL);

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10,10));

                Label passwordLabel = new Label("Enter Password: ");
                PasswordField pwd1 = new PasswordField();

                Label confirmLabel = new Label("Confirm Password: ");
                PasswordField pwd2 = new PasswordField();
                pwd2.setPromptText("Password Confirm");

                grid.add(passwordLabel,0,0);
                grid.add(pwd1,1,0);
                grid.add(confirmLabel,0,1);
                grid.add(pwd2,1,1);

                dialog.getDialogPane().lookupButton(changeButtonType).disableProperty()
                        // button disabled until fields are populated
                        .bind(Bindings.createBooleanBinding(()->
                        pwd1.getText().trim().isEmpty()
                            || pwd2.getText().trim().isEmpty()
                        ,pwd1.textProperty()
                        ,pwd2.textProperty()));

                dialog.getDialogPane().setContent(grid);

                Platform.runLater(pwd1::requestFocus);

                dialog.setResultConverter(dialogButton -> {
                            if (dialogButton == changeButtonType) {
                                if (!((pwd1.getText().equals(pwd2.getText()))
                                        && (pwd1.getText().length() >= 4))) {
                                    final var info = "Your passwords must match and be at least four characters!!!";
                                    Alert alert = new Alert(Alert.AlertType.ERROR, info);
                                    alert.setTitle("ERROR");
                                    alert.setHeaderText("");
                                    alert.setResizable(false);
                                    alert.show();
                                } else {
                                    return pwd1.getText();
                                }
                            }
                            return null;
                        });

                        Optional<String> result = dialog.showAndWait();

                        result.ifPresent(newPassword ->{
                            var newUser = new Users(); // updates password in database
                            newUser.setUserId(user.getUserId());
                            newUser.setUserName(user.getUserName());
                            String[] strings;
                            strings = PassWrdHash.doPrivateStuffWithPlainTextPassword(newPassword.toCharArray());
                            newUser.setPassHash(strings[0]);
                            newUser.setSaltValue(strings[1],this);
                            newUser.setLastUpdated(LocalDateTime.now());
                            newUser.setLastUpdatedBy("admin");
                            int didItWork = dbUsers.updateUser(user.getUserId(),newUser);
                            if(didItWork!=1){
                                final var info1 = "Something strange happened.. Please try again."; // should never happen
                                Alert alert1 = new Alert(Alert.AlertType.ERROR,info1);
                                alert1.setTitle("");
                                alert1.setHeaderText("");
                                alert1.setResizable(false);
                                alert1.show();
                            }
                            else{
                                final var info1 = "Password Successfully Changed. ";
                                Alert alert1 = new Alert(Alert.AlertType.INFORMATION,info1);
                                alert1.setTitle("");
                                alert1.setHeaderText("");
                                alert1.setResizable(false);
                                alert1.show();
                            }
                        });
                    }
        masterTableData.clear();
        masterTableData.addAll(dbUsers.displayAllUsers()); // repopulates table
    }

    @FXML
    private void handleDeleteUserAction(ActionEvent actionEvent) { // deletes user with prompt
        if(userviewtable.getSelectionModel().getSelectedItem()==null){ // checks if user selected
            final var info = "You cannot delete a user that is not selected!!";
            final var information="ERROR ALERT!!";
            Alert alert = new Alert(Alert.AlertType.ERROR,info);
            alert.setTitle(information);
            alert.setHeaderText("");
            alert.setResizable(false);
            alert.show();
            return;
        }
        final var userName = userviewtable.getSelectionModel().getSelectedItem().getUserName();
        final var userId = userviewtable.getSelectionModel().getSelectedItem().getUserId();
        ObservableList<Appointments> list = new AppointmentsDAO().displayAllAppointmentsUnderUserId(userId);
        if(userName.equals("test") // cannot delete user "test" or "admin"
                || userName.equals("admin")){
            final var info = "You cannot delete test user or Yourself!!";
            final var information="ERROR ALERT!!";
            Alert alert = new Alert(Alert.AlertType.ERROR,info);
            alert.setTitle(information);
            alert.setHeaderText("");
            alert.setResizable(false);
            alert.show();
            return;
        }
        else if(!(list.isEmpty())){ // cannot delete a user that has appointments assigned
                final var info = "You cannot delete a user that still has appointments associated!!";
                final var information="ERROR ALERT!!";
                Alert alert = new Alert(Alert.AlertType.ERROR,info);
                alert.setTitle(information);
                alert.setHeaderText("");
                alert.setResizable(false);
                alert.show();
                return;
            }
            else{
                final var info = "Are you sure you want to delete username : "+userName+"?";
                final var information="Please choose carefully";
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,info);
                alert.setTitle(information);
                alert.setHeaderText("");
                alert.setResizable(false);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent()) {
                    if(result.get()== ButtonType.OK){
                        int didItWork = dbUsers.deleteUser(userId);
                        if(didItWork!=1){
                            final var info1 = "Something strange happened.. Please try again.";//should never happen
                            Alert alert1 = new Alert(Alert.AlertType.ERROR,info1);
                            alert1.setTitle("");
                            alert1.setHeaderText("");
                            alert1.setResizable(false);
                            alert1.show();
                        }
                        else{
                            final var info1 = "User Successfully Deleted. ";
                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION,info1);
                            alert1.setTitle("Click OK to proceed. ");
                            alert1.setHeaderText("");
                            alert1.setResizable(false);
                            alert1.show();
                        }
                    }
                }
            }
        masterTableData.clear();
        masterTableData.addAll(dbUsers.displayAllUsers()); // repopulates tables
        }
    }