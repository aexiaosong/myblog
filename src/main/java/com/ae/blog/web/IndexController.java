package com.ae.blog.web;

import com.ae.blog.po.Tag;
import com.ae.blog.po.Type;
import com.ae.blog.service.BlogService;
import com.ae.blog.service.TagService;
import com.ae.blog.service.TypeService;
import com.ae.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {
        model.addAttribute("page",blogService.listBlog(pageable));
        List<Type> types = typeService.listTypeTop(6);
        model.addAttribute("types", types);
        List<Tag> tags = tagService.listTagTop(10);
        model.addAttribute("tags", tags);
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
        return "index";
    }

    @GetMapping("/blog")
    public String blog() {
        return "blog";
    }

}
