package stip.stipperscheckin.rest.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckInResult {
    private final boolean checkInSuccessful;
    private final CheckInResultCodes resultCode;
    private final boolean isWeeklyWinner;
    private final String userFirstName;
    private final String userLastName;
    private final String checkInMessage;

    @JsonCreator
    public CheckInResult(@JsonProperty("checkInSuccessful") boolean checkInSuccessful, @JsonProperty("resultCode") int resultCode, @JsonProperty("isWeeklyWinner") boolean isWeeklyWinner, @JsonProperty("userFirstName") String userFirstName, @JsonProperty("userLastName") String userLastName, @JsonProperty("checkInMessage") String checkInMessage) {
        this.checkInSuccessful = checkInSuccessful;
        this.resultCode = CheckInResultCodes.GetCheckInResultCodeFromInt(resultCode);
        this.isWeeklyWinner = isWeeklyWinner;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.checkInMessage = checkInMessage;
    }

    public boolean isCheckInSuccessful() {
        return checkInSuccessful;
    }

    public CheckInResultCodes getResultCode() {
        return resultCode;
    }

    public boolean isWeeklyWinner() {
        return isWeeklyWinner;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public String getCheckInMessage() {
        return checkInMessage;
    }
}
