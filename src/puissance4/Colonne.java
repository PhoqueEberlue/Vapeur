package puissance4;

import java.util.ArrayList;
import java.util.List;

public class Colonne {

    private List<Pion> colonne;
    private int ligne;

    
    public Colonne(){
        this.colonne=new ArrayList<>();
        for(int j=0;j<6;j++){
            Pion p = new Pion();
            this.colonne.add(p);
        }
        this.ligne=ligne;
    }

    public Pion getPion(int i){
        return this.colonne.get(i);
    }

    public void ajoutPionColonne(Pion p){
        for(int i=5;i>=0;i--){
            if(!this.getPion(i).existe()){
                this.colonne.set(i,p);
                this.ligne=i;
                break;
            }           
        }     
    }


    public List<Pion> getListePion(){
        return this.colonne;
    }

    public int getLigne(){
        return this.ligne;
    }

    public boolean colonnePleine(){
        if(!this.getPion(0).existe()){
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public String toString(){
        return this.colonne+"";
    }
    
}