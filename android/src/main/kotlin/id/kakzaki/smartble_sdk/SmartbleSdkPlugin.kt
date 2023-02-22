package id.kakzaki.smartble_sdk

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.graphics.*
import android.media.AudioManager
import android.os.*
import android.telecom.TelecomManager
import android.text.format.DateFormat
import android.util.Log
import android.view.KeyEvent
import androidx.multidex.BuildConfig
import com.bestmafen.baseble.scanner.BleDevice
import com.bestmafen.baseble.scanner.BleScanCallback
import com.bestmafen.baseble.scanner.BleScanFilter
import com.bestmafen.baseble.scanner.ScannerFactory
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.*
import com.google.gson.Gson
import com.szabh.smable3.BleKey
import com.szabh.smable3.BleKeyFlag
import com.szabh.smable3.component.*
import com.szabh.smable3.entity.*
import com.szabh.smable3.entity.BleAlarm
import com.szabh.smable3.music.MusicController
import com.szabh.smable3.watchface.Element
import com.szabh.smable3.watchface.WatchFaceBuilder
import id.kakzaki.smartble_sdk.tools.*
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.EventSink
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.InputStream
import java.net.URL
import java.nio.ByteBuffer
import java.util.*
import kotlin.random.Random

/** SmartbleSdkPlugin */
class  SmartbleSdkPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private var mContext: Context?=null
  private var mActivity: Activity?=null
  private var pluginBinding: FlutterPluginBinding? = null
  private var activityBinding: ActivityPluginBinding? = null

  private val ID_ALL = 0xff
  private lateinit var mBleKey: BleKey
  private lateinit var mBleKeyFlag: BleKeyFlag
  private var mType = 0
  data class Contact(var name: String, var phone: String)
  private var mLocationTimes = 1
  private var mClassicBluetoothState = 2
  private var sportState = BleAppSportState.STATE_START
  private var sportMode = BleAppSportState.MODE_INDOOR

  private lateinit var channel : MethodChannel

  private var scanChannel : EventChannel?=null
  private lateinit var scanSink: EventSink
  private var onDeviceConnectedChannel : EventChannel?=null
  private var onDeviceConnectedSink: EventSink?=null
  private var onIdentityCreateChannel : EventChannel?=null
  private var onIdentityCreateSink: EventSink?=null
  private var onCommandReplyChannel : EventChannel?=null
  private var onCommandReplySink: EventSink?=null
  private var onOTAChannel : EventChannel?=null
  private var onOTASink: EventSink?=null
  private var onReadPowerChannel : EventChannel?=null
  private var onReadPowerSink: EventSink?=null
  private var onReadFirmwareVersionChannel : EventChannel?=null
  private var onReadFirmwareVersionSink: EventSink?=null
  private var onReadBleAddressChannel : EventChannel?=null
  private var onReadBleAddressSink: EventSink?=null
  private var onReadSedentarinessChannel : EventChannel?=null
  private var onReadSedentarinessSink: EventSink?=null
  private var onReadNoDisturbChannel : EventChannel?=null
  private var onReadNoDisturbSink: EventSink?=null
  private var onReadAlarmChannel : EventChannel?=null
  private var onReadAlarmSink: EventSink?=null
  private var onReadCoachingIdsChannel : EventChannel?=null
  private var onReadCoachingIdsSink: EventSink?=null
  private var onReadUiPackVersionChannel : EventChannel?=null
  private var onReadUiPackVersionSink: EventSink?=null
  private var onReadLanguagePackVersionChannel : EventChannel?=null
  private var onReadLanguagePackVersionSink: EventSink?=null
  private var onIdentityDeleteByDeviceChannel : EventChannel?=null
  private var onIdentityDeleteByDeviceSink: EventSink?=null
  private var onCameraStateChangeChannel : EventChannel?=null
  private var onCameraStateChangeSink: EventSink?=null
  private var onCameraResponseChannel : EventChannel?=null
  private var onCameraResponseSink: EventSink?=null
  private var onSyncDataChannel : EventChannel?=null
  private var onSyncDataSink: EventSink?=null
  private var onReadActivityChannel : EventChannel?=null
  private var onReadActivitySink: EventSink?=null
  private var onReadHeartRateChannel : EventChannel?=null
  private var onReadHeartRateSink: EventSink?=null
  private var onUpdateHeartRateChannel : EventChannel?=null
  private var onUpdateHeartRateSink: EventSink?=null
  private var onReadBloodPressureChannel : EventChannel?=null
  private var onReadBloodPressureSink: EventSink?=null
  private var onReadSleepChannel : EventChannel?=null
  private var onReadSleepSink: EventSink?=null
  private var onReadLocationChannel : EventChannel?=null
  private var onReadLocationSink: EventSink?=null
  private var onReadTemperatureChannel : EventChannel?=null
  private var onReadTemperatureSink: EventSink?=null
  private var onReadWorkout2Channel : EventChannel?=null
  private var onReadWorkout2Sink: EventSink?=null
  private var onStreamProgressChannel : EventChannel?=null
  private var onStreamProgressSink: EventSink?=null
  private var onUpdateAppSportStateChannel : EventChannel?=null
  private var onUpdateAppSportStateSink: EventSink?=null
  private var onClassicBluetoothStateChangeChannel : EventChannel?=null
  private var onClassicBluetoothStateChangeSink: EventSink?=null
  private var onDeviceFileUpdateChannel : EventChannel?=null
  private var onDeviceFileUpdateSink: EventSink?=null
  private var onReadDeviceFileChannel : EventChannel?=null
  private var onReadDeviceFileSink: EventSink?=null
  private var onReadTemperatureUnitChannel : EventChannel?=null
  private var onReadTemperatureUnitSink: EventSink?=null
  private var onReadDateFormatChannel : EventChannel?=null
  private var onReadDateFormatSink: EventSink?=null
  private var onReadWatchFaceSwitchChannel : EventChannel?=null
  private var onReadWatchFaceSwitchSink: EventSink?=null
  private var onUpdateWatchFaceSwitchChannel : EventChannel?=null
  private var onUpdateWatchFaceSwitchSink: EventSink?=null
  private var onAppSportDataResponseChannel : EventChannel?=null
  private var onAppSportDataResponseSink: EventSink?=null
  private var onReadWatchFaceIdChannel : EventChannel?=null
  private var onReadWatchFaceIdSink: EventSink?=null
  private var onWatchFaceIdUpdateChannel : EventChannel?=null
  private var onWatchFaceIdUpdateSink: EventSink?=null
  private var onHIDStateChannel : EventChannel?=null
  private var onHIDStateSink: EventSink?=null
  private var onHIDValueChangeChannel : EventChannel?=null
  private var onHIDValueChangeSink: EventSink?=null
  private var onDeviceSMSQuickReplyChannel : EventChannel?=null
  private var onDeviceSMSQuickReplySink: EventSink?=null
  private var onReadDeviceInfoChannel : EventChannel?=null
  private var onReadDeviceInfoSink: EventSink?=null
  private var onSessionStateChangeChannel : EventChannel?=null
  private var onSessionStateChangeSink: EventSink?=null
  private var onNoDisturbUpdateChannel : EventChannel?=null
  private var onNoDisturbUpdateSink: EventSink?=null
  private var onAlarmUpdateChannel : EventChannel?=null
  private var onAlarmUpdateSink: EventSink?=null
  private var onAlarmDeleteChannel : EventChannel?=null
  private var onAlarmDeleteSink: EventSink?=null
  private var onAlarmAddChannel : EventChannel?=null
  private var onAlarmAddSink: EventSink?=null
  private var onFindPhoneChannel : EventChannel?=null
  private var onFindPhoneSink: EventSink?=null
  private var onRequestLocationChannel : EventChannel?=null
  private var onRequestLocationSink: EventSink?=null
  private var onDeviceRequestAGpsFileChannel : EventChannel?=null
  private var onDeviceRequestAGpsFileSink: EventSink?=null
  private var onReadBloodOxygenChannel : EventChannel?=null
  private var onReadBloodOxygenSink: EventSink?=null
  private var onReadWorkoutChannel : EventChannel?=null
  private var onReadWorkoutSink: EventSink?=null
  private var onReadBleHrvChannel : EventChannel?=null
  private var onReadBleHrvSink: EventSink?=null
  private var onReadPressureChannel : EventChannel?=null
  private var onReadPressureSink: EventSink?=null
  private var onDeviceConnectingChannel : EventChannel?=null
  private var onDeviceConnectingSink: EventSink?=null
  private var onIncomingCallStatusChannel : EventChannel?=null
  private var onIncomingCallStatusSink: EventSink?=null
  private var onReceiveMusicCommandChannel : EventChannel?=null
  private var onReceiveMusicCommandSink: EventSink?=null

  private var mResult: Result? = null
  private val mDevices = mutableListOf<Any>()
  private val mBleScanner by lazy {
    // ScannerFactory.newInstance(arrayOf(UUID.fromString(BleConnector.BLE_SERVICE)))
    ScannerFactory.newInstance()
      .setScanDuration(30)
      .setScanFilter(object : BleScanFilter {

        override fun match(device: BleDevice): Boolean {
          //Filter the Bluetooth signal value, the larger the signal value, the stronger the signal, for example -66 > -88
          return device.mRssi > -88
        }
      })
      .setBleScanCallback(object : BleScanCallback {

        override fun onBluetoothDisabled() {
          print("onBluetoothDisabled")
        }

        override fun onScan(scan: Boolean) {
          if (scan) {
            mDevices.clear()
          }
          print("onScan $scan")
        }

        override fun onDeviceFound(device: BleDevice) {
          val item: MutableMap<String, Any> = HashMap()
          item["deviceName"] = device.mBluetoothDevice.name
          item["deviceMacAddress"] = device.mBluetoothDevice.address
          if (!(mDevices.contains(item))) {
            mDevices.add(item)
          }
//         Handler(Looper.getMainLooper()).post {
          scanSink.success(mDevices)
//          }
          if (BuildConfig.DEBUG) {
            Log.d("mDevices Found ","$mDevices")
          }
        }
      })
  }

  private val mBleHandleCallback by lazy {
    object : BleHandleCallback {

      override fun onDeviceConnected(device: BluetoothDevice) {
        if (BuildConfig.DEBUG) {
          Log.d("onDeviceConnected","$device")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["deviceName"] = device.name
        item["deviceMacAddress"] = device.address
        if(onDeviceConnectedSink!=null){
          onDeviceConnectedSink!!.success(item)
        }
      }

      override fun onIdentityCreate(status: Boolean, deviceInfo: BleDeviceInfo?) {
        if (status) {
          BleConnector.connectClassic()
        }
        if (BuildConfig.DEBUG) {
          Log.d("onIdentityCre","$status")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["status"] = status
        item["deviceInfo"] = gson.toJson(deviceInfo)
        if(onIdentityCreateSink!=null)
          onIdentityCreateSink!!.success(item)
      }

      override fun onCommandReply(bleKey: BleKey, bleKeyFlag: BleKeyFlag, status: Boolean) {
        if (BuildConfig.DEBUG) {
          Log.d("onCommandReply","$bleKey $bleKeyFlag -> $status")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["status"] = status
        item["bleKey"] = gson.toJson(bleKey)
        item["bleKeyFlag"] = gson.toJson(bleKeyFlag)
        if(onCommandReplySink!=null)
          onCommandReplySink!!.success(item)
      }

      override fun onOTA(status: Boolean) {
        if (BuildConfig.DEBUG) {
          Log.d("onOTA","$status")
        }
        if (!status) return

        if (mContext != null) {
          //FirmwareHelper.gotoOtaReally(mContext)
        }
      }

      override fun onReadMtkOtaMeta() {
        if (mContext != null) {
          // FirmwareHelper.gotoOtaReally(mContext)
        }
      }

      override fun onReadPower(power: Int) {
        if (BuildConfig.DEBUG) {
          Log.d("onReadPower","$power")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["power"] = power
        if(onReadPowerSink!=null)
          onReadPowerSink!!.success(item)
      }

      override fun onReadFirmwareVersion(version: String) {
        if (BuildConfig.DEBUG) {
          Log.d("onReadFirmware", version)
        }
        val item: MutableMap<String, Any> = HashMap()
        item["version"] = version
        if(onReadFirmwareVersionSink!=null)
          onReadFirmwareVersionSink!!.success(item)
      }

      override fun onReadBleAddress(address: String) {
        if (BuildConfig.DEBUG) {
          Log.d("onReadBleAddress", address)
        }
        val item: MutableMap<String, Any> = HashMap()
        item["address"] = address
        if(onReadBleAddressSink!=null)
          onReadBleAddressSink!!.success(item)
      }

      override fun onReadSedentariness(sedentarinessSettings: BleSedentarinessSettings) {
        if (BuildConfig.DEBUG) {
          Log.d("onReadSedentariness","$sedentarinessSettings")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["sedentarinessSettings"] = gson.toJson(sedentarinessSettings)
        if(onReadSedentarinessSink!=null)
          onReadSedentarinessSink!!.success(item)
      }

      override fun onReadNoDisturb(noDisturbSettings: BleNoDisturbSettings) {
        if (BuildConfig.DEBUG) {
          Log.d("onReadNoDisturb","$noDisturbSettings")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["noDisturbSettings"] = gson.toJson(noDisturbSettings)
        if(onReadNoDisturbSink!=null)
          onReadNoDisturbSink!!.success(item)
      }

      override fun onReadAlarm(alarms: List<BleAlarm>) {
        if (BuildConfig.DEBUG) {
          Log.d("onReadAlarm","$alarms")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["alarms"] = gson.toJson(alarms)
        if(onReadAlarmSink!=null)
          onReadAlarmSink!!.success(item)
      }

      override fun onReadCoachingIds(bleCoachingIds: BleCoachingIds) {
        if (BuildConfig.DEBUG) {
          Log.d("onReadCoachingIds","$bleCoachingIds")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["onReadCoachingIds"] = gson.toJson(bleCoachingIds)
        if(onReadCoachingIdsSink!=null)
          onReadCoachingIdsSink!!.success(item)
      }

      override fun onReadUiPackVersion(version: String) {
        if (BuildConfig.DEBUG) {
          Log.d("onReadUiPackVersion", version)
        }
        val item: MutableMap<String, Any> = HashMap()
        item["version"] = version
        if(onReadUiPackVersionSink!=null)
          onReadUiPackVersionSink!!.success(item)
      }

      override fun onReadLanguagePackVersion(version: BleLanguagePackVersion) {
        if (BuildConfig.DEBUG) {
          Log.d("onReadLanguagePackVersi","$version")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["version"] = version
        if(onReadLanguagePackVersionSink!=null)
          onReadLanguagePackVersionSink!!.success(item)
      }


      override fun onIdentityDeleteByDevice(isDevice: Boolean) {
        if (BuildConfig.DEBUG) {
          Log.d("onIdentityDeleteByDevic","$isDevice")
        }
        BleConnector.unbind()
        unbindCompleted()
        val item: MutableMap<String, Any> = HashMap()
        item["isDevice"] = isDevice
        if(onIdentityDeleteByDeviceSink!=null)
          onIdentityDeleteByDeviceSink!!.success(item)
      }

      override fun onCameraStateChange(cameraState: Int) {
        if (BuildConfig.DEBUG) {
          Log.d("onCameraStateChange","${CameraState.getState(cameraState)}")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["cameraState"] = cameraState
        item["cameraStateName"] = "${CameraState.getState(cameraState)}"
        if(onCameraStateChangeSink!=null)
          onCameraStateChangeSink!!.success(item)
      }

      override fun onCameraResponse(status: Boolean, cameraState: Int) {
        if (BuildConfig.DEBUG) {
          Log.d("onCameraResponse","$status ${CameraState.getState(cameraState)}")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["status"] = status
        item["cameraState"] = cameraState
        item["cameraStateName"] = "${CameraState.getState(cameraState)}"
        if(onCameraResponseSink!=null)
          onCameraResponseSink!!.success(item)
      }

      override fun onSyncData(syncState: Int, bleKey: BleKey) {
        if (BuildConfig.DEBUG) {
          Log.d("onSyncData","syncState=$syncState, bleKey=$bleKey")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["syncState"] = syncState
        item["bleKey"] = gson.toJson(bleKey)
        if(onSyncDataSink!=null)
          onSyncDataSink!!.success(item)
      }
      val gson = Gson()
      override fun onReadActivity(activities: List<BleActivity>) {
        if (BuildConfig.DEBUG) {
          Log.d("onReadActivity","$activities")
        }
//        var activitiesDecode: MutableList<Any> = ArrayList()
//        for(e in activities){
//          activitiesDecode.add(e.decode())
//        }
        val item: MutableMap<String, Any> = HashMap()

        item["activities"] = gson.toJson(activities)
        if(onReadActivitySink!=null)
          onReadActivitySink!!.success(item)
      }

      override fun onReadHeartRate(heartRates: List<BleHeartRate>) {
        if (BuildConfig.DEBUG) {
          Log.d("onReadHeartRate","$heartRates")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["heartRates"] = gson.toJson(heartRates)
        if(onReadHeartRateSink!=null)
          onReadHeartRateSink!!.success(item)
      }

      override fun onUpdateHeartRate(heartRate: BleHeartRate) {
        if (BuildConfig.DEBUG) {
          Log.d("onUpdateHeartRate","$heartRate")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["heartRate"] = gson.toJson(heartRate)
        if(onUpdateHeartRateSink!=null)
          onUpdateHeartRateSink!!.success(item)
      }

      override fun onReadBloodPressure(bloodPressures: List<BleBloodPressure>) {
        if (BuildConfig.DEBUG) {
          Log.d("onReadBloodPressure","$bloodPressures")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["bloodPressures"] = gson.toJson(bloodPressures)
        if(onReadBloodPressureSink!=null)
          onReadBloodPressureSink!!.success(item)
        // print( "$bloodPressures")
      }

      override fun onReadSleep(sleeps: List<BleSleep>) {
        if (BuildConfig.DEBUG) {
          Log.d("onReadSleep","$sleeps")
          LogUtils.d("onReadSleep $sleeps")
        }

//        val listSleep : List<Map<String, Int>>? = call.argument<List<Map<String, Int>>>("listSleep")
//        if (listSleep != null) {
//          val listNew = ArrayList<BleSleep>()
//          for (item in listSleep) {
//            val bleS=BleSleep(mTime =  item["mTime"]!!, mMode =item["mMode"]!! , mSoft = item["mSoft"]!!, mStrong =item["mStrong"]!! )
//            listNew.add(bleS)
//          }
//          val res = BleSleep.getSleepStatusDuration(sleeps = BleSleep.analyseSleep(listNew), origin = listNew)
//          result.success(res);
//        }
        //val analyze = BleSleep.analyseSleep(origin = sleeps)
        //val sleepQuality = BleSleep.getSleepStatusDuration(sleeps = BleSleep.analyseSleep(sleeps), origin = sleeps)
        //result.success(res);

        val item: MutableMap<String, Any> = HashMap()
        item["sleeps"] = gson.toJson(sleeps)
        if(onReadSleepSink!=null)
          onReadSleepSink!!.success(item)
      }

      override fun onReadLocation(locations: List<BleLocation>) {
        if (BuildConfig.DEBUG) {
          Log.d("onReadLocation","$locations")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["locations"] = gson.toJson(locations)
        if(onReadLocationSink!=null)
          onReadLocationSink!!.success(item)
      }



      override fun onReadTemperature(temperatures: List<BleTemperature>) {
        if (BuildConfig.DEBUG) {
          Log.d("onReadTemperature","$temperatures")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["temperatures"] = gson.toJson(temperatures)
        if(onReadTemperatureSink!=null)
          onReadTemperatureSink!!.success(item)
      }

      override fun onReadWorkout2(workouts: List<BleWorkout2>) {
        if (BuildConfig.DEBUG) {
          Log.d("onReadWorkout2","$workouts")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["workouts"] = gson.toJson(workouts)
        if(onReadWorkout2Sink!=null)
          onReadWorkout2Sink!!.success(item)
      }

      override fun onStreamProgress(
        status: Boolean,
        errorCode: Int,
        total: Int,
        completed: Int
      ) {
        if (BuildConfig.DEBUG) {
          Log.d("onStreamProgress","$status $errorCode $total $completed")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["status"] = status
        item["errorCode"] = errorCode
        item["total"] = total
        item["completed"] = completed
        if(onStreamProgressSink!=null)
          onStreamProgressSink!!.success(item)
      }


      override fun onIncomingCallStatus(status: Int) {
        if (BuildConfig.DEBUG) {
          Log.d("onIncomingCallStatus","$status ${if (status == 0) "Answer" else "Reject"}")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["status"] = status
        item["message"] = if (status == 0) "Answer" else "Reject"
        if(onIncomingCallStatusSink!=null){
          onIncomingCallStatusSink!!.success(item)
        }

        if (status == 0) {
          //angkat telepon
          try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
              val manager =
                mContext?.getSystemService(Context.TELECOM_SERVICE) as TelecomManager?
              manager?.acceptRingingCall()
            }  else {
              val audioManager =
                mContext?.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
              val eventDown =
                KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK)
              val eventUp = KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK)
              audioManager?.dispatchMediaKeyEvent(eventDown)
              audioManager?.dispatchMediaKeyEvent(eventUp)
              Runtime.getRuntime()
                .exec("input keyevent " + Integer.toString(KeyEvent.KEYCODE_HEADSETHOOK))
            }
          } catch (e: Exception) {
            e.printStackTrace()
          }
        } else {
          //reject telepon
          if (Build.VERSION.SDK_INT < 28) {
            try {
              val telephonyClass =
                Class.forName("com.android.internal.telephony.ITelephony")
              val telephonyStubClass = telephonyClass.classes[0]
              val serviceManagerClass = Class.forName("android.os.ServiceManager")
              val serviceManagerNativeClass =
                Class.forName("android.os.ServiceManagerNative")
              val getService =
                serviceManagerClass.getMethod("getService", String::class.java)
              val tempInterfaceMethod =
                serviceManagerNativeClass.getMethod(
                  "asInterface",
                  IBinder::class.java
                )
              val tmpBinder = Binder()
              tmpBinder.attachInterface(null, "fake")
              val serviceManagerObject = tempInterfaceMethod.invoke(null, tmpBinder)
              val retbinder =
                getService.invoke(serviceManagerObject, "phone") as IBinder
              val serviceMethod =
                telephonyStubClass.getMethod("asInterface", IBinder::class.java)
              val telephonyObject = serviceMethod.invoke(null, retbinder)
              val telephonyEndCall = telephonyClass.getMethod("endCall")
              telephonyEndCall.invoke(telephonyObject)
            } catch (e: Exception) {
              LogUtils.d("hang up error " + e.message)
            }
          } else {
            PermissionUtils
              .permission(PermissionConstants.PHONE)
              .request2 {
                if (it == PermissionStatus.GRANTED) {
                  LogUtils.d("hang up OK")
                  val manager =
                    mContext?.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
                  manager.endCall()
                }
              }
          }
        }

      }

      override fun onUpdateAppSportState(appSportState: BleAppSportState) {
        if (BuildConfig.DEBUG) {
          Log.d("onUpdateAppSportState","$appSportState")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["appSportState"] = gson.toJson(appSportState)
        if(onUpdateAppSportStateSink!=null)
          onUpdateAppSportStateSink!!.success(item)
      }

      override fun onClassicBluetoothStateChange(state: Int) {
        if (BuildConfig.DEBUG) {
          Log.d("onClassicBluetoot","$state")
        }
        print( ClassicBluetoothState.getState(state))
        mClassicBluetoothState = state
        val item: MutableMap<String, Any> = HashMap()
        item["state"] = state
        if(onClassicBluetoothStateChangeSink!=null)
          onClassicBluetoothStateChangeSink!!.success(item)
      }

      override fun onDeviceFileUpdate(deviceFile: BleDeviceFile) {
        if (BuildConfig.DEBUG) {
          Log.d("onDeviceFileUpdate","$deviceFile")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["deviceFile"] = gson.toJson(deviceFile)
        if(onDeviceFileUpdateSink!=null)
          onDeviceFileUpdateSink!!.success(item)
      }

      override fun onReadDeviceFile(deviceFile: BleDeviceFile) {
        if (deviceFile.mFileSize != 0) {
          BleConnector.sendData(BleKey.DEVICE_FILE, BleKeyFlag.READ_CONTINUE)
        }
        if (BuildConfig.DEBUG) {
          Log.d("onReadDeviceFile","$deviceFile")
        }
        FileIOUtils.writeFileFromBytesByStream(
          File(PathUtils.getExternalAppDataPath() + "/voice/${deviceFile.mTime}.wav"),
          deviceFile.mFileContent,
          true
        )
        val item: MutableMap<String, Any> = HashMap()
        item["deviceFile"] = gson.toJson(deviceFile)
        if(onReadDeviceFileSink!=null)
          onReadDeviceFileSink!!.success(item)
      }

      override fun onReadTemperatureUnit(value: Int) {
        if (BuildConfig.DEBUG) {
          Log.d("onReadTemperatureUnit","$value")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["value"] = value
        if(onReadTemperatureUnitSink!=null)
          onReadTemperatureUnitSink!!.success(item)
      }

      override fun onReadDateFormat(value: Int) {
        if (BuildConfig.DEBUG) {
          Log.d("onReadDateFormat","$value")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["value"] = value
        if(onReadDateFormatSink!=null)
          onReadDateFormatSink!!.success(item)
      }

      override fun onReadWatchFaceSwitch(value: Int) {
        if (BuildConfig.DEBUG) {
          Log.d("onReadWatchFaceSwitch","$value")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["value"] = value
        if(onReadWatchFaceSwitchSink!=null)
          onReadWatchFaceSwitchSink!!.success(item)
      }

      override fun onUpdateWatchFaceSwitch(status: Boolean) {
        if (BuildConfig.DEBUG) {
          Log.d("onUpdateWatchFaceSwitch","$status")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["status"] = status
        if(onUpdateWatchFaceSwitchSink!=null)
          onUpdateWatchFaceSwitchSink!!.success(item)
      }

      override fun onAppSportDataResponse(status: Boolean) {
        if (BuildConfig.DEBUG) {
          Log.d("onAppSportDataResponse","$status")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["status"] = status
        if(onAppSportDataResponseSink!=null)
          onAppSportDataResponseSink!!.success(item)
      }

      override fun onReadWatchFaceId(watchFaceId: BleWatchFaceId) {
        if (BuildConfig.DEBUG) {
          Log.d("onReadWatchFaceId","${watchFaceId.mIdList}")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["watchFaceId"] = gson.toJson(watchFaceId)
        if(onReadWatchFaceIdSink!=null)
          onReadWatchFaceIdSink!!.success(item)
      }

      override fun onWatchFaceIdUpdate(status: Boolean) {
        if (BuildConfig.DEBUG) {
          Log.d("onWatchFaceIdUpdate","$status")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["status"] = status
        if(onWatchFaceIdUpdateSink!=null)
          onWatchFaceIdUpdateSink!!.success(item)
        //chooseFile(mContext, BleKey.WATCH_FACE.mKey)
      }

      override fun onHIDState(state: Int) {
        if (BuildConfig.DEBUG) {
          Log.d("onHIDState","$state")
        }
        if(state == HIDState.DISCONNECTED){
          BleConnector.connectHID()
        }
        val item: MutableMap<String, Any> = HashMap()
        item["status"] = state
        if(onHIDStateSink!=null)
          onHIDStateSink!!.success(item)
      }

      override fun onHIDValueChange(value: Int) {
        if (BuildConfig.DEBUG) {
          Log.d("onHIDValueChange","$value")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["value"] = value
        if(onHIDValueChangeSink!=null)
          onHIDValueChangeSink!!.success(item)
      }

      override fun onDeviceSMSQuickReply(smsQuickReply: BleSMSQuickReply) {
        if (BuildConfig.DEBUG) {
          Log.d("onDeviceSMSQuickReply", smsQuickReply.toString())
        }
        val item: MutableMap<String, Any> = HashMap()
        item["smsQuickReply"] = gson.toJson(smsQuickReply)
        if(onDeviceSMSQuickReplySink!=null)
          onDeviceSMSQuickReplySink!!.success(item)
      }

      override fun onReadDeviceInfo(deviceInfo: BleDeviceInfo) {
        if (BuildConfig.DEBUG) {
          Log.d("onReadDeviceInfo","$deviceInfo")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["deviceInfo"] = gson.toJson(deviceInfo)
        if(onReadDeviceInfoSink!=null)
          onReadDeviceInfoSink!!.success(item)
      }

      override fun onSessionStateChange(status: Boolean) {
        if (BuildConfig.DEBUG) {
          Log.d("onSessionStateChange","$status")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["status"] = status
        if(onSessionStateChangeSink!=null)
          onSessionStateChangeSink!!.success(item)
      }

      override fun onNoDisturbUpdate(noDisturbSettings: BleNoDisturbSettings) {
        if (BuildConfig.DEBUG) {
          Log.d("onNoDisturbUpdate","$noDisturbSettings")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["noDisturbSettings"] = gson.toJson(noDisturbSettings)
        if(onNoDisturbUpdateSink!=null)
          onNoDisturbUpdateSink!!.success(item)
      }

      override fun onAlarmUpdate(alarm: BleAlarm) {
        if (BuildConfig.DEBUG) {
          Log.d("onAlarmUpdate","$alarm")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["alarm"] = gson.toJson(alarm)
        if(onAlarmUpdateSink!=null)
          onAlarmUpdateSink!!.success(item)
      }

      override fun onAlarmDelete(id: Int) {
        if (BuildConfig.DEBUG) {
          Log.d("onAlarmDelete","$id")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["id"] = id
        if(onAlarmDeleteSink!=null)
          onAlarmDeleteSink!!.success(item)
      }

      override fun onAlarmAdd(alarm: BleAlarm) {
        if (BuildConfig.DEBUG) {
          Log.d("onAlarmAdd","$alarm")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["alarm"] = gson.toJson(alarm)
        if(onAlarmAddSink!=null)
          onAlarmAddSink!!.success(item)
      }

      override fun onFindPhone(start: Boolean) {
        if (BuildConfig.DEBUG) {
          Log.d("onFindPhone","onFindPhone ${if (start) "started" else "stopped"}")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["start"] = start
        if(onFindPhoneSink!=null)
          onFindPhoneSink!!.success(item)
      }

      override fun onRequestLocation(workoutState: Int) {
        if (BuildConfig.DEBUG) {
          Log.d("onRequestLocation","${WorkoutState.getState(workoutState)}")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["workoutState"] = workoutState
        if(onRequestLocationSink!=null)
          onRequestLocationSink!!.success(item)
      }

      override fun onDeviceRequestAGpsFile(url: String) {
        if (BuildConfig.DEBUG) {
          Log.d("onDeviceRequestAGpsFile", url)
        }
        val item: MutableMap<String, Any> = HashMap()
        item["url"] = url
        if(onDeviceRequestAGpsFileSink!=null)
          onDeviceRequestAGpsFileSink!!.success(item)
        // The following is a sample code, the files in the sdk will expire, just for demonstration
//        when (BleCache.mAGpsType) {
//          1 -> BleConnector.sendStream(BleKey.AGPS_FILE, assets.open("type1_epo_gr_3_1.dat"))
//          2 -> BleConnector.sendStream(BleKey.AGPS_FILE, assets.open("type2_current_1d.alp"))
//          6 -> BleConnector.sendStream(BleKey.AGPS_FILE, assets.open("type6_ble_epo_offline.bin"))
//        }
        // 实际工程要从url下载aGps文件，然后发送该aGps文件
        // val file = download(url)
        // BleConnector.sendStream(BleKey.AGPS_FILE, file)
      }


      override fun onReadBloodOxygen(bloodOxygen: List<BleBloodOxygen>){
        if (BuildConfig.DEBUG) {
          Log.d("onReadBloodOxygen","$bloodOxygen")

        }
        LogUtils.d("onReadBloodOxygentes $bloodOxygen")
        val item: MutableMap<String, Any> = HashMap()
        item["bloodOxygen"] = gson.toJson(bloodOxygen)
        if(onReadBloodOxygenSink!=null)
          onReadBloodOxygenSink!!.success(item)
      }

      override fun onReadWorkout(workouts: List<BleWorkout>){
        if (BuildConfig.DEBUG) {
          Log.d("onReadWorkout","$workouts")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["workouts"] = gson.toJson(workouts)
        if(onReadWorkoutSink!=null)
          onReadWorkoutSink!!.success(item)
      }

      override fun onReadBleHrv(hrv: List<BleHrv>){
        if (BuildConfig.DEBUG) {
          Log.d("onReadBleHrv","$hrv")

        }
        LogUtils.d("onReadBleHrvtes $hrv")
        val item: MutableMap<String, Any> = HashMap()
        item["hrv"] = gson.toJson(hrv)
        if(onReadBleHrvSink!=null)
          onReadBleHrvSink!!.success(item)
      }

      override fun onReadPressure(pressures: List<BlePressure>){
        if (BuildConfig.DEBUG) {
          Log.d("onReadPressure","$pressures")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["pressures"] = gson.toJson(pressures)
        if(onReadPressureSink!=null)
          onReadPressureSink!!.success(item)
      }


      override fun onDeviceConnecting(status: Boolean) {
        if (BuildConfig.DEBUG) {
          Log.d("onDeviceConnecting","$status")
        }
        val item: MutableMap<String, Any> = HashMap()
        item["status"] = status
        if(onDeviceConnectingSink!=null){
          onDeviceConnectingSink!!.success(item)
        }
      }

      var isPlay = false;

      override fun onReceiveMusicCommand(musicCommand: MusicCommand) {
        val mAudioManager = mContext?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (BuildConfig.DEBUG) {
          Log.d("onReceiveMusicCommand","$musicCommand")
          Log.d("receivedMusics","$musicCommand")
        }
        val currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val format = String.format("%.2f", (currentVolume/10.0))
        LogUtils.d("currenVolume: $currentVolume, format: $format")
        val musicSoundLevel = BleMusicControl(MusicEntity.PLAYER, MusicAttr.PLAYER_VOLUME, String.format("%.2f", (currentVolume/10.0)))
        BleConnector.sendObject(BleKey.MUSIC_CONTROL, BleKeyFlag.UPDATE, musicSoundLevel)

        //var event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY)


        when(musicCommand){
          MusicCommand.TOGGLE -> {
            if(isPlay){
              isPlay = false
              val event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PAUSE)
              val musicControlPause = BleMusicControl(MusicEntity.PLAYER, MusicAttr.PLAYER_PLAYBACK_INFO, "${PlaybackState.PAUSED.mState}")
              BleConnector.sendObject(BleKey.MUSIC_CONTROL, BleKeyFlag.UPDATE, musicControlPause)
              mAudioManager.dispatchMediaKeyEvent(event)
            }else{
              isPlay = true
              val event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY)
              val musicControlPlay = BleMusicControl(MusicEntity.PLAYER, MusicAttr.PLAYER_PLAYBACK_INFO, "${PlaybackState.PLAYING.mState}")
              BleConnector.sendObject(BleKey.MUSIC_CONTROL, BleKeyFlag.UPDATE, musicControlPlay)
              mAudioManager.dispatchMediaKeyEvent(event)
            }

          }
          MusicCommand.PLAY -> {
            val eventPlay = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY)
            val musicControlPlay = BleMusicControl(MusicEntity.PLAYER, MusicAttr.PLAYER_PLAYBACK_INFO, "${PlaybackState.PAUSED.mState}")
            BleConnector.sendObject(BleKey.MUSIC_CONTROL, BleKeyFlag.UPDATE, musicControlPlay)
            mAudioManager.dispatchMediaKeyEvent(eventPlay)
          }
          MusicCommand.PAUSE -> {
            val eventPause = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PAUSE)
            val musicControlPause = BleMusicControl(MusicEntity.PLAYER, MusicAttr.PLAYER_PLAYBACK_INFO, "${PlaybackState.PLAYING.mState}")
            BleConnector.sendObject(BleKey.MUSIC_CONTROL, BleKeyFlag.UPDATE, musicControlPause)
            mAudioManager.dispatchMediaKeyEvent(eventPause)

          }
          MusicCommand.NEXT -> {
            val eventNext = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT)
            val eventNext2 = KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT)
            var musicControlPlay = BleMusicControl(MusicEntity.PLAYER, MusicAttr.PLAYER_PLAYBACK_INFO, "${PlaybackState.PLAYING.mState}")
            isPlay = true
            BleConnector.sendObject(BleKey.MUSIC_CONTROL, BleKeyFlag.UPDATE, musicControlPlay)
            LogUtils.d("MediaNext")
            mAudioManager.dispatchMediaKeyEvent(eventNext)
            mAudioManager.dispatchMediaKeyEvent(eventNext2)
          }
          MusicCommand.PRE -> {
            val eventPrev = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS)
            val eventPrev2 = KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PREVIOUS)
            var musicControlPlay = BleMusicControl(MusicEntity.PLAYER, MusicAttr.PLAYER_PLAYBACK_INFO, "${PlaybackState.PLAYING.mState}")
            isPlay = true
            BleConnector.sendObject(BleKey.MUSIC_CONTROL, BleKeyFlag.UPDATE, musicControlPlay)

            LogUtils.d("MediaPrevious")
            mAudioManager.dispatchMediaKeyEvent(eventPrev)
            mAudioManager.dispatchMediaKeyEvent(eventPrev2)
          }
          MusicCommand.VOLUME_UP -> {
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI)
            val musicControlVolUp = BleMusicControl(MusicEntity.PLAYER, MusicAttr.PLAYER_VOLUME, String.format("%.2f", (currentVolume/10.0)))
            val musicControlPlay = BleMusicControl(MusicEntity.PLAYER, MusicAttr.PLAYER_PLAYBACK_INFO, "${PlaybackState.PLAYING.mState}")
            isPlay = true
            BleConnector.sendObject(BleKey.MUSIC_CONTROL, BleKeyFlag.UPDATE, musicControlVolUp)
            BleConnector.sendObject(BleKey.MUSIC_CONTROL, BleKeyFlag.UPDATE, musicControlPlay)
          }
          MusicCommand.VOLUME_DOWN -> {
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI)
            val musicControlVolDown = BleMusicControl(MusicEntity.PLAYER, MusicAttr.PLAYER_VOLUME, String.format("%.2f", (currentVolume/10.0)))
            val musicControlPlay = BleMusicControl(MusicEntity.PLAYER, MusicAttr.PLAYER_PLAYBACK_INFO, "${PlaybackState.PLAYING.mState}")
            isPlay = true
            BleConnector.sendObject(BleKey.MUSIC_CONTROL, BleKeyFlag.UPDATE, musicControlVolDown)
            BleConnector.sendObject(BleKey.MUSIC_CONTROL, BleKeyFlag.UPDATE, musicControlPlay)
          }
          MusicCommand.UNKNOWN -> {}
        }
      }

    }
  }


  override fun onAttachedToEngine(flutterPluginBinding: FlutterPluginBinding) {
    pluginBinding=flutterPluginBinding
    mContext=flutterPluginBinding.applicationContext
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "smartble_sdk")
    channel.setMethodCallHandler(this)
    scanChannel = EventChannel(flutterPluginBinding.binaryMessenger, "smartble_sdk/scan")
    scanChannel!!.setStreamHandler(scanResultsHandler)
    onDeviceConnectedChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onDeviceConnected")
    onDeviceConnectedChannel!!.setStreamHandler(onDeviceConnectedResultsHandler)
    onIdentityCreateChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onIdentityCreate")
    onIdentityCreateChannel!!.setStreamHandler(onIdentityCreateResultsHandler)
    onCommandReplyChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onCommandReply")
    onCommandReplyChannel!!.setStreamHandler(onCommandReplyResultsHandler)
    onOTAChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onOTA")
    onOTAChannel!!.setStreamHandler(onOTAResultsHandler)
    onReadPowerChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadPower")
    onReadPowerChannel!!.setStreamHandler(onReadPowerResultsHandler)
    onReadFirmwareVersionChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadFirmwareVersion")
    onReadFirmwareVersionChannel!!.setStreamHandler(onReadFirmwareVersionResultsHandler)
    onReadBleAddressChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadBleAddress")
    onReadBleAddressChannel!!.setStreamHandler(onReadBleAddressResultsHandler)
    onReadSedentarinessChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadSedentariness")
    onReadSedentarinessChannel!!.setStreamHandler(onReadSedentarinessResultsHandler)
    onReadNoDisturbChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadNoDisturb")
    onReadNoDisturbChannel!!.setStreamHandler(onReadNoDisturbResultsHandler)
    onReadAlarmChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadAlarm")
    onReadAlarmChannel!!.setStreamHandler(onReadAlarmResultsHandler)
    onReadCoachingIdsChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadCoachingIds")
    onReadCoachingIdsChannel!!.setStreamHandler(onReadCoachingIdsResultsHandler)
    onReadUiPackVersionChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadUiPackVersion")
    onReadUiPackVersionChannel!!.setStreamHandler(onReadUiPackVersionResultsHandler)
    onReadLanguagePackVersionChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadLanguagePackVersion")
    onReadLanguagePackVersionChannel!!.setStreamHandler(onReadLanguagePackVersionResultsHandler)
    onIdentityDeleteByDeviceChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onIdentityDeleteByDevice")
    onIdentityDeleteByDeviceChannel!!.setStreamHandler(onIdentityDeleteByDeviceResultsHandler)
    onCameraStateChangeChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onCameraStateChange")
    onCameraStateChangeChannel!!.setStreamHandler(onCameraStateChangeResultsHandler)
    onCameraResponseChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onCameraResponse")
    onCameraResponseChannel!!.setStreamHandler(onCameraResponseResultsHandler)
    onSyncDataChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onSyncData")
    onSyncDataChannel!!.setStreamHandler(onSyncDataResultsHandler)
    onReadActivityChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadActivity")
    onReadActivityChannel!!.setStreamHandler(onReadActivityResultsHandler)
    onReadHeartRateChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadHeartRate")
    onReadHeartRateChannel!!.setStreamHandler(onReadHeartRateResultsHandler)
    onUpdateHeartRateChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onUpdateHeartRate")
    onUpdateHeartRateChannel!!.setStreamHandler(onUpdateHeartRateResultsHandler)
    onReadBloodPressureChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadBloodPressure")
    onReadBloodPressureChannel!!.setStreamHandler(onReadBloodPressureResultsHandler)
    onReadSleepChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadSleep")
    onReadSleepChannel!!.setStreamHandler(onReadSleepResultsHandler)
    onReadLocationChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadLocation")
    onReadLocationChannel!!.setStreamHandler(onReadLocationResultsHandler)
    onReadTemperatureChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadTemperature")
    onReadTemperatureChannel!!.setStreamHandler(onReadTemperatureResultsHandler)
    onReadWorkout2Channel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadWorkout2")
    onReadWorkout2Channel!!.setStreamHandler(onReadWorkout2ResultsHandler)
    onStreamProgressChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onStreamProgress")
    onStreamProgressChannel!!.setStreamHandler(onStreamProgressResultsHandler)
    onUpdateAppSportStateChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onUpdateAppSportState")
    onUpdateAppSportStateChannel!!.setStreamHandler(onUpdateAppSportStateResultsHandler)
    onClassicBluetoothStateChangeChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onClassicBluetoothStateChange")
    onClassicBluetoothStateChangeChannel!!.setStreamHandler(onClassicBluetoothStateChangeResultsHandler)
    onDeviceFileUpdateChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onDeviceFileUpdate")
    onDeviceFileUpdateChannel!!.setStreamHandler(onDeviceFileUpdateResultsHandler)
    onReadDeviceFileChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadDeviceFile")
    onReadDeviceFileChannel!!.setStreamHandler(onReadDeviceFileResultsHandler)
    onReadTemperatureUnitChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadTemperatureUnit")
    onReadTemperatureUnitChannel!!.setStreamHandler(onReadTemperatureUnitResultsHandler)
    onReadDateFormatChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadDateFormat")
    onReadDateFormatChannel!!.setStreamHandler(onReadDateFormatResultsHandler)
    onReadWatchFaceSwitchChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadWatchFaceSwitch")
    onReadWatchFaceSwitchChannel!!.setStreamHandler(onReadWatchFaceSwitchResultsHandler)
    onUpdateWatchFaceSwitchChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onUpdateWatchFaceSwitch")
    onUpdateWatchFaceSwitchChannel!!.setStreamHandler(onUpdateWatchFaceSwitchResultsHandler)
    onAppSportDataResponseChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onAppSportDataResponse")
    onAppSportDataResponseChannel!!.setStreamHandler(onAppSportDataResponseResultsHandler)
    onReadWatchFaceIdChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadWatchFaceId")
    onReadWatchFaceIdChannel!!.setStreamHandler(onReadWatchFaceIdResultsHandler)
    onWatchFaceIdUpdateChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onWatchFaceIdUpdate")
    onWatchFaceIdUpdateChannel!!.setStreamHandler(onWatchFaceIdUpdateResultsHandler)
    onHIDStateChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onHIDState")
    onHIDStateChannel!!.setStreamHandler(onHIDStateResultsHandler)
    onHIDValueChangeChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onHIDValueChange")
    onHIDValueChangeChannel!!.setStreamHandler(onHIDValueChangeResultsHandler)
    onDeviceSMSQuickReplyChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onDeviceSMSQuickReply")
    onDeviceSMSQuickReplyChannel!!.setStreamHandler(onDeviceSMSQuickReplyResultsHandler)
    onReadDeviceInfoChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadDeviceInfo")
    onReadDeviceInfoChannel!!.setStreamHandler(onReadDeviceInfoResultsHandler)
    onSessionStateChangeChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onSessionStateChange")
    onSessionStateChangeChannel!!.setStreamHandler(onSessionStateChangeResultsHandler)
    onNoDisturbUpdateChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onNoDisturbUpdate")
    onNoDisturbUpdateChannel!!.setStreamHandler(onNoDisturbUpdateResultsHandler)
    onAlarmUpdateChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onAlarmUpdate")
    onAlarmUpdateChannel!!.setStreamHandler(onAlarmUpdateResultsHandler)
    onAlarmDeleteChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onAlarmDelete")
    onAlarmDeleteChannel!!.setStreamHandler(onAlarmDeleteResultsHandler)
    onAlarmAddChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onAlarmAdd")
    onAlarmAddChannel!!.setStreamHandler(onAlarmAddResultsHandler)
    onFindPhoneChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onFindPhone")
    onFindPhoneChannel!!.setStreamHandler(onFindPhoneResultsHandler)
    onRequestLocationChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onRequestLocation")
    onRequestLocationChannel!!.setStreamHandler(onRequestLocationResultsHandler)
    onDeviceRequestAGpsFileChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onDeviceRequestAGpsFile")
    onDeviceRequestAGpsFileChannel!!.setStreamHandler(onDeviceRequestAGpsFileResultsHandler)
    onReadBloodOxygenChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadBloodOxygen")
    onReadBloodOxygenChannel!!.setStreamHandler(onReadBloodOxygenResultsHandler)
    onReadWorkoutChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadWorkout")
    onReadWorkoutChannel!!.setStreamHandler(onReadWorkoutResultsHandler)
    onReadBleHrvChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadBleHrv")
    onReadBleHrvChannel!!.setStreamHandler(onReadBleHrvResultsHandler)
    onReadPressureChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReadPressure")
    onReadPressureChannel!!.setStreamHandler(onReadPressureResultsHandler)
    onDeviceConnectingChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onDeviceConnecting")
    onDeviceConnectingChannel!!.setStreamHandler(onDeviceConnectingResultsHandler)
    onIncomingCallStatusChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onIncomingCallStatus")
    onIncomingCallStatusChannel!!.setStreamHandler(onIncomingCallStatusResultsHandler)
    onReceiveMusicCommandChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onReceiveMusicCommand")
    onReceiveMusicCommandChannel!!.setStreamHandler(onReceiveMusicCommandResultsHandler)


    val connector = BleConnector.Builder(flutterPluginBinding.applicationContext)
      .supportRealtekDfu(false) // Whether to support Realtek device Dfu, pass false if no support is required.
      .supportMtkOta(false) // Whether to support MTK device Ota, pass false if no support is required.
      .supportLauncher(true) // Whether to support automatic connection to Ble Bluetooth device method (if bound), if not, please pass false
      .supportFilterEmpty(true) // Whether to support filtering empty data, such as ACTIVITY, HEART_RATE, BLOOD_PRESSURE, SLEEP, WORKOUT, LOCATION, TEMPERATURE, BLOOD_OXYGEN, HRV, if you do not need to support false.
      .build()

    connector.addHandleCallback(object : BleHandleCallback {
      override fun onSessionStateChange(status: Boolean) {
        if (status) {
          connector.sendObject(BleKey.TIME_ZONE, BleKeyFlag.UPDATE, BleTimeZone())
          connector.sendObject(BleKey.TIME, BleKeyFlag.UPDATE, BleTime.local())
          connector.sendInt8(
            BleKey.HOUR_SYSTEM, BleKeyFlag.UPDATE,
            if (DateFormat.is24HourFormat(Utils.getApp())) 0 else 1)
          connector.sendData(BleKey.POWER, BleKeyFlag.READ)
//          connector.sendData(BleKey.FIRMWARE_VERSION, BleKeyFlag.READ)
//          connector.sendInt8(BleKey.LANGUAGE, BleKeyFlag.UPDATE, Languages.languageToCode())
          connector.sendData(BleKey.MUSIC_CONTROL, BleKeyFlag.READ)
        }
      }
    })
    BleConnector.addHandleCallback(mBleHandleCallback)
  }

  private fun getBg(isRound: Boolean,bgBitmapx:Bitmap): ByteArray {
    val finalBgBitMap = getBgBitmap(false,isRound,bgBitmapx)
    ImageUtils.save(finalBgBitMap, File(PathUtils.getExternalAppDataPath(),"dial_bg_file.png"), Bitmap.CompressFormat.PNG)
    return bitmap2Bytes(finalBgBitMap)
  }

  private var controlViewStep = false
  private var controlViewStepX = 0
  private var controlViewStepY = 0

  //心率
  private var controlViewHr = false
  private var controlViewHrX = 0
  private var controlViewHrY = 0

  //卡路理
  private var controlViewCa = false
  private var controlViewCaX = 0
  private var controlViewCaY = 0

  //距离
  private var controlViewDis = false
  private var controlViewDisX = 0
  private var controlViewDisY = 0

  //数字时间
  private var timeDigitalView=false
  private  var timeDigitalViewWidth=0
  //指针
  private var timePointView = false
  //刻度
  private var timeScaleView = false

  private  var btnSync = false

  private var bgBitmapx: Bitmap? = null
  private  var customizeDialBg: Bitmap? = null

  private  var isRound = false //whether it is a round screen
  private  var roundCornerRadius = 0f //The corner radius of the rounded rectangle
  private var screenReservedBoundary = 0 //The actual resolution of some devices is inconsistent with the displayed area, and the boundary needs to be reserved to avoid deviations, such as T88_pro devices
  private var controlValueInterval = 0 //Controls such as distance and steps, the distance interval between the picture and the number part below
  private var controlValueRange = 9 //The content of the digital part below the control such as distance and steps
  private var fileFormat = "png" // The original format of the image of the dial element is generally in png format, and Realtek's is in bmp format
  private var imageFormat = WatchFaceBuilder.PNG_ARGB_8888 //Image encoding, the default is 8888, Realtek is RGB565
  private var X_CENTER = WatchFaceBuilder.GRAVITY_X_CENTER //Relative coordinate mark, MTK and Realtek have different implementations
  private var Y_CENTER = WatchFaceBuilder.GRAVITY_Y_CENTER //Relative coordinate mark, MTK and Realtek have different implementations
  private var borderSize = 0 //When drawing graphics, add the width of the ring
  private var ignoreBlack = 0 //Whether to ignore black, 0-do not ignore; 1-ignore



  var screenWidth = 0 //The actual size of the device screen - width
  var screenHeight = 0 //The actual size of the device screen - height
  var screenPreviewWidth = 0 //The actual preview size of the device screen - width
  var screenPreviewHeight = 0 //The actual preview size of the device screen - height

  //控件相关
  private var stepValueCenterX = 0f
  private var stepValueCenterY = 0f
  private var caloriesValueCenterX = 0f
  private var caloriesValueCenterY = 0f
  private var distanceValueCenterX = 0f
  private var distanceValueCenterY = 0f
  private var heartRateValueCenterX = 0f
  private var heartRateValueCenterY = 0f
  var valueColor = 0
  var custom = 0

  //数字时间
  private var amLeftX = 0f
  private var amTopY = 0f
  private var digitalTimeHourLeftX = 0f
  private var digitalTimeHourTopY = 0f
  private var digitalTimeMinuteLeftX = 0f
  private var digitalTimeMinuteRightX = 0f
  private var digitalTimeMinuteTopY = 0f
  private var digitalTimeSymbolLeftX = 0f
  private var digitalTimeSymbolTopY = 0f
  private var digitalDateMonthLeftX = 0f
  private var digitalDateMonthTopY = 0f
  private var digitalDateDayLeftX = 0f
  private var digitalDateDayTopY = 0f
  private var digitalDateSymbolLeftX = 0f
  private var digitalDateSymbolTopY = 0f
  private var digitalWeekLeftX = 0f
  private var digitalWeekTopY = 0f
  private var digitalValueColor = 0

  //pointer
  private var pointerSelectNumber = 0

  //digital_parameter
  private val DIGITAL_AM_DIR = "am_pm"
  private val DIGITAL_DATE_DIR = "date"
  private val DIGITAL_HOUR_MINUTE_DIR = "hour_minute"
  private val DIGITAL_WEEK_DIR = "week"

  //pointer_parameter
  private val POINTER_HOUR = "pointer/hour"
  private val POINTER_MINUTE = "pointer/minute"
  private  val POINTER_SECOND = "pointer/second"

  private fun getBgBitmap(isCanvasValue: Boolean,isRound: Boolean,bgBitmapx:Bitmap): Bitmap {
    val customDir: String
    if (custom == 2) {
      customDir = "dial_customize_454"
    } else {
      customDir = "dial_customize_240"
    }

    //初始资源路径
    val CONTROL_DIR = "$customDir/control"
    val STEP_DIR = "$CONTROL_DIR/step"
    val CALORIES_DIR = "$CONTROL_DIR/calories"
    val DISTANCE_DIR = "$CONTROL_DIR/distance"
    val HEART_RATE_DIR = "$CONTROL_DIR/heart_rate"

    //time
    val TIME_DIR = "$customDir/time"
    val DIGITAL_DIR = "$TIME_DIR/digital"

    //value
    val VALUE_DIR = "$customDir/value"

    val bgBitmap = if (isRound) {
      //圆
      bgBitmapx
    } else {
      //非圆
      bgBitmapx
    }
    var scaleWidth = screenWidth.toFloat() / bgBitmap.width
    var scaleHeight = (screenHeight.toFloat() - screenReservedBoundary) / bgBitmap.height
    LogUtils.d("test getBgBitmap = ${bgBitmap.width}- ${bgBitmap.height} ; $scaleWidth - $scaleHeight ")
    val scale = ImageUtils.scale(
      bgBitmap,
      scaleWidth,
      scaleHeight
    )
    val bgBitMap = if (isRound) {
      //裁圆,并且加黑边藏锯齿
      ImageUtils.toRound(scale, borderSize, Color.parseColor("#000000"))
    } else {
      //非圆
      ImageUtils.toRoundCorner(
        scale,
        roundCornerRadius,
        0f,
        Color.parseColor("#000000")
      )
    }
    //非圆,因为获取bitmap方式不一样,此处需要重新计算比列,为后续计算做准备
    if (!isRound) {
      if(bgBitmapx!=null){
        scaleWidth = screenWidth.toFloat() / bgBitmapx!!.width
        scaleHeight = (screenHeight.toFloat() - screenReservedBoundary) / bgBitmapx!!.height
      }
    }
    //获取控件的bitmap
    val canvas = Canvas(bgBitMap)
    val (stepX, stepY) = addControlBitmap(
      "$STEP_DIR/step_0.png",
      controlViewStep,
      controlViewStepX,
      controlViewStepY,
      "$VALUE_DIR/${valueColor}/",
      "18564",
      canvas,
      scaleWidth, scaleHeight,
      isCanvasValue
    )
    stepValueCenterX = stepX
    stepValueCenterY = stepY
    val (caloriesX, caloriesY) = addControlBitmap(
      "$CALORIES_DIR/calories_0.png",
      controlViewCa,
      controlViewCaX,
      controlViewCaY,
      "$VALUE_DIR/${valueColor}/",
      "50",
      canvas,
      scaleWidth, scaleHeight,
      isCanvasValue
    )
    caloriesValueCenterX = caloriesX
    caloriesValueCenterY = caloriesY
    val (distanceX, distanceY) = addControlBitmap(
      "$DISTANCE_DIR/distance_0.png",
      controlViewDis,
      controlViewDisX,
      controlViewDisY,
      "$VALUE_DIR/${valueColor}/",
      "6",
      canvas,
      scaleWidth, scaleHeight,
      isCanvasValue
    )
    distanceValueCenterX = distanceX
    distanceValueCenterY = distanceY
    val (heartRateX, heartRateY) = addControlBitmap(
      "$HEART_RATE_DIR/heart_rate_0.png",
      controlViewHr,
      controlViewHrX,
      controlViewHrY,
      "$VALUE_DIR/${valueColor}/",
      "90",
      canvas,
      scaleWidth, scaleHeight,
      isCanvasValue
    )
    heartRateValueCenterX = heartRateX
    heartRateValueCenterY = heartRateY
    //获取时间的bitmap
    if (timeDigitalView) {
      //数字时间
      addDigitalTime(
        "$DIGITAL_DIR/${digitalValueColor}/",
        scaleWidth, scaleHeight,
        canvas,
        isCanvasValue
      )
    }
    if (timePointView) {
      //指针
      //   getPointerBg(timePointView, isCanvasValue, canvas)
    }
    if (timeScaleView) {
      //刻度如果有显示,则必然绘制
      // getPointerBg(timeScaleView, true, canvas)
    }

    return getFinalBgBitmap(bgBitMap)
  }

  private fun getPointer(type: Int, dir: String, elements: ArrayList<Element>) {
    val customDir: String
    if (custom == 2) {
      customDir = "dial_customize_454"
    } else {
      customDir = "dial_customize_240"
    }

    //time
    val TIME_DIR = "$customDir/time"

    val POINTER_DIR = "$TIME_DIR/pointer"
    val pointerHour = ArrayList<ByteArray>()
    val tmpBitmap =
      ImageUtils.getBitmap(mContext!!.assets.open("$POINTER_DIR/${dir}/${pointerSelectNumber}.${fileFormat}"))
    val w = tmpBitmap.width
    val h = tmpBitmap.height
    val pointerHourValue =
      mContext!!.assets.open("$POINTER_DIR/${dir}/${pointerSelectNumber}.${fileFormat}")
        .use { it.readBytes() }

    pointerHour.add(defaultConversion(fileFormat, pointerHourValue, w))
    val elementAmPm = Element(
      type = type,
      w = w,
      h = h,
      gravity = WatchFaceBuilder.GRAVITY_X_LEFT or WatchFaceBuilder.GRAVITY_Y_TOP,
      ignoreBlack = ignoreBlack,
      x = screenWidth / 2 - 1,
      y = screenHeight / 2 - 1,
      bottomOffset = if (fileFormat == "png") 0 else h / 2,
      leftOffset = if (fileFormat == "png") 0 else w / 2,
      imageBuffers = pointerHour.toTypedArray()
    )
    elements.add(elementAmPm)
  }


  private fun addDigitalTimeParam(
    digitalDir: String,
    digitalSecondaryDir: String,
    tempValue: String,
    timeTop: Float,
    top: Int,
    canvas: Canvas,
    timeLeft: Float,
    isCanvasValue: Boolean
  ): Pair<Bitmap, Float> {
    //特殊符号处理-即使是瑞昱系列，也使用bng格式的特殊符号，因为特殊符号会被嵌入背景中，如果无法嵌入，需要单独做处理
    val symbolBitmap =
      ImageUtils.getBitmap(mContext!!.assets.open("$digitalDir$digitalSecondaryDir/symbol.png"))
    //此处涉及到预览，所以强制使用PNG图片，避免透明色不显示
    val valueBitmap =
      ImageUtils.getBitmap(mContext!!.assets.open("$digitalDir$digitalSecondaryDir/${tempValue[0]}.png"))
    val elementValueTop = timeTop + top + 6       //普通元素距离上方二外增加6像素间隔
    val symbolValueTop =
      elementValueTop + (valueBitmap.height - symbolBitmap.height) / 2       //特殊元素在普通元素的基础上，在增加差异高度一半的间隔
//        val valueWidth = valueBitmap.width * 4 + symbolBitmap.width
    if (digitalSecondaryDir.contains(DIGITAL_HOUR_MINUTE_DIR)) {
      digitalTimeHourLeftX = timeLeft
      digitalTimeHourTopY = elementValueTop
      digitalTimeSymbolLeftX = timeLeft + valueBitmap.width * 2
      digitalTimeSymbolTopY = symbolValueTop
      digitalTimeMinuteLeftX = timeLeft + valueBitmap.width * 2 + symbolBitmap.width
      digitalTimeMinuteTopY = elementValueTop
      digitalTimeMinuteRightX = digitalTimeMinuteLeftX + valueBitmap.width * 2 - 6
    } else if (digitalSecondaryDir.contains(DIGITAL_DATE_DIR)) {
      digitalDateMonthLeftX = timeLeft
      digitalDateMonthTopY = elementValueTop
      digitalDateSymbolLeftX = timeLeft + valueBitmap.width * 2
      digitalDateSymbolTopY = symbolValueTop
      digitalDateDayLeftX = timeLeft + valueBitmap.width * 2 + symbolBitmap.width
      digitalDateDayTopY = elementValueTop
    }

    if (screenReservedBoundary == 0) {
      //正常的图片，直接绘制特殊元素到背景，不用单独传输过去了，省得麻烦
      var valueParam = 0
      var valueSymWidth = 0
      for (index in tempValue.indices) {
        if (index == 2) {
          canvas.drawBitmap(
            symbolBitmap,
            timeLeft + valueBitmap.width * valueParam,
            symbolValueTop,
            null
          )
          valueSymWidth = symbolBitmap.width
        } else {
          if (isCanvasValue) {
            val itemBitmap =
              ImageUtils.getBitmap(mContext!!.assets.open("$digitalDir$digitalSecondaryDir/${tempValue[index]}.png"))
            canvas.drawBitmap(
              itemBitmap,
              timeLeft + valueBitmap.width * valueParam + valueSymWidth,
              elementValueTop,
              null
            )
          }
          valueParam++
        }
      }
    } else {
      if (isCanvasValue) {
        var valueParam = 0
        var valueSymWidth = 0
        for (index in tempValue.indices) {
          if (index == 2) {
            canvas.drawBitmap(
              symbolBitmap,
              timeLeft + valueBitmap.width * valueParam,
              symbolValueTop,
              null
            )
            valueSymWidth = symbolBitmap.width
          } else {
            val itemBitmap =
              ImageUtils.getBitmap(mContext!!.assets.open("$digitalDir$digitalSecondaryDir/${tempValue[index]}.${fileFormat}"))
            canvas.drawBitmap(
              itemBitmap,
              timeLeft + valueBitmap.width * valueParam + valueSymWidth,
              elementValueTop,
              null
            )
            valueParam++
          }
        }
      }
    }
    return Pair(valueBitmap, elementValueTop)
  }

  private fun addDigitalTime(
    digitalDir: String,
    scaleWidth: Float,
    scaleHeight: Float,
    canvas: Canvas,
    isCanvasValue: Boolean
  ) {
    val timeLeft = (screenWidth/3.5f) * scaleWidth
    val timeTop = (screenHeight/3) * scaleHeight
    LogUtils.d("test timeLeft=$timeLeft,  timeTop=$timeTop, timeDigitalView.width=${timeDigitalViewWidth} ,scaleWidth =$scaleWidth")
    //获取AM原始资源.此处涉及到预览，所以强制使用PNG图片，避免透明色不显示
    val amBitmap =
      ImageUtils.getBitmap(mContext!!.assets.open("$digitalDir$DIGITAL_AM_DIR/pm.png"))
    //绘制小时分钟的时间,这几个元素总宽度最长,根据这几个元素,可以确定时间元素整体的总宽度为120
    val tempValue = "08:30"
    val (hourMinuteBitmap, hourMinuteTop) = addDigitalTimeParam(
      digitalDir,
      DIGITAL_HOUR_MINUTE_DIR,
      tempValue,
      timeTop,
      amBitmap.height,
      canvas,
      timeLeft,
      isCanvasValue
    )
    //日期
    val tempDate = "07/08"
    val (_, dateAndWeekTop) = addDigitalTimeParam(
      digitalDir,
      DIGITAL_DATE_DIR,
      tempDate,
      hourMinuteTop,
      hourMinuteBitmap.height,
      canvas,
      timeLeft,
      isCanvasValue
    )
    //时间主体绘制完毕后,即可确认位置,然后就可以绘制其他的从属元素了
    //AM-PM
    if (isCanvasValue) {
      canvas.drawBitmap(
        amBitmap,
        digitalTimeMinuteRightX - amBitmap.width,
        timeTop,
        null
      )
    }
    amLeftX = digitalTimeMinuteRightX - amBitmap.width
    amTopY = timeTop
    //week 此处涉及到预览，所以强制使用PNG图片，避免透明色不显示
    val weekBitmap =
      ImageUtils.getBitmap(mContext!!.assets.open("$digitalDir$DIGITAL_WEEK_DIR/4.png"))
    if (isCanvasValue) {
      canvas.drawBitmap(
        weekBitmap,
        digitalTimeMinuteRightX - weekBitmap.width,
        dateAndWeekTop,
        null
      )
    }
    digitalWeekLeftX = digitalTimeMinuteRightX - weekBitmap.width
    digitalWeekTopY = dateAndWeekTop
  }

  private fun addControlBitmap(
    controlFileName: String,
    elementView: Boolean,
    elementViewX:Int,
    elementViewY:Int,
    controlValueFileDir: String,
    controlValue: String,
    canvas: Canvas,
    scaleWidth: Float,
    scaleHeight: Float,
    isCanvasValue: Boolean
  ): Pair<Float, Float> {
    if (elementView) {
      LogUtils.d("test addControlBitmap $controlFileName ,${elementViewX} ${elementViewY}  $scaleWidth $scaleHeight")
      val viewBitmap = ImageUtils.getBitmap(mContext!!.assets.open(controlFileName))
      val viewLeft = elementViewX * scaleWidth
      val viewTop = elementViewY * scaleHeight
      //x.y 获取该view相对于父 view的的left,top坐标点
      canvas.drawBitmap(
        viewBitmap,
        viewLeft,
        viewTop,
        null
      )
      //计算底部横向中心点.为计算数值做准备
      val bottomCenterX = viewBitmap.width / 2 + viewLeft
      val bottomY =
        viewTop + viewBitmap.height + controlValueInterval   //图片和数字之间添加指定间隔，避免过于靠近

      val firstValue =
        ImageUtils.getBitmap(mContext!!.assets.open("${controlValueFileDir}${controlValue[0]}.${fileFormat}"))
      val valueWidth = firstValue.width * controlValue.length + controlValue.length - 1
      val valueStartX = bottomCenterX - valueWidth / 2

      if (isCanvasValue) {
        for (index in controlValue.indices) {
          //此处涉及到预览，所以强制使用PNG图片，避免透明色不显示
          val value =
            ImageUtils.getBitmap(mContext!!.assets.open("${controlValueFileDir}${controlValue[index]}.png"))
          canvas.drawBitmap(
            value,
            valueStartX + index + value.width * index,
            bottomY,
            null
          )
        }
      }

      //计算数值显示区域的中心点,并返回
      val bottomCenterY = bottomY + firstValue.height / 2
      return Pair(bottomCenterX, bottomCenterY)
    }
    return Pair(0f, 0f)
  }

  private fun getFinalBgBitmap(bgBitMap: Bitmap): Bitmap {
    val finalBitmap = Bitmap.createBitmap(
      bgBitMap.width,
      bgBitMap.height + 1,
      Bitmap.Config.RGB_565
    )
    val canvas = Canvas(finalBitmap)
    val paint = Paint()
    paint.color = Color.BLACK
    canvas.drawBitmap(bgBitMap, 0f, 0f, paint)
    return finalBitmap
  }

  private fun bitmap2Bytes(finalBgBitMap: Bitmap): ByteArray {
    val allocate = ByteBuffer.allocate(finalBgBitMap.byteCount)
    finalBgBitMap.copyPixelsToBuffer(allocate)
    val array = allocate.array()
    return defaultConversion(fileFormat, array, finalBgBitMap.width, 16, 0, false)
  }

  private fun defaultConversion(
    fileFormat: String,
    data: ByteArray,
    w: Int,                      //图片宽度
    bitCount: Int = 16,          //位深度，可为8，16，24，32
    headerInfoSize: Int = 70,   //头部信息长度，默认70
    isReverseRows: Boolean = true   //是否反转行数据，就是将第一行置换为最后一行
  ): ByteArray {
    if (fileFormat == "bmp") {

      //标准bmp文件可以从第十个字节读取到头长度，如果那些设备有问题可以先检查这里
      val headerInfoSize2 = if (headerInfoSize == 0) 0 else data[10]
      LogUtils.d("headerInfoSize $headerInfoSize2")

      val data1 = data.takeLast(data.size - headerInfoSize2)
      //分别获取每一行的数据
      //计算每行多少字节
      val rowSize: Int = (bitCount * w + 31) / 32 * 4
      val data2 = java.util.ArrayList<ByteArray>()
      //判断单、双数，单数要减去无用字节
      val offset = if (w % 2 == 0) 0 else 2
      for (index in 0 until (data1.size / rowSize)) {
        val tmpData = ByteArray(rowSize - offset)
        for (rowIndex in 0 until (rowSize - offset)) {
          tmpData[rowIndex] = data1[index * rowSize + rowIndex]
        }
        data2.add(tmpData)
      }

      //将获得的行数据反转
      val data3 = if (isReverseRows) {
        data2.reversed()
      } else {
        data2
      }
      //将每行数据中，依据 16bit（此处定义，如果是不同的位深度，则需要跟随调整） 即 2 字节的内容从小端序修改为大端序
      val test3 = java.util.ArrayList<Byte>()
      for (index in data3.indices) {
        var j = 0
        while (j < data3[index].size) {
          test3.add(data3[index][j + 1])
          test3.add(data3[index][j])
          j += 2
        }
      }
      //取得最终元素
      val finalData = ByteArray(test3.size)
      for (index in finalData.indices) {
        finalData[index] = test3[index]
      }
      return finalData
    } else {
      //非bmp的目前不需要转换，直接用
      return data
    }
  }

  private fun getTimeDigital(elements: ArrayList<Element>) {
    //AM PM
    val amPmValue = ArrayList<ByteArray>()

    val customDir: String = if (custom == 2) {
      "dial_customize_454"
    } else {
      "dial_customize_240"
    }

    //time
    val TIME_DIR = "$customDir/time"

    //digital
    val DIGITAL_DIR = "$TIME_DIR/digital"
    val tmpBitmap =
      ImageUtils.getBitmap(mContext!!.assets.open("$DIGITAL_DIR/${digitalValueColor}/$DIGITAL_AM_DIR/am.${fileFormat}"))
    var w = tmpBitmap.width
    var h = tmpBitmap.height
    val amValue =
      mContext!!.assets.open("$DIGITAL_DIR/${digitalValueColor}/$DIGITAL_AM_DIR/am.${fileFormat}")
        .use { it.readBytes() }
    val pmValue =
      mContext!!.assets.open("$DIGITAL_DIR/${digitalValueColor}/$DIGITAL_AM_DIR/pm.${fileFormat}")
        .use { it.readBytes() }
    amPmValue.add(defaultConversion(fileFormat, amValue, w))
    amPmValue.add(defaultConversion(fileFormat, pmValue, w))
    val elementAmPm = Element(
      type = WatchFaceBuilder.ELEMENT_DIGITAL_AMPM,
      w = w,
      h = h,
      gravity = WatchFaceBuilder.GRAVITY_X_LEFT or WatchFaceBuilder.GRAVITY_Y_TOP,
      ignoreBlack = ignoreBlack,
      x = amLeftX.toInt(),
      y = amTopY.toInt(),
      imageBuffers = amPmValue.toTypedArray()
    )
    elements.add(elementAmPm)
    //数字时间
    val hourMinute =
      getNumberBuffers("$DIGITAL_DIR/${digitalValueColor}/$DIGITAL_HOUR_MINUTE_DIR/")
    w = hourMinute.first
    h = hourMinute.second
    var valueBuffers = hourMinute.third.toTypedArray()
    val elementHour = Element(
      type = WatchFaceBuilder.ELEMENT_DIGITAL_HOUR,
      w = w,
      h = h,
      gravity = WatchFaceBuilder.GRAVITY_X_LEFT or WatchFaceBuilder.GRAVITY_Y_TOP,
      ignoreBlack = ignoreBlack,
      x = digitalTimeHourLeftX.toInt(),
      y = digitalTimeHourTopY.toInt(),
      imageBuffers = valueBuffers
    )
    elements.add(elementHour)
    val elementMinute = Element(
      type = WatchFaceBuilder.ELEMENT_DIGITAL_MIN,
      w = w,
      h = h,
      gravity = WatchFaceBuilder.GRAVITY_X_LEFT or WatchFaceBuilder.GRAVITY_Y_TOP,
      ignoreBlack = ignoreBlack,
      x = digitalTimeMinuteLeftX.toInt(),
      y = digitalTimeMinuteTopY.toInt(),
      imageBuffers = valueBuffers
    )
    elements.add(elementMinute)
    //特殊元素需要手动传输，部分设备不能直接嵌入背景，那样部分设备可能回出现不一致的问题,例如MTK的设备，实际分辨率320x385,但是最终只用320x363
    if (screenReservedBoundary != 0) {
      getSymbol(
        "$DIGITAL_DIR/${digitalValueColor}/$DIGITAL_HOUR_MINUTE_DIR",
        WatchFaceBuilder.ELEMENT_DIGITAL_DIV_HOUR,
        digitalTimeSymbolLeftX.toInt(),
        digitalTimeSymbolTopY.toInt(),
        elements
      )
    }
    //日期
    val date = getNumberBuffers("$DIGITAL_DIR/${digitalValueColor}/$DIGITAL_DATE_DIR/")
    w = date.first
    h = date.second
    valueBuffers = date.third.toTypedArray()
    val elementMonth = Element(
      type = WatchFaceBuilder.ELEMENT_DIGITAL_MONTH,
      w = w,
      h = h,
      gravity = WatchFaceBuilder.GRAVITY_X_LEFT or WatchFaceBuilder.GRAVITY_Y_TOP,
      ignoreBlack = ignoreBlack,
      x = digitalDateMonthLeftX.toInt(),
      y = digitalDateMonthTopY.toInt(),
      imageBuffers = valueBuffers
    )
    elements.add(elementMonth)
    val elementDay = Element(
      type = WatchFaceBuilder.ELEMENT_DIGITAL_DAY,
      w = w,
      h = h,
      gravity = WatchFaceBuilder.GRAVITY_X_LEFT or WatchFaceBuilder.GRAVITY_Y_TOP,
      ignoreBlack = ignoreBlack,
      x = digitalDateDayLeftX.toInt(),
      y = digitalDateDayTopY.toInt(),
      imageBuffers = valueBuffers
    )
    elements.add(elementDay)
    //特殊元素需要手动传输，不能直接嵌入背景，那样部分设备可能回出现不一致的问题
    if (screenReservedBoundary != 0) {
      getSymbol(
        "$DIGITAL_DIR/${digitalValueColor}/$DIGITAL_DATE_DIR",
        WatchFaceBuilder.ELEMENT_DIGITAL_DIV_MONTH,
        digitalDateSymbolLeftX.toInt(),
        digitalDateSymbolTopY.toInt(),
        elements
      )
    }
    //week
    val week = getNumberBuffers("$DIGITAL_DIR/${digitalValueColor}/$DIGITAL_WEEK_DIR/", 6)
    w = week.first
    h = week.second
    valueBuffers = week.third.toTypedArray()
    val elementWeek = Element(
      type = WatchFaceBuilder.ELEMENT_DIGITAL_WEEKDAY,
      w = w,
      h = h,
      gravity = WatchFaceBuilder.GRAVITY_X_LEFT or WatchFaceBuilder.GRAVITY_Y_TOP,
      ignoreBlack = ignoreBlack,
      x = digitalWeekLeftX.toInt(),
      y = digitalWeekTopY.toInt(),
      imageBuffers = valueBuffers
    )
    elements.add(elementWeek)
  }

  private fun getSymbol(
    dir: String,
    type: Int,
    x: Int, y: Int,
    elements: ArrayList<Element>
  ) {
    val symbolValue = ArrayList<ByteArray>()
    val symbolBitmap =
      ImageUtils.getBitmap(mContext!!.assets.open("${dir}/symbol.${fileFormat}"))
    val w = symbolBitmap.width
    val h = symbolBitmap.height
    symbolValue.add(
      defaultConversion(
        fileFormat,
        mContext!!.assets.open("${dir}/symbol.${fileFormat}")
          .use { it.readBytes() }, w
      )
    )
    val valueBuffers = symbolValue.toTypedArray()
    val elementSymbol = Element(
      type = type,
      w = w,
      h = h,
      gravity = WatchFaceBuilder.GRAVITY_X_LEFT or WatchFaceBuilder.GRAVITY_Y_TOP,
      ignoreBlack = ignoreBlack,
      x = x,
      y = y,
      imageBuffers = valueBuffers
    )
    elements.add(elementSymbol)
  }

  private fun getControl(elements: ArrayList<Element>) {
    val customDir: String = if (custom == 2) {
      "dial_customize_454"
    } else {
      "dial_customize_240"
    }
    //value
    val VALUE_DIR = "$customDir/value"
    val triple = getNumberBuffers("$VALUE_DIR/${valueColor}/", controlValueRange)
    val w = triple.first
    val h = triple.second
    val valueBuffers = triple.third.toTypedArray()
    //获取步数数值
    if (controlViewStep) {
      val elementStep = Element(
        type = WatchFaceBuilder.ELEMENT_DIGITAL_STEP,
        w = w,
        h = h,
        gravity = X_CENTER or Y_CENTER,
        ignoreBlack = ignoreBlack,
        x = stepValueCenterX.toInt(),
        y = stepValueCenterY.toInt(),
        imageBuffers = valueBuffers
      )
      elements.add(elementStep)
    }
    //获取心率数值
    if (controlViewHr) {
      val elementHr = Element(
        type = WatchFaceBuilder.ELEMENT_DIGITAL_HEART,
        w = w,
        h = h,
        gravity = X_CENTER or Y_CENTER,
        ignoreBlack = ignoreBlack,
        x = heartRateValueCenterX.toInt(),
        y = heartRateValueCenterY.toInt(),
        imageBuffers = valueBuffers
      )
      elements.add(elementHr)
    }
    //获取卡路里数值
    if (controlViewCa) {
      val elementCa = Element(
        type = WatchFaceBuilder.ELEMENT_DIGITAL_CALORIE,
        w = w,
        h = h,
        gravity = X_CENTER or Y_CENTER,
        ignoreBlack = ignoreBlack,
        x = caloriesValueCenterX.toInt(),
        y = caloriesValueCenterY.toInt(),
        imageBuffers = valueBuffers
      )
      elements.add(elementCa)
    }
    //获取距离数值
    if (controlViewDis) {
      val elementDis = Element(
        type = WatchFaceBuilder.ELEMENT_DIGITAL_DISTANCE,
        w = w,
        h = h,
        gravity = X_CENTER or Y_CENTER,
        ignoreBlack = ignoreBlack,
        x = distanceValueCenterX.toInt(),
        y = distanceValueCenterY.toInt(),
        imageBuffers = valueBuffers
      )
      LogUtils.d("test distanceValueCenterX=$distanceValueCenterX  distanceValueCenterY=$distanceValueCenterY")
      elements.add(elementDis)
    }
  }

  private fun getNumberBuffers(
    dir: String,
    range: Int = 9
  ): Triple<Int, Int, ArrayList<ByteArray>> {
    var w = 0
    var h = 0
    val valueByte = ArrayList<ByteArray>()
    for (index in 0..range) {
      if (w == 0) {
        val tmpBitmap =
          ImageUtils.getBitmap(mContext!!.assets.open("$dir${index}.${fileFormat}"))
        w = tmpBitmap.width
        h = tmpBitmap.height
      }
      val value =
        mContext!!.assets.open("$dir${index}.${fileFormat}")
          .use { it.readBytes() }
      valueByte.add(defaultConversion(fileFormat, value, w))
    }
    return Triple(w, h, valueByte)
  }

  private fun getPreview(isRound:Boolean,bgBitmax:Bitmap): ByteArray {
    // The size needs to be strictly corresponding, so the background needs to be generated twice
    // Get the background bitmap--with numbers, in preparation for generating a preview
    // Ukuran harus benar-benar sesuai, jadi latar belakang perlu dibuat dua kali
    // Dapatkan bitmap latar belakang--dengan angka, sebagai persiapan untuk membuat pratinjau
    val finalBgBitMap = getBgBitmap(true,isRound,bgBitmax)
    //根据此处表盘背景,生成背景对应的预览文件
    val previewScaleWidth = screenPreviewWidth.toFloat() / finalBgBitMap.width
    val previewScaleHeight = screenPreviewHeight.toFloat() / finalBgBitMap.height
    val previewScale = ImageUtils.scale(
      finalBgBitMap,
      previewScaleWidth,
      previewScaleHeight
    )
    val finalPreviewBitMap = if (isRound) {
      //裁圆,并且加黑边藏锯齿
      ImageUtils.toRound(previewScale, borderSize, Color.parseColor("#FF0000"))
    } else {
      //裁圆,并且加黑边藏锯齿
      ImageUtils.toRoundCorner(
        previewScale,
        roundCornerRadius * previewScaleWidth,
        borderSize.toFloat(),
        Color.parseColor("#FF0000")
      )
    }

    ImageUtils.save(finalPreviewBitMap, File(PathUtils.getExternalAppDataPath(), "dial_bg_preview_file.png"), Bitmap.CompressFormat.PNG)
    return bitmap2Bytes(finalPreviewBitMap)
  }

  val elements = ArrayList<Element>()

  override fun onMethodCall(call: MethodCall, result: Result) {
    this.mResult = result
    when (call.method) {
      "scan" -> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
          PermissionUtils
            .permission(
              PermissionConstants.LOCATION,
              Manifest.permission.BLUETOOTH_SCAN,
              Manifest.permission.BLUETOOTH_CONNECT,
              Manifest.permission.BLUETOOTH_ADVERTISE
            )
            .require(
              PermissionConstants.LOCATION,
              Manifest.permission.BLUETOOTH_SCAN,
              Manifest.permission.BLUETOOTH_CONNECT,
              Manifest.permission.BLUETOOTH_ADVERTISE,
            ) { granted ->
              if (granted) {
                val isScan : Boolean? = call.argument<Boolean>("isScan")
                if (isScan != null) {
                  mBleScanner.scan(isScan)
                }
              }
            }
        } else {
          PermissionUtils
            .permission(PermissionConstants.LOCATION)
            .require(Manifest.permission.ACCESS_FINE_LOCATION) { granted ->
              if (granted) {
                val isScan : Boolean? = call.argument<Boolean>("isScan")
                if (isScan != null) {
                  mBleScanner.scan(isScan)
                }
              }
            }
        }
      }
      "setAddress" -> {
        mBleScanner.scan(false)
        val bmac : String? = call.argument<String>("bmac")
        if (bmac != null) {
          BleConnector.setAddress(bmac)
        }
      }
      "connect" -> {
        BleConnector.connect(true)
      }
      "isConnecting" -> {
        result.success(BleConnector.isConnecting)
      }
      "isNeedBind" -> {
        result.success(BleConnector.isNeedBind)
      }
      "connectHID" -> {
        BleConnector.connectHID();
      }
      "connectClassic" -> {
        BleConnector.connectClassic();
      }
      "closeConnection" -> {
        BleConnector.closeConnection(stopReconnecting = true);
      }
      "unbind" -> {
        BleConnector.unbind();
      }
      "analyzeSleep" -> {
        val listSleep  = call.argument<String>("listSleep")
        if (listSleep != null) {
          var listsome = JSONArray(listSleep)

          var listData = ArrayList<JSONObject>()

          for(i in 0 until listsome.length()){
            listData.add(JSONObject(listsome.get(i).toString()))
          }

          val listNew = ArrayList<BleSleep>()
          for (item in listData) {
            val bleS=BleSleep(mTime =  (item["mTime"]).toString().toInt(), mMode =item["mMode"].toString().toInt() , mSoft = item["mSoft"].toString().toInt(), mStrong =item["mStrong"].toString().toInt() )
            listNew.add(bleS)
          }
          val reversedListNew = listNew.reversed()

          val res = BleSleep.getSleepStatusDuration(sleeps = BleSleep.analyseSleep(reversedListNew))

          val mapSleep = mapOf("light" to res.valueAt(0), "deep" to res.valueAt(1), "awake" to res.valueAt(2), "dateDur" to listData[0].getInt("mDateDur"))
          result.success(mapSleep);
        }
      }
      "customDials" -> {
        elements.clear()
        timeDigitalView  = call.argument<Boolean>("isDigital")!!
        isRound = call.argument<Boolean>("isRound")!!
        val offset = 0
        custom = call.argument<Int>("custom")!!
        screenWidth = call.argument<Int>("screenWidth")!!
        screenHeight = call.argument<Int>("screenHeight")!!
        screenPreviewWidth = call.argument<Int>("screenPreviewWidth")!!
        screenPreviewHeight = call.argument<Int>("screenPreviewHeight")!!

        controlViewStep = call.argument<Boolean>("controlViewStep")!!
        controlViewStepX = call.argument<Int>("controlViewStepX")!!
        controlViewStepY = call.argument<Int>("controlViewStepY")!!

        controlViewCa = call.argument<Boolean>("controlViewCa")!!
        controlViewCaX = call.argument<Int>("controlViewCaX")!!
        controlViewCaY = call.argument<Int>("controlViewCaY")!!

        controlViewDis = call.argument<Boolean>("controlViewDis")!!
        controlViewDisX = call.argument<Int>("controlViewDisX")!!
        controlViewDisY = call.argument<Int>("controlViewDisY")!!

        controlViewHr = call.argument<Boolean>("controlViewHr")!!
        controlViewHrX = call.argument<Int>("controlViewHrX")!!
        controlViewHrY = call.argument<Int>("controlViewHrY")!!

        controlValueInterval = 1
        ignoreBlack = 1
        controlValueRange = 10

        fileFormat = "bmp"
        imageFormat = WatchFaceBuilder.BMP_565
        X_CENTER = WatchFaceBuilder.GRAVITY_X_CENTER_R
        Y_CENTER = WatchFaceBuilder.GRAVITY_Y_CENTER_R

        val bgPreviewBytes : ByteArray? = call.argument<ByteArray?>("bgPreviewBytes")
        val bgPreviewlength = bgPreviewBytes!!.size
        val bgPreviewBitmapX = BitmapFactory.decodeByteArray(bgPreviewBytes, offset, bgPreviewlength)

        val bgPreviewBytesNew = getPreview(isRound!!,bgPreviewBitmapX!!)
        // get the background preview
        val elementPreview = Element(
          type = WatchFaceBuilder.ELEMENT_PREVIEW,
          w = screenPreviewWidth, //预览的尺寸为
          h = screenPreviewHeight,
          gravity = X_CENTER or Y_CENTER,
          x = screenWidth / 2,
          y = screenHeight / 2 + 2,
          imageBuffers = arrayOf(bgPreviewBytesNew!!)
        )
        elements.add(elementPreview)

        // get the background size
        val bgBytes : ByteArray? = call.argument<ByteArray?>("bgBytes")


        val bglength = bgBytes!!.size
        val bgBitmapX = BitmapFactory.decodeByteArray(bgBytes, offset, bglength)

        val bgBytesNew = getBg(isRound!!,bgBitmapX!!)
        val elementBg = Element(
          type = WatchFaceBuilder.ELEMENT_BACKGROUND,
          w = screenWidth, //背景的尺寸
          h = screenHeight,
          gravity = X_CENTER or Y_CENTER,
          x = screenWidth / 2,
          y = screenHeight / 2,
          imageBuffers = arrayOf(bgBytesNew!!)
        )
        elements.add(elementBg)

        // Get the relevant content of the control value
        getControl(elements)

        // Get time related content
        if (timeDigitalView) {
          getTimeDigital(elements)
        }else{
          getPointer(WatchFaceBuilder.ELEMENT_NEEDLE_HOUR, POINTER_HOUR, elements)
          getPointer(WatchFaceBuilder.ELEMENT_NEEDLE_MIN, POINTER_MINUTE, elements)
          getPointer(WatchFaceBuilder.ELEMENT_NEEDLE_SEC, POINTER_SECOND, elements)
        }

        for (element in elements) {
          LogUtils.d("customize dial length: ${element.imageBuffers.first().size * 10 / 1024 / 10.0} KB")
        }

        val bytes = WatchFaceBuilder.build(
          elements.toTypedArray(),
          imageFormat
        )

        LogUtils.d("customize dial bytes size  ${bytes.size}")
        BleConnector.sendStream(
          BleKey.WATCH_FACE,
          bytes
        )

      }
      "musicCommand" /*"MUSIC_CONTROL"*/->{
        val musicControls = arrayOf(
          BleMusicControl(MusicEntity.PLAYER, MusicAttr.PLAYER_NAME, "Music Player"),
          BleMusicControl(MusicEntity.PLAYER, MusicAttr.PLAYER_PLAYBACK_INFO, "${PlaybackState.PAUSED.mState}"),
          BleMusicControl(MusicEntity.PLAYER, MusicAttr.PLAYER_PLAYBACK_INFO, "${PlaybackState.PLAYING.mState}"),
          BleMusicControl(MusicEntity.PLAYER, MusicAttr.PLAYER_PLAYBACK_INFO, "${PlaybackState.REWINDING.mState}"),
          BleMusicControl(MusicEntity.PLAYER, MusicAttr.PLAYER_PLAYBACK_INFO, "${PlaybackState.FAST_FORWARDING.mState}"),
          BleMusicControl(MusicEntity.PLAYER, MusicAttr.PLAYER_VOLUME, String.format("%.2f", Random.nextDouble(0.0, 1.0))),
          BleMusicControl(MusicEntity.QUEUE, MusicAttr.QUEUE_INDEX, "0"),
          BleMusicControl(MusicEntity.QUEUE, MusicAttr.QUEUE_COUNT, "1"),
          BleMusicControl(MusicEntity.TRACK, MusicAttr.TRACK_TITLE, "以父之名"),
        )
        val array = musicControls.map { musicControl ->
          if (musicControl.mMusicEntity == MusicEntity.PLAYER
            && musicControl.mMusicAttr == MusicAttr.PLAYER_PLAYBACK_INFO) {
            "${musicControl.mMusicAttr}_" + when (musicControl.mContent) {
              "${PlaybackState.PAUSED.mState}" -> "PAUSED"
              "${PlaybackState.PLAYING.mState}" -> "PLAYING"
              "${PlaybackState.REWINDING.mState}" -> "REWINDING"
              "${PlaybackState.FAST_FORWARDING.mState}" -> "FAST_FORWARDING"
              else -> "UNKNOWN"
            }
          } else {
            "${musicControl.mMusicAttr}"
          }
        }
        //BleConnector.sendObject(BleKey.MUSIC_CONTROL, BleKeyFlag.UPDATE, musicControls[0])
      }
      else -> {
        when (call.method) {
          "OTA" -> {
            mBleKey=BleKey.OTA
          }
          "XMODEM" -> {
            mBleKey=BleKey.XMODEM
          }
          "TIME" -> {
            mBleKey=BleKey.TIME
          }
          "TIME_ZONE" -> {
            mBleKey=BleKey.TIME_ZONE
          }
          "POWER" -> {
            mBleKey=BleKey.POWER
          }
          "FIRMWARE_VERSION" -> {
            mBleKey=BleKey.FIRMWARE_VERSION
          }
          "BLE_ADDRESS" -> {
            mBleKey=BleKey.BLE_ADDRESS
          }
          "USER_PROFILE" -> {
            mBleKey=BleKey.USER_PROFILE
          }
          "STEP_GOAL" -> {
            mBleKey=BleKey.STEP_GOAL
          }
          "BACK_LIGHT" -> {
            mBleKey=BleKey.BACK_LIGHT
          }
          "SEDENTARINESS" -> {
            mBleKey=BleKey.SEDENTARINESS
          }
          "NO_DISTURB_RANGE" -> {
            mBleKey=BleKey.NO_DISTURB_RANGE
          }
          "VIBRATION" -> {
            mBleKey=BleKey.VIBRATION
          }
          "GESTURE_WAKE" -> {
            mBleKey=BleKey.GESTURE_WAKE
          }
          "HR_ASSIST_SLEEP" -> {
            mBleKey=BleKey.HR_ASSIST_SLEEP
          }
          "HOUR_SYSTEM" -> {
            mBleKey=BleKey.HOUR_SYSTEM
          }
          "LANGUAGE" -> {
            mBleKey=BleKey.LANGUAGE
          }
          "ALARM" -> {
            mBleKey=BleKey.ALARM
          }
          "COACHING" -> {
            mBleKey=BleKey.COACHING
          }
          "FIND_PHONE" -> {
            mBleKey=BleKey.FIND_PHONE
          }
          "NOTIFICATION_REMINDER" -> {
            mBleKey=BleKey.NOTIFICATION_REMINDER
          }
          "ANTI_LOST" -> {
            mBleKey=BleKey.ANTI_LOST
          }
          "HR_MONITORING" -> {
            mBleKey=BleKey.HR_MONITORING
          }
          "UI_PACK_VERSION" -> {
            mBleKey=BleKey.UI_PACK_VERSION
          }
          "LANGUAGE_PACK_VERSION" -> {
            mBleKey=BleKey.LANGUAGE_PACK_VERSION
          }
          "SLEEP_QUALITY" -> {
            mBleKey=BleKey.SLEEP_QUALITY
          }
          "GIRL_CARE" -> {
            mBleKey=BleKey.GIRL_CARE
          }
          "TEMPERATURE_DETECTING" -> {
            mBleKey=BleKey.TEMPERATURE_DETECTING
          }
          "AEROBIC_EXERCISE" -> {
            mBleKey=BleKey.AEROBIC_EXERCISE
          }
          "TEMPERATURE_UNIT" -> {
            mBleKey=BleKey.TEMPERATURE_UNIT
          }
          "DATE_FORMAT" -> {
            mBleKey=BleKey.DATE_FORMAT
          }
          "WATCH_FACE_SWITCH" -> {
            mBleKey=BleKey.WATCH_FACE_SWITCH
          }
          "AGPS_PREREQUISITE" -> {
            mBleKey=BleKey.AGPS_PREREQUISITE
          }
          "DRINK_WATER" -> {
            mBleKey=BleKey.DRINK_WATER
          }
          "SHUTDOWN" -> {
            mBleKey=BleKey.SHUTDOWN
          }
          "APP_SPORT_DATA" -> {
            mBleKey=BleKey.APP_SPORT_DATA
          }
          "REAL_TIME_HEART_RATE" -> {
            mBleKey=BleKey.REAL_TIME_HEART_RATE
          }
          "BLOOD_OXYGEN_SET" -> {
            mBleKey=BleKey.BLOOD_OXYGEN_SET
          }
          "WASH_SET" -> {
            mBleKey=BleKey.WASH_SET
          }
          "WATCHFACE_ID" -> {
            mBleKey=BleKey.WATCHFACE_ID
          }
          "IBEACON_SET" -> {
            mBleKey=BleKey.IBEACON_SET
          }
          "MAC_QRCODE" -> {
            mBleKey=BleKey.MAC_QRCODE
          }
          "REAL_TIME_TEMPERATURE" -> {
            mBleKey=BleKey.REAL_TIME_TEMPERATURE
          }
          "REAL_TIME_BLOOD_PRESSURE" -> {
            mBleKey=BleKey.REAL_TIME_BLOOD_PRESSURE
          }
          "TEMPERATURE_VALUE" -> {
            mBleKey=BleKey.TEMPERATURE_VALUE
          }
          "GAME_SET" -> {
            mBleKey=BleKey.GAME_SET
          }
          "FIND_WATCH" -> {
            mBleKey=BleKey.FIND_WATCH
          }
          "SET_WATCH_PASSWORD" -> {
            mBleKey=BleKey.SET_WATCH_PASSWORD
          }
          "REALTIME_MEASUREMENT" -> {
            mBleKey=BleKey.REALTIME_MEASUREMENT
          }
          "LOCATION_GSV" -> {
            mBleKey=BleKey.LOCATION_GSV
          }
          "HR_RAW" -> {
            mBleKey=BleKey.HR_RAW
          }
          "REALTIME_LOG" -> {
            mBleKey=BleKey.REALTIME_LOG
          }
          "GSENSOR_OUTPUT" -> {
            mBleKey=BleKey.GSENSOR_OUTPUT
          }
          "GSENSOR_RAW" -> {
            mBleKey=BleKey.GSENSOR_RAW
          }
          "MOTION_DETECT" -> {
            mBleKey=BleKey.MOTION_DETECT
          }
          "LOCATION_GGA" -> {
            mBleKey=BleKey.LOCATION_GGA
          }
          "RAW_SLEEP" -> {
            mBleKey=BleKey.RAW_SLEEP
          }
          "NO_DISTURB_GLOBAL" -> {
            mBleKey=BleKey.NO_DISTURB_GLOBAL
          }
          "IDENTITY" -> {
            mBleKey=BleKey.IDENTITY
          }
          "SESSION" -> {
            mBleKey=BleKey.SESSION
          }
          "NOTIFICATION" -> {
            mBleKey=BleKey.NOTIFICATION
          }
          "MUSIC_CONTROL" -> {
            mBleKey=BleKey.MUSIC_CONTROL
          }
          "SCHEDULE" -> {
            mBleKey=BleKey.SCHEDULE
          }
          "WEATHER_REALTIME" -> {
            mBleKey=BleKey.WEATHER_REALTIME
          }
          "WEATHER_FORECAST" -> {
            mBleKey=BleKey.WEATHER_FORECAST
          }
          "HID" -> {
            mBleKey=BleKey.HID
          }
          "WORLD_CLOCK" -> {
            mBleKey=BleKey.WORLD_CLOCK
          }
          "STOCK" -> {
            mBleKey=BleKey.STOCK
          }
          "SMS_QUICK_REPLY_CONTENT" -> {
            mBleKey=BleKey.SMS_QUICK_REPLY_CONTENT
          }
          "NOTIFICATION2" -> {
            mBleKey=BleKey.NOTIFICATION2
          }
          "DATA_ALL" -> {
            mBleKey=BleKey.DATA_ALL
          }
          "ACTIVITY_REALTIME" -> {
            mBleKey=BleKey.ACTIVITY_REALTIME
          }
          "ACTIVITY" -> {
            mBleKey=BleKey.ACTIVITY
          }
          "HEART_RATE" -> {
            mBleKey=BleKey.HEART_RATE
          }
          "SLEEP" -> {
            mBleKey=BleKey.SLEEP
          }
          "LOCATION" -> {
            mBleKey=BleKey.LOCATION
          }
          "TEMPERATURE" -> {
            mBleKey=BleKey.TEMPERATURE
          }
          "BLOOD_OXYGEN" -> {
            mBleKey=BleKey.BLOOD_OXYGEN
          }
          "HRV" -> {
            LogUtils.d("HRV get at line 2450")
            mBleKey=BleKey.HRV
          }
          "LOG" -> {
            mBleKey=BleKey.LOG
          }
          "SLEEP_RAW_DATA" -> {
            mBleKey=BleKey.SLEEP_RAW_DATA
          }
          "PRESSURE" -> {
            mBleKey=BleKey.PRESSURE
          }
          "WORKOUT2" -> {
            mBleKey=BleKey.WORKOUT2
          }
          "MATCH_RECORD" -> {
            mBleKey=BleKey.MATCH_RECORD
          }
          "CAMERA" -> {
            mBleKey=BleKey.CAMERA
          }
          "REQUEST_LOCATION" -> {
            mBleKey=BleKey.REQUEST_LOCATION
          }
          "INCOMING_CALL" -> {
            mBleKey=BleKey.INCOMING_CALL
          }
          "APP_SPORT_STATE" -> {
            mBleKey=BleKey.APP_SPORT_STATE
          }
          "CLASSIC_BLUETOOTH_STATE" -> {
            mBleKey=BleKey.CLASSIC_BLUETOOTH_STATE
          }
          "DEVICE_SMS_QUICK_REPLY" -> {
            mBleKey=BleKey.DEVICE_SMS_QUICK_REPLY
          }
          "WATCH_FACE" -> {
            mBleKey=BleKey.WATCH_FACE
          }
          "AGPS_FILE" -> {
            mBleKey=BleKey.AGPS_FILE
          }
          "FONT_FILE" -> {
            mBleKey=BleKey.FONT_FILE
          }
          "CONTACT" -> {
            mBleKey=BleKey.CONTACT
          }
          "UI_FILE" -> {
            mBleKey=BleKey.UI_FILE
          }
          "DEVICE_FILE" -> {
            mBleKey=BleKey.DEVICE_FILE
          }
          "LANGUAGE_FILE" -> {
            mBleKey=BleKey.LANGUAGE_FILE
          }
          "BRAND_INFO_FILE" -> {
            mBleKey=BleKey.BRAND_INFO_FILE
          }
          "BLOOD_PRESSURE" -> {
            mBleKey=BleKey.BLOOD_PRESSURE
          }
          else -> {
            mBleKey=BleKey.NONE
          }
        }
        when(call.argument<String>("flag")){
          "UPDATE" -> {
            mBleKeyFlag=BleKeyFlag.UPDATE
          }
          "READ" -> {
            mBleKeyFlag=BleKeyFlag.READ
          }
          "CREATE" -> {
            mBleKeyFlag=BleKeyFlag.CREATE
          }
          "DELETE" -> {
            mBleKeyFlag=BleKeyFlag.DELETE
          }
          "READ_CONTINUE" -> {
            mBleKeyFlag=BleKeyFlag.READ_CONTINUE
          }
          "RESET" -> {
            mBleKeyFlag=BleKeyFlag.RESET
          }
          else -> {
            mBleKeyFlag=BleKeyFlag.NONE
          }
        }
        setupKeyFlagList(mBleKey,mBleKeyFlag,call)
      }
    }
  }

  // 显示该BleKey对应的Flag列表
  private fun setupKeyFlagList(bleKey: BleKey,bleKeyFlag:BleKeyFlag, call: MethodCall) {
    if (bleKey == BleKey.IDENTITY) {
      if (bleKeyFlag == BleKeyFlag.DELETE) { // unbind
        // Send the unbinding command, some devices will trigger BleHandleCallback.onIdentityDelete() after replying
        BleConnector.sendData(bleKey, bleKeyFlag)
        // wait for a while to unbind
        Handler().postDelayed({
          BleConnector.unbind()
          unbindCompleted()
        }, 2000)
        return
      }
    }

    if (mContext != null) {
      doBle(mContext!!) {
        when (bleKey) {
          // BleCommand.UPDATE
//          BleKey.OTA -> FirmwareHelper.gotoOta(mContext)
//          BleKey.WATCH_FACE -> {
////                findViewById<TextView>(R.id.tv_custom1).apply {
////                    visibility = View.VISIBLE
////                    text = "custom 240*240"
////                    setOnClickListener {
////                        startActivity(
////                            Intent(
////                                this@KeyFlagListActivity,
////                                WatchFaceActivity::class.java
////                            ).putExtra("custom", 1)
////                        )
////                    }
////                }
////
////                findViewById<TextView>(R.id.tv_custom2).apply {
////                    visibility = View.VISIBLE
////                    text = "custom 454*454"
////                    setOnClickListener {
////                        startActivity(
////                            Intent(
////                                this@KeyFlagListActivity,
////                                WatchFaceActivity::class.java
////                            ).putExtra("custom", 2)
////                        )
////                    }
////                }
//          }
          BleKey.XMODEM -> BleConnector.sendData(bleKey, bleKeyFlag)
          BleKey.NOTIFICATION -> {
            val mTitle: String? = call.argument<String>("mTitle")
            val mContent : String? = call.argument<String>("mContent")
            val mCategory : String? = call.argument<String>("mCategory")
            val mPackage : String? = call.argument<String>("mPackage")
            when (bleKeyFlag) {
              BleKeyFlag.UPDATE -> {
                BleNotification(
                  mCategory = if(mCategory=="1"){
                    BleNotification.CATEGORY_INCOMING_CALL
                  }else{
                    BleNotification.CATEGORY_MESSAGE
                  },
                  mTime = Date().time,
                  mPackage = "$mPackage",
                  mTitle = "$mTitle",
                  mContent = "$mContent"
                ).let { notification ->
                  BleConnector.sendObject(BleKey.NOTIFICATION, BleKeyFlag.UPDATE, notification)
                }
              }
              BleKeyFlag.READ -> {
                BleConnector.sendData(bleKey, bleKeyFlag)
              }
              BleKeyFlag.DELETE -> {
                BleNotification(
                  mCategory = if(mCategory=="1"){
                    BleNotification.CATEGORY_INCOMING_CALL
                  }else{
                    BleNotification.CATEGORY_MESSAGE
                  },
                ).let { notification ->
                  BleConnector.sendObject(BleKey.NOTIFICATION, BleKeyFlag.DELETE, notification)
                }
              }
              else -> {
                Log.i("No Flag","")
              }
            }
          }
          BleKey.NOTIFICATION2 -> {
            val mTitle: String? = call.argument<String>("mTitle")
            val mContent : String? = call.argument<String>("mContent")
            val mCategory : String? = call.argument<String>("mCategory")
            val mPackage : String? = call.argument<String>("mPackage")
            val mPhone : String? = call.argument<String>("mPhone")
            when (bleKeyFlag) {
              BleKeyFlag.UPDATE -> {
                BleNotification2(
                  mCategory = if(mCategory=="1"){
                    BleNotification.CATEGORY_INCOMING_CALL
                  }else{
                    BleNotification.CATEGORY_MESSAGE
                  },
                  mTime = Date().time,
                  mPackage = "$mPackage",
                  mTitle = "$mTitle",
                  mPhone = "$mPhone",
                  mContent = "$mContent"
                ).let { notification ->
                  BleConnector.sendObject(BleKey.NOTIFICATION2, BleKeyFlag.UPDATE, notification)
                }
              }
              BleKeyFlag.READ -> {
                BleConnector.sendData(bleKey, bleKeyFlag)
              }
              BleKeyFlag.DELETE -> {
                BleNotification2(
                  mCategory = if(mCategory=="1"){
                    BleNotification.CATEGORY_INCOMING_CALL
                  }else{
                    BleNotification.CATEGORY_MESSAGE
                  },
                ).let { notification ->
                  BleConnector.sendObject(BleKey.NOTIFICATION2, BleKeyFlag.DELETE, notification)
                }
              }
              else -> {
                Log.i("No Flag","")
              }
            }
          }
          // BleCommand.SET
          BleKey.TIME -> {
            if (bleKeyFlag == BleKeyFlag.UPDATE) {
              // 设置时间, 设置时间之前必须先设置时区
              BleConnector.sendObject(bleKey, bleKeyFlag, BleTime.local())
            } else if (bleKeyFlag == BleKeyFlag.READ) {
              BleConnector.sendData(bleKey, bleKeyFlag)
            }
          }
          BleKey.TIME_ZONE -> {
            if (bleKeyFlag == BleKeyFlag.UPDATE) {
              // 设置时区
              BleConnector.sendObject(bleKey, bleKeyFlag, BleTimeZone())
            } else if (bleKeyFlag == BleKeyFlag.READ) {
              BleConnector.sendData(bleKey, bleKeyFlag)
            }
          }
          BleKey.POWER -> BleConnector.sendData(bleKey, bleKeyFlag) // 读取电量
          BleKey.FIRMWARE_VERSION -> BleConnector.sendData(bleKey, bleKeyFlag) // 读取固件版本
          BleKey.BLE_ADDRESS -> BleConnector.sendData(bleKey, bleKeyFlag) // 读取BLE蓝牙地址
          BleKey.USER_PROFILE -> {
            if (bleKeyFlag == BleKeyFlag.UPDATE) {
              // 设置用户信息
              val bleUserProfile = BleUserProfile(
                mUnit = BleUserProfile.METRIC,
                mGender = BleUserProfile.FEMALE,
                mAge = 20,
                mHeight = 170f,
                mWeight = 60f
              )
              BleConnector.sendObject(bleKey, bleKeyFlag, bleUserProfile)
            } else if (bleKeyFlag == BleKeyFlag.READ) {
              BleConnector.sendData(bleKey, bleKeyFlag)
            }
          }
          BleKey.STEP_GOAL -> {
            if (bleKeyFlag == BleKeyFlag.UPDATE) {
              // 设置目标步数
              BleConnector.sendInt32(bleKey, bleKeyFlag, 10)
            } else if (bleKeyFlag == BleKeyFlag.READ) {
              BleConnector.sendData(bleKey, bleKeyFlag)
            }
          }
          BleKey.BACK_LIGHT -> {
            if (bleKeyFlag == BleKeyFlag.UPDATE) {
              // 设置背光时长
              val times: Int? = call.argument<Int>("times")
              if (times != null) {
                BleConnector.sendInt8(bleKey, bleKeyFlag, times)
              } // 0 is off, or 5 ~ 20
            } else if (bleKeyFlag == BleKeyFlag.READ) {
              BleConnector.sendData(bleKey, bleKeyFlag)
            }
          }
          BleKey.SEDENTARINESS -> {
            if (bleKeyFlag == BleKeyFlag.UPDATE) {
              val mEnabled: Int? = call.argument<Int>("mEnabled")
              val mRepeat: Int? = call.argument<Int>("mRepeat")
              val mStartHour: Int? = call.argument<Int>("mStartHour")
              val mStartMinute: Int? = call.argument<Int>("mStartMinute")
              val mEndHour: Int? = call.argument<Int>("mEndHour")
              val mEndMinute: Int? = call.argument<Int>("mEndMinute")
              val mInterval: Int? = call.argument<Int>("mInterval")
              val listRepeat : List<String>? = call.argument<List<String>>("listRepeat")
              var bleRepeat : Int? = null
              if(listRepeat!=null){
                bleRepeat=0
                for (item in listRepeat) {
                  var itemRepeat : Int? = null
                  when (item){
                    "MONDAY" -> {
                      itemRepeat =BleRepeat.MONDAY
                    }
                    "TUESDAY" -> {
                      itemRepeat =BleRepeat.TUESDAY
                    }
                    "THURSDAY" -> {
                      itemRepeat =BleRepeat.THURSDAY
                    }
                    "FRIDAY" -> {
                      itemRepeat =BleRepeat.FRIDAY
                    }
                    "SATURDAY" -> {
                      itemRepeat =BleRepeat.SATURDAY
                    }
                    "SUNDAY" -> {
                      itemRepeat =BleRepeat.SUNDAY
                    }
                    "ONCE" -> {
                      itemRepeat =BleRepeat.ONCE
                    }
                    "WORKDAY" -> {
                      itemRepeat =BleRepeat.WORKDAY
                    }
                    "WEEKEND" -> {
                      itemRepeat =BleRepeat.WEEKEND
                    }
                    "EVERYDAY" -> {
                      itemRepeat =BleRepeat.EVERYDAY
                    }
                  }
                  bleRepeat = bleRepeat!!.or(itemRepeat!!)
                }
              }
              /*if (mRepeat != null) {
                when (mRepeat){
                  "MONDAY" -> {
                    bleRepeat =BleRepeat.MONDAY
                  }
                  "TUESDAY" -> {
                    bleRepeat =BleRepeat.TUESDAY
                  }
                  "THURSDAY" -> {
                    bleRepeat =BleRepeat.THURSDAY
                  }
                  "FRIDAY" -> {
                    bleRepeat =BleRepeat.FRIDAY
                  }
                  "SATURDAY" -> {
                    bleRepeat =BleRepeat.SATURDAY
                  }
                  "SUNDAY" -> {
                    bleRepeat =BleRepeat.SUNDAY
                  }
                  "ONCE" -> {
                    bleRepeat =BleRepeat.ONCE
                  }
                  "WORKDAY" -> {
                    bleRepeat =BleRepeat.WORKDAY
                  }
                  "WEEKEND" -> {
                    bleRepeat =BleRepeat.WEEKEND
                  }
                  "EVERYDAY" -> {
                    bleRepeat =BleRepeat.EVERYDAY
                  }
                }
              }*/
              // 设置久坐 set sedentary
              val bleSedentariness = BleSedentarinessSettings(
                mEnabled = mEnabled!!,
                // Monday ~ Saturday
                //mRepeat = bleRepeat!!,
                mRepeat = mRepeat!!,
                mStartHour = mStartHour!!,
                mStartMinute = mStartMinute!!,
                mEndHour = mEndHour!!,
                mEndMinute = mEndMinute!!,
                mInterval = mInterval!!
              )
              BleConnector.sendObject(bleKey, bleKeyFlag, bleSedentariness)
              //LogUtils.d("bleSedentary : $bleSedentariness")
            } else if (bleKeyFlag == BleKeyFlag.READ) {
              BleConnector.sendData(bleKey, bleKeyFlag)
            }
          }
          BleKey.NO_DISTURB_RANGE -> {
            if (bleKeyFlag == BleKeyFlag.UPDATE) {
              // 设置勿扰
              val noDisturb = BleNoDisturbSettings().apply {
                mBleTimeRange1 = BleTimeRange(1, 2, 0, 18, 0)
              }
              BleConnector.sendObject(bleKey, bleKeyFlag, noDisturb)
            } else if (bleKeyFlag == BleKeyFlag.READ) {
              BleConnector.sendData(bleKey, bleKeyFlag)
            }
          }
          BleKey.NO_DISTURB_GLOBAL -> {
            val isDoNotDistrub: Boolean? = call.argument<Boolean>("isDoNotDistrub")
            if (isDoNotDistrub != null) {
              BleConnector.sendBoolean(
                bleKey,
                bleKeyFlag,
                isDoNotDistrub
              )
            }
          }
          BleKey.VIBRATION -> {
            val frequency: Int? = call.argument<Int>("frequency")
            if (bleKeyFlag == BleKeyFlag.UPDATE) {
              // 设置震动次数
              if (frequency != null) {
                BleConnector.sendInt8(bleKey, bleKeyFlag, frequency)
              } // 0~10, 0 is off
            } else if (bleKeyFlag == BleKeyFlag.READ) {
              BleConnector.sendData(bleKey, bleKeyFlag)
            }
          }
          BleKey.GESTURE_WAKE -> {
            if (bleKeyFlag == BleKeyFlag.UPDATE) {
              // 设置抬手亮
              val mEnabled: Int? = call.argument<Int>("mEnabled")
              val mStartHour: Int? = call.argument<Int>("mStartHour")
              val mStartMinute: Int? = call.argument<Int>("mStartMinute")
              val mEndHour: Int? = call.argument<Int>("mEndHour")
              val mEndMinute: Int? = call.argument<Int>("mEndMinute")
              BleConnector.sendObject(
                bleKey, bleKeyFlag,
                BleGestureWake(BleTimeRange(mEnabled!!, mStartHour!!, mStartMinute!!, mEndHour!!, mEndMinute!!))
              )
            } else if (bleKeyFlag == BleKeyFlag.READ) {
              BleConnector.sendData(bleKey, bleKeyFlag)
            }
          }
//                BleKey.HR_ASSIST_SLEEP -> BleConnector.sendBoolean(bleKey, bleKeyFlag, true) // on
          // 设置小时制
          BleKey.HOUR_SYSTEM ->
            // 切换, 0: 24-hourly; 1: 12-hourly
            BleConnector.sendInt8(bleKey, bleKeyFlag, BleCache.getInt(bleKey, 0) xor 1)
          // 设置设备语言
          BleKey.LANGUAGE -> BleConnector.sendInt8(
            bleKey,
            bleKeyFlag,
            Languages.languageToCode()
          )
          BleKey.ALARM -> {
            // 创建一个1分钟后的闹钟
            val index: Int? = call.argument<Int>("index")
            val mEnabled: Int? = call.argument<Int>("mEnabled")
            val mRepeat: String? = call.argument<String>("mRepeat")
            val mYear: Int? = call.argument<Int>("mYear")
            val mMonth: Int? = call.argument<Int>("mMonth")
            val mDay: Int? = call.argument<Int>("mDay")
            val mHour: Int? = call.argument<Int>("mHour")
            val mMinute: Int? = call.argument<Int>("mMinute")
            val mTag: String? = call.argument<String>("mTag")
            val listRepeat : List<String>? = call.argument<List<String>>("listRepeat")
            var bleRepeat : Int? = null
            if(listRepeat!=null){
              bleRepeat=0
              for (item in listRepeat) {
                var itemRepeat : Int? = null
                when (item){
                  "MONDAY" -> {
                    itemRepeat =BleRepeat.MONDAY
                  }
                  "TUESDAY" -> {
                    itemRepeat =BleRepeat.TUESDAY
                  }
                  "THURSDAY" -> {
                    itemRepeat =BleRepeat.THURSDAY
                  }
                  "FRIDAY" -> {
                    itemRepeat =BleRepeat.FRIDAY
                  }
                  "SATURDAY" -> {
                    itemRepeat =BleRepeat.SATURDAY
                  }
                  "SUNDAY" -> {
                    itemRepeat =BleRepeat.SUNDAY
                  }
                  "ONCE" -> {
                    itemRepeat =BleRepeat.ONCE
                  }
                  "WORKDAY" -> {
                    itemRepeat =BleRepeat.WORKDAY
                  }
                  "WEEKEND" -> {
                    itemRepeat =BleRepeat.WEEKEND
                  }
                  "EVERYDAY" -> {
                    itemRepeat =BleRepeat.EVERYDAY
                  }
                }
                bleRepeat = bleRepeat!!.or(itemRepeat!!)
              }
            }
            if (mRepeat != null) {
              when (mRepeat){
                "MONDAY" -> {
                  bleRepeat =BleRepeat.MONDAY
                }
                "TUESDAY" -> {
                  bleRepeat =BleRepeat.TUESDAY
                }
                "THURSDAY" -> {
                  bleRepeat =BleRepeat.THURSDAY
                }
                "FRIDAY" -> {
                  bleRepeat =BleRepeat.FRIDAY
                }
                "SATURDAY" -> {
                  bleRepeat =BleRepeat.SATURDAY
                }
                "SUNDAY" -> {
                  bleRepeat =BleRepeat.SUNDAY
                }
                "ONCE" -> {
                  bleRepeat =BleRepeat.ONCE
                }
                "WORKDAY" -> {
                  bleRepeat =BleRepeat.WORKDAY
                }
                "WEEKEND" -> {
                  bleRepeat =BleRepeat.WEEKEND
                }
                "EVERYDAY" -> {
                  bleRepeat =BleRepeat.EVERYDAY
                }
              }
            }
            if (bleKeyFlag == BleKeyFlag.CREATE) {
//              val calendar = Calendar.getInstance().apply { add(Calendar.MINUTE, 1) }
              BleConnector.sendObject(
                bleKey, bleKeyFlag,
//                BleAlarm(
//                  mEnabled = 1,
//                  mRepeat = BleRepeat.,
//                  mYear = calendar.get(Calendar.YEAR),
//                  mMonth = calendar.get(Calendar.MONTH) + 1,
//                  mDay = calendar.get(Calendar.DAY_OF_MONTH),
//                  mHour = calendar.get(Calendar.HOUR_OF_DAY),
//                  mMinute = calendar.get(Calendar.MINUTE),
//                  mTag = "tag"
//                )
                BleAlarm(
                  mEnabled = mEnabled!!,
                  mRepeat = mRepeat!!.toInt(),
                  mYear = mYear!!,
                  mMonth = mMonth!!,
                  mDay = mDay!!,
                  mHour = mHour!!,
                  mMinute = mMinute!!,
                  mTag = mTag!!
                )
              )
            } else if (bleKeyFlag == BleKeyFlag.DELETE) {
              // 如果缓存中有闹钟的话，删除第一个
              val alarms = BleCache.getList(BleKey.ALARM, BleAlarm::class.java)
              if (alarms.isNotEmpty()) {
                BleConnector.sendInt8(bleKey, bleKeyFlag, alarms[index!!].mId)
              }
            } else if (bleKeyFlag == BleKeyFlag.UPDATE) {
              // 如果缓存中有闹钟的话，切换第一个闹钟的开启状态
              val alarms = BleCache.getList(BleKey.ALARM, BleAlarm::class.java)
              if (alarms.isNotEmpty()) {
                alarms[index!!].let { alarm ->
                  if (mEnabled != null) {
                    alarm.mEnabled = mEnabled
                  }
                  if (bleRepeat != null) {
                    alarm.mRepeat = bleRepeat
                  }
                  if (mYear != null) {
                    alarm.mYear = mYear
                  }
                  if (mMonth != null) {
                    alarm.mMonth = mMonth
                  }
                  if (mDay != null) {
                    alarm.mDay = mDay
                  }
                  if (mHour != null) {
                    alarm.mHour = mHour
                  }
                  if (mMinute != null) {
                    alarm.mMinute = mMinute
                  }
                  if (mTag != null) {
                    alarm.mTag = mTag
                  }
                  BleConnector.sendObject(bleKey, bleKeyFlag, alarm)
                }
              }
            } else if (bleKeyFlag == BleKeyFlag.READ) {
              // 读取设备上所有的闹钟
              BleConnector.sendInt8(bleKey, bleKeyFlag, ID_ALL)
            } else if (bleKeyFlag == BleKeyFlag.RESET) {
              // 重置设备上的闹钟
              val calendar = Calendar.getInstance()
              BleConnector.sendList(bleKey, bleKeyFlag, List(8) {
                BleAlarm(
                  mEnabled = it.rem(2),
                  mRepeat = it,
                  mYear = calendar.get(Calendar.YEAR),
                  mMonth = calendar.get(Calendar.MONTH) + 1,
                  mDay = calendar.get(Calendar.DAY_OF_MONTH),
                  mHour = calendar.get(Calendar.HOUR_OF_DAY),
                  mMinute = it,
                  mTag = "$it"
                )
              })
            }
          }
          BleKey.COACHING -> {
            if (bleKeyFlag == BleKeyFlag.CREATE) {
              BleConnector.sendObject(
                bleKey, bleKeyFlag, BleCoaching(
                  "My title", // title
                  "My description", // description
                  3, // repeat
                  listOf(
                    BleCoachingSegment(
                      CompletionCondition.DURATION.condition, // completion condition
                      "My name", // name
                      0, // activity
                      Stage.WARM_UP.stage, // stage
                      10, // completion value
                      0 // hr zone
                    )
                  )
                )
              )
            } else if (bleKeyFlag == BleKeyFlag.UPDATE) {
              // 如果缓存中有Coaching的话，修改第一个Coaching的标题
              val coachings = BleCache.getList(BleKey.COACHING, BleCoaching::class.java)
              if (coachings.isNotEmpty()) { // update the first coaching
                coachings[0].let { coaching ->
                  coaching.mTitle += "nice"
                  BleConnector.sendObject(bleKey, bleKeyFlag, coaching)
                }
              }
            } else if (bleKeyFlag == BleKeyFlag.READ) {
              // 读取所有Coaching
              BleConnector.sendInt8(bleKey, bleKeyFlag, ID_ALL)
            }
          }
          // 设置是否开启消息推送
          BleKey.NOTIFICATION_REMINDER ->
            BleConnector.sendBoolean(
              bleKey,
              bleKeyFlag,
              !BleCache.getBoolean(bleKey, false)
            ) // 切换
          // 设置防丢提醒
          BleKey.ANTI_LOST -> {
            val isAntiLost: Boolean? = call.argument<Boolean>("isAntiLost")
            if (isAntiLost != null) {
              BleConnector.sendBoolean(
                bleKey,
                bleKeyFlag,
                isAntiLost
                // !BleCache.getBoolean(bleKey, false)
              )
            } // 切换
          }
          // 设置心率自动检测
          BleKey.HR_MONITORING -> {
            if (bleKeyFlag == BleKeyFlag.UPDATE) {
              val mEnabled: Int? = call.argument<Int>("mEnabled")
              val mStartHour: Int? = call.argument<Int>("mStartHour")
              val mStartMinute: Int? = call.argument<Int>("mStartMinute")
              val mEndHour: Int? = call.argument<Int>("mEndHour")
              val mEndMinute: Int? = call.argument<Int>("mEndMinute")
              val mInterval: Int? = call.argument<Int>("mInterval")
              val hrMonitoring = BleHrMonitoringSettings(
                mBleTimeRange = BleTimeRange(mEnabled!!, mStartHour!!, mStartMinute!!, mEndHour!!, mEndMinute!!),
                mInterval = mInterval!! // an hour
              )
              BleConnector.sendObject(bleKey, bleKeyFlag, hrMonitoring)
            } else if (bleKeyFlag == BleKeyFlag.READ) {
              BleConnector.sendData(bleKey, bleKeyFlag)
            }
          }
          // 读取UI包版本
          BleKey.UI_PACK_VERSION -> BleConnector.sendData(bleKey, bleKeyFlag)
          // 读取语言包版本
          BleKey.LANGUAGE_PACK_VERSION -> BleConnector.sendData(bleKey, bleKeyFlag)
          // 发送睡眠质量
          BleKey.SLEEP_QUALITY -> BleConnector.sendObject(
            bleKey, bleKeyFlag,
            BleSleepQuality(mLight = 202, mDeep = 201, mTotal = 481)
          )
          // 设置女性健康提醒
          BleKey.GIRL_CARE -> BleConnector.sendObject(
            bleKey, bleKeyFlag,
            BleGirlCareSettings(
              mEnabled = 1,
              mReminderHour = 9,
              mReminderMinute = 0,
              mMenstruationReminderAdvance = 2,
              mOvulationReminderAdvance = 2,
              mLatestYear = 2020,
              mLatestMonth = 1,
              mLatestDay = 1,
              mMenstruationDuration = 7,
              mMenstruationPeriod = 30
            )
          )
          // 设置温度检测
          BleKey.TEMPERATURE_DETECTING -> BleConnector.sendObject(
            bleKey, bleKeyFlag,
            BleTemperatureDetecting(
              mBleTimeRange = BleTimeRange(1, 8, 0, 22, 0),
              mInterval = 60 // an hour
            )
          )
          // 设置有氧运动
          BleKey.AEROBIC_EXERCISE -> {
            if (bleKeyFlag == BleKeyFlag.UPDATE) {
              BleConnector.sendObject(
                bleKey, bleKeyFlag,
                BleAerobicExerciseSettings(
                  mExerciseHour = 1,
                  mExerciseMinute = 30,
                  mHrMin = 50,
                  mHrMax = 150,
                  mLowHrMinMinute = 10,
                  mLowHrMinVibration = 2,
                  mHighHrMaxMinute = 20,
                  mHighHrMaxVibration = 2,
                  mLowOrHighHrDuration = 30,
                )
              )
            } else if (bleKeyFlag == BleKeyFlag.READ) {
              BleConnector.sendData(bleKey, bleKeyFlag)
            }
          }
          // 温度单位设置
          BleKey.TEMPERATURE_UNIT ->{
            val unit: Int? = call.argument<Int>("unit")
            // Beralih, 0: Celcius; 1: Fahrenheit
            BleConnector.sendInt8(bleKey, bleKeyFlag, unit!!)
          }
          // 日期格式设置
          BleKey.DATE_FORMAT -> {
            // 切换, 0: 年月日; 1: 日月年; 2: 月日年;
            // switch, 0: year month day; 1: day month year; 2: month day year;
            val format: Int? = call.argument<Int>("format")
            if (format != null) {
              BleConnector.sendInt8(bleKey, bleKeyFlag, format)
            }
          }
          BleKey.RAW_SLEEP ->
            BleConnector.sendData(bleKey, bleKeyFlag)

          //Default dial switch settings
          BleKey.WATCH_FACE_SWITCH ->
            // value, 0-4;
            BleConnector.sendInt8(bleKey, bleKeyFlag, (BleCache.getInt(bleKey, 0) + 1) % 5)

          // dial id
          BleKey.WATCHFACE_ID -> {
            if (bleKeyFlag == BleKeyFlag.UPDATE) {
              mType = 1//Posisi tampilan jam di perangkat
              BleConnector.sendInt32(bleKey, bleKeyFlag, 100)// hanya >=100 atau tidak valid
            } else if (bleKeyFlag == BleKeyFlag.READ) {
              BleConnector.sendData(bleKey, bleKeyFlag)
            }
          }

          //IBEACON_SET
          BleKey.IBEACON_SET -> {
            //安卓绑定成功后，强制让设备关闭这个功能，省电
            BleConnector.sendInt8(bleKey, bleKeyFlag, 0)
          }

          //二维码的进制转换设置
          BleKey.MAC_QRCODE -> {
            //0x01:十进制 0x02:十六进制
            BleConnector.sendInt8(bleKey, bleKeyFlag, 0x01)
          }

          BleKey.SET_WATCH_PASSWORD -> {
            BleConnector.sendObject(
              bleKey,
              bleKeyFlag,
              BleSettingWatchPassword(mEnabled = 1, mPassword = "1234")
            )
          }

          BleKey.REALTIME_MEASUREMENT -> {
            BleConnector.sendObject(
              bleKey,
              bleKeyFlag,
              BleRealTimeMeasurement(mHRSwitch = 0, mBOSwitch = 1, mBPSwitch = 0)
            )
          }

          //BleCommand.CONNECT
          BleKey.IDENTITY ->
            if (bleKeyFlag == BleKeyFlag.CREATE) {
              // 绑定设备, 外部无需手动调用, 框架内部会自动发送该指令
              BleConnector.sendInt32(bleKey, bleKeyFlag, Random.nextInt())
            } else if (bleKeyFlag == BleKeyFlag.READ) {
              BleConnector.sendData(bleKey, bleKeyFlag)
            }

          // BleCommand.PUSH
          BleKey.MUSIC_CONTROL -> {
            if(bleKeyFlag == BleKeyFlag.UPDATE){
              val mTitle: String? = call.argument<String>("mTitle")
              val mContent: String? = call.argument<String>("mContent")

              var bleMusicTitle = BleMusicControl(MusicEntity.TRACK, MusicAttr.TRACK_TITLE, mTitle.toString())
              var bleMusicArtist = BleMusicControl(MusicEntity.TRACK, MusicAttr.TRACK_ARTIST, mContent.toString())
              BleConnector.sendObject(BleKey.MUSIC_CONTROL, BleKeyFlag.UPDATE, bleMusicTitle)
              BleConnector.sendObject(BleKey.MUSIC_CONTROL, BleKeyFlag.UPDATE, bleMusicArtist)
            }
          }
          BleKey.SCHEDULE -> {
            if (bleKeyFlag == BleKeyFlag.CREATE) {
              // 创建一个1分钟后的日程
              val calendar = Calendar.getInstance().apply { add(Calendar.MINUTE, 1) }
              BleConnector.sendObject(
                bleKey, bleKeyFlag,
                BleSchedule(
                  mYear = calendar.get(Calendar.YEAR),
                  mMonth = calendar.get(Calendar.MONTH) + 1,
                  mDay = calendar.get(Calendar.DAY_OF_MONTH),
                  mHour = calendar.get(Calendar.HOUR_OF_DAY),
                  mMinute = calendar.get(Calendar.MINUTE),
                  mAdvance = 0,
                  mTitle = "Title8",
                  mContent = "Content9"
                )
              )
            } else if (bleKeyFlag == BleKeyFlag.DELETE) {
              // 如果缓存中有日程的话，删除第一个
              val schedules = BleCache.getList(BleKey.SCHEDULE, BleSchedule::class.java)
              if (schedules.isNotEmpty()) {
                BleConnector.sendInt8(bleKey, bleKeyFlag, schedules[0].mId)
              }
            } else if (bleKeyFlag == BleKeyFlag.UPDATE) {
              // 如果缓存中有日程的话，修改第一个日程的时间
              val schedules = BleCache.getList(BleKey.SCHEDULE, BleSchedule::class.java)
              if (schedules.isNotEmpty()) {
                schedules[0].let { schedule ->
                  schedule.mHour = Random.nextInt(23)
                  BleConnector.sendObject(bleKey, bleKeyFlag, schedule)
                }
              }
            }
          }
          // 发送实时天气
          BleKey.WEATHER_REALTIME -> {
            if (bleKeyFlag == BleKeyFlag.UPDATE) {
              BleConnector.sendObject(
                BleKey.WEATHER_REALTIME, bleKeyFlag, BleWeatherRealtime(
                  mTime = (Date().time / 1000L).toInt(),
                  mWeather = BleWeather(
                    mCurrentTemperature = 1,
                    mMaxTemperature = 1,
                    mMinTemperature = 1,
                    mWeatherCode = BleWeather.RAINY,
                    mWindSpeed = 1,
                    mHumidity = 1,
                    mVisibility = 1,
                    mUltraVioletIntensity = 1,
                    mPrecipitation = 1
                  )
                )
              )
            }
          }
          // 发送天气预备
          BleKey.WEATHER_FORECAST -> {
            if (bleKeyFlag == BleKeyFlag.UPDATE) {
              BleConnector.sendObject(
                BleKey.WEATHER_FORECAST, bleKeyFlag, BleWeatherForecast(
                  mTime = (Date().time / 1000L).toInt(),
                  mWeather1 = BleWeather(
                    mCurrentTemperature = 2,
                    mMaxTemperature = 2,
                    mMinTemperature = 3,
                    mWeatherCode = BleWeather.CLOUDY,
                    mWindSpeed = 2,
                    mHumidity = 2,
                    mVisibility = 2,
                    mUltraVioletIntensity = 2,
                    mPrecipitation = 2
                  ),
                  mWeather2 = BleWeather(
                    mCurrentTemperature = 3,
                    mMaxTemperature = 3,
                    mMinTemperature = 3,
                    mWeatherCode = BleWeather.OVERCAST,
                    mWindSpeed = 3,
                    mHumidity = 3,
                    mVisibility = 3,
                    mUltraVioletIntensity = 3,
                    mPrecipitation = 3
                  ),
                  mWeather3 = BleWeather(
                    mCurrentTemperature = 4,
                    mMaxTemperature = 4,
                    mMinTemperature = 4,
                    mWeatherCode = BleWeather.RAINY,
                    mWindSpeed = 4,
                    mHumidity = 4,
                    mVisibility = 4,
                    mUltraVioletIntensity = 4,
                    mPrecipitation = 4
                  )
                )
              )
            }
          }
          // HID
          BleKey.HID -> {
            BleConnector.sendData(bleKey, bleKeyFlag)
          }
          //世界时钟
          BleKey.WORLD_CLOCK -> {
            // 创建一个时钟
            if (bleKeyFlag == BleKeyFlag.CREATE) {
              BleConnector.sendObject(
                bleKey, bleKeyFlag,
                BleWorldClock(
                  isLocal = 0,
                  mTimeZoneOffset = TimeZone.getDefault().rawOffset / 1000 / 60 / 15,
                  mCityName = "本地时间"
                )
              )
            } else if (bleKeyFlag == BleKeyFlag.DELETE) {
              // 如果缓存中有时钟的话，删除第一个
              val clocks = BleCache.getList(BleKey.WORLD_CLOCK, BleWorldClock::class.java)
              if (clocks.isNotEmpty()) {
                BleConnector.sendInt8(bleKey, bleKeyFlag, clocks[0].mId)
              }
            } else if (bleKeyFlag == BleKeyFlag.UPDATE) {
              val clocks = BleCache.getList(BleKey.WORLD_CLOCK, BleWorldClock::class.java)
              if (clocks.isNotEmpty()) {
                clocks[0].let { clock ->
                  clock.isLocal = if (clock.isLocal == 0) 1 else 0
                  BleConnector.sendObject(bleKey, bleKeyFlag, clock)
                }
              }
            } else if (bleKeyFlag == BleKeyFlag.READ) {
              // 读取设备上所有的时钟
              BleConnector.sendInt8(bleKey, bleKeyFlag, ID_ALL)
            }
          }
          //股票
          BleKey.STOCK -> {
            // 创建一个股票
            if (bleKeyFlag == BleKeyFlag.CREATE) {
              BleConnector.sendObject(
                bleKey, bleKeyFlag,
                BleStock(
                  mStockCode = "AAPL",
                  mSharePrice = 148.470f,
                  mNetChangePoint = 2.980f,
                  mNetChangePercent = 2.05f,
                  mMarketCapitalization = 2.40f
                )
              )
            } else if (bleKeyFlag == BleKeyFlag.DELETE) {
              // 如果缓存中有股票的话，删除第一个
              val stocks = BleCache.getList(BleKey.STOCK, BleStock::class.java)
              if (stocks.isNotEmpty()) {
                BleConnector.sendInt8(bleKey, bleKeyFlag, stocks[0].mId)
              }
            } else if (bleKeyFlag == BleKeyFlag.UPDATE) {
              val stocks = BleCache.getList(BleKey.STOCK, BleStock::class.java)
              if (stocks.isNotEmpty()) {
                stocks[0].let { stock ->
                  stock.mSharePrice = stock.mSharePrice + 1
                  BleConnector.sendObject(bleKey, bleKeyFlag, stock)
                }
              }
            } else if (bleKeyFlag == BleKeyFlag.READ) {
              // 读取设备上所有的股票
              BleConnector.sendInt8(bleKey, bleKeyFlag, ID_ALL)
            }
          }
          //短信快捷回复
          BleKey.SMS_QUICK_REPLY_CONTENT -> {
            if (bleKeyFlag == BleKeyFlag.CREATE) {
              BleConnector.sendObject(
                bleKey, bleKeyFlag,
                BleSMSQuickReplyContent(
                  mContent = "我正在会议中，${Random.nextInt(10)}请稍后再联系我"
                )
              )
            } else if (bleKeyFlag == BleKeyFlag.DELETE) {
              // 如果缓存中有内容的话，删除第一个
              val smsQuickReply = BleCache.getList(
                BleKey.SMS_QUICK_REPLY_CONTENT,
                BleSMSQuickReplyContent::class.java
              )
              if (smsQuickReply.isNotEmpty()) {
                BleConnector.sendInt8(bleKey, bleKeyFlag, smsQuickReply[0].mId)
              }
            } else if (bleKeyFlag == BleKeyFlag.UPDATE) {
              // 如果缓存中有内容的话，修改第一个
              val smsQuickReply = BleCache.getList(
                BleKey.SMS_QUICK_REPLY_CONTENT,
                BleSMSQuickReplyContent::class.java
              )
              if (smsQuickReply.isNotEmpty()) {
                smsQuickReply[0].let { smsQuickReply ->
                  smsQuickReply.mContent = "我正在会议中，${Random.nextInt(10)}请稍后再联系我"
                  BleConnector.sendObject(bleKey, bleKeyFlag, smsQuickReply)
                }
              }
            }
          }
          // BleCommand.DATA
          BleKey.DATA_ALL,BleKey.ACTIVITY, BleKey.ACTIVITY_REALTIME, BleKey.HEART_RATE,BleKey.REAL_TIME_HEART_RATE, BleKey.BLOOD_PRESSURE, BleKey.SLEEP,
          BleKey.WORKOUT, BleKey.LOCATION, BleKey.TEMPERATURE, BleKey.BLOOD_OXYGEN, BleKey.HRV, BleKey.LOG, BleKey.WORKOUT2 ->
            // 读取数据
            BleConnector.sendData(bleKey, bleKeyFlag)

          BleKey.APP_SPORT_DATA -> {
            // App-led movement, sending the data calculated by the App to the device during the movement
            sportMode = when (call.argument<Int>("sportMode")!!) {
              1 -> BleActivity.INDOOR
              2 -> BleActivity.OUTDOOR
              3 -> BleActivity.CYCLING
              4 -> BleActivity.CLIMBING
              else -> BleActivity.INDOOR
            }
            val reply = BleAppSportData(
              mDuration = call.argument<Int>("mDuration")!!,
              mAltitude = call.argument<Int>("mAltitude")!!,
              mAirPressure = call.argument<Int>("mAirPressure")!!,
              mSpm = call.argument<Int>("mSpm")!!,
              mMode = sportMode,
              mStep = call.argument<Int>("mStep")!!,
              mDistance = call.argument<Int>("mDistance")!!,
              mCalorie = call.argument<Int>("mCalorie")!!,
              mSpeed = call.argument<Int>("mSpeed")!!,
              mPace = call.argument<Int>("mPace")!!,
            )
            BleConnector.sendObject(bleKey, bleKeyFlag, reply)
            print("$reply")
            LogUtils.d(reply)
          }

          // BleCommand.CONTROL
          BleKey.CAMERA -> {
            val mCameraEntered: Boolean? = call.argument<Boolean>("mCameraEntered")
            if (mCameraEntered!!) {
              BleConnector.sendInt8(bleKey, bleKeyFlag, CameraState.ENTER)
            } else {
              BleConnector.sendInt8(bleKey, bleKeyFlag, CameraState.EXIT)
            }
          }
          BleKey.REQUEST_LOCATION -> {
            // reply location information
            val reply = BleLocationReply(
              mSpeed = call.argument<Float>("mSpeed")!!,
              mTotalDistance = call.argument<Float>("mTotalDistance")!!,
              mAltitude = call.argument<Int>("mAltitude")!!
            )
//            print("$bleKey $bleKeyFlag")
            BleConnector.sendObject(bleKey, bleKeyFlag, reply)
//            print("$reply")
//            mLocationTimes++
//            if (mLocationTimes > 10) mLocationTimes = 1
          }
          BleKey.APP_SPORT_STATE -> {
            // App-led movement, sending movement state changes
            sportState = when (call.argument<Int>("sportState")!!) {
              1 -> BleAppSportState.STATE_START
              2 -> BleAppSportState.STATE_RESUME
              3 -> BleAppSportState.STATE_PAUSE
              4 -> BleAppSportState.STATE_END
              else -> BleAppSportState.STATE_START
            }
            sportMode = when (call.argument<Int>("sportMode")!!) {
              1 -> BleActivity.INDOOR
              2 -> BleActivity.OUTDOOR
              3 -> BleActivity.CYCLING
              4 -> BleActivity.CLIMBING
              else -> BleActivity.INDOOR
            }
            val reply = BleAppSportState(
              mMode = sportMode,
              mState = sportState,
            )
            BleConnector.sendObject(bleKey, bleKeyFlag, reply)
            print("$reply")
            LogUtils.d(reply)
          }
          BleKey.INCOMING_CALL -> {
            BleConnector.sendData(bleKey, bleKeyFlag, /*null, true*/)
          }
          BleKey.CLASSIC_BLUETOOTH_STATE -> {
            // 3.0 开关
            when (mClassicBluetoothState) {
              ClassicBluetoothState.CLOSE_SUCCESSFULLY -> {
                mClassicBluetoothState = ClassicBluetoothState.OPEN
                BleConnector.sendInt8(bleKey, bleKeyFlag, mClassicBluetoothState)
              }
              ClassicBluetoothState.OPEN_SUCCESSFULLY -> {
                mClassicBluetoothState = ClassicBluetoothState.CLOSE
                BleConnector.sendInt8(bleKey, bleKeyFlag, mClassicBluetoothState)
              }
              else -> {
                //3.0状态正在切换中，请稍等
                print("3.0 status is switching, please wait")
              }
            }
          }

          // BleCommand.IO
          BleKey.WATCH_FACE, BleKey.AGPS_FILE, BleKey.FONT_FILE, BleKey.UI_FILE, BleKey.LANGUAGE_FILE, BleKey.BRAND_INFO_FILE -> {
            if (bleKeyFlag == BleKeyFlag.UPDATE) {
              // 发送文件
              val url: String? = call.argument<String>("url")
              if(url!=null){
                DownloadTask(bleKey).execute(url)
//                val executor = Executors.newSingleThreadExecutor()
//                val future = executor.submit {
//                  val url = URL(url)
//                  val connection = url.openConnection()
//                  connection.getInputStream()
//                }
//                val inputStream = future.get()
//                if (inputStream != null) {
//                  BleConnector.sendStream(bleKey ,inputStream as InputStream,0)
//                }
              }
              val path: String? = call.argument<String>("path")
              if(path!=null){
                val inputStream: InputStream = mContext!!.assets.open(path)
                BleConnector.sendStream(bleKey ,inputStream,0)
              }
            }

          }
//          BleKey.CONTACT -> {
//            PermissionUtils
//              .permission(PermissionConstants.CONTACTS)
//              .require(Manifest.permission.READ_CONTACTS) { granted ->
//                if (granted) {
//                  val bytes = getContactBytes()
//                  if (bytes.isNotEmpty()) {
//                    BleConnector.sendStream(BleKey.CONTACT, bytes)
//                  } else {
//                    LogUtils.d("contact is empty")
//                  }
//                }
//              }
//          }
          BleKey.CONTACT -> {
            val listContact : List<Map<String,String>>? = call.argument<List<Map<String,String>>>("listContact")
            val contact = ArrayList<Contact>()
            if (listContact != null) {
              for (cont in listContact) {
                contact.add(Contact(cont["displayName"]!!, cont["phone"]!!))
              }
            }
            //Firmware drafting: name 24 and phone number 16 bytes, so create an array here based on the data size
            val bytes = ByteArray(contact.size * 40)
            for (index in contact.indices) {
              val nameBytes = contact[index].name.toByteArray()
              for (valueIndex in nameBytes.indices) {
                if (valueIndex < 24) {
                  bytes[index * 40 + valueIndex] = nameBytes[valueIndex]
                }
              }
              val phoneBytes = contact[index].phone.toByteArray()
              for (valueIndex in phoneBytes.indices) {
                if (valueIndex < 16) {
                  bytes[index * 40 + 24 + valueIndex] = phoneBytes[valueIndex]
                }
              }
            }
            if (bytes.isNotEmpty()) {
              BleConnector.sendStream(BleKey.CONTACT, bytes)
            } else {
              LogUtils.d("contact is empty")
            }
          }
          BleKey.DEVICE_FILE -> {
            if (bleKeyFlag == BleKeyFlag.READ) {
              BleConnector.sendData(bleKey, bleKeyFlag)
            }
          }

          else -> {
            print("$bleKey")
          }
        }
      }
    }
  }


  private fun unbindCompleted() {

  }

  override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
//    channel.setMethodCallHandler(null)
//    BleConnector.removeHandleCallback(mBleHandleCallback)
//   mBleScanner.exit()
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activityBinding = binding
    mActivity = binding.activity
  }

  override fun onDetachedFromActivityForConfigChanges() {

  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {

  }

  override fun onDetachedFromActivity() {

  }


  private val scanResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        scanSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onDeviceConnectedResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onDeviceConnectedSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onIdentityCreateResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onIdentityCreateSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onCommandReplyResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onCommandReplySink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onOTAResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onOTASink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadPowerResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadPowerSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadFirmwareVersionResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadFirmwareVersionSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadBleAddressResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadBleAddressSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadSedentarinessResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadSedentarinessSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadNoDisturbResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadNoDisturbSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadAlarmResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadAlarmSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadCoachingIdsResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadCoachingIdsSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadUiPackVersionResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadUiPackVersionSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadLanguagePackVersionResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadLanguagePackVersionSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onIdentityDeleteByDeviceResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onIdentityDeleteByDeviceSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onCameraStateChangeResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onCameraStateChangeSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onCameraResponseResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onCameraResponseSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onSyncDataResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onSyncDataSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadActivityResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadActivitySink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadHeartRateResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadHeartRateSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onUpdateHeartRateResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onUpdateHeartRateSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadBloodPressureResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadBloodPressureSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadSleepResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadSleepSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadLocationResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadLocationSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadTemperatureResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadTemperatureSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadWorkout2ResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadWorkout2Sink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onStreamProgressResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onStreamProgressSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onUpdateAppSportStateResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onUpdateAppSportStateSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onClassicBluetoothStateChangeResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onClassicBluetoothStateChangeSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onDeviceFileUpdateResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onDeviceFileUpdateSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }


  private val onReadDeviceFileResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadDeviceFileSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadTemperatureUnitResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadTemperatureUnitSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }



  private val onReadDateFormatResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadDateFormatSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadWatchFaceSwitchResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadWatchFaceSwitchSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onUpdateWatchFaceSwitchResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onUpdateWatchFaceSwitchSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onAppSportDataResponseResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onAppSportDataResponseSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadWatchFaceIdResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadWatchFaceIdSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onWatchFaceIdUpdateResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onWatchFaceIdUpdateSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onHIDStateResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onHIDStateSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onHIDValueChangeResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onHIDValueChangeSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }



  private val onDeviceSMSQuickReplyResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onDeviceSMSQuickReplySink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }


  private val onReadDeviceInfoResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadDeviceInfoSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onSessionStateChangeResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onSessionStateChangeSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onNoDisturbUpdateResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onNoDisturbUpdateSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onAlarmUpdateResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onAlarmUpdateSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onAlarmDeleteResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onAlarmDeleteSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onAlarmAddResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onAlarmAddSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onFindPhoneResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onFindPhoneSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onRequestLocationResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onRequestLocationSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onDeviceRequestAGpsFileResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onDeviceRequestAGpsFileSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadBloodOxygenResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadBloodOxygenSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadWorkoutResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadWorkoutSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadBleHrvResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadBleHrvSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReadPressureResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReadPressureSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onDeviceConnectingResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onDeviceConnectingSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }
  private val onIncomingCallStatusResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onIncomingCallStatusSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

  private val onReceiveMusicCommandResultsHandler: EventChannel.StreamHandler = object : EventChannel.StreamHandler {
    override fun onListen(o: Any?, eventSink: EventSink?) {
      if (eventSink != null) {
        onReceiveMusicCommandSink = eventSink
      }
    }
    override fun onCancel(o: Any?) {
    }
  }

}


class DownloadTask(val bleKey: BleKey) : AsyncTask<String, Int, ByteArray>() {

  override fun doInBackground(vararg urls: String): ByteArray {
    val url = URL(urls[0])
    val connection = url.openConnection()
    return connection.getInputStream().readBytes()
  }

  override fun onPostExecute(result: ByteArray) {
    BleConnector.sendStream(bleKey ,result)
  }
}
