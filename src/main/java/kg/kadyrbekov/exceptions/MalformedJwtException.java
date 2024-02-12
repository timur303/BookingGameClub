package kg.kadyrbekov.exceptions;

import io.jsonwebtoken.JwtException;

public class MalformedJwtException extends RuntimeException {

    public MalformedJwtException(String message) {
        super(message);
    }
}



