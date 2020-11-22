package puissance4;

import java.util.ArrayList;
import java.util.List;

public class Grille {
    
    private List<Colonne> array;
    String Newligne=System.getProperty("line.separator");
    private boolean victoireJaune; 
    private boolean victoireRouge; 
    private int colonne;
    private int nbPionR = 21;
    private int nbPionJ = 21;
    private boolean couleurPion;


    public Grille(boolean couleurPion){
        this.array = new ArrayList<>();
        for(int i=0;i<7;i++){
            Colonne c = new Colonne();
            this.array.add(c);
            this.victoireJaune=false;
            this.victoireRouge=false;
            this.colonne=colonne;
            this.nbPionR=nbPionR;
            this.nbPionJ=nbPionJ;
            this.couleurPion=couleurPion;
        }
    }

    public void ajoutPionGrille(int n){
        Pion p = new Pion(couleurPion);
        this.getColonne(n).ajoutPionColonne(p);
        this.colonne=n;
        if(couleurPion){
            this.nbPionJ--;
        }
        else{
            this.nbPionR--;
        }

    }

    public void ajoutPionGrilleRandom(){
        Pion p = new Pion(couleurPion);
        for(int n=0; n<7;n++){
            if(!this.getColonne(n).colonnePleine()){
                this.getColonne(n).ajoutPionColonne(p);
                this.colonne=n;
                if(couleurPion){
                    this.nbPionJ--;
                }
                else{
                    this.nbPionR--;
                }
                break;
            }
                
        }
    }

    /*
  
    public void ajoutPionGrille(int n){
        Pion p;
        if(this.couleurPrec){
            p = new Pion(false);
            this.couleurPrec=false;
            this.nbPionR--;
        }
        else{
            p = new Pion(true);
            this.couleurPrec=true;
            this.nbPionJ--;
        }
        this.getColonne(n).ajoutPionColonne(p);
        this.colonne=n;
        
    }
    */

    public int getNbPionR(){
        return this.nbPionR;
    }

    public int getNbPionJ(){
        return this.nbPionJ;
    }

    private Colonne getColonne(int i){
        return this.array.get(i);
    }


    public Pion getCoorPion(int x,int y){
        return this.getColonne(x).getPion(y);
    }

    public List<Colonne> getGrille(){
        return this.array;
    }

    public List<Boolean> getListeColonneRemplies(){
        List<Boolean> liste = new ArrayList<>();
        for(int i = 0; i<=6;i++){
            liste.add(this.getColonne(i).colonnePleine());
        }
        return liste;
    }

    public boolean detectionVictoireVerticale(){
        boolean res = false;
        int compteur = 0;
        int indiceLigne= this.getColonne(this.colonne).getLigne();
        for(int i=indiceLigne;i<6;i++){                            
            if(this.getCoorPion(this.colonne, i).getCouleur()==this.couleurPion && this.getCoorPion(this.colonne, i).existe()){
                compteur+=1;
                
            if(compteur>=4){
                res=true;
            }
        }
        else{
            break;
        }
     }
        return res;
    }
 
 
    public boolean detectionVictoireHorizontale(){
        boolean res = false;
        int compteur = -1;
        int indiceLigne= this.getColonne(this.colonne).getLigne();
        for(int i=this.colonne;i>=0;i--){
            if(this.getCoorPion(i, indiceLigne).getCouleur()==this.couleurPion && this.getCoorPion(i, indiceLigne).existe()){
                compteur+=1;
                
                if(compteur>=4){
                    res=true;

                }
            }
            else{
                break;
            }
            
        }
        for(int i=this.colonne;i<6;i++){
            if(this.getCoorPion(i, indiceLigne).getCouleur()==this.couleurPion && this.getCoorPion(i, indiceLigne).existe()){
                compteur+=1;
                
                if(compteur>=4){
                    res=true;

                }
            }
            else{
                break;
            }
            
        }
        return res;
    }

    public boolean detectionVictoireDiagHautGauche(){
        boolean res = false;
        int compteur = -1;
        int j = this.colonne;
        int indiceLigne= this.getColonne(this.colonne).getLigne();
        for(int i=indiceLigne;i>=0;i--){
            if(j<0){
                break;
            }
            if(this.getCoorPion(j, i).getCouleur()==this.couleurPion && this.getCoorPion(j, i).existe()){
                compteur+=1;
                
                if(compteur>=4){
                    res=true;

                }
            }
            else{
                break;
            }
            j--;
        }
        j = this.colonne;
        for(int i=indiceLigne;i<6;i++){
            if(j>6){
                break;
            }
            if(this.getCoorPion(j, i).getCouleur()==this.couleurPion && this.getCoorPion(j, i).existe()){
                compteur+=1;
                
                if(compteur>=4){
                    res=true;

                }
            }
            else{
                break;
            }
            j++;
        }
        return res;

    }

    public boolean detectionVictoireDiagBasGauche(){
        boolean res = false;
        int compteur = -1;
        int j = this.colonne;
        int indiceLigne= this.getColonne(this.colonne).getLigne();
        for(int i=indiceLigne;i<6;i++){
            if(j<0){
                break;
            }
            if(this.getCoorPion(j, i).getCouleur()==this.couleurPion && this.getCoorPion(j, i).existe()){
                compteur+=1;
                
                if(compteur>=4){
                    
                    res=true;

                }
            }
            else{
                break;
            }
            j--;
        }
        j = this.colonne;
        for(int i=indiceLigne;i>0;i--){
            if(j>6){
                break;
            }
            if(this.getCoorPion(j, i).getCouleur()==this.couleurPion && this.getCoorPion(j, i).existe()){
                compteur+=1;
                
                if(compteur>=4){
                    res=true;

                }
            }
            else{
                break;
            }
            j++;
        }
        return res;

    }
    
    public boolean getVictoireGlobal(){
        boolean res = false;
        if(this.detectionVictoireVerticale()){
            res=true;
        }
        if(this.detectionVictoireHorizontale()){
            res=true;
        }      
        if(this.detectionVictoireDiagHautGauche()){
            res=true;
        }
        if(this.detectionVictoireDiagBasGauche()){
            res=true;
        }
        return res;
    }
    
    
    public String toJson(){
        StringBuilder res = new StringBuilder("[");
        for(Colonne c: this.array){
            res.append("[");
            for(Pion p: c.getListePion()){
                if(p.existe()){
                    res.append(p.getVraiCouleur()).append(",");
                }
                else{
                    res.append("v,");
                }
            }
            res = new StringBuilder(res.substring(0, res.length() - 1));
            res.append("],");
        }
        res = new StringBuilder(res.substring(0, res.length() - 1));
        res.append("]");
        return res.toString();
    }

    public void updateGrille(String json){
        ArrayList<ArrayList<String>> array = this.toArray(json);
        for(int i=0;i<7;i++){
            for(int j=0;j<6;j++){
                if(array.get(i).get(j).equals("v")){
                    this.getCoorPion(i, j).setExiste(false);;
                }
                else{
                    if(!this.getCoorPion(i, j).existe()){
                        this.getCoorPion(i, j).setExiste(true);
                        this.getCoorPion(i, j).setCouleur(array.get(i).get(j));
                    }
                }
            }
        }
    }

    public ArrayList<ArrayList<String>> toArray(String json){
        ArrayList<ArrayList<String>> res = new ArrayList<>();
        boolean start = false;
        int numArray = 0;
        for(int i = 1; i<json.length()-1; i++){
            if(json.charAt(i) == '['){
                start = true;
                res.add(new ArrayList<>());
            } else if(json.charAt(i) == ']'){
                start = false;
                numArray += 1;
            }
            if(start && json.charAt(i) != ',' && json.charAt(i) != ']' && json.charAt(i) != '['){
                res.get(numArray).add(String.valueOf(json.charAt(i)));
            }
        }
        return res;
    }

    @Override
    public String toString(){
        StringBuilder res = new StringBuilder();
        Pion p;

        for(int n=0;n<6;n++){
            for(int k=0;k<7;k++){
                p=this.getColonne(k).getPion(n);
                res.append(p.toString());
                res.append(" ");
            }
            res.append(Newligne);
        }
        return res.toString();
    }
}