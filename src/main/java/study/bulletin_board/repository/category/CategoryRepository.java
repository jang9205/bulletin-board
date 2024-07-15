package study.bulletin_board.repository.category;

import study.bulletin_board.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    List<String> findAll();

    Optional<Category> findCategory(String category);
}
