package stip.stipperscheckin.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import stannieman.commonservices.models.IHasDataAndSuccessState;
import stannieman.mvvm.ViewModelBase;
import stip.stipperscheckin.R;
import stip.stipperscheckin.BR;
import stip.stipperscheckin.models.CheckInResult;
import stip.stipperscheckin.models.CheckInResultCodes;
import stip.stipperscheckin.rest.restservices.ICheckInService;
import stip.stipperscheckin.resultCodeTranslators.ICheckInServiceResultCodeTranslator;
import stip.stipperscheckin.services.ISettingsService;

@Singleton
public class CheckInResultViewModel extends ViewModelBase implements ICheckInService.IDoCheckInAsyncCallback {
    public static final String CARD_NUMBER_PARAMETER_NAME = "cardNumber";

    private static final int SHORT_DELAY = 3000;
    private static final int LONG_DELAY = 5000;
    private static final int EXTRA_LONG_DELAY = 10000;

    @Inject
    Context context;
    @Inject
    ICheckInService checkInService;
    @Inject
    ICheckInServiceResultCodeTranslator checkInServiceResultCodeTranslator;
    @Inject
    ISettingsService settingsService;

    //region displayOrrientation

    private int displayOrrientation;

    @Bindable
    public int getDisplayOrrientation() {
        return displayOrrientation;
    }

    //endregion

    //region message

    private String message;

    @Bindable
    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        if (!this.message.equals(message)) {
            this.message = message;
            notifyPropertyChanged(BR.message);
        }

    }

    //endregion

    //region background

    private Drawable background;

    @Bindable
    public Drawable getBackground() {
        return background;
    }

    private void setBackground(Drawable background) {
        if (!this.background.equals(background)) {
            this.background = background;
            notifyPropertyChanged(BR.background);
        }
    }

    //endregion

    //region message visibilities

    private int longMessageVisibility;

    @Bindable
    public int getLongMessageVisibility() {
        return longMessageVisibility;
    }

    private void setLongMessageVisibility(int longMessageVisibility) {
        if (this.longMessageVisibility != longMessageVisibility) {
            this.longMessageVisibility = longMessageVisibility;
            notifyPropertyChanged(BR.longMessageVisibility);
            notifyPropertyChanged(BR.standardMessageVisibility);
        }
    }

    @Bindable
    public int getStandardMessageVisibility() {
        return longMessageVisibility == View.VISIBLE ? View.GONE : View.VISIBLE;
    }

    //endregion

    private final Handler navigateBackHandler;
    private final Runnable navigateBackRunnable;

    private boolean checkInStarted;
    private boolean delayedNavigationStarted;
    private Date delayedNavigationStartedTime;
    private long msLeftBeforeNavigating;

    @Inject
    public CheckInResultViewModel() {
        navigateBackHandler = new Handler();
        navigateBackRunnable = new Runnable() {
            @Override
            public void run() {
                checkInStarted = false;
                delayedNavigationStarted = false;
                getNavigationService().navigateBack();
            }
        };
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, Intent intent) {
        super.onCreate(savedInstanceState, intent);

        displayOrrientation = settingsService.getDisplayOrrientation();

        if (checkInStarted) {
            return;
        }
        checkInStarted = true;

        int cardNumber = intent.getIntExtra(CARD_NUMBER_PARAMETER_NAME, 0);

        setLongMessageVisibility(View.GONE);

        message=context.getString(R.string.CheckInResultView_busy_checking_in);
        background=context.getResources().getDrawable(R.drawable.solidBlue);

        checkInService.doCheckInAsync(cardNumber,this);
    }

    @Override
    public void onResume() {
        if (delayedNavigationStarted) {
            NavigateDelayed(msLeftBeforeNavigating >= 0 ? msLeftBeforeNavigating : 0);
        }
    }

    @Override
    public void onPause() {
        if (delayedNavigationStarted) {
            msLeftBeforeNavigating = msLeftBeforeNavigating - (new Date().getTime() - delayedNavigationStartedTime.getTime());
        }

        if (navigateBackHandler != null && navigateBackRunnable != null) {
            navigateBackHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void doCheckInAsyncCallback(IHasDataAndSuccessState<CheckInResult> result) {
        if (!result.isSuccess()) {
            setBackground(context.getResources().getDrawable(R.drawable.solidRed));
            setMessage(checkInServiceResultCodeTranslator.getErrorMessage(result));
            NavigateDelayed(LONG_DELAY);
            return;
        }

        CheckInResult checkInResult = result.getData();

        setBackground(context.getResources().getDrawable(checkInResult.getResultCode() == CheckInResultCodes.OK ? R.drawable.solidGreen : R.drawable.solidRed));

        switch (checkInResult.getResultCode()) {
            case OK:
                if (checkInResult.isWeeklyWinner()) {
                    setMessage(String.format(context.getString(R.string.CheckIn_check_in_succeeded_weekly_winner), checkInResult.getUserFirstName()));
                    NavigateDelayed(EXTRA_LONG_DELAY);
                    return;
                }

                String customMessage = checkInResult.getCheckInMessage();
                if (customMessage != null && !customMessage.equals("")) {
                    setMessage(customMessage);
                    setLongMessageVisibility(View.VISIBLE);
                    NavigateDelayed(LONG_DELAY);
                    return;
                }

                setMessage(String.format(context.getString(R.string.CheckIn_check_in_succeeded), checkInResult.getUserFirstName()));
                NavigateDelayed(SHORT_DELAY);
                return;

            case ALREADY_CHECKED_IN:
                setMessage(String.format(context.getString(R.string.CheckIn_already_checked_in), checkInResult.getUserFirstName()));
                NavigateDelayed(LONG_DELAY);
                return;

            case CANNOT_CHECK_WEEKLY_WINNER:
                setMessage(String.format(context.getString(R.string.CheckIn_check_in_succeeded_could_not_check_weekly_winner), checkInResult.getUserFirstName()));
                setLongMessageVisibility(View.VISIBLE);
                NavigateDelayed(LONG_DELAY);
                return;

            case CANNOT_SEND_WINNER_NOTIFICATIONS:
                setMessage(String.format("%s %s", String.format(context.getString(R.string.CheckIn_check_in_succeeded_weekly_winner), checkInResult.getUserFirstName()), context.getString(R.string.CheckIn_check_in_could_not_notify)));
                setLongMessageVisibility(View.VISIBLE);
                // In this case we don't navigate back.
                return;

            case NO_USER_FOR_CARD_NUMBER:
                setMessage(context.getString(R.string.CheckIn_card_number_not_connected_to_user));
                NavigateDelayed(LONG_DELAY);
                return;

            default:
                setMessage(context.getString(R.string.global_something_went_wrong));
                NavigateDelayed(LONG_DELAY);
        }
    }
    
    private void NavigateDelayed(long delay) {
        delayedNavigationStarted = true;
        delayedNavigationStartedTime = new Date();
        msLeftBeforeNavigating = delay;
        navigateBackHandler.postDelayed(navigateBackRunnable, delay);
    }
}
