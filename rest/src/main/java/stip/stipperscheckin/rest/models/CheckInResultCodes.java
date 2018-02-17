package stip.stipperscheckin.rest.models;

public enum CheckInResultCodes {
    OK(0),
    ALREADY_CHECKED_IN(1),
    MALFORMED_CARD_NUMBER(2),
    NO_USER_FOR_CARD_NUMBER(3),
    CANNOT_GET_USER_DATA(4),
    CANNOT_CHECK_IN(5),
    CANNOT_CHECK_WEEKLY_WINNER(6),
    CANNOT_SEND_WINNER_NOTIFICATIONS(7),
    UNKNOWN_RESULT(8);

    private final int value;

    CheckInResultCodes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static CheckInResultCodes GetCheckInResultCodeFromInt(int resultCodeInt) {
        for (CheckInResultCodes checkInResultCode : CheckInResultCodes.values()) {
            if (checkInResultCode.getValue() == resultCodeInt) {
                return checkInResultCode;
            }
        }

        return UNKNOWN_RESULT;
    }
}
