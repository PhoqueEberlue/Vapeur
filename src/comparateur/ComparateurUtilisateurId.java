package comparateur;

import modele.Utilisateur;
import java.util.Comparator;

public class ComparateurUtilisateurId implements Comparator<Utilisateur> {
    @Override
    public int compare(Utilisateur p1, Utilisateur p2) {
        return Integer.compare(p1.getIdUtilisateur(), p2.getIdUtilisateur());
    }
}