package cn.itsource.hrm.controller;

import cn.itsource.basic.util.AjaxResult;
import cn.itsource.basic.util.PageList;
import cn.itsource.hrm.document.CourseDocument;
import cn.itsource.hrm.document.CourseDocumentQuery;
import cn.itsource.hrm.repository.CourseRepository;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/es")
public class CourseESController {

    @Autowired
    private CourseRepository repository;

    @PostMapping("/create")
    public AjaxResult createIndexes(@RequestBody List<CourseDocument> courses){
        try {
            repository.saveAll(courses);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("失败!");
        }

    }

    @PostMapping("/delete")
    public AjaxResult deleteIndexes(@RequestBody List<CourseDocument> courses){
        try {
            repository.deleteAll(courses);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("失败!");
        }

    }

    @PostMapping("/searchIndexes")
    public PageList<CourseDocument> searchIndexes(@RequestBody CourseDocumentQuery courseDocumentQuery){
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        //构造查询条件
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if(StringUtils.isNotEmpty(courseDocumentQuery.getKeyword()))
            boolQueryBuilder.must(new MatchQueryBuilder("all",courseDocumentQuery.getKeyword()));
        List<QueryBuilder> filter = boolQueryBuilder.filter();
        if(courseDocumentQuery.getCourseTypeId()!=null)
            filter.add(new TermQueryBuilder("courseTypeId",courseDocumentQuery.getCourseTypeId()));
        //最高价格和最低价格
        if(courseDocumentQuery.getMaxPrice()!=null)
            filter.add(new RangeQueryBuilder("price").lte(courseDocumentQuery.getMaxPrice()));
        if(courseDocumentQuery.getMinPrice()!=null)
            filter.add(new RangeQueryBuilder("price").gte(courseDocumentQuery.getMinPrice()));
        //构建排序条件
        //排序的列
        String orderField = "startTime";
        if(StringUtils.isNotEmpty(courseDocumentQuery.getOrderFiled())){
            switch (courseDocumentQuery.getOrderFiled()){
                case "xp":
                    orderField = "startTime";
                    break;
                case  "jg":
                    orderField = "price";
                    break;
            }
        }
        //排序的方式
        SortOrder orderType = SortOrder.ASC;
        if(courseDocumentQuery.getOrderType()!=null){
            switch (courseDocumentQuery.getOrderType()){
                case 1:
                    orderType = SortOrder.DESC;
                    break;
                case 0:
                    orderType = SortOrder.ASC;
                    break;
            }
        }

        builder.withSort(new FieldSortBuilder(orderField).order(orderType));
        builder.withQuery(boolQueryBuilder);
        //分页
        builder.withPageable(PageRequest.of(courseDocumentQuery.getPage()-1, courseDocumentQuery.getRows()));
        Page<CourseDocument> page = repository.search(builder.build());
        return new PageList<>(page.getTotalElements(),page.getContent());
    }

}
