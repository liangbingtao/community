package life.majiang.community.service;

import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * QuestionService将UserMapper和QuestionMapper组装起来，并且获得问题的创建者
 */

@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;

    public List<QuestionDTO> list() {
        List<Question> questions = questionMapper.list();   //获得问题列表
        List<QuestionDTO> questionDTOList = new ArrayList<>(); //新建一个List用来存放返回的questionDTO
        for (Question question : questions) {               //遍历问题列表，并通过question表中的creator到User表中查找对应的user
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();    //QuestionDTO是将Question表中额外添加了一个User属性
            BeanUtils.copyProperties(question,questionDTO); //BeanUtils方法可以快速的将question中的属性拷贝到questionDTO对象中去
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }
}
