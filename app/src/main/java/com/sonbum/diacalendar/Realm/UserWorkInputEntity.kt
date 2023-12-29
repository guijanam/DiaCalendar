package com.sonbum.diacalendar.Realm

class UserWorkInputEntity {
}


//enum UserWorkInputType: String, PersistableEnum, CaseIterable {
//    case huemooChoongDang = "휴무충당"
//    case daegiChoongDang = "대기충당"
//    case jijeongGuenMoo = "지정근무"
//    case bibeon = "비번~"
//    case jiGuenwill = "지근예정"
//    case huegawill = "휴가예정"
//    case bosang = "보상"
//    case chokyoun = "촉연"
//    case youncha = "연차"
//    case jihue = "지휴"
//    case sangri = "생리"
//    case jangjae = "장재"
//    case dolbom = "돌봄"
//    case cheongwon = "청원"
//    case beongga = "병가"
//    case heihang = "회행"
//    case gongga = "공가"
//    case kyoyuk = "교육"
//    case gitar = "기타"
//
//    var themeColor: Color {
//        switch self {
//            case .huemooChoongDang:
//            return Color.yellow
//            case .daegiChoongDang:
//            return Color.green
//            case .jiGuenwill, .jijeongGuenMoo:
//            return Color.purple
//            case .bibeon:
//            return Color.gray
//            case .huegawill,.bosang,.cheongwon,
//            .chokyoun,.dolbom,.gitar,
//            .gongga,.heihang,.jangjae,.youncha,
//            .sangri,.jihue,.kyoyuk,.beongga:
//            return Color.red
//
//        }
//    }
//
//    var inputName: String {
//        switch self {
//            case .huemooChoongDang: return "휴무충당"
//            case .daegiChoongDang: return "대기충당"
//
//            case .jijeongGuenMoo: return "지정근무"
//            case .bibeon : return "~"
//            case .jiGuenwill : return "지근"
//            case .huegawill : return "예정"
//            case .bosang : return "보상"
//            case .chokyoun : return "촉연"
//            case .youncha : return "연차"
//            case .jihue : return "지휴"
//            case .sangri : return "생리"
//            case .jangjae : return "장재"
//            case .dolbom : return "돌봄"
//            case .cheongwon : return "청원"
//            case .beongga : return "병가"
//            case .heihang : return "회행"
//            case .gongga : return "공가"
//            case .kyoyuk : return "교육"
//            case .gitar : return "기타"
//        }
//    }
//
//}
//
/////work input
//class UserWorkInputEntity: Object, ObjectKeyIdentifiable {
//    @Persisted(primaryKey: true) var id: ObjectId
//    @Persisted var turn: String = "1"//다이아
//    @Persisted var _calendarData: String // 달력날짜
//    @Persisted var type: UserWorkInputType?
//    var calendarData: Date {
//        return Date.makeDate(from: _calendarData)
//    }
//    convenience init(date: String, turn: String, type: UserWorkInputType) {
//        self.init()
//        self._calendarData = date
//        self.turn = turn
//        self.type = type
//    }
//}
