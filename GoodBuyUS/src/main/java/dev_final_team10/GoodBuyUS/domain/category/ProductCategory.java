package dev_final_team10.GoodBuyUS.domain.category;
import lombok.Getter;

@Getter
public enum ProductCategory {
    FOOD("식료품", FoodCategory.class),
    LIFESTYLE("생활용품", HouseholdCategory.class),
    FASHION("패션/의류", FashionCategory.class),
    ELECTRONICS("전자제품", ElectronicsCategory.class),
    FURNITURE("가구/인테리어", FurnitureCategory.class),
    KIDS("유아/아동용품", KidsCategory.class),
    SPORTS("스포츠/레저", SportsCategory.class),
    PETS("반려동물 용품", PetSuppliesCategory.class),
    BEAUTY("미용/건강", BeautyHealthCategory.class),
    SEASONAL("계절용품", SeasonalGoodsCategory.class),
    HOBBIES("이벤트/취미", EventsHobbiesCategory.class),
    AUTOMOTIVE("자동차/공구", AutoToolsCategory.class);

    private final String categoryName;
    private final Class<? extends Enum<?>> subCategory;

    ProductCategory(String categoryName, Class<? extends Enum<?>> subCategory) {
        this.categoryName = categoryName;
        this.subCategory = subCategory;
    }
}


