public class PlayerData {
    public static PlayerData playerDataInstance = null;
    private static int money;
    private static int life;

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
        money = 500;
        life = 25;
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

}

