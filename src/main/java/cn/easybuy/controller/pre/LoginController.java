package cn.easybuy.controller.pre;

import cn.easybuy.entity.User;
import cn.easybuy.service.user.UserService;
import cn.easybuy.util.EmptyUtils;
import cn.easybuy.util.ReturnResult;
import cn.easybuy.util.SecurityUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Resource
    private UserService userService;

    /**
     * 跳转到登陆界面
     * @return
     */
    @RequestMapping(value = "/toLogin")
    public String toLogin()throws Exception{
        return "pre/login";
    }
    /**
     * 登陆的方法
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/login")
    public String login(HttpSession session, String loginName, String password)throws Exception{
        ReturnResult result = new ReturnResult();
        User user=userService.getUser(null, loginName);
        if(EmptyUtils.isEmpty(user)){
            result.returnFail("用户不存在");
        }else{
            if(user.getPassword().equals(SecurityUtils.md5Hex(password))){
                //登陆成功
                session.setAttribute("loginUser", user);
                result.returnSuccess("登陆成功");
            }else{
                result.returnFail("密码错误");
            }
        }
        return JSON.toJSONString(result);
    }
    /**
     * 登陆的方法
     * @return
     */
    @RequestMapping(value = "/loginOut")
    public String loginOut(Model model, HttpSession session)throws Exception{
        try {
            User user=(User)session.getAttribute("loginUser");
            session.removeAttribute("loginUser");
            // 清除购物车
            session.removeAttribute("cart");
            session.removeAttribute("cart2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("message", "注销成功");
        return "/pre/login";
    }
}
