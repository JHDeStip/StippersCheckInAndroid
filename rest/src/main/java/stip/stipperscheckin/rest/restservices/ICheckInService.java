package stip.stipperscheckin.rest.restservices;

import stannieman.commonservices.models.IHasDataAndSuccessState;
import stip.stipperscheckin.models.CheckInResult;

public interface ICheckInService {
    void doCheckInAsync(int cardNumber, IDoCheckInAsyncCallback callback);
    void init();

    interface IDoCheckInAsyncCallback {
        void doCheckInAsyncCallback(IHasDataAndSuccessState<CheckInResult> result);
    }
}
