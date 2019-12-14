package sample;

import javafx.scene.image.Image;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

public class Network {

    private static String nasaAPODURL;
    private Image apodImage;
    private String title;
    private String explanationText;
    private String creditText;
    private LocalDate date;
    private JSONObject json;



    public Network(LocalDate date) {
        this.date = date;
    }

    public void retrieveJson(LocalDate date){
        JSONParser parser = new JSONParser();
        nasaAPODURL = ("https://api.nasa.gov/planetary/apod?api_key=/*INSERT API KEY HERE*/"+"&date="+date.toString());
        System.out.println(nasaAPODURL);
        Controller.setApodURL(nasaAPODURL);

        try{
            URL url = new URL(nasaAPODURL);
            System.out.print("Sending API Request...");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept-Charset", "UTF-8");


            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            this.json = (JSONObject) parser.parse(bufferedReader.readLine());

            if (connection.getResponseCode() == 200 && this.json != null){
                this.explanationText = (json.get("explanation").toString() != null) ? json.get("explanation").toString() : " ";
                this.creditText = (json.get("copyright") != null) ? "Credit: "+json.get("copyright").toString() : " ";
                this.title = (json.get("title") != null) ? json.get("title").toString() : " ";
                retrieveImage((json.get("hdurl") != null) ? json.get("hdurl").toString() : null);
            }else{
                
                System.out.println("Error Connecting, Resposne Code: " + connection.getResponseCode());
            }

            connection.disconnect();

        }catch(Exception e){
            System.out.println("Exception:(");
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private void retrieveImage(String url){
        JSONParser parser = new JSONParser();
        try{
            if (url != null){
                this.apodImage = new Image(new URL(url).openStream());
                System.out.println("OK!");
            }else{
                this.apodImage = null;
            }

        }catch(Exception e){

            e.printStackTrace();
        }
    }

    public void sendRequest(){
        retrieveJson(this.date);
        Controller.addAPOD(checkResponse());
    }

    public APOD getAPOD(){
        retrieveJson(this.date);
        return checkResponse();
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return this.date;
    }

    private APOD checkResponse(){

        if (apodImage != null){
            return new APOD(this.title, this.explanationText, this.creditText, this.date, this.apodImage);
        }
        return null;
    }
}
