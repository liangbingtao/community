package life.majiang.community.controller;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value = "page",defaultValue = "1") Integer page, //从页面接收页码数，默认值为1
                        @RequestParam(value = "size",defaultValue = "5") Integer size  //从页面接收每页展示的问题数量，默认每页展示5条问题
                        ){
        //通过questionService中的list方法查询到所有的问题，并展示到前端页面上
        PaginationDTO pagination = questionService.list(page,size);
        model.addAttribute("pagination",pagination);
        return "index";
    }

    //退出登录
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        request.getSession().removeAttribute("user");   //移除session
        Cookie cookie = new Cookie("token", null);  //要删除cookie需要新建一个同名的cookie，并将value设置为null
        cookie.setMaxAge(0);    //立即删除型
        response.addCookie(cookie);//删除cookie
        return "redirect:/";
    }
}
