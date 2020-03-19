import processing.core.PApplet;

public class Main extends PApplet {
    public static void main(String[] args) {
        Main.main("Main");
    }

    Board bd = new Board();

    public void draw(){
        background(200);
        bd.Boardd();
    }

    public void settings() {
        size(600,600);

    }

}
