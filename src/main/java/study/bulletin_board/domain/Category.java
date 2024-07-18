package study.bulletin_board.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Category {

    private Long id;
    private String category;

    //생성 메서드
    public static Category createCategory(String categoryName) {
        Category category = new Category();
        category.setCategory(categoryName);
        return category;
    }
}
