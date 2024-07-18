package study.bulletin_board.repository.category;

import org.apache.ibatis.annotations.Mapper;
import study.bulletin_board.domain.Category;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CategoryMapper {

    void save(Category category);

    List<String> findAll();

    Optional<Category> findCategory(String category);
}
