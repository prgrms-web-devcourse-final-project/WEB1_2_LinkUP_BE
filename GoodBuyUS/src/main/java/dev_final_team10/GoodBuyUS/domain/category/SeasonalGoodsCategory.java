package dev_final_team10.GoodBuyUS.domain.category;

import lombok.Getter;

@Getter
public enum SeasonalGoodsCategory {
    SUMMER_GOODS("여름용품", SummerGoods.class),
    WINTER_GOODS("겨울용품", WinterGoods.class),
    OUTDOOR_GOODS("야외용품", OutdoorGoods.class);

    private final String subCategoryName;
    private final Class<? extends Enum<?>> subCategoryDetails;

    SeasonalGoodsCategory(String subCategoryName, Class<? extends Enum<?>> subCategoryDetails) {
        this.subCategoryName = subCategoryName;
        this.subCategoryDetails = subCategoryDetails;
    }

    @Getter
    public enum SummerGoods {
        FAN("선풍기"),
        AIR_CONDITIONER("에어컨"),
        SUMMER_CLOTHING("여름 의류");

        private final String detailName;

        SummerGoods(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum WinterGoods {
        HEATER("히터"),
        HEATING_MAT("온열매트"),
        THICK_BLANKET("두꺼운 담요");

        private final String detailName;

        WinterGoods(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum OutdoorGoods {
        HAMMOCK("해먹"),
        PARASOL("파라솔"),
        ICE_BOX("아이스박스");

        private final String detailName;

        OutdoorGoods(String detailName) {
            this.detailName = detailName;
        }
    }
}
