package cn.easybuy.controller.pre;

import cn.easybuy.entity.Order;
import cn.easybuy.entity.Product;
import cn.easybuy.entity.User;
import cn.easybuy.entity.UserAddress;
import cn.easybuy.service.order.CartService;
import cn.easybuy.service.order.OrderService;
import cn.easybuy.service.product.ProductCategoryService;
import cn.easybuy.service.product.ProductService;
import cn.easybuy.service.user.UserAddressService;
import cn.easybuy.util.*;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping(value = "/cart")
@Controller
public class CartController {
    @Resource
    private ProductService productService;
    @Resource
    private OrderService orderService;
    @Resource
    private ProductCategoryService productCategoryService;
    @Resource
    private CartService cartService;
    @Resource
    private UserAddressService userAddressService;

    /**
     * 添加到购物车
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add")
    public String add(HttpServletRequest request) throws Exception {
        ReturnResult result = new ReturnResult();
        String id = request.getParameter("entityId");
        String quantityStr = request.getParameter("quantity");
        Integer quantity = 1;
        if (!EmptyUtils.isEmpty(quantityStr))
            quantity = Integer.parseInt(quantityStr);
        //查询出商品
        Product product = productService.getProductById(Integer.valueOf(id));
        if(product.getStock()<quantity){
            result.returnFail("商品数量不足");
            return JSON.toJSONString(result);

        }
        //获取购物车
        ShoppingCart cart = getCartFromSession(request);
        //往购物车放置商品
        result=cart.addItem(product, quantity);
        if(result.getStatus()== Constants.ReturnResult.SUCCESS){
            cart.setSum((EmptyUtils.isEmpty(cart.getSum()) ? 0.0 : cart.getSum()) + (product.getPrice() * quantity * 1.0));
        }
        return JSON.toJSONString(result);
    }



    /**
     * 刷新购物车
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/refreshCart")
    public String refreshCart(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        ShoppingCart cart = getCartFromSession(request);
        cart = cartService.calculate(cart);
        session.setAttribute("cart", cart);//全部的商品
        return "/common/pre/searchBar";
    }

    /**
     * 跳到结算页面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/toSettlement")
    public String toSettlement(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<ProductCategoryVo> productCategoryVoList = productCategoryService.queryAllProductCategoryList();
        //封装返回
        request.setAttribute("productCategoryVoList", productCategoryVoList);
        return "/pre/settlement/toSettlement";
    }

    /**
     * 跳转到购物车页面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/settlement1")
    public String settlement1(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ShoppingCart cart = getCartFromSession(request);
        cart = cartService.calculate(cart);
        request.getSession().setAttribute("cart",cart);
        return "/pre/settlement/settlement1";
    }

    /**
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/settlement2")
    public String settlement2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = getUserFromSession(request);
        List<UserAddress> userAddressList = userAddressService.queryUserAdressList(user.getId());
        request.setAttribute("userAddressList", userAddressList);
        return "/pre/settlement/settlement2";
    }

    /**
     * 生成订单
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/settlement3")
    public String settlement3(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ShoppingCart cart = getCartFromSession(request);
        cart = cartService.calculate(cart);
        User user = getUserFromSession(request);
        ReturnResult result=checkCart(request);
        if(result.getStatus()==Constants.ReturnResult.FAIL){
//            return JSON.toJSONString(result);
        }
        //新增地址
        String addressId=request.getParameter("addressId");
        String newAddress=request.getParameter("newAddress");
        String newRemark=request.getParameter("newRemark");
        UserAddress userAddress=new UserAddress();
        if(addressId.equals("-1")){
            userAddress.setRemark(newRemark);
            userAddress.setAddress(newAddress);
            userAddress.setId(userAddressService.addUserAddress(user.getId(),newAddress,newRemark));
        }else{
            userAddress=userAddressService.getUserAddressById(Integer.parseInt(addressId));
        }

        //生成订单
        Order order = orderService.payShoppingCart(cart,user,userAddress.getAddress());
        clearCart(request);
        request.setAttribute("currentOrder", order);
        return "/pre/settlement/settlement3";
    }

    /**
     * 清空购物车
     *
     * @param request
     */
    @ResponseBody
    @RequestMapping(value = "/clearCart")
    public String clearCart(HttpServletRequest request) throws Exception {
        ReturnResult result = new ReturnResult();
        //结账后清空购物车
        request.getSession().removeAttribute("cart");
        result.returnSuccess(null);
        return JSON.toJSONString(result);
    }

    /**
     * 修改购物车信息
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/modCart")
    public String modCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReturnResult result = new ReturnResult();
        HttpSession session = request.getSession();
        String id = request.getParameter("entityId");
        String quantityStr = request.getParameter("quantity");
        ShoppingCart cart = getCartFromSession(request);
        Product product=productService.getProductById(Integer.valueOf(id));
        if(EmptyUtils.isNotEmpty(quantityStr)){
            if(Integer.parseInt(quantityStr)>product.getStock()){
                result.returnFail("商品数量不足");
                return JSON.toJSONString(result);
            }
        }
        cart = cartService.modifyShoppingCart(id, quantityStr, cart);
        session.setAttribute("cart", cart);//全部的商品
        result.returnSuccess();
        return JSON.toJSONString(result);
    }

    /**
     * 从session中获取购物车
     *
     * @param request
     * @return
     */
    private ShoppingCart getCartFromSession(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private ReturnResult checkCart(HttpServletRequest request) throws Exception {
        ReturnResult result = new ReturnResult();
        HttpSession session = request.getSession();
        ShoppingCart cart = getCartFromSession(request);
        cart = cartService.calculate(cart);
        for (ShoppingCartItem item : cart.getItems()) {
            Product product=productService.getProductById(item.getProduct().getId());
            if(product.getStock()<item.getQuantity()){
                return result.returnFail(product.getName()+"商品数量不足");
            }
        }
        return result.returnSuccess();
    }

    /**
     * @param request
     * @return
     */
    private User getUserFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginUser");
        return user;
    }
}
