package executable;

import jdbc.ConnexionMySQL;
import jdbc.InvitationJDBC;
import jdbc.PartieJDBC;
import jdbc.UtilisateurJDBC;
import modele.Moderateur;
import modele.Utilisateur;

import java.sql.SQLException;

public class ExecutableUtilisateur {
    public static void main (String[] args) throws ClassNotFoundException, SQLException {
        ConnexionMySQL connexionMySQL = new ConnexionMySQL();
        connexionMySQL.connecter();
        Moderateur modo = new Moderateur(5, "Bernard", "azefkjberg", "bernard@gmail.com", "util", "O");
        Utilisateur utilisateur = new Utilisateur(2, "Bernard", "azefkjberg", "bernard@gmail.com", "util", "O");
        /*
        System.out.println(utilisateur.getListeAmis(UtJDBC));
        System.out.println(modo);
        UtilisateurJDBC p4connec = new UtilisateurJDBC(connexionMySQL);
        Utilisateur utilisateur2 = new Utilisateur(2, p4connec);
        System.out.println(utilisateur2.addAmi("administra", p4connec));
        */
        System.out.println(modo.getListeJoueur("Connected"));
        System.out.println(modo.getListePartie("score"));
        System.out.println(utilisateur.getInvitation());
    }
}