package stip.stipperscheckin.models;

import stannieman.commonservices.models.IHasResultCode;

public class CheckInResult implements IHasResultCode {
    private boolean checkInSuccessful;
    private CheckInResultCodes resultCode;
    private boolean isWeeklyWinner;
    private String userFirstName;
    private String userLastName;
    private String checkInMessage;

    public CheckInResult(boolean checkInSuccessful, CheckInResultCodes resultCode, boolean isWeeklyWinner, String userFirstName, String userLastName, String checkInMessage) {
        this.checkInSuccessful = checkInSuccessful;
        this.resultCode = resultCode;
        this.isWeeklyWinner = isWeeklyWinner;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.checkInMessage = checkInMessage;
    }

    public boolean isCheckInSuccessful() {
        return checkInSuccessful;
    }

    public void setCheckInSuccessful(boolean checkInSuccessful) {
        this.checkInSuccessful = checkInSuccessful;
    }

    public CheckInResultCodes getResultCode() {
        return resultCode;
    }

    public void setResultCode(CheckInResultCodes errorCode) {
        this.resultCode = resultCode;
    }

    public boolean isWeeklyWinner() {
        return isWeeklyWinner;
    }

    public void setWeeklyWinner(boolean weeklyWinner) {
        isWeeklyWinner = weeklyWinner;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getCheckInMessage() {
        return checkInMessage;
    }

    public void setCheckInMessage(String checkInMessage) {
        this.checkInMessage = checkInMessage;
    }
}