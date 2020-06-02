package PlayerPackage;

public class PlayerData {
    private static PlayerData playerDataInstance = null;
    private static int money;
    private static int life;
    private static int timescale;
    //Constructor
    private PlayerData(){
        resetData();
    }

    //Getters and setters
    public static PlayerData getInstance(){
        if (playerDataInstance == null) {
            playerDataInstance = new PlayerData();
        }
        return playerDataInstance;
    }

    public int getLife(){
        return life;
    }

    public int getMoney(){
        return money;
    }

    //Sets initial values, as well as used when resetting a level
    public void resetData(){
        money = 500000;
        life = 25;
        timescale = 1;
    }

    public void loseLife(int penalty) {
        life -= penalty;
    }

    public void addMoney(int money){
        this.money += money;
    }

    public void loseMoney(int money){
        this.money -= money;
    }

    public void increaseTimescale() {
        timescale++;
    }

    public void decreaseTimescale(){
        if(timescale > 1) {
            timescale--;
        }
    }

    public int getTimescale(){
        return timescale;
    }
}

