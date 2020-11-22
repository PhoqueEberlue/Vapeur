package executable;

import jdbc.ConnexionMySQL;
import jdbc.MessageJDBC;
import jdbc.UtilisateurJDBC;
import modele.Message;
import vue.VueVapeur;

import java.sql.SQLException;

public class ExecutableMessage {
    public static void main (String[] args) throws ClassNotFoundException, SQLException {
        ConnexionMySQL connexionMySQL = new ConnexionMySQL();
        connexionMySQL.connecter();
        Message msg = new Message(3, 2,"AHAHAHAHAHAHHAA");

        UtilisateurJDBC.setPasswordUt(3, "mdpsteven");
        //MessageJDBC.ajouterMessage(msg);
        //msgconnect.updateMessage(2, "Coucou les amis");
        //msgconnect.supprimerMessage(msg);
        //VueVapeur vueVapeur = new VueVapeur();
        //vueVapeur.init();
        //vueVapeur.getVueMatchHistory();
    }
}