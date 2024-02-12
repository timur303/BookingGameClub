package kg.kadyrbekov.exceptions;

public class ConflictException extends RuntimeException {
    public ConflictException(String football_field_is_already_booked) {
        super(football_field_is_already_booked);
    }
}
