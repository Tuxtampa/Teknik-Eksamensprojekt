import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;

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
        //if((millis() - lastTick) > millisPerTick)tick();
    }

    public void tick(){
        System.out.println("tick" + frameCount%(10*frameRate) + " There are " + units.size() + " units");
        for(Unit currentUnit : units){
            Unit target = getNearbyUnits(currentUnit.posX, currentUnit.posY);
            if(target != null){
                System.out.println("move");
                currentUnit.nextMove(target);
            }
        }
        lastTick = millis();
        drawUnits();
        System.out.println(units.get(0).currentHp);
    }

    public Unit getNearbyUnits(int startX, int startY){
            for(int i = 0; i < 100; i++){
                Unit tempUnit = units.get((int) Math.floor(Math.random()*units.size()));
                if(tempUnit.posX != startX || tempUnit.posY != startY){
                    return tempUnit;
                }
            }
            return null;
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
        }
    }
    public void mouseClicked(){
        units.add(unitFromParent(units.get(units.size()-1)));
        System.out.println(Arrays.toString(units.get(units.size() - 1).statWeights));
        System.out.println(units.get(units.size() - 1).range);
        System.out.println(units.get(units.size() - 1).attackSpeed);
        System.out.println(units.get(units.size() - 1).hp);
        System.out.println(units.get(units.size() - 1).damage);
    }
    public void keyPressed(){
        if(key == 't')tick();
    }

    public void loadImages(){
        unitImage = loadImage("Images\\placeholder.PNG");
    }

    public void createUnit() {
        for (int i = 0; i < 1; i++) {
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
    public Unit unitFromParent(Unit parent){
        Unit newUnit = new Unit();
        newUnit.UnitWeights(parent.geneticAlgorithm(parent.statWeights));
        return newUnit;
    }
}
