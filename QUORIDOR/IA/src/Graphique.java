package src;
import javax.swing.*;


import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.lang.reflect.Array;



public class Graphique extends JFrame {
    private Ia2 iaFunct = new Ia2("non joueur", -1, -1, 0);
    private boolean choixMur = false;
    private int[] centreMurChoisi = new int[2];
    private Jeu jeu;
    private Grille grille;
    private Joueur joueur1;
    private Joueur joueur2;
    private JPanel gridPanel;
    private JPanel mainMenuPanel;
    private JLabel playerLabel;
    private JTextField centerWallTextField;
    private ButtonGroup actionButtonGroup;
    private ButtonGroup orientationButtonGroup;
    private JPanel controlPanel;
    private JPanel directionPanel;
    private JButton hautButton;
    private JButton basButton;
    private JButton gaucheButton;
    private JButton droiteButton;
    private JButton retourButton;
    private JButton validationButton;
    private JButton quitButton;
    private ArrayList<CustomButton> buttonCells;
    private JLabel labelAction;

    public Graphique() {
        

        this.mainMenuPanel = new JPanel(new BorderLayout());
        JPanel centreMenu = new JPanel(new GridLayout(1, 3));
        
        mainMenuPanel.add(centreMenu, BorderLayout.CENTER);
        JButton startButton = new JButton("Jouer avec un autre joueur");
        JButton startButton2 = new JButton("Jouer avec une IA");
        JButton quitButton2 = new JButton("Quitter");
        centreMenu.add(startButton);
        centreMenu.add(startButton2);
        centreMenu.add(quitButton2);
        playerLabel = new JLabel("Joueur courant: J1");
        playerLabel.setHorizontalAlignment(JLabel.CENTER);
        labelAction = new JLabel("Choisissez une action");
        labelAction.setHorizontalAlignment(JLabel.CENTER);
        labelAction.setFont(labelAction.getFont().deriveFont(20f));
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        gridPanel = new JPanel(new GridLayout(17, 17));
        //playerLabel = new JLabel("Joueur courant: ");
        centerWallTextField = new JTextField("i:j");
// Boutons pour l'orientation d'un placement de mur
        JRadioButton verticalButton = new JRadioButton("Vertical");
        JRadioButton horizontalButton = new JRadioButton("Horizontal");
         // Les actions possibles dans les choix "bouger" et "placer mur" 
        directionPanel = new JPanel();
        directionPanel.setLayout(new BoxLayout(directionPanel, BoxLayout.Y_AXIS));
        validationButton = new JButton("Valider");
        retourButton = new JButton("Retour");
        hautButton = new JButton("Haut");
        basButton = new JButton("Bas");
        gaucheButton = new JButton("Gauche");
        droiteButton = new JButton("Droite");
        directionPanel.add(hautButton);
        directionPanel.add(basButton);
        directionPanel.add(gaucheButton);
        directionPanel.add(droiteButton);
        directionPanel.add(centerWallTextField);
        directionPanel.add(verticalButton);
        directionPanel.add(horizontalButton);
        directionPanel.add(retourButton);  
        directionPanel.add(validationButton);  

        //Iniatiliser l'interface sans les boutons inutiles à ce stade
        directionPanel.setVisible(false);
        retourButton.setVisible(false);
        // Boutons des choix d'actions
        JButton moveButton = new JButton("Bouger le pion");
        JButton wallButton = new JButton("Placer un mur");
        actionButtonGroup = new ButtonGroup();
        actionButtonGroup.add(moveButton);
        actionButtonGroup.add(wallButton);

        
        orientationButtonGroup = new ButtonGroup();
        orientationButtonGroup.add(verticalButton);
        orientationButtonGroup.add(horizontalButton);


        // Les éléments d'actions qu'on peut cliquer à l'ouverture de l'interface
        controlPanel = new JPanel();
        quitButton = new JButton("Quitter");
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.add(new JLabel("Actions:"));
        controlPanel.add(moveButton);
        controlPanel.add(wallButton);
        controlPanel.add(quitButton);
        

        // Panel prinicipal qui contient tout ce qui est visible
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(playerLabel, BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.WEST);
        mainPanel.add(labelAction, BorderLayout.SOUTH);
        mainPanel.add(gridPanel, BorderLayout.CENTER);





        
        

       
        


        
        


       

        //Evenement sur le bouton "jouer avec un autre joueur"
        startButton.addActionListener(e -> {
            mainMenuPanel.setVisible(false);
            getContentPane().add(mainPanel, BorderLayout.CENTER);
            this.setSize(1600, 900);
            controlPanel.setVisible(true);
            directionPanel.setVisible(false);
            hautButton.setVisible(true);
            basButton.setVisible(true);
            gaucheButton.setVisible(true);
            droiteButton.setVisible(true);
            validationButton.setVisible(false);
            retourButton.setVisible(false);
            Joueur J1 = new Joueur("J1", 8, 0);
            this.joueur1 = J1;
            Joueur J2 = new Joueur("J2", 8, 16);
            this.joueur2 = J2;
            Grille grilleDeJeu = new Grille(17, J1, J2);
            this.grille = grilleDeJeu;
            creerGrille();
            revalidate();
            repaint();
        });


        //Evenement sur le bouton "jouer avec une IA"
        startButton2.addActionListener(e -> {
            mainMenuPanel.setVisible(false);
            getContentPane().add(mainPanel, BorderLayout.CENTER);
            this.setSize(1600, 900);
            controlPanel.setVisible(true);
            directionPanel.setVisible(false);
            hautButton.setVisible(true);
            basButton.setVisible(true);
            gaucheButton.setVisible(true);
            droiteButton.setVisible(true);
            validationButton.setVisible(false);
            retourButton.setVisible(false);
            Joueur J1 = new Joueur("J1", 8, 0);
            this.joueur1 = J1;
            Ia2 J2 = new Ia2("J2", 8, 16, 0);
            this.joueur2 = J2;
            Grille grilleDeJeu = new Grille(17, J1, J2);
            this.grille = grilleDeJeu;
            creerGrille();
            revalidate();
            repaint();
        });

        //evenement sur le bouton quitter du menu prinicpal
        quitButton.addActionListener(e -> {
            dispose();
        });

        //Evenement sur le radioButton "bouger un pion" 
        moveButton.addActionListener(e -> {
            resetColorAction();
            labelAction.setText("Choisissez une case verte en cliquant dessus pour vous déplacer");
            ArrayList<Node> casesAccessibles = grille.casesAccessibles(grille.getJoueurCourant());
            for (Node node : casesAccessibles) {
                String coords = node.y + ":" + node.x;
                for (Component component : gridPanel.getComponents()) {
                    if (component instanceof CustomButton) {
                        CustomButton button = (CustomButton) component;
                        
                        if (button.getCoords().equals(coords)) {
                            button.setEnabled(true);
                            button.setBorderPainted(true);
                            button.setBackground(Color.green);
                            
                        }
                    }
                }
            }
            revalidate();
            repaint();
            
        });
        

      
        //Evenement sur le radioButton "placer un mur" 
        wallButton.addActionListener(e -> {
            labelAction.setText("Choisissez une case verte en cliquant dessus pour choisir le centre du mur");
            resetColorAction();
            ArrayList<Node> mursDispo = grille.mursDispos(grille.getJoueurCourant());
            for (Node node : mursDispo) {
                String coords = node.y + ":" + node.x;
                for (Component component : gridPanel.getComponents()) {
                    if (component instanceof CustomButton) {
                        CustomButton button = (CustomButton) component;
                        
                        if (button.getCoords().equals(coords)) {
                            button.setEnabled(true);
                            button.setBorderPainted(true);
                            button.setBackground(Color.green);
                            
                        }
                    }
                }
            }
            revalidate();
            repaint();

        });

        //Evenement sur le  bouton "retour"
        retourButton.addActionListener(e -> {
            retourButton.setVisible(false);
            directionPanel.setVisible(false);
            hautButton.setVisible(true);
            basButton.setVisible(true);
            gaucheButton.setVisible(true);
            droiteButton.setVisible(true);
            controlPanel.setVisible(true);
            validationButton.setVisible(false);

        });



        //Evenement sur le  bouton "retour"
        validationButton.addActionListener(e -> {

        });

        //Evenement sur le bouton "quitter"
         quitButton2.addActionListener(e -> {
            System.exit(0);

        });


       

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainMenuPanel, BorderLayout.CENTER);
      

        setTitle("QUORIDOR");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 200);
        setLocationRelativeTo(null);
        setVisible(true);
        
    }

    public void choixDirectionMur(String coords){
        resetColorAction();
        String[] coordonnees = coords.split(":");
        int x = Integer.parseInt(coordonnees[0]);
        int y = Integer.parseInt(coordonnees[1]);

        choixMur = true;
        centreMurChoisi[0] = x;
        centreMurChoisi[1] = y;
        

        if (x > 0){
            for (Component component : gridPanel.getComponents()) {
                    if (component instanceof CustomButton) {
                        CustomButton button = (CustomButton) component;
                        if (button.getCoords().equals((x-1) + ":" + y)) {
                            button.setEnabled(true);
                            button.setBorderPainted(true);
                            button.setBackground(Color.green);
                        }
                    }
                }
        }
        if (y > 0){
            for (Component component : gridPanel.getComponents()) {
                    if (component instanceof CustomButton) {
                        CustomButton button = (CustomButton) component;
                        if (button.getCoords().equals(x + ":" + (y-1))) {
                            button.setEnabled(true);
                            button.setBorderPainted(true);
                            button.setBackground(Color.green);
                        }
                    }
                }
        }

        
    }







    public void affichageMur(CustomButton buttonCell){
        String contenuCase = buttonCell.getContenuCase();
        String coords = buttonCell.getCoords();
        String[] coordonnees = coords.split(":");
        int x = Integer.parseInt(coordonnees[0]);
        int y = Integer.parseInt(coordonnees[1]);
        
        int direction = 0;
        String directionMur = "";
        if (x== centreMurChoisi[0] -1){
            direction = 1;
            directionMur = "v";
        } else if (y == centreMurChoisi[1]-1){
            direction = 2;
            directionMur = "h";
        }

    

        if (iaFunct.murPossible(centreMurChoisi[1] + ":" + centreMurChoisi[0], directionMur, grille)){ 
                 
        
       
        ArrayList<Node> casesMur = new ArrayList<>();
        
        if (direction == 1){
            casesMur.add(new Node(null, centreMurChoisi[0], centreMurChoisi[1], 0, 0));
            casesMur.add(new Node(null, centreMurChoisi[0]-1, centreMurChoisi[1], 0, 0));
            casesMur.add(new Node(null, centreMurChoisi[0]+1, centreMurChoisi[1], 0, 0));
            grille.placerMur(grille.getJoueurCourant(), centreMurChoisi[1] + ":" + centreMurChoisi[0], "v");
            
        } else if (direction == 2){
            casesMur.add(new Node(null, centreMurChoisi[0], centreMurChoisi[1], 0, 0));
            casesMur.add(new Node(null, centreMurChoisi[0], centreMurChoisi[1]-1, 0, 0));
            casesMur.add(new Node(null, centreMurChoisi[0], centreMurChoisi[1]+1, 0, 0));
            grille.placerMur(grille.getJoueurCourant(), centreMurChoisi[1] + ":" + centreMurChoisi[0], "h");
            
            
        } 
        
        
        for (Component component : gridPanel.getComponents()) {
                if (component instanceof CustomButton) {
                    CustomButton button = (CustomButton) component;
                    for (Node node : casesMur) {
                       
                        String coordButton = button.getCoords();
                        String[] coords2 = coordButton.split(":");
                        int x2 = Integer.parseInt(coords2[0]);
                        int y2 = Integer.parseInt(coords2[1]);
                        if ((y2 + ":" + x2).equals(node.y + ":" + node.x)) {
                            
                            button.setContenuCase("mur");
                            button.setEnabled(false);
                            button.setBorderPainted(false);
                            button.setBackground(Color.BLACK);
                        }
                    }
                    
            }
        }

        } else {
            JOptionPane.showMessageDialog(null, "Impossible de placer un mur ici, vous allez bloquer un joueur ! essayez ailleurs");
        }
        resetColorAction();
        gridPanel.repaint();
        gridPanel.revalidate();

        
}







    public void affichageDeplacement(CustomButton buttonCell){
        resetColorAction();
        String coords = buttonCell.getCoords();
        String contenuCase = buttonCell.getContenuCase();
        String[] coordonnees = coords.split(":");
        int x = Integer.parseInt(coordonnees[0]);
        int y = Integer.parseInt(coordonnees[1]);
        

        
        for (Component component : gridPanel.getComponents()) {
                if (component instanceof CustomButton) {
                        
            
                    CustomButton button = (CustomButton) component;
                    if (button.getCoords().equals(coords)) {
                        
                        button.setText(grille.getJoueurCourant().getName());
                        contenuCase = null;
                    }
                    if (button.getBackground() == Color.green) {
                        button.setEnabled(false);
                        button.setBorderPainted(false);
                        button.setBackground(Color.pink);
                    }
                    
                    if (button.getCoords().equals(grille.getJoueurCourant().getY() + ":" + grille.getJoueurCourant().getX())) {
                        button.setText(null);
                    }
                    
                }
            }
        
            
        grille.movePlayerAvecCoords(y, x);
        
    }

    public void afficherDeplacementIa(int x, int y){
        for (Component component : gridPanel.getComponents()) {
                if (component instanceof CustomButton) {
                        
            
                    CustomButton button = (CustomButton) component;
                    if (button.getCoords().equals(x + ":" + y)) {
                        
                        button.setText(grille.getJoueurCourant().getName());
                        button.setContenuCase(null);
                    }
                    if (button.getBackground() == Color.green) {
                        button.setEnabled(false);
                        button.setBorderPainted(false);
                        button.setBackground(Color.pink);
                    }
                    
                    if (button.getCoords().equals(grille.getJoueurCourant().getY() + ":" + grille.getJoueurCourant().getX())) {
                        button.setText(null);
                    }
                    
                }
            }
        
            
        grille.movePlayerAvecCoords(y, x);

    }


    public void actionIa(Ia2 ia){
        Node start = new Node(null, grille.getJoueurCourant().getX(), grille.getJoueurCourant().getY(), 0, 0);
        ArrayList<Node> goal = new ArrayList<>();
        for (int i = 0; i < 17; i++){
            if (grille.getJoueurCourant().getName().equals("J1"))
                goal.add(new Node(null, i, 16, 0, 0));
            else{
                goal.add(new Node(null, i, 0, 0, 0));
            }
        }
            
            ArrayList<Node> result = ia.bestFirstSearch(grille, start, goal);
            grille.moveIA(ia, result.get(1));
            afficherDeplacementIa(result.get(1).y, result.get(1).x);

            CustomButton button = null;
            for (Component component : gridPanel.getComponents()) {
                if (component instanceof CustomButton) {
                    button = (CustomButton) component;
                    if (button.getCoords().equals(result.get(1).y + ":" + result.get(1).x)) {
                    }
                    
                }
            }
            
    }

    public void finAction(){
        labelAction.setText("Choisissez une action");
        if (grille.partieFinie()){
            int choice = JOptionPane.showConfirmDialog(null, "Partie terminée, le joueur " + grille.getJoueurCourant().getName() + " a gagné ! Voulez-vous rejouer ?", "Fin de partie", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                // Reset the game and start again
                resetGame();
            } else {
                System.exit(0);
            }
        } else {
            grille.switchPlayer();
            playerLabel.setText("Joueur courant: "+grille.getJoueurCourant().getName());
            choixMur = false;
            if (grille.getJoueurCourant().getClass() ==  Ia2.class){
                actionIa((Ia2) grille.getJoueurCourant());
                finAction();
            }
        }
    }


   
    
    private void resetGame() {
        
        joueur1.setX(8);
        joueur1.setY(0);
        joueur1.setNbMur(10);
        joueur2.setX(8);
        joueur2.setY(16);
        joueur2.setNbMur(10);
        grille = new Grille(grille.getSize(), joueur1, joueur2 );
        gridPanel.removeAll();
        buttonCells.clear();
        creerGrille();
        grille.setJoueurCourant(joueur1);

        
    }

    public void resetColorAction(){
        choixMur = false;
        for (Component component : gridPanel.getComponents()) {
            if (component instanceof CustomButton) {
                CustomButton button = (CustomButton) component;
                String[] coordonnees = button.getCoords().split(":");
                int x = Integer.parseInt(coordonnees[0]);
                int y = Integer.parseInt(coordonnees[1]);
                if (button.getBackground() == Color.green || button.getBackground() == Color.yellow) {
                    button.setEnabled(false);
                    button.setBorderPainted(false);
                    if (x % 2 == 0 && y % 2 == 0) {
                        button.setBackground(Color.pink);
                    } else if (x % 2 == 1 && y % 2 == 1) {
                        button.setText(button.getCoords()); //Pour faciliter la recherche des centres du mur
                        button.setBackground(Color.white);
                    } else {
                        button.setBackground(Color.lightGray);
                        
                    }
                } else if (button.getContenuCase() != null && button.getContenuCase().equals("mur")) {
                    button.setText(button.getContenuCase());
                    button.setBackground(Color.black);
                }
            }
        }
    }



    public void creerGrille(){
         // Création de l'aspect du plateau de jeu
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 17; j++) {
                
                CustomButton buttonCell;
                
                //les coordonnées sont inversées pour l'affichage étant donné la nature row/col des elements jswing
                if (i == joueur1.getY() && j == joueur1.getX()) {
                    buttonCell = new CustomButton("J1", i + ":" + j, "J1");
                } else if (i == joueur2.getY() && j == joueur2.getX()) {
                    buttonCell = new CustomButton("J2", i + ":" + j, "J2");
                } else {
                    buttonCell = new CustomButton("", i + ":" + j, null);
                }

                String[] coordonnees = buttonCell.getCoords().split(":");
                int y= Integer.parseInt(coordonnees[0]);
                int x = Integer.parseInt(coordonnees[1]);
                
                buttonCell.setEnabled(false); // Disable the button
                buttonCells = new ArrayList<>(); // Initialize the buttonCells ArrayList

                buttonCell.setBorderPainted(false); // Remove the border
                buttonCell.setPreferredSize(new Dimension(50, 50));
                buttonCell.setForeground(Color.BLACK);

                // Une coloration histoire de... 
                if (i % 2 == 0 && j % 2 == 0) {
                    buttonCell.setBackground(Color.pink);
                } else if (i % 2 == 1 && j % 2 == 1) {
                    buttonCell.setText(buttonCell.getCoords()); //Pour faciliter la recherche des centres du mur
                } else {
                    buttonCell.setBackground(Color.lightGray);
                }




                buttonCell.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        


                        if (x % 2 == 1 && y % 2 == 1) {
                            labelAction.setText("Choisissez une case verte en cliquant dessus pour choisir la direction du mur");
                            choixDirectionMur(buttonCell.getCoords());
                        } else if (x % 2 == 0 && y % 2 == 0){
                          
                            affichageDeplacement((CustomButton) e.getSource());
                            finAction();
                        } else {
                            affichageMur((CustomButton) e.getSource());
                            finAction();
                        }
                        
                        revalidate();
                        repaint();
                }});



                gridPanel.add(buttonCell);
                buttonCells.add(buttonCell);
            }
        
        }

    }

    public static void main(String[] args) {
    


        // Create the graphical interface
        SwingUtilities.invokeLater(() -> new Graphique());
    }
}
