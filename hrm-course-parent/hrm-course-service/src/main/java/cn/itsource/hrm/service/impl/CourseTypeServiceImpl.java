package cn.itsource.hrm.service.impl;

import cn.itsource.basic.util.StrUtils;
import cn.itsource.hrm.client.PageClient;
import cn.itsource.hrm.client.RedisClient;
import cn.itsource.hrm.domain.CourseType;
import cn.itsource.hrm.mapper.CourseTypeMapper;
import cn.itsource.hrm.service.ICourseTypeService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private PageClient pageClient;

    private final String COURSE_TYPE = "hrm:course_type:treeData";
    @Override
    public List<CourseType> loadTreeData() {
        //List<CourseType> courseTypes = getByParentId(0L);
        String courseTypeStr = redisClient.get(COURSE_TYPE);
        System.out.println(courseTypeStr);
        List<CourseType> list = null;
        //如果redis不存在，使用双重校验第一次redis不存在的时候查询数据库
        if(StringUtils.isEmpty(courseTypeStr)){
            //查询数据库，防止缓存穿透，大量请求同时查询数据库，同步代码块
            synchronized (CourseTypeServiceImpl.class){
                courseTypeStr = redisClient.get(COURSE_TYPE);
                if(StringUtils.isEmpty(courseTypeStr)){
                    //如果redis中不存在就查询数据库
                    list = loadTreeDataLoop_2();
                    //集合转换为json字符串
                    String jsonString = JSONObject.toJSONString(list);
                    redisClient.set(COURSE_TYPE, jsonString);
                    return list;
                }
            }
        }
        //如果redis里面存在 json字符串转换成集合直接返回
        list = JSONObject.parseArray(courseTypeStr, CourseType.class);
        return list;
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

    /**
     * 增删改数据同步到redis
     */
    public void synchronization(){
        List<CourseType> list = loadTreeDataLoop_2();
        String jsonString = JSONObject.toJSONString(list);
        redisClient.set(COURSE_TYPE, jsonString);
        //重新生成静态页面
        staticCourseIndex(2L);
    }
    @Override
    public boolean save(CourseType entity) {
        super.save(entity);
        synchronization();
        return true;
    }

    @Override
    public boolean removeById(Serializable id) {
        super.removeById(id);
        synchronization();
        return true;
    }

    @Override
    public boolean updateById(CourseType entity) {
        super.updateById(entity);
        synchronization();
        return true;
    }

    /**
     * 静态化课程主页
     * @param pageId
     */
    @Override
    public void staticCourseIndex(Long pageId) {
        //查询静态化需要的数据存放到redis中
        String dataKey = initData(pageId);
        pageClient.staticPage(dataKey, pageId);
    }

    /**
     * 页面静态化数据准备
     * @param pageId
     */
    private String initData(Long pageId) {
        List<CourseType> courseTypes = loadTreeDataLoop_2();
        String jsonString = JSONObject.toJSONString(courseTypes);
        String key = "page:" + pageId + ":courseTypes";
        redisClient.set(key, jsonString);

        return key;
    }

    @Override
    public List<Map<String, Object>> getCrumbs(Long courseTypeId) {
        CourseType courseType = baseMapper.selectById(courseTypeId);
        String path = courseType.getPath();
        //查询各个级别的类型
        path = path.replaceAll("\\.", ",");
        path = path.substring(1);
        List<Long> ids = StrUtils.splitStr2LongArr(path);
        ArrayList<Map<String,Object>> list = new ArrayList<>();
        //循环id封装数据
        for (Long id : ids) {
            HashMap<String, Object> map = new HashMap<>();
            //当前类型
            CourseType currentType = baseMapper.selectById(id);
            map.put("currentType", currentType);
            //同级别的类型
            List<CourseType> otherTypes = baseMapper.selectList(new QueryWrapper<CourseType>()
                    .eq("pid", currentType.getPid())
                    .ne("id", currentType.getId())
            );
            map.put("otherTypes", otherTypes);
            list.add(map);
        }
        return list;
    }
}
