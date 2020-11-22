package controleur;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.application.Platform;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import jdbc.UtilisateurJDBC;
import modele.Utilisateur;
import vue.VueVBoxParent;
import vue.VueVapeur;

public class ControleurConnexion implements EventHandler<ActionEvent> {
    
    private VueVapeur vueVapeur;
    private VueVBoxParent vueVBox;

    public ControleurConnexion(VueVapeur vueVapeur, VueVBoxParent vueVBox){
        this.vueVapeur = vueVapeur;
        this.vueVBox = vueVBox;
    }

    @Override
    public void handle(ActionEvent actionEvent){
        String texte = ((Button)actionEvent.getTarget()).getText();
        switch (texte){
            case "LOG IN":
                //l'utilisateur est connecté à la plateforme si son identifiant et mot de passe sont corrects
                if(connexionPossible()){
                    try{
                        VueVapeur.setClient(UtilisateurJDBC.getUtilisateurWithId(UtilisateurJDBC.getIdUt(this.vueVBox.getPseudo())));
                    }
                    catch(SQLException ignore){
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
                    System.out.println("connection...");
                    try{ UtilisateurJDBC.setActiveUt(VueVapeur.getClient().getIdUtilisateur(), "O"); }
                    catch(SQLException ignore){}

                    this.vueVapeur.initialisationDesVues();
                    this.vueVapeur.showVue(this.vueVapeur.getVueJeux());
                }
                break;
            case "CREATE ACCOUNT":
                //Les valeurs entrées par l'utilisateur
                String pseudo = this.vueVBox.getPseudo();
                String mdp = this.vueVBox.getMotDePasse();
                String email = this.vueVBox.getEmail();
                //un nouveau compte est créer dans la BD et l'utilisateur est connecté si les données sont correctes
                if(inscriptionPossible(pseudo, mdp, email)){
                    try{
                        Utilisateur util = new Utilisateur(UtilisateurJDBC.getNewIdJoueur(), pseudo, mdp, email, "util", "O");
                        UtilisateurJDBC.ajouterJoueur(util);
                        VueVapeur.setClient(util);
                    }
                    catch(SQLException ignore){
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
                    System.out.println("connection...");
                    try{ UtilisateurJDBC.setActiveUt(VueVapeur.getClient().getIdUtilisateur(), "O"); }
                    catch(SQLException ignore){}
                    
                    this.vueVapeur.initialisationDesVues();
                    this.vueVapeur.showVue(this.vueVapeur.getVueJeux());
                }
                break;
            case "CREATE AN ACCOUNT":
                this.vueVapeur.showVue(this.vueVapeur.getVueInscription());
                break;
            case "CANCEL":
                this.vueVapeur.showVue(this.vueVapeur.getVueConnexion());
                break;
            case "QUIT":
                try {
                    VueVapeur.getConnexionMySQL().close();
                    this.vueVapeur.stop();
                } catch (Exception ignore){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Exception System");
                    alert.setHeaderText("Don't find connection with database.");
                    alert.setContentText("You probably did not connect your account to the platform.");

                    Exception ex = new Exception("You probably did not connect your account to the platform.");

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
                Platform.exit();
                System.exit(0);
                break;
            default:
                creerAlerte("Option " + texte + " inconnue", "Problème de définition des boutons");
        }
    }

    private boolean connexionPossible(){
        boolean pseudoDansBD = false;
        boolean motDePasseCorrect = false;
        boolean estBloque = false;
        try{
            pseudoDansBD = UtilisateurJDBC.pseudoDansBD(this.vueVBox.getPseudo());
            estBloque = UtilisateurJDBC.getActiveUt(UtilisateurJDBC.getIdUt(this.vueVBox.getPseudo())).equals("B");
            motDePasseCorrect = UtilisateurJDBC.verificationBD(this.vueVBox.getPseudo(), this.vueVBox.getMotDePasse());
        } catch(SQLException ignore){
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

        if(!pseudoDansBD){
            creerAlerte("Incorrect username", "The specified username does not exist.");
        }
        else if(estBloque){
            creerAlerte("Blocked account", "Your account has been blocked by an \nadministrator.");
        }
        else if(!motDePasseCorrect){
            creerAlerte("Incorrect password", "The specified password does not correspond to \nthe username " + this.vueVBox.getPseudo() + ".");
        }

        return pseudoDansBD && motDePasseCorrect && !estBloque;
    }

    private boolean inscriptionPossible(String pseudo, String mdp, String email){
        boolean possible = true;
        String msg = "";

        try{
            //detection d'erreurs
            if(pseudo.length() > 10 || mdp.length() > 50 || email.length() > 50){
                possible = false;
                creerAlerte("Invalid entry", "The username is too long.");
            }
            else if(contientDesEspaces(pseudo) || contientDesEspaces(mdp) || contientDesEspaces(email)){
                possible = false;
                creerAlerte("Invalid entry", "There can't be any spaces.");
            }
            else if(pseudo.equals("") || mdp.equals("") || email.equals("")){
                possible = false;
                creerAlerte("Invalid entry", "Each field must at least have 1 character.");
            }
            else if(UtilisateurJDBC.pseudoDansBD(pseudo)){
                possible = false;
                creerAlerte("Invalid entry", "This username is already taken.");
            }
        } catch(SQLException ignore){
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

        return possible;
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("System Message");
        alert.setHeaderText(header);
        alert.setContentText(body);
        alert.showAndWait();
    } 

}