package com.example.demo.controller;

import com.example.demo.dto.ArticleDto;
import com.example.demo.dto.ArticleForm;
import com.example.demo.model.MemberUser;
import com.example.demo.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/list-without-pagination")
    public String getArticleList(Model model) {
        List<ArticleDto> articles = articleService.findAllWithoutPagination();
        model.addAttribute("articles", articles);
        return "article-list-without-pagination";
    }

    // Pageable http://localhost:8080/article/list?page=0&size=10&sort=id,asc&sort=name,desc
    // @PageableDefault(page, size, sort, direction) - 이 경우 sort는 하나만 가능
    // 여러개의 Sort 옵션을 전달하려면 @PageableDefault로는 page와 size만 전달하고 추가로 @SortDefault.SortDefaults()를 전달
//    @GetMapping("/list")
//    public String getArticleList(@PageableDefault(page = 0, size = 10)
//                                 @SortDefault.SortDefaults({
//                                         @SortDefault(sort="id", direction = Sort.Direction.DESC),
//                                         @SortDefault(sort="updated", direction = Sort.Direction.ASC)
//                                 }) Pageable pageable, Model model) {
//        Page<ArticleDto> page = articleService.findAll(pageable);
//        model.addAttribute("page", page);
//        return "article-list";
//    }

    @GetMapping("/list")
    public String getArticleList(@PageableDefault(page = 0, size = 10, sort="id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<ArticleDto> page = articleService.findAll(pageable);
        model.addAttribute("page", page);
        return "article-list";
    }

    @GetMapping("/content")
    public String getArticle(@RequestParam("id") Long id, Model model) {
        model.addAttribute("article", articleService.findById(id));
        return "article-content";
    }

    // 게시글 입력 폼 요청
//    @GetMapping("/add")
//    public String getArticleAdd() {
//        return "article-add";
//    }

    // 디폴트 값이 채워진 게시글 입력 폼 요청
//    @GetMapping("/add")
//    public String getArticleAdd(Model model) {
//        var articleForm = ArticleForm.builder()
//                .description("바르고 고운말을 사용하여 주세요^^")
//                .build();
//        model.addAttribute("article", articleForm);
//        return "article-add";
//    }

    // 메서드 파라미터로 모델 어트리뷰트를 전달 받으면 자동으로 모델 객체에 추가된다
    @GetMapping("/add")
    public String getArticleAdd(@ModelAttribute("article") ArticleForm articleForm) {
        articleForm.setDescription("바르고 고운말을 사용하여 주세요^^");
        return "article-add";
    }

    // 입력 폼에 있는 게시글을 저장
//    @PostMapping("/add")
//    public String postArticleAddWithoutValidation(@ModelAttribute("article") ArticleForm articleForm) {
//        articleService.create(1L, articleForm);
//        return "redirect:/article/list";
//    }

    // 입력 폼 검증 추가
    @PostMapping("/add")
    public String postArticleAdd(@Valid @ModelAttribute("article") ArticleForm articleForm,
                                 BindingResult bindingResult,
                                 @AuthenticationPrincipal MemberUser memberUser) {

        if (articleForm.getTitle() != null && articleForm.getTitle().contains("T발")) {
            bindingResult.rejectValue("title", "SlangDetected", "욕설을 사용하지 마세요");
        }
        if (articleForm.getDescription() != null && articleForm.getDescription().contains("T발")) {
            bindingResult.rejectValue("description", "SlangDetected", "욕설을 사용하지 마세요");
        }

        if (bindingResult.hasErrors()) {
            return "article-add";
        }

        articleService.create(memberUser.getId(), articleForm);
        return "redirect:/article/list";
    }

    // 게시글 수정 폼 요청, 수정할 게시글 id를 파라미터로 전달받아 해당 게시글을 조회하여 입력폼을 초기화 한다
//    @GetMapping("/edit")
//    public String getArticleEdit(@RequestParam("id") Long id, Model model) {
//        ArticleDto articleDto = articleService.findById(id);
//        var articleForm = ArticleForm.builder()
//                .id(id)
//                .title(articleDto.getTitle())
//                .description(articleDto.getDescription())
//                .build();
//        model.addAttribute("article", articleForm);
//        return "article-edit";
//    }

    // 모델 어트리뷰트로 ArticleForm을 전달 받으면 id 파라미터가 채워 전달되고 자동으로 모델 객체에 어트리뷰트로 포함된다
    @GetMapping("/edit")
    public String getArticleEdit(@ModelAttribute("article") ArticleForm articleForm) {
        ArticleDto articleDto = articleService.findById(articleForm.getId());
        articleForm.setId(articleDto.getId());
        articleForm.setTitle(articleDto.getTitle());
        articleForm.setDescription(articleDto.getDescription());
        return "article-edit";
    }

    @PostMapping("/edit")
    public String postArticleEdit(@Valid @ModelAttribute("article") ArticleForm articleForm,
                                  BindingResult bindingResult,
                                  @AuthenticationPrincipal MemberUser userDetails) throws BadRequestException {
        if (bindingResult.hasErrors()) {
            return "article-edit";
        }
        articleService.update(userDetails.getId(), articleForm);
        return "redirect:/article/content?id=" + articleForm.getId();
    }

    @GetMapping("/delete")
    public String getArticleDelete(@RequestParam("id") Long id) {
        articleService.delete(id);
        return "redirect:/article/list";
    }
}
