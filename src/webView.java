import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
    @Override
    public void start(Stage stage) throws Exception {
        WebView webView = new WebView();
        final String defaultURL = "https://www.cs.usu.edu/";
        URLsVisited.add("https://www.cs.usu.edu/");
        WebEngine webEngine = webView.getEngine();
        webEngine.load(defaultURL);

        Image reverseArrow = new Image(new FileInputStream("img/reverseArrow.png"), 20, 15, true, true);
        Image star = new Image(new FileInputStream("img/favorites.png"), 20, 15, true,  true);

        Button previousPage = new Button();
        previousPage.setGraphic(new ImageView(reverseArrow));

        Button favorites = new Button();
        favorites.setGraphic(new ImageView(star));

        ComboBox<String> favSites = new ComboBox();

        TextField URL = new TextField(defaultURL);
        URL.setPrefWidth(screenSize.getWidth() - previousPage.getWidth() - favorites.getWidth() - favSites.getWidth());

        HBox topBar = new HBox();
        topBar.getChildren().addAll(previousPage, URL, favorites, favSites);
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
                webEngine.load(URL.getText());
                URLsVisited.add(URL.getText());
                pageCount++;
            }
        });

        favorites.setOnAction(e-> {
            if (!favSites.getItems().contains(URL.getText())) {
                favSites.getItems().add(URL.getText());
            }
        });


        VBox pane = new VBox();
        pane.setAlignment(Pos.TOP_LEFT);
        pane.getChildren().addAll(topBar, webView);


        Scene scene = new Scene(pane, 1000, 1000);

        stage.setTitle("Beehive Browser");
        stage.setScene(scene);
        stage.show();
    }

}
