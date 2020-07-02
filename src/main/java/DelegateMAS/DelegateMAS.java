package DelegateMAS;

public abstract class DelegateMAS {

    /*
    The tick rate at which the Delegate MAS
        module will take action.
     */
    private int actionRate;

    /*
    Counter that count the number of ticks.
        Used together with actionRate to
        determine when to take action.
     */
    private int tickCounter;


    /*
    Constructor
     */
    public DelegateMAS(int actionRate)
            throws IllegalArgumentException {
        if (! isValidActionRate(actionRate)) {
            throw new IllegalArgumentException(
                    "DELEGATE MAS | THE GIVEN ACTION RATE IS NOT A VALID ONE"
            );
        }
        this.actionRate = actionRate;

        this.tickCounter = 0;
    }


    /*
    Action
     */
    private boolean isValidActionRate(int actionRate) {
        return actionRate > 0;
    }

    public int getActionRate() {
        return actionRate;
    }

    protected abstract void takeAction();

    /*
    Tick
     */
    public void tick() {
        if (tickCounter % actionRate == 0) {
            tickCounter = 0;
            takeAction();
        }
        tickCounter += 1;
    }
}
