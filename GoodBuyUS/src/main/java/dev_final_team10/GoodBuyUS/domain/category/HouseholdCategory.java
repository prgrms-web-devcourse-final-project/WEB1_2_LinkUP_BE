package dev_final_team10.GoodBuyUS.domain.category;

import lombok.Getter;

@Getter
public enum HouseholdCategory {
    KITCHEN("주방용품", KitchenItems.class),
    BATHROOM("욕실용품", BathroomItems.class),
    CLEANING("청소용품", CleaningItems.class);

    private final String subCategoryName;
    private final Class<? extends Enum<?>> subCategoryDetails;

    HouseholdCategory(String subCategoryName, Class<? extends Enum<?>> subCategoryDetails) {
        this.subCategoryName = subCategoryName;
        this.subCategoryDetails = subCategoryDetails;
    }

    @Getter
    public enum KitchenItems {
        POT("냄비"),
        FRYING_PAN("프라이팬"),
        CUTTING_BOARD("도마"),
        KNIFE("칼"),
        KITCHEN_APPLIANCES("주방가전");

        private final String detailName;

        KitchenItems(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum BathroomItems {
        TOILETRIES("세면도구"),
        TOWEL("수건"),
        BATHROOM_CLEANING_TOOLS("욕실 청소 도구");

        private final String detailName;

        BathroomItems(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum CleaningItems {
        DETERGENT("세제"),
        VACUUM_CLEANER("청소기"),
        TRASH_BAG("쓰레기봉투"),
        MOP("걸레");

        private final String detailName;

        CleaningItems(String detailName) {
            this.detailName = detailName;
        }
    }
}
