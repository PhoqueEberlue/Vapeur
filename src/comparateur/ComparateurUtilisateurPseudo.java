package comparateur;

import modele.Utilisateur;
import java.util.Comparator;

public class ComparateurUtilisateurPseudo implements Comparator<Utilisateur> {

    @Override
    public int compare(Utilisateur p1, Utilisateur p2) {
        return p1.getNomUtilisateur().toLowerCase().compareTo(p2.getNomUtilisateur().toLowerCase());
    }
}