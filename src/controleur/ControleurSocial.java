package controleur;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import modele.Utilisateur;
import vue.Palette;
import vue.VueAdmin;
import vue.VueSocial;
import vue.VueVapeur;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

public class ControleurSocial implements EventHandler<ActionEvent> {

    private VueVapeur vueVapeur;
    private VueSocial vueSocial;

    public ControleurSocial(VueVapeur vueVapeur, VueSocial vueSocial){
        this.vueVapeur = vueVapeur;
        this.vueSocial = vueSocial;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Button bouton = (Button)actionEvent.getTarget();
        String texte = bouton.getText();
        switch (texte) {
            case "CHAT":
                try {
                    this.vueVapeur.showVue(this.vueVapeur.getVueMessage(this.vueSocial.getAmisActuel()));
                } catch (SQLException e) {
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
                break;
        }
    }
}
