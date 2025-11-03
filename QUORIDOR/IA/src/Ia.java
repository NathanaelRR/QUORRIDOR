package src; 
import java.util.ArrayList;
public class Ia extends Joueur{
    public Ia(String name, int x, int y){
        super(name, x, y );
    }

    public boolean murLegal (String depart, Grille grille, String centreMur, String direction) {
        Joueur fauxJoueur1 = new Joueur("faux", 0, 0);
        Joueur fauxJoueur2 = new Joueur("faux", grille.getSize()-1, grille.getSize()-1);
        Grille copieGrille = new Grille(grille.getSize(), fauxJoueur1, fauxJoueur2);
        for(int i = 0; i < grille.getSize(); i++){ //pour chaque colonne
            for(int j = 0; j < grille.getSize(); j++){ //pour chaque ligne
                copieGrille.getGrille()[i][j] = grille.getGrille()[i][j];
            }
        }
        this.placerFauxMur(copieGrille, centreMur, direction);

        int nbCasesAVisiter = (((copieGrille.getSize()+1)/2) * ((copieGrille.getSize()+1)/2));
        String caseDepart = depart;
        ArrayList<String> casesVu = new ArrayList<String>();
        casesVu.add(caseDepart);

        ArrayList<String>[] resultatTout = casesAccessible(casesVu, copieGrille);// pour le bien de la methode cheminOpti, la methode casesAccecssible renvoie 2 ArrayList dans un tableau mais ici seul la premiere ArrayList nous interesse
       
        ArrayList<String> casesParcourues = resultatTout[0];// On recupere la liste CaseVue

        if (!(casesParcourues.size() == nbCasesAVisiter)){
            System.out.println("Vous tentez d'isoler au moins une case du reste du plateau, ce n'est pas possible, cordialement");
        }
        
        return (casesParcourues.size() == nbCasesAVisiter);
        
    }

    public ArrayList<String> cheminOpti (String depart, Grille grille, Integer but){

        String caseDepart = depart;
        //System.out.println("La case de départ est : " + caseDepart);
        ArrayList<String> caseDepartAlgo = new ArrayList<String>();
        ArrayList<String> chemin = new ArrayList<String>();

        caseDepartAlgo.add(caseDepart);
        //System.out.println("La liste caseDepartAlgo est : " + caseDepartAlgo);

        
        ArrayList<String>[] resultatTout = casesAccessible(caseDepartAlgo, grille);
        //System.out.println("La liste des cases accessible depuis la position du joueur est : " + resultatTout[0]);
        ArrayList<String> casesVu = resultatTout[0];
        //System.out.println("les cases vues sont "+ casesVu);
        ArrayList<String> casesPere = resultatTout[1];
        //System.out.println("les cases peres sont "+ casesPere);

        for (int i = 0; i < casesVu.size(); i++){
            Integer[] caseCheck = new Integer[2];
            caseCheck = coordoneesCases(casesVu.get(i));
            //System.out.println("casCheck :"+ caseCheck[0] + ":" + caseCheck[1]);
            if(caseCheck[1]==but){
                chemin.add(casesVu.get(i));
                chemin.add(casesPere.get(i));
                String pere = casesPere.get(i);
                while (!(chemin.get(chemin.size()-1).equals(caseDepart))) {
                    for (int j = 0; j < casesVu.size(); j++){
                        if(casesVu.get(j).equals(pere)){
                            chemin.add(casesPere.get(j));
                            pere=casesPere.get(j);
                        }
                    }
                }
                break;
            }
        }
        return chemin;
    }
            

        

    public ArrayList<String>[] casesAccessible (ArrayList<String> casesVu, Grille grille){

        ArrayList<String> casesPere = new ArrayList<>();
        casesPere.add(casesVu.get(0));

        for(int i = 0; i < casesVu.size(); i++){

            String caseCourante = casesVu.get(i);
            String caseGauche = vgauche(caseCourante, grille);
            String caseHaut = vhaut(caseCourante, grille);
            String caseDroite = vdroite(caseCourante, grille);
            String caseBas = vbas(caseCourante, grille);

            if(!caseGauche.equals("")){
                if(!casesVu.contains(caseGauche)){
                    casesVu.add(caseGauche);
                    casesPere.add(caseCourante);
                }
            }
            if(!caseHaut.equals("")){
                if(!casesVu.contains(caseHaut)){
                    casesVu.add(caseHaut);
                    casesPere.add(caseCourante);
                }
            }
            if(!caseDroite.equals("")){
                if(!casesVu.contains(caseDroite)){
                    casesVu.add(caseDroite);
                    casesPere.add(caseCourante);
                }
            }
            if(!caseBas.equals("")){
                if(!casesVu.contains(caseBas)){
                    casesVu.add(caseBas);
                    casesPere.add(caseCourante);
                }
            }
        }

        ArrayList<String>[] resultat = new ArrayList[2];
        resultat[0]= casesVu;
        resultat[1]= casesPere;

        //System.out.println(resultat[0]);
        //System.out.println(resultat[1]);
        return resultat;
        

    }

    public Integer[] coordoneesCases(String coords){
        String[] coordonnees = coords.split(":");
        int x = Integer.parseInt(coordonnees[0]);
        int y = Integer.parseInt(coordonnees[1]);
        Integer[] tab = new Integer[2];
        tab[0] = x;
        tab[1] = y;
        return tab;
    }

    public String vgauche (String coords, Grille grille){
        String[] coordonnees = coords.split(":");
        int x = Integer.parseInt(coordonnees[0]);
        int y = Integer.parseInt(coordonnees[1]);
        String caseGauche = (x-2) + ":" +y;
        if(x==0){
            return "";
        }
        if (verifierMur(coords, caseGauche, grille)) {
            return caseGauche;
        }
        else {
            return "";
        }
    }

    public String vhaut (String coords, Grille grille){
        String[] coordonnees = coords.split(":");
        int x = Integer.parseInt(coordonnees[0]);
        int y = Integer.parseInt(coordonnees[1]);
        String caseHaut = x + ":" + (y-2);
        if(y==0){
            return "";
        }
        if (verifierMur(coords, caseHaut, grille)) {
            return caseHaut;
        }
        else {
            return "";
        }
    }
    
    public String vdroite (String coords, Grille grille){
        String[] coordonnees = coords.split(":");
        int x = Integer.parseInt(coordonnees[0]);
        int y = Integer.parseInt(coordonnees[1]);
        String caseDroite = (x+2) +":"+ y;
        if (x==grille.getSize()-1) {
            return "";
        }
        if (verifierMur(coords, caseDroite, grille)) {
            return caseDroite;
        }
        else {
            return "";
        }
    }

    public String vbas (String coords, Grille grille){
        String[] coordonnees = coords.split(":");
        int x = Integer.parseInt(coordonnees[0]);
        int y = Integer.parseInt(coordonnees[1]);
        String caseBas = x +":"+ (y+2);
        if (y==grille.getSize()-1) {
            return "";
        }
        if (verifierMur(coords, caseBas, grille)) {
            return caseBas;
        }
        else {
            return "";
        }
        
    }

    public double mean(int[] m) {
        double sum = 0;
        for (int i = 0; i < m.length; i++) {
            sum += m[i];
        }
        return sum / m.length;
    }
    
    public boolean verifierMur(String case1 , String case2, Grille grille){
        Integer[] c1 = coordoneesCases(case1);
        Integer[] c2 = coordoneesCases(case2);

        int[] meantab1 = new int[2];
        meantab1[0] = c1[0];
        meantab1[1] = c2[0];
        double mean1 = mean(meantab1);
        int indicex = (int)mean1;

        int[] meantab2 = new int[2];
        meantab1[0] = c1[1];
        meantab1[1] = c2[1];
        double mean2 = mean(meantab1);
        int indicey = (int)mean2;

        if(grille.getGrille()[indicex][indicey] != null){
            if(grille.getGrille()[indicex][indicey].equals(" X")){
                return false;
            }
        }
        return true;
    }
    public void placerFauxMur(Grille grille, String coords, String direction){
        String[] coordonnees = coords.split(":");
        int x = Integer.parseInt(coordonnees[0]);
        int y = Integer.parseInt(coordonnees[1]);
        if (direction.equals("v")){
            grille.getGrille()[x][y] = " X";
            grille.getGrille()[x][y+1] = " X";
            grille.getGrille()[x][y-1] = " X";
        } else if (direction.equals("h")){
            grille.getGrille()[x][y] = " X";
            grille.getGrille()[x+1][y] = " X";
            grille.getGrille()[x-1][y] = " X";
        }
    }
}