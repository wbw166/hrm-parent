package cn.itsource.hrm.service.impl;

import cn.itsource.hrm.domain.CourseType;
import cn.itsource.hrm.mapper.CourseTypeMapper;
import cn.itsource.hrm.service.ICourseTypeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 课程目录 服务实现类
 * </p>
 *
 * @author wbw
 * @since 2019-12-25
 */
@Service
public class CourseTypeServiceImpl extends ServiceImpl<CourseTypeMapper, CourseType> implements ICourseTypeService {

    @Override
    public List<CourseType> loadTreeData() {
        List<CourseType> courseTypes = getByParentId(0L);
        return courseTypes;
    }

    public List<CourseType> loadTreeDataLoop_2(){
        //创建集合 存放一级类型
        List<CourseType> firstLevelTypes = new ArrayList<>();
        //查询出所有类型
        List<CourseType> courseTypes = baseMapper.selectList(null);
        //创建一个map 将查询出来的集合里的数据存放到map中
        HashMap<Long, CourseType> map = new HashMap<>();
        for (CourseType courseType : courseTypes) {
            map.put(courseType.getId(), courseType);
        }

        for (CourseType courseType : courseTypes) {
            if (courseType.getPid().longValue() == 0L){
                firstLevelTypes.add(courseType);
            }else {
                //如果不是一级类型 就从map中取出父级类型
                CourseType parentType = map.get(courseType.getPid());
                if (parentType!=null){
                    parentType.getChildren().add(courseType);
                }
            }
        }
        return firstLevelTypes;
    }

    /**
     * 嵌套循环
     * @return
     */
    public List<CourseType> loadTreeDataLoop_1(){
        //创建集合 存放一级类型
        List<CourseType> firstLevelTypes = new ArrayList<>();
        //查询出所有类型
        List<CourseType> courseTypes = baseMapper.selectList(null);
        for (CourseType courseType : courseTypes) {
            //如果是一级类型 直接保存到集合
            if (courseType.getPid() == 0L){
                firstLevelTypes.add(courseType);
            }else {
                //如果不是一级类型 就找到父类型 放入父类型的children集合中
                for (CourseType parentType : courseTypes) {
                    if(courseType.getPid().longValue() == parentType.getId().longValue()){
                        parentType.getChildren().add(courseType);
                    }
                }
            }
        }

        return firstLevelTypes;

    }

    /**
     * 递归方式
     * @param pid
     * @return
     */
    public List<CourseType> getByParentId(Long pid){
        List<CourseType> children = baseMapper.selectList(new QueryWrapper<CourseType>()
                .eq("pid", pid));

        //递归的出口
        if(children==null&&children.size()==0){
            return null;
        }

        for (CourseType child : children) {
            List<CourseType> childs = getByParentId(child.getId());
            child.setChildren(childs);
        }
        return children;
    }
}
