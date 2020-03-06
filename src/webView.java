import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


import java.awt.*;
import java.io.FileInputStream;
import java.util.ArrayList;


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

        TextField current = new TextField();
        current.setEditable(true);
        current.setPromptText("URL");
        current.setText(URL.getText());

        // Displays the tabs above the URL
        HBox tabs = new HBox();
        tabs.getChildren().addAll(addTab, current);

        addTab.setOnAction(e -> {
            TextField newTab = new TextField();
            newTab.setEditable(true);
            newTab.setPromptText("URL");
            newTab.setOnKeyPressed(ev -> {
                if (ev.getCode() == KeyCode.ENTER) {
                    URL.setText(newTab.getText());
                    webEngine.load(URL.getText());
                    URLsVisited.add(URL.getText());
                    pageCount++;
                }
            });
            tabs.getChildren().add(newTab);
        });

        Image reverseArrow = new Image(new FileInputStream("img/reverseArrow.png"), 20, 15, true, true);
        Image star = new Image(new FileInputStream("img/favorites.png"), 20, 15, true,  true);
        Image goldStar = new Image(new FileInputStream("img/goldStar.png"), 20, 15, true, true);

        Button previousPage = new Button();
        previousPage.setGraphic(new ImageView(reverseArrow));

        Button favorites = new Button();
        favorites.setGraphic(new ImageView(star));

        // Store favorite links
        ComboBox favSites = new ComboBox();

        URL.setPrefWidth(screenSize.getWidth() - previousPage.getWidth() - favorites.getWidth() - favSites.getWidth());

        HBox topBar = new HBox();
        topBar.getChildren().addAll(previousPage, URL, favorites, favSites);
        topBar.setAlignment(Pos.TOP_LEFT);

        // return to last website visited
        previousPage.setOnAction(e-> {
            webEngine.load(URLsVisited.get(pageCount-1));
            URLsVisited.add(URLsVisited.get(pageCount-1));
            URL.setText(URLsVisited.get(pageCount-1));
            if (favSites.getItems().contains(URLsVisited.get(pageCount-1))) {
                favorites.setGraphic(new ImageView(goldStar));
            }
            else {
                favorites.setGraphic(new ImageView(star));
            }
        });

        // hit the enter key to go to the input website
        URL.setOnKeyPressed(e-> {
            if (e.getCode() == KeyCode.ENTER) {
                webEngine.load(URL.getText());
                URLsVisited.add(URL.getText());
                pageCount++;
                if (favSites.getItems().contains(URL.getText())) {
                    favorites.setGraphic(new ImageView(goldStar));
                }
                else {
                    favorites.setGraphic(new ImageView(star));
                }
            }
        });

        // Button
        favorites.setOnAction(e-> {
            if (!favSites.getItems().contains(URL.getText())) {
                favSites.getItems().add(URL.getText());
            }
            favorites.setGraphic(new ImageView(goldStar));
        });

        // Go site fav site when clicked on
        favSites.setOnAction(e-> {
            URL.setText((String) favSites.getValue());
            webEngine.load((String) favSites.getValue());
            favorites.setGraphic(new ImageView(goldStar));
        });


        VBox pane = new VBox();
        pane.setAlignment(Pos.TOP_LEFT);
        pane.getChildren().addAll(tabs, topBar, webView);


        Scene scene = new Scene(pane, 1000, 1000);

        stage.setTitle("Beehive Browser");
        stage.setScene(scene);
        stage.show();
    }

}
