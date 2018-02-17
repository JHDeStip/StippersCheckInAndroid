package stip.stipperscheckin.services;

import stannieman.commonservices.models.IHasSuccessState;

public interface IBarcodeTextValidator {
    IHasSuccessState validate(String barcodeText);
}
