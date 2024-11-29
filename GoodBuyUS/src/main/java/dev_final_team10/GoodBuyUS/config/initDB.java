package dev_final_team10.GoodBuyUS.config;
import dev_final_team10.GoodBuyUS.domain.product.entity.Product;
import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import dev_final_team10.GoodBuyUS.domain.product.category.DetailCategory;
import dev_final_team10.GoodBuyUS.domain.product.category.ProductCategory;
import dev_final_team10.GoodBuyUS.domain.product.category.SubCategory;
import dev_final_team10.GoodBuyUS.repository.ProductPostRepository;
import dev_final_team10.GoodBuyUS.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 상단 상품를 초기에 미리 생성
 */
@EnableJpaAuditing
@Component
@RequiredArgsConstructor
public class initDB {
    private final ProductRepository productRepository;
    private final ProductPostRepository productPostRepository;

    /**
     * 식료품, 생활용품, 패션/의류
     */
    @PostConstruct
    public void createProduct(){
        Product fruit1 = Product.createProduct("싱싱한 사과", 5000, "imageUrl1", ProductCategory.FOOD, SubCategory.FRESH_FOOD, DetailCategory.FRESH_FOOD_FRUITS,3);
        Product fruit2 = Product.createProduct("달콤한 바나나", 4000, "imageUrl2", ProductCategory.FOOD, SubCategory.FRESH_FOOD, DetailCategory.FRESH_FOOD_FRUITS,100);
        Product fruit3 = Product.createProduct("신선한 포도", 6000, "imageUrl3", ProductCategory.FOOD, SubCategory.FRESH_FOOD, DetailCategory.FRESH_FOOD_FRUITS,100);
        Product vegetable1 = Product.createProduct("유기농 당근", 3000, "imageUrl4", ProductCategory.FOOD, SubCategory.FRESH_FOOD, DetailCategory.FRESH_FOOD_VEGETABLES,100);
        Product vegetable2 = Product.createProduct("싱싱한 오이", 2500, "imageUrl5", ProductCategory.FOOD, SubCategory.FRESH_FOOD, DetailCategory.FRESH_FOOD_VEGETABLES,100);
        Product vegetable3 = Product.createProduct("신선한 양파", 2000, "imageUrl6", ProductCategory.FOOD, SubCategory.FRESH_FOOD, DetailCategory.FRESH_FOOD_VEGETABLES,100);
        Product meat1 = Product.createProduct("프리미엄 소고기", 20000, "imageUrl7", ProductCategory.FOOD, SubCategory.FRESH_FOOD, DetailCategory.FRESH_FOOD_MEAT,100);
        Product meat2 = Product.createProduct("신선한 돼지고기", 15000, "imageUrl8", ProductCategory.FOOD, SubCategory.FRESH_FOOD, DetailCategory.FRESH_FOOD_MEAT,100);
        Product meat3 = Product.createProduct("닭가슴살", 10000, "imageUrl9", ProductCategory.FOOD, SubCategory.FRESH_FOOD, DetailCategory.FRESH_FOOD_MEAT,100);
        Product seafood1 = Product.createProduct("싱싱한 연어", 18000, "imageUrl10", ProductCategory.FOOD, SubCategory.FRESH_FOOD, DetailCategory.FRESH_FOOD_SEAFOOD,100);
        Product seafood2 = Product.createProduct("탱글한 새우", 15000, "imageUrl11", ProductCategory.FOOD, SubCategory.FRESH_FOOD, DetailCategory.FRESH_FOOD_SEAFOOD,100);
        Product seafood3 = Product.createProduct("고소한 오징어", 12000, "imageUrl12", ProductCategory.FOOD, SubCategory.FRESH_FOOD, DetailCategory.FRESH_FOOD_SEAFOOD,100);
        Product dairy1 = Product.createProduct("신선한 우유", 4000, "imageUrl13", ProductCategory.FOOD, SubCategory.FRESH_FOOD, DetailCategory.FRESH_FOOD_DAIRY,100);
        Product dairy2 = Product.createProduct("고소한 치즈", 8000, "imageUrl14", ProductCategory.FOOD, SubCategory.FRESH_FOOD, DetailCategory.FRESH_FOOD_DAIRY,100);
        Product dairy3 = Product.createProduct("부드러운 요거트", 5000, "imageUrl15", ProductCategory.FOOD, SubCategory.FRESH_FOOD, DetailCategory.FRESH_FOOD_DAIRY,100);
        Product frozen1 = Product.createProduct("냉동 만두", 5000, "imageUrl1", ProductCategory.FOOD, SubCategory.PROCESSED_FOOD, DetailCategory.PROCESSED_FOOD_FROZEN,100);
        Product frozen2 = Product.createProduct("냉동 피자", 8000, "imageUrl2", ProductCategory.FOOD, SubCategory.PROCESSED_FOOD, DetailCategory.PROCESSED_FOOD_FROZEN,100);
        Product frozen3 = Product.createProduct("냉동 닭가슴살", 10000, "imageUrl3", ProductCategory.FOOD, SubCategory.PROCESSED_FOOD, DetailCategory.PROCESSED_FOOD_FROZEN,100);
        Product snack1 = Product.createProduct("감자칩", 3000, "imageUrl4", ProductCategory.FOOD, SubCategory.PROCESSED_FOOD, DetailCategory.PROCESSED_FOOD_SNACKS,100);
        Product snack2 = Product.createProduct("초콜릿 바", 2500, "imageUrl5", ProductCategory.FOOD, SubCategory.PROCESSED_FOOD, DetailCategory.PROCESSED_FOOD_SNACKS,100);
        Product snack3 = Product.createProduct("쿠키", 4000, "imageUrl6", ProductCategory.FOOD, SubCategory.PROCESSED_FOOD, DetailCategory.PROCESSED_FOOD_SNACKS,100);
        Product drink1 = Product.createProduct("콜라", 1500, "imageUrl7", ProductCategory.FOOD, SubCategory.PROCESSED_FOOD, DetailCategory.PROCESSED_FOOD_DRINKS,100);
        Product drink2 = Product.createProduct("오렌지 주스", 2500, "imageUrl8", ProductCategory.FOOD, SubCategory.PROCESSED_FOOD, DetailCategory.PROCESSED_FOOD_DRINKS,100);
        Product drink3 = Product.createProduct("커피 음료", 3000, "imageUrl9", ProductCategory.FOOD, SubCategory.PROCESSED_FOOD, DetailCategory.PROCESSED_FOOD_DRINKS,100);
        Product canned1 = Product.createProduct("참치 통조림", 3500, "imageUrl10", ProductCategory.FOOD, SubCategory.PROCESSED_FOOD, DetailCategory.PROCESSED_FOOD_CANNED,100);
        Product canned2 = Product.createProduct("옥수수 통조림", 2000, "imageUrl11", ProductCategory.FOOD, SubCategory.PROCESSED_FOOD, DetailCategory.PROCESSED_FOOD_CANNED,100);
        Product canned3 = Product.createProduct("콩 통조림", 2500, "imageUrl12", ProductCategory.FOOD, SubCategory.PROCESSED_FOOD, DetailCategory.PROCESSED_FOOD_CANNED,100);
        Product vitamin1 = Product.createProduct("비타민C", 12000, "imageUrl13", ProductCategory.FOOD, SubCategory.HEALTH_FOOD, DetailCategory.HEALTH_FOOD_VITAMINS,100);
        Product vitamin2 = Product.createProduct("비타민D", 15000, "imageUrl14", ProductCategory.FOOD, SubCategory.HEALTH_FOOD, DetailCategory.HEALTH_FOOD_VITAMINS,100);
        Product vitamin3 = Product.createProduct("종합 비타민", 18000, "imageUrl15", ProductCategory.FOOD, SubCategory.HEALTH_FOOD, DetailCategory.HEALTH_FOOD_VITAMINS,100);
        Product supplement1 = Product.createProduct("프로바이오틱스", 20000, "imageUrl16", ProductCategory.FOOD, SubCategory.HEALTH_FOOD, DetailCategory.HEALTH_FOOD_SUPPLEMENTS,100);
        Product supplement2 = Product.createProduct("오메가3", 25000, "imageUrl17", ProductCategory.FOOD, SubCategory.HEALTH_FOOD, DetailCategory.HEALTH_FOOD_SUPPLEMENTS,100);
        Product supplement3 = Product.createProduct("글루코사민", 30000, "imageUrl18", ProductCategory.FOOD, SubCategory.HEALTH_FOOD, DetailCategory.HEALTH_FOOD_SUPPLEMENTS,100);
        Product superfood1 = Product.createProduct("치아씨드", 15000, "imageUrl19", ProductCategory.FOOD, SubCategory.HEALTH_FOOD, DetailCategory.HEALTH_FOOD_SUPERFOODS,100);
        Product superfood2 = Product.createProduct("아사이베리 분말", 20000, "imageUrl20", ProductCategory.FOOD, SubCategory.HEALTH_FOOD, DetailCategory.HEALTH_FOOD_SUPERFOODS,100);
        Product superfood3 = Product.createProduct("귀리", 8000, "imageUrl21", ProductCategory.FOOD, SubCategory.HEALTH_FOOD, DetailCategory.HEALTH_FOOD_SUPERFOODS,100);
        Product pot1 = Product.createProduct("스테인리스 냄비", 20000, "imageUrl1", ProductCategory.LIFESTYLE, SubCategory.KITCHEN_PRODUCTS, DetailCategory.KITCHEN_TOOLS,100);
        Product pot2 = Product.createProduct("압력솥", 50000, "imageUrl2", ProductCategory.LIFESTYLE, SubCategory.KITCHEN_PRODUCTS, DetailCategory.KITCHEN_TOOLS,100);
        Product pot3 = Product.createProduct("양수 냄비", 15000, "imageUrl3", ProductCategory.LIFESTYLE, SubCategory.KITCHEN_PRODUCTS, DetailCategory.KITCHEN_TOOLS,100);
        Product fryingPan1 = Product.createProduct("코팅 프라이팬", 25000, "imageUrl4", ProductCategory.LIFESTYLE, SubCategory.KITCHEN_PRODUCTS, DetailCategory.KITCHEN_TOOLS_FRYING_PAN,100);
        Product fryingPan2 = Product.createProduct("철제 프라이팬", 30000, "imageUrl5", ProductCategory.LIFESTYLE, SubCategory.KITCHEN_PRODUCTS, DetailCategory.KITCHEN_TOOLS_FRYING_PAN,100);
        Product fryingPan3 = Product.createProduct("세라믹 프라이팬", 28000, "imageUrl6", ProductCategory.LIFESTYLE, SubCategory.KITCHEN_PRODUCTS, DetailCategory.KITCHEN_TOOLS_FRYING_PAN,100);
        Product cuttingBoard2 = Product.createProduct("플라스틱 도마", 8000, "imageUrl8", ProductCategory.LIFESTYLE, SubCategory.KITCHEN_PRODUCTS, DetailCategory.KITCHEN_TOOLS_CUTTING_BOARD,100);
        Product cuttingBoard1 = Product.createProduct("대나무 도마", 10000, "imageUrl7", ProductCategory.LIFESTYLE, SubCategory.KITCHEN_PRODUCTS, DetailCategory.KITCHEN_TOOLS_CUTTING_BOARD,100);
        Product cuttingBoard3 = Product.createProduct("항균 도마", 15000, "imageUrl9", ProductCategory.LIFESTYLE, SubCategory.KITCHEN_PRODUCTS, DetailCategory.KITCHEN_TOOLS_CUTTING_BOARD,100);
        Product knife1 = Product.createProduct("일반 주방용 칼", 12000, "imageUrl10", ProductCategory.LIFESTYLE, SubCategory.KITCHEN_PRODUCTS, DetailCategory.KITCHEN_TOOLS_KNIFE,100);
        Product knife2 = Product.createProduct("세라믹 칼", 20000, "imageUrl11", ProductCategory.LIFESTYLE, SubCategory.KITCHEN_PRODUCTS, DetailCategory.KITCHEN_TOOLS_KNIFE,100);
        Product knife3 = Product.createProduct("다용도 칼", 15000, "imageUrl12", ProductCategory.LIFESTYLE, SubCategory.KITCHEN_PRODUCTS, DetailCategory.KITCHEN_TOOLS_KNIFE,100);
        Product appliances1 = Product.createProduct("믹서기", 45000, "imageUrl13", ProductCategory.LIFESTYLE, SubCategory.KITCHEN_PRODUCTS, DetailCategory.KITCHEN_TOOLS_APPLIANCES,100);
        Product appliances2 = Product.createProduct("토스터기", 35000, "imageUrl14", ProductCategory.LIFESTYLE, SubCategory.KITCHEN_PRODUCTS, DetailCategory.KITCHEN_TOOLS_APPLIANCES,100);
        Product appliances3 = Product.createProduct("전기밥솥", 60000, "imageUrl15", ProductCategory.LIFESTYLE, SubCategory.KITCHEN_PRODUCTS, DetailCategory.KITCHEN_TOOLS_APPLIANCES,100);
        Product bathroomTool1 = Product.createProduct("비누", 2000, "imageUrl16", ProductCategory.LIFESTYLE, SubCategory.BATHROOM_PRODUCTS, DetailCategory.BATHROOM_TOOLS,100);
        Product bathroomTool2 = Product.createProduct("치약", 3000, "imageUrl17", ProductCategory.LIFESTYLE, SubCategory.BATHROOM_PRODUCTS, DetailCategory.BATHROOM_TOOLS,100);
        Product bathroomTool3 = Product.createProduct("샴푸", 8000, "imageUrl18", ProductCategory.LIFESTYLE, SubCategory.BATHROOM_PRODUCTS, DetailCategory.BATHROOM_TOOLS,100);
        Product towel1 = Product.createProduct("목욕 수건", 5000, "imageUrl19", ProductCategory.LIFESTYLE, SubCategory.BATHROOM_PRODUCTS, DetailCategory.BATHROOM_TOOLS_TOWELS,100);
        Product towel2 = Product.createProduct("페이스 타월", 3000, "imageUrl20", ProductCategory.LIFESTYLE, SubCategory.BATHROOM_PRODUCTS, DetailCategory.BATHROOM_TOOLS_TOWELS,100);
        Product towel3 = Product.createProduct("손수건", 2000, "imageUrl21", ProductCategory.LIFESTYLE, SubCategory.BATHROOM_PRODUCTS, DetailCategory.BATHROOM_TOOLS_TOWELS,100);
        Product cleaningTool1 = Product.createProduct("욕실 브러쉬", 7000, "imageUrl22", ProductCategory.LIFESTYLE, SubCategory.BATHROOM_PRODUCTS, DetailCategory.BATHROOM_TOOLS_CLEANING,100);
        Product cleaningTool2 = Product.createProduct("욕실용 세제", 5000, "imageUrl23", ProductCategory.LIFESTYLE, SubCategory.BATHROOM_PRODUCTS, DetailCategory.BATHROOM_TOOLS_CLEANING,100);
        Product cleaningTool3 = Product.createProduct("욕실 걸레", 6000, "imageUrl24", ProductCategory.LIFESTYLE, SubCategory.BATHROOM_PRODUCTS, DetailCategory.BATHROOM_TOOLS_CLEANING,100);
        Product detergent1 = Product.createProduct("액체 세탁 세제", 10000, "imageUrl1", ProductCategory.LIFESTYLE, SubCategory.CLEANING_PRODUCTS, DetailCategory.CLEANING_SUPPLIES_DETERGENT,100);
        Product detergent2 = Product.createProduct("가루 세제", 8000, "imageUrl2", ProductCategory.LIFESTYLE, SubCategory.CLEANING_PRODUCTS, DetailCategory.CLEANING_SUPPLIES_DETERGENT,100);
        Product detergent3 = Product.createProduct("친환경 세제", 12000, "imageUrl3", ProductCategory.LIFESTYLE, SubCategory.CLEANING_PRODUCTS, DetailCategory.CLEANING_SUPPLIES_DETERGENT,100);
        Product vacuum1 = Product.createProduct("유선 청소기", 50000, "imageUrl4", ProductCategory.LIFESTYLE, SubCategory.CLEANING_PRODUCTS, DetailCategory.CLEANING_SUPPLIES_VACUUM,100);
        Product vacuum2 = Product.createProduct("무선 청소기", 120000, "imageUrl5", ProductCategory.LIFESTYLE, SubCategory.CLEANING_PRODUCTS, DetailCategory.CLEANING_SUPPLIES_VACUUM,100);
        Product vacuum3 = Product.createProduct("로봇 청소기", 300000, "imageUrl6", ProductCategory.LIFESTYLE, SubCategory.CLEANING_PRODUCTS, DetailCategory.CLEANING_SUPPLIES_VACUUM,100);
        Product trashBag1 = Product.createProduct("20L 쓰레기봉투", 2000, "imageUrl7", ProductCategory.LIFESTYLE, SubCategory.CLEANING_PRODUCTS, DetailCategory.CLEANING_SUPPLIES_TRASH_BAGS,100);
        Product trashBag2 = Product.createProduct("50L 쓰레기봉투", 4000, "imageUrl8", ProductCategory.LIFESTYLE, SubCategory.CLEANING_PRODUCTS, DetailCategory.CLEANING_SUPPLIES_TRASH_BAGS,100);
        Product trashBag3 = Product.createProduct("100L 쓰레기봉투", 7000, "imageUrl9", ProductCategory.LIFESTYLE, SubCategory.CLEANING_PRODUCTS, DetailCategory.CLEANING_SUPPLIES_TRASH_BAGS,100);
        Product mop1 = Product.createProduct("일반 걸레", 5000, "imageUrl10", ProductCategory.LIFESTYLE, SubCategory.CLEANING_PRODUCTS, DetailCategory.CLEANING_SUPPLIES_MOP,100);
        Product mop2 = Product.createProduct("마른 걸레", 6000, "imageUrl11", ProductCategory.LIFESTYLE, SubCategory.CLEANING_PRODUCTS, DetailCategory.CLEANING_SUPPLIES_MOP,100);
        Product mop3 = Product.createProduct("스팀 걸레", 25000, "imageUrl12", ProductCategory.LIFESTYLE, SubCategory.CLEANING_PRODUCTS, DetailCategory.CLEANING_SUPPLIES_MOP,100);
        Product menTshirt1 = Product.createProduct("기본 티셔츠", 15000, "imageUrl1", ProductCategory.FASHION, SubCategory.MEN_CLOTHING, DetailCategory.MEN_CLOTHING_TSHIRT,100);
        Product menTshirt2 = Product.createProduct("스트라이프 티셔츠", 20000, "imageUrl2", ProductCategory.FASHION, SubCategory.MEN_CLOTHING, DetailCategory.MEN_CLOTHING_TSHIRT,100);
        Product menTshirt3 = Product.createProduct("긴팔 티셔츠", 25000, "imageUrl3", ProductCategory.FASHION, SubCategory.MEN_CLOTHING, DetailCategory.MEN_CLOTHING_TSHIRT,100);
        Product menPants1 = Product.createProduct("청바지", 40000, "imageUrl4", ProductCategory.FASHION, SubCategory.MEN_CLOTHING, DetailCategory.MEN_CLOTHING_PANTS,100);
        Product menPants2 = Product.createProduct("슬랙스", 35000, "imageUrl5", ProductCategory.FASHION, SubCategory.MEN_CLOTHING, DetailCategory.MEN_CLOTHING_PANTS,100);
        Product menPants3 = Product.createProduct("운동복 바지", 30000, "imageUrl6", ProductCategory.FASHION, SubCategory.MEN_CLOTHING, DetailCategory.MEN_CLOTHING_PANTS,100);
        Product menOuter1 = Product.createProduct("가죽 자켓", 90000, "imageUrl7", ProductCategory.FASHION, SubCategory.MEN_CLOTHING, DetailCategory.MEN_CLOTHING_OUTER,100);
        Product menOuter2 = Product.createProduct("패딩", 120000, "imageUrl8", ProductCategory.FASHION, SubCategory.MEN_CLOTHING, DetailCategory.MEN_CLOTHING_OUTER,100);
        Product menOuter3 = Product.createProduct("트렌치코트", 150000, "imageUrl9", ProductCategory.FASHION, SubCategory.MEN_CLOTHING, DetailCategory.MEN_CLOTHING_OUTER,100);
        Product womenDress1 = Product.createProduct("미니 원피스", 30000, "imageUrl10", ProductCategory.FASHION, SubCategory.WOMEN_CLOTHING, DetailCategory.WOMEN_CLOTHING_DRESS,100);
        Product womenDress2 = Product.createProduct("플라워 원피스", 35000, "imageUrl11", ProductCategory.FASHION, SubCategory.WOMEN_CLOTHING, DetailCategory.WOMEN_CLOTHING_DRESS,100);
        Product womenDress3 = Product.createProduct("셔츠 원피스", 40000, "imageUrl12", ProductCategory.FASHION, SubCategory.WOMEN_CLOTHING, DetailCategory.WOMEN_CLOTHING_DRESS,100);
        Product womenBlouse1 = Product.createProduct("쉬폰 블라우스", 25000, "imageUrl13", ProductCategory.FASHION, SubCategory.WOMEN_CLOTHING, DetailCategory.WOMEN_CLOTHING_BLOUSE,100);
        Product womenBlouse2 = Product.createProduct("레이스 블라우스", 28000, "imageUrl14", ProductCategory.FASHION, SubCategory.WOMEN_CLOTHING, DetailCategory.WOMEN_CLOTHING_BLOUSE,100);
        Product womenBlouse3 = Product.createProduct("실크 블라우스", 30000, "imageUrl15", ProductCategory.FASHION, SubCategory.WOMEN_CLOTHING, DetailCategory.WOMEN_CLOTHING_BLOUSE,100);
        Product womenSkirt1 = Product.createProduct("플리츠 스커트", 20000, "imageUrl16", ProductCategory.FASHION, SubCategory.WOMEN_CLOTHING, DetailCategory.WOMEN_CLOTHING_SKIRT,100);
        Product womenSkirt2 = Product.createProduct("타이트 스커트", 22000, "imageUrl17", ProductCategory.FASHION, SubCategory.WOMEN_CLOTHING, DetailCategory.WOMEN_CLOTHING_SKIRT,100);
        Product womenSkirt3 = Product.createProduct("롱 스커트", 25000, "imageUrl18", ProductCategory.FASHION, SubCategory.WOMEN_CLOTHING, DetailCategory.WOMEN_CLOTHING_SKIRT,100);
        Product bag1 = Product.createProduct("토트백", 50000, "imageUrl19", ProductCategory.FASHION, SubCategory.ACCESSORIES, DetailCategory.ACCESSORIES_BAGS,100);
        Product bag2 = Product.createProduct("백팩", 60000, "imageUrl20", ProductCategory.FASHION, SubCategory.ACCESSORIES, DetailCategory.ACCESSORIES_BAGS,100);
        Product bag3 = Product.createProduct("크로스백", 55000, "imageUrl21", ProductCategory.FASHION, SubCategory.ACCESSORIES, DetailCategory.ACCESSORIES_BAGS,100);
        Product wallet1 = Product.createProduct("가죽 지갑", 30000, "imageUrl22", ProductCategory.FASHION, SubCategory.ACCESSORIES, DetailCategory.ACCESSORIES_WALLETS,100);
        Product wallet2 = Product.createProduct("지퍼 지갑", 28000, "imageUrl23", ProductCategory.FASHION, SubCategory.ACCESSORIES, DetailCategory.ACCESSORIES_WALLETS,100);
        Product wallet3 = Product.createProduct("카드 지갑", 20000, "imageUrl24", ProductCategory.FASHION, SubCategory.ACCESSORIES, DetailCategory.ACCESSORIES_WALLETS,100);
        Product hat1 = Product.createProduct("야구 모자", 15000, "imageUrl25", ProductCategory.FASHION, SubCategory.ACCESSORIES, DetailCategory.ACCESSORIES_HATS,100);
        Product hat2 = Product.createProduct("버킷햇", 18000, "imageUrl26", ProductCategory.FASHION, SubCategory.ACCESSORIES, DetailCategory.ACCESSORIES_HATS,100);
        Product hat3 = Product.createProduct("비니", 20000, "imageUrl27", ProductCategory.FASHION, SubCategory.ACCESSORIES, DetailCategory.ACCESSORIES_HATS,100);
        Product shoe1 = Product.createProduct("운동화", 70000, "imageUrl28", ProductCategory.FASHION, SubCategory.ACCESSORIES, DetailCategory.ACCESSORIES_SHOES,100);
        Product shoe2 = Product.createProduct("구두", 85000, "imageUrl29", ProductCategory.FASHION, SubCategory.ACCESSORIES, DetailCategory.ACCESSORIES_SHOES,100);
        Product shoe3 = Product.createProduct("슬리퍼", 20000, "imageUrl30", ProductCategory.FASHION, SubCategory.ACCESSORIES, DetailCategory.ACCESSORIES_SHOES,100);
        Product kidsTshirt1 = Product.createProduct("아동 반팔 티셔츠", 12000, "imageUrl31", ProductCategory.FASHION, SubCategory.CHILDREN_CLOTHING, DetailCategory.KIDS_CLOTHING_TSHIRT,100);
        Product kidsTshirt2 = Product.createProduct("아동 긴팔 티셔츠", 15000, "imageUrl32", ProductCategory.FASHION, SubCategory.CHILDREN_CLOTHING, DetailCategory.KIDS_CLOTHING_TSHIRT,100);
        Product kidsTshirt3 = Product.createProduct("아동 프린트 티셔츠", 14000, "imageUrl33", ProductCategory.FASHION, SubCategory.CHILDREN_CLOTHING, DetailCategory.KIDS_CLOTHING_TSHIRT,100);
        Product kidsPants1 = Product.createProduct("아동 청바지", 18000, "imageUrl34", ProductCategory.FASHION, SubCategory.CHILDREN_CLOTHING, DetailCategory.KIDS_CLOTHING_PANTS,100);
        Product kidsPants2 = Product.createProduct("아동 트레이닝 팬츠", 15000, "imageUrl35", ProductCategory.FASHION, SubCategory.CHILDREN_CLOTHING, DetailCategory.KIDS_CLOTHING_PANTS,100);
        Product kidsPants3 = Product.createProduct("아동 면바지", 16000, "imageUrl36", ProductCategory.FASHION, SubCategory.CHILDREN_CLOTHING, DetailCategory.KIDS_CLOTHING_PANTS,100);
        Product kidsShoes1 = Product.createProduct("아동 운동화", 25000, "imageUrl37", ProductCategory.FASHION, SubCategory.CHILDREN_CLOTHING, DetailCategory.KIDS_CLOTHING_SHOES,100);
        Product kidsShoes2 = Product.createProduct("아동 샌들", 20000, "imageUrl38", ProductCategory.FASHION, SubCategory.CHILDREN_CLOTHING, DetailCategory.KIDS_CLOTHING_SHOES,100);

        productRepository.saveAll(Arrays.asList(
                fruit1,fruit2, fruit3,
                vegetable1,
                vegetable2,
                vegetable3,
                meat1,meat2, meat3,
                seafood1, seafood2, seafood3,
                dairy1, dairy2, dairy3,
                frozen1, frozen2, frozen3,
                snack1, snack2, snack3,
                drink1, drink2, drink3,
                canned1, canned2, canned3,
                vitamin1, vitamin2, vitamin3,
                supplement1, supplement2, supplement3,
                superfood1, superfood2, superfood3,
                pot1, pot2, pot3,
                fryingPan1, fryingPan2, fryingPan3,
                cuttingBoard2, cuttingBoard1, cuttingBoard3,
                knife1, knife2, knife3,
                appliances1, appliances2, appliances3,
                bathroomTool1, bathroomTool2, bathroomTool3,
                towel1, towel2, towel3,
                cleaningTool1, cleaningTool2, cleaningTool3,
                detergent1, detergent2, detergent3,
                vacuum1, vacuum2, vacuum3,
                trashBag1, trashBag2, trashBag3,
                mop1, mop2, mop3,
                menTshirt1, menTshirt2, menTshirt3,
                menPants1, menPants2, menPants3,
                menOuter1, menOuter2, menOuter3,
                womenDress1, womenDress2, womenDress3,
                womenBlouse1, womenBlouse2, womenBlouse3,
                womenSkirt1, womenSkirt2, womenSkirt3,
                bag1, bag2, bag3,
                wallet1, wallet2, wallet3,
                hat1, hat2, hat3,
                shoe1, shoe2, shoe3,
                kidsTshirt1, kidsTshirt2, kidsTshirt3,
                kidsPants1, kidsPants2, kidsPants3,
                kidsShoes1, kidsShoes2
                ));

    }

    @PostConstruct
    public void createProPost(){
        List<Product> products = productRepository.findAll();
        Random random = new Random();
        for (Product product : products) {
            int minAmount = random.nextInt(2) + 3;
            int dayCount = random.nextInt(3)+1;
            ProductPost productPost = ProductPost.createProPost(product,product.getProductName()+"에 관한 상품 설명입니다.",minAmount, LocalDateTime.now().plusDays(dayCount));
            productPostRepository.save(productPost);
        }
    }
}
