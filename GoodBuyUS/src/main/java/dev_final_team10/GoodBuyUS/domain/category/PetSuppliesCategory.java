package dev_final_team10.GoodBuyUS.domain.category;

import lombok.Getter;

@Getter
public enum PetSuppliesCategory {
    FOOD_SNACKS("사료/간식", FoodSnacks.class),
    LIVING_ITEMS("생활용품", LivingItems.class),
    HEALTH_CARE("건강관리", HealthCare.class);

    private final String subCategoryName;
    private final Class<? extends Enum<?>> subCategoryDetails;

    PetSuppliesCategory(String subCategoryName, Class<? extends Enum<?>> subCategoryDetails) {
        this.subCategoryName = subCategoryName;
        this.subCategoryDetails = subCategoryDetails;
    }

    @Getter
    public enum FoodSnacks {
        DOG_FOOD("강아지 사료"),
        CAT_FOOD("고양이 사료"),
        OTHER_PET_FOOD("기타 반려동물 사료");

        private final String detailName;

        FoodSnacks(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum LivingItems {
        TOYS("장난감"),
        COLLAR("목줄"),
        CARRIER("이동장");

        private final String detailName;

        LivingItems(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum HealthCare {
        SHAMPOO("샴푸"),
        VITAMINS("비타민"),
        MEDICATION("치료제");

        private final String detailName;

        HealthCare(String detailName) {
            this.detailName = detailName;
        }
    }
}
