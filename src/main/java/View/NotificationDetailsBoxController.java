package View;

import Model.Flight;
import Model.SwitchNotification;
import Model.User;
import com.sun.javafx.scene.control.behavior.PaginationBehavior;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Main;
import Model.Notification;
import javafx.fxml.FXML;
import sample.Aview;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;

public class NotificationDetailsBoxController extends Aview {
    Notification notification;
    Pane pane;
    MainScreenController main;

    @FXML
    public Button btn_accept;
    public Button btn_deny;
    public Button btn_buy;
    public Button btn_close;
    public Label lbl_msg;
    public Label lbl_msgcon;
    public Label lbl_user;
    public Hyperlink hyper_watchFlight;
    public HBox hbox;

    @FXML
    public void initialize(){
        btn_accept.managedProperty().bind(btn_accept.visibleProperty());
        btn_close.managedProperty().bind(btn_close.visibleProperty());
        btn_buy.managedProperty().bind(btn_buy.visibleProperty());
        btn_deny.managedProperty().bind(btn_deny.visibleProperty());
        hbox.managedProperty().bind(hbox.visibleProperty());
    }

    public void setData(Notification notification, Pane pane, MainScreenController main){
        this.main = main;
        this.notification = notification;
        this.pane = pane;
        if(notification instanceof SwitchNotification){
            if (notification.getIsResponsed() == false && notification.getToUser().equals(Main.loggedUser.getUserName())) {
                lbl_user.setText(notification.getFromUser());
                lbl_msg.setText("ביקש להחליף בין טיסה מס' " + notification.getFlightID());
                hbox.setVisible(true);
                lbl_msgcon.setText("לטיסה מס'");
                hyper_watchFlight.setText(((SwitchNotification) notification).getSecondFlightID() + "");

                Tooltip tooltip = new Tooltip("לצפייה");
                hackTooltipStartTiming(tooltip);
                hyper_watchFlight.setTooltip(tooltip);

                btn_accept.setVisible(true);
                btn_deny.setVisible(true);
            } else if (notification.getIsResponsed() == true && notification.getFromUser().equals(Main.loggedUser.getUserName())) {
                String str1 = "החלפת הטיסות ";
                String str2 = notification.getFlightID() + " עם" + ((SwitchNotification) notification).getSecondFlightID();
                if (notification.getIsAccept()) {
                    lbl_user.setText(notification.getToUser());
                    lbl_msg.setText("אישר " + str1);
                    hbox.setVisible(true);
                    lbl_msgcon.setText(str2);
                    btn_buy.setVisible(true);
                    btn_buy.setText("החלף");
                    btn_close.setVisible(true);
                } else {
                    lbl_user.setText(notification.getToUser());
                    lbl_msg.setText("לא אישר " + str1);
                    hbox.setVisible(true);
                    lbl_msgcon.setText(str2);
                    btn_close.setVisible(true);
                    btn_close.setText("קראתי");
                }
            }
        }
        else {
            if (notification.getIsResponsed() == false && notification.getToUser().equals(Main.loggedUser.getUserName())) {
                lbl_user.setText(notification.getFromUser());
                lbl_msg.setText(" ביקש לרכוש את טיסה מס' " + notification.getFlightID());
                btn_accept.setVisible(true);
                btn_deny.setVisible(true);
            } else if (notification.getIsResponsed() == true && notification.getFromUser().equals(Main.loggedUser.getUserName())) {
                String str = "רכישת הטיסה " + notification.getFlightID();
                if (notification.getIsAccept()) {
                    lbl_user.setText(notification.getToUser());
                    lbl_msg.setText("אישר " + str);
                    btn_buy.setVisible(true);
                    btn_close.setVisible(true);
                } else {
                    lbl_user.setText(notification.getToUser());
                    lbl_msg.setText("לא אישר " + str);
                    btn_close.setVisible(true);
                    btn_close.setText("קראתי");
                }
            } else if (notification.isPayProcess() == true && notification.getToUser().equals(Main.loggedUser.getUserName())) {
                lbl_user.setText(notification.getFromUser());
                lbl_msg.setText(" ביקש לרכוש את טיסה מס' " + notification.getFlightID());
                lbl_msg.setText("מבצע תשלום על טיסה מס' " + notification.getFlightID());
                btn_close.setVisible(true);
                btn_close.setText("התשלום בוצע");
            }
        }
    }

    public void accept() throws SQLException {
        notification.setIsResponsed(true);
        notification.setIsAccept(true);
        if(notification instanceof SwitchNotification)
            getController().update((SwitchNotification)notification);
        else
            getController().update(notification);
        pane.getChildren().remove(lbl_user.getParent());
    }

    public void deny() throws SQLException {
        notification.setIsResponsed(true);
        notification.setIsAccept(false);
        if(notification instanceof SwitchNotification)
            getController().update((SwitchNotification)notification);
        else
            getController().update(notification);
        pane.getChildren().remove(lbl_user.getParent());
    }

    public void buy() throws IOException {
        if(notification instanceof SwitchNotification){
            getController().updateActiveField(notification.getFlightID());
            getController().updateActiveField(((SwitchNotification)notification).getSecondFlightID());
            getController().delete((SwitchNotification)notification);
            pane.getChildren().remove(lbl_user.getParent());
            main.removeFlight(notification.getFlightID());
            main.removeFlight(((SwitchNotification) notification).getSecondFlightID());
        }
        else {
            //show payment screen
            /*openPaymentChoose();*/

            /*first change after requirements update*/
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            alert.setTitle("רכישת טיסה");
            alert.setHeaderText("תשלום במזומן");
            alert.setContentText("עליך להעביר את הכסף ל" + notification.getToUser());
            ButtonType buttonTypeWatchProfile = new ButtonType("לצפייה בפרטי המוכר");
            ButtonType buttonTypeOK = new ButtonType("אישור");
            ButtonType buttonTypeCancel = new ButtonType("ביטול");
            alert.getButtonTypes().setAll(buttonTypeWatchProfile, buttonTypeOK, buttonTypeCancel);
            alert.showAndWait();

            if (alert.getResult() == buttonTypeWatchProfile) {
                User userSerach = getController().search(notification.getToUser());
                if (userSerach != null) {
                    Main.setUser(userSerach);
                    Main.isProfile = false;
                    Parent root = FXMLLoader.load(getClass().getResource("../View/UserDetailsScreen.fxml"));
                    Main.newStage(root, "User profile", Main.registerWidth, Main.registerHeight, btn_close.getScene().getWindow());

                /*stage.setOnHidden(ex->{
                    //stage.close();
                    alert.showAndWait();
                    if(alert.getResult() == buttonTypeWatchProfile){
                        stage.show();
                    }
                    if(alert.getResult() == buttonTypeOK){
                        try {
                            notification.setPayProcess(true);
                            getController().update(notification);
                            pane.getChildren().remove(lbl_user.getParent());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });*/
                }
            }
            if (alert.getResult() == buttonTypeOK) {
                try {
                    notification.setPayProcess(true);
                    getController().update(notification);
                    pane.getChildren().remove(lbl_user.getParent());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            Main.not = this.notification;
        }
    }

    private void openPaymentChoose() throws IOException {
        Stage stage=new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/PaymentChoose.fxml"));
        Parent root = fxmlLoader.load();
        stage.setTitle("Payment");
        Scene scene=new Scene(root,616 ,430);
        scene.getStylesheets().add(getClass().getResource("../View/Style.css").toExternalForm());
        stage.setScene(scene);
        //stage.setResizable(false);
        stage.initOwner(btn_buy.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
        stage.setOnHiding(e->{
            boolean isPay = ((PaymentChooseController)fxmlLoader.getController()).isPay();
            if(isPay) {
                pane.getChildren().remove(lbl_user.getParent());
                main.removeFlight(notification.getFlightID());
                getController().updateActiveField(notification.getFlightID());
                getController().delete(notification);
            }
        });
    }

    public void close() {
        if(notification.isPayProcess()){
            pane.getChildren().remove(lbl_user.getParent());
            main.removeFlight(notification.getFlightID());
            getController().updateActiveField(notification.getFlightID());
        }
        //delete notification
        if(notification instanceof SwitchNotification)
            getController().delete((SwitchNotification)notification);
        else
            getController().delete(notification);
        //delete notification from gui
        pane.getChildren().remove(lbl_user.getParent());
    }

    public void watchFlight(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/FlightDetailScreen.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        FlightDetailScreenController controller = fxmlLoader.getController();
        Flight flight = getController().serach(((SwitchNotification)notification).getSecondFlightID());
        controller.setFlight(flight, false);

        Main.newStage(root, "Flight details", 825, 500, lbl_user.getScene().getWindow());
    }

    private static void hackTooltipStartTiming(Tooltip tooltip) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(50)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
