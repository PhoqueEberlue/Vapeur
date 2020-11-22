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
import vue.VueProfil;
import vue.VueVapeur;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class ControleurProfil implements EventHandler<ActionEvent> {

    private VueVapeur vueVapeur;
    private VueProfil vueProfil;

    public ControleurProfil(VueVapeur vueVapeur, VueProfil vueProfil){
        this.vueVapeur = vueVapeur;
        this.vueProfil = vueProfil;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Button bouton = (Button)actionEvent.getTarget();
        String texte = bouton.getText();
        switch (texte){
            case "UPLOAD A PROFILE PICTURE":
                break;
            case "EDIT YOUR PROFILE":
                vueProfil.activerBoutonValider(false);
                vueProfil.activerTextfield(false);
                vueProfil.activerBoutonValider(false);
                vueProfil.activerBoutonEditer(true);
                break;
            case "VALIDATE":
                        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
                        alert2.setTitle("System Message");
                        alert2.setHeaderText("Profile confirmation");
                        alert2.setContentText("Are you sure you want to modify your profile ?");
                        Optional<ButtonType> result2 = alert2.showAndWait();
                        if (result2.get() == ButtonType.OK){
                            try{
                                if(vueProfil.getPseudo().length() > 10 || vueProfil.getMotDePasse().length() > 50 || vueProfil.getEMail().length() > 50){
                                    creerAlerte("Invalid entry", "You have probably entered a username, password or email that is too long.");
                                } else if(contientDesEspaces(vueProfil.getPseudo()) || contientDesEspaces(vueProfil.getMotDePasse()) || contientDesEspaces(vueProfil.getEMail())){
                                    creerAlerte("Invalid entry", "There can't be any spaces.");
                                } else if(vueProfil.getPseudo().equals("") || vueProfil.getMotDePasse().equals("") || vueProfil.getEMail().equals("")){
                                    creerAlerte("Invalid entry", "Each field must at least have 1 character.");
                                } else if(vueProfil.getPseudo().equalsIgnoreCase(UtilisateurJDBC.getPseudoUt(VueVapeur.getClient().getIdUtilisateur()))) {
                                    UtilisateurJDBC.setPseudoUt(VueVapeur.getClient().getIdUtilisateur(), vueProfil.getPseudo());
                                    UtilisateurJDBC.setEmailUt(VueVapeur.getClient().getIdUtilisateur(), vueProfil.getEMail());
                                    UtilisateurJDBC.setPasswordUt(VueVapeur.getClient().getIdUtilisateur(), vueProfil.getMotDePasse());
                                    vueProfil.activerBoutonValider(true);
                                    vueProfil.activerTextfield(true);
                                    vueProfil.activerBoutonValider(true);
                                    vueProfil.activerBoutonEditer(false);
                                } else {
                                    creerAlerte("Invalid entry", "This username is already taken.");
                                }
                            }
                            catch(SQLException ignore){
                                creerAlerteSQL("Don't find connection with database.", "You probably did not connect your account to the platform.");
                            }
                            this.vueProfil.miseAJourAffichage();
                        }
                break;
            case "LOG OUT":
                Alert alert3 = new Alert(Alert.AlertType.CONFIRMATION);
                alert3.setTitle("System Message");
                alert3.setHeaderText("Log out confirmation");
                alert3.setContentText("Are you sure you want to log out ?");
                Optional<ButtonType> result3 = alert3.showAndWait();
                if (result3.get() == ButtonType.OK){
                    this.vueVapeur.showVue(this.vueVapeur.getVueConnexion());
                    System.out.println("déconnection...");
                    try{ UtilisateurJDBC.setActiveUt(VueVapeur.getClient().getIdUtilisateur(), "H"); }
                    catch(SQLException ignore){creerAlerteSQL("Don't find connection with database.", "You probably did not connect your account to the platform.");}
                }
                break;
        }
    }

    private boolean contientDesEspaces(String str){
        for(char c : str.toCharArray()){
            if(Character.isWhitespace(c)){
                return true;
            }
        }
        return false;
    }

    private void creerAlerte(String header, String body){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("System Message");
        alert.setHeaderText(header);
        alert.setContentText(body);
        alert.showAndWait();
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
