package stip.stipperscheckin.rest.restservices;

import android.os.AsyncTask;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import stannieman.commonservices.models.DataServiceResult;
import stannieman.commonservices.models.GeneralResultCodes;
import stannieman.commonservices.models.IHasDataAndSuccessState;
import stannieman.mvvm.messaging.IHandle;
import stannieman.mvvm.messaging.IMessenger;
import stannieman.rest.IKeyAuthRestClientFactory;
import stannieman.rest.IRestClient;
import stannieman.rest.models.ErrorResponseDataBase;
import stannieman.rest.models.RequestProperties;
import stannieman.rest.models.RestResult;
import stip.stipperscheckin.rest.messages.RecreateRestClientsMessage;
import stip.stipperscheckin.rest.models.CheckInResult;
import stip.stipperscheckin.rest.models.CheckInResultCodes;

public class CheckInService implements ICheckInService, IHandle<RecreateRestClientsMessage> {
    private static final String ENDPOINT = "checkIn";
    private static final String CARD_NUMBER_PARAM_NAME = "cardNumber";

    private static final Map<CheckInResultCodes, stip.stipperscheckin.models.CheckInResultCodes> resultCodesMap = new EnumMap<CheckInResultCodes, stip.stipperscheckin.models.CheckInResultCodes>(CheckInResultCodes.class);
    static {
        resultCodesMap.put(CheckInResultCodes.OK, stip.stipperscheckin.models.CheckInResultCodes.OK);
        resultCodesMap.put(CheckInResultCodes.ALREADY_CHECKED_IN, stip.stipperscheckin.models.CheckInResultCodes.ALREADY_CHECKED_IN);
        resultCodesMap.put(CheckInResultCodes.MALFORMED_CARD_NUMBER, stip.stipperscheckin.models.CheckInResultCodes.MALFORMED_CARD_NUMBER);
        resultCodesMap.put(CheckInResultCodes.NO_USER_FOR_CARD_NUMBER, stip.stipperscheckin.models.CheckInResultCodes.NO_USER_FOR_CARD_NUMBER);
        resultCodesMap.put(CheckInResultCodes.CANNOT_GET_USER_DATA, stip.stipperscheckin.models.CheckInResultCodes.CANNOT_GET_USER_DATA);
        resultCodesMap.put(CheckInResultCodes.CANNOT_CHECK_IN, stip.stipperscheckin.models.CheckInResultCodes.CANNOT_CHECK_IN);
        resultCodesMap.put(CheckInResultCodes.CANNOT_CHECK_WEEKLY_WINNER, stip.stipperscheckin.models.CheckInResultCodes.CANNOT_CHECK_WEEKLY_WINNER);
        resultCodesMap.put(CheckInResultCodes.CANNOT_SEND_WINNER_NOTIFICATIONS, stip.stipperscheckin.models.CheckInResultCodes.CANNOT_SEND_WINNER_NOTIFICATIONS);
        resultCodesMap.put(CheckInResultCodes.UNKNOWN_RESULT, stip.stipperscheckin.models.CheckInResultCodes.UNKNOWN_RESULT);
    }

    @Inject
    IMessenger messenger;

    private IRestClient client;
    private final IKeyAuthRestClientFactory restClientFactory;

    @Inject
    public CheckInService(IKeyAuthRestClientFactory restClientFactory) {
        this.restClientFactory = restClientFactory;
    }

    public void init() {
        client = restClientFactory.getRestClient(ENDPOINT);
    }

    public void doCheckInAsync(int cardNumber, ICheckInService.IDoCheckInAsyncCallback callback) {
        new DoCheckInAsyncTask(cardNumber, callback).execute();
    }

    private List<AbstractMap.SimpleEntry<String, String>> getDoCheckInQueryParameters(int cardNumber) {
        List<AbstractMap.SimpleEntry<String, String>> parameters = new ArrayList<>(1);
        parameters.add(new AbstractMap.SimpleEntry<>(CARD_NUMBER_PARAM_NAME, Integer.toString(cardNumber)));
        return parameters;
    }

    private class DoCheckInAsyncTask extends AsyncTask<Void, Void, IHasDataAndSuccessState<stip.stipperscheckin.models.CheckInResult>> {
        private int carNumber;
        private ICheckInService.IDoCheckInAsyncCallback callback;

        DoCheckInAsyncTask(int cardNumber, ICheckInService.IDoCheckInAsyncCallback callback) {
            this.carNumber = cardNumber;
            this.callback = callback;
        }
        @Override
        protected IHasDataAndSuccessState<stip.stipperscheckin.models.CheckInResult> doInBackground(Void... params) {
            RequestProperties<CheckInResult, ErrorResponseDataBase> requestProperties = new RequestProperties<>();
            requestProperties.setQueryParameters(getDoCheckInQueryParameters(carNumber));
            requestProperties.setSuccessResponseDataType(CheckInResult.class);
            IHasDataAndSuccessState<RestResult<CheckInResult, ErrorResponseDataBase>> result = client.get(requestProperties);
            if (!result.isSuccess()) {
                return new DataServiceResult<>(CheckInServiceResultCodes.CANNOT_CHECK_IN);
            }
            RestResult<CheckInResult, ErrorResponseDataBase> restResult = result.getData();

            if (!restResult.isSuccess()) {
                return new DataServiceResult<>(CheckInServiceResultCodes.CANNOT_CHECK_IN);
            }

            return new DataServiceResult<>(mapCheckInResult(restResult.getSuccessData()), GeneralResultCodes.OK);
        }

        @Override
        protected void onPostExecute(IHasDataAndSuccessState<stip.stipperscheckin.models.CheckInResult> checkInResponseServiceResult) {
            callback.doCheckInAsyncCallback(checkInResponseServiceResult);
        }

        private stip.stipperscheckin.models.CheckInResult mapCheckInResult(CheckInResult restResult) {
            return new stip.stipperscheckin.models.CheckInResult(
                    restResult.isCheckInSuccessful(),
                    resultCodesMap.get(restResult.getResultCode()),
                    restResult.isWeeklyWinner(),
                    restResult.getUserFirstName(),
                    restResult.getUserLastName(),
                    restResult.getCheckInMessage()
            );
        }
    }

    @Override
    public void Handle(RecreateRestClientsMessage message) {
        client = restClientFactory.getRestClient(ENDPOINT);
    }
}
