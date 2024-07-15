package study.bulletin_board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostDto {

    @NotBlank(message = "제목을 입력해 주세요.")
    private String title;

    @NotBlank(message = "본문을 작성해 주세요.")
    private String content;

    @NotBlank(message = "카테고리를 선택해 주세요.")
    private String category;

    private String picturePath;
    private MultipartFile picture;

    public PostDto() {
    }

    public PostDto(String title, String content, String category, String picturePath, MultipartFile picture) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.picturePath = picturePath;
        this.picture = picture;
    }
}
