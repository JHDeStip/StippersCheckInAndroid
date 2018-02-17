package stip.stipperscheckin.services;

import android.os.AsyncTask;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.Code128Reader;

import javax.inject.Inject;

import stannieman.commonservices.helpers.ResultCodeHelper;
import stannieman.commonservices.models.DataServiceResult;
import stannieman.commonservices.models.GeneralResultCodes;
import stannieman.commonservices.models.IHasDataAndSuccessState;
import stannieman.commonservices.models.IHasSuccessState;

public class BarcodeImageProcessor implements IBarcodeImageProcessor {
    public enum BarcodeImageProcessorResultCodes {
        CANNOT_READ_BARCODE_FROM_IMAGE,
        CANNOT_VALIDATE_BARCODE
    }

    private static final Reader barcodeReader = new Code128Reader();

    @Inject
    IBarcodeTextValidator validator;

    @Inject
    public BarcodeImageProcessor() {}

    public void processCameraDataAsync(byte[] data, int width, int height, final IBarcodeImageProcessor.IBarcodeImageProcessedCallback callback) {
        new ProcessBarcodeImageAsyncTask(data, width, height, callback).execute();
    }

    private class ProcessBarcodeImageAsyncTask extends AsyncTask<Void, Void, IHasDataAndSuccessState<Integer>> {
        private byte[] data;
        private int width, height;
        IBarcodeImageProcessor.IBarcodeImageProcessedCallback callback;

        public ProcessBarcodeImageAsyncTask(byte[] data, int width, int height, IBarcodeImageProcessor.IBarcodeImageProcessedCallback callback) {
            this.data = data;
            this.width = width;
            this.height = height;
            this.callback = callback;
        }

        @Override
        protected IHasDataAndSuccessState<Integer> doInBackground(Void... params) {
            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, width, height, 0, 0, width, height, false);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            String barcodeText;
            try {
                barcodeText = barcodeReader.decode(bitmap).getText();
            } catch (Exception e) {
                return new DataServiceResult<>(BarcodeImageProcessorResultCodes.CANNOT_READ_BARCODE_FROM_IMAGE);
            }

            IHasSuccessState validationResult = validator.validate(barcodeText);
            if (!validationResult.isSuccess()) {
                BarcodeTextValidator.BarcodeTextValidatorResultCodes resultCode = ResultCodeHelper.GetResultCodeOrNull(validationResult, BarcodeTextValidator.BarcodeTextValidatorResultCodes.class);
                return resultCode != null
                        ? new DataServiceResult<Integer, BarcodeTextValidator.BarcodeTextValidatorResultCodes>(resultCode)
                        : new DataServiceResult<Integer, BarcodeImageProcessorResultCodes>(BarcodeImageProcessorResultCodes.CANNOT_VALIDATE_BARCODE);
            }

            return new DataServiceResult<>(Integer.parseInt(barcodeText), GeneralResultCodes.OK);
        }

        @Override
        protected void onPostExecute(IHasDataAndSuccessState<Integer> processBarcodeResult) {
            callback.onBarcodeImageProcessed(processBarcodeResult);
        }
    }
}
