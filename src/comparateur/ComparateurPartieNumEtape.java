package comparateur;

import modele.Partie;
import java.util.Comparator;

public class ComparateurPartieNumEtape implements Comparator<Partie> {

    @Override
    public int compare(Partie p1, Partie p2) {
        return Integer.compare(p1.getNumetape(), p2.getNumetape());
    }
}