package stip.stipperscheckin.models;

public enum CheckInResultCodes {
    OK,
    ALREADY_CHECKED_IN,
    MALFORMED_CARD_NUMBER,
    NO_USER_FOR_CARD_NUMBER,
    CANNOT_GET_USER_DATA,
    CANNOT_CHECK_IN,
    CANNOT_CHECK_WEEKLY_WINNER,
    CANNOT_SEND_WINNER_NOTIFICATIONS,
    UNKNOWN_RESULT
}
