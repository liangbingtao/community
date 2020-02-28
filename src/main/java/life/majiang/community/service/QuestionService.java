package life.majiang.community.service;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
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

    /*展示在首页的问题列表*/
    public PaginationDTO list(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalPage;
        Integer totalCount = questionMapper.count();    //计算出问题总数
        /* 计算总页数 */
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        /* 容错处理*/
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        if (page == 0) {
            page = 1;
        }
        paginationDTO.setPagination(totalPage, page);//setPagination方法来计算页面的展示逻辑
        Integer offset = size * (page - 1); //偏移量和页码的关系
        List<Question> questions = questionMapper.list(offset, size);   //获得问题列表
        List<QuestionDTO> questionDTOList = new ArrayList<>(); //新建一个List用来存放返回的questionDTO
        for (Question question : questions) {               //遍历问题列表，并通过question表中的creator到User表中查找对应的user
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();    //QuestionDTO是将Question表中额外添加了一个User属性
            BeanUtils.copyProperties(question, questionDTO); //BeanUtils方法可以快速的将question中的属性拷贝到questionDTO对象中去
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    /*展示在profile中的问题列表*/
    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        Integer totalPage;
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.countByUserId(userId);    //计算出问题总数
        /* 计算总页数 */
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        /* 容错处理*/
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page);//setPagination方法来计算页面的展示逻辑
        Integer offset = size * (page - 1); //偏移量和页码的关系
        List<Question> questions = questionMapper.listByUserId(userId, offset, size);   //获得问题列表
        List<QuestionDTO> questionDTOList = new ArrayList<>(); //新建一个List用来存放返回的questionDTO
        for (Question question : questions) {               //遍历问题列表，并通过question表中的creator到User表中查找对应的user
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();    //QuestionDTO是将Question表中额外添加了一个User属性
            BeanUtils.copyProperties(question, questionDTO); //BeanUtils方法可以快速的将question中的属性拷贝到questionDTO对象中去
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;

    }

    /*问题详情页面*/
    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            //创建问题
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        } else {
            //更新问题
            question.setGmtModified(System.currentTimeMillis());
            int updated = questionMapper.update(question);
            if (updated != 1) {//问题更新成功返回1,更新失败返回0；
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Integer id) {
        Question question = questionMapper.getById(id);
        questionMapper.updateViewCount(question);

    }
}
