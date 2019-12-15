package sample;

import javafx.scene.image.Image;

import java.time.LocalDate;

public class APOD {

    private String title;
    private String explanationText;
    private Image image;
    private String creditText;
    private LocalDate date;


    public APOD(String title, String explanationText, String creditText, LocalDate date,Image image) {
        this.title = title;
        this.explanationText = explanationText;
        this.date = date;
        this.image = image;
        this.creditText = creditText;
    }

    public APOD() {
        this.title = title;
        this.explanationText = explanationText;
        this.date = LocalDate.now();
        this.image = null;
        this.creditText = creditText;

    }

    public void setExplanationText(String explanationText) {
        this.explanationText = explanationText;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setCreditText(String creditText) {
        this.creditText = creditText;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getExplanationText() {
        return explanationText;
    }

    public Image getImage() {
        return image;
    }

    public String getCreditText() {
        return creditText;
    }


    public boolean isAnyNull(){

        if (this.creditText == null || this.explanationText == null || this.image == null || this.title == null){
            return true;
        }
        return false;
    }

    public LocalDate getDate() {
        return date;
    }

}
