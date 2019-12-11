package com.laola.hello.laola.server.impl;

import com.laola.hello.laola.entity.User;
import com.laola.hello.laola.mapper.UserMapper;
import com.laola.hello.laola.server.LaolaProjectParameters;
import com.laola.hello.laola.utils.DataUtils;
import com.laola.hello.laola.vo.ProjectListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class LaolaProjectParametersImpl implements LaolaProjectParameters {

    @Autowired
    private UserMapper userMapper;

    /**
     * 查询所有项目的id和项目名
     * @return List<ProjectListVO>
     */
    @Override
    public List<ProjectListVO> projectList() {
        List<User> users = userMapper.selectAll();
        List<ProjectListVO> projectList = new ArrayList<>();
        for (User user:users) {
            ProjectListVO projectListVO = new ProjectListVO();
            projectListVO.setId(user.getId());
            projectListVO.setName(user.getName());
            projectList.add(projectListVO);
        }
        return projectList;
    }

    /**
     * 根据项目id查询项目详情
     * @param id
     * @return User
     */
    @Override
    public User onePoject(int id) {
        User user = new User();
        user.setId(id);
        user = userMapper.selectOne(user);
        //浮点数退位
        carryDigit(user,"P");
        return user;
    }

    @Override
    public boolean update(User user) {

        carryDigit(user,"M");
        userMapper.updateByPrimaryKey(user);

        return true;
    }

    private User carryDigit(User user,String PorM){
        String minAbsorbance = user.getMinAbsorbance();
        String maxAbsorbance = user.getMaxAbsorbance();
        String normalLow = user.getNormalLow();
        String normalHigh = user.getNormalHigh();
        Float abs = 0F;
        Float nor = 0F;
        if (PorM.equals("P")){
            abs = 10F;
            nor= 100F;
        }
        if (PorM.equals("M")){
            abs = 0.1F;
            nor= 0.01F;
        }
        user.setMinAbsorbance(DataUtils.carryDigit(minAbsorbance,abs));
        user.setMaxAbsorbance(DataUtils.carryDigit(maxAbsorbance,abs));
        user.setNormalLow(DataUtils.carryDigit(normalLow,nor));
        user.setNormalHigh(DataUtils.carryDigit(normalHigh,nor));
        return user;
    }
}
