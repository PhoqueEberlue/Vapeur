package comparateur;

import modele.Partie;
import java.util.Comparator;

public class ComparateurPartieScore implements Comparator<Partie> {

    @Override
    public int compare(Partie p1, Partie p2) {
        return Integer.compare(p1.getMaxScore(), p2.getMaxScore());
    }
}