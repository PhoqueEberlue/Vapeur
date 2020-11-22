package controleur;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import jdbc.InvitationJDBC;
import jdbc.UtilisateurJDBC;
import modele.Invitation;
import modele.Utilisateur;
import vue.VueAdmin;
import vue.VueSocial;
import vue.VueVapeur;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Optional;

public class ControleurActionsAdmin implements EventHandler<ActionEvent> {

    private VueAdmin vueAdmin;
    private VueVapeur vueVapeur;

    public ControleurActionsAdmin(VueVapeur vueVapeur, VueAdmin vueAdmin){
        this.vueVapeur = vueVapeur;
        this.vueAdmin = vueAdmin;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Button bouton = (Button)actionEvent.getTarget();
        int id = Integer.parseInt(bouton.getId());
        String texte = bouton.getText();
        switch (texte){
            case "CHAT":
                try {
                    this.vueVapeur.showVue(this.vueVapeur.getVueMessage(UtilisateurJDBC.getUtilisateurWithId(id)));
                } catch (SQLException e) {creerAlerteSQL("Don't find connection with database.", "You probably did not connect your account to the platform.");}
                break;
            case "BLOCK":
                boolean confirmBlock = false;
                try{
                    confirmBlock = creerAlerteConfirmation("Blocking confirmation", "Are you sure you want to block " + UtilisateurJDBC.getPseudoUt(id) + "'s\naccess to Vapeur?");
                } catch(SQLException ignore){creerAlerteSQL("Don't find connection with database.", "You probably did not connect your account to the platform.");}
                if(confirmBlock){
                    try{ UtilisateurJDBC.setActiveUt(id, "B"); }
                    catch(SQLException ignore){creerAlerteSQL("Don't find connection with database.", "You probably did not connect your account to the platform.");}
                    this.vueAdmin.miseAJourUsers(id, "Blocked", "UNBLOCK");
                }
                break;
            case "UNBLOCK":
                boolean confirmUnblock = false;
                try{
                    confirmUnblock = creerAlerteConfirmation("Unblocking confirmation", "Are you sure you want to unblock " + UtilisateurJDBC.getPseudoUt(id) + "'s\naccess to Vapeur?");
                } catch(SQLException ignore){creerAlerteSQL("Don't find connection with database.", "You probably did not connect your account to the platform.");}
                if(confirmUnblock){
                    try{ UtilisateurJDBC.setActiveUt(id, "H"); }
                    catch(SQLException ignore){creerAlerteSQL("Don't find connection with database.", "You probably did not connect your account to the platform.");}
                    this.vueAdmin.miseAJourUsers(id, "Offline", "BLOCK");
                }
                break;
            default:
                creerAlerte("Option " + texte + " inconnue", "Problème de définition des boutons");
        }
    }

    private void creerAlerte(String header, String body){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("System Message");
        alert.setHeaderText(header);
        alert.setContentText(body);
        alert.showAndWait();
    }

    private boolean creerAlerteConfirmation(String header, String body){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("System Message");
        alert.setHeaderText(header);
        alert.setContentText(body);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        }
        return false;
    }

    private void creerAlerteSQL(String header, String body){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception System");
        alert.setHeaderText(header);
        alert.setContentText(body);

        Exception ex = new SQLException(body);

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

}
