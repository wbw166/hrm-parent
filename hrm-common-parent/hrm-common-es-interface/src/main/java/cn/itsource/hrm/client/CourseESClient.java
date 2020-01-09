package cn.itsource.hrm.client;

import cn.itsource.basic.util.AjaxResult;
import cn.itsource.basic.util.PageList;
import cn.itsource.hrm.client.impl.CourseESFallback;
import cn.itsource.hrm.document.CourseDocument;
import cn.itsource.hrm.document.CourseDocumentQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(path = "/es",value = "ES-SERVICE",fallback = CourseESFallback.class)
public interface CourseESClient {

    @PostMapping("/create")
    AjaxResult createIndexes(@RequestBody List<CourseDocument> courses);

    @PostMapping("/delete")
    AjaxResult deleteIndexes(@RequestBody List<CourseDocument> courses);

    @PostMapping("/searchIndexes")
    PageList<CourseDocument> searchIndexes(@RequestBody CourseDocumentQuery courseDocumentQuery);
}
