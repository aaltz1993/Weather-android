package a.alt.z.weather.data.api.model.weather

enum class DailyTemperatureRegionId(val code: String) {
    /* 서울 */
    SEOUL("11B10101"),
    /* 인천 */
    INCHEON("11B20201"), BAENGNYEONGDO("11A00101"), GANGHWA("11B20101"),
    /* 경기 */
    SUWON("11B20601"), SUNGNAM("11B20605"), ANYANG("11B20602"), GWANGMYEONG("11B10103"),
    GWACHEON("11B10102"), PYEONGTAEK("11B20606"), OSAN("11B20603"), UIWANG("11B20609"), YONGIN("11B20612"),
    GUNPO("11B20610"), ANSUNG("11B20611"), HWASUNG("11B20604"), YANGPEONG("11B20503"), GURI("11B20501"),
    NAMYANGJU("11B20502"), HANAM("11B20504"), ICHEON("11B20701"), YEOJU("11B20703"), GYEONGGI_GWANGJU("11B20702"),
    UIJEONGBU("11B20301"), GOYANG("11B20302"), PAJU("11B20305"), YANGJU("11B20304"), DONGDUCHEON("11B20401"),
    YEONCHEON("11B20402"), POCHEON("11B20403"), GAPYEONG("11B20404"),  GIMPO("11B20102"),
    SIHEUNG("11B20202"), BUCHEON("11B20204"), ANSAN("11B20203"),
    /* 강원 */
    CHEOLWON("11D10101"), HWACHEON("11D10102"), INJE("11D10201"), YANGGU("11D1202"), CHUNCHEON("11D10301"),
    HONGCHEON("11D10302"), WONJU("11D10401"), HWENGSUNG("11D10402"), YOUNGWOL("11D10501"),
    JUNGSUN("11D10502"), PYEONGCHANG("11D10503"), DAEGWANRYEONG("11D20201"), SOKCHO("11D20401"),
    GANGWON_GOSUNG("11D20402"), YANGYANG("11D20403"), GANGREUNG("11D20501"),
    DONGHAE("11D20601"), SAMCHEOK("11D20602"), TAEBAEK("11D20301"),
    /* 대전 */
    DAEJEON("11C20401"),
    /* 세종 */
    SEJONG("11C20404"),
    /* 충청북도 */
    CHEONGJU("11C10301"), JEUNGPYEONG("11C10304"), GUESAN("11C10303"), JINCHEON("11C10102"),
    CHUNGJU("11C10101"), EUMSUNG("11C10103"), JECHEON("11C10201"), DANYANG("11C10202"), BOEUN("11C10302"),
    OKCHEON("11C10403"), YEONGDONG("11C10402"), CHUPUNGRYEONG("11C10401"),
    /* 충청남도 */
    GONGJU("11C20402"), NONSAN("11C20602"), GYERYONG("11C20403"), GEUMSAN("11C20601"), CHEONAN("11C20301"),
    ASAN("11C20302"), YESAN("11C20303"), SEOSAN("11C20101"), TAEAN("11C20102"), DANGJIN("11C20103"),
    HONGSUNG("11C20104"), BORYEONG("11C20201"), SEOCHEON("11C20202"), CHUNGYANG("11C20502"), BUYEO("11C20501"),
    /* 경상북도 */
    YOUNGCHUN("11H10702"), GYEONGSAN("11H10703"), CHEONGDO("11H10704"), CHILGOK("11H10705"), GIMCHEON("11H10601"),
    GUMI("11H10602"), GUNWI("11H10603"), GORYEONG("11H10604"), SUNGJU("11H10605"), ANDONG("11H10501"),
    EUISUNG("11H10502"), CHUNGSONG("11H10503"), SANGJU("11H10302"), MUNGYEONG("11H10301"), YECHEON("11H10303"),
    YOUNGJU("11H10401"), BONGHWA("11H10402"), YOUNGYANG("11H10403"), ULJIN("11H10101"), YOUNGDEOK("11H10102"),
    POHANG("11H10201"), GYEONGJU("11H10202"), ULREUNGDO("11E00101"), DOKDO("11E00102"),
    /* 경상남도 */
    GIMHAE("11H20304"), YANGSAN("11H20102"), CHANGWON("11H20301"), MILYANG("11H20601"), HAMAN("11H20603"),
    CHANGNYEONG("11H20604"), UIRYEONG("11H20602"), JINJU("11H20701"), HADONG("11H20704"), SACHEON("11H20402"),
    GEOCHANG("11H20502"), HAPCHEON("11H20503"), SANCHUNG("11H20703"), HAMYANG("11H20501"), TONGYEONG("11H20401"),
    GEOJE("11H20403"), GOSUNG("11H20404"), NAMHAE("11H20405"),
    /* 대구 */
    DAEGU("11H10701"),
    /* 울산 */
    ULSAN("11H20101"),
    /* 부산 */
    BUSAN("11H20201"),
    /* 광주 */
    GWANGJU("11F20501"),
    /* 전라북도 */
    JEONJU("11F10201"), IKSAN("11F10202"), GUNSAN("21F10501"), JEONGEUP("11F10203"), GIMJE("21F10502"),
    NAMWON("11F10401"), GOCHANG("21F10601"), MUJU("11F10302"), BUAN("21F10602"), SUNCHANG("11F10403"),
    WANJU("11F10204"), IMSIL("11F10402"), JANGSU("11F10301"), JINAN("11F10303"),
    /* 전라남도 */
    NAJU("11F20503"), JANGSUNG("11F20502"), DAMYANG("11F20504"), HWASUN("11F20505"), YOUNGGWANG("21F20102"),
    HAMPYEONG("21F20101"), MOKPO("21F20801"), MUAN("21F20804"), YOUNGAM("21F20802"), JINDO("21F20201"),
    SINAN("21F20803"), HEUKSANDO("11F20701"), SUNCHEON("11F20603"), GWANGYANG("11F20402"), GURYE("11F20601"),
    GOKSUNG("11F20602"), WANDO("11F20301"), GANGJIN("11F20303"), JANGHEUNG("11F20304"), HAENAM("11F20302"),
    YEOSU("11F20401"), GOHEUNG("11F20403"), BOSUNG("11F20404"),
    /* 제주 */
    JEJU("11G00201"), SUGWIPO("11G00401");

    companion object {
        fun addressOf(regionDepth1Name: String, regionDepth2Name: String, regionDepth3Name: String): DailyTemperatureRegionId {
            return when (regionDepth1Name) {
                "서울" -> SEOUL
                "부산" -> BUSAN
                "대구" -> DAEGU
                "인천" -> {
                    when {
                        regionDepth3Name == "백령면" -> {
                            BAENGNYEONGDO
                        }
                        regionDepth2Name == "강화군" -> {
                            GANGHWA
                        }
                        else -> {
                            INCHEON
                        }
                    }
                }
                "광주" -> GWANGJU
                "대전" -> DAEJEON
                "울산" -> ULSAN
                "세종특별자치시" -> SEJONG
                "경기" -> {
                    when (regionDepth2Name) {
                        "수원시" -> SUWON
                        "성남시" -> SUNGNAM
                        "안양시" -> ANYANG
                        "광명시" -> GWANGMYEONG
                        "과천시" -> GWACHEON
                        "평택시" -> PYEONGTAEK
                        "오산시" -> OSAN
                        "의왕시" -> UIWANG
                        "용인시" -> YONGIN
                        "군포시" -> GUNPO
                        "안성시" -> ANSUNG
                        "화성시" -> HWASUNG
                        "양평군" -> YANGPEONG
                        "구리시" -> GURI
                        "남양주시" -> NAMYANGJU
                        "하남시" -> HANAM
                        "이천시" -> ICHEON
                        "여주시" -> YEOJU
                        "광주시" -> GYEONGGI_GWANGJU
                        "의정부시" -> UIJEONGBU
                        "고양시" -> GOYANG
                        "파주시" -> PAJU
                        "양주시" -> YANGJU
                        "동두천시" -> DONGDUCHEON
                        "연천군" -> YEONCHEON
                        "포천시" -> POCHEON
                        "가평시" -> GAPYEONG
                        "김포시" -> GIMPO
                        "시흥시" -> SIHEUNG
                        "부천시" -> BUCHEON
                        "안산시" -> ANSAN
                        else -> SUWON
                    }
                }
                "강원" -> {
                    when (regionDepth2Name) {
                        "원주시" -> WONJU
                        "춘천시" -> CHUNCHEON
                        "강릉시" -> GANGREUNG
                        "동해시" -> DONGHAE
                        "속초시" -> SOKCHO
                        "삼척시" -> SAMCHEOK
                        "태백시" -> TAEBAEK
                        "홍천군" -> HONGCHEON
                        "철원군" -> CHEOLWON
                        "횡성군" -> HWENGSUNG
                        "평창군" -> {
                            if (regionDepth3Name == "대관령면") {
                                DAEGWANRYEONG
                            } else {
                                PYEONGCHANG
                            }
                        }
                        "정선군" -> JUNGSUN
                        "영월군" -> YOUNGWOL
                        "인제군" -> INJE
                        "고성군" -> GANGWON_GOSUNG
                        "양양군" -> YANGYANG
                        "화천군" -> HWACHEON
                        "양구군" -> YANGGU
                        else -> CHUNCHEON
                    }
                }
                "충북" -> {
                    when (regionDepth2Name) {
                        "청주시" -> CHEONGJU
                        "충주시" -> CHUNGJU
                        "제천시" -> JECHEON
                        "보은군" -> BOEUN
                        "옥천군" -> OKCHEON
                        "영동군" -> {
                            if (regionDepth3Name == "추풍령면") {
                                CHUPUNGRYEONG
                            } else {
                                YEONGDONG
                            }
                        }
                        "증평군" -> JEUNGPYEONG
                        "진천군" -> JINCHEON
                        "괴산군" -> GUESAN
                        "음성군" -> EUMSUNG
                        "단양군" -> DANYANG
                        else -> CHEONGJU
                    }
                }
                "충남" -> {
                    when (regionDepth2Name) {
                        "천안시" -> CHEONAN
                        "공주시" -> GONGJU
                        "보령시" -> BORYEONG
                        "아산시" -> ASAN
                        "서산시" -> SEOSAN
                        "논산시" -> NONSAN
                        "계룡시" -> GYERYONG
                        "당진시" -> DANGJIN
                        "금산군" -> GEUMSAN
                        "부여군" -> BUYEO
                        "서천군" -> SEOCHEON
                        "청양군" -> CHUNGYANG
                        "홍성군" -> HONGSUNG
                        "예산군" -> YESAN
                        "태안군" -> TAEAN
                        else -> YESAN
                    }
                }
                "전북" -> {
                    when (regionDepth2Name) {
                        "전주시" -> JEONJU
                        "익산시" -> IKSAN
                        "군산시" -> GUNSAN
                        "정읍시" -> JEONGEUP
                        "김제시" -> GIMJE
                        "남원시" -> NAMWON
                        "완주군" -> WANJU
                        "고창군" -> GOCHANG
                        "부안군" -> BUAN
                        "임실군" -> IMSIL
                        "순창군" -> SUNCHANG
                        "진안군" -> JINAN
                        "무주군" -> MUJU
                        "장수군" -> JANGSU
                        else -> JEONJU
                    }
                }
                "전남" -> {
                    when (regionDepth2Name) {
                        "목포시" -> MOKPO
                        "여수시" -> YEOSU
                        "순천시" -> SUNCHEON
                        "나주시" -> NAJU
                        "광양시" -> GWANGYANG
                        "담양군" -> DAMYANG
                        "곡성군" -> GOKSUNG
                        "구례군" -> GURYE
                        "고흥군" -> GOHEUNG
                        "보성군" -> BOSUNG
                        "화순군" -> HWASUN
                        "장흥군" -> JANGHEUNG
                        "강진군" -> GANGJIN
                        "해남군" -> HAENAM
                        "영암군" -> YOUNGAM
                        "무안군" -> MUAN
                        "함평군" -> HAMPYEONG
                        "영광군" -> YOUNGGWANG
                        "장성군" -> JANGSUNG
                        "완도군" -> WANDO
                        "진도군" -> JINDO
                        "신안군" -> {
                            if (regionDepth3Name == "흑산면") {
                                HEUKSANDO
                            } else {
                                SINAN
                            }
                        }
                        else -> MUAN
                    }
                }
                "경북" -> {
                    when (regionDepth2Name) {
                        "포항시" -> POHANG
                        "경주시" -> GYEONGJU
                        "김천시" -> GIMCHEON
                        "안동시" -> ANDONG
                        "구미시" -> GUMI
                        "영주시" -> YOUNGJU
                        "영천시" -> YOUNGCHUN
                        "상주시" -> SANGJU
                        "문경시" -> MUNGYEONG
                        "경산시" -> GYEONGSAN
                        "군위군" -> GUNWI
                        "의성군" -> EUISUNG
                        "청송군" -> CHUNGSONG
                        "영양군" -> YOUNGYANG
                        "영덕군" -> YOUNGDEOK
                        "청도군" -> CHEONGDO
                        "고령군" -> GORYEONG
                        "성주군" -> SUNGJU
                        "칠곡군" -> CHILGOK
                        "예천군" -> YECHEON
                        "봉화군" -> BONGHWA
                        "울진군" -> ULJIN
                        "울릉군" -> {
                            if (regionDepth3Name == "울릉읍 독도리") {
                                DOKDO
                            } else {
                                ULREUNGDO
                            }
                        }
                        else -> ANDONG
                    }
                }
                "경남" -> {
                    when (regionDepth2Name) {
                        "창원시" -> CHANGWON
                        "김해시" -> GIMHAE
                        "진주시" -> JINJU
                        "양산시" -> YANGSAN
                        "거제시" -> GEOJE
                        "통영시" -> TONGYEONG
                        "사천시" -> SACHEON
                        "밀양시" -> MILYANG
                        "함안군" -> HAMAN
                        "거창군" -> GEOCHANG
                        "창녕군" -> CHANGNYEONG
                        "고성군" -> GOSUNG
                        "하동군" -> HADONG
                        "합천군" -> HAPCHEON
                        "남해군" -> NAMHAE
                        "함양군" -> HAMYANG
                        "산청군" -> SANCHUNG
                        "의령군" -> UIRYEONG
                        else -> CHANGWON
                    }
                }
                "제주특별자치도" -> JEJU
                else -> throw IllegalArgumentException()
            }
        }
    }
}