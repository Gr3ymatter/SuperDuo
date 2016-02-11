package it.jaschke.alexandria.CameraPreview;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

/**
        * Factory for creating a tracker and associated graphic to be associated with a new barcode.  The
        * multi-processor uses this factory to create barcode trackers as needed -- one for each barcode.
        */
class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;
    private BarcodeGraphicTracker.Callback mCallback;

    BarcodeTrackerFactory(GraphicOverlay<BarcodeGraphic> barcodeGraphicOverlay, BarcodeGraphicTracker.Callback callback) {
        mGraphicOverlay = barcodeGraphicOverlay;
        mCallback = callback;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        BarcodeGraphic graphic = new BarcodeGraphic(mGraphicOverlay);
        return new BarcodeGraphicTracker(mGraphicOverlay, graphic, mCallback);
    }

}
