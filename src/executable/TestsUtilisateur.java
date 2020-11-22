package executable;

import jdbc.ConnexionMySQL;
import jdbc.UtilisateurJDBC;
import modele.Utilisateur;

import java.sql.SQLException;

public class TestsUtilisateur {
  public static void main (String[] args) throws ClassNotFoundException, SQLException {
    ConnexionMySQL connexionMySQL = new ConnexionMySQL();
    connexionMySQL.connecter();
    Utilisateur utilisateur = new Utilisateur(2, "Bernard", "azefkjberg", "bernard@gmail.com", "util", "O");
    UtilisateurJDBC.supprimerJoueur(utilisateur);
  }
}