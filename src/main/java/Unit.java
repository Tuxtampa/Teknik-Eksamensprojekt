import static java.lang.Integer.signum;

public class Unit {
    //Baseline stats that all units will need, as of now the idea is that extra stats might just be in the constructor,
    // although now that i think of it we might just want to initialize all the possible stats,
    // and then set the relevant ones to positive in each of their constructors.
    int hp;
    int currentHp;
    int damage;
    int attackSpeed;            //hundredths of a second just to keep it an integer, might be stupid but whatever.
    int range = 1;
    int posX = -1;
    int posY = -1;

    void UnitGeneric(){
        hp = 50;
        damage = 5;
        attackSpeed = 100;
    }

    void loadUnit(int newPosX, int newPosY){
        posX = newPosX;
        posY = newPosY;
        currentHp = hp;
    }

    int[] nextMove(Unit target){
        int[] conclusion = new int[3];          //conclusion[0] = xDestination, conclusion[1] = yDestination, conclusion[2] = damage done.
        if((-1-range) < posX-target.posX && posX-target.posX < (range+1) && (-1-range) < posY-target.posY && posY-target.posY < (range+1)){
            target.currentHp -= damage();
            //attackAnimation(posX, posY, target.posX, target.posY);    TODO
        } else {
            posX += signum(target.posX);
            posY += signum(target.posY);
            //moveAnimation(posX, posY, signum(target.posX), signum(target.posY); TODO
        }
        return conclusion;
    }

    int damage(){
        return damage;
    }
}
