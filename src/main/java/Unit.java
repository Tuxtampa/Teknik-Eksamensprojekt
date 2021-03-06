import java.util.HashMap;

import static java.lang.StrictMath.round;
import static java.lang.StrictMath.signum;

public class Unit {
    //Baseline stats that all units will need, as of now the idea is that extra stats might just be in the constructor,
    // although now that i think of it we might just want to initialize all the possible stats,
    // and then set the relevant ones to positive in each of their constructors.
    float hp;
    float currentHp;
    float damage;
    float attackSpeed;            //hundredths of a second just to keep it an integer, might be stupid but whatever.
    String faction;
    String type;
    String facing;
    int level = 1;
    int range = 1;
    int posX = 1;
    int posY = 1;
    int team;
    int currentTicksToNextMove = 10;
    int ticksToNextMove = 10;
    boolean moved = false;                     //Used so units don't move more than once per tick.
    boolean viewStats = false;
    float[] statWeights = {1,1,1,1};  //statWeights[0] = hp, statWeights[1] = damage, statWeights[2] = attackSpeed, statWeights[3] = range.

    void UnitGeneric(){
        hp = 50;
        damage = 5;
        attackSpeed = 100;
    }

    void UnitFull(String tempFaction, String tempType, String tempFacing){
        hp = 50;
        damage = 5;
        attackSpeed = 100;
        faction = tempFaction;
        type = tempType;
        facing = tempFacing;
    }

    void UnitWeights(float[] importedWeights){
        range = round(importedWeights[3]);
        if(range > 1) type = "ranger";
        hp = 50*importedWeights[0];
        if(hp < 10) hp = 10;
        damage = 5*importedWeights[1];
        if(damage > 10) type = "heavy";
        ticksToNextMove = (int) (ticksToNextMove*importedWeights[2]);
        currentTicksToNextMove = ticksToNextMove;
        statWeights = importedWeights;
    }

    void loadUnit(int newPosY, int newTeam){
        team = newTeam;
        posX = team;
        posY = newPosY;
        currentHp = hp;
    }

    void nextMove(Unit target, HashMap<String,String> tiles){
        int tempTeam = (int) signum(team-2);
        if((-1-range) < posX-target.posX && posX-target.posX < (range+1) && (-1-range) < posY-target.posY && posY-target.posY < (range+1)){
            System.out.println(posX + " " + posY + " attacking " + target.posX + " " + target.posY + "from " + target.currentHp + " to ");
            target.currentHp -= damage();
            System.out.println(target.currentHp);
        } else {
            moved = false;
            if(posY > target.posY){
                System.out.println("Top,Mid,Down");
                topFree(tiles, tempTeam);
                midFree(tiles, tempTeam);
                downFree(tiles, tempTeam);
            } else if (posY == target.posY){
                System.out.println("Mid,Top,Down");
                midFree(tiles, tempTeam);
                if (posY > 4){
                    topFree(tiles, tempTeam);
                } else {
                    downFree(tiles, tempTeam);
                }
            } else {
                System.out.println("Down,Mid,Top");
                downFree(tiles, tempTeam);
                midFree(tiles, tempTeam);
                topFree(tiles, tempTeam);
            }
                }
            System.out.println(posX + " " + posY);
        }

    void topFree(HashMap<String,String> tiles, int tempTeam) {
        if(!moved) {
            int tempPosX = posX - tempTeam;
            int tempPosY = posY - 1;
            relocate(tiles, tempPosX, tempPosY);
            System.out.println("topFree excuted");
        }
    }
    void midFree(HashMap<String,String> tiles, int tempTeam) {
        if(!moved) {
            int tempPosX = posX - tempTeam;
            int tempPosY = posY;
            relocate(tiles, tempPosX, tempPosY);
            System.out.println("midFree excuted");
        }
    }
    void downFree(HashMap<String,String> tiles, int tempTeam) {
        if(!moved) {
            int tempPosX = posX - tempTeam;
            int tempPosY = posY + 1;
            relocate(tiles, tempPosX, tempPosY);
            System.out.println("downFree excuted");
        }
    }

    private void relocate(HashMap<String, String> tiles, int tempPosX, int tempPosY) {
        System.out.println("getting " + tempPosX + "," + tempPosY);
        if (tiles.get(tempPosX + "," + tempPosY).equals("0")){
            tiles.remove(posX + "," + posY);
            tiles.put(posX + "," + posY, "0");
            posY = tempPosY;
            posX = tempPosX;
            tiles.remove(tempPosX + "," + tempPosY);
            tiles.put(tempPosX + "," + tempPosY, String.valueOf(Integer.signum(team)+1));
            moved = true;
        }
    }

    float damage(){
        return damage;
    }

    public float[] geneticAlgorithm(float[] startWeights){
        float[] newWeights = new float[4];
        for (int i = 0; i < newWeights.length; i++) {
            newWeights[i] = (float) (startWeights[i] + (0.4 * (Math.random() - 0.5)));
            System.out.println(newWeights[i]);
        }
        return newWeights;
    }

}
