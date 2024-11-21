package dev_final_team10.GoodBuyUS.domain.category;

import lombok.Getter;

@Getter
public enum ElectronicsCategory {
    HOME_APPLIANCES("가전제품", HomeAppliances.class),
    IT_DEVICES("IT 기기", ItDevices.class),
    SMALL_APPLIANCES("소형가전", SmallAppliances.class),
    PERIPHERALS("주변기기", Peripherals.class);

    private final String subCategoryName;
    private final Class<? extends Enum<?>> subCategoryDetails;

    ElectronicsCategory(String subCategoryName, Class<? extends Enum<?>> subCategoryDetails) {
        this.subCategoryName = subCategoryName;
        this.subCategoryDetails = subCategoryDetails;
    }

    @Getter
    public enum HomeAppliances {
        REFRIGERATOR("냉장고"),
        WASHING_MACHINE("세탁기"),
        AIR_PURIFIER("공기청정기");

        private final String detailName;

        HomeAppliances(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum ItDevices {
        LAPTOP("노트북"),
        TABLET("태블릿"),
        SMARTPHONE("스마트폰");

        private final String detailName;

        ItDevices(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum SmallAppliances {
        COFFEE_MACHINE("커피머신"),
        ELECTRIC_KETTLE("전기포트"),
        BLENDER("믹서기");

        private final String detailName;

        SmallAppliances(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum Peripherals {
        CHARGER("충전기"),
        EARPHONES("이어폰"),
        CABLE("케이블");

        private final String detailName;

        Peripherals(String detailName) {
            this.detailName = detailName;
        }
    }
}
