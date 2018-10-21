package sample;

import Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.*;
import java.sql.SQLException;

import static Model.Query.delete;
import static Model.Query.update;
import static sample.RegisterController.*;

public class UserDetailsScreenController {
    @FXML
    public ImageView img_profile;
    public TextField lbl_firstName;
    public TextField lbl_lastName;
    public TextField lbl_birthDate;
    public ChoiceBox lbl_city;
    public TextField lbl_email;
    public Button updateButton;
    public Button returnMain;
    public Hyperlink makeEdit;
    public Hyperlink changePass;
    public Hyperlink deleteuser;

    public Label title;
    public ChoiceBox _city;
    public Label erorfirstname;
    public Label erorlastname;
    public Label erormail;


    public Label lbl_firstName1;
    public Label lbl_lastName1;
    public Label lbl_birthDate1;
    public Label lbl_city1;
    public Label lbl_email1;

    public void initialize(){
        if(Main.isProfile) {
            title.setText("אזור אישי");
            lbl_firstName1.setText(Main.loggedUser.getFirstName());
            lbl_lastName1.setText(Main.loggedUser.getLastName());
            lbl_birthDate1.setText(Main.loggedUser.getBirthDate());
            lbl_city1.setText(Main.loggedUser.getCity());
            lbl_email1.setText(Main.loggedUser.getEmail());
        }
        else{
            title.setText("פרופיל משתמש");
            lbl_firstName1.setText(Main.user.getFirstName());
            lbl_lastName1.setText(Main.user.getLastName());
            lbl_birthDate1.setText(Main.user.getBirthDate());
            lbl_city1.setText(Main.user.getCity());
            lbl_email1.setText(Main.user.getEmail());
        }
        updateButton.setVisible(false);

        lbl_firstName.setVisible(false);
        lbl_lastName.setVisible(false);
        lbl_birthDate.setVisible(false);
        lbl_city.setVisible(false);
        lbl_email.setVisible(false);

        // disableEdit();
    }

    public void disableEdit(){
        lbl_firstName.setDisable(true);
        lbl_birthDate.setDisable(true);
        lbl_email.setDisable(true);
        lbl_city.setDisable(true);
        lbl_lastName.setDisable(true);
        updateButton.setVisible(false);
        returnMain.setVisible(false);
        makeEdit.setVisible(false);
        changePass.setVisible(false);
    }

    public void enableEdit()
    {


        /*
        lbl_firstName.setDisable(false);
        lbl_email.setDisable(false);
        lbl_city.setDisable(false);
        lbl_lastName.setDisable(false);
        changePass.setVisible(true);
        updateButton.setVisible(true);
        lbl_city.setVisible(false);
        _city.setVisible(true);
         */
        lbl_firstName1.setVisible(false);
        lbl_lastName1.setVisible(false);
        lbl_birthDate1.setVisible(false);
        lbl_city1.setVisible(false);
        lbl_email1.setVisible(false);


        lbl_firstName.setVisible(true);
        lbl_lastName.setVisible(true);
        lbl_birthDate.setVisible(true);
        lbl_city.setVisible(true);
        lbl_email.setVisible(true);


        lbl_firstName.setText(Main.loggedUser.getFirstName());
        lbl_lastName.setText(Main.loggedUser.getLastName());
        lbl_birthDate.setText(Main.loggedUser.getBirthDate());
        lbl_city.setValue(Main.loggedUser.getCity());
        lbl_email.setText(Main.loggedUser.getEmail());
        updateButton.setVisible(true);

    }

    public void returnClick(){
        Main.switchScene("../View/MainScreen.fxml", Main.getStage(), 1000, 500);
    }
    public void updateClicked() throws SQLException {


        if (validateMail(lbl_email.getText())) {
            Main.loggedUser.setEmail(lbl_email.getText());
            erormail.setVisible(false);
        }
        else
            erormail.setVisible(true);
        if (validateName(lbl_firstName.getText())) {
            Main.loggedUser.setFirstName(lbl_firstName.getText());
            erorfirstname.setVisible(false);
        }
        else
            erorfirstname.setVisible(true);
        if (validateName(lbl_lastName.getText())) {
            Main.loggedUser.setLastName(lbl_lastName.getText());
            erorlastname.setVisible(false);
        }
        else
            erorlastname.setVisible(true);

        if(!erormail.isVisible()&&!erorlastname.isVisible()&&!erorfirstname.isVisible())
        {
            Main.loggedUser.setEmail(lbl_email.getText());
            Main.loggedUser.setFirstName(lbl_firstName.getText());
            Main.loggedUser.setLastName(lbl_lastName.getText());
            //Main.user.setCity(_city.getValue().toString());
            update(Main.loggedUser);
            Main.switchScene("../View/MainScreen.fxml", Main.getStage(), 720,500);
        }

    }
    public void delUser() throws SQLException {
        delete(Main.user.getUserName());
        Main.switchScene("../View/MainScreen.fxml", (Stage) updateButton.getScene().getWindow(), 720,500);

    }

    public void changepass(MouseEvent mouseEvent)
    {

        Main.switchScene("../View/SwitchPassword.fxml", (Stage) updateButton.getScene().getWindow(), 720,500);
    }
}
