package cn.easybuy.controller.pre;

import cn.easybuy.entity.User;
import cn.easybuy.service.user.UserService;
import cn.easybuy.util.*;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class RegisterController {
    @Resource
    private UserService userService;

    /**
     * 跳到注册页面
     * @return
     */
    @RequestMapping(value = "/toRegister")
    public String toRegister() throws Exception {
        return "/pre/register";
    }

    /**
     * 保存用户信息
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/register")
    public String saveUserToDatabase(HttpServletRequest request) throws Exception {
        ReturnResult result = new ReturnResult();
        try {
            User user = new User();
            String loginName = request.getParameter("loginName");
            String sex = request.getParameter("sex");
            User oldUser = userService.getUser(null, loginName);
            //判断用户
            if (EmptyUtils.isNotEmpty(oldUser)) {
                result.returnFail("用户已经存在");
                return JSON.toJSONString(result);

            }
            user.setLoginName(request.getParameter("loginName"));
            user.setUserName(request.getParameter("userName"));
            user.setSex(EmptyUtils.isEmpty(sex) ? 1 : 0);
            user.setPassword(SecurityUtils.md5Hex(request.getParameter("password")));
            user.setIdentityCode(request.getParameter("identityCode"));
            user.setEmail(request.getParameter("email"));
            user.setMobile(request.getParameter("mobile"));
            user.setType(Constants.UserType.PRE);
            result=checkUser(user);
            //是否验证通过
            if(result.getStatus()==Constants.ReturnResult.SUCCESS){
                if(!userService.add(user)){
                    result.returnFail("注册失败！");
                    return JSON.toJSONString(result);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.returnSuccess("注册成功");
        return JSON.toJSONString(result);
    }

    private ReturnResult checkUser(User user){
        ReturnResult result = new ReturnResult();
        boolean flag=true;
        if(EmptyUtils.isNotEmpty(user.getMobile())){
            if(!RegUtils.checkMobile(user.getMobile())){
                return result.returnFail("手机格式不正确");
            }
        }

        if(EmptyUtils.isNotEmpty(user.getIdentityCode())){
            if(!RegUtils.checkIdentityCodeReg(user.getIdentityCode())){
                return result.returnFail("身份证号码不正确");
            }
        }

        if(EmptyUtils.isNotEmpty(user.getEmail())){
            if(!RegUtils.checkEmail(user.getEmail())){
                return result.returnFail("邮箱格式不正确");
            }
        }
        return result.returnSuccess();
    }
}
