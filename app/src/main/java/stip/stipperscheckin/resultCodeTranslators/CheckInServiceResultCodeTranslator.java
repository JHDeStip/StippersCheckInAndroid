package stip.stipperscheckin.resultCodeTranslators;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import stannieman.commonservices.resultCodeTranslators.ServiceResultCodeTranslatorBase;
import stip.stipperscheckin.R;

public class CheckInServiceResultCodeTranslator extends ServiceResultCodeTranslatorBase implements ICheckInServiceResultCodeTranslator {
    @Inject
    public CheckInServiceResultCodeTranslator(Context context) {
        super(context.getString(R.string.global_something_went_wrong));
    }

    @Override
    protected Map<Object, String> getMap() {
        return new HashMap<>();
    }
}
