package dev_final_team10.GoodBuyUS.domain.category;

import lombok.Getter;

@Getter
public enum KidsCategory {
    TOYS("완구/장난감", Toys.class),
    EDUCATIONAL("교육용품", EducationalItems.class),
    BABY_SUPPLIES("유아용품", BabySupplies.class);

    private final String subCategoryName;
    private final Class<? extends Enum<?>> subCategoryDetails;

    KidsCategory(String subCategoryName, Class<? extends Enum<?>> subCategoryDetails) {
        this.subCategoryName = subCategoryName;
        this.subCategoryDetails = subCategoryDetails;
    }

    @Getter
    public enum Toys {
        BLOCKS("블록"),
        PUZZLES("퍼즐"),
        DOLLS("인형");

        private final String detailName;

        Toys(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum EducationalItems {
        LEARNING_TOOLS("학습도구"),
        BOOKS("책"),
        FLASH_CARDS("플래시 카드");

        private final String detailName;

        EducationalItems(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum BabySupplies {
        DIAPERS("기저귀"),
        STROLLER("유모차"),
        BABY_BED("아기 침대");

        private final String detailName;

        BabySupplies(String detailName) {
            this.detailName = detailName;
        }
    }
}
