package life.majiang.community.controller;

import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;


    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    /**
     * 将提问页面的参数传递给服务器，保存至数据库中
     * @param title
     * @param description
     * @param tag
     * @return
     */
    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request,
            Model model){


        /*将输入框中上次的写入的值显示出来*/
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        /* 判断提问界面中的各输入框内容是否为空*/
        if (title == null || title ==""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if (description == null || description ==""){
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }
        if (tag == null || tag ==""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }
        /* 验证是否登录，获取user信息*/
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){      //用户未登录，跳转回发布页面
            model.addAttribute("error","用户未登录,请登录后再次尝试");
            return "publish";
        }

        /* 将提问界面的title，description，tag传递到服务器并保存至数据库中*/
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.create(question);
        return "redirect:/";
    }
}
