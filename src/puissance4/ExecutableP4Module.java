package puissance4;

import java.util.ArrayList;

public class ExecutableP4Module {
    public static void main (String[] args){

        Grille g = new Grille(true);
        g.ajoutPionGrille(0); //j
        g.ajoutPionGrille(1); //r
        g.ajoutPionGrille(1); //j
        g.ajoutPionGrille(2); //r
        g.ajoutPionGrille(2); //j
        g.ajoutPionGrille(3); //r
        g.ajoutPionGrille(2); //j
        g.ajoutPionGrille(3); //r
        g.ajoutPionGrille(3); //j
        g.ajoutPionGrille(4); //r
        g.ajoutPionGrille(3); //j
        System.out.println(g.toString());
        System.out.println(g.getVictoireGlobal());


        /*
        String json = g.toJson();
        System.out.println(json);
        ArrayList<ArrayList<String>> array = g.toArray(json);
        System.out.println(array);
        Grille d = new Grille();
        d.updateGrille(json);
        System.out.println(d.toString());
        */
    }
    
}