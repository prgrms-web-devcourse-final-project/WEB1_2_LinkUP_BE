package dev_final_team10.GoodBuyUS.domain.community.entity;

import lombok.Getter;

@Getter
public enum CommunityCategory {
    FOOD("식료품"),
    LIFESTYLE("생활용품"),
    FASHION("패션/의류"),
    ELECTRONICS("전자제품"),
    FURNITURE("가구/인테리어"),
    KIDS("유아/아동용품"),
    SPORTS("스포츠/레저");
    private final String categoryName;
    CommunityCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    //Enum에서 이름으로 Category 찾기 (프론트단에서 카테고리로 "이벤트/취미"등을 넘겨줬을 때 처리)
    public static CommunityCategory fromString(String name){
        for (CommunityCategory category : CommunityCategory.values()) {
            if(category.categoryName.equals(name)){
                return category;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 카테고리입니다." + name);
    }
}