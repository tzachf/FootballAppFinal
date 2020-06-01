package Domain.Events;

import Domain.Users.Player;

import java.util.UUID;

public class OffSide extends AGameEvent implements IEvent {
    public OffSide(double gameMinute, Player playerWhocommit) {
        super(gameMinute, playerWhocommit);
    }

    public OffSide(double gameMinute) {
        super(gameMinute);
    }

    /***FOR DB SELECT
     * @param gameMinute
     * @param playerWhocommit
     * @param objectID*/
    public OffSide(double gameMinute, Player playerWhocommit, UUID objectID, Event_Logger ev) {
        super(gameMinute, playerWhocommit, objectID,ev);
    }

    @Override
    public String toString() {
        return (getClass().getName());
    }
}
