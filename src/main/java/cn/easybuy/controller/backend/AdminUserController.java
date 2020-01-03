package cn.easybuy.controller.backend;

import cn.easybuy.entity.User;
import cn.easybuy.service.user.UserService;
import cn.easybuy.util.*;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping(path = "/admin/user")
@Controller
public class AdminUserController {
    @Resource
    private UserService userService;
    /**
     * 跳到用户主页
     *
     * @param request
     * @return
     */
    @RequestMapping(path = "/index")
    public String index(HttpServletRequest request) throws Exception {
        //获取登陆用户
        User user = (User) request.getSession().getAttribute("loginUser");
        request.setAttribute("user", user);
        request.setAttribute("menu", 2);
        return "/backend/user/userInfo";
    }

    /**
     * 订单的主页面
     * @return
     */
    @RequestMapping("/queryUserList")
    public String queryUserList(Model model, String currentPage, String pageSize) throws Exception {
        //获取当前页数
        int rowPerPage = EmptyUtils.isEmpty(pageSize) ? 10 : Integer.parseInt(pageSize);
        int currentPageInt = EmptyUtils.isEmpty(currentPage) ? 1 : Integer.parseInt(currentPage);
        int total = userService.count();
        Pager pager = new Pager(total, rowPerPage, currentPageInt);
        List<User> userList = userService.getUserList(currentPageInt, rowPerPage);
        pager.setUrl("admin/user/queryUserList");
        model.addAttribute("userList", userList);
        model.addAttribute("pager", pager);
        model.addAttribute("menu", 8);
        return "/backend/user/userList";
    }

    /**
     * 修改用户信息
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/toUpdateUser")
    public String toUpdateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        User user = userService.getUser(Integer.parseInt(id), null);
        request.setAttribute("user", user);
        return "/backend/user/toUpdateUser";
    }

    /**
     * 去添加用户
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(path = "/toAddUser")
    public String toAddUser(HttpServletRequest request) throws Exception {
        User user = new User();
        request.setAttribute("user", user);
        return "/backend/user/toUpdateUser";
    }
    /**
     * 更新用户
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/updateUser")
    public String updateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReturnResult result = new ReturnResult();
        String id = request.getParameter("id");
        User user = new User();
        String loginName = request.getParameter("loginName");
        String sex = request.getParameter("sex");
        User oldUser = userService.getUser(null, loginName);

        //判断用户
        if (EmptyUtils.isNotEmpty(oldUser) && (EmptyUtils.isEmpty(id) || oldUser.getId() != Integer.parseInt(id))) {
            result.returnFail("用户已经存在");
            return JSON.toJSONString(result);
        }
        user.setLoginName(loginName);
        user.setUserName(request.getParameter("userName"));
        user.setSex(EmptyUtils.isEmpty(sex) ? 1 : 0);
        if (EmptyUtils.isEmpty(id) || id.equals("0")) {
            user.setPassword(SecurityUtils.md5Hex(request.getParameter("password")));
        }
        user.setIdentityCode(request.getParameter("identityCode"));
        user.setEmail(request.getParameter("email"));
        user.setMobile(request.getParameter("mobile"));
        user.setType(Integer.parseInt(request.getParameter("type")));

        result=checkUser(user);
        //是否验证通过
        if(result.getStatus()== Constants.ReturnResult.SUCCESS){
            if (EmptyUtils.isEmpty(id) || id.equals("0")) {
                if(!userService.add(user)){
                    result.returnFail("增加失败！");
                    return JSON.toJSONString(result);
                }
            } else {
                user.setId(Integer.parseInt(id));
                if(!userService.update(user)){
                    result.returnFail("修改失败！");
                    return JSON.toJSONString(result);
                }
            }
        }
        result.returnSuccess();
        return JSON.toJSONString(result);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteUserById")
    public String deleteUserById(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReturnResult result = new ReturnResult();
        String id = request.getParameter("id");
        userService.deleteUserById(Integer.parseInt(id));
        result.returnSuccess();
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
