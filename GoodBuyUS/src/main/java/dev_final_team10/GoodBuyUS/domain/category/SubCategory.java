package dev_final_team10.GoodBuyUS.domain.category;

public enum SubCategory {
    // 식료품
    FRESH_FOOD("신선식품"),
    PROCESSED_FOOD("가공식품"),
    HEALTH_FOOD("건강식품"),

    // 생활용품
    KITCHEN_PRODUCTS("주방용품"),
    BATHROOM_PRODUCTS("욕실용품"),
    CLEANING_PRODUCTS("청소용품"),

    // 패션/의류
    MEN_CLOTHING("남성의류"),
    WOMEN_CLOTHING("여성의류"),
    ACCESSORIES("액세서리"),
    CHILDREN_CLOTHING("유아동복"),

    // 전자제품
    HOME_APPLIANCES("가전제품"),
    IT_DEVICES("IT 기기"),
    SMALL_APPLIANCES("소형가전"),
    PERIPHERALS("주변기기"),

    // 가구/인테리어
    FURNITURE("가구"),
    INTERIOR_DECOR("인테리어 소품"),
    STORAGE("수납용품"),

    // 유아/아동용품
    TOYS("완구/장난감"),
    EDUCATIONAL_TOOLS("교육용품"),
    BABY_PRODUCTS("유아용품"),

    // 스포츠/레저
    SPORTS_EQUIPMENT("스포츠 장비"),
    CAMPING("캠핑용품"),
    LEISURE_EQUIPMENT("레저용품"),

    // 반려동물 용품
    PET_FOOD("사료/간식"),
    PET_LIVING("생활용품"),
    PET_HEALTHCARE("건강관리"),

    // 미용/건강
    COSMETICS("화장품"),
    HEALTH_PRODUCTS("건강용품"),
    HAIR_BODY("헤어/바디"),

    // 계절용품
    SUMMER_PRODUCTS("여름용품"),
    WINTER_PRODUCTS("겨울용품"),
    OUTDOOR_PRODUCTS("야외용품"),

    // 이벤트/취미
    PARTY_SUPPLIES("파티용품"),
    DIY_SUPPLIES("DIY 용품"),
    MUSICAL_INSTRUMENTS("악기/음악"),

    // 자동차/공구
    CAR_ACCESSORIES("자동차 용품"),
    TOOLS("공구/산업용품");

    private final String description;

    SubCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
