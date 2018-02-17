package stip.stipperscheckin.services;

import javax.inject.Inject;

import stannieman.commonservices.models.IHasSuccessState;
import stannieman.commonservices.models.ServiceResult;

public class BarcodeTextValidator implements IBarcodeTextValidator {
    public enum BarcodeTextValidatorResultCodes {
        BARCODE_TEXT_TOO_LONG,
        BARCODE_TEXT_NOT_AN_INTEGER,
    }

    @Inject
    public BarcodeTextValidator() {}

    @Override
    public IHasSuccessState validate(String barcodeText) {
        if (barcodeText.length() > 8) {
            return new ServiceResult<>(BarcodeTextValidatorResultCodes.BARCODE_TEXT_TOO_LONG);
        }

        int barcodeValue;
        try {
            barcodeValue = Integer.parseInt(barcodeText);
        }
        catch (Exception e) {
            return new ServiceResult<>(BarcodeTextValidatorResultCodes.BARCODE_TEXT_NOT_AN_INTEGER);
        }

        return new ServiceResult();
    }
}
