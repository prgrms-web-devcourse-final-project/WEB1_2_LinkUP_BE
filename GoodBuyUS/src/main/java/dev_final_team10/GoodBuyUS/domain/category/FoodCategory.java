package dev_final_team10.GoodBuyUS.domain.category;

import lombok.Getter;
@Getter
public enum FoodCategory {
    FRESH("신선식품", FreshFood.class),
    PROCESSED("가공식품", ProcessedFood.class),
    HEALTH("건강식품", HealthFood.class);

    private final String subCategoryName;
    private final Class<? extends Enum<?>> subCategoryDetails;

    FoodCategory(String subCategoryName, Class<? extends Enum<?>> subCategoryDetails) {
        this.subCategoryName = subCategoryName;
        this.subCategoryDetails = subCategoryDetails;
    }

    // 예시로 하위 항목을 정의한 Enum
    @Getter
    public enum FreshFood {
        FRUITS("과일"),
        VEGETABLES("채소"),
        MEAT("육류"),
        SEAFOOD("해산물"),
        DAIRY_PRODUCT("유제품");

        private final String detailName;

        FreshFood(String detailName) {
            this.detailName = detailName;
        }

    }

    @Getter
    public enum ProcessedFood {
        CANNED("통조림"),
        SNACKS("스낵류"),
        READY_MEAL("즉석식품"),
        ICED_FOOD("냉동식품");


        private final String detailName;

        ProcessedFood(String detailName) {
            this.detailName = detailName;
        }

    }

    @Getter
    public enum HealthFood {
        VITAMINS("비타민"),
        NUTRITIONAL_SUPPLEMENTS("영양제"),
        SUPER_FOOD("슈퍼푸드");

        private final String detailName;

        HealthFood(String detailName) {
            this.detailName = detailName;
        }

    }
}
