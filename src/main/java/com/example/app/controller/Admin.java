package com.example.app.controller;

import com.example.app.config.JwtUtil;
import com.example.app.config.Result;
import com.example.app.model.dto.LoginDTO;
import com.example.app.model.dto.ProjectDTO;
import com.example.app.model.entity.PriceEntity;
import com.example.app.model.entity.ProjectEntity;
import com.example.app.model.entity.QuoteEntity;
import com.example.app.model.entity.UserEntity;
import com.example.app.model.vo.UserVO;
import com.example.app.service.AdminImp;
import com.example.app.utils.UserLoginToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/BFBidding")
public class Admin {

    @Autowired
    AdminImp adminImp;

    /**
     * 登录
     *
     * @return
     */
    @PostMapping("/login")
    public Result<Map> Login(@RequestBody LoginDTO login, HttpServletRequest request) {

        // 获取存储中的验证码
        String sessionCode = (String) request.getSession().getAttribute("captcha");

        // 判断验证码是否相等
        if (!login.getCaptcha().toLowerCase().equals(sessionCode.toLowerCase())) {
            return Result.error("验证码输入错误");
        }

        UserEntity userInfo = adminImp.Login(login);

        // 设置密码为空
        userInfo.setPassword("******");

        // 判断是否查询不到信息
        if (userInfo == null) {
            return Result.error("用户名或密码输入错误");
        }

        Map<Object, Object> objectMap = new HashMap<>();
        objectMap.put("token", JwtUtil.sign(userInfo.getUsername(), userInfo.getId()));
        objectMap.put("userInfo", userInfo);
        return Result.success(objectMap);
    }

    // 获取用户信息
    @UserLoginToken
    @GetMapping("/getUserInfo")
    public Result<UserVO> getUserInfo() {
        UserVO userInfo = adminImp.getUserInfo();
        return Result.success(userInfo);
    }

    // 获取项目
    @UserLoginToken
    @GetMapping("/getProjectList")
    public Result<List<ProjectEntity>> getProjectList() {
        List<ProjectEntity> projectList = adminImp.getProjectList();
        return Result.success(projectList);
    }

    // 新增项目
    @UserLoginToken
    @PostMapping("/addProject")
    public Result<String> AddProject(@RequestBody ProjectDTO projectDTO) {
        adminImp.addProject(projectDTO);

        return Result.success("新增成功");
    }

    // 编辑项目
    @UserLoginToken
    @PostMapping("/editProject")
    public Result<String> editProject(@RequestBody ProjectDTO projectDTO) {

        adminImp.editProject(projectDTO);
        return Result.success("修改成功");
    }

    // 获取项目详情
    @UserLoginToken
    @GetMapping("/getProjectDetails")
    public Result<ProjectEntity> getProjectDetails(String projectId, String purchaseId, String supplierId) {

        ProjectEntity projectDetails;

        if (purchaseId != null) {
            projectDetails = adminImp.getProjectDetailsByPurchaseId(projectId, purchaseId);
        } else {
            projectDetails = adminImp.getProjectDetailsBySupplierId(projectId, supplierId);
        }
        return Result.success(projectDetails);
    }

    // 获取轮次列表数据
    @UserLoginToken
    @GetMapping("/getQuoteList")
    public Result<List<QuoteEntity>> getQuoteList(String projectId) {
        List<QuoteEntity> quoteList = adminImp.getQuoteList(projectId);
        return Result.success(quoteList);
    }

    // 报价
    @UserLoginToken
    @PostMapping("/setPrice")
    public Result<String> setPrice(@RequestBody PriceEntity priceEntity) {

        adminImp.setPrice(priceEntity);

        return Result.success("设置成功");
    }

    // 获取报价
    @UserLoginToken
    @GetMapping("/getPrice")
    public Result<PriceEntity> getPrice(String supplierId, Integer round) {
        PriceEntity price = adminImp.getPrice(supplierId, round);
        return Result.success(price);
    }
}
