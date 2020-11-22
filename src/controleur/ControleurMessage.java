package controleur;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Duration;

import modele.Utilisateur;
import vue.Jeux;
import vue.VueMessage;
import vue.VueVapeur;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

public class ControleurMessage implements EventHandler<ActionEvent> {

    private VueMessage vueMessage;

    public ControleurMessage(VueMessage vueMessage){
        this.vueMessage = vueMessage;
        Jeux.TIMELINES.put("message", refresher(this));
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if(!this.vueMessage.estVide()){
            try { this.vueMessage.envoyerMessage(); }
            catch(SQLException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Exception System");
                alert.setHeaderText("Don't find connection with database.");
                alert.setContentText("You probably did not connect your account to the platform.");

                Exception ex = new SQLException("You probably did not connect your account to the platform.");

                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                String exceptionText = sw.toString();

                Label label = new Label("The exception stacktrace was:");

                TextArea textArea = new TextArea(exceptionText);
                textArea.setEditable(false);
                textArea.setWrapText(true);

                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(textArea, Priority.ALWAYS);
                GridPane.setHgrow(textArea, Priority.ALWAYS);

                GridPane expContent = new GridPane();
                expContent.setMaxWidth(Double.MAX_VALUE);
                expContent.add(label, 0, 0);
                expContent.add(textArea, 0, 1);

                alert.getDialogPane().setExpandableContent(expContent);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("System Message");
            alert.setHeaderText("Empty message");
            alert.setContentText("You cannot send empty messages.");
            alert.showAndWait();
        }
    }

    private Timeline refresher(ControleurMessage cm) {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0),
                actionEvent -> {
                    try {
                        cm.vueMessage.updateMessages();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            ),
           new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        return timeline;
    }
}
