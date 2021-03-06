/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package ti.redlaser;

import java.util.HashMap;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;

import android.content.Intent;
import android.graphics.Rect;

import com.ebay.redlasersdk.BarcodeTypes;
import com.ebay.redlasersdk.RedLaserExtras;

@Kroll.module(name="Redlaser", id="ti.redlaser")
public class RedlaserModule extends KrollModule
{

	// Standard Debugging variables
	private static final String LCAT = "RedlaserModule";
	private static final boolean DBG = TiConfig.LOGD;

	// Constants
	@Kroll.constant public static final int STATUS_API_LEVEL_TOO_LOW = -1;
	@Kroll.constant public static final int STATUS_BAD_LICENSE = -2;
	@Kroll.constant public static final int STATUS_MISSING_PERMISSIONS = -3;
	@Kroll.constant public static final int STATUS_NO_CAMERA = -4;
	@Kroll.constant public static final int STATUS_SCAN_LIMIT_REACHED = -5;
	@Kroll.constant public static final int STATUS_UNKNOWN_STATE = -6;
	@Kroll.constant public static final int STATUS_EVAL_MODE_READY = 1;
	@Kroll.constant public static final int STATUS_LICENSED_MODE_READY = 2;

	@Kroll.constant public static final int BARCODE_TYPE_CODABAR = BarcodeTypes.CODABAR;
	@Kroll.constant public static final int BARCODE_TYPE_CODE128 = BarcodeTypes.CODE128;
	@Kroll.constant public static final int BARCODE_TYPE_CODE39 = BarcodeTypes.CODE39;
	@Kroll.constant public static final int BARCODE_TYPE_CODE93 = BarcodeTypes.CODE93;
	@Kroll.constant public static final int BARCODE_TYPE_DATAMATRIX = BarcodeTypes.DATAMATRIX;
	@Kroll.constant public static final int BARCODE_TYPE_EAN13 = BarcodeTypes.EAN13;
	@Kroll.constant public static final int BARCODE_TYPE_EAN2 = BarcodeTypes.EAN2;
	@Kroll.constant public static final int BARCODE_TYPE_EAN5 = BarcodeTypes.EAN5;
	@Kroll.constant public static final int BARCODE_TYPE_EAN8 = BarcodeTypes.EAN8;
	@Kroll.constant public static final int BARCODE_TYPE_GS1DATABAR = BarcodeTypes.GS1DATABAR;
	@Kroll.constant public static final int BARCODE_TYPE_GS1DATABAR_EXPANDED = BarcodeTypes.GS1DATABAR_EXPANDED;
	@Kroll.constant public static final int BARCODE_TYPE_ITF = BarcodeTypes.ITF;
	@Kroll.constant public static final int BARCODE_TYPE_NONE = BarcodeTypes.NONE;
	@Kroll.constant public static final int BARCODE_TYPE_PDF417 = BarcodeTypes.PDF417;
	@Kroll.constant public static final int BARCODE_TYPE_QRCODE = BarcodeTypes.QRCODE;
	@Kroll.constant public static final int BARCODE_TYPE_RSS14 = BarcodeTypes.RSS14;
	@Kroll.constant public static final int BARCODE_TYPE_UPCE = BarcodeTypes.UPCE;
	
	@Kroll.constant public static final String PREF_ORIENTATION_LANDSCAPE = ScannerActivity.PREF_ORIENTATION_LANDSCAPE;
	@Kroll.constant public static final String PREF_ORIENTATION_PORTRAIT = ScannerActivity.PREF_ORIENTATION_PORTRAIT;

	public RedlaserModule()
	{
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app)
	{
		if (DBG) {
			Log.d(LCAT, "inside onAppCreate of Red Laser module");
		}
	}

	@SuppressWarnings("rawtypes")
	@Kroll.method
	public void startScanning(HashMap options)
	{
		ScannerActivity.moduleInstance = this;
		
		if (options.containsKey("overlay")) {
			ScannerActivity.overlayProxy = (TiViewProxy) options.get("overlay");
		}

		if (options.containsKey("cameraPreview")) {
			ScannerActivity.cameraPreviewProxy = (CameraPreviewProxy) options.get("cameraPreview");
			ScannerActivity.cameraPreviewProxy.getOrCreateView();
		}

		if (options.containsKey("cameraIndex") &&
			options.get("cameraIndex") instanceof Integer) {
				ScannerActivity.requestedCameraIndex = (Integer)options.get("cameraIndex");
		}

		if (options.containsKey("activeRect")) {
			setActiveRect((HashMap)options.get("activeRect"));
		}

		if (options.containsKey("orientation") &&
				options.get("orientation") instanceof String) {
					ScannerActivity.prefOrientation = (String)options.get("orientation");
			}

		try {
			Intent scanIntent = new Intent(TiApplication.getInstance(), ScannerActivity.class);
			TiApplication.getAppRootOrCurrentActivity().startActivityForResult(scanIntent,1);
		} catch(Exception e)
		{
			Log.d(LCAT, e.getLocalizedMessage() + " " + e.getCause());
		}

	}

	@Kroll.method
	public void doneScanning()
	{
		// MOD-1478 - NullPointerException if doneScanning is called when scanner not active
		if (ScannerActivity.activeInstance != null) {
			ScannerActivity.activeInstance.doneScanning();
		} else {
			Log.w(LCAT, "Scanner is not active!");
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Kroll.setProperty
	public void setActiveRect(HashMap options)
	{
		Log.w(LCAT, "Setting active rect is likely to cause a crash! This is a bug in the underlying RedLaser library.");
		
		Rect rect = new Rect(
				(Integer)options.get("left"),
				(Integer)options.get("top"),
				(Integer)options.get("left") + (Integer)options.get("width"),
				(Integer)options.get("top") + (Integer)options.get("height")
		);
			
		if (ScannerActivity.activeInstance != null) {
			ScannerActivity.activeInstance.setActiveRect(rect);
		} else {
			ScannerActivity.activeRect = rect;
		}
	}

	@Kroll.getProperty
	public boolean getIsFlashAvailable()
	{
		if (ScannerActivity.activeInstance != null) {
			return ScannerActivity.activeInstance.isTorchAvailable();
		} else {
			return false;
		}
	}
	
	@Kroll.setProperty
	public void setTorchState(Boolean value)
	{
		if (ScannerActivity.activeInstance != null) {
			ScannerActivity.activeInstance.setTorch(!ScannerActivity.activeInstance.getTorch());
		}
	}
	
	@Kroll.getProperty
	public Integer getTorchState()
	{
		if (ScannerActivity.activeInstance != null &&
				ScannerActivity.activeInstance.getTorch()) {
			return 1;
		} else {
			return 0;
		}
	}
	
	@Kroll.method
	public void turnFlash(Boolean value) {
		// In the iOS version, this exposes the turnFlash
		// method. There doesn't seem to be an equivalent in the Android
		// version of the RedLaser SDK, but then it also seems redundant on
		// iOS because we have the torchState property. So for now, for the
		// sake of world peace, we'll just provide this method on Android
		// as well and tie it to the torchState property.
		setTorchState(value);
	}

	@Kroll.method
	public void requestCameraSnapshot()
	{
		if (ScannerActivity.activeInstance != null) {
			ScannerActivity.activeInstance.requestImageData();
		}
	}
	
	// Properties
	@Kroll.getProperty
	public Integer getStatus()
	{
		switch(RedLaserExtras.checkReadyStatus(TiApplication.getInstance())) {
		case APILevelTooLow:
				return STATUS_API_LEVEL_TOO_LOW;
		case BadLicense:
				return STATUS_BAD_LICENSE;
		case EvalModeReady:
				return STATUS_EVAL_MODE_READY;
		case LicensedModeReady:
        		return STATUS_LICENSED_MODE_READY;
		case MissingPermissions:
        		return STATUS_MISSING_PERMISSIONS;
		case NoCamera:
        		return STATUS_NO_CAMERA;
		case ScanLimitReached:
				return STATUS_SCAN_LIMIT_REACHED;
		case UnknownState:
				return STATUS_UNKNOWN_STATE;
		}
		return STATUS_UNKNOWN_STATE;
	}
	
	@Kroll.getProperty
	public String getSdkVersion()
	{
		return RedLaserExtras.getRedLaserSDKVersion();
	}
	
	@Kroll.setProperty
	public void setScanCodabar(Boolean value)
	{
		if (ScannerActivity.activeInstance != null) {
			ScannerActivity.activeInstance.enabledTypes.setCodabar(value);
		}
	}
	
	@Kroll.getProperty
	public Boolean getScanCodabar()
	{
		return ScannerActivity.activeInstance != null &&
				ScannerActivity.activeInstance.enabledTypes.getCodabar();
	}

	@Kroll.setProperty
	public void setScanCode128(Boolean value)
	{
		if (ScannerActivity.activeInstance != null) {
			ScannerActivity.activeInstance.enabledTypes.setCode128(value);
		}
	}
	
	@Kroll.getProperty
	public Boolean getScanCode128()
	{
		return ScannerActivity.activeInstance != null &&
				ScannerActivity.activeInstance.enabledTypes.getCode128();
	}

	@Kroll.setProperty
	public void setScanCode39(Boolean value)
	{
		if (ScannerActivity.activeInstance != null) {
			ScannerActivity.activeInstance.enabledTypes.setCode39(value);
		}
	}
	
	@Kroll.getProperty
	public Boolean getScanCode39()
	{
		return ScannerActivity.activeInstance != null &&
				ScannerActivity.activeInstance.enabledTypes.getCode39();
	}

	@Kroll.setProperty
	public void setScanCode93(Boolean value)
	{
		if (ScannerActivity.activeInstance != null) {
			ScannerActivity.activeInstance.enabledTypes.setCode93(value);
		}
	}
	
	@Kroll.getProperty
	public Boolean getScanCode93()
	{
		return ScannerActivity.activeInstance != null &&
				ScannerActivity.activeInstance.enabledTypes.getCode93();
	}

	@Kroll.setProperty
	public void setScanDataMatrix(Boolean value)
	{
		if (ScannerActivity.activeInstance != null) {
			ScannerActivity.activeInstance.enabledTypes.setDataMatrix(value);
		}
	}
	
	@Kroll.getProperty
	public Boolean getScanDataMatrix()
	{
		return ScannerActivity.activeInstance != null &&
				ScannerActivity.activeInstance.enabledTypes.getDataMatrix();
	}

	@Kroll.setProperty
	public void setScanEan13(Boolean value)
	{
		if (ScannerActivity.activeInstance != null) {
			ScannerActivity.activeInstance.enabledTypes.setEan13(value);
		}
	}
	
	@Kroll.getProperty
	public Boolean getScanEan13()
	{
		return ScannerActivity.activeInstance != null &&
				ScannerActivity.activeInstance.enabledTypes.getEan13();
	}

	@Kroll.setProperty
	public void setScanEan2(Boolean value)
	{
		if (ScannerActivity.activeInstance != null) {
			ScannerActivity.activeInstance.enabledTypes.setEan2(value);
		}
	}
	
	@Kroll.getProperty
	public Boolean getScanEan2()
	{
		return ScannerActivity.activeInstance != null &&
				ScannerActivity.activeInstance.enabledTypes.getEAN2();
	}

	@Kroll.setProperty
	public void setScanEan5(Boolean value)
	{
		if (ScannerActivity.activeInstance != null) {
			ScannerActivity.activeInstance.enabledTypes.setEan5(value);
		}
	}
	
	@Kroll.getProperty
	public Boolean getScanEan5()
	{
		return ScannerActivity.activeInstance != null &&
				ScannerActivity.activeInstance.enabledTypes.getEAN5();
	}

	@Kroll.setProperty
	public void setScanEan8(Boolean value)
	{
		if (ScannerActivity.activeInstance != null) {
			ScannerActivity.activeInstance.enabledTypes.setEan8(value);
		}
	}
	
	@Kroll.getProperty
	public Boolean getScanEan8()
	{
		return ScannerActivity.activeInstance != null &&
				ScannerActivity.activeInstance.enabledTypes.getEan8();
	}
	
	@Kroll.setProperty
	public void setScanGS1Databar(Boolean value)
	{
		if (ScannerActivity.activeInstance != null) {
			ScannerActivity.activeInstance.enabledTypes.setGS1Databar(value);
		}
	}
	
	@Kroll.getProperty
	public Boolean getScanGS1Databar()
	{
		return ScannerActivity.activeInstance != null &&
				ScannerActivity.activeInstance.enabledTypes.getGS1Databar();
	}
	
	@Kroll.setProperty
	public void setScanGS1DatabarExpanded(Boolean value)
	{
		if (ScannerActivity.activeInstance != null) {
			ScannerActivity.activeInstance.enabledTypes.setGS1DatabarExpanded(value);
		}
	}
	
	@Kroll.getProperty
	public Boolean getScanGS1DatabarExpanded()
	{
		return ScannerActivity.activeInstance != null &&
				ScannerActivity.activeInstance.enabledTypes.getGS1DatabarExpanded();
	}

	@Kroll.setProperty
	public void setScanITF(Boolean value)
	{
		if (ScannerActivity.activeInstance != null) {
			ScannerActivity.activeInstance.enabledTypes.setITF(value);
		}
	}
	
	@Kroll.getProperty
	public Boolean getScanITF()
	{
		return ScannerActivity.activeInstance != null &&
				ScannerActivity.activeInstance.enabledTypes.getITF();
	}

	@Kroll.setProperty
	public void setScanPDF417(Boolean value)
	{
		if (ScannerActivity.activeInstance != null) {
			ScannerActivity.activeInstance.enabledTypes.setPDF417(value);
		}
	}
	
	@Kroll.getProperty
	public Boolean getScanPDF417()
	{
		return ScannerActivity.activeInstance != null &&
				ScannerActivity.activeInstance.enabledTypes.getPDF417();
	}

	@Kroll.setProperty
	public void setScanQRCode(Boolean value)
	{
		if (ScannerActivity.activeInstance != null) {
			ScannerActivity.activeInstance.enabledTypes.setQRCode(value);
		}
	}
	
	@Kroll.getProperty
	public Boolean getScanQRCode()
	{
		return ScannerActivity.activeInstance != null &&
				ScannerActivity.activeInstance.enabledTypes.getQRCode();
	}

	@Kroll.setProperty
	public void setScanRSS14(Boolean value)
	{
		if (ScannerActivity.activeInstance != null) {
			ScannerActivity.activeInstance.enabledTypes.setRSS14(value);
		}
	}
	
	@Kroll.getProperty
	public Boolean getScanRSS14()
	{
		return ScannerActivity.activeInstance != null &&
				ScannerActivity.activeInstance.enabledTypes.getRSS14();
	}

	@Kroll.setProperty
	public void setScanUPCE(Boolean value)
	{
		if (ScannerActivity.activeInstance != null) {
			ScannerActivity.activeInstance.enabledTypes.setUpce(value);
		}
	}
	
	@Kroll.getProperty
	public Boolean getScanUPCE()
	{
		return ScannerActivity.activeInstance != null &&
				ScannerActivity.activeInstance.enabledTypes.getUpce();
	}

	@Kroll.setProperty
	public void setEnabledTypes(Integer value)
	{
		if (ScannerActivity.activeInstance != null) {
			ScannerActivity.activeInstance.enabledTypes.setEnabledTypes(value);
		}
	}
	
	@Kroll.getProperty
	public Integer getEnabledTypes()
	{
		if (ScannerActivity.activeInstance != null) {
			return ScannerActivity.activeInstance.enabledTypes.getEnabledTypes();
		}
		return 0;
	}
	
	@Kroll.method
	public Object[] findBarcodesInBlob() {
		// In the iOS version, this exposes the FindBarcodesInUIImage
		// method. There doesn't seem to be an equivalent in the Android
		// version of the RedLaser SDK. This is a dummy method to avoid
		// a crash in case the Ti app calls this method on the wrong platform.
		Log.w(LCAT, "findBarcodesInBlob is not available on Android.");
		return new Object[0];
	}
}