//
// Created by Best Mafen on 2019/9/30.
// Copyright (c) 2019 szabh. All rights reserved.
//

import Foundation

class BleSedentarinessSettings: BleWritable {
    static let ITEM_LENGTH = 6

    override var mLengthToWrite: Int {
        BleSedentarinessSettings.ITEM_LENGTH
    }
    //////初始化值 10:00～21:00
    var mEnabled: Int = 0 //0->off 1->open
    var mRepeat: Int = 127 //Monday - Sunday (Arbitrary choice)
    var mStartHour: Int = 8
    var mStartMinute: Int = 0
    var mEndHour: Int = 18
    var mEndMinute: Int = 0
    var mInterval: Int = 120 // 分钟数

    required init(_ data: Data? = nil, _ byteOrder: ByteOrder = .BIG_ENDIAN) {
        super.init(data, byteOrder)
    }

    override func encode() {
        super.encode()
        writeIntN(mEnabled, 1)
        writeIntN(mRepeat, 7)
        writeInt8(mStartHour)
        writeInt8(mStartMinute)
        writeInt8(mEndHour)
        writeInt8(mEndMinute)
        writeInt8(mInterval)
    }

    override func decode() {
        super.decode()
        mEnabled = Int(readUIntN(1))
        mRepeat = Int(readUIntN(7))
        mStartHour = Int(readUInt8())
        mStartMinute = Int(readUInt8())
        mEndHour = Int(readUInt8())
        mEndMinute = Int(readUInt8())
        mInterval = Int(readUInt8())
    }

    required init(from decoder: Decoder) throws {
        try super.init(from: decoder)
        let container = try decoder.container(keyedBy: CodingKeys.self)
        mEnabled = try container.decode(Int.self, forKey: .mEnabled)
        mRepeat = try container.decode(Int.self, forKey: .mRepeat)
        mStartHour = try container.decode(Int.self, forKey: .mStartHour)
        mStartMinute = try container.decode(Int.self, forKey: .mStartMinute)
        mEndHour = try container.decode(Int.self, forKey: .mEndHour)
        mEndMinute = try container.decode(Int.self, forKey: .mEndMinute)
        mInterval = try container.decode(Int.self, forKey: .mInterval)
    }

    override func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(mEnabled, forKey: .mEnabled)
        try container.encode(mRepeat, forKey: .mRepeat)
        try container.encode(mStartHour, forKey: .mStartHour)
        try container.encode(mStartMinute, forKey: .mStartMinute)
        try container.encode(mEndHour, forKey: .mEndHour)
        try container.encode(mEndMinute, forKey: .mEndMinute)
        try container.encode(mInterval, forKey: .mInterval)
    }

    private enum CodingKeys: String, CodingKey {
        case mEnabled, mRepeat, mStartHour, mStartMinute, mEndHour, mEndMinute, mInterval
    }

    override var description: String {
        "BleSedentarinessSettings(mEnabled: \(mEnabled), mRepeat: \(mRepeat), mStartHour: \(mStartHour)"
            + ", mStartMinute: \(mStartMinute), mEndHour: \(mEndHour), mEndMinute: \(mEndMinute)"
            + ", mInterval: \(mInterval))"
    }
    
    func toDictionary()->[String:Any]{
        let dic : [String : Any] = ["mEnabled":mEnabled,
                                    "mRepeat":mRepeat,
                                    "mStartHour":mStartHour,
                                    "mStartMinute":mStartMinute,
                                    "mEndHour":mEndHour,
                                    "mEndMinute":mEndMinute,
                                    "mInterval":mInterval]
        return dic
    }
    
    func dictionaryToObjct(_ dic:[String:Any]) ->BleSedentarinessSettings{

        let newModel = BleSedentarinessSettings()
        if dic.keys.count<1{
            return newModel
        }
        newModel.mEnabled = dic["mEnabled"] as? Int ?? 0
        newModel.mRepeat = dic["mRepeat"] as? Int ?? 0
        newModel.mStartHour = dic["mStartHour"] as? Int ?? 0
        newModel.mStartMinute = dic["mStartMinute"] as? Int ?? 0
        newModel.mEndHour = dic["mEndHour"] as? Int ?? 0
        newModel.mEndMinute = dic["mEndMinute"] as? Int ?? 0
        newModel.mInterval = dic["mInterval"] as? Int ?? 0
        return newModel
    }
}
