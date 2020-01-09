package cn.itsource.hrm.client.impl;

import cn.itsource.basic.util.AjaxResult;
import cn.itsource.basic.util.PageList;
import cn.itsource.hrm.client.CourseESClient;
import cn.itsource.hrm.document.CourseDocument;
import cn.itsource.hrm.document.CourseDocumentQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseESFallback implements CourseESClient {
    @Override
    public AjaxResult createIndexes(List<CourseDocument> courses) {
        return AjaxResult.me().setSuccess(false).setMessage("失败！");
    }

    @Override
    public AjaxResult deleteIndexes(List<CourseDocument> courses) {
        return AjaxResult.me().setSuccess(false).setMessage("失败！");
    }

    @Override
    public PageList<CourseDocument> searchIndexes(CourseDocumentQuery courseDocumentQuery) {
        return null;
    }
}
