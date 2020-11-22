package puissance4;
public class Pion{

    private boolean estJaune;
    private boolean existe;

    public Pion( boolean estJaune){  // c'est un vrai pion 
        this.estJaune = estJaune;
        this.existe=true;
    }

    public Pion(){  //c'est un pion vide ce qui représente une case vide
        this.existe=false;
        this.estJaune=true;
    }

    public boolean getCouleur(){
        if (this.existe){
            return this.estJaune;
        }
        else{
            return false;
        }
    }

    public void setCouleur(String couleur){
        if(couleur.equals("v")){
            this.existe=false;
            this.estJaune=true;
        }
        else{
            if(couleur.equals("r")){
                this.existe=true;
                this.estJaune=false;
            }
            else{
                this.existe=true;
                this.estJaune=true;
            }
        }
    }

    public void setExiste(boolean exi){
        this.existe=exi;
    }

    public String getVraiCouleur(){
        if(this.existe){
            if (this.estJaune){
                return "j";
            }
            else{
                return "r";
            }
        }
        else{
            return "v";
        }
    }

    public boolean existe(){
        return this.existe;
    }

    @Override
    public String toString(){
        if (!this.existe){
            return "v";
        }
        else{
            return this.getVraiCouleur();
        }
    } 
    
}