package study.bulletin_board.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.bulletin_board.domain.Category;
import study.bulletin_board.repository.category.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<String> findAllCategories() {
        return categoryRepository.findAll();
    }
}
