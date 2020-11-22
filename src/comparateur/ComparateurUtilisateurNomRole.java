package comparateur;

import modele.Utilisateur;
import java.util.Comparator;

public class ComparateurUtilisateurNomRole implements Comparator<Utilisateur> {

    @Override
    public int compare(Utilisateur p1, Utilisateur p2) {
        return p1.getRole().toLowerCase().compareTo(p2.getRole().toLowerCase());
    }
}