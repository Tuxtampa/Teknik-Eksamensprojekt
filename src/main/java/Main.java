import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class Main extends PApplet {
    public static void main(String[] args) {
        Main.main("Main");
    }
    ArrayList<Unit> units = new ArrayList<Unit>();
    Board bd = new Board();
    int millisPerTick = 1000;
    int lastTick = 0;
    PImage unitImage = null;

    public void draw(){
        //background(200);
        bd.Boardd();
        if((millis() - lastTick) > millisPerTick)tick();
    }

    public void tick(){
        System.out.println("tick" + frameCount%(10*frameRate) + " There are " + units.size() + " units");
        for(Unit currentUnit : units){
            //Unit target = getNearbyUnits(currentUnit.posX, currentUnit.posY);
            //int[] move = currentUnit.nextMove(target);
            //bd.executeMove(move);
        }
        lastTick = millis();
        drawUnits();
    }

    public void settings() {
        size(600,600);
    }

    public void setup(){
        loadImages();
        createUnit();
        loadUnits();
        System.out.println("Setup ran");
    }

    public void drawUnits(){
        for(Unit currentUnit : units){
            image(unitImage,currentUnit.posX,currentUnit.posY);
            System.out.println("units drawn");
        }
    }

    public void loadImages(){
        unitImage = loadImage("Images\\placeholder.PNG");
    }

    public void createUnit() {
        for (int i = 0; i < 5; i++) {
            Unit unit2 = new Unit();
            unit2.UnitGeneric();
            units.add(unit2);
        }
    }
    public void loadUnits(){
        for(int i = 0; i < units.size(); i++){
            Unit currentUnit = units.get(i);
            currentUnit.loadUnit(100,100*i);
        }
    }
}
