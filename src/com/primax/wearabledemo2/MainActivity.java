package com.primax.wearabledemo2;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.Date;

import com.primax.wearabledemo2.chart.BudgetPieChart;
import com.primax.wearabledemo2.chart.SalesStackedBarChart;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    // my variable below
    public static boolean isDebug = true;
// 	public static boolean isDebug = false;
 	
 // Constant
	public int mConnectionState = STATE_DISCONNECTED;
	public static final int STATE_DISCONNECTED = 0;
	public static final int STATE_CONNECTING = 1;
	public static final int STATE_CONNECTED = 2;
	public final static String ACTION_GATT_CONNECTED = "com.primax.bluetooth.le.ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED = "com.primax.bluetooth.le.ACTION_GATT_DISCONNECTED";
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.primax.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
	public final static String ACTION_DATA_AVAILABLE = "com.primax.bluetooth.le.ACTION_DATA_AVAILABLE";
	public final static String EXTRA_DATA = "com.primax.bluetooth.le.EXTRA_DATA";
     
    // UUIDs =============================================================================================
    // Descriptors
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    
    // Services
    public static String ALERT_NOTIFICATION_SERVICE = "00001811-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_ALERT_NOTIFICATION_SERVICE = UUID.fromString(ALERT_NOTIFICATION_SERVICE);
    
    public static String BATTERY_SERVICE = "0000180f-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_BATTERY_SERVICE = UUID.fromString(BATTERY_SERVICE);

    public static String BLOOD_PRESSURE = "00001810-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_BLOOD_PRESSURE = UUID.fromString(BLOOD_PRESSURE);

    public static String CURRENT_TIME_SERVICE = "00001805-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_CURRENT_TIME_SERVICE = UUID.fromString(CURRENT_TIME_SERVICE);
    
    public static String CYCLING_POWER = "00001818-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_CYCLING_POWER = UUID.fromString(CYCLING_POWER);

    public static String CYCLING_SPEED_AND_CADENCE = "00001816-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_CYCLING_SPEED_AND_CADENCE = UUID.fromString(CYCLING_SPEED_AND_CADENCE);
    
    public static String DEVICE_INFORMATION = "0000180a-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_DEVICE_INFORMATION = UUID.fromString(DEVICE_INFORMATION);
    
    public static String GENERIC_ACCESS = "00001800-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_GENERIC_ACCESS = UUID.fromString(GENERIC_ACCESS);

    public static String GENERIC_ATTRIBUTE = "00001801-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_GENERIC_ATTRIBUTE = UUID.fromString(GENERIC_ATTRIBUTE);

    public static String GLUCOSE = "00001808-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_GLUCOSE = UUID.fromString(GLUCOSE);

    public static String HEALTH_THERMOMETER = "00001809-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_HEALTH_THERMOMETER = UUID.fromString(HEALTH_THERMOMETER);

    public static String HEART_RATE = "0000180d-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_HEART_RATE = UUID.fromString(HEART_RATE);
    
    public static String HUMAN_INTERFACE_DEVICE = "00001812-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_HUMAN_INTERFACE_DEVICE = UUID.fromString(HUMAN_INTERFACE_DEVICE);
    
    public static String IMMEDIATE_ALERT = "00001802-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_IMMEDIATE_ALERT = UUID.fromString(IMMEDIATE_ALERT);

    public static String LINK_LOSS = "00001803-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_LINK_LOSS = UUID.fromString(LINK_LOSS);

    public static String LOCATION_AND_NAVIGATION = "00001819-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_LOCATION_AND_NAVIGATION = UUID.fromString(LOCATION_AND_NAVIGATION);

    public static String NEXT_DST_CHANGE_SERVICE = "00001807-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_NEXT_DST_CHANGE_SERVICE = UUID.fromString(NEXT_DST_CHANGE_SERVICE);

    public static String PHONE_ALERT_STATUS_SERVICE = "0000180E-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_PHONE_ALERT_STATUS_SERVICE = UUID.fromString(PHONE_ALERT_STATUS_SERVICE);

    public static String REFERENCE_TIME_UPDATE_SERVICE = "00001806-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_REFERENCE_TIME_UPDATE_SERVICE = UUID.fromString(REFERENCE_TIME_UPDATE_SERVICE);

    public static String RUNNING_SPEED_AND_CADENCE = "00001814-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_RUNNING_SPEED_AND_CADENCE = UUID.fromString(RUNNING_SPEED_AND_CADENCE);

    public static String SCAN_PARAMETERS = "00001813-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_SCAN_PARAMETERS = UUID.fromString(SCAN_PARAMETERS);

    public static String TX_POWER = "00001804-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_TX_POWER = UUID.fromString(TX_POWER);

    // wristband services (WBS) ****************************************************
    public static String WBS1 = "00001000-6868-6868-6868-686868686868";
    public final static UUID UUID_WBS1 = UUID.fromString(WBS1);

    public static String WBS2 = "00003000-6868-6868-6868-686868686868";
    public final static UUID UUID_WBS2 = UUID.fromString(WBS2);
    // wristband services **********************************************************
    
    // Characteristics
    public static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString(HEART_RATE_MEASUREMENT);
    
    public static String BATTERY_LEVEL = "00002a19-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_BATTERY_LEVEL = UUID.fromString(BATTERY_LEVEL);
    
    public static String DEVICE_NAME = "00002a00-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_DEVICE_NAME = UUID.fromString(DEVICE_NAME);

    public static String MODEL_NUMBER_STRING = "00002a24-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_MODEL_NUMBER_STRING = UUID.fromString(MODEL_NUMBER_STRING);
    public static String SERIAL_NUMBER_STRING = "00002a25-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_SERIAL_NUMBER_STRING = UUID.fromString(SERIAL_NUMBER_STRING);
    public static String FIRMWARE_REVISION_STRING = "00002a26-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_FIRMWARE_REVISION_STRING = UUID.fromString(FIRMWARE_REVISION_STRING);
    public static String HARDWARE_REVISION_STRING = "00002a27-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_HARDWARE_REVISION_STRING = UUID.fromString(HARDWARE_REVISION_STRING);
    public static String SOFTWARE_REVISION_STRING = "00002a28-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_SOFTWARE_REVISION_STRING = UUID.fromString(SOFTWARE_REVISION_STRING);
    public static String MANUFACTURER_NAME_STRING = "00002a29-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_MANUFACTURER_NAME_STRING = UUID.fromString(MANUFACTURER_NAME_STRING);

    public static String ARRORW_TEST = "00001525-1212-efde-1523-785feabcd123";
    public final static UUID UUID_ARRORW_TEST = UUID.fromString(ARRORW_TEST);
    public static String ARRORW_TEST2 = "00001524-1212-efde-1523-785feabcd123";
    public final static UUID UUID_ARRORW_TEST2 = UUID.fromString(ARRORW_TEST2);
    
    public static String HID_READ = "00002a4d-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_HID_READ = UUID.fromString(HID_READ);

    public static String HID_WRITE_TEST = "000018ff-0000-1000-8000-00805f9b34fb";
//    public static String HID_WRITE_TEST = "00002a4c-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_HID_WRITE_TEST = UUID.fromString(HID_WRITE_TEST);
    
    // wristband characteristics (WBC) ****************************************************
    // 2 bytes, property:N/R
    public static String WBC_PRESSURE = "00001001-6868-6868-6868-686868686868";
    public final static UUID UUID_WBC_PRESSURE = UUID.fromString(WBC_PRESSURE);
    
    // 2 bytes, property:N/R
    public static String WBC_WALK_STEP_COUNT = "00001002-6868-6868-6868-686868686868";
    public final static UUID UUID_WBC_WALK_STEP_COUNT = UUID.fromString(WBC_WALK_STEP_COUNT);

    // 2 bytes, property:N/R
    public static String WBC_RUNNING_STEP_COUNT = "00001005-6868-6868-6868-686868686868";
    public final static UUID UUID_WBC_RUNNING_STEP_COUNT = UUID.fromString(WBC_RUNNING_STEP_COUNT);
    
    // 1 byte, property:N/R
    public static String WBC_ACTIVITY_TYPE = "00001003-6868-6868-6868-686868686868";	
    public final static UUID UUID_WBC_ACTIVITY_TYPE = UUID.fromString(WBC_ACTIVITY_TYPE);
    
    // 1 byte, property:N/R
    public static String WBC_ACTIVITY_CONFIDENCE = "00001004-6868-6868-6868-686868686868";
    public final static UUID UUID_WBC_ACTIVITY_CONFIDENCE = UUID.fromString(WBC_ACTIVITY_CONFIDENCE);
    
    // 10 bytes, property:N/R
    public static String WBC_FLASH_TIME = "00003001-6868-6868-6868-686868686868";
    public final static UUID UUID_WBC_FLASH_TIME = UUID.fromString(WBC_FLASH_TIME);
    
    // 20 bytes, property:N/R
    public static String WBC_FLASH_ACTIVITY = "00003002-6868-6868-6868-686868686868";
    public final static UUID UUID_WBC_FLASH_ACTIVITY = UUID.fromString(WBC_FLASH_ACTIVITY);
    
    // 4 bytes, property:N/R
    public static String WBC_DATA_SEND_SIZE = "00003003-6868-6868-6868-686868686868";
    public final static UUID UUID_WBC_DATA_SEND_SIZE = UUID.fromString(WBC_DATA_SEND_SIZE);
    
    // 1 byte, property:W
    public static String WBC_INPUT = "00003004-6868-6868-6868-686868686868";
    public final static UUID UUID_WBC_INPUT = UUID.fromString(WBC_INPUT);
    
    // 4 bytes, property:R/W 
    public static String WBC_RW_TIMESTAMP = "00003005-6868-6868-6868-686868686868";
    public final static UUID UUID_WBC_RW_TIMESTAMP = UUID.fromString(WBC_RW_TIMESTAMP);
    // wristband characteristics **********************************************************
    
    // UUIDs =============================================================================================

	// activity
	public static final int ACTIVITY_STANDING = 1;
	public static final int ACTIVITY_RUNNING = 2;
	public static final int ACTIVITY_WALKING = 3;
	public static final int ACTIVITY_DRIVING = 4;
	public static final int ACTIVITY_BIKING = 5;
	public static final int ACTIVITY_SITTING = 6;
	public static final int ACTIVITY_SWIMMING = 7;
	public static final int ACTIVITY_SLEEPING = 128;
	
	public static final int FRAMEPAGE_MAIN = 1;
	public static final int FRAMEPAGE_HISTORY = 2;
	public static final int FRAMEPAGE_PER_MIN = 3;
	public static final int FRAMEPAGE_PER_DAY = 4;
	public static final int FRAMEPAGE_ACTIVITY_PIE = 5;
	public static final int FRAMEPAGE_DEBUG_INFO = 6;
	public static final int FRAMEPAGE_DEVICE_INFO = 7;

	// refer to http://en.wikipedia.org/wiki/Metabolic_equivalent
	// MET ( Metabolic Equivalent of Task )
	public static final double MET_STANDING = 1.0;
	public static final double MET_RUNNING = 7.0;
	public static final double MET_WALKING = 3.3;
	public static final double MET_DRIVING = 1.8;
	public static final double MET_BIKING = 4.0;
	public static final double MET_SITTING = 1.0;
	public static final double MET_SWIMMING = 6.0;
	public static final double MET_SLEEPING = 0.9;
    
	public static final long SCAN_PERIOD = 5000;
	public static final long CHECK_NOTIFICATION_PERIOD = 10000;
	public static final long CALORIES_CALCULATE_PERIOD = 1000;
	public static final long HISTORY_RECORD_PERIOD = 60000;
	public static final long RECONNECT_PERIOD = 30000;
	public static final long WAIT_TIME = 200;
	public final static int REQUEST_ENABLE_BT = 1;
	
 	public BluetoothManager mBluetoothManager;
	public BluetoothAdapter mBluetoothAdapter;
	public ArrayList<BluetoothDevice> mLeDevices;
	public BluetoothDevice mLastConnectedLeDevice;
	public BluetoothDevice mSelectedLeDevice;
	public BluetoothGatt mBluetoothGatt;
	public List<BluetoothGattService> mBluetoothGattServices;
	public BluetoothGattService mBluetoothGattService;
	public List<BluetoothGattCharacteristic> mBluetoothGattCharacteristics;
	public BluetoothGattCharacteristic mBluetoothGattCharacteristic;
	public BluetoothGattCharacteristic mGattCharacteristics;
	public Handler mHandler;
	
	public static MyDatabase myDB = null;
	public static SQLiteDatabase mDB = null;
	public static Cursor mCursor = null;
	
    public BluetoothGattCharacteristic mCharacteristicHidWriteTest;
    public BluetoothGattCharacteristic mCharacteristicBatteryLevel;
    public BluetoothGattCharacteristic mCharacteristicDeviceName;
    public BluetoothGattCharacteristic mCharacteristicModelNumber;
    public BluetoothGattCharacteristic mCharacteristicSerialNumber;
    public BluetoothGattCharacteristic mCharacteristicFWRevision;
    public BluetoothGattCharacteristic mCharacteristicHWRevision;
    public BluetoothGattCharacteristic mCharacteristicSWRevision;
    public BluetoothGattCharacteristic mCharacteristicManufacturerName;

    public BluetoothGattCharacteristic mCharacteristicWBCPressure;
    public BluetoothGattCharacteristic mCharacteristicWBCWalkStepCount;
    public BluetoothGattCharacteristic mCharacteristicWBCRunningStepCount;
    public BluetoothGattCharacteristic mCharacteristicWBCActivityType;
    public BluetoothGattCharacteristic mCharacteristicWBCActivityConfidence;
    public BluetoothGattCharacteristic mCharacteristicWBCFlashTime;
    public BluetoothGattCharacteristic mCharacteristicWBCFlashActivity;
    public BluetoothGattCharacteristic mCharacteristicWBCDataSendSize;
    public BluetoothGattCharacteristic mCharacteristicWBCInput;
    public BluetoothGattCharacteristic mCharacteristicWBCRWTimestamp;
    
	public static String mDeviceNameString;
	public static String mModelNumberString;
	public static String mSerialNumberString;
	public static String mFWRevisionString;
	public static String mHWRevisionString;
	public static String mSWRevisionString;
	public static String mManufacturerNameString;
	public static String mConnectStatusString;
	public static int mBatteryLevelInt;
	public static String mEnumInfoString;
	public static String mDeviceInfoString;
	public static String mTempString;
	public static String mActivityString;
	public static String mActivityString2;
	// auto re-connect
	public static String lastConnectedBDA; 		// bluetooth device address

	public static int mWBCPressureInt;
	public static int mWBCWalkStepCount;
	public static int mWBCRunningStepCount;
	public static int prWalkStepCount;			// previousRecodCalories 
	public static int prRunningStepCount;		// previousRecodCalories 
	public static int mSleepQuality;
	public static int mWBCActivityType;
	public static int mWBCActivityConfidence;
	public static int mWBCFlashTime;
	public static int mWBCFlashActivity;
	public static int mWBCDataSendSize;
	public static boolean mWBCInput;
	public static int mWBCRWTimestamp;
	
	public static PlaceholderFragment fragment;
	public static PlaceholderFragment fragmentMain;
	public static PlaceholderFragment fragmentHistory;
	public static PlaceholderFragment fragmentPerDay;
	public static PlaceholderFragment fragmentPerMin;
	public static PlaceholderFragment fragmentActivityPie;
	public static PlaceholderFragment fragmentDebugInfo;
	public static int fragmentPreviousPage;
	
	public static View rootView;
	public static WebView wv_heartrate;
	public static TextView tv_heartrate;
	public static int drawerItemSelectedIndex;
	
	public static double startTime; 
	public static double totalTime; 
	public static double currentActivityMET; 
	public static double calories_standing; 
	public static double calories_running; 
	public static double calories_walking; 
	public static double calories_driving; 
	public static double calories_biking; 
	public static double calories_sitting; 
	public static double calories_swimming; 
	public static double calories_sleeping; 
	public static double calories_total; 
	public static double prCalories;			// previousRecodCalories 
	
	public static boolean isNotifyWBCPressureSet = false;	
	public static boolean isNotifyWBCWalkStepCountSet = false;	
	public static boolean isNotifyWBCRunningkStepCountSet = false;	
	public static boolean isNotifyWBCActivityType = false;	
	public static boolean isNotifyWBCActivityConfidence = false;
	public static boolean isWriteWBCWriteTimestamp = false;
	
	public static boolean isUserDisconnect = false;	
	public static boolean isDeviceConnect = false;	
	public static boolean isPause = false;	
	public static boolean isStopThread = false;	
	public static boolean isSetTimestamp = false;	
	
	// for drawing 
	public final static int TOTAL_ACTIVITY_NUMBER = 8;
	public final static int MAX_ARRAY_SIZE = 100;
	public static double[] draw_x = new double[MAX_ARRAY_SIZE];
	public static double[] draw_y_walking = new double[MAX_ARRAY_SIZE];
	public static double[] draw_y_running = new double[MAX_ARRAY_SIZE];
	public static double[] draw_y_calories = new double[MAX_ARRAY_SIZE];
	public static double[] draw_y4 = new double[MAX_ARRAY_SIZE];
	public static double draw_xMax = 0.0;
	public static double draw_yMax_stepCount = 0.0;
	public static double draw_yMax_walkStepCount = 0.0;
	public static double draw_yMax_runningStepCount = 0.0;
	public static double draw_yMax_calories = 0.0;
	public static double[] draw_pie = new double[TOTAL_ACTIVITY_NUMBER];;
	public static double[] draw_pie2 = new double[TOTAL_ACTIVITY_NUMBER];;
	
	public final static int MAX_DAY_TO_DRAW = 10;
	public static String draw_x_dateArray[] = new String[MAX_DAY_TO_DRAW];
	public static double draw_y_calories_perday[] = new double[MAX_DAY_TO_DRAW];
	public static double draw_y_walking_perday[] = new double[MAX_DAY_TO_DRAW];
	public static double draw_y_running_perday[] = new double[MAX_DAY_TO_DRAW];
	
	public static FragmentManager fragmentManager;
	public static  android.app.FragmentTransaction fragmentTransaction;
	
	// ProgressDialog
	public ProgressDialog mProgressDialog;
	
	public boolean mScanning;

	// SharedPreferences instance
	public static SharedPreferences sharedPrefs;
	public static SharedPreferences.Editor sharedPrefsEditor;
	public static int retryTimes;
    public static int userHeight;
    public static int userWeight;
    public static int userGender;
	
	public void resetVariables() {

		isNotifyWBCPressureSet = false;
		isNotifyWBCWalkStepCountSet = false;
		isNotifyWBCRunningkStepCountSet = false;
		isNotifyWBCActivityType = false;
		isNotifyWBCActivityConfidence = false;
		isWriteWBCWriteTimestamp = false;

		mCharacteristicWBCPressure = null;
		mCharacteristicWBCWalkStepCount = null;
		mCharacteristicWBCRunningStepCount = null;
		mCharacteristicWBCActivityType = null;
		mCharacteristicWBCActivityConfidence = null;
		mCharacteristicWBCFlashTime = null;
		mCharacteristicWBCFlashActivity = null;
		mCharacteristicWBCDataSendSize = null;
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (isDebug) {
			Log.e("gray", "onCreate, " + "");
		}

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        
        // initial variables
 		mHandler = new Handler();
 		mLeDevices = new ArrayList<BluetoothDevice>();
 		startTime = (double)System.currentTimeMillis();
 		totalTime = 0.0;

 		isUserDisconnect = false;	
 		isDeviceConnect = false;
 		isStopThread = false;
 		isPause = false;	
 		retryTimes = 0;
 		prWalkStepCount = 0;
 		prRunningStepCount = 0;
 		calories_standing = 0.0; 
 		calories_running = 0.0; 
 		calories_walking = 0.0; 
 		calories_driving = 0.0; 
 		calories_biking = 0.0; 
 		calories_sitting = 0.0; 
 		calories_swimming = 0.0; 
 		calories_sleeping = 0.0; 
 		calories_total = 0.0;
 		prCalories = 0.0;
 		
 		Calendar calendar = Calendar.getInstance(); 
 		DatePickerFragment.pickedYear = calendar.get(Calendar.YEAR);
 		DatePickerFragment.pickedMonth = calendar.get(Calendar.MONTH);
 		DatePickerFragment.pickedDay = calendar.get(Calendar.DAY_OF_MONTH);
 		
		// open database
		openDB();
		mDB = myDB.getWritableDatabase();
		getCalories();
		
 		// get SharedPreferences instance
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		sharedPrefsEditor = sharedPrefs.edit();  
		lastConnectedBDA = sharedPrefs.getString("pref_last_connected_BDA", "none");
		userWeight = Integer.valueOf(sharedPrefs.getString("pref_userWeight", "70"));
		userGender = Integer.valueOf( sharedPrefs.getString("pref_userGender", "0") );
		
 		IntentFilter filter = new IntentFilter();
 		filter.addAction(ACTION_GATT_CONNECTED);
 		filter.addAction(ACTION_GATT_DISCONNECTED);
 		filter.addAction(ACTION_GATT_SERVICES_DISCOVERED);
 		filter.addAction(ACTION_DATA_AVAILABLE);
 		filter.addAction(EXTRA_DATA);
 		registerReceiver(mGattUpdateReceiver, filter);
 		
     	// Initializes Bluetooth adapter.
		mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = mBluetoothManager.getAdapter();
		
		// Ensures Bluetooth is available on the device and it is enabled. If not,
		// displays a dialog requesting user permission to enable Bluetooth.
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		
		// try to reconnect last linked device thread
		reconnectThread();
		
		// check notification thread
		checkNotificationThread();
		
		// history record thread
		historyRecordThread();
		
		// calculate time & calories thread
		mySleep(3000);
		caloriesCalculateThread();
		
    }

    public void caloriesCalculateThread(){
    	
    	new Thread(new Runnable() 
		{ 
			@Override
			public void run() 
			{ 
				try {
		            while(true) {
		            	
		            	java.lang.Thread.sleep(CALORIES_CALCULATE_PERIOD);
		                
		            	if (!isDeviceConnect) {
							continue;
						}
		            	
		            	if (isStopThread) {
		            		break;
						}
		            	
		            	if (isDebug) {
							Log.e("gray", "caloriesCalculateThread");
						}
		            	
		            	mActivityString = getCurrentAvtivity(mWBCActivityType);

		            	totalTime = System.currentTimeMillis() - startTime;
		            	mActivityString2 = "Total Time : " + (int)(totalTime/1000) + " sec<br>";
		    			
		    			// count calories
		    			// http://www.calories-calculator.net/Calculator_Formulars.html
		            	double factor = 0.0;
		            	if (userGender == 0) {		// male
		            		factor = 2.2;
						} else {					// female
							factor = 1.0;
						}
		            	if (currentActivityMET == MET_STANDING) {
		            		calories_standing += factor * currentActivityMET * (double)userWeight * (CALORIES_CALCULATE_PERIOD/1000.0/60.0/60.0);
		            		mActivityString2 += "<br>Calories Burned : " + String.format("%-12.2f", calories_standing) + " kcal<br>";
						} else if (currentActivityMET == MET_RUNNING) {
							calories_running += factor * currentActivityMET * (double)userWeight * (CALORIES_CALCULATE_PERIOD/1000.0/60.0/60.0);
							mActivityString2 += "<br>Calories Burned : " + String.format("%-12.2f", calories_running) + " kcal<br>";
						} else if (currentActivityMET == MET_WALKING) {
							calories_walking += factor * currentActivityMET * (double)userWeight * (CALORIES_CALCULATE_PERIOD/1000.0/60.0/60.0);
							mActivityString2 += "<br>Calories Burned : " + String.format("%-12.2f", calories_walking) + " kcal<br>";
						} else if (currentActivityMET == MET_DRIVING) {
							calories_driving += factor * currentActivityMET * (double)userWeight * (CALORIES_CALCULATE_PERIOD/1000.0/60.0/60.0);
							mActivityString2 += "<br>Calories Burned : " + String.format("%-12.2f", calories_driving) + " kcal<br>";
						} else if (currentActivityMET == MET_BIKING) {
							calories_biking += factor * currentActivityMET * (double)userWeight * (CALORIES_CALCULATE_PERIOD/1000.0/60.0/60.0);
							mActivityString2 += "<br>Calories Burned : " + String.format("%-12.2f", calories_biking) + " kcal<br>";
						} else if (currentActivityMET == MET_SITTING) {
							calories_sitting += factor * currentActivityMET * (double)userWeight * (CALORIES_CALCULATE_PERIOD/1000.0/60.0/60.0);
							mActivityString2 += "<br>Calories Burned : " + String.format("%-12.2f", calories_sitting) + " kcal<br>";
						} else if (currentActivityMET == MET_SWIMMING) {
							calories_swimming += factor * currentActivityMET * (double)userWeight * (CALORIES_CALCULATE_PERIOD/1000.0/60.0/60.0);
							mActivityString2 += "<br>Calories Burned : " + String.format("%-12.2f", calories_swimming) + " kcal<br>";
						} else if (currentActivityMET == MET_SLEEPING) {
							calories_sleeping += factor * currentActivityMET * (double)userWeight * (CALORIES_CALCULATE_PERIOD/1000.0/60.0/60.0);
							mActivityString2 += "<br>Calories Burned : " + String.format("%-12.2f", calories_sleeping) + " kcal<br>";
						}
		            	
		            	calories_total = calories_standing + calories_running + calories_walking + calories_driving + calories_biking 
		            					+ calories_sitting + calories_swimming + calories_sleeping;
		    			
		    			mActivityString2 += "<br>Total Calories : " + String.format("%-12.2f", calories_total) + " kcal<br>";
		    			
		    			if (mWBCActivityType == ACTIVITY_WALKING) {
		    				mActivityString2 += "<br>Walk Step Count : " + mWBCWalkStepCount + "<br>";
		    			}
		    			if (mWBCActivityType == ACTIVITY_RUNNING) {
		    				mActivityString2 += "<br>Running Step Count : " + mWBCRunningStepCount + "<br>";
		    			}
		    			if ((mWBCActivityType & ACTIVITY_SLEEPING) == ACTIVITY_SLEEPING) {
		    				mActivityString2 += "Sleep Quality : " + mSleepQuality + "(smaller is better)";
						}
		    			
		    			mTempString = "<br>";
//		    			mTempString += "Pressure            : " + mWBCPressureInt + "<br><br>";
		    			mTempString += "Connection counter  : " + mWBCPressureInt + "<br><br>";
		    			mTempString += "Walk Step Count     : " + mWBCWalkStepCount + "<br><br>";
		    			mTempString += "Running Step Count  : " + mWBCRunningStepCount + "<br><br>";
		    			mTempString += "Activity Type       : " + mWBCActivityType + " : " + getCurrentAvtivity(mWBCActivityType) + "<br><br>";
		    			mTempString += "Activity Confidence : " + mWBCActivityConfidence + "<br><br>";
//		    			mTempString += "Flash Time          : " + mWBCFlashTime + "<br><br>";
//		    			mTempString += "Flash Activity      : " + mWBCFlashActivity + "<br><br>";
//		    			mTempString += "Data Send Size      : " + mWBCDataSendSize + "<br><br>";

		    			// update UI
			        	if (fragmentMain != null && drawerItemSelectedIndex == FRAMEPAGE_MAIN && isPause != true && isDeviceConnect == true) {
			        		try {
			        			fragmentManager = getFragmentManager();
			        			fragmentTransaction = fragmentManager.beginTransaction();
			        			fragmentTransaction.detach(fragmentMain);
			        			fragmentTransaction.attach(fragmentMain);
			        			fragmentTransaction.commit();
								
							} catch (Exception e) {
								Log.e("gray", "Exception 1: " + e.toString());
							}
			        	}
			        	if (fragmentDebugInfo != null && drawerItemSelectedIndex == FRAMEPAGE_DEBUG_INFO && isPause != true && isDeviceConnect == true) {
			        		try {
			        			fragmentManager = getFragmentManager();
			        			fragmentTransaction = fragmentManager.beginTransaction();
			        			fragmentTransaction.detach(fragmentDebugInfo);
			        			fragmentTransaction.attach(fragmentDebugInfo);
			        			fragmentTransaction.commit();
								
							} catch (Exception e) {
								Log.e("gray", "Exception 2: " + e.toString());
							}
						}
			        	if (fragmentActivityPie != null && drawerItemSelectedIndex == FRAMEPAGE_ACTIVITY_PIE && isPause != true && isDeviceConnect == true && DatePickerFragment.isReDrawPie == true) {
			        		try {
			        			fragmentManager = getFragmentManager();
			        			fragmentTransaction = fragmentManager.beginTransaction();
			        			fragmentTransaction.detach(fragmentActivityPie);
			        			fragmentTransaction.attach(fragmentActivityPie);
			        			fragmentTransaction.commit();
			        			DatePickerFragment.isReDrawPie = false;
								
							} catch (Exception e) {
								Log.e("gray", "Exception 3: " + e.toString());
							}
						}
			        	
		            }
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
				if (isDebug) {
					Log.e("gray", "exit caloriesCalculateThread");
				}
			} 
		}).start();
    }
    
    public void historyRecordThread(){
    	
    	new Thread(new Runnable() 
		{ 
			@Override
			public void run() 
			{ 
				try {
		            while(true) {
		            	
		            	java.lang.Thread.sleep(HISTORY_RECORD_PERIOD);			
		            	
		            	if (!isDeviceConnect) {
		                	continue;
						}
		            	
		            	if (isStopThread) {
							break;
						}
		            	
		            	if (isDebug) {
							Log.e("gray", "historyRecordThread");
						}
		            	// record data to database
		                recordHistoryData();
		                
		                // update UI
			        	if (fragmentPerMin != null && drawerItemSelectedIndex == FRAMEPAGE_PER_MIN && isPause != true) {
			        		try {
			        			fragmentManager = getFragmentManager();
			        			fragmentTransaction = fragmentManager.beginTransaction();
			        			fragmentTransaction.detach(fragmentPerMin);
			        			fragmentTransaction.attach(fragmentPerMin);
			        			fragmentTransaction.commit();
								
							} catch (Exception e) {
								Log.e("gray", "Exception : " + e.toString());
							}
						}
		            }
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
				
				if (isDebug) {
					Log.e("gray", "exit historyRecordThread");
				}
			} 
		}).start();
    }
    
    public void checkNotificationThread(){
		new Thread(new Runnable() 
		{ 
			@Override
			public void run() 
			{ 
				try {
		            while(true) {
		            	
		                java.lang.Thread.sleep(CHECK_NOTIFICATION_PERIOD);
		                
		                if (!isDeviceConnect || (isNotifyWBCPressureSet && isNotifyWBCWalkStepCountSet && isNotifyWBCRunningkStepCountSet && isNotifyWBCActivityType && isNotifyWBCActivityConfidence) ) {
		                	continue;
						}
		                
		                if (isStopThread ) {
		                	// finish thread
		                	break;
		                }

		                if (isDebug) {
							Log.e("gray", "checkNotificationThread");
						}
		                
		                // check notification 
		                if (isNotifyWBCPressureSet != true) {
		                	mySleep(WAIT_TIME);
	                		setNotification(mCharacteristicWBCPressure, true);
	                		Log.e("gray", "set notification, mCharacteristicWBCPressure");
						}
		                if (isNotifyWBCWalkStepCountSet != true) {
		                	mySleep(WAIT_TIME);
		                	setNotification(mCharacteristicWBCWalkStepCount, true);
	                		Log.e("gray", "set notification, mCharacteristicWBCWalkStepCount");
						}
		                if (isNotifyWBCRunningkStepCountSet != true) {
		                	mySleep(WAIT_TIME);
	                		setNotification(mCharacteristicWBCRunningStepCount, true);
	                		Log.e("gray", "set notification, mCharacteristicWBCRunningStepCount");
						}
		                if (isNotifyWBCActivityType != true) {
		                	mySleep(WAIT_TIME);
	                		setNotification(mCharacteristicWBCActivityType, true);
	                		Log.e("gray", "set notification, mCharacteristicWBCActivityType");
						}
		                if (isNotifyWBCActivityConfidence != true) {
		                	mySleep(WAIT_TIME);
		                	setNotification(mCharacteristicWBCActivityConfidence, true);
		                	Log.e("gray", "set notification, mCharacteristicWBCActivityConfidence");
		                }
//		                if (isWriteWBCWriteTimestamp != true) {
//		                	mySleep(WAIT_TIME);
//	                		Log.e("gray", "write Characteristic, mCharacteristicWBCRWTimestamp");
//						}
		            }
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
				if (isDebug) {
					Log.e("gray", "exst checkNotificationThread");
				}
			}
		}).start();
    }
    
    public void reconnectThread(){
    	
		// try to reconnect last linked device thread
		if (isDebug) {
			Log.e("gray", "reconnectThreadStart, lastConnectedBDA:" + lastConnectedBDA);
		}
		
		if (!lastConnectedBDA.equalsIgnoreCase("none")) {
			
			mLastConnectedLeDevice = mBluetoothAdapter.getRemoteDevice(lastConnectedBDA);
			new Thread(new Runnable() 
			{ 
				@Override
				public void run()
				{
					// will retry within 1 min
					while (retryTimes < 3 && !isDeviceConnect) {
						
						if (isStopThread) {
							break;
						}
						try {
							Log.e("gray", "reconnectThread, retryTimes:" + retryTimes);
							mBluetoothGatt = mLastConnectedLeDevice.connectGatt(MainActivity.this, false, mGattCallback);
							retryTimes++;
							java.lang.Thread.sleep(RECONNECT_PERIOD);
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					if (isDebug) {
						Log.e("gray", "reconnectThread exist.");
					}
				} 
			}).start();
		}
    }
    
    @Override
    public void onNavigationDrawerItemSelected(int position) {
    	
    	if (isDebug) {
			Log.e("gray", "onNavigationDrawerItemSelected, position" + position);
		}
    	
    	drawerItemSelectedIndex = position+1;
    	
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
    	
    	if (isDebug) {
			Log.e("gray", "onSectionAttached, " + "");
		}
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
            	mTitle = getString(R.string.title_section3);
            	break;
            case 4:
            	mTitle = getString(R.string.title_section4);
            	break;
            case 5:
            	mTitle = getString(R.string.title_section5);
            	break;
            case 6:
            	mTitle = getString(R.string.title_section6);
            	break;
            case 7:
                mTitle = getString(R.string.title_section7);
                break;
        }
    }

    public void restoreActionBar() {
    	if (isDebug) {
			Log.e("gray", "restoreActionBar, " + "");
		}
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	if (isDebug) {
			Log.e("gray", "onCreateOptionsMenu, " + "");
		}
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        
        switch (item.getItemId()) {
        
        case R.id.action_settings:
        	if (isDebug) {
        		Log.e("gray", "onOptionsItemSelected, case R.id.action_settings");
        	}
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, 0);
            break;
            
        case R.id.action_scan_connect:
        	if (isDebug) {
        		Log.e("gray", "onOptionsItemSelected, case R.id.action_scan_connect");
        	}
        	
        	scanLeDevice(true);
            
            break;
            
        case R.id.action_disconnect:
        	if (isDebug) {
        		Log.e("gray", "onOptionsItemSelected, case R.id.action_disconnect");
        	}
        	
        	isUserDisconnect = true;
        	mLeDevices.clear();
        	// set notification
            disableNotification();
        	disconnect();
        	close();
        	resetVariables();
            
        	// refresh pages
        	if (fragmentMain != null && drawerItemSelectedIndex == FRAMEPAGE_MAIN && isPause != true) {
        		try {
        			fragmentManager = getFragmentManager();
        			fragmentTransaction = fragmentManager.beginTransaction();
        			fragmentTransaction.detach(fragmentMain);
        			fragmentTransaction.attach(fragmentMain);
        			fragmentTransaction.commit();
					
				} catch (Exception e) {
					Log.e("gray", "Exception : " + e.toString());
				}
        	}
        	
            break;
            
//		case R.id.action_test:
//        	if (isDebug) {
//        		Log.e("gray", "onOptionsItemSelected, case R.id.action_test");
//        	}
//        	
//        	reNewDB();
//        	
////        	if (mBluetoothGatt == null) {
////        		return false;
////        	}
//            
////        	writeWBCTimestamp();
//        	
//        	break;
        	
//		case R.id.action_read:
//        	if (isDebug) {
//        		Log.e("gray", "onOptionsItemSelected, case R.id.action_read");
//        	}
//        	
//        	if (mBluetoothGatt == null) {
//				return false;
//			}
//        	
////        	boolean res = mBluetoothGatt.readCharacteristic(mCharacteristicWBCPressure);
////        	if (isDebug) {
////				Log.e("gray", "onOptionsItemSelected, read result:" + res);
////			}
//        	
//        	// set notification
//        	enableNotification();        	
//            break;
            
		case R.id.action_about:
        	if (isDebug) {
        		Log.e("gray", "onOptionsItemSelected, case R.id.action_about");
        	}
        	
        	TextView tv  = new TextView(this);
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            tv.setText(Html.fromHtml(
            		"* About <b><a href='http://www.primax.com.tw/en/Index.aspx'>Primax</a></b>" + 
            		"<br><br>" +
            		"* About <b><a href='http://www.calories-calculator.net/Calculator_Formulars.html'>Calories calculation</a></b>" +
            		"<br><br>" +
            		"* Version : 1.0.0" +
            		"<br>"
            		)); 
            tv.setTextSize(18);
            tv.setPadding(40, 40, 40, 40);
            new AlertDialog.Builder(MainActivity.this)
            .setTitle("About")
            .setView(tv)
            .show();
        	
        	break;
        	
		default:
			break;
		}
            
        return super.onOptionsItemSelected(item);
    }
    
	// BLE scan
	public void scanLeDevice(final boolean enable) {
		if (isDebug) {
			Log.e("gray", "scanLeDevice, " + "");
		}
        if (enable) {
        	mLeDevices.clear();
        	
        	// Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    if (isDebug) {
						Log.e("gray", "stopLeScan" + "");
					}
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    
                    mProgressDialog.dismiss();
					mProgressDialog = null;
					
					String[] devicesList = new String[5];
					for (int i = 0; i < devicesList.length; i++) {
						devicesList[i] = "****";
					}
					for (int i = 0; i < mLeDevices.size() && i < 5; i++) {
						devicesList[i] = mLeDevices.get(i).getName() + " ( " + mLeDevices.get(i).getAddress() + " ) ";	
						if (isDebug) {
							Log.e("gray", "devicesList:" + devicesList[i]);
						}
					}
				    
					new AlertDialog.Builder(MainActivity.this)
							.setTitle("Choose BLE Device")
							.setItems(devicesList,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog,int which) {

											if (isDebug) {
												Log.e("gray", "which:" + which);
											}
											if (which > mLeDevices.size()-1) {
												showAlertDialog("Error","Wrong device to connect!");
												return;
											}
											mSelectedLeDevice = mLeDevices.get(which);
											mBluetoothGatt = mSelectedLeDevice.connectGatt(MainActivity.this, false, mGattCallback);
											sharedPrefsEditor.putString("pref_last_connected_BDA", mSelectedLeDevice.toString());
											sharedPrefsEditor.commit();
										}
									}
							).show();
					
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mLeDevices.clear();
            if (isDebug) {
				Log.e("gray", "startLeScan" + "");
			}
            showProcessDialog("Please Wait...", "Scaning devices...");
            
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
	}
	
	// Device scan callback.
	private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
		
	    @Override
	    public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
	        runOnUiThread(new Runnable() {
	           @Override
	           public void run() {
	        	   if (isDebug) {
	        		   Log.e("gray", "LeScanCallback, BluetoothDevice:" + device.toString());
	        	   }
	        	   if(!mLeDevices.contains(device)) {
						if (isDebug) {
							Log.e("gray", "add LE device"	+ "");
						}
						mLeDevices.add(device);
	               } else {
	            	   	Log.e("gray", "device already exist!");
	               }
	           }
	       });
	   }
	};
	
	// weight, unit >> kg
	// timePeriod, unit >> second
	public double calculateCalories(int weight, int timePeriod, double met) {
		double calories = 0.0;
		
		calories = weight * met * timePeriod;
		
		return calories;
	}
	
	public static boolean isCharacteristicWriteable(BluetoothGattCharacteristic pChar) {
	    return (pChar.getProperties() & (BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) != 0;
	}
	
	public static boolean isCharacterisitcReadable(BluetoothGattCharacteristic pChar) {
	    return ((pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) != 0);
	}
	
	public boolean isCharacterisiticNotifiable(BluetoothGattCharacteristic pChar) {
	    return (pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;
	}
	
	public void showProperty(BluetoothGattCharacteristic pChar) {
		
		mEnumInfoString += "Property : ";
		
		if ( (pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_BROADCAST) != 0) {
			mEnumInfoString += "Broadcast. ";
		}
		if ( (pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS) != 0) {
			mEnumInfoString += "Extended_props. ";
		}
		if ( (pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0) {
			mEnumInfoString += "Indicate. ";
		}
		if ( (pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
			mEnumInfoString += "Notify. ";
		}
		if ( (pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) != 0) {
			mEnumInfoString += "Read. ";
		}
		if ( (pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE) != 0) {
			mEnumInfoString += "Signed_write. ";
		}
		if ( (pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_WRITE) != 0) {
			mEnumInfoString += "Write. ";
		}
		if ( (pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) != 0) {
			mEnumInfoString += "Write_no_response. ";
		}
		mEnumInfoString += "\n";
	}
		    
	public void checkSpecServices(BluetoothGattService pService) {
		
		if ( UUID_ALERT_NOTIFICATION_SERVICE.equals(pService.getUuid())) {
			mEnumInfoString += "<b>Alert Notification Service</b>\n";
		}
		else if ( UUID_BATTERY_SERVICE.equals(pService.getUuid())) {
			mEnumInfoString += "<b>Battery Service</b>\n";
		}
		else if ( UUID_BLOOD_PRESSURE.equals(pService.getUuid())) {
			mEnumInfoString += "<b>Blood Pressure Service</b>\n";
		}
		else if ( UUID_CURRENT_TIME_SERVICE.equals(pService.getUuid())) {
			mEnumInfoString += "<b>Current Time Service Service</b>\n";
		}
		else if ( UUID_CYCLING_POWER.equals(pService.getUuid())) {
			mEnumInfoString += "<b>Cycling Power Service</b>\n";
		}
		else if ( UUID_CYCLING_SPEED_AND_CADENCE.equals(pService.getUuid())) {
			mEnumInfoString += "<b>Cycling Speed And Cadence Service</b>\n";
		}
		else if ( UUID_DEVICE_INFORMATION.equals(pService.getUuid())) {
			mEnumInfoString += "<b>Device Information Service</b>\n";
		}
		else if ( UUID_GENERIC_ACCESS.equals(pService.getUuid())) {
			mEnumInfoString += "<b>Generic Access Service</b>\n";
		}
		else if ( UUID_GENERIC_ATTRIBUTE.equals(pService.getUuid())) {
			mEnumInfoString += "<b>Generic Attribute Service</b>\n";
		}
		else if ( UUID_GLUCOSE.equals(pService.getUuid())) {
			mEnumInfoString += "<b>Glucose Service</b>\n";
		}
		else if ( UUID_HEALTH_THERMOMETER.equals(pService.getUuid())) {
			mEnumInfoString += "<b>Health Thermometer Service</b>\n";
		}
		else if ( UUID_HEART_RATE.equals(pService.getUuid())) {
			mEnumInfoString += "<b>Heart Rate Service</b>\n";
		}
		else if ( UUID_HUMAN_INTERFACE_DEVICE.equals(pService.getUuid())) {
			mEnumInfoString += "<b>Human Interface Device Service</b>\n";
		}
		else if ( UUID_IMMEDIATE_ALERT.equals(pService.getUuid())) {
			mEnumInfoString += "<b>Immediate Alert Service</b>\n";
		}
		else if ( UUID_LINK_LOSS.equals(pService.getUuid())) {
			mEnumInfoString += "<b>Link Loss Service</b>\n";
		}
		else if ( UUID_LOCATION_AND_NAVIGATION.equals(pService.getUuid())) {
			mEnumInfoString += "<b>Location And Navigation Service</b>\n";
		}
		else if ( UUID_NEXT_DST_CHANGE_SERVICE.equals(pService.getUuid())) {
			mEnumInfoString += "<b>Next Dst Change Service</b>\n";
		}
		else if ( UUID_PHONE_ALERT_STATUS_SERVICE.equals(pService.getUuid())) {
			mEnumInfoString += "<b>Phone Alert Status Service</b>\n";
		}
		else if ( UUID_REFERENCE_TIME_UPDATE_SERVICE.equals(pService.getUuid())) {
			mEnumInfoString += "<b>Reference Time Update Service</b>\n";
		}
		else if ( UUID_RUNNING_SPEED_AND_CADENCE.equals(pService.getUuid())) {
			mEnumInfoString += "<b>Running Speed And Cadence Service</b>\n";
		}
		else if ( UUID_SCAN_PARAMETERS.equals(pService.getUuid())) {
			mEnumInfoString += "<b>Scan Parameters Service</b>\n";
		}
		else if ( UUID_TX_POWER.equals(pService.getUuid())) {
			mEnumInfoString += "<b>TX Power Service</b>\n";
		}
		else {
			mEnumInfoString += "<b>Unknown Service</b>\n";
		}
	}
	
	public void showAlertDialog(String title, String message) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
	}
	
	// set Activity's MET & return Activity String
	public String getCurrentAvtivity(int activity){
		
		String s;
		
		if (activity == ACTIVITY_STANDING) {
			s = "Standing";
			currentActivityMET = MET_STANDING; 
		} else if (activity == ACTIVITY_WALKING) {
			s = "Walking";
			currentActivityMET = MET_WALKING;
		} else if (activity == ACTIVITY_RUNNING) {
			s = "Running";
			currentActivityMET = MET_RUNNING;
		} else if (activity == ACTIVITY_DRIVING) {
			s = "Driving";
			currentActivityMET = MET_DRIVING;
		} else if (activity == ACTIVITY_BIKING) {
			s = "Biking";
			currentActivityMET = MET_BIKING;
		} else if (activity == ACTIVITY_SITTING) {
			s = "Sitting";
			currentActivityMET = MET_SITTING;
		} else if (activity == ACTIVITY_SWIMMING) {
			s = "Swimming";
			currentActivityMET = MET_SWIMMING;
		} else if ( ( activity & ACTIVITY_SLEEPING) ==  ACTIVITY_SLEEPING) {
			s = "Sleeping";
			currentActivityMET = MET_SLEEPING;
			mSleepQuality = activity - ACTIVITY_SLEEPING;
		} else {
			s = "unknow";
		}
		
		return s;
	}
	
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }
    
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e("gray", "disconnect");
            return;
        }
        mBluetoothGatt.disconnect();
        
    }
    
	protected void onPause() {
        super.onPause();
        isPause = true;
        if (isDebug) {
			Log.e("gray", "onPause");	
		}
	}
	
	protected void onResume() {
		super.onResume();
		isPause = false;
		if (isDebug) {
			Log.e("gray", "onResume");	
		}
	}
    
    @Override
	public void onDestroy() {
		
		if (isDebug) {
			Log.e("gray", "onDestroy");	
		}
		
		if (mCursor != null) {
			mCursor.close();
			mCursor = null;
		}
		closeDB();
		
		unregisterReceiver(mGattUpdateReceiver);
		isDeviceConnect = false;
		isUserDisconnect = true;
		isStopThread = true;
		resetVariables();
    	mLeDevices.clear();
    	// set notification
        disableNotification();
    	disconnect();
    	close();
    	
		super.onDestroy();
	}
    
	// Various callback methods defined by the BLE API.
    private final BluetoothGattCallback mGattCallback =
            new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                int newState) {
        	if (isDebug) {
				Log.e("gray", "onConnectionStateChange, " + "");
			}
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
            	mConnectStatusString = "Connected";
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                if (isDebug) {
                	Log.e("gray", "Connected to GATT server." + "");
					Log.e("gray", "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());
				}
                
                // refresh pages
                if (fragmentMain != null && drawerItemSelectedIndex == FRAMEPAGE_MAIN && isPause != true) {
                	try {
            			fragmentManager = getFragmentManager();
            			fragmentTransaction = fragmentManager.beginTransaction();
            			fragmentTransaction.detach(fragmentMain);
            			fragmentTransaction.attach(fragmentMain);
            			fragmentTransaction.commit();
    					
    				} catch (Exception e) {
    					Log.e("gray", "Exception : " + e.toString());
    				}
	        	}

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            	mConnectStatusString = "Disconnected";
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                if (isDebug) {
					Log.e("gray", "Disconnected from GATT server." + "");
				}
                
                isDeviceConnect = false;
                broadcastUpdate(intentAction);
                
                // refresh pages
                if (fragmentMain != null && drawerItemSelectedIndex == FRAMEPAGE_MAIN && isPause != true) {
                	try {
            			fragmentManager = getFragmentManager();
            			fragmentTransaction = fragmentManager.beginTransaction();
            			fragmentTransaction.detach(fragmentMain);
            			fragmentTransaction.attach(fragmentMain);
            			fragmentTransaction.commit();
    					
    				} catch (Exception e) {
    					Log.e("gray", "Exception : " + e.toString());
    				}
	        	}
                
                if (!isUserDisconnect) {
                	// try reconnect
//                	try {
//						Thread.sleep(3000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//                	disconnect();
//	                close();
//	                mBluetoothGatt = mSelectedLeDevice.connectGatt(MainActivity.this, false, mGattCallback);
					
				}
            } else if (newState == BluetoothProfile.STATE_CONNECTING) {
            	mConnectStatusString = "Connecting";
			} else if (newState == BluetoothProfile.STATE_DISCONNECTING) {
				mConnectStatusString = "Disconnecting";
			}
        }
	
        @Override
        // New services discovered
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        	if (isDebug) {
				Log.e("gray", "onServicesDiscovered, status:" + status);
			}
        	
        	mBluetoothGattServices = gatt.getServices();
        	
			Log.e("gray", "Total Service Number : " + mBluetoothGattServices.size());
			mEnumInfoString = "Enumeration : \nTotal Service Number : <b>" + mBluetoothGattServices.size() + "</b>\n";
			
			for (int i = 0; i < mBluetoothGattServices.size(); i++) {
				Log.e("gray", "============================================================");
				mEnumInfoString += "============================================================\n";
				checkSpecServices(mBluetoothGattServices.get(i));
				Log.e("gray", "service " + (i+1) + ", UUID:" + mBluetoothGattServices.get(i).getUuid().toString());
				mEnumInfoString += "Service " + (i+1) + ", UUID:" + mBluetoothGattServices.get(i).getUuid().toString() + "\n";
				
				mBluetoothGattCharacteristics = mBluetoothGattServices.get(i).getCharacteristics();
				Log.e("gray", "Total Characteristic Number : " + mBluetoothGattCharacteristics.size());
				mEnumInfoString += "Total Characteristic Number : <b>" + mBluetoothGattCharacteristics.size() + "</b>\n";
				
				for (int j = 0; j < mBluetoothGattCharacteristics.size(); j++) {
					Log.e("gray", "characteristic " + (j+1) + ", UUID:" + mBluetoothGattCharacteristics.get(j).getUuid().toString());
					mEnumInfoString += "Characteristic " + (j+1) + ", UUID:" + mBluetoothGattCharacteristics.get(j).getUuid().toString() + "\n";
					showProperty(mBluetoothGattCharacteristics.get(j));
					
//					if (isDebug) {
//						Log.e("gray", "getInstanceId:" + mBluetoothGattCharacteristics.get(j).getInstanceId());
//						if (mBluetoothGattCharacteristics.get(j).getValue() != null) {
//								Log.e("gray", "getValueLength:" + mBluetoothGattCharacteristics.get(j).getValue().length);
//								Log.e("gray", "getValue:" + bytesToHex(mBluetoothGattCharacteristics.get(j).getValue()));
//								Log.e("gray", "getStringValue:" + mBluetoothGattCharacteristics.get(j).getStringValue(0));
//								Log.e("gray", "getIntValue:" + mBluetoothGattCharacteristics.get(j).getIntValue(0x00000011, 0));
//						}
//						Log.e("gray", "getPermissions:" + mBluetoothGattCharacteristics.get(j).getPermissions());
//						Log.e("gray", "getProperties:" + mBluetoothGattCharacteristics.get(j).getProperties());
//						Log.e("gray", "getWriteType:" + mBluetoothGattCharacteristics.get(j).getWriteType());
//						Log.e("gray", "isCharacterisitcReadable:" + isCharacterisitcReadable(mBluetoothGattCharacteristics.get(j)));
//						Log.e("gray", "isCharacteristicWriteable:" + isCharacteristicWriteable(mBluetoothGattCharacteristics.get(j)));
//						Log.e("gray", "isCharacterisiticNotifiable:" + isCharacterisiticNotifiable(mBluetoothGattCharacteristics.get(j)));
//					}
					
					// match characteristic
					if (UUID_HEART_RATE_MEASUREMENT.equals(mBluetoothGattCharacteristics.get(j).getUuid())) {
						
						if (isDebug) {
							Log.e("gray", "match characteristic HEART_RATE_MEASUREMENT");
						}
						// set notification
						mBluetoothGatt.setCharacteristicNotification(mBluetoothGattCharacteristics.get(j), true);
			            BluetoothGattDescriptor descriptor = mBluetoothGattCharacteristics.get(j).getDescriptor(
			                    UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
			            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			            mBluetoothGatt.writeDescriptor(descriptor);
			            
			        } else if (UUID_HID_WRITE_TEST.equals(mBluetoothGattCharacteristics.get(j).getUuid())) {
			        	
						if (isDebug) {
							Log.e("gray", "match characteristic HID_WRITE_TEST");
						}
						if (mBluetoothGattCharacteristics.get(j).getWriteType() == 1) {
							if (isDebug) {
								Log.e("gray","Characteristic assigned!!");
							}
							mCharacteristicHidWriteTest = mBluetoothGattCharacteristics.get(j);
						}
						
					} else if (UUID_BATTERY_LEVEL.equals(mBluetoothGattCharacteristics.get(j).getUuid())) {
						
						if (isDebug) {
							Log.e("gray", "match characteristic BATTERY_LEVEL");
						}
						// set notification
						mBluetoothGatt.setCharacteristicNotification(mBluetoothGattCharacteristics.get(j), true);
			            BluetoothGattDescriptor descriptor = mBluetoothGattCharacteristics.get(j).getDescriptor(
			                    UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
			            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			            mBluetoothGatt.writeDescriptor(descriptor);
			            
						mCharacteristicBatteryLevel = mBluetoothGattCharacteristics.get(j);
						
					} else if (UUID_DEVICE_NAME.equals(mBluetoothGattCharacteristics.get(j).getUuid())) {
						
						if (isDebug) {
							Log.e("gray", "match characteristic DEVICE_NAME");
						}
						mCharacteristicDeviceName = mBluetoothGattCharacteristics.get(j);
						
					} else if (UUID_MODEL_NUMBER_STRING.equals(mBluetoothGattCharacteristics.get(j).getUuid())) {
						
						if (isDebug) {
							Log.e("gray", "match characteristic MODEL_NUMBER");
						}
						mCharacteristicModelNumber = mBluetoothGattCharacteristics.get(j);
						
					} else if (UUID_SERIAL_NUMBER_STRING.equals(mBluetoothGattCharacteristics.get(j).getUuid())) {
						
						if (isDebug) {
							Log.e("gray", "match characteristic SERIAL_NUMBER");
						}
						mCharacteristicSerialNumber = mBluetoothGattCharacteristics.get(j);
						
					} else if (UUID_FIRMWARE_REVISION_STRING.equals(mBluetoothGattCharacteristics.get(j).getUuid())) {
						
						if (isDebug) {
							Log.e("gray", "match characteristic FIRMWARE_REVISION");
						}
						mCharacteristicFWRevision = mBluetoothGattCharacteristics.get(j);
						
					} else if (UUID_HARDWARE_REVISION_STRING.equals(mBluetoothGattCharacteristics.get(j).getUuid())) {
						
						if (isDebug) {
							Log.e("gray", "match characteristic HARDWARE_REVISION");
						}
						mCharacteristicHWRevision = mBluetoothGattCharacteristics.get(j);
						
					} else if (UUID_SOFTWARE_REVISION_STRING.equals(mBluetoothGattCharacteristics.get(j).getUuid())) {
						
						if (isDebug) {
							Log.e("gray", "match characteristic SOFTWARE_REVISION");
						}
						mCharacteristicSWRevision = mBluetoothGattCharacteristics.get(j);
						
					} else if (UUID_MANUFACTURER_NAME_STRING.equals(mBluetoothGattCharacteristics.get(j).getUuid())) {
						
						if (isDebug) {
							Log.e("gray", "match characteristic MANUFACTURER_NAME");
						}
						mCharacteristicManufacturerName = mBluetoothGattCharacteristics.get(j);

					} else if (UUID_WBC_PRESSURE.equals(mBluetoothGattCharacteristics.get(j).getUuid())) {
						
						if (isDebug) {
							Log.e("gray", "match characteristic WBC_PRESSURE");
						}
						// set notification
						mBluetoothGatt.setCharacteristicNotification(mBluetoothGattCharacteristics.get(j), true);
			            BluetoothGattDescriptor descriptor = mBluetoothGattCharacteristics.get(j).getDescriptor(
			                    UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
			            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			            mBluetoothGatt.writeDescriptor(descriptor);
						mCharacteristicWBCPressure = mBluetoothGattCharacteristics.get(j);
						
					} else if (UUID_WBC_WALK_STEP_COUNT.equals(mBluetoothGattCharacteristics.get(j).getUuid())) {
						
						if (isDebug) {
							Log.e("gray", "match characteristic WBC_WALK_STEP_COUNT");
						}
						// set notification
						mBluetoothGatt.setCharacteristicNotification(mBluetoothGattCharacteristics.get(j), true);
			            BluetoothGattDescriptor descriptor = mBluetoothGattCharacteristics.get(j).getDescriptor(
			                    UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
			            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			            mBluetoothGatt.writeDescriptor(descriptor);
						mCharacteristicWBCWalkStepCount = mBluetoothGattCharacteristics.get(j);
						
					} else if (UUID_WBC_RUNNING_STEP_COUNT.equals(mBluetoothGattCharacteristics.get(j).getUuid())) {
						
						if (isDebug) {
							Log.e("gray", "match characteristic WBC_RUNNING_STEP_COUNT");
						}
						// set notification
						mBluetoothGatt.setCharacteristicNotification(mBluetoothGattCharacteristics.get(j), true);
			            BluetoothGattDescriptor descriptor = mBluetoothGattCharacteristics.get(j).getDescriptor(
			                    UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
			            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			            mBluetoothGatt.writeDescriptor(descriptor);
						mCharacteristicWBCRunningStepCount = mBluetoothGattCharacteristics.get(j);
						
					} else if (UUID_WBC_ACTIVITY_TYPE.equals(mBluetoothGattCharacteristics.get(j).getUuid())) {
						
						if (isDebug) {
							Log.e("gray", "match characteristic WBC_ACTIVITY_TYPE");
						}
						// set notification
						mBluetoothGatt.setCharacteristicNotification(mBluetoothGattCharacteristics.get(j), true);
			            BluetoothGattDescriptor descriptor = mBluetoothGattCharacteristics.get(j).getDescriptor(
			                    UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
			            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			            mBluetoothGatt.writeDescriptor(descriptor);
						mCharacteristicWBCActivityType = mBluetoothGattCharacteristics.get(j);
						
					} else if (UUID_WBC_ACTIVITY_CONFIDENCE.equals(mBluetoothGattCharacteristics.get(j).getUuid())) {
						
						if (isDebug) {
							Log.e("gray", "match characteristic WBC_ACTIVITY_CONFIDENCE");
						}
						// set notification
						mBluetoothGatt.setCharacteristicNotification(mBluetoothGattCharacteristics.get(j), true);
			            BluetoothGattDescriptor descriptor = mBluetoothGattCharacteristics.get(j).getDescriptor(
			                    UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
			            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			            mBluetoothGatt.writeDescriptor(descriptor);
						mCharacteristicWBCActivityConfidence = mBluetoothGattCharacteristics.get(j);
						
					} else if (UUID_WBC_FLASH_TIME.equals(mBluetoothGattCharacteristics.get(j).getUuid())) {
						
						if (isDebug) {
							Log.e("gray", "match characteristic WBC_FLASH_TIME");
						}
						// set notification
//						mBluetoothGatt.setCharacteristicNotification(mBluetoothGattCharacteristics.get(j), true);
//			            BluetoothGattDescriptor descriptor = mBluetoothGattCharacteristics.get(j).getDescriptor(
//			                    UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
//			            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//			            mBluetoothGatt.writeDescriptor(descriptor);
//						mCharacteristicWBCFlashTime = mBluetoothGattCharacteristics.get(j);
						
					} else if (UUID_WBC_FLASH_ACTIVITY.equals(mBluetoothGattCharacteristics.get(j).getUuid())) {
						
						if (isDebug) {
							Log.e("gray", "match characteristic WBC_FLASH_ACTIVITY");
						}
						// set notification
//						mBluetoothGatt.setCharacteristicNotification(mBluetoothGattCharacteristics.get(j), true);
//			            BluetoothGattDescriptor descriptor = mBluetoothGattCharacteristics.get(j).getDescriptor(
//			                    UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
//			            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//			            mBluetoothGatt.writeDescriptor(descriptor);
//						mCharacteristicWBCFlashActivity = mBluetoothGattCharacteristics.get(j);
						
					} else if (UUID_WBC_DATA_SEND_SIZE.equals(mBluetoothGattCharacteristics.get(j).getUuid())) {
						
						if (isDebug) {
							Log.e("gray", "match characteristic WBC_DATA_SEND_SIZE");
						}
						// set notification
//						mBluetoothGatt.setCharacteristicNotification(mBluetoothGattCharacteristics.get(j), true);
//			            BluetoothGattDescriptor descriptor = mBluetoothGattCharacteristics.get(j).getDescriptor(
//			                    UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
//			            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//			            mBluetoothGatt.writeDescriptor(descriptor);
//						mCharacteristicWBCDataSendSize = mBluetoothGattCharacteristics.get(j);
						
					} else if (UUID_WBC_INPUT.equals(mBluetoothGattCharacteristics.get(j).getUuid())) {
						
						if (isDebug) {
							Log.e("gray", "match characteristic WBC_INPUT");
						}
						mCharacteristicWBCInput = mBluetoothGattCharacteristics.get(j);
						
					} else if (UUID_WBC_RW_TIMESTAMP.equals(mBluetoothGattCharacteristics.get(j).getUuid())) {
						
						if (isDebug) {
							Log.e("gray", "match characteristic WBC_RW_TIMESTAMP");
						}
						mCharacteristicWBCRWTimestamp = mBluetoothGattCharacteristics.get(j);
						
					} else {
						if (isDebug) {
							Log.e("gray", "No match!!");
						}
					}
				}
			}
			Log.e("gray", "============================================================");
			mEnumInfoString += "============================================================\n";
        	
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
            	if (isDebug) {
					Log.e("gray", "onServicesDiscovered received: " + status);
				}
            }
        }

		@Override
		// Characteristic notification
		public void onCharacteristicChanged(BluetoothGatt gatt,	BluetoothGattCharacteristic characteristic) {
			if (isDebug) {
				Log.e("gray", "onCharacteristicChanged, Uuid:" + characteristic.getUuid() + ", Data:" + bytesToHex(characteristic.getValue()));
			}
			
			if (isSetTimestamp != true) {
				
				isSetTimestamp = true;
				mHandler.postDelayed(new Runnable() {
	                @Override
	                public void run() {
	                	writeWBCTimestamp();
	                }
	            }, 20000);
				
			}
			
			// BluetoothGattCharacteristic.getIntValue(int formatType, int offset)
			// FORMAT_UINT8 	(0x00000011)
			// FORMAT_UINT16	(0x00000012)
			if (UUID_BATTERY_LEVEL.equals(characteristic.getUuid())) {
				
				mBatteryLevelInt = characteristic.getIntValue(0x00000011, 0);
				
			} else if (UUID_WBC_PRESSURE.equals(characteristic.getUuid())) {
				// 2 bytes
				mWBCPressureInt = characteristic.getIntValue(0x00000012, 0);
				isNotifyWBCPressureSet = true;
				
			} else if (UUID_WBC_WALK_STEP_COUNT.equals(characteristic.getUuid())) {
				// 2 bytes
				mWBCWalkStepCount = characteristic.getIntValue(0x00000012, 0);
				isNotifyWBCWalkStepCountSet = true;
//				if (isDebug) {
//					Log.e("gray", "MainActivity.java, mWBCWalkStepCount :" + mWBCWalkStepCount);
//				}
				
			} else if (UUID_WBC_RUNNING_STEP_COUNT.equals(characteristic.getUuid())) {
				// 2 bytes
				mWBCRunningStepCount = characteristic.getIntValue(0x00000012, 0);
				isNotifyWBCRunningkStepCountSet = true;
//				if (isDebug) {
//					Log.e("gray", "MainActivity.java, mWBCRunningStepCount :" + mWBCRunningStepCount);
//				}
				
			} else if (UUID_WBC_ACTIVITY_TYPE.equals(characteristic.getUuid())) {
				// 1 byte
				mWBCActivityType = characteristic.getIntValue(0x00000011, 0);
				isNotifyWBCActivityType = true;
				
			} else if (UUID_WBC_ACTIVITY_CONFIDENCE.equals(characteristic.getUuid())) {
				// 1 byte
				mWBCActivityConfidence = characteristic.getIntValue(0x00000011, 0);
				isNotifyWBCActivityConfidence = true;
				
			} else if (UUID_WBC_FLASH_TIME.equals(characteristic.getUuid())) {
				// 10 bytes
				mWBCFlashTime = characteristic.getIntValue(0x00000011, 0);
				
			} else if (UUID_WBC_FLASH_ACTIVITY.equals(characteristic.getUuid())) {
				// 20 bytes
				mWBCFlashActivity = characteristic.getIntValue(0x00000011, 0);
				
			} else if (UUID_WBC_DATA_SEND_SIZE.equals(characteristic.getUuid())) {
				// 4 bytes
				mWBCDataSendSize = characteristic.getIntValue(0x00000011, 0);

			}
			
			
			
			broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
		}
		

		@Override
		// Result of a characteristic read operation
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic,
				int status) {
			if (isDebug) {
				Log.e("gray", "onCharacteristicRead, status:" + status + ", Uuid:" + characteristic.getUuid());
//				Log.e("gray", "getInstanceId:" + characteristic.getInstanceId());
//				Log.e("gray", "onCharacteristicRead, Uuid:" + characteristic.getUuid());
//				if (characteristic.getValue() != null) {
//					Log.e("gray", "getValueLength:" + characteristic.getValue().length);
//					Log.e("gray", "getValue:" + bytesToHex(characteristic.getValue()));
//					Log.e("gray", "getStringValue:" + characteristic.getStringValue(0));
//					Log.e("gray", "getIntValue:" + characteristic.getIntValue(0x00000011, 0));
//				}
//				Log.e("gray", "getPermissions:" + characteristic.getPermissions());
//				Log.e("gray", "getProperties:" + characteristic.getProperties());
//				Log.e("gray", "getWriteType:" + characteristic.getWriteType());
			}
			
			if (UUID_DEVICE_NAME.equals(characteristic.getUuid())) {
				
				mDeviceNameString = characteristic.getStringValue(0);
				
			} else if (UUID_MODEL_NUMBER_STRING.equals(characteristic.getUuid())) {
				
				mModelNumberString = characteristic.getStringValue(0);
				
			} else if (UUID_SERIAL_NUMBER_STRING.equals(characteristic.getUuid())) {
				
				mSerialNumberString = characteristic.getStringValue(0);
				
			} else if (UUID_FIRMWARE_REVISION_STRING.equals(characteristic.getUuid())) {
				
				mFWRevisionString = characteristic.getStringValue(0);
				
			} else if (UUID_HARDWARE_REVISION_STRING.equals(characteristic.getUuid())) {
				
				mHWRevisionString = characteristic.getStringValue(0);
				
			} else if (UUID_SOFTWARE_REVISION_STRING.equals(characteristic.getUuid())) {
				
				mSWRevisionString = characteristic.getStringValue(0);
				
			} else if (UUID_MANUFACTURER_NAME_STRING.equals(characteristic.getUuid())) {
				
				mManufacturerNameString = characteristic.getStringValue(0);
				
			} else if (UUID_BATTERY_LEVEL.equals(characteristic.getUuid())) {
				// FORMAT_UINT8 == 0x00000011
				mBatteryLevelInt = characteristic.getIntValue(0x00000011, 0);
				
			} 

		}

		@Override
		public void onCharacteristicWrite (BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			if (isDebug) {
				Log.e("gray", "onCharacteristicWrite, status:" + status + ", Uuid:" + characteristic.getUuid());
//				Log.e("gray", "getInstanceId:" + characteristic.getInstanceId());
//				Log.e("gray", "getUuid:" + characteristic.getUuid());
//				if (characteristic.getValue() != null) {
//					Log.e("gray", "getValueLength:" + characteristic.getValue().length);
//					Log.e("gray", "getValue:" + characteristic.getValue()[0]);
//				}
//				Log.e("gray", "getPermissions:" + characteristic.getPermissions());
//				Log.e("gray", "getProperties:" + characteristic.getProperties());
//				Log.e("gray", "getWriteType:" + characteristic.getWriteType());
				
				if (UUID_WBC_RW_TIMESTAMP.equals(characteristic.getUuid())) {
					
					isWriteWBCWriteTimestamp = true;
					
				}
				
				
			}
		}
    };
        
    public static String bytesToHex(byte[] bytes) {
    	char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 3];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = hexArray[v >>> 4];
            hexChars[j * 3 + 1] = hexArray[v & 0x0F];
            if (j != bytes.length-1) {
            	hexChars[j * 3 + 2] = '-';
            } else {
            	hexChars[j * 3 + 2] = ' ';
			}
        }
        return new String(hexChars);
    }
    
    public String byteToHex (byte b) {
		String str = String.format("%02X", b); 
		return str;
	}
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (isDebug) {
			Log.e("gray", "onActivityResult, requestCode: " + requestCode);
			Log.e("gray", "onActivityResult, resultCode: " + resultCode);
		}
		
		switch (requestCode) {
		case 0:
			// settings return
			userWeight = Integer.valueOf(sharedPrefs.getString("pref_userWeight", "70"));
			userGender = Integer.valueOf( sharedPrefs.getString("pref_userGender", "0") );
			
			break;

		default:
			break;
		}

	}
	
	public void showProcessDialog(CharSequence title, CharSequence message){
    	
		mProgressDialog = ProgressDialog.show(MainActivity.this, title, message, true);
		mProgressDialog.setCancelable(true); 
    }
	
	private void broadcastUpdate(final String action) {
	    final Intent intent = new Intent(action);
	    sendBroadcast(intent);
	}
	
	private void broadcastUpdate(final String action,
            final BluetoothGattCharacteristic characteristic) {
		final Intent intent = new Intent(action);
		
		// This is special handling for the Heart Rate Measurement profile. Data
		// parsing is carried out as per profile specifications.
//		if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
//		int flag = characteristic.getProperties();
//		int format = -1;
//		if ((flag & 0x01) != 0) {
//		format = BluetoothGattCharacteristic.FORMAT_UINT16;
//		Log.d(TAG, "Heart rate format UINT16.");
//		} else {
//		format = BluetoothGattCharacteristic.FORMAT_UINT8;
//		Log.d(TAG, "Heart rate format UINT8.");
//		}
//		final int heartRate = characteristic.getIntValue(format, 1);
//		Log.d(TAG, String.format("Received heart rate: %d", heartRate));
//		intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
//		} else {
//		// For all other profiles, writes the data formatted in HEX.
//		final byte[] data = characteristic.getValue();
//		if (data != null && data.length > 0) {
//		final StringBuilder stringBuilder = new StringBuilder(data.length);
//		for(byte byteChar : data)
//		stringBuilder.append(String.format("%02X ", byteChar));
//		intent.putExtra(EXTRA_DATA, new String(data) + "\n" +
//		   stringBuilder.toString());
//		}
//		}
		sendBroadcast(intent);
	}
	
	public void mySleep(long sleepTime) {
		try {
    		Thread.sleep(sleepTime);
    	} catch (InterruptedException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
	}
	
	public void writeWBCTimestamp(){

		if (isDebug) {
			Log.e("gray", "writeWBCTimestamp");
		}
		
		if (isDeviceConnect != true || isUserDisconnect == true) {
			return;
		}
        // set wristband timestamp
        // year(6bits) + month(4bits) + day(5bits) + hour(5bits) + min(6bits) + sec(6bits) => total 4bytes
        // Ex:  14year  5month  12day  11hour  17min  0sec => 0x3958B440
        if (mCharacteristicWBCRWTimestamp != null) {
    		mySleep(3000);
    		Calendar cal = Calendar.getInstance(); 
            int currentYear = cal.get(Calendar.YEAR);
            currentYear -= 2000;
            int currentMonth = cal.get(Calendar.MONTH);
            int currentDate = cal.get(Calendar.DATE);
            int currentHour = cal.get(Calendar.HOUR);
            int currentMinute = cal.get(Calendar.MINUTE);
            int currentSecond = cal.get(Calendar.SECOND);

            int writeHex = 0;
    		writeHex |= currentYear << 26;
    		writeHex |= currentMonth << 22;
    		writeHex |= currentDate << 17;
    		writeHex |= currentHour << 12;
    		writeHex |= currentMinute << 6;
    		writeHex |= currentSecond << 0;
    		if (isDebug) {
    			Log.e("gray", "writeHex:, " + writeHex);
    		}
    		
    		byte[] writeByte = ByteBuffer.allocate(4).putInt(writeHex).array();
    		
			if (isDebug) {
				Log.e("gray", "writeByte, " + writeByte[0] + ", " + writeByte[1] + ", " + writeByte[2] + ", " + writeByte[3]);
			}
    	    mCharacteristicWBCRWTimestamp.setValue(writeByte);
    	    
    	    if (mBluetoothGatt == null) {
				Log.e("gray", "mBluetoothGatt null");
			}
    	    
    	    if (mCharacteristicWBCRWTimestamp == null) {
				Log.e("gray", "mCharacteristicWBCRWTimestamp null");
			}
			
        	boolean status = mBluetoothGatt.writeCharacteristic(mCharacteristicWBCRWTimestamp);
        	if (status != true) {
				Log.e("gray", "writeCharacteristic(mCharacteristicWBCRWTimestamp) error!");
			}
    	}
	}
	
	// Handles various events fired by the Service.
	// ACTION_GATT_CONNECTED: connected to a GATT server.
	// ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
	// ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
	// ACTION_DATA_AVAILABLE: received data from the device. This can be a
	// result of read or notification operations.
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        final String action = intent.getAction();
//	        if (isDebug) {
//				Log.e("gray", "BroadcastReceiver, onReceive, action:" + action);
//			}
	        if (MainActivity.ACTION_GATT_CONNECTED.equals(action)) {
//	            mConnected = true;
//	            updateConnectionState(R.string.connected);
	            invalidateOptionsMenu();
	        } else if (MainActivity.ACTION_GATT_DISCONNECTED.equals(action)) {
//	            mConnected = false;
//	            updateConnectionState(R.string.disconnected);
//	            invalidateOptionsMenu();
//	            clearUI();
	            
//	            TextView tv  = new TextView(getApplicationContext());
//	            tv.setMovementMethod(LinkMovementMethod.getInstance());
//	            tv.setText(Html.fromHtml(
//	            		"<b>"+mDeviceNameString+"</b> Disconnect !"
//	            		)); 
//	            tv.setTextSize(18);
//	            tv.setTextColor(android.graphics.Color.BLACK);
//	            tv.setPadding(20, 20, 20, 20);
//	            new AlertDialog.Builder(MainActivity.this)
//	            .setTitle("Warning")
//	            .setView(tv)
//	            .show();
	        	
	        	disableNotification();
	        	disconnect();
	        	close();
	        	resetVariables();
//	        	reconnectThread();
	        	
	        } else if (MainActivity.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
	            // Show all the supported services and characteristics on the
	            // user interface.
//	            displayGattServices(mBluetoothLeService.getSupportedGattServices());
	        	
	            isDeviceConnect = true;
	        	isUserDisconnect = false;
	        	
	        	// wait for a period to make read work!!
	        	boolean res = false;
	        	if (mCharacteristicDeviceName != null) {
	        		mySleep(WAIT_TIME);
	        		res = mBluetoothGatt.readCharacteristic(mCharacteristicDeviceName);
	        		if (res != true) {
	        			Log.e("gray", "readCharacteristic(mCharacteristicDeviceName) error!");
	        		}
				}
	        	if (mCharacteristicModelNumber != null) {
	        		mySleep(WAIT_TIME);
	        		res = mBluetoothGatt.readCharacteristic(mCharacteristicModelNumber);
	        		if (res != true) {
	        			Log.e("gray", "readCharacteristic(mCharacteristicModelNumber) error!");
	        		}
				}
	        	if (mCharacteristicSerialNumber != null) {
	        		mySleep(WAIT_TIME);
	        		res = mBluetoothGatt.readCharacteristic(mCharacteristicSerialNumber);
	        		if (res != true) {
	        			Log.e("gray", "readCharacteristic(mCharacteristicSerialNumber) error!");
	        		}
	        	}
	        	if (mCharacteristicFWRevision != null) {
	        		mySleep(WAIT_TIME);
	        		res = mBluetoothGatt.readCharacteristic(mCharacteristicFWRevision);
	        		if (res != true) {
	        			Log.e("gray", "readCharacteristic(mCharacteristicFWRevision) error!");
	        		}
	        	}
	        	if (mCharacteristicHWRevision != null) {
	        		mySleep(WAIT_TIME);
	        		res = mBluetoothGatt.readCharacteristic(mCharacteristicHWRevision);
	        		if (res != true) {
	        			Log.e("gray", "readCharacteristic(mCharacteristicHWRevision) error!");
	        		}
	        	}
	        	if (mCharacteristicSWRevision != null) {
	        		mySleep(WAIT_TIME);
	        		res = mBluetoothGatt.readCharacteristic(mCharacteristicSWRevision);
	        		if (res != true) {
	        			Log.e("gray", "readCharacteristic(mCharacteristicSWRevision) error!");
	        		}
	        	}
	        	if (mCharacteristicManufacturerName != null) {
	        		mySleep(WAIT_TIME);
	        		res = mBluetoothGatt.readCharacteristic(mCharacteristicManufacturerName);
	        		if (res != true) {
	        			Log.e("gray", "readCharacteristic(mCharacteristicManufacturerName) error!");
	        		}
	        	}
	        	if (mCharacteristicBatteryLevel != null) {
	        		mySleep(WAIT_TIME);
	        		res = mBluetoothGatt.readCharacteristic(mCharacteristicBatteryLevel);
	        		if (res != true) {
	        			Log.e("gray", "readCharacteristic(mCharacteristicBatteryLevel) error!");
	        		}
	        	}
	        	
	            // set notification
	            mySleep(3000);
	            enableNotification();
	            
				// alert dialog
	            if (!isPause && mDeviceNameString!= null) {
					
	            	TextView tv  = new TextView(getApplicationContext());
	            	tv.setMovementMethod(LinkMovementMethod.getInstance());
	            	tv.setText(Html.fromHtml(
	            			"Complete connection : <b>"+mDeviceNameString+"</b>!"
	            			)); 
	            	tv.setTextSize(18);
	            	tv.setTextColor(android.graphics.Color.BLACK);
	            	tv.setPadding(20, 20, 20, 20);
	            	new AlertDialog.Builder(MainActivity.this)
	            	.setTitle("Information")
	            	.setView(tv)
	            	.show();
				}
	            
	        } else if (MainActivity.ACTION_DATA_AVAILABLE.equals(action)) {
//	            displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
	        }
	    }
	};
    
	public boolean setNotification(BluetoothGattCharacteristic characteristic, boolean flag){
		
		if (characteristic == null) {
			return false;
		}
		
		boolean err1 = false;
        boolean err2 = false;
        boolean err3 = false;
        BluetoothGattDescriptor descriptor;
		err1 = mBluetoothGatt.setCharacteristicNotification(characteristic, flag);
    	descriptor = characteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
    	err2 = descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
    	err3 = mBluetoothGatt.writeDescriptor(descriptor);
    	if (err1 != true || err2 != true || err3 != true) {
			return false;
		}  else {
			return true;			
		}
	}
	
	public void setNotifications(boolean flag){
		
		boolean status = false;
		if (mCharacteristicWBCPressure != null) {
        	mySleep(WAIT_TIME);
        	status = setNotification(mCharacteristicWBCPressure, flag);
        	if (status == false) {
				Log.e("gray", "set notification err, mCharacteristicWBCPressure");
			}
		}
		if (mCharacteristicWBCWalkStepCount != null) {
        	mySleep(WAIT_TIME);
        	status = setNotification(mCharacteristicWBCWalkStepCount, flag);
        	if (status == false) {
				Log.e("gray", "set notification err, mCharacteristicWBCWalkStepCount");
			}
		}
		if (mCharacteristicWBCRunningStepCount != null) {
        	mySleep(WAIT_TIME);
        	status = setNotification(mCharacteristicWBCRunningStepCount, flag);
        	if (status == false) {
				Log.e("gray", "set notification err, mCharacteristicWBCRunningStepCount");
			}
		}
		if (mCharacteristicWBCActivityType != null) {
        	mySleep(WAIT_TIME);
        	status = setNotification(mCharacteristicWBCActivityType, flag);
        	if (status == false) {
				Log.e("gray", "set notification err, mCharacteristicWBCActivityType");
			}
		}
		if (mCharacteristicWBCActivityConfidence != null) {
        	mySleep(WAIT_TIME);
        	status = setNotification(mCharacteristicWBCActivityConfidence, flag);
        	if (status == false) {
				Log.e("gray", "set notification err, mCharacteristicWBCActivityConfidence");
			}
		}
//		if (mCharacteristicWBCFlashTime != null) {
//        	mySleep(WAIT_TIME);
//        	status = setNotification(mCharacteristicWBCFlashTime, flag);
//        	if (status == false) {
//				Log.e("gray", "set notification err, mCharacteristicWBCFlashTime");
//			}
//		}
//		if (mCharacteristicWBCFlashActivity != null) {
//        	mySleep(WAIT_TIME);
//        	status = setNotification(mCharacteristicWBCFlashActivity, flag);
//        	if (status == false) {
//				Log.e("gray", "set notification err, mCharacteristicWBCFlashActivity");
//			}
//		}
//		if (mCharacteristicWBCDataSendSize != null) {
//        	mySleep(WAIT_TIME);
//        	status = setNotification(mCharacteristicWBCDataSendSize, flag);
//        	if (status == false) {
//				Log.e("gray", "set notification err, mCharacteristicWBCDataSendSize");
//			}
//		}
	}
	
	public void enableNotification(){
		setNotifications(true);
		mySleep(WAIT_TIME);
	}
	
	public void disableNotification(){
		setNotifications(false);
		mySleep(WAIT_TIME);
	}

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
        	if (isDebug) {
				Log.e("gray", "newInstance, sectionNumber" + sectionNumber);
			}
            fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        	if (isDebug) {
				Log.e("gray", "PlaceholderFragment, " + "");
			}
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
//        	if (isDebug) {
//				Log.e("gray", "onCreateView, sectionNumber:" + getArguments().getInt(ARG_SECTION_NUMBER));
//			}
        	
        	// different view
        	rootView = null;
        	
        	int goPage = 0;
        	
        	if (getArguments().getInt(ARG_SECTION_NUMBER) == FRAMEPAGE_HISTORY) {
        		goPage = fragmentPreviousPage;
			} else {
				goPage = getArguments().getInt(ARG_SECTION_NUMBER);
			}
        	
            switch (goPage) {
            
            case FRAMEPAGE_MAIN:
            	
            	fragmentMain = fragment;
            	fragmentPreviousPage = FRAMEPAGE_MAIN;
            	
            	rootView = inflater.inflate(R.layout.fragment_activity, container, false);
            	
            	ImageView iv = (ImageView)rootView.findViewById(R.id.iv_activity);
            	if (isDeviceConnect == false) {
            		iv.setImageResource(R.drawable.disconnect_512);
            		mActivityString = "Disconnect";
            		mActivityString2 = "";
				} else if (mWBCActivityType == ACTIVITY_STANDING) {
            		iv.setImageResource(R.drawable.activity_stand_w_512);
				} else if (mWBCActivityType == ACTIVITY_WALKING) {
					iv.setImageResource(R.drawable.activity_walk_w_512);
				} else if (mWBCActivityType == ACTIVITY_RUNNING) {
					iv.setImageResource(R.drawable.activity_run_w_512);
				} else if (mWBCActivityType == ACTIVITY_DRIVING) {
					iv.setImageResource(R.drawable.activity_driving_w_512);
				} else if (mWBCActivityType == ACTIVITY_BIKING) {
					iv.setImageResource(R.drawable.activity_bike_w_512);
				} else if (mWBCActivityType == ACTIVITY_SITTING) {
					iv.setImageResource(R.drawable.activity_sit_w_512);
				} else if (mWBCActivityType == ACTIVITY_SWIMMING) {
					iv.setImageResource(R.drawable.activity_swim_w_512);
				} else if ( (mWBCActivityType & ACTIVITY_SLEEPING) == ACTIVITY_SLEEPING) {
					iv.setImageResource(R.drawable.activity_sleep_w_512);
				} else {
					iv.setImageResource(R.drawable.sand_glass_g_512);
					mActivityString = "Waiting...";
					mActivityString2 = "";
				}
            	iv.getLayoutParams().height = 512;
            	iv.getLayoutParams().width = 512;
            	
            	TextView tv = (TextView)rootView.findViewById(R.id.tv_activity);
            	tv.setText(Html.fromHtml("<b>"+mActivityString+"</b>"));
            	tv.setTextSize(44);
            	
            	TextView tv2 = (TextView)rootView.findViewById(R.id.tv_activity2);
            	tv2.setText(Html.fromHtml("<b>"+mActivityString2+"</b>"));
            	tv2.setTextSize(30);
            	
            	break;
            	
			case FRAMEPAGE_HISTORY:
            	fragmentPreviousPage = FRAMEPAGE_HISTORY;
				break;

			case FRAMEPAGE_PER_MIN:
            	
            	fragmentPerMin = fragment;
				fragmentPreviousPage = FRAMEPAGE_PER_MIN;

				MainActivity.getDrawingDataPerMin();
            	
            	rootView = inflater.inflate(R.layout.fragment_history, container, false);
            	RelativeLayout relativeLayout_history1 = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_history1); 
//            	relativeLayout_history1.addView(new AverageCubicTemperatureChart().execute(getActivity().getApplicationContext()));
            	relativeLayout_history1.addView(new SalesStackedBarChart().executeCaloriesView(getActivity().getApplicationContext()));
            	
            	RelativeLayout relativeLayout_history2 = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_history2); 
            	relativeLayout_history2.addView(new SalesStackedBarChart().executeWalkingView(getActivity().getApplicationContext()));
            	
            	RelativeLayout relativeLayout_history3 = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_history3); 
            	relativeLayout_history3.addView(new SalesStackedBarChart().executeRunningView(getActivity().getApplicationContext()));
            	
				break;

			case FRAMEPAGE_PER_DAY:
            	
            	fragmentPerDay = fragment;
            	fragmentPreviousPage = FRAMEPAGE_PER_DAY;
            	
            	MainActivity.getDrawingDataPerDay();
            	
            	rootView = inflater.inflate(R.layout.fragment_history2, container, false);
            	RelativeLayout relativeLayout_history21 = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_history21); 
            	relativeLayout_history21.addView(new SalesStackedBarChart().executeCaloriesViewPerDay(getActivity().getApplicationContext()));
            	
            	RelativeLayout relativeLayout_history22 = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_history22); 
            	relativeLayout_history22.addView(new SalesStackedBarChart().executeWalkingViewPerDay(getActivity().getApplicationContext()));
            	
            	RelativeLayout relativeLayout_history23 = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_history23); 
            	relativeLayout_history23.addView(new SalesStackedBarChart().executeRunningViewPerDay(getActivity().getApplicationContext()));
            	
				break;

			case FRAMEPAGE_ACTIVITY_PIE:

				getPieData();
				getPieData2();

				fragmentActivityPie = fragment;
				fragmentPreviousPage = FRAMEPAGE_ACTIVITY_PIE;
            	
            	rootView = inflater.inflate(R.layout.fragment_pie, container, false);
            	
            	TextView tv_pie = (TextView)rootView.findViewById(R.id.tv_pie); 
            	tv_pie.setText(DatePickerFragment.pickedYear+" / "+ (DatePickerFragment.pickedMonth+1) +" / "+DatePickerFragment.pickedDay);
            	tv_pie.setTextSize(30);
            	
            	RelativeLayout relativeLayout_pie = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_pie); 
            	relativeLayout_pie.addView(new BudgetPieChart().execute(getActivity().getApplicationContext()));
            	
            	RelativeLayout relativeLayout_pie2 = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_pie2); 
            	relativeLayout_pie2.addView(new BudgetPieChart().execute2(getActivity().getApplicationContext()));

            	Button btn = (Button) rootView.findViewById(R.id.button_pick_date);
            	btn.setOnClickListener(new Button.OnClickListener() {
            		
            		@Override
            		public void onClick(View v) {
            			DialogFragment newFragment = new DatePickerFragment();
            			newFragment.show(getFragmentManager(), "datePicker");
            		}
            	});
				break;

			case FRAMEPAGE_DEBUG_INFO:

				fragmentDebugInfo = fragment;
				fragmentPreviousPage = FRAMEPAGE_DEBUG_INFO;
            	
            	rootView = inflater.inflate(R.layout.fragment_heartrate, container, false);
//            	wv_heartrate = (WebView) rootView.findViewById(R.id.section_label_heartrate);
//            	wv_heartrate.loadDataWithBaseURL(null, mTempString, "text/html", "utf-8", null);
            	tv_heartrate = (TextView) rootView.findViewById(R.id.section_label_heartrate);
            	tv_heartrate.setText(Html.fromHtml("<b>"+mTempString+"</b>"));
            	tv_heartrate.setTextSize(20.0f);
				break;
				
			case FRAMEPAGE_DEVICE_INFO:

				fragmentPreviousPage = FRAMEPAGE_DEVICE_INFO;
				
				mDeviceInfoString = "<table>" + "<tr>" +
            			"<td>Device Name</td>" +
            			"<td> : <b>" + mDeviceNameString + "</b></td>" +
            			"</tr><tr>" +
            			"<td>Manufacturer Name</td>" +
            			"<td> : <b>" + mManufacturerNameString + "</b></td>" +
            			"</tr><tr>" +
            			"<td>Model Number</td>" +
            			"<td> : <b>" + mModelNumberString + "</b></td>" +
            			"</tr><tr>" +
            			"<td>Serial Number</td>" +
            			"<td> : <b>" + mSerialNumberString + "</b></td>" +
            			"</tr><tr>" +
            			"<td>Firmware Version</td>" +
            			"<td> : <b>" + mFWRevisionString + "</b></td>" +
            			"</tr><tr>" +
            			"<td>Hardware Version</td>" +
            			"<td> : <b>" + mHWRevisionString + "</b></td>" +
            			"</tr><tr>" +
            			"<td>Software Version</td>" +
            			"<td> : <b>" + mSWRevisionString + "</b></td>" +
            			"</tr><tr>" +
            			"<td>Battery Level</td>" +
            			"<td> : <b>" + mBatteryLevelInt + " %</b></td>" +
            			"</tr><tr>" +
            			"<td>Status</td>" +
            			"<td> : <b>" + mConnectStatusString + "</b></td>" +
            			"</tr></table><br>";
            	
    			mDeviceInfoString += mEnumInfoString + "\n";

            	rootView = inflater.inflate(R.layout.fragment_deviceinfo, container, false);
            	WebView wv = (WebView) rootView.findViewById(R.id.section_label);
    			mDeviceInfoString = mDeviceInfoString.replaceAll("\n", "<br>");
            	wv.loadDataWithBaseURL(null, mDeviceInfoString, "text/html", "utf-8", null);

				break;
            	
			default:
				break;
			}
            
            return rootView;
            
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            if (isDebug) {
				Log.e("gray", "onAttach, " + "");
			}
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
        
        public void setTextView(String s) {
            if (isDebug) {
				Log.e("gray", "setTextView, " + "");
			}
            tv_heartrate = (TextView) rootView.findViewById(R.id.section_label_heartrate);
            tv_heartrate.setText(s);
        }
    }
    
    public void openDB() {
		myDB = new MyDatabase(this);
	}

	public void closeDB() {
		myDB.close();
	}
	
	public void reNewDB() {
		mDB.execSQL("DROP TABLE IF EXISTS " + MyDatabase._TableName);
//		closeDB();
//		openDB();
	}
    
	public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
	}
	
	public void addToDB(int pressure, int activity, int walkStepCount, int runningStepCount, int sleepQuality, 
			float calories, float calories_standing, float calories_running, float calories_walking, float calories_driving, 
			float calories_biking, float calories_sitting, float calories_swimming, float calories_sleeping){
		ContentValues values = new ContentValues();
		values.put("_pressure", pressure);
		values.put("_activity", activity);
		values.put("_walkStepCount", walkStepCount);
		values.put("_runningStepCount", runningStepCount);
		values.put("_sleepQulity", sleepQuality);
		values.put("_calories", calories);
		values.put("_calories_standing", calories_standing);
		values.put("_calories_running", calories_running);
		values.put("_calories_walking", calories_walking);
		values.put("_calories_driving", calories_driving);
		values.put("_calories_biking", calories_biking);
		values.put("_calories_sitting", calories_sitting);
		values.put("_calories_swimming", calories_swimming);
		values.put("_calories_sleeping", calories_sleeping);
		values.put("_created_at", getDateTime());
		mDB.insert(MyDatabase._TableName, null, values);
	}

	// per min
	public static void getDrawingDataPerMin(){
		
//		mCursor = mDB.rawQuery(
//				"SELECT _id, _pressure, _activity, _walkStepCount, _runningStepCount, _sleepQulity, " +
//				"_calories, _calories_standing, _calories_running, _calories_walking, _calories_driving, " +
//				"_calories_biking, _calories_sitting, _calories_swimming, _calories_sleeping, _created_at FROM " + 
//				MyDatabase._TableName, null);
		
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String tempS = sdf.format(calendar.getTime());
		String queryS = "SELECT _walkStepCount, _runningStepCount, _calories, _created_at FROM " + MyDatabase._TableName + 
						" ORDER BY _created_at DESC";
		if (isDebug) {
			Log.e("gray", "query date:" + tempS);
			Log.e("gray", "query string:" + queryS);
		}
		
		mCursor = mDB.rawQuery(
				queryS,
				null);
		
		if (mCursor.moveToFirst() == false) {
			Log.e("gray", "mCursor.moveToFirst() error!!");
       	}
		
		int index = MAX_ARRAY_SIZE;
		
		draw_yMax_walkStepCount = 0;
		draw_yMax_runningStepCount = 0;
		draw_yMax_calories = 0;
       
		while(!mCursor.isAfterLast()) {

			index--;
			if (index < 0) {
				break;
			}
			
			draw_x[index] = index+1;

			draw_y_walking[index] = mCursor.getInt(0);
			if (draw_yMax_walkStepCount < mCursor.getInt(0)) {
				draw_yMax_walkStepCount = mCursor.getInt(0);
			}

			draw_y_running[index] = mCursor.getInt(1);
			if (draw_yMax_runningStepCount < mCursor.getInt(1)) {
				draw_yMax_runningStepCount = mCursor.getInt(1);
			}
			
			draw_y_calories[index] = mCursor.getFloat(2);
			if (draw_yMax_calories < mCursor.getFloat(2)) {
				draw_yMax_calories = mCursor.getFloat(2);
			}
			
			mCursor.moveToNext();
		}
		mCursor.close();
		mCursor = null;
	}
	
	public static void getDrawingDataPerDay(){
		
//		mCursor = mDB.rawQuery(
//				"SELECT _id, _pressure, _activity, _walkStepCount, _runningStepCount, _sleepQulity, " +
//				"_calories, _calories_standing, _calories_running, _calories_walking, _calories_driving, " +
//				"_calories_biking, _calories_sitting, _calories_swimming, _calories_sleeping, _created_at FROM " + 
//				MyDatabase._TableName, null);
		
		draw_yMax_walkStepCount = 0;
		draw_yMax_runningStepCount = 0;
		draw_yMax_calories = 0;
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -(MAX_DAY_TO_DRAW)); 			// MAX_DAY_TO_DRAW days ago	

		for (int i = 0; i < MAX_DAY_TO_DRAW; i++) {

			calendar.add(Calendar.DAY_OF_MONTH, 1);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String tempS = sdf.format(calendar.getTime());
			String queryS = "SELECT _walkStepCount, _runningStepCount, _calories, _created_at FROM " + MyDatabase._TableName + 
					" WHERE _created_at >= '" + tempS + " 00:00:00' AND _created_at <= '" + tempS + " 23:59:59'";
			
			if (isDebug) {
				Log.e("gray", "query date:" + tempS);
				Log.e("gray", "query string:" + queryS);
			}
			
			mCursor = mDB.rawQuery(
					queryS,
					null);

			if (mCursor.moveToFirst() == false) {
				Log.e("gray", "mCursor.moveToFirst() error!!");
//				return;
			}

			draw_x_dateArray[i] = (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DATE);

			while (!mCursor.isAfterLast()) {

				if (draw_y_walking_perday[i] < mCursor.getInt(0)) {
					draw_y_walking_perday[i] = mCursor.getInt(0);
				}
				if (draw_y_running_perday[i] < mCursor.getInt(1)) {
					draw_y_running_perday[i] = mCursor.getInt(1);
				}
				if (draw_y_calories_perday[i] < mCursor.getInt(2)) {
					draw_y_calories_perday[i] = mCursor.getInt(2);
				}

				mCursor.moveToNext();
			}

			if (draw_yMax_walkStepCount < draw_y_walking_perday[i]) {
				draw_yMax_walkStepCount = draw_y_walking_perday[i];
			}
			if (draw_yMax_runningStepCount < draw_y_running_perday[i]) {
				draw_yMax_runningStepCount = draw_y_running_perday[i];
			}
			if (draw_yMax_calories < draw_y_calories_perday[i]) {
				draw_yMax_calories = draw_y_calories_perday[i];
			}
		}
		
		for (int i = 0; i < MAX_DAY_TO_DRAW; i++) {
			Log.e("gray", "draw_x_dateArray " + i + ": " + draw_x_dateArray[i]);
			Log.e("gray", "draw_y_calories_perday " + i + ": " + draw_y_calories_perday[i]);
			Log.e("gray", "draw_y_walking_perday " + i + ": " + draw_y_walking_perday[i]);
			Log.e("gray", "draw_y_running_perday " + i + ": " + draw_y_running_perday[i]);
		}
		
		mCursor.close();
		mCursor = null;
	}
	
	// activity pie
	public static void getPieData(){
		
//		mCursor = mDB.rawQuery(
//				"SELECT _id, _pressure, _activity, _walkStepCount, _runningStepCount, _sleepQulity, " +
//						"_calories, _calories_standing, _calories_running, _calories_walking, _calories_driving, " +
//						"_calories_biking, _calories_sitting, _calories_swimming, _calories_sleeping, _created_at FROM " + 
//						MyDatabase._TableName, null);
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(DatePickerFragment.pickedYear, DatePickerFragment.pickedMonth, DatePickerFragment.pickedDay);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String tempS = sdf.format(calendar.getTime());
		String queryS = "SELECT _activity, _created_at FROM " + MyDatabase._TableName + 
						" WHERE _created_at >= '" + tempS + " 00:00:00' AND _created_at <= '" + tempS + " 23:59:59'";
		if (isDebug) {
			Log.e("gray", "query date:" + tempS);
			Log.e("gray", "query string:" + queryS);
		}
		
		mCursor = mDB.rawQuery(
				queryS,
				null);
		
		if (mCursor.moveToFirst() == false) {
			Log.e("gray", "mCursor.moveToFirst() error!!");
       	}
		
		int activityCounter_standing = 0;
		int activityCounter_running = 0;
		int activityCounter_walking = 0;
		int activityCounter_driving = 0;
		int activityCounter_biking = 0;
		int activityCounter_sitting = 0;
		int activityCounter_swimming = 0;
		int activityCounter_sleeping = 0;
		int activityCounter_total = 0;
		boolean isDateMatch = false;
		
		if (mCursor.getCount() == 0) {
			isDateMatch = false;
			Log.e("gray", "mCursor.getCount() == 0");
		} else {
			isDateMatch = true;
		}
		
		while(!mCursor.isAfterLast()) {
			
				switch (mCursor.getInt(0)) {
				case ACTIVITY_STANDING:
					activityCounter_standing++;
					break;
				case ACTIVITY_RUNNING:
					activityCounter_running++;
					break;
				case ACTIVITY_WALKING:
					activityCounter_walking++;
					break;
				case ACTIVITY_DRIVING:
					activityCounter_driving++;
					break;
				case ACTIVITY_BIKING:
					activityCounter_biking++;
					break;
				case ACTIVITY_SITTING:
					activityCounter_sitting++;
					break;
				case ACTIVITY_SWIMMING:
					activityCounter_swimming++;
					break;
				case ACTIVITY_SLEEPING:
					activityCounter_sleeping++;
					break;
				default:
					Log.e("gray", "mismatch!");
					break;
				}
			mCursor.moveToNext();
		}
		
		activityCounter_total = activityCounter_standing + activityCounter_running + activityCounter_walking +
								activityCounter_driving + activityCounter_biking + activityCounter_sitting +
								activityCounter_swimming + activityCounter_sleeping;
		
		if (isDateMatch && (activityCounter_total != 0) ) {
			activityCounter_standing = (int) ((activityCounter_standing/(double)activityCounter_total)*100.0);
			activityCounter_running = (int) ((activityCounter_running/(double)activityCounter_total)*100.0);
			activityCounter_walking = (int) ((activityCounter_walking/(double)activityCounter_total)*100.0);
			activityCounter_driving = (int) ((activityCounter_driving/(double)activityCounter_total)*100.0);
			activityCounter_biking = (int) ((activityCounter_biking/(double)activityCounter_total)*100.0);
			activityCounter_sitting = (int) ((activityCounter_sitting/(double)activityCounter_total)*100.0);
			activityCounter_swimming = (int) ((activityCounter_swimming/(double)activityCounter_total)*100.0);
			activityCounter_sleeping = (int) ((activityCounter_sleeping/(double)activityCounter_total)*100.0);
			draw_pie[0] = activityCounter_standing;
			draw_pie[1] = activityCounter_running;
			draw_pie[2] = activityCounter_walking;
			draw_pie[3] = activityCounter_driving;
			draw_pie[4] = activityCounter_biking;
			draw_pie[5] = activityCounter_sitting;
			draw_pie[6] = activityCounter_swimming;
			draw_pie[7] = activityCounter_sleeping;
		} else {
			draw_pie[0] = 10;
			draw_pie[1] = 5;
			draw_pie[2] = 10;
			draw_pie[3] = 5;
			draw_pie[4] = 20;
			draw_pie[5] = 15;
			draw_pie[6] = 5;
			draw_pie[7] = 30;
		}
		
		mCursor.close();
		mCursor = null;
	}
	
	// calories pie
	public static void getPieData2(){
		
//		mCursor = mDB.rawQuery(
//				"SELECT _id, _pressure, _activity, _walkStepCount, _runningStepCount, _sleepQulity, " +
//						"_calories, _calories_standing, _calories_running, _calories_walking, _calories_driving, " +
//						"_calories_biking, _calories_sitting, _calories_swimming, _calories_sleeping, _created_at FROM " + 
//						MyDatabase._TableName, null);
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(DatePickerFragment.pickedYear, DatePickerFragment.pickedMonth, DatePickerFragment.pickedDay);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String tempS = sdf.format(calendar.getTime());
		String queryS = "SELECT _activity, _calories_standing, _calories_running, _calories_walking, _calories_driving, " +
						"_calories_biking, _calories_sitting, _calories_swimming, _calories_sleeping, _created_at FROM " + MyDatabase._TableName + 
						" WHERE _created_at >= '" + tempS + " 00:00:00' AND _created_at <= '" + tempS + " 23:59:59'";
		if (isDebug) {
			Log.e("gray", "query date:" + tempS);
			Log.e("gray", "query string:" + queryS);
		}
		
		mCursor = mDB.rawQuery(
				queryS,
				null);
		
		if (mCursor.moveToFirst() == false) {
			Log.e("gray", "mCursor.moveToFirst() error!!");
       	}
		
		double caloriesCounter_standing = 0.0;
		double caloriesCounter_running = 0.0;
		double caloriesCounter_walking = 0.0;
		double caloriesCounter_driving = 0.0;
		double caloriesCounter_biking = 0.0;
		double caloriesCounter_sitting = 0.0;
		double caloriesCounter_swimming = 0.0;
		double caloriesCounter_sleeping = 0.0;
		double caloriesCounter_total = 0.0;
		boolean isDateMatch = false;
		
		if (mCursor.getCount() == 0) {
			isDateMatch = false;
			Log.e("gray", "mCursor.getCount() == 0");
		} else {
			isDateMatch = true;
		}
		
		while(!mCursor.isAfterLast()) {
			
				switch (mCursor.getInt(0)) {
				case ACTIVITY_STANDING:
					if (caloriesCounter_standing < mCursor.getDouble(1)) {
						caloriesCounter_standing = mCursor.getDouble(1);
					}
					break;
				case ACTIVITY_RUNNING:
					if (caloriesCounter_running < mCursor.getDouble(2)) {
						caloriesCounter_standing = mCursor.getDouble(2);
					}
					break;
				case ACTIVITY_WALKING:
					if (caloriesCounter_walking < mCursor.getDouble(3)) {
						caloriesCounter_walking = mCursor.getDouble(3);
					}
					break;
				case ACTIVITY_DRIVING:
					if (caloriesCounter_driving < mCursor.getDouble(4)) {
						caloriesCounter_driving = mCursor.getDouble(4);
					}
					break;
				case ACTIVITY_BIKING:
					if (caloriesCounter_biking < mCursor.getDouble(4)) {
						caloriesCounter_biking = mCursor.getDouble(4);
					}					
					break;
				case ACTIVITY_SITTING:
					if (caloriesCounter_sitting < mCursor.getDouble(5)) {
						caloriesCounter_sitting = mCursor.getDouble(5);
					}
					break;
				case ACTIVITY_SWIMMING:
					if (caloriesCounter_swimming < mCursor.getDouble(6)) {
						caloriesCounter_swimming = mCursor.getDouble(6);
					}
					break;
				case ACTIVITY_SLEEPING:
					if (caloriesCounter_sleeping < mCursor.getDouble(7)) {
						caloriesCounter_sleeping = mCursor.getDouble(7);
					}
					break;
				default:
					Log.e("gray", "mismatch!");
					break;
				}
			mCursor.moveToNext();
		}
		
		caloriesCounter_total = caloriesCounter_standing + caloriesCounter_running + caloriesCounter_walking +
								caloriesCounter_driving + caloriesCounter_biking + caloriesCounter_sitting +
								caloriesCounter_swimming + caloriesCounter_sleeping;
		
		if (isDateMatch  && (caloriesCounter_total != 0) ) {
			draw_pie2[0] = (int)caloriesCounter_standing;
			draw_pie2[1] = (int)caloriesCounter_running;
			draw_pie2[2] = (int)caloriesCounter_walking;
			draw_pie2[3] = (int)caloriesCounter_driving;
			draw_pie2[4] = (int)caloriesCounter_biking;
			draw_pie2[5] = (int)caloriesCounter_sitting;
			draw_pie2[6] = (int)caloriesCounter_swimming;
			draw_pie2[7] = (int)caloriesCounter_sleeping;
		} else {
			draw_pie2[0] = 1014;
			draw_pie2[1] = 58;
			draw_pie2[2] = 1870;
			draw_pie2[3] = 509;
			draw_pie2[4] = 2056;
			draw_pie2[5] = 543;
			draw_pie2[6] = 134;
			draw_pie2[7] = 607;
		}
		
		mCursor.close();
		mCursor = null;
	}
	
	public static void getCalories(){
		
//		mCursor = mDB.rawQuery(
//				"SELECT _id, _pressure, _activity, _walkStepCount, _runningStepCount, _sleepQulity, " +
//				"_calories, _calories_standing, _calories_running, _calories_walking, _calories_driving, " +
//				"_calories_biking, _calories_sitting, _calories_swimming, _calories_sleeping, _created_at FROM " + 
//				MyDatabase._TableName, null);
		
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String tempS = sdf.format(calendar.getTime());
		String queryS = "SELECT _calories_standing, _calories_running, _calories_walking, _calories_driving, _calories_biking, _calories_sitting, _calories_swimming, _calories_sleeping _created_at FROM " + MyDatabase._TableName + 
						" WHERE _created_at >= '" + tempS + " 00:00:00' AND _created_at <= '" + tempS + " 23:59:59'";
		if (isDebug) {
			Log.e("gray", "query date:" + tempS);
			Log.e("gray", "query string:" + queryS);
		}
		
		mCursor = mDB.rawQuery(
				queryS,
				null);
		
		if (mCursor.moveToFirst() == false) {
			Log.e("gray", "mCursor.moveToFirst() error!!");
       	}
		
		while(!mCursor.isAfterLast()) {
			
			calories_standing = mCursor.getFloat(0);
			calories_running = mCursor.getFloat(1);
			calories_walking = mCursor.getFloat(2);
			calories_driving = mCursor.getFloat(3);
			calories_biking = mCursor.getFloat(4);
			calories_sitting = mCursor.getFloat(5);
			calories_swimming = mCursor.getFloat(6);
			calories_sleeping = mCursor.getFloat(7);
			mCursor.moveToNext();
		}
		mCursor.close();
		mCursor = null;
	}
   
	public void recordHistoryData(){

		addToDB(mWBCPressureInt, mWBCActivityType, mWBCWalkStepCount, mWBCRunningStepCount, mSleepQuality, 
				(float) calories_total, (float) calories_standing, (float) calories_running, (float) calories_walking,
				(float) calories_driving, (float) calories_biking, (float) calories_sitting, (float) calories_swimming, (float) calories_sleeping);
		
		prWalkStepCount = mWBCWalkStepCount;
		prRunningStepCount = mWBCRunningStepCount;
		prCalories = calories_total;
   	}

}
