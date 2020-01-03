package cn.easybuy.controller.backend;

import cn.easybuy.entity.Order;
import cn.easybuy.entity.OrderDetail;
import cn.easybuy.service.order.OrderService;
import cn.easybuy.util.EmptyUtils;
import cn.easybuy.util.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping(value = "/admin/order")
@Controller
public class AdminOrderController {
    @Resource
    private OrderService orderService;

    /**
     * 订单的主页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request, HttpServletResponse response)throws Exception{
        //获取用户id
        String userId=request.getParameter("userId");
        //获取当前页数
        String currentPageStr = request.getParameter("currentPage");
        //获取页大小
        String pageSize = request.getParameter("pageSize");
        int rowPerPage  = EmptyUtils.isEmpty(pageSize)?5:Integer.parseInt(pageSize);
        int currentPage = EmptyUtils.isEmpty(currentPageStr)?1:Integer.parseInt(currentPageStr);
        int total=orderService.count(Integer.valueOf(userId));
        Pager pager=new Pager(total,rowPerPage,currentPage);
        pager.setUrl("/admin/order/index?userId="+userId);
        List<Order> orderList=orderService.getOrderList(Integer.valueOf(userId), currentPage, rowPerPage);
        request.setAttribute("orderList", orderList);
        request.setAttribute("pager", pager);
        request.setAttribute("menu", 1);
        return "/backend/order/orderList";
    }
    /**
     * 查询订单明细
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/queryOrderDeatil")
    public String queryOrderDeatil(HttpServletRequest request,HttpServletResponse response)throws Exception{
        String orderId=request.getParameter("orderId");
        List<OrderDetail> orderDetailList=orderService.getOrderDetailList(Integer.valueOf(orderId));
        request.setAttribute("orderDetailList", orderDetailList);
        request.setAttribute("menu", 1);
        return "/backend/order/orderDetailList";
    }

    @RequestMapping(value = "/queryAllOrder")
    public String queryAllOrder(HttpServletRequest request,HttpServletResponse response)throws Exception{
        //获取当前页数
        String currentPageStr = request.getParameter("currentPage");
        //获取页大小
        String pageSize = request.getParameter("pageSize");
        int rowPerPage  = EmptyUtils.isEmpty(pageSize)?5:Integer.parseInt(pageSize);
        int currentPage = EmptyUtils.isEmpty(currentPageStr)?1:Integer.parseInt(currentPageStr);
        int total=orderService.count(null);
        Pager pager=new Pager(total,rowPerPage,currentPage);
        pager.setUrl("/admin/order/queryAllOrder");
        List<Order> orderList=orderService.getOrderList(null, currentPage, rowPerPage);
        request.setAttribute("orderList", orderList);
        request.setAttribute("pager", pager);
        request.setAttribute("menu", 9);
        return "/backend/order/orderList";
    }
}
