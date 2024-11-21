package dev_final_team10.GoodBuyUS.domain.category;

import lombok.Getter;

@Getter
public enum EventsHobbiesCategory {
    PARTY_SUPPLIES("파티용품", PartySupplies.class),
    DIY_GOODS("DIY 용품", DiyGoods.class),
    MUSICAL_INSTRUMENTS("악기/음악", MusicalInstruments.class);

    private final String subCategoryName;
    private final Class<? extends Enum<?>> subCategoryDetails;

    EventsHobbiesCategory(String subCategoryName, Class<? extends Enum<?>> subCategoryDetails) {
        this.subCategoryName = subCategoryName;
        this.subCategoryDetails = subCategoryDetails;
    }

    @Getter
    public enum PartySupplies {
        BALLOONS("풍선"),
        TABLE_SETTING("테이블 세팅"),
        DECORATIONS("장식품");

        private final String detailName;

        PartySupplies(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum DiyGoods {
        CRAFT_KITS("공예 키트"),
        PAINTS("페인트"),
        TOOL_KITS("공구 세트");

        private final String detailName;

        DiyGoods(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum MusicalInstruments {
        GUITAR("기타"),
        KEYBOARD("키보드"),
        SOUND_EQUIPMENT("음향 장비");

        private final String detailName;

        MusicalInstruments(String detailName) {
            this.detailName = detailName;
        }
    }
}
