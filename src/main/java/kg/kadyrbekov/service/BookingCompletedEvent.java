package kg.kadyrbekov.service;

import kg.kadyrbekov.model.entity.GameZone;
import org.springframework.context.ApplicationEvent;

public class BookingCompletedEvent extends ApplicationEvent {

    private final GameZone gameZone;

    public BookingCompletedEvent(Object source, GameZone gameZone) {
        super(source);
        this.gameZone = gameZone;
    }

    public GameZone getGameZone() {
        return gameZone;
    }
}
