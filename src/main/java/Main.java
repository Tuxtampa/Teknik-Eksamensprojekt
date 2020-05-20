import processing.core.PApplet;
import processing.core.PImage;
import processing.sound.SoundFile;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends PApplet {
    public static void main(String[] args) {
        Main.main("Main");
    }
    ArrayList<Unit> unitsTeam1 = new ArrayList<Unit>();
    ArrayList<Unit> unitsTeam2 = new ArrayList<Unit>();
    ArrayList<Unit> unitsTeam1InPlay = new ArrayList<Unit>();
    ArrayList<Unit> unitsTeam2InPlay = new ArrayList<Unit>();
    public HashMap<String,String> tiles = new HashMap<>();
    public HashMap<String,PImage> images = new HashMap<>();
    int millisPerTick = 80;
    int lastTick = 0;
    float xTiles = 10;
    float yTiles = 6;
    float xTileSpace = 1000;
    int yTileSpace = 600;
    boolean playing = false;
    SoundFile sound1;
    SoundFile humanDeath;
    SoundFile treeDeath;
    boolean paused = false;
    Unit stats = null;
    String currentMenu = "mainmenuconcept1";
    String map = "forest";

    public void draw(){
        if(playing){
            tileLines();
            if((millis() - lastTick) > millisPerTick && !paused)tick();
        } else {
            if(currentMenu.equals("mainmenuconcept2") || currentMenu.equals("mainmenuconcept1") || currentMenu.equals("mainmenuconcept3")) {
                if (mouseY > 431 && mouseY < 552 && mouseX > 859 && mouseX < 1036) {
                    currentMenu = "mainmenuconcept2";
                } else if (mouseY > 600 && mouseY < 720 && mouseX > 859 && mouseX < 1036) {
                    currentMenu = "mainmenuconcept3";
                } else {
                    currentMenu = "mainmenuconcept1";
                }
            }

            if (currentMenu.equals("mainmenuno") || currentMenu.equals("mainmenucave") ||currentMenu.equals("mainmenuforest") ){
                if (mouseX+(mouseY*1.77f) > width){
                    currentMenu = "mainmenucave";
                } else {
                    currentMenu = "mainmenuforest";
                }
            }
            try {
                image(loadImage2(currentMenu),0,0);
            } catch (NullPointerException nice) {
                System.out.println("something wrong with " + currentMenu);
                nice.printStackTrace();
            }
        }

    }

    private void tileLines() {
        translate((width-xTileSpace)/2f, (height-yTileSpace)/2f );
        for(int i = 0; i < xTiles; i++) {
            line(xTileSpace / xTiles*i, 0, xTileSpace / xTiles*i, yTileSpace);
            if(xTiles-i > 3) {
                line(0, 0 + (yTileSpace / yTiles * i), xTileSpace, 0 + (yTileSpace / yTiles * i));
            }
        }
        line(xTileSpace,0,xTileSpace,yTileSpace);
        translate(-(width-xTileSpace)/2f,-(height-yTileSpace)/2f);
    }

    public int[] whichDamnTileAmIOn(){
        float xOffset = (width-xTileSpace)/2f;
        float yOffset = (height-yTileSpace)/2f;
        int tempMouseX = (int) (mouseX-xOffset);
        int tempMouseY = (int) (mouseY-yOffset);
        int xTile = (int) (tempMouseX/(xTileSpace / xTiles));
        int yTile = (int) (tempMouseY/(yTileSpace / yTiles));
        return new int[]{xTile, yTile};
    }

    public void death(String faction){
        if (faction.equals("human")) {
            humanDeath.play();
        }
        if (faction.equals("tree")) {
            treeDeath.play();
        }
    }

    public void tick(){
        Action(unitsTeam1InPlay);
        Action(unitsTeam2InPlay);
        lastTick = millis();
        killOffTheWeakOnes(unitsTeam1InPlay);
        killOffTheWeakOnes(unitsTeam2InPlay);
        unitsTeam1InPlay.removeIf(currentUnit -> currentUnit.currentHp < 0.1);
        unitsTeam2InPlay.removeIf(currentUnit -> currentUnit.currentHp < 0.1);
        drawUnits();
    }

    private void Action(ArrayList<Unit> unitsTeam2InPlay) {

        for(Unit currentUnit : unitsTeam2InPlay){
            if(currentUnit.currentTicksToNextMove < 1) {
                Unit target = getNearbyUnits(currentUnit);
                if (target != null) {
                    System.out.println("move");
                    currentUnit.nextMove(target, tiles);
                } else {
                    System.out.println("Target is null");
                }
                currentUnit.currentTicksToNextMove = currentUnit.ticksToNextMove;
            } else currentUnit.currentTicksToNextMove--;
        }
    }

    public void killOffTheWeakOnes(ArrayList<Unit> list){
        for(Unit currentUnit : list){
            if(currentUnit.currentHp < 0.1){
                tiles.remove(currentUnit.posX + "," + currentUnit.posY);
                tiles.put(currentUnit.posX + "," + currentUnit.posY, "0");
                death(currentUnit.faction);
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
                    for(int k = -12; k < 12; k++) {
                        if (!tiles.get((currentunit.posX - 1 + k) + "," + (currentunit.posY - 1 + i)).contains("3") && !tiles.get((currentunit.posX - 1 + k) + "," + (currentunit.posY - 1 + i)).contains("0") ) {
                            System.out.println("Checking if " + tiles.get((currentunit.posX - 1 + k) + "," + (currentunit.posY - 1 + i)) + " is the same as " + (3 - (Integer.signum(currentunit.team) + 1)));
                            if (tiles.get((currentunit.posX - 1 + k) + "," + (currentunit.posY - 1 + i)).equals(String.valueOf(3 - (Integer.signum(currentunit.team) + 1)))) {
                                double tempDist = Math.sqrt(Math.pow(k - 1, 2) + Math.pow(i - 1, 2));
                                System.out.println((currentunit.posX - 1 + k) + "," + (currentunit.posY - 1 + i) + " is " + tempDist + " away");
                                if (tempDist < closestDistance) {
                                    endX = (currentunit.posX - 1 + k);
                                    System.out.println("endX and hopefully also endY changed");
                                    endY = (currentunit.posY - 1 + i);
                                    System.out.println("Unit " + currentunit.posX + "," + currentunit.posY + " Is looking for a unit in " + endX + "," + endY);
                                    closestDistance = tempDist;
                                    returnUnit = findUnit(endX, endY, (int) Math.signum(currentunit.team - 1));
                                }
                            }
                        }
                    }
                }
        } catch (IndexOutOfBoundsException ie){
            ie.printStackTrace();
            return null;
        }
        return returnUnit;
    }

    public Unit findUnit(int posX, int posY, int desiredTeam){
        Unit returnUnit = null;
        System.out.println("Desiredteam is " + desiredTeam);
        if (desiredTeam == 1 || desiredTeam == 0) {
            for (Unit unit : unitsTeam1InPlay) {
                if (unit.posY == posY && unit.posX == posX) {
                    returnUnit = unit;
                    System.out.print("returnunit is not null");
                } else {
                    System.out.println("Apparently " + unit.posX + "," + unit.posY + " is not the same as " + posX + "," + posY + " for de");
                }
            }
        }
        if (desiredTeam == -1 || desiredTeam == 0) {
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
        loadAllTheDamnImages();
        fillTileBoundaries();
        fillTilesCenter();
        startMusic("menu");
        System.out.println("Setup ran");
    }

    public void startMusic(String theme){
        SoundFile music1 = new SoundFile(this, "Sounds\\" + theme + "music.mp3");
        music1.loop();
    }

    private void loadSoundFiles(String name) {
         sound1 = new SoundFile(this,"Sounds\\" + name + ".mp3");
         sound1.play();
         humanDeath = new SoundFile(this,"Sounds\\oof.mp3");
         treeDeath = new SoundFile(this,"Sounds\\yikes.mp3");
    }

    public void drawUnits(){
        if(map.equals("cave")){
            PImage cave = loadImage2("cave");
            background(loadImage2("cave"));
        }
        if(map.equals("forest")){
            background(loadImage2("forest"));
        }

        tileLines();
        for(Unit currentUnit : unitsTeam1InPlay){
            image(loadImage2(currentUnit.faction + currentUnit.type + currentUnit.facing + currentUnit.level),currentUnit.posX*(xTileSpace/xTiles)+((width-xTileSpace)/2f),currentUnit.posY*(yTileSpace/yTiles)+((height-yTileSpace)/2f));
            pushMatrix();
            fill(0);
            rect(currentUnit.posX*(xTileSpace/xTiles)+((width-xTileSpace)/2f)+20,currentUnit.posY*(yTileSpace/yTiles)+((height-yTileSpace)/2f)-10,60,10);
            fill(255,0,0);
            rect(currentUnit.posX*(xTileSpace/xTiles)+((width-xTileSpace)/2f)+20,currentUnit.posY*(yTileSpace/yTiles)+((height-yTileSpace)/2f)-10,60*(currentUnit.currentHp/currentUnit.hp*1f),10);
            popMatrix();

        }
        for(Unit currentUnit : unitsTeam2InPlay){
            image(loadImage2(currentUnit.faction + currentUnit.type + currentUnit.facing + currentUnit.level),currentUnit.posX*(xTileSpace/xTiles)+((width-xTileSpace)/2f),currentUnit.posY*(yTileSpace/yTiles)+((height-yTileSpace)/2f));
            pushMatrix();
            fill(0);
            rect(currentUnit.posX*(xTileSpace/xTiles)+((width-xTileSpace)/2f)+20,currentUnit.posY*(yTileSpace/yTiles)+((height-yTileSpace)/2f)-10,60,10);
            fill(255,0,0);
            rect(currentUnit.posX*(xTileSpace/xTiles)+((width-xTileSpace)/2f)+20,currentUnit.posY*(yTileSpace/yTiles)+((height-yTileSpace)/2f)-10,60*(currentUnit.currentHp/currentUnit.hp*1f),10);
            popMatrix();
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
        if(key == '5'){
            paused = !paused;
        }
    }

    @Override
    public void mouseClicked() {
        loadSoundFiles("button");
        boolean uno = true;
        if(playing) {
            int[] coordinates = whichDamnTileAmIOn();
            try {
                stats = findUnit(coordinates[0], coordinates[1], 0);
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            }
        }
        if(!playing) {
            if (mouseY > 431 && mouseY < 552 && mouseX > 859 && mouseX < 1036) {
                currentMenu = "mainmenuno";
                uno = false;
            }
            if (mouseY > 600 && mouseY < 720 && mouseX > 859 && mouseX < 1036) {
                currentMenu = "mainmenuinfo";
                uno = false;
            }
        }

        if(uno) {
            if (currentMenu.equals("mainmenuno") || currentMenu.equals("mainmenucave") || currentMenu.equals("mainmenuforest")) {
                if (mouseX + (mouseY * 1.77f) > width) {
                    map = "cave";
                    playing = true;
                    System.out.println("playing the fucking cave please");
                } else {
                    map = "forest";
                    playing = true;
                    System.out.println("playing the fucking forest please");
                }
            }
        }

        if (mouseY > 0 && mouseY < 69 && mouseX > 1849 && mouseX < 20000){
            exit();
        }
        if (mouseY > 0 && mouseY < 69 && mouseX > 0 && mouseX < 72){
            currentMenu = "mainmenuconcept1";
            playing = false;
        }
    }

    public void createUnit(int team) {
        for (int i = 0; i < 1; i++) {
            Unit unit2 = new Unit();
            if(team == 1 && unitsTeam1.size() < 4){
                unit2.UnitFull("human", "dude", "left");
                unitsTeam1.add(unit2);
            }
            if(team == 2 && unitsTeam2.size() < 4){
                unit2.UnitFull("tree", "dude", "right");
                unitsTeam2.add(unit2);
            }
        }
        System.out.print("Unit created for team " + team);
    }
    public void loadUnits(){
        unitsTeam1InPlay.clear();
        unitsTeam2InPlay.clear();
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
        if(tiles.size() != 0)tiles.clear();
        for(int i = -20; i < 22; i++){
            for(int j = -20; j < 22; j++){
                tiles.put(j + "," + i, "3");
            }
        }
    }

    public void unitFromParent(Unit parent){
        parent.UnitWeights(parent.geneticAlgorithm(parent.statWeights));
    }

    public PImage loadImage1(String imageName){
        PImage returnImage = loadImage("Images\\"+imageName + ".png");
        if(imageName.contains("forest") || imageName.contains("cave")){
            returnImage.resize(width,height);
        } else if (imageName.contains("tree")) {
            returnImage.resize(120, 0);
        } if (imageName.contains("human")){
            returnImage.resize(100,0);
        }
        System.out.println(imageName + "loaded");
        return returnImage;
    }



    public PImage loadImage2(String imageName){
        return images.get(imageName);
    }

    public void loadAllTheDamnImages(){
        String[] imageNames = {"mainmenuconcept3","mainmenuinfo","humandudeleft1", "humanduderight1", "humanheavyleft1", "humanheavyright1", "humanrangerleft1", "humanrangerright1", "mainmenu", "mainmenucave","mainmenuforest","mainmenuconcept1","mainmenuconcept2","mainmenuno","treedudeleft1","treeduderight1","treeheavyleft1","treeheavyright1","treerangerleft1","treerangerright1","cave","forest"};
        for(String imageName : imageNames){
            images.put(imageName, loadImage1(imageName));
            System.out.println(imageName + " loaded");
        }
    }
}
