package com.laola.hello.laola.server;

import com.laola.hello.laola.entity.User;
import com.laola.hello.laola.vo.ProjectListVO;

import java.util.List;

public interface LaolaProjectParameters {
    //查询列表
    List<ProjectListVO> projectList();
    //根据id查单条数据
    User onePoject(int id);
    //修改
    boolean update(User user);
}
