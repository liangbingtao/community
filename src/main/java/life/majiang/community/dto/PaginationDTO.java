package life.majiang.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private List<Integer> pages = new ArrayList<>();    //这个列表展示的是当前分页标签所包含的页码
    private Integer totalPage;


    public void setPagination(Integer totalCount, Integer page, Integer size) {
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
        this.page = page; //将传进来的page赋值给类中的page


        /* 分页标签的展示逻辑 */
        pages.add(page);
        for (int i = 1; i <= 3; i++) {   //在当前页的前后最多各展示3页
            if (page - i > 0) {
                pages.add(0, page - i);  //往头部插入
            }
            if (page + i <= totalPage) {
                pages.add(page + i);       //往尾部插入
            }
        }

        /*是否展示上一页*/
        if (page == 1) {          //如果当前页面为第一页，则不展示上一页，否则展示上一页
            showPrevious = false;
        } else {
            showPrevious = true;
        }

        /*是否展示下一页*/
        if (page == totalPage) {      //如果当前页面为最后一页，则不展示下一页，否则展示下一页
            showNext = false;
        } else {
            showNext = true;
        }

        /*是否展示第一页*/
        if (pages.contains(1)) {
            showFirstPage = false;  //如果当前分页列表包含第一页，则不显示跳转回第一页的按钮
        } else {
            showFirstPage = true;  //否则展示跳转到第一页的按钮
        }

        /* 是否展示最后一页 */
        if (pages.contains(totalPage)) {
            showEndPage = false;    //如果当前分页列表包含最后一页，则不展示跳转到最后一页的按钮
        } else {
            showEndPage = true;     //否则展示跳转到最后一页的按钮
        }
    }
}
