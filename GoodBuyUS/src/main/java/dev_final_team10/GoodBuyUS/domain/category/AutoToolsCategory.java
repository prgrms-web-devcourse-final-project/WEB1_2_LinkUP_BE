package dev_final_team10.GoodBuyUS.domain.category;

import lombok.Getter;

@Getter
public enum AutoToolsCategory {
    AUTO_SUPPLIES("자동차 용품", AutoSupplies.class),
    TOOLS_INDUSTRIAL("공구/산업용품", ToolsIndustrial.class);

    private final String subCategoryName;
    private final Class<? extends Enum<?>> subCategoryDetails;

    AutoToolsCategory(String subCategoryName, Class<? extends Enum<?>> subCategoryDetails) {
        this.subCategoryName = subCategoryName;
        this.subCategoryDetails = subCategoryDetails;
    }

    @Getter
    public enum AutoSupplies {
        VEHICLE_MAINTENANCE("차량 관리 용품"),
        NAVIGATION("네비게이션");

        private final String detailName;

        AutoSupplies(String detailName) {
            this.detailName = detailName;
        }
    }

    @Getter
    public enum ToolsIndustrial {
        DRILL("드릴"),
        SCREW("나사"),
        WORK_CLOTHES("작업복");

        private final String detailName;

        ToolsIndustrial(String detailName) {
            this.detailName = detailName;
        }
    }
}
