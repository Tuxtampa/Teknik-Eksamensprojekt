import static java.lang.Integer.signum;
import static java.lang.StrictMath.round;

public class Unit {
    //Baseline stats that all units will need, as of now the idea is that extra stats might just be in the constructor,
    // although now that i think of it we might just want to initialize all the possible stats,
    // and then set the relevant ones to positive in each of their constructors.
    float hp;
    float currentHp;
    float damage;
    float attackSpeed;            //hundredths of a second just to keep it an integer, might be stupid but whatever.
    int range = 1;
    int posX = -1;
    int posY = -1;
    float[] statWeights = {1,1,1,1};  //statWeights[0] = hp, statWeights[1] = damage, statWeights[2] = attackSpeed, statWeights[3] = range.

    void UnitGeneric(){
        hp = 50;
        damage = 5;
        attackSpeed = 100;
    }

    void UnitWeights(float[] importedWeights){
        range = round(importedWeights[3]);
        hp = 50*importedWeights[0];
        damage = 5*importedWeights[1];
        attackSpeed = 100*importedWeights[2];
        statWeights = importedWeights;
    }

    void loadUnit(int newPosX, int newPosY){
        posX = newPosX;
        posY = newPosY;
        currentHp = hp;
    }

    void nextMove(Unit target){
        if((-1-range) < posX-target.posX && posX-target.posX < (range+1) && (-1-range) < posY-target.posY && posY-target.posY < (range+1)){
            System.out.println("attacking");
            target.currentHp -= damage();
            //attackAnimation(posX, posY, target.posX, target.posY);    TODO
        } else {
            System.out.println("moving");
            posX += signum(target.posX);
            posY += signum(target.posY);
            //moveAnimation(posX, posY, signum(target.posX), signum(target.posY); TODO
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
