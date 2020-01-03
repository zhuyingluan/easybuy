package cn.easybuy.service.order;

import java.util.*;

import cn.easybuy.dao.order.*;
import cn.easybuy.dao.product.ProductDao;
import cn.easybuy.util.*;
import cn.easybuy.entity.Order;
import cn.easybuy.entity.OrderDetail;
import cn.easybuy.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderDetailDao orderDetailDao;
    @Resource
    private OrderDao orderDao;
    @Resource
    private ProductDao productDao;
    /**
     * 结算购物车物品包含以下步骤：
     * 1.生成订单
     * 2.生成订单明细
     * 3.更新商品表，减库存
     * 注意加入事物的控制
     */

    @Override
    public Order payShoppingCart(ShoppingCart cart, User user, String address) {
        // TODO Auto-generated method stub
        Order order = new Order();
        try {
            //增加订单
            order.setUserId(user.getId());
            order.setLoginName(user.getLoginName());
            order.setUserAddress(address);
            order.setCreateTime(new Date());
            order.setCost(cart.getTotalCost());
            order.setSerialNumber(StringUtils.randomUUID());
            orderDao.add(order);
            order = orderDao.getOrderBySerialNumber(order.getSerialNumber());
            //增加订单对应的明细信息
            for (ShoppingCartItem item : cart.getItems()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderId(order.getId());
                orderDetail.setCost(item.getCost());
                orderDetail.setProduct(item.getProduct());
                orderDetail.setProductId(item.getProduct().getId());
                orderDetail.setQuantity(item.getQuantity());
                orderDetailDao.add(orderDetail);
                //更新商品表的库存
                productDao.updateStock(item.getProduct().getId(), item.getQuantity());
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            order = null;
        }
        return order;
    }

    @Override
    public List<Order> getOrderList(Integer userId, Integer currentPageNo, Integer pageSize) {
        List<Order> orderList = new ArrayList<Order>();
        try {
            int total = count(userId);
            Pager pager = new Pager(total, pageSize, currentPageNo);
            orderList = orderDao.getOrderList(userId, pager.getStartIndex(), pager.getRowPerPage());
            for(Order order:orderList){
            	order.setOrderDetailList(orderDetailDao.getOrderDetailList(order.getId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return orderList;
        }
    }

    @Override
    public int count(Integer userId) {
        Integer count=0;
        try {
            count=orderDao.count(userId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return count;
        }
    }

    /**
     * 调用dao接口：OrderDetailMapper的方法实现
     */
    @Override
    public List<OrderDetail> getOrderDetailList(Integer orderId) {
        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
        try {
            orderDetailList = orderDetailDao.getOrderDetailList(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return orderDetailList;
        }
    }
}
