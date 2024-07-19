package study.bulletin_board.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import study.bulletin_board.domain.Category;
import study.bulletin_board.repository.category.CategoryRepository;
import study.bulletin_board.service.post.PostService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @MockBean
    PostService postService;

    @Test
    void saveCategory() {
        //given
        Category category = Category.createCategory("유머");
        categoryRepository.save(category);

        //when
        Category searchCategory = categoryRepository.findCategory("유머").get();

        //then
        assertThat(category.getCategory()).isEqualTo(searchCategory.getCategory());
    }

    @Test
    void findAllCategories() {
        //given
        Category category1 = Category.createCategory("유머");
        Category category2 = Category.createCategory("게임");
        Category category3 = Category.createCategory("연예인");
        Category category4 = Category.createCategory("기타");

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);
        categoryRepository.save(category4);

        //then
        List<String> result = categoryRepository.findAll();

        //then
        assertThat(result).containsExactlyInAnyOrder("유머", "게임", "연예인", "기타");
    }
}
