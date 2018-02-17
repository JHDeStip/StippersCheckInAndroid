package stip.stipperscheckin.services;

import stannieman.commonservices.models.IHasDataAndSuccessState;

public interface IBarcodeImageProcessor {
    void processCameraDataAsync(byte[] data, int width, int height, final IBarcodeImageProcessor.IBarcodeImageProcessedCallback callback);

    interface IBarcodeImageProcessedCallback {
        void onBarcodeImageProcessed(IHasDataAndSuccessState<Integer> processBarcodeResult);
    }
}
