import processing.core.PApplet;
import processing.core.PImage;
import processing.sound.SoundFile;

import java.util.ArrayList;
import java.util.HashMap;

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
    float xTiles = 10;
    float yTiles = 6;
    float xTileSpace = 1000;
    int yTileSpace = 600;
    PImage unitImage = null;
    SoundFile sound1;

    public void draw(){
        //background(200);
        bd.Board();
        tileLines();
        //if((millis() - lastTick) > millisPerTick)tick();
    }

    private void tileLines() {
        translate((width-xTileSpace)/2f, (height-yTileSpace)/2f );
        for(int i = 0; i < xTiles; i++) {
            line(xTileSpace / xTiles*i, 0, xTileSpace / xTiles*i, yTileSpace);
            line(0, 0+(yTileSpace/yTiles*i), xTileSpace, 0+(yTileSpace/yTiles*i));
        }
        line(xTileSpace,0,xTileSpace,yTileSpace);
        translate(-(width-xTileSpace)/2f,-(height-yTileSpace)/2f);
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
            //if(currentUnit.currentHp < 0) unitsTeam2InPlay.remove(currentUnit);
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

    public Unit getNearbyUnits(Unit currentunit){
        Unit returnUnit = null;
        try {
            double closestDistance = 100;
            int endX;
            int endY;
                for(int i = -12; i < 12; i++){
                    for(int k = -12; k < 12; k++){
                        //System.out.println("Checking if " + tiles.get((currentunit.posX-1+k) + "," + (currentunit.posY-1+i)) + " is the same as " + (3-(Integer.signum(currentunit.team)+1)) );
                        if(tiles.get((currentunit.posX-1+k) + "," + (currentunit.posY-1+i)).equals(String.valueOf(3-(Integer.signum(currentunit.team)+1)))){
                            double tempDist = Math.sqrt(Math.pow(k-1, 2) + Math.pow(i-1,2));
                            System.out.println((currentunit.posX-1+k) + "," + (currentunit.posY-1+i) +" is " + tempDist + " away");
                            if(tempDist < closestDistance){
                                endX = (currentunit.posX-1+k);
                                System.out.println("endX and hopefully also endY changed");
                                endY = (currentunit.posY-1+i);
                                System.out.println("Unit " + currentunit.posX + "," + currentunit.posY + " Is looking for a unit in " + endX + "," + endY);
                                closestDistance = tempDist;
                                returnUnit = findUnit(endX,endY, (int) Math.signum(currentunit.team-1));
                            }
                        }
                    }
                }
        } catch (IndexOutOfBoundsException ie){
            ie.printStackTrace();
            System.out.println("REEEEEEEEEEEEEEEEEE");
            return null;
        }
        return returnUnit;
    }

    public Unit findUnit(int posX, int posY, int desiredTeam){
        Unit returnUnit = null;
        System.out.println("Desiredteam is " + desiredTeam);
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
        fullScreen();
    }

    public void setup(){
        //loadImages();
        fillTileBoundaries();
        fillTilesCenter();
        loadSoundFiles();
        System.out.println("Setup ran");
    }

    private void loadSoundFiles() {
        SoundFile sound1 = new SoundFile(this,"testSound.mp3");
        sound1.play();
    }

    public void drawUnits(){
        background(125);
        tileLines();
        for(Unit currentUnit : unitsTeam1InPlay){
            image(loadImage2(currentUnit.faction + currentUnit.type + currentUnit.facing + currentUnit.level),currentUnit.posX*(xTileSpace/xTiles)+((width-xTileSpace)/2f),currentUnit.posY*(yTileSpace/yTiles)+((height-yTileSpace)/2f));
        }
        for(Unit currentUnit : unitsTeam2InPlay){
            image(loadImage2(currentUnit.faction + currentUnit.type + currentUnit.facing + currentUnit.level),currentUnit.posX*(xTileSpace/xTiles)+((width-xTileSpace)/2f),currentUnit.posY*(yTileSpace/yTiles)+((height-yTileSpace)/2f));
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
        if(key == '3'){
            for(Unit unit : unitsTeam1){
                unitFromParent(unit);
            }
            for(Unit unit : unitsTeam2){
                unitFromParent(unit);
            }
        }
        if(key == '4'){
            sound1.play();
        }
    }


    public void createUnit(int team) {
        for (int i = 0; i < 1; i++) {
            Unit unit2 = new Unit();
            if(team == 1){
                unit2.UnitFull("tree", "dude", "left");
                unitsTeam1.add(unit2);
            }
            if(team == 2){
                unit2.UnitFull("tree", "dude", "right");
                unitsTeam2.add(unit2);
            }
        }
        System.out.print("Unit created for team " + team);
    }
    public void loadUnits(){
        fillTileBoundaries();
        fillTilesCenter();
        for(int i = 0; i < unitsTeam1.size(); i++){
            Unit currentUnit = unitsTeam1.get(i);
            currentUnit.loadUnit(i+1,0 );
            unitsTeam1InPlay.add(currentUnit);
            tiles.remove(currentUnit.posX + "," + currentUnit.posY);
            tiles.put(currentUnit.posX + "," + currentUnit.posY, "1");
        }
        for(int i = 0; i < unitsTeam2.size(); i++){
            Unit currentUnit = unitsTeam2.get(i);
            currentUnit.loadUnit(i+1,9 );
            unitsTeam2InPlay.add(currentUnit);
            tiles.remove(currentUnit.posX + "," + currentUnit.posY);
            tiles.put(currentUnit.posX + "," + currentUnit.posY, "2");
        }
    }

    public void fillTilesCenter(){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
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

    public void unitFromParent(Unit parent){
        parent.UnitWeights(parent.geneticAlgorithm(parent.statWeights));
    }

    public PImage loadImage2(String imageName){
        PImage returnImage = loadImage("Images\\"+imageName + ".png");
        returnImage.resize(100,0);
        return returnImage;
    }
}
