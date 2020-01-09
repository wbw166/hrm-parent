package cn.itsource.hrm.service.impl;

import cn.itsource.hrm.domain.Systemdictionary;
import cn.itsource.hrm.domain.Systemdictionaryitem;
import cn.itsource.hrm.mapper.SystemdictionaryMapper;
import cn.itsource.hrm.mapper.SystemdictionaryitemMapper;
import cn.itsource.hrm.service.ISystemdictionaryitemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wbw
 * @since 2019-12-24
 */
@Service
public class SystemdictionaryitemServiceImpl extends ServiceImpl<SystemdictionaryitemMapper, Systemdictionaryitem> implements ISystemdictionaryitemService {
    @Autowired
    private SystemdictionaryMapper systemdictionaryMapper;
    @Override
    public List<Systemdictionaryitem> listSn(String sn) {
        Systemdictionary systemdictionary = systemdictionaryMapper.selectOne(new QueryWrapper<Systemdictionary>()
                .eq("sn", sn));
        List<Systemdictionaryitem> systemdictionaryitems = baseMapper.selectList(new QueryWrapper<Systemdictionaryitem>()
                .eq("parent_id", systemdictionary.getId()));
        return systemdictionaryitems;
    }
}
