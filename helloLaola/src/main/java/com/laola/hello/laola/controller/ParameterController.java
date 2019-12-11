package com.laola.hello.laola.controller;


import com.laola.hello.laola.entity.User;
import com.laola.hello.laola.server.LaolaProjectParameters;
import com.laola.hello.laola.utils.DataUtils;
import com.laola.hello.laola.utils.SerialUtil;
import com.laola.hello.laola.vo.ProjectListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping(value = "parameter")
public class ParameterController {
    InputStream inputStream; // 从串口来的输入流
    static OutputStream outputStream;// 向串口输出的流
    @Autowired
    private LaolaProjectParameters laolaProjectParameters;


    /**
     * 初始化
     */
    @RequestMapping(value = "init" , method = RequestMethod.GET)
    public String init(){
        SerialUtil cRead = new SerialUtil();
        String init = cRead.init("E5 90 82 00 00 00 00 00 00 00 00 00 00 00 00 00");
        System.out.println(init);
        return "200";
    }

    /**
     * 项目列表
     * @return
     */
    @RequestMapping(value = "projectList" , method = RequestMethod.GET)
    public List<ProjectListVO> projectList(){
        List<ProjectListVO> projectListVOS = laolaProjectParameters.projectList();
        return projectListVOS;
    }

    /**
     * 单个项目查询
     * @param id
     * @return
     */
    @RequestMapping(value = "onePoject" , method = RequestMethod.GET)
    public User onePoject(Integer id){
        User user = laolaProjectParameters.onePoject(id);
        return user;
    }

    /**
     * 更新
     * @param user
     * @return
     */
    @RequestMapping(value = "update" , method = RequestMethod.GET)
    public String update(User user){
        laolaProjectParameters.update(user);
        return "200";
    }
}
