package com.szabh.smable3.component

import android.bluetooth.BluetoothDevice
import com.szabh.smable3.BleKey
import com.szabh.smable3.BleKeyFlag
import com.szabh.smable3.entity.*

/**
 * 事件是在主线程派发，所以不要在回调中做耗时操作
 */
interface BleHandleCallback {

    /**
     * 设备连接成功时触发。
     */
    fun onDeviceConnected(device: BluetoothDevice) {}

    /**
     * 绑定时触发。
     */
    fun onIdentityCreate(status: Boolean, deviceInfo: BleDeviceInfo? = null) {}

    /**
     * 解绑时触发。
     * 有些设备解绑会触发，但有些不会
     */
    fun onIdentityDelete(status: Boolean) {}

    /**
     * 设备主动解绑时触发。例如设备恢复出厂设置
     */
    fun onIdentityDeleteByDevice(isDevice: Boolean) {}

    /**
     * 连接状态变化时触发。
     */
    fun onSessionStateChange(status: Boolean) {}

    /**
     * 设备回复某些指令时触发。
     */
    fun onCommandReply(bleKey: BleKey, bleKeyFlag: BleKeyFlag, status: Boolean) {}

    /**
     * 设备进入OTA时触发。
     */
    fun onOTA(status: Boolean) {}

    /**
     * MTK设备返回固件信息，该信息需要通过[BleConnector.SERVICE_MTK]和[BleConnector.CH_MTK_OTA_META]来读取，
     * 设备返回该信息后会通过[BleCache.putMtkOtaMeta]保存该信息，然后通过[BleCache.getMtkOtaMeta]可以获取该信息。
     * mid=xx;mod=xx;oem=xx;pf=xx;p_id=xx;p_sec=xx;ver=xx;d_ty=xx;
     */
    fun onReadMtkOtaMeta() {}

    fun onXModem(status: Byte) {}

    /**
     * 设备返回电量时触发。
     */
    fun onReadPower(power: Int) {}

    /**
     * 设备返回固件版本时触发。
     */
    fun onReadFirmwareVersion(version: String) {}

    /**
     * 设备返回mac地址时触发。
     */
    fun onReadBleAddress(address: String) {}

    /**
     * 设备返回久坐设置时触发。
     */
    fun onReadSedentariness(sedentarinessSettings: BleSedentarinessSettings) {}

    /**
     * 设备返回勿扰设置时触发。
     */
    fun onReadNoDisturb(noDisturbSettings: BleNoDisturbSettings) {}

    /**
     * 设备端修改勿扰设置时触发。
     */
    fun onNoDisturbUpdate(noDisturbSettings: BleNoDisturbSettings) {}

    /**
     * 设备返回闹钟列表时触发。
     */
    fun onReadAlarm(alarms: List<BleAlarm>) {}

    /**
     * 设备端修改闹钟时触发。
     */
    fun onAlarmUpdate(alarm: BleAlarm) {}

    /**
     * 设备端删除闹钟时触发。
     */
    fun onAlarmDelete(id: Int) {}

    /**
     * 设备端创建闹钟时触发。
     */
    fun onAlarmAdd(alarm: BleAlarm) {}

    /**
     * 设备返回Coaching id时触发。
     */
    fun onReadCoachingIds(bleCoachingIds: BleCoachingIds) {}

    /**
     * 当设备发起找手机触发。
     */
    fun onFindPhone(start: Boolean) {}

    /**
     * 设备返回UI包版本时触发，[BleDeviceInfo.PLATFORM_REALTEK]专属。
     */
    fun onReadUiPackVersion(version: String) {}

    /**
     * 设备返回语言包信息时触发，[BleDeviceInfo.PLATFORM_REALTEK]专属。
     */
    fun onReadLanguagePackVersion(version: BleLanguagePackVersion) {}

    fun onReadSleepQuality(sleepQuality: BleSleepQuality) {}

    fun onRequestAgpsPrerequisite(){}

    fun onReceiveRealtimeLog(realtimeLog: BleRealtimeLog) {}

    /**
     * 设备返回定位GGA数据时触发
     */
    fun onReceiveLocationGga(locationGga: BleLocationGga) {}

    /**
     * 设备返回定位过程中的GSV数据时触发
     */
    fun onReceiveLocationGsv(locationGsv: List<BleLocationGsv>) {}

    /**
     * 设备点击音乐相关按键时触发。
     */
    fun onReceiveMusicCommand(musicCommand: MusicCommand) {}

    /**
     * 同步数据时触发。
     * @param syncState [SyncState]
     * @param bleKey [BleKey]
     */
    fun onSyncData(syncState: Int, bleKey: BleKey) {}

    /**
     * 当设备返回[BleActivity]时触发。
     */
    fun onReadActivity(activities: List<BleActivity>) {}

    /**
     * 发送读取命令后，设备返回[BleHeartRate]时触发。
     */
    fun onReadHeartRate(heartRates: List<BleHeartRate>) {}

    /**
     * 当设备主动更新[BleHeartRate]时触发。
     */
    fun onUpdateHeartRate(heartRate: BleHeartRate) {}

    /**
     * 当设备返回[BleBloodPressure]时触发。
     */
    fun onReadBloodPressure(bloodPressures: List<BleBloodPressure>) {}

    /**
     * 当设备返回[BleSleep]时触发。
     */
    fun onReadSleep(sleeps: List<BleSleep>) {}

    /**
     * 当设备返回[BleWorkout]时触发。
     */
    fun onReadWorkout(workouts: List<BleWorkout>) {}

    /**
     * 当设备返回[BleLocation]时触发。
     */
    fun onReadLocation(locations: List<BleLocation>) {}

    /**
     * 当设备返回[BleTemperature]时触发。
     */
    fun onReadTemperature(temperatures: List<BleTemperature>) {}

    /**
     * 当设备返回[BleBloodOxygen]时触发。
     */
    fun onReadBloodOxygen(bloodOxygen: List<BleBloodOxygen>) {}

    /**
     * 当设备返回[BleHrv]时触发。
     */
    fun onReadBleHrv(hrv: List<BleHrv>) {}

    /**
     * 当设备返回[BleLogText]时触发。
     */
    fun onReadBleLogText(logs: List<BleLogText>) {}

    /**
     * 设备主动执行拍照相关操作时触发。
     * @param cameraState [CameraState]
     */
    fun onCameraStateChange(cameraState: Int) {}

    /**
     * 手机执行拍照相关操作，设备回复时触发。
     * 手机发起后设备响应。用于确认设备是否能立即响应手机发起的操作，比如设备在某些特定界面是不能进入相机的，
     * 如果手机发起进入相机指令，设备会回复失败
     * @param cameraState [CameraState]
     */
    fun onCameraResponse(status: Boolean, cameraState: Int) {}

    /**
     * 设备主动返回3.0开关状态时触发。
     * @param state [ClassicBluetoothState]
     */
    fun onClassicBluetoothStateChange(state: Int) {}

    /**
     * 调用[BleConnector.sendStream]后触发，用于回传发送进度。
     */
    fun onStreamProgress(status: Boolean, errorCode: Int, total: Int, completed: Int) {}

    /**
     * 设备请求定位时触发，一些无Gps设备在锻炼时会请求手机定位。
     * @param workoutState [WorkoutState]
     */
    fun onRequestLocation(workoutState: Int) {}

    /**
     * 设备开启Gps时，如果检测到没有aGps文件，或aGps文件已过期，设备发起请求aGps文件
     * @param url aGps文件的下载链接
     */
    fun onDeviceRequestAGpsFile(url: String) {}

    /**
     * 当设备控制来电时触发。  0 -接听 ； 1-拒接
     * @param url aGps文件的下载链接
     */
    fun onIncomingCallStatus(status: Int) {}

    /**
     * 当用户从设备控制运动状态变化时触发
     */
    fun onUpdateAppSportState(appSportState: BleAppSportState) {}

    /**
     * 当设备本地文件增加，或者变化，通知App可以获取新文件时触发
     */
    fun onDeviceFileUpdate(deviceFile: BleDeviceFile) {}

    /**
     * 读取设备本地存储的文件，App端需要循环读取
     */
    fun onReadDeviceFile(deviceFile: BleDeviceFile) {}

    /**
     * 当设备返回睡眠原始数据时触发,针对此原始数据，直接保存即可
     */
    fun onReadSleepRaw(sleepRawData: ByteArray) {}

    /**
     * 当设备返回[BlePressure]时触发。
     */
    fun onReadPressure(pressures: List<BlePressure>) {}

    /**
     * 设备语言设置选择跟随手机时触发
     */
    fun onFollowSystemLanguage(status: Boolean) {}

    /**
     * 设备端主动请求读取天气数据
     */
    fun onReadWeatherRealTime(status: Boolean) {}

    fun onReceiveGSensorRaw(gSensorRaws: List<BleGSensorRaw>) {}

    fun onReceiveGSensorMotion(gSensorMotions: List<BleGSensorMotion>) {}

    /**
     * 设备端主动返回心率原始数据
     */
    fun onReceiveHRRaw(hrRaw: List<BleHRRaw>) {}

    /**
     * 返回当前设备温度单位
     */
    fun onReadTemperatureUnit(value: Int) {}

    /**
     * 返回当前设备日期格式
     */
    fun onReadDateFormat(value: Int) {}

    /**
     * 返回当前设备表盘值
     */
    fun onReadWatchFaceSwitch(value: Int) {}

    /**
     * 设备切换默认表盘时触发
     * 如果失败有可能是设置的值超过范围或者当前设备没有同步表盘
     */
    fun onUpdateWatchFaceSwitch(status: Boolean) {}

    /**
     * 设备返回喝水提醒设置时触发。
     */
    fun onReadDrinkWater(drinkWaterSettings: BleDrinkWaterSettings) {}

    /**
     * 当设备返回[BleWorkout2]时触发。
     */
    fun onReadWorkout2(workouts: List<BleWorkout2>) {}

    /**
     * 设备发送某条指令超时触发。
     */
    fun onCommandSendTimeout(bleKey: BleKey, bleKeyFlag: BleKeyFlag) {}

    /**
     * 当设备返回[BleMatchRecord]时触发。
     */
    fun onReadMatchRecord(matchRecords: List<BleMatchRecord>) {}

    /**
     * 当App发送了运动期间的数据，固件返回确认时触发
     */
    fun onAppSportDataResponse(status: Boolean) {}

    /**
     * [BleConnector.isConnecting]发生变化时触发
     */
    fun onDeviceConnecting(status: Boolean) {}

    /**
     * 设备端修改震动设置时触发, 返回次数
     */
    fun onVibrationUpdate(value: Int) {}

    /**
     * 当设备返回[BleWatchFaceId]时触发。
     */
    fun onReadWatchFaceId(watchFaceId: BleWatchFaceId) {}

    /**
     * 表盘id设置成功
     */
    fun onWatchFaceIdUpdate(status: Boolean) {}

    /**
     * 设备返回HID状态时触发。
     * @param state [HIDState]
     */
    fun onHIDState(state: Int) {}

    /**
     * 设备返回HID值时触发。
     * @param value [HIDValue]
     */
    fun onHIDValueChange(value: Int) {}

    /**
     * 当设备主动更新[BleTemperature]时触发。
     */
    fun onUpdateTemperature(temperature: BleTemperature) {}

    /**
     * 当设备主动更新[BleBloodPressure]时触发。
     */
    fun onUpdateBloodPressure(bloodPressure: BleBloodPressure) {}

    /**
     * 设备返回世界时钟列表时触发。
     */
    fun onReadWorldClock(clocks: List<BleWorldClock>) {}

    /**
     * 设备端删除世界时钟时触发。
     */
    fun onWorldClockDelete(id: Int) {}

    /**
     * 设备返回股票列表时触发。
     */
    fun onReadStock(stocks: List<BleStock>) {}

    /**
     * 设备端删除股票时触发。
     */
    fun onStockDelete(id: Int) {}

    /**
     * 设备端主动请求股票数据
     */
    fun onReadStock(status: Boolean) {}

    /**
     * 设备端选择快捷回复短信
     */
    fun onDeviceSMSQuickReply(smsQuickReply: BleSMSQuickReply) {}

    /**
     * 当设备返回[BleRealTimeMeasurement]时触发。
     */
    fun onRealTimeMeasurement(realTimeMeasurement: BleRealTimeMeasurement) {}

    /**
     * 当读取设备信息时返回
     */
    fun onReadDeviceInfo(deviceInfo: BleDeviceInfo) {}
}
