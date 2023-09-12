#if 0
#elif defined(__arm64__) && __arm64__
// Generated by Apple Swift version 5.8 (swiftlang-5.8.0.124.2 clang-1403.0.22.11.100)
#ifndef ALICLOUDIOTKITADVANCE_SWIFT_H
#define ALICLOUDIOTKITADVANCE_SWIFT_H
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wgcc-compat"

#if !defined(__has_include)
# define __has_include(x) 0
#endif
#if !defined(__has_attribute)
# define __has_attribute(x) 0
#endif
#if !defined(__has_feature)
# define __has_feature(x) 0
#endif
#if !defined(__has_warning)
# define __has_warning(x) 0
#endif

#if __has_include(<swift/objc-prologue.h>)
# include <swift/objc-prologue.h>
#endif

#pragma clang diagnostic ignored "-Wauto-import"
#if defined(__OBJC__)
#include <Foundation/Foundation.h>
#endif
#if defined(__cplusplus)
#include <cstdint>
#include <cstddef>
#include <cstdbool>
#include <cstring>
#include <stdlib.h>
#include <new>
#include <type_traits>
#else
#include <stdint.h>
#include <stddef.h>
#include <stdbool.h>
#include <string.h>
#endif
#if defined(__cplusplus)
#if __has_include(<ptrauth.h>)
# include <ptrauth.h>
#else
# ifndef __ptrauth_swift_value_witness_function_pointer
#  define __ptrauth_swift_value_witness_function_pointer(x)
# endif
#endif
#endif

#if !defined(SWIFT_TYPEDEFS)
# define SWIFT_TYPEDEFS 1
# if __has_include(<uchar.h>)
#  include <uchar.h>
# elif !defined(__cplusplus)
typedef uint_least16_t char16_t;
typedef uint_least32_t char32_t;
# endif
typedef float swift_float2  __attribute__((__ext_vector_type__(2)));
typedef float swift_float3  __attribute__((__ext_vector_type__(3)));
typedef float swift_float4  __attribute__((__ext_vector_type__(4)));
typedef double swift_double2  __attribute__((__ext_vector_type__(2)));
typedef double swift_double3  __attribute__((__ext_vector_type__(3)));
typedef double swift_double4  __attribute__((__ext_vector_type__(4)));
typedef int swift_int2  __attribute__((__ext_vector_type__(2)));
typedef int swift_int3  __attribute__((__ext_vector_type__(3)));
typedef int swift_int4  __attribute__((__ext_vector_type__(4)));
typedef unsigned int swift_uint2  __attribute__((__ext_vector_type__(2)));
typedef unsigned int swift_uint3  __attribute__((__ext_vector_type__(3)));
typedef unsigned int swift_uint4  __attribute__((__ext_vector_type__(4)));
#endif

#if !defined(SWIFT_PASTE)
# define SWIFT_PASTE_HELPER(x, y) x##y
# define SWIFT_PASTE(x, y) SWIFT_PASTE_HELPER(x, y)
#endif
#if !defined(SWIFT_METATYPE)
# define SWIFT_METATYPE(X) Class
#endif
#if !defined(SWIFT_CLASS_PROPERTY)
# if __has_feature(objc_class_property)
#  define SWIFT_CLASS_PROPERTY(...) __VA_ARGS__
# else
#  define SWIFT_CLASS_PROPERTY(...) 
# endif
#endif
#if !defined(SWIFT_RUNTIME_NAME)
# if __has_attribute(objc_runtime_name)
#  define SWIFT_RUNTIME_NAME(X) __attribute__((objc_runtime_name(X)))
# else
#  define SWIFT_RUNTIME_NAME(X) 
# endif
#endif
#if !defined(SWIFT_COMPILE_NAME)
# if __has_attribute(swift_name)
#  define SWIFT_COMPILE_NAME(X) __attribute__((swift_name(X)))
# else
#  define SWIFT_COMPILE_NAME(X) 
# endif
#endif
#if !defined(SWIFT_METHOD_FAMILY)
# if __has_attribute(objc_method_family)
#  define SWIFT_METHOD_FAMILY(X) __attribute__((objc_method_family(X)))
# else
#  define SWIFT_METHOD_FAMILY(X) 
# endif
#endif
#if !defined(SWIFT_NOESCAPE)
# if __has_attribute(noescape)
#  define SWIFT_NOESCAPE __attribute__((noescape))
# else
#  define SWIFT_NOESCAPE 
# endif
#endif
#if !defined(SWIFT_RELEASES_ARGUMENT)
# if __has_attribute(ns_consumed)
#  define SWIFT_RELEASES_ARGUMENT __attribute__((ns_consumed))
# else
#  define SWIFT_RELEASES_ARGUMENT 
# endif
#endif
#if !defined(SWIFT_WARN_UNUSED_RESULT)
# if __has_attribute(warn_unused_result)
#  define SWIFT_WARN_UNUSED_RESULT __attribute__((warn_unused_result))
# else
#  define SWIFT_WARN_UNUSED_RESULT 
# endif
#endif
#if !defined(SWIFT_NORETURN)
# if __has_attribute(noreturn)
#  define SWIFT_NORETURN __attribute__((noreturn))
# else
#  define SWIFT_NORETURN 
# endif
#endif
#if !defined(SWIFT_CLASS_EXTRA)
# define SWIFT_CLASS_EXTRA 
#endif
#if !defined(SWIFT_PROTOCOL_EXTRA)
# define SWIFT_PROTOCOL_EXTRA 
#endif
#if !defined(SWIFT_ENUM_EXTRA)
# define SWIFT_ENUM_EXTRA 
#endif
#if !defined(SWIFT_CLASS)
# if __has_attribute(objc_subclassing_restricted)
#  define SWIFT_CLASS(SWIFT_NAME) SWIFT_RUNTIME_NAME(SWIFT_NAME) __attribute__((objc_subclassing_restricted)) SWIFT_CLASS_EXTRA
#  define SWIFT_CLASS_NAMED(SWIFT_NAME) __attribute__((objc_subclassing_restricted)) SWIFT_COMPILE_NAME(SWIFT_NAME) SWIFT_CLASS_EXTRA
# else
#  define SWIFT_CLASS(SWIFT_NAME) SWIFT_RUNTIME_NAME(SWIFT_NAME) SWIFT_CLASS_EXTRA
#  define SWIFT_CLASS_NAMED(SWIFT_NAME) SWIFT_COMPILE_NAME(SWIFT_NAME) SWIFT_CLASS_EXTRA
# endif
#endif
#if !defined(SWIFT_RESILIENT_CLASS)
# if __has_attribute(objc_class_stub)
#  define SWIFT_RESILIENT_CLASS(SWIFT_NAME) SWIFT_CLASS(SWIFT_NAME) __attribute__((objc_class_stub))
#  define SWIFT_RESILIENT_CLASS_NAMED(SWIFT_NAME) __attribute__((objc_class_stub)) SWIFT_CLASS_NAMED(SWIFT_NAME)
# else
#  define SWIFT_RESILIENT_CLASS(SWIFT_NAME) SWIFT_CLASS(SWIFT_NAME)
#  define SWIFT_RESILIENT_CLASS_NAMED(SWIFT_NAME) SWIFT_CLASS_NAMED(SWIFT_NAME)
# endif
#endif
#if !defined(SWIFT_PROTOCOL)
# define SWIFT_PROTOCOL(SWIFT_NAME) SWIFT_RUNTIME_NAME(SWIFT_NAME) SWIFT_PROTOCOL_EXTRA
# define SWIFT_PROTOCOL_NAMED(SWIFT_NAME) SWIFT_COMPILE_NAME(SWIFT_NAME) SWIFT_PROTOCOL_EXTRA
#endif
#if !defined(SWIFT_EXTENSION)
# define SWIFT_EXTENSION(M) SWIFT_PASTE(M##_Swift_, __LINE__)
#endif
#if !defined(OBJC_DESIGNATED_INITIALIZER)
# if __has_attribute(objc_designated_initializer)
#  define OBJC_DESIGNATED_INITIALIZER __attribute__((objc_designated_initializer))
# else
#  define OBJC_DESIGNATED_INITIALIZER 
# endif
#endif
#if !defined(SWIFT_ENUM_ATTR)
# if __has_attribute(enum_extensibility)
#  define SWIFT_ENUM_ATTR(_extensibility) __attribute__((enum_extensibility(_extensibility)))
# else
#  define SWIFT_ENUM_ATTR(_extensibility) 
# endif
#endif
#if !defined(SWIFT_ENUM)
# define SWIFT_ENUM(_type, _name, _extensibility) enum _name : _type _name; enum SWIFT_ENUM_ATTR(_extensibility) SWIFT_ENUM_EXTRA _name : _type
# if __has_feature(generalized_swift_name)
#  define SWIFT_ENUM_NAMED(_type, _name, SWIFT_NAME, _extensibility) enum _name : _type _name SWIFT_COMPILE_NAME(SWIFT_NAME); enum SWIFT_COMPILE_NAME(SWIFT_NAME) SWIFT_ENUM_ATTR(_extensibility) SWIFT_ENUM_EXTRA _name : _type
# else
#  define SWIFT_ENUM_NAMED(_type, _name, SWIFT_NAME, _extensibility) SWIFT_ENUM(_type, _name, _extensibility)
# endif
#endif
#if !defined(SWIFT_UNAVAILABLE)
# define SWIFT_UNAVAILABLE __attribute__((unavailable))
#endif
#if !defined(SWIFT_UNAVAILABLE_MSG)
# define SWIFT_UNAVAILABLE_MSG(msg) __attribute__((unavailable(msg)))
#endif
#if !defined(SWIFT_AVAILABILITY)
# define SWIFT_AVAILABILITY(plat, ...) __attribute__((availability(plat, __VA_ARGS__)))
#endif
#if !defined(SWIFT_WEAK_IMPORT)
# define SWIFT_WEAK_IMPORT __attribute__((weak_import))
#endif
#if !defined(SWIFT_DEPRECATED)
# define SWIFT_DEPRECATED __attribute__((deprecated))
#endif
#if !defined(SWIFT_DEPRECATED_MSG)
# define SWIFT_DEPRECATED_MSG(...) __attribute__((deprecated(__VA_ARGS__)))
#endif
#if !defined(SWIFT_DEPRECATED_OBJC)
# if __has_feature(attribute_diagnose_if_objc)
#  define SWIFT_DEPRECATED_OBJC(Msg) __attribute__((diagnose_if(1, Msg, "warning")))
# else
#  define SWIFT_DEPRECATED_OBJC(Msg) SWIFT_DEPRECATED_MSG(Msg)
# endif
#endif
#if defined(__OBJC__)
#if !defined(IBSegueAction)
# define IBSegueAction 
#endif
#endif
#if !defined(SWIFT_EXTERN)
# if defined(__cplusplus)
#  define SWIFT_EXTERN extern "C"
# else
#  define SWIFT_EXTERN extern
# endif
#endif
#if !defined(SWIFT_CALL)
# define SWIFT_CALL __attribute__((swiftcall))
#endif
#if !defined(SWIFT_INDIRECT_RESULT)
# define SWIFT_INDIRECT_RESULT __attribute__((swift_indirect_result))
#endif
#if !defined(SWIFT_CONTEXT)
# define SWIFT_CONTEXT __attribute__((swift_context))
#endif
#if !defined(SWIFT_ERROR_RESULT)
# define SWIFT_ERROR_RESULT __attribute__((swift_error_result))
#endif
#if defined(__cplusplus)
# define SWIFT_NOEXCEPT noexcept
#else
# define SWIFT_NOEXCEPT 
#endif
#if defined(_WIN32)
#if !defined(SWIFT_IMPORT_STDLIB_SYMBOL)
# define SWIFT_IMPORT_STDLIB_SYMBOL __declspec(dllimport)
#endif
#else
#if !defined(SWIFT_IMPORT_STDLIB_SYMBOL)
# define SWIFT_IMPORT_STDLIB_SYMBOL 
#endif
#endif
#if defined(__OBJC__)
#if __has_feature(objc_modules)
#if __has_warning("-Watimport-in-framework-header")
#pragma clang diagnostic ignored "-Watimport-in-framework-header"
#endif
@import Foundation;
@import ObjectiveC;
#endif

#endif
#pragma clang diagnostic ignored "-Wproperty-attribute-mismatch"
#pragma clang diagnostic ignored "-Wduplicate-method-arg"
#if __has_warning("-Wpragma-clang-attribute")
# pragma clang diagnostic ignored "-Wpragma-clang-attribute"
#endif
#pragma clang diagnostic ignored "-Wunknown-pragmas"
#pragma clang diagnostic ignored "-Wnullability"
#pragma clang diagnostic ignored "-Wdollar-in-identifier-extension"

#if __has_attribute(external_source_symbol)
# pragma push_macro("any")
# undef any
# pragma clang attribute push(__attribute__((external_source_symbol(language="Swift", defined_in="AliCloudIotKitAdvance",generated_declaration))), apply_to=any(function,enum,objc_interface,objc_category,objc_protocol))
# pragma pop_macro("any")
#endif

#if defined(__OBJC__)
@class NSData;
@class NSString;
enum BleStatus : NSInteger;

SWIFT_PROTOCOL("_TtP21AliCloudIotKitAdvance22AliCloudIotBleDelegate_")
@protocol AliCloudIotBleDelegate
/// 从广播数据中解析出Mac地址，用于匹配连接指定mac地址的设备
/// \param manufacturerData 对应CoreBluetooth的AdvertisementData 的manufacturerData
///
///
/// returns:
/// 返回16进制，全大写，以”:“分隔的字符串，如”EF:20:11:10:AD:BD”
- (NSString * _Nullable)parseMacAddressFromManufacturerDataDataWithManufacturerData:(NSData * _Nullable)manufacturerData SWIFT_WARN_UNUSED_RESULT;
/// Ble 连接状态变化回调
/// \param state Ble 连接状态
///
/// \param mac 对应现在的设备mac地址
///
- (void)onBleConnectionStateChangedWithState:(enum BleStatus)state mac:(NSString * _Nonnull)mac;
/// 接受到Ble数据的回调
/// \param uuid 对应uuid
///
/// \param data 数据内容
///
- (void)onBleDataReceivedWithUuid:(NSString * _Nonnull)uuid data:(NSData * _Nonnull)data;
@end


SWIFT_PROTOCOL("_TtP21AliCloudIotKitAdvance19AliCloudIotDelegate_")
@protocol AliCloudIotDelegate
/// iot断开
- (void)onIotDisconnected;
/// iot已连接
- (void)onIotConnected;
/// iot连接失败
- (void)onIotConnecting;
/// 消息已到达，下行数据
/// \param topic 订阅主题
///
/// \param message 消息体
///
- (void)onMessageArrivedWithTopic:(NSString * _Nonnull)topic message:(id _Nullable)message;
/// iot消息已到达
/// \param data 业务数据
///
/// \param message 错误消息内容
///
/// \param code 错误码
///
- (void)onIotMessageArrivedWithData:(NSString * _Nonnull)data message:(NSString * _Nonnull)message code:(int32_t)code;
/// 乘车码消息已到达
/// \param data 业务数据
///
/// \param message 错误消息内容
///
/// \param code 错误码
///
- (void)onBusCodeMessageArrivedWithData:(NSString * _Nonnull)data message:(NSString * _Nonnull)message code:(int32_t)code;
@end

@class ConfigWrapper;

SWIFT_PROTOCOL("_TtP21AliCloudIotKitAdvance29AliCloudIotSdkConfigBaseClass_")
@protocol AliCloudIotSdkConfigBaseClass
- (ConfigWrapper * _Nonnull)getConfig SWIFT_WARN_UNUSED_RESULT;
@end


SWIFT_CLASS("_TtC21AliCloudIotKitAdvance28AliCloudIotDirectConfigClass")
@interface AliCloudIotDirectConfigClass : NSObject <AliCloudIotSdkConfigBaseClass>
- (nonnull instancetype)initWithProductKey:(NSString * _Nonnull)productKey deviceName:(NSString * _Nonnull)deviceName server:(NSString * _Nonnull)server deviceSecret:(NSString * _Nonnull)deviceSecret OBJC_DESIGNATED_INITIALIZER;
- (nonnull instancetype)initWithProductKey:(NSString * _Nonnull)productKey deviceName:(NSString * _Nonnull)deviceName server:(NSString * _Nonnull)server port:(int32_t)port deviceSecret:(NSString * _Nonnull)deviceSecret OBJC_DESIGNATED_INITIALIZER;
- (ConfigWrapper * _Nonnull)getConfig SWIFT_WARN_UNUSED_RESULT;
- (nonnull instancetype)init SWIFT_UNAVAILABLE;
+ (nonnull instancetype)new SWIFT_UNAVAILABLE_MSG("-init is unavailable");
@end


SWIFT_CLASS("_TtC21AliCloudIotKitAdvance21AliCloudIotLogManager")
@interface AliCloudIotLogManager : NSObject
SWIFT_CLASS_PROPERTY(@property (nonatomic, class, readonly, strong) AliCloudIotLogManager * _Nonnull shared;)
+ (AliCloudIotLogManager * _Nonnull)shared SWIFT_WARN_UNUSED_RESULT;
/// 切换日志显示
/// \param isOpen 是否开启日志，为TRUE时，打印日志
///
- (void)switchLogStatus:(BOOL)isOpen;
- (nonnull instancetype)init OBJC_DESIGNATED_INITIALIZER;
@end


SWIFT_CLASS("_TtC21AliCloudIotKitAdvance28AliCloudIotQulianConfigClass")
@interface AliCloudIotQulianConfigClass : NSObject <AliCloudIotSdkConfigBaseClass>
- (nonnull instancetype)initWithProductKey:(NSString * _Nonnull)productKey deviceName:(NSString * _Nonnull)deviceName server:(NSString * _Nonnull)server timestamp:(NSString * _Nonnull)timestamp password:(NSString * _Nonnull)password OBJC_DESIGNATED_INITIALIZER;
- (nonnull instancetype)initWithProductKey:(NSString * _Nonnull)productKey deviceName:(NSString * _Nonnull)deviceName server:(NSString * _Nonnull)server port:(int32_t)port timestamp:(NSString * _Nonnull)timestamp password:(NSString * _Nonnull)password OBJC_DESIGNATED_INITIALIZER;
- (ConfigWrapper * _Nonnull)getConfig SWIFT_WARN_UNUSED_RESULT;
- (nonnull instancetype)init SWIFT_UNAVAILABLE;
+ (nonnull instancetype)new SWIFT_UNAVAILABLE_MSG("-init is unavailable");
@end



SWIFT_PROTOCOL("_TtP21AliCloudIotKitAdvance26AliCloudIotSdkInitDelegate_")
@protocol AliCloudIotSdkInitDelegate
/// SDK初始化结果，注意：成功不代表MQTT长连接通道建立成功
/// \param successed 是否初始化成功
///
/// \param error 初始化失败时的错误信息
///
- (void)onInitWithSuccessed:(BOOL)successed error:(NSError * _Nullable)error;
/// SDK注销初始化结果
/// \param successed 是否注销初始化成功
///
/// \param error 失败时的错误信息
///
- (void)onDeinitWithSuccessed:(BOOL)successed error:(NSError * _Nullable)error;
@end


SWIFT_CLASS("_TtC21AliCloudIotKitAdvance23AliCloudIotWatchManager")
@interface AliCloudIotWatchManager : NSObject
SWIFT_CLASS_PROPERTY(@property (nonatomic, class, readonly, strong) AliCloudIotWatchManager * _Nonnull shared;)
+ (AliCloudIotWatchManager * _Nonnull)shared SWIFT_WARN_UNUSED_RESULT;
/// SDK初始化和注销初始化是否在子线程运行
/// 回调在主线程
@property (nonatomic) BOOL sdkInitOrDeInitInAsync;
/// 初始化SDK
/// \param config 配置信息
///
/// \param sdkDelegate sdk初始化回调
///
/// \param iotDelegate iot相关回调
///
- (BOOL)initSdkWithConfig:(id <AliCloudIotSdkConfigBaseClass> _Nonnull)config sdkDelegate:(id <AliCloudIotSdkInitDelegate> _Nonnull)sdkDelegate iotDelegate:(id <AliCloudIotDelegate> _Nonnull)iotDelegate error:(NSError * _Nullable * _Nullable)error SWIFT_METHOD_FAMILY(none);
/// 注销初始化，断开设备与物联网平台的MQTT长连接。
- (void)deInitSdk;
- (nonnull instancetype)init OBJC_DESIGNATED_INITIALIZER;
@end


@interface AliCloudIotWatchManager (SWIFT_EXTENSION(AliCloudIotKitAdvance))
- (BOOL)publishMessageWithTopic:(NSString * _Nonnull)topic data:(NSData * _Nonnull)data error:(NSError * _Nullable * _Nullable)error;
- (BOOL)publishMessageWithTopic:(NSString * _Nonnull)topic data:(NSData * _Nonnull)data qos:(int32_t)qos error:(NSError * _Nullable * _Nullable)error resultBlock:(void (^ _Nonnull)(BOOL, NSError * _Nullable))resultBlock;
/// 订阅主题
/// \param topic 主题
///
- (BOOL)subscribeTopicWithTopic:(NSString * _Nonnull)topic error:(NSError * _Nullable * _Nullable)error;
/// 取消订阅主题
/// \param topic 主题
///
- (BOOL)unsubscribeTopicWithTopic:(NSString * _Nonnull)topic error:(NSError * _Nullable * _Nullable)error;
@end

@class RideYardService;
@class BleService;

@interface AliCloudIotWatchManager (SWIFT_EXTENSION(AliCloudIotKitAdvance))
/// 乘车码服务
@property (nonatomic, readonly, strong) RideYardService * _Nonnull rideYardService;
/// ble服务
@property (nonatomic, readonly, strong) BleService * _Nonnull bleService;
@end


@protocol BleNeedSendDataDelegate;

SWIFT_CLASS("_TtC21AliCloudIotKitAdvance18AliConnectMananger")
@interface AliConnectMananger : NSObject
SWIFT_CLASS_PROPERTY(@property (nonatomic, class, readonly, strong) AliConnectMananger * _Nonnull shared;)
+ (AliConnectMananger * _Nonnull)shared SWIFT_WARN_UNUSED_RESULT;
@property (nonatomic, strong) id <BleNeedSendDataDelegate> _Nullable bleSendDataDelegate;
/// 收到来自蓝牙的消息
/// \param data 二进制数据
///
- (void)bleDataReceivedWithData:(NSData * _Nonnull)data;
/// 发起飞鸽书请求
- (void)checkFgsStateWithResult:(void (^ _Nonnull)(BOOL, NSDictionary<NSString *, id> * _Nonnull))result;
/// 连接Lp
- (void)startConnectLpStateWithResult:(void (^ _Nonnull)(BOOL, NSDictionary<NSString *, id> * _Nonnull))result;
/// 主动断开Lp
- (void)disConnect;
- (nonnull instancetype)init OBJC_DESIGNATED_INITIALIZER;
@end




SWIFT_CLASS("_TtC21AliCloudIotKitAdvance18BleBaseSendService")
@interface BleBaseSendService : NSObject
- (nonnull instancetype)init OBJC_DESIGNATED_INITIALIZER;
@end


SWIFT_PROTOCOL("_TtP21AliCloudIotKitAdvance23BleNeedSendDataDelegate_")
@protocol BleNeedSendDataDelegate
- (void)sendBleDataWithData:(NSData * _Nonnull)data;
@end


SWIFT_CLASS("_TtC21AliCloudIotKitAdvance10BleService")
@interface BleService : NSObject
/// 扫描连接指定设备
/// \param mac mac地址
///
/// \param scanTimeout 扫描超时时间
///
/// \param delegate ble 相关回调
///
- (void)connectBleWithMac:(NSString * _Nonnull)mac scanTimeout:(NSInteger)scanTimeout delegate:(id <AliCloudIotBleDelegate> _Nonnull)delegate;
/// 断开连接设备
- (void)disconnect;
/// 发送数据到BLE
/// \param data 数据
///
- (void)sendBleDataWithData:(NSData * _Nonnull)data;
- (nonnull instancetype)init OBJC_DESIGNATED_INITIALIZER;
@end

typedef SWIFT_ENUM(NSInteger, BleStatus, closed) {
  BleStatusConnecting = 0,
  BleStatusConnected = 1,
  BleStatusDisconnected = 2,
};


SWIFT_CLASS("_TtC21AliCloudIotKitAdvance13ConfigWrapper")
@interface ConfigWrapper : NSObject
- (nonnull instancetype)init SWIFT_UNAVAILABLE;
+ (nonnull instancetype)new SWIFT_UNAVAILABLE_MSG("-init is unavailable");
@end


SWIFT_CLASS("_TtC21AliCloudIotKitAdvance10IotService")
@interface IotService : NSObject
/// 上报iot数据
/// \param eventContent 时间内容
///
/// \param trackId id
///
- (BOOL)uploadDataWithEventContent:(NSString * _Nonnull)eventContent trackId:(NSString * _Nullable)trackId error:(NSError * _Nullable * _Nullable)error;
- (nonnull instancetype)init OBJC_DESIGNATED_INITIALIZER;
@end

/// mq状态
typedef SWIFT_ENUM(uint8_t, MqttState, closed) {
/// 上线成功
  MqttStateOnlineSuccess = 0x00,
/// 鉴权失败
  MqttStateAuthenticationFailure = 0x01,
/// 鉴权超时
  MqttStateAuthenticationTimeout = 0x02,
/// 下线成功
  MqttStateOfflineSuccess = 0x03,
};



SWIFT_CLASS("_TtC21AliCloudIotKitAdvance15RideYardService")
@interface RideYardService : BleBaseSendService
/// 上报乘车码数据
/// \param eventContent 时间内容
///
/// \param trackId id
///
- (BOOL)uploadDataWithEventContent:(NSString * _Nonnull)eventContent trackId:(NSString * _Nullable)trackId error:(NSError * _Nullable * _Nullable)error;
- (nonnull instancetype)init OBJC_DESIGNATED_INITIALIZER;
@end


#endif
#if defined(__cplusplus)
#endif
#if __has_attribute(external_source_symbol)
# pragma clang attribute pop
#endif
#pragma clang diagnostic pop
#endif

#else
#error unsupported Swift architecture
#endif
