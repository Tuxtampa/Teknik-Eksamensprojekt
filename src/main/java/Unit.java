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

    void nextMove(){
        attack();
        //move();
    }

    void attack(){
        for(int i = 0; i < 1+(2*range); i++){    //First i wanna check the sorrounding tiles to see if there are enemies withing range
            for(int j = 0; j < 1+(2*range); j++){
                //dostuff;
            }
        }
    }
}
