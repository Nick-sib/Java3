package client;

import client.network.IncomeMessages;
import client.network.Message;
import client.network.Network;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import server.AuthException;
import total.MessagesText;

import java.io.IOException;
import java.util.List;


public class Controller  implements IncomeMessages {

    private Alert errorAlert;
    private Network network;
    private boolean isAuthorized;


    @FXML
    private ListView<Message> listMessages;

    @FXML
    private TextField txtMessageInput;

    @FXML
    private Button bttnSendMessage;

    @FXML
    private VBox panelLogin;

    @FXML
    private VBox panelMessages;

    @FXML
    private Button bttnLogIn;

    @FXML
    private Button bttnSignUp;

    @FXML
    private Button bttnCancel;

    @FXML
    private TextField txtLogin;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private VBox mainLayout;

    @FXML
    private ListView<String> usersOnLine;

    private ObservableList<String> users = FXCollections.observableArrayList("all");

    private Stage stage;

    private ObservableList<Message> messages = FXCollections.observableArrayList();


    @FXML
    private void initialize() {
        errorAlert = new Alert(Alert.AlertType.ERROR);

        usersOnLine.setItems(users);
        usersOnLine.getSelectionModel().select(0);

        bttnSendMessage.setOnAction(this::sendMessage);
        txtMessageInput.setOnAction(this::sendMessage);
        bttnLogIn.setOnAction(this::okBttnCLicked);
        bttnSignUp.setOnAction(this::okBttnCLicked);
        bttnCancel.setOnAction(this::cancelBttnClicked);

        setAuthorized(false);

        /*listMessages.setCellFactory(messageListView -> new MessageCell());
        listMessages.setItems(messages);*/

    }

    private void sendMessage(ActionEvent actionEvent) {
        //TODO: Посылаем сообщение
    }


    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
        panelLogin.setVisible(!isAuthorized);
        panelLogin.setManaged(!isAuthorized);
        panelMessages.setVisible(isAuthorized);
        panelMessages.setManaged(isAuthorized);

    }

    public boolean authorize(String login, String password, String action) {
        try {
            network.authorize(login, password, action);


        } catch (AuthException e) {
            showError("Authorization ERROR", e.getMessage());
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void okBttnCLicked(ActionEvent event) {

        String login = txtLogin.getText().trim();
        String password = txtPassword.getText().trim();

        if (login.isEmpty() || password.isEmpty()) {
            showError("Data ERROR", "Please enter all datas");
        } else  try {
            txtPassword.clear();

            network = new Network(MessagesText.HOST, MessagesText.PORT);
            network.setIncomeMessages(this);

            setAuthorized(
                    authorize(login, password, ((Button) event.getTarget()).getId()));

            //network.requestOnlineUsersList();

            //((Stage) mainLayout.getScene().getWindow()).setTitle(stage.getTitle() + " " + login);

            txtMessageInput.requestFocus();

        } catch (IOException e) {
            showError("Network ERROR", "No network connection");
        }

    }

    private void cancelBttnClicked(ActionEvent actionEvent) {
        System.exit(1);
    }

    private void showError(String title, String errorMessage) {
        errorAlert.setTitle(title);
        errorAlert.setContentText(errorMessage);
        errorAlert.setHeaderText(null);
        errorAlert.showAndWait();
    }


    @Override
    public void userLogined(String userName) {
        Platform.runLater(() -> users.add(userName));
    }

    @Override
    public void userRemoved(String userName) {
        Platform.runLater(() -> users.remove(userName));
    }

    @Override
    public void handleMessage(Message message) {
        if (message.getUserName().equals(network.getLogin())) {
            message.setUserName("Вы");
        }
        Platform.runLater(() -> {
            messages.add(message);
            listMessages.scrollTo(messages.size()-1);
        });
    }

    @Override
    public void setOnlineUsersList(List<String> users) {
        /*if (!usersList.isEmpty())
            Platform.runLater(() -> users.addAll(usersList));*/
    }
}
