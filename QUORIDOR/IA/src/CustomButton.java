package src;
import javax.swing.*;

public class CustomButton extends JButton {
    private String coords;
    private String contenuCase;

    public CustomButton(String text, String coords, String contenuCase) {
        super(text);
        this.coords = coords;
        this.contenuCase = contenuCase;
    }

    public String getCoords() {
        return this.coords;
    }

    public String getContenuCase() {
        return this.contenuCase;
    }

    public void setContenuCase(String contenuCase) {
        this.contenuCase = contenuCase;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

}