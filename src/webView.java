import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class webView extends Application {
    ArrayList<String> URLsVisited = new ArrayList<>();
    int pageCount = 0;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    // Website the program loads to when starting up
    final String defaultURL = "https://www.cs.usu.edu/";

    // current URL
    TextField URL = new TextField(defaultURL);


    @Override
    public void start(Stage stage) throws Exception {
        WebView webView = new WebView();
        webView.setPrefHeight(screenSize.getHeight());

        URLsVisited.add("https://www.cs.usu.edu/");
        WebEngine webEngine = webView.getEngine();
        webEngine.load(defaultURL);

        Image plusSign = new Image(new FileInputStream("img/plus.png"), 20, 15, true, true);
        Button addTab = new Button();
        addTab.setGraphic(new ImageView(plusSign));


        // Displays the tabs above the URL
        HBox tabs = new HBox();
        tabs.getChildren().addAll(addTab);

        addTab.setOnAction(e -> {
            TextField newTab = new TextField();
            newTab.setEditable(true);
            newTab.setPromptText("URL");
            newTab.setOnKeyPressed(ev -> {
                if (ev.getCode() == KeyCode.ENTER) {
                    URL.setText(newTab.getText());
                    if (isValidURL(URL.getText()))
                        webEngine.load(URL.getText());
                    else
                        webEngine.load("https://www.google.com/search?q=" + URL.getText());
                    URLsVisited.add(URL.getText());
                    pageCount++;
                }
            });
            newTab.setOnMouseClicked(eve -> {
                URL.setText(newTab.getText());
                webEngine.load(URL.getText());
                URLsVisited.add(URL.getText());
                pageCount++;
            });
            tabs.getChildren().add(newTab);
        });

        Image reverseArrow = new Image(new FileInputStream("img/reverseArrow.png"), 20, 15, true, true);
        Image hive = new Image(new FileInputStream("img/blankHive.png"), 20, 15, true,  true);
        Image goldHive = new Image(new FileInputStream("img/honeyStick.png"), 20, 15, true, true);


        Button previousPage = new Button();
        previousPage.setGraphic(new ImageView(reverseArrow));

        Button favorites = new Button();
        favorites.setGraphic(new ImageView(hive));

        // Store favorite links
        ComboBox favSites = new ComboBox();
        Scanner scan = new Scanner(new File("data/favorites.txt"));
        while (scan.hasNextLine()) {
            favSites.getItems().add(scan.nextLine());
        }

        // Listener to manage whether site should be marked as favorite (gold star) or not
        URL.textProperty().addListener((ov, oldVal, newVal) -> {
            if (favSites.getItems().contains(URL.getText())) {
                favorites.setGraphic(new ImageView(goldHive));
            }
            else {
                favorites.setGraphic(new ImageView(hive));
            }
        });


        Spinner<Double> zoom = new Spinner<Double>(0.5, 5.0, 1.0);
        zoom.increment(1);

        zoom.valueProperty().addListener(ov -> {
            webView.setZoom(zoom.getValue());
        });



        // set URL box to span the length of the screen
        URL.setPrefWidth(screenSize.getWidth() - previousPage.getWidth() - favorites.getWidth() - favSites.getWidth() - zoom.getWidth());

        HBox topBar = new HBox();
        topBar.getChildren().addAll(previousPage, URL, zoom, favorites, favSites);
        topBar.setAlignment(Pos.TOP_LEFT);

        // return to last website visited
        previousPage.setOnAction(e-> {
            webEngine.load(URLsVisited.get(pageCount-1));
            URLsVisited.add(URLsVisited.get(pageCount-1));
            URL.setText(URLsVisited.get(pageCount-1));
        });

        // hit the enter key to go to the input website
        URL.setOnKeyPressed(e-> {
            if (e.getCode() == KeyCode.ENTER) {
                // go to website
                if (isValidURL(URL.getText()))
                    webEngine.load(URL.getText());
                // google result
                else
                    webEngine.load("https://www.google.com/search?q=" + URL.getText());
                URLsVisited.add(URL.getText());
                pageCount++;
            }
        });



        // favorites button
        favorites.setOnAction(e-> {
            if (!favSites.getItems().contains(URL.getText())) {
                favSites.getItems().add(URL.getText());
                // Adds favorite sites to txt file
                FileWriter fileWriter = null;
                try {
                    fileWriter = new FileWriter("data/favorites.txt", true);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    fileWriter.write(URL.getText()+"\n");
                    fileWriter.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                favorites.setGraphic(new ImageView(goldHive));
            }
        });

        // Go to fav site when clicked on
        favSites.setOnAction(e-> {
            URL.setText((String) favSites.getValue());
            webEngine.load((String) favSites.getValue());
        });


        VBox pane = new VBox();
        pane.setAlignment(Pos.TOP_LEFT);
        pane.getChildren().addAll(tabs, topBar, webView);

        beeBuzz(pane);


        Scene scene = new Scene(pane, 1000, 1000);

        stage.setTitle("Beehive Browser");
        stage.setScene(scene);
        stage.show();
    }

    private boolean isValidURL(String url) {
        return url.startsWith("https:");
    }

    // Bee animation at bottom of screen
    private void beeBuzz(VBox pane) throws FileNotFoundException {
        Image bee = new Image(new FileInputStream("img/bee.png"), 20, 15, true, true);
        ImageView b = new ImageView(bee);
        b.setFitHeight(50);
        b.setFitWidth(50);
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(2);
        timeline.setAutoReverse(true);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(10),
                new KeyValue(b.translateXProperty(), 1000)));
        timeline.play();
        pane.getChildren().add(b);
        b.toFront();
    }

}