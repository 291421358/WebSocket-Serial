package com.laola.hello.laola;

import com.laola.hello.laola.entity.User;
import com.laola.hello.laola.mapper.UserMapper;
import com.laola.hello.laola.server.LaolaProjectParameters;
import com.laola.hello.laola.vo.ProjectListVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = LaolaApplication.class)
public class LaolaTest {
//    @Autowired
    private UserMapper userMapper;
//    @Test
    public void fTest(){
        List<User> users = userMapper.selectAll();
        for (User user:users
             ) {
            System.out.println(user.getName()+user.getChineseName());
        }
    }

//    @Autowired
    private LaolaProjectParameters laolaProjectParameters;

//    @Test
    public void projectListTest(){
        List<ProjectListVO> projectListVOS = laolaProjectParameters.projectList();
        for (ProjectListVO projectListVO:projectListVOS
        ) {
            System.out.println(projectListVO.getId()+"\t"+projectListVO.getName());
        }
    }

//    @Test
    public void onePojectTest() throws Exception {
        User user = laolaProjectParameters.onePoject(3);
        Field[] fields = user.getClass().getDeclaredFields();
        for (Field field:fields
        ) {
            // 获取属性的名字
            String name = field.getName();
            // 将属性的首字符大写，方便构造get，set方法
            String aname = name.substring(0, 1).toUpperCase() + name.substring(1);
            Method method = user.getClass().getMethod("get" + aname);
            System.out.println(name+"\t"+ method.invoke(user)
            );
        }


    }
//    @Tes/t
    public void update(){
        User user = new User();
        user.setId(3);
        user.setNormalHigh("10");
        user.setNormalLow("0");
        user.setMinAbsorbance("-2");
        user.setMaxAbsorbance("10");
        laolaProjectParameters.update(user);
    }
}
