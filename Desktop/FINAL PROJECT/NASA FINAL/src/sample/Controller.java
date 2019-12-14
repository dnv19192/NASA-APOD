package sample;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;


public class Controller implements Initializable{

    @FXML
    private Label titleLabel;
    @FXML
    private Label explanationLabel;
    @FXML
    public Label errorLabel;
    @FXML
    public Label loadingLabel;
    @FXML
    public Label badSelection;
    @FXML
    private Label creditLabel;
    @FXML
    public Label saveCompletedLabel;
    @FXML
    private ImageView mainImageView;
    @FXML
    public ImageView loadingGIF;
    @FXML
    public ImageView spaceCatGIF;
    @FXML
    public ImageView errorImageView;
    @FXML
    public ImageView saveCompleted;
    @FXML
    public Hyperlink apodLink;
    @FXML
    private MenuButton menuButton;
    @FXML
    private DatePicker datePicker;

    private static String apodURL;
    private static ArrayList<APOD> apodList = new ArrayList<>();
    private Network apodRequest = new Network(LocalDate.now());
    private APOD currentAPOD = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emptyAllFields();
        getInitialAPODS();
        this.menuButton.getItems().get(2).setDisable(true);
        this.menuButton.getItems().get(3).setDisable(true);

        datePicker.setValue(LocalDate.now());
    }


    public void setDate(ActionEvent e){

        if (datePicker.getValue().isAfter(LocalDate.now()) || datePicker.getValue().isBefore(LocalDate.of(1998, 11, 25))){
            invalidDate();
        }else{
            getAPOD(datePicker.getValue());

        }
    }

    public void PressMenuButton(ActionEvent e){
        if (e.getSource().equals(menuButton.getItems().get(0))) {
            System.out.println("You pressed Save to Desktop!");
            saveToDesktop();

        }else if (e.getSource().equals(menuButton.getItems().get(1))) {
            UpdateUI(apodList.get(0));
            this.datePicker.setValue(apodList.get(0).getDate());
            System.out.println("Today");

        }else if (e.getSource().equals(menuButton.getItems().get(2))) {
            UpdateUI(apodList.get(1));
            this.datePicker.setValue(apodList.get(1).getDate());
            System.out.println("Yesterday");

        }else if (e.getSource().equals(menuButton.getItems().get(3))) {
            UpdateUI(apodList.get(2));
            this.datePicker.setValue(apodList.get(2).getDate());
            System.out.println("Day Before");

        }
    }

    public void openBrowser(){
        try{
            String day = "";

            if (datePicker.getValue().getDayOfMonth() < 10){
                day = ("0"+datePicker.getValue().getDayOfMonth());
            }else{
                day = (String.valueOf(datePicker.getValue().getDayOfMonth()));
            }

            String month = "";

            if (datePicker.getValue().getMonth().getValue() < 10){
                month = ("0"+datePicker.getValue().getMonth().getValue());
            }else{
                month = (String.valueOf(datePicker.getValue().getMonth().getValue()));
            }

            String customDate = ((datePicker.getValue().getYear() - 2000) +""+(month)+
                    ""+day);
            Desktop.getDesktop().browse(new URI("https://apod.nasa.gov/apod/ap"+customDate+".html"));

        }catch(Exception e){
            System.out.println("Error opening browser");
        }
    }

    public void errorUI(String text, boolean isEnabled){
        errorLabel.setText(text);
        this.errorLabel.setVisible(isEnabled);
        loadingGIF.setVisible(!isEnabled);
    }

    public void saveToDesktop(){
        if (currentAPOD != null){
            try{
                BufferedImage bImage = SwingFXUtils.fromFXImage(this.mainImageView.getImage(),null);
                File outfile = new File(System.getProperty("user.home") + "/Desktop/"+this.titleLabel.getText()+".png");
                ImageIO.write(bImage, "png", outfile);
                successfulSave();

            }catch (Exception e){
                System.out.println(e.getLocalizedMessage());
            }
        }
    }



    public void UpdateUI(APOD apod) {
        emptyAllFields();

        if (apod != null && apod.getImage() != null) {
            this.titleLabel.setText((apod.getTitle() == null) ? " " : apod.getTitle());
            this.explanationLabel.setText((apod.getExplanationText() == null) ? " " : apod.getExplanationText());
            this.creditLabel.setText((apod.getCreditText() == null) ? " " : apod.getCreditText());
            this.mainImageView.setImage(apod.getImage());
            this.currentAPOD = apod;
        }else{
            System.out.println("APOD is null or interactive");
            emptyAllFields();
            errorUI("Interactive APOD.", true);
            this.spaceCatGIF.setVisible(true);
            this.apodLink.setText(apodURL);
            this.loadingLabel.setText("To view APOD click link");
            this.loadingLabel.setVisible(true);
            this.apodLink.setVisited(false);
            this.apodLink.setVisible(true);
        }
    }


    public void emptyAllFields(){
        errorUI(" ",false);
        stopGIF();
        this.apodLink.setVisible(false);
        this.apodLink.setVisible(false);
        this.spaceCatGIF.setVisible(false);
        this.titleLabel.setText(" ");
        this.explanationLabel.setText(" ");
        this.creditLabel.setText(" ");
        this.mainImageView.setImage(null);
    }

    public void invalidDate(){
        errorImageView.setVisible(true);
        badSelection.setVisible(true);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() ->{
                    errorImageView.setVisible(false);
                    badSelection.setVisible(false);
                });
            }
        };

        Timer timer = new Timer();
        timer.schedule(task,2500);

    }

    public void successfulSave(){
        this.saveCompleted.setVisible(true);
        this.saveCompletedLabel.setVisible(true);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() ->{
                    saveCompleted.setVisible(false);
                    saveCompletedLabel.setVisible(false);
                });
            }
        };

        Timer timer = new Timer();
        timer.schedule(task,2500);

    }

    public void beginGIF(){
        this.loadingGIF.setVisible(true);
        this.loadingLabel.setVisible(true);
    }

    public void stopGIF(){
        this.loadingGIF.setVisible(false);
        this.loadingLabel.setVisible(false);
    }

    public void enableControls(boolean isEnabled) {
        this.menuButton.getItems().get(0).setDisable(!isEnabled);
        this.menuButton.getItems().get(1).setDisable(!isEnabled);
        this.menuButton.getItems().get(2).setDisable(!isEnabled);
        this.menuButton.getItems().get(3).setDisable(!isEnabled);
        this.datePicker.setDisable(!isEnabled);
    }

    public static void setApodList(ArrayList<APOD> apodList) {
        Controller.apodList = apodList;
    }

    public static void addAPOD(APOD apod){

        if (!Controller.apodList.contains(apod)){
            Controller.apodList.add(apod);
            System.out.println("APOD successfully added to ArrayList.");
        }else{
            System.out.println("APOD already exists.");
        }
    }

    public static void setApodURL(String url){
        Controller.apodURL = url;
    }

    public void getInitialAPODS(){
        Network apodRequest = new Network(LocalDate.now());

        System.out.print("Retrieving Today APOD: ");

        APOD todayAPOD = apodRequest.getAPOD();
        Controller.addAPOD(todayAPOD);
        UpdateUI(apodList.get(0));

        CompletableFuture.runAsync(() -> {

            apodRequest.setDate(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth() -1));
            APOD yesterdayAPOD = apodRequest.getAPOD();
            apodList.add(yesterdayAPOD);

            apodRequest.setDate(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth() -2));
            APOD dayBefore = apodRequest.getAPOD();
            apodList.add(dayBefore);

            Platform.runLater(() -> {
                this.enableControls(true);
            });
        });
    }

    private void getAPOD(LocalDate date){
        boolean doesExist = false;
        currentAPOD = null;

        for (APOD apod : apodList) {

            if (apod != null && apod.getDate().isEqual(date)){
                doesExist = true;
                currentAPOD = apod;
            }

        }

        if (!doesExist){
            System.out.println(doesExist);
            emptyAllFields();
            loadingLabel.setText("Loading...");
            enableControls(false);
            beginGIF();
            apodRequest.setDate(date);

            CompletableFuture.runAsync(() -> {
                APOD result = apodRequest.getAPOD();

                if (result != null){
                    addAPOD(result);
                }
                Platform.runLater(() ->{
                    UpdateUI(result);
                    enableControls(true);
                    this.currentAPOD = result;
                });

            });

        }else{

            System.out.println("Loading APOD from apodList");
            UpdateUI(currentAPOD);
            enableControls(true);

        }
    }
}
