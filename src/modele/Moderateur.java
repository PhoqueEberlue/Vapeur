package modele;

import comparateur.*;
import jdbc.InvitationJDBC;
import jdbc.PartieJDBC;
import jdbc.UtilisateurJDBC;
import java.sql.SQLException;
import java.util.ArrayList;

public class Moderateur extends Utilisateur {

    public Moderateur(int idUtilisateur, String nomUtilisateur, String motDePasse, String email, String role, String etat) {
        super(idUtilisateur, nomUtilisateur, motDePasse, email, role, etat);
    }

    public static ArrayList<Utilisateur> filtrageParEtat(ArrayList<Utilisateur> unfilteredArray,String etat){
        ArrayList<Utilisateur> filteredArray = new ArrayList<>();
        for(Utilisateur ut: unfilteredArray){
            if(ut.getEtat().equals(etat)){
                filteredArray.add(ut);
            }
        }
        return filteredArray;
    }

    public ArrayList<Utilisateur> getListeJoueur(String paramTri) throws SQLException {
        ArrayList<Utilisateur> res = UtilisateurJDBC.getListeJoueur();
        switch (paramTri) {
            case "id":
                res.sort(new ComparateurUtilisateurId());
                break;
            case "pseudo":
                res.sort(new ComparateurUtilisateurPseudo());
                break;
            case "role":
                res.sort(new ComparateurUtilisateurNomRole());
                break;
            case "Connected":
                res = filtrageParEtat(res, "O");
                break;
            case "Disconected":
                res = filtrageParEtat(res, "D");
                break;
            case "Blocked":
                res = filtrageParEtat(res, "B");
                break;
        }
        return res;
    }

    public ArrayList<Partie> getListePartie(String paramTri) throws SQLException {
        ArrayList<Partie> res = PartieJDBC.getListePartie();
        if(paramTri.equals("score")){
            res.sort(new ComparateurPartieScore());
        }
        else if(paramTri.equals("numEtape")){
            res.sort(new ComparateurPartieNumEtape());
        }
        return res;
    }
    /*
    public ArrayList<Invitation> getListeInvitation(String paramTri) throws SQLException {

    }
    */
}