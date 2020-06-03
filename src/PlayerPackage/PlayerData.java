package PlayerPackage;

/**
 * PlayerData that contains all the static data of the game
 * Follows the singleton pattern as there should only be one instance of a PlayerData for each game
 */
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

    /**
     * Gets the singular instance of PlayerData
     * @return PlayerData instance
     */
    public static PlayerData getInstance(){
        //If player data hasn't been created yet, create a new one
        if (playerDataInstance == null) {
            playerDataInstance = new PlayerData();
        }
        return playerDataInstance;
    }

    /**
     * Gets the number of lives
     * @return lives as int
     */
    public int getLife(){
        return life;
    }

    /**
     * Gets current money
     * @return money as int
     */
    public int getMoney(){
        return money;
    }

    /**
     * Returns the timescale
     * @return timescale as int
     */
    public int getTimescale(){
        return timescale;
    }

    //Changing the stats in the PlayerData
    /**
     * Starting stats
     * Also used when starting a new level
     */
    public void resetData(){
        money = 500;
        life = 25;
        timescale = 1;
    }

    /**
     * Loses a life depending on the penalty
     * @param penalty decided by enemies
     */
    public void loseLife(int penalty) {
        life -= penalty;
    }

    /**
     * Adds money depending on amount
     * @param money adds given amount of money
     */
    public void addMoney(int money){
        this.money += money;
    }

    /**
     * Loses money when purchasing towers
     * @param money loses given amount of money
     */
    public void loseMoney(int money){
        //Shouldn't have a case where money < 0 from code in purchasing a tower
        this.money -= money;
    }

    /**
     * Increases the timescale (when L is pressed)
     */
    public void increaseTimescale() {
        timescale++;
    }

    /**
     * Decreases the timescale (when S is pressed)
     */
    public void decreaseTimescale(){
        //Decreases timescale if over 1
        if(timescale > 1) {
            timescale--;
        }
    }

}

