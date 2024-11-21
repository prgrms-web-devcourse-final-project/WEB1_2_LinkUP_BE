package dev_final_team10.GoodBuyUS.domain.category;

import lombok.Getter;

@Getter
public enum SportsCategory {
    SPORTS_EQUIPMENT("스포츠 장비", SportsEquipment.class),
    CAMPING("캠핑용품", CampingItems.class),
    LEISURE("레저용품", LeisureItems.class);

    private final String subCategoryName;
    private final Class<? extends Enum<?>> subCategoryDetails;

    SportsCategory(String subCategoryName, Class<? extends Enum<?>> subCategoryDetails) {
        this.subCategoryName = subCategoryName;
        this.subCategoryDetails = subCategoryDetails;
    }

    @Getter
    public enum SportsEquipment {
        BICYCLE("자전거"),
        SOCCER_BALL("축구공"),
        YOGA_MAT("요가 매트");

        private final String detailName;

        SportsEquipment(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum CampingItems {
        TENT("텐트"),
        SLEEPING_BAG("침낭"),
        CAMPING_CHAIR("캠핑 의자");

        private final String detailName;

        CampingItems(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum LeisureItems {
        DRONE("드론"),
        FISHING_ROD("낚싯대"),
        SCUBA_EQUIPMENT("스쿠버 장비");

        private final String detailName;

        LeisureItems(String detailName) {
            this.detailName = detailName;
        }
    }
}
