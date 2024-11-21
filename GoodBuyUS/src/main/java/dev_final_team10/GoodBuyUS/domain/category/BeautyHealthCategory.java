package dev_final_team10.GoodBuyUS.domain.category;

import lombok.Getter;

@Getter
public enum BeautyHealthCategory {
    COSMETICS("화장품", Cosmetics.class),
    HEALTH_PRODUCTS("건강용품", HealthProducts.class),
    HAIR_BODY("헤어/바디", HairBody.class);

    private final String subCategoryName;
    private final Class<? extends Enum<?>> subCategoryDetails;

    BeautyHealthCategory(String subCategoryName, Class<? extends Enum<?>> subCategoryDetails) {
        this.subCategoryName = subCategoryName;
        this.subCategoryDetails = subCategoryDetails;
    }

    @Getter
    public enum Cosmetics {
        SKINCARE("스킨케어"),
        MAKEUP("메이크업"),
        MEN_COSMETICS("남성 화장품");

        private final String detailName;

        Cosmetics(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum HealthProducts {
        SCALE("체중계"),
        MASSAGER("마사지기"),
        REHABILITATION_DEVICE("재활 기구");

        private final String detailName;

        HealthProducts(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum HairBody {
        SHAMPOO("샴푸"),
        HAIR_DRYER("헤어드라이어"),
        BODY_LOTION("바디로션");

        private final String detailName;

        HairBody(String detailName) {
            this.detailName = detailName;
        }
    }
}
