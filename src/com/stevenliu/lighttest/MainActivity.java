package com.stevenliu.lighttest;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements SurfaceHolder.Callback {

	private static final String TAG = "Light";
	private boolean isLightOn = false;
	private Button btnOnOff;
	private View viewBackground;
	private Camera camera = null;
	private Parameters p;
	private SurfaceHolder surfaceHolder = null;
	private boolean isSurfaceViewCreated = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		setContentView(R.layout.activity_main);

		// Context context = this;
//		Log.d(TAG, "getPackageManager...");
		PackageManager pm = this.getPackageManager();

		// if device support camera?
		if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
			Log.e(TAG, "Device has no camera flash!");
			return;
		}

		// Log.d(TAG, "Opening Camera");
		// camera = Camera.open();

//		Log.d(TAG, "findViews...");
		findViews();
		Log.d(TAG, "onCreate done.");
	}

	void findViews() {
		Log.d(TAG, "findViews");
		viewBackground = (View) findViewById(R.id.layoutBackground);
		viewBackground.setBackgroundColor(Color.BLACK);

		SurfaceView preview = (SurfaceView) findViewById(R.id.surfaceView);
		surfaceHolder = preview.getHolder();
//		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(this);

		btnOnOff = (Button) findViewById(R.id.btnOnOff);
		btnOnOff.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isLightOn == false) {
					turnOnLight();
				} else {
					turnOffLight();
				}
			}
		});
	}

	private void turnOnLight() {
		Log.d(TAG, "turnOnLight");
		btnOnOff.setText("OFF");
		viewBackground.setBackgroundColor(Color.WHITE);
		isLightOn = true;
		// Turn on the camera flash light
		try {
			// Log.d(TAG, "Opening Camera");
			// camera = Camera.open();
			Log.d(TAG, "camera.getParameters.");
			p = camera.getParameters();
			// p.setFlashMode(Parameters.FLASH_MODE_ON);
			p.setFlashMode(Parameters.FLASH_MODE_TORCH);
			camera.setParameters(p);
			camera.startPreview();
			Log.d(TAG, "camera.startPreview");
		} catch (RuntimeException e) {
			Log.d(TAG, "turnOnLight failed.");
			Toast.makeText(MainActivity.this, "Excepion: " + e.toString(),
					Toast.LENGTH_LONG).show();
		}
	}

	private void turnOffLight() {
		Log.d(TAG, "turnOffLight");
		btnOnOff.setText("ON");
		viewBackground.setBackgroundColor(Color.BLACK);
		isLightOn = false;
		// Turn off the camera flash light
		try {
			if (camera != null) {
				Log.d(TAG, "camera.getParameters");
				p = camera.getParameters();
				p.setFlashMode(Parameters.FLASH_MODE_OFF);
				camera.setParameters(p);
				camera.stopPreview();
				// camera.release();
				// camera = null;
				Log.d(TAG, "camera.stopPreview");
			}
		} catch (RuntimeException e) {
			Log.d(TAG, "turnOffLight failed.");
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "onResume");

//		try {
//			Log.d(TAG, "Open Camera");
//			camera = Camera.open();
//		} catch (Exception e) {
//			Log.d(TAG, "Open Camera failed");
//		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "onPause");
//		if (camera != null) {
//			Log.d(TAG, "Release Camera");
//			camera.release();
//			camera = null;
//		}
	}
	
	

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(TAG, "onStart");
		
		try {
			Log.d(TAG, "Open Camera");
			camera = Camera.open();
			
			if (isSurfaceViewCreated && camera != null) {
				Log.d(TAG, "camera.setPreviewDisplay");
				camera.setPreviewDisplay(surfaceHolder);
			}
		} catch (Exception e) {
			Log.d(TAG, "Open Camera failed");
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(TAG, "onStop");
		turnOffLight();
		if (camera != null) {
			Log.d(TAG, "Release Camera");
			camera.release();
			camera = null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(TAG, "onCreateOptionsMenu");

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.d(TAG, "surfaceCreated()");
		isSurfaceViewCreated = true;
		try {
			if (camera != null) {
				Log.d(TAG, "camera.setPreviewDisplay");
				camera.setPreviewDisplay(holder);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		Log.d(TAG, "surfaceChanged");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.d(TAG, "surfaceDestoryed");
		isSurfaceViewCreated = false;
		if (camera != null) {
			Log.d(TAG, "camera.stopPreview");
			camera.stopPreview();
		}

	}

}
