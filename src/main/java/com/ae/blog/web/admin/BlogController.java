package com.ae.blog.web.admin;

import com.ae.blog.po.Blog;
import com.ae.blog.po.User;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    /**
     * 博客列表页面
     * @param pageable Spring Boot内置的Pageable对象
     * @param blog 博客查询对象
     * @param model 前端model
     * @return 返回博客列表页面
     */
    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return LIST;
    }

    /**
     * 博客搜索
     * @param pageable Spring Boot内置的Pageable对象
     * @param blog 博客查询对象
     * @param model 前端model
     * @return 返回博客列表片段
     */
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs :: blogList";
    }

    /**
     * 博客添加页面
     * @param model 前端model
     * @return 返回博客添加页面
     */
    @GetMapping("/blogs/input")
    public String input(Model model) {
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }

    /**
     * 提交博客
     * @param blog 前端传过来的博客对象
     * @param attributes 重定向页面属性对象
     * @param session HttpSession
     * @return 返回博客列表页面
     */
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()).get());
        blog.setTags(tagService.listTag(blog.getTagIds()));
        final Blog b;
        if (blog.getId() == null) {
            b =  blogService.saveBlog(blog);
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }

        if (b == null ) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return REDIRECT_LIST;
    }

    /**
     * 修改博客
     * @param id 博客Id
     * @param model 前端model
     * @return 返回修改博客页面
     */
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id).get();
        blog.init();
        model.addAttribute("blog",blog);
        return INPUT;
    }

    /**
     * 删除博客
     * @param id 博客Id
     * @param attributes 重定向页面属性对象
     * @return 返回博客列表页面
     */
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_LIST;
    }
}
