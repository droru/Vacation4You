package View;

import Model.Flight;
import Model.Notification;
import Model.Vecation;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.Main;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import sample.Aview;

public class FlightDetailScreenController extends Aview {
    public Flight flight;
    public Vecation vecation;

    @FXML
    public Label lbl_destinationCountry;
    public Label lbl_destinationCity;
    public Label lbl_fromDate;
    public Label lbl_toDate;
    public Label lbl_flightID;
    public Label lbl_company;
    public Label lbl_isConnection;
    public Label lbl_isSeparate;
    public Label lbl_price;
    public Label lbl_seller;
    public Button btn_buy;
    public Button btn_switch;


    public Label lbl_hotelName;
    public Label lbl_hotelKind;
    //public Label lbl_hotelprice;
    public Label lbl_hotelType;
    public Label lbl_hotelRank;

    public Label hotelname_lbl;
    public Label kind_lbl;
    public Label rank_lbl;
    public Label vecKind_lbl;
    public HBox hbox_hotel;

    @FXML
    public void initialize(){
        showVec(false);

    }

    private void showVec(boolean var) {
        hotelname_lbl.setVisible(var);
        kind_lbl.setVisible(var);
        rank_lbl.setVisible(var);
        vecKind_lbl.setVisible(var);
        lbl_hotelRank.setVisible(var);
        lbl_hotelType.setVisible(var);
        lbl_hotelKind.setVisible(var);
        lbl_hotelName.setVisible(var);
        hbox_hotel.setVisible(var);

        hotelname_lbl.managedProperty().bind(hotelname_lbl.visibleProperty());
        kind_lbl.managedProperty().bind(kind_lbl.visibleProperty());
        rank_lbl.managedProperty().bind(rank_lbl.visibleProperty());
        vecKind_lbl.managedProperty().bind(vecKind_lbl.visibleProperty());
        lbl_hotelRank.managedProperty().bind(lbl_hotelRank.visibleProperty());
        lbl_hotelType.managedProperty().bind(lbl_hotelType.visibleProperty());
        lbl_hotelKind.managedProperty().bind(lbl_hotelKind.visibleProperty());
        lbl_hotelName.managedProperty().bind(lbl_hotelName.visibleProperty());
        hbox_hotel.managedProperty().bind(hbox_hotel.visibleProperty());
    }

    public void setFlight(Flight flight, boolean isCanBuy){
        if(!isCanBuy){
            btn_buy.setVisible(false);
            btn_buy.setManaged(false);
            btn_switch.setVisible(false);
            btn_switch.setManaged(false);
        }
        setFlight(flight);
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
        lbl_destinationCountry.setText(flight.getDestinationCountry());
        lbl_destinationCity.setText(flight.getDestinationCity());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        lbl_fromDate.setText(df.format(flight.getFromDate()));
        lbl_toDate.setText(df.format(flight.getToDate()));
        lbl_flightID.setText(String.valueOf(flight.getFlightID()));
        lbl_company.setText(flight.getCompany());
        lbl_isConnection.setText(flight.isConnection() ? "כולל קונקשיין" : "ללא קונקשיין");
        lbl_isSeparate.setText(flight.isSeparate() ? "ניתן לרכוש כרטיסים בנפרד" : "אין אפשרות לרכוש כרטיסים בנפרד");
        lbl_price.setText(flight.getPrice() + "ש\"ח");
        lbl_seller.setText(flight.getSeller());

        vecation=getController().search(flight.getFlightID());
        if(vecation!=null) {
            showVec(true);
            lbl_hotelName.setText(vecation.getHotel_name());
            lbl_hotelKind.setText(vecation.getVec_Kind());
            lbl_hotelType.setText(vecation.getVec_Hotel());
            lbl_hotelRank.setText(vecation.getRate());
        }
    }

    public void buyFlight(ActionEvent actionEvent) {
        if (Main.loggedUser != null) {
            if(Main.loggedUser.getUserName().equals(flight.getUserNameSeller())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("User Error");
                alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                alert.setHeaderText("רכישת טיסה");
                alert.setContentText("אינך יכול לרכוש טיסה שהינך מוכר אותה");
                alert.showAndWait();
            }
            else {
                Notification notification = new Notification(Main.loggedUser.getUserName(), flight.getUserNameSeller(), flight.getFlightID(), false, false, false);
                if(getController().search(notification) != null){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Buy Flight");
                    alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    alert.setHeaderText("בקשת רכישה");
                    alert.setContentText("כבר קיימת עבורך בקשה לרכישת הטיסה הזו");
                    alert.showAndWait();
                }
                else {
                    getController().insert(notification);

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Buy Flight");
                    alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    alert.setHeaderText("בקשת רכישה");
                    alert.setContentText("בקשת הרכישה הועברה למוכר הטיסה\nתתיקבל התראה כאשר המוכר יאשר את הרכישה");
                    //ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().clear();
                    alert.getButtonTypes().addAll(ButtonType.OK);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.OK) {
                        alert.close();
                        ((Stage) lbl_seller.getScene().getWindow()).close();
                    }
                }
            }
        }
        else {
            logInValidationBeforeBuyOrSwitchFlight("רק למשתמשים מחוברים יש הרשאות לבצע רכישה של חופשות\nאנא בצע התחברות");
        }
    }

    public void switchFlight(ActionEvent actionEvent) throws IOException {
        //open switch flight screen
        if(Main.loggedUser != null) {
            if(Main.loggedUser.getUserName().equals(flight.getUserNameSeller())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("User Error");
                alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                alert.setHeaderText("החלפת טיסה");
                alert.setContentText("אינך יכול להציע החלפה לטיסה שהינך מוכר אותה");
                alert.showAndWait();
            }
            else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/SwitchFlightScreen.fxml"));
                Parent root = (Parent)fxmlLoader.load();
                SwitchFlightScreenController switchFlightScreenController = fxmlLoader.getController();
                switchFlightScreenController.setData(flight);
                Stage stage = Main.newStage(root, "ChooseFlight", Main.switchFlightWidth, Main.switchFlightHeight, kind_lbl.getScene().getWindow());
            }
        }
        else{
            logInValidationBeforeBuyOrSwitchFlight("רק למשתמשים מחוברים יש הרשאות הציע החלפה של חופשות\nאנא בצע התחברות");
        }
    }

    private void logInValidationBeforeBuyOrSwitchFlight(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("User Error");
        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        alert.setHeaderText("המשתמש אינו מחובר למערכת");
        alert.setContentText(s);
        ButtonType buttonTypeLogIn = new ButtonType("התחבר");
        ButtonType buttonTypeCancel = new ButtonType("ביטול");
        alert.getButtonTypes().setAll(buttonTypeLogIn, buttonTypeCancel);

        alert.showAndWait();

        if(alert.getResult() == buttonTypeLogIn){
            Main.switchScene("../View/LoginForm.fxml", (Stage) lbl_seller.getScene().getWindow(), Main.loginWidth, Main.loginHeight);
        }
    }
}
