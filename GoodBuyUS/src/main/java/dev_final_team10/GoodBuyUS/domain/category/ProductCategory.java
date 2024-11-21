package dev_final_team10.GoodBuyUS.domain.category;
import lombok.Getter;

@Getter
public enum ProductCategory {
    FOOD("식료품"),
    LIFESTYLE("생활용품"),
    FASHION("패션/의류"),
    ELECTRONICS("전자제품"),
    FURNITURE("가구/인테리어"),
    KIDS("유아/아동용품"),
    SPORTS("스포츠/레저"),
    PETS("반려동물 용품"),
    BEAUTY("미용/건강"),
    SEASONAL("계절용품"),
    HOBBIES("이벤트/취미"),
    AUTOMOTIVE("자동차/공구");
    private final String categoryName;
    ProductCategory(String categoryName) {
        this.categoryName = categoryName;
    }
}


