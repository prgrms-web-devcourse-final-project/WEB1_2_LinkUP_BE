package dev_final_team10.GoodBuyUS.domain.category;

import lombok.Getter;

@Getter
public enum FurnitureCategory {
    FURNITURE("가구", FurnitureItems.class),
    INTERIOR_ACCESSORIES("인테리어 소품", InteriorAccessories.class),
    STORAGE("수납용품", StorageItems.class);

    private final String subCategoryName;
    private final Class<? extends Enum<?>> subCategoryDetails;

    FurnitureCategory(String subCategoryName, Class<? extends Enum<?>> subCategoryDetails) {
        this.subCategoryName = subCategoryName;
        this.subCategoryDetails = subCategoryDetails;
    }

    @Getter
    public enum FurnitureItems {
        DESK("책상"),
        CHAIR("의자"),
        BED("침대"),
        SOFA("소파");

        private final String detailName;

        FurnitureItems(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum InteriorAccessories {
        LIGHTING("조명"),
        FRAME("액자"),
        RUG("러그"),
        CURTAIN("커튼");

        private final String detailName;

        InteriorAccessories(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum StorageItems {
        STORAGE_BOX("수납박스"),
        DRAWER("서랍장"),
        HANGER("옷걸이");

        private final String detailName;

        StorageItems(String detailName) {
            this.detailName = detailName;
        }
    }
}
