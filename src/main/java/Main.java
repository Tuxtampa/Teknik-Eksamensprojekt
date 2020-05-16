import processing.core.PApplet;
import processing.core.PImage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class Main extends PApplet {
    public static void main(String[] args) {
        Main.main("Main");
    }
    ArrayList<Unit> units = new ArrayList<Unit>();
    ArrayList<Unit> unitsTeam1 = new ArrayList<Unit>();
    ArrayList<Unit> unitsTeam2 = new ArrayList<Unit>();
    ArrayList<Unit> unitsTeam1InPlay = new ArrayList<Unit>();
    ArrayList<Unit> unitsTeam2InPlay = new ArrayList<Unit>();
    public HashMap<String,String> tiles = new HashMap<>();
    Board bd = new Board();
    int millisPerTick = 1000;
    int lastTick = 0;
    float xTiles = 8;
    float yTiles = 8;
    PImage unitImage = null;

    public void draw(){
        //background(200);
        bd.Boardd();
        tileLines();
        //if((millis() - lastTick) > millisPerTick)tick();
    }

    private void tileLines() {
        for(int i = 1; i < xTiles; i++) {
            line(width / xTiles*i, 0, width / xTiles*i, height);
            line(0, 0+(height/yTiles*i), width, 0+(height/yTiles*i));
        }
    }

    public void tick(){
        System.out.println("tick" + frameCount%(10*frameRate) + " There are " + (unitsTeam1.size()+unitsTeam2.size()) + " units at ");
        Action(unitsTeam1InPlay);
        Action(unitsTeam2InPlay);
        lastTick = millis();
        unitsTeam1InPlay.removeIf(currentUnit -> currentUnit.currentHp < 0.1);
        unitsTeam2InPlay.removeIf(currentUnit -> currentUnit.currentHp < 0.1);
        drawUnits();
    }

    private void Action(ArrayList<Unit> unitsTeam2InPlay) {
        for(Unit currentUnit : unitsTeam2InPlay){
            if(currentUnit.currentHp < 0) unitsTeam2InPlay.remove(currentUnit);
            if(currentUnit.ticksToNextMove < 1) {
                Unit target = getNearbyUnits(currentUnit);
                if (target != null) {
                    System.out.println("move");
                    currentUnit.nextMove(target, tiles);
                } else {
                    System.out.println("Target is null dummy");
                }
            }
        }
    }

    /*
    public Unit getNearbyUnits(Unit currentunit){
        try {
            for (int i = 0; i < 100; i++) {
                Unit tempUnit = unitsTeam1InPlay.get((int) Math.floor(Math.random() * unitsTeam1InPlay.size()));
                if (tempUnit.posX != currentunit.posX || tempUnit.posY != currentunit.posY) {
                    if (tempUnit.team != currentunit.team) return tempUnit;
                }
                Unit tempUnit2 = unitsTeam2InPlay.get((int) Math.floor(Math.random() * unitsTeam2InPlay.size()));
                if (tempUnit2.posX != currentunit.posX || tempUnit2.posY != currentunit.posY) {
                    if (tempUnit2.team != currentunit.team) return tempUnit2;
                }
            }
            return null;
        } catch (IndexOutOfBoundsException ie){
            return null;
        }
    }
     */

    public Unit getNearbyUnits(Unit currentunit){
        Unit returnUnit = null;
        try {
            double closestDistance = 100;
            int endX;
            int endY;
                for(int i = -8; i < 8; i++){
                    for(int k = -8; k < 8; k++){
                        //System.out.println("the position currently considered is: " + (currentunit.posX-1+k) + "," + (currentunit.posY-1+i));
                        if(tiles.get((currentunit.posX-1+k) + "," + (currentunit.posY-1+i)).equals("1")){
                            double tempDist = Math.sqrt(Math.pow(k-1, 2) + Math.pow(i-1,2));
                            if(tempDist < closestDistance){
                                endX = (currentunit.posX-1+k);
                                endY = (currentunit.posY-1+i);
                                returnUnit = findUnit(endX,endY, (int) Math.signum(currentunit.team-1));
                            }
                        }
                    }
                }
        } catch (IndexOutOfBoundsException ie){
            System.out.println("REEEEEEEEEEEEEEEEEE");
            return null;
        }
        return returnUnit;
    }

    public Unit findUnit(int posX, int posY, int desiredTeam){
        Unit returnUnit = null;
        System.out.print("Desiredteam is " + desiredTeam);
        if (desiredTeam == 1) {
            for (Unit unit : unitsTeam1InPlay) {
                if (unit.posY == posY && unit.posX == posX) {
                    returnUnit = unit;
                    System.out.print("returnunit is not null");
                } else {
                    System.out.println("Apparently " + unit.posX + "," + unit.posY + " is not the same as " + posX + "," + posY + " for de");
                }
            }
        }
        if (desiredTeam == -1) {
            for (Unit unit : unitsTeam2InPlay) {
                if (unit.posY == posY && unit.posX == posX) {
                    returnUnit = unit;
                    System.out.print("returnunit is not null");
                } else {
                    System.out.println("Apparently " + unit.posX + "," + unit.posY + " is not the same as " + posX + "," + posY);
                }
            }
        }
        if(returnUnit == null){
            System.out.println("Why even bother returning mate" + posX + "," + posY + " for desired team " + desiredTeam);
        }
        return returnUnit;
    }

    public void settings() {
        size(600,600);
    }

    public void setup(){
        loadImages();
        fillTileBoundaries();
        fillTilesCenter();
        System.out.println("Setup ran");
    }

    public void drawUnits(){
        background(125);
        tileLines();
        for(Unit currentUnit : unitsTeam1InPlay){
            image(unitImage,currentUnit.posX*(width/xTiles),currentUnit.posY*(height/yTiles));
        }
        for(Unit currentUnit : unitsTeam2InPlay){
            image(unitImage,currentUnit.posX*(width/xTiles),currentUnit.posY*(height/yTiles));
        }
    }

    public void keyPressed(){
        if(key == 't')tick();
        if(key == 'l'){
            loadUnits();
            drawUnits();
        }
        if(key == '1')createUnit(1);
        if(key == '2')createUnit(2);
    }

    public void loadImages(){
        unitImage = loadImage("Images\\placeholder.PNG");
    }

    public void createUnit(int team) {
        for (int i = 0; i < 1; i++) {
            Unit unit2 = new Unit();
            unit2.UnitGeneric();
            if(team == 1)unitsTeam1.add(unit2);
            if(team == 2)unitsTeam2.add(unit2);
        }
        System.out.print("Unit created for team " + team);
    }
    public void loadUnits(){
        for(int i = 0; i < unitsTeam1.size(); i++){
            Unit currentUnit = unitsTeam1.get(i);
            currentUnit.loadUnit(i+1,0 );
            unitsTeam1InPlay.add(currentUnit);
            tiles.remove(currentUnit.posX + "," + currentUnit.posY);
            tiles.put(currentUnit.posX + "," + currentUnit.posY, "1");
        }
        for(int i = 0; i < unitsTeam2.size(); i++){
            Unit currentUnit = unitsTeam2.get(i);
            currentUnit.loadUnit(i+1,7 );
            unitsTeam2InPlay.add(currentUnit);
            tiles.remove(currentUnit.posX + "," + currentUnit.posY);
            tiles.put(currentUnit.posX + "," + currentUnit.posY, "2");
        }
    }

    public void fillTilesCenter(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                tiles.remove(j + "," + i);
                tiles.put(j + "," + i, "0");
            }
        }
    }
    public void fillTileBoundaries(){
        for(int i = -20; i < 22; i++){
            for(int j = -20; j < 22; j++){
                tiles.put(j + "," + i, "3");
            }
        }
    }

    public Unit unitFromParent(Unit parent){
        Unit newUnit = new Unit();
        newUnit.UnitWeights(parent.geneticAlgorithm(parent.statWeights));
        return newUnit;
    }
}
