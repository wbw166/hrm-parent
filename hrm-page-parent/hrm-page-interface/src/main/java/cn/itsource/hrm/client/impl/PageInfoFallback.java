package cn.itsource.hrm.client.impl;

import cn.itsource.hrm.client.PageInfoClient;
import cn.itsource.hrm.domain.PageInfo;
import org.springframework.stereotype.Component;

@Component
public class PageInfoFallback implements PageInfoClient {
    @Override
    public PageInfo get(Long id) {
        return null;
    }
}
