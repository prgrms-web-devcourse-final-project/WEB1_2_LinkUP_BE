package dev_final_team10.GoodBuyUS.domain.category;

import lombok.Getter;

@Getter
public enum FashionCategory {
    MEN("남성의류", MenClothing.class),
    WOMEN("여성의류", WomenClothing.class),
    ACCESSORIES("액세서리", Accessories.class),
    KIDS("유아동복", KidsClothing.class);

    private final String subCategoryName;
    private final Class<? extends Enum<?>> subCategoryDetails;

    FashionCategory(String subCategoryName, Class<? extends Enum<?>> subCategoryDetails) {
        this.subCategoryName = subCategoryName;
        this.subCategoryDetails = subCategoryDetails;
    }

    @Getter
    public enum MenClothing {
        T_SHIRT("티셔츠"),
        PANTS("바지"),
        OUTER("아우터");

        private final String detailName;

        MenClothing(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum WomenClothing {
        DRESS("원피스"),
        BLOUSE("블라우스"),
        SKIRT("스커트");

        private final String detailName;

        WomenClothing(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum Accessories {
        BAG("가방"),
        WALLET("지갑"),
        HAT("모자"),
        SHOES("신발");

        private final String detailName;

        Accessories(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum KidsClothing {
        KIDS_T_SHIRT("아동 티셔츠"),
        KIDS_PANTS("바지"),
        KIDS_SHOES("신발");

        private final String detailName;

        KidsClothing(String detailName) {
            this.detailName = detailName;
        }
    }
}
