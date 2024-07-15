package study.bulletin_board.repository.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.bulletin_board.domain.Category;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryMapper categoryMapper;

    @Override
    public List<String> findAll() {
        return categoryMapper.findAll();
    }

    @Override
    public Optional<Category> findCategory(String category) {
        return categoryMapper.findCategory(category);
    }
}
