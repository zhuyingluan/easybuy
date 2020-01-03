package cn.easybuy.controller.pre;

import cn.easybuy.entity.Product;
import cn.easybuy.service.product.ProductCategoryService;
import cn.easybuy.service.product.ProductService;
import cn.easybuy.util.EmptyUtils;
import cn.easybuy.util.Pager;
import cn.easybuy.util.ProductCategoryVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RequestMapping(value = "/product")
@Controller
public class ProductController {
    @Resource
    private ProductService productService;
    @Resource
    private ProductCategoryService productCategoryService;

    /**
     * 查询商品列表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/queryProductList")
    public String queryProductList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String category = request.getParameter("category");
        String levelStr = request.getParameter("level");
        String currentPageStr = request.getParameter("currentPage");
        String keyWord = request.getParameter("keyWord");
        //获取页大小
        String pageSizeStr = request.getParameter("pageSize");
        int rowPerPage = EmptyUtils.isEmpty(pageSizeStr) ? 20:Integer.parseInt(pageSizeStr);
        int currentPage = EmptyUtils.isEmpty(currentPageStr) ? 1 : Integer.parseInt(currentPageStr);
        int  level=EmptyUtils.isNotEmpty(levelStr)?Integer.parseInt(levelStr):0;
        int total = productService.count(keyWord, EmptyUtils.isEmpty(category)?null:Integer.valueOf(category), level);
        Pager pager = new Pager(total, rowPerPage, currentPage);
        pager.setUrl("/Product?action=queryProductList&level="+level+"&category="+(EmptyUtils.isEmpty(category)?"":category));
        List<ProductCategoryVo> productCategoryVoList = productCategoryService.queryAllProductCategoryList();
        List<Product> productList = productService.getProductList(currentPage, rowPerPage, keyWord, EmptyUtils.isEmpty(category)?null:Integer.valueOf(category), level);
        request.setAttribute("productList", productList);
        request.setAttribute("pager", pager);
        request.setAttribute("total", total);
        request.setAttribute("keyWord", keyWord);
        request.setAttribute("productCategoryVoList", productCategoryVoList);
        return "/pre/product/queryProductList";
    }
    /**
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryProductDeatil")
    public String queryProductDeatil(HttpServletRequest request, String id) throws Exception{
        Product product = productService.getProductById(Integer.valueOf(id));
        List<ProductCategoryVo> productCategoryVoList = productCategoryService.queryAllProductCategoryList();
        request.setAttribute("product", product);
        request.setAttribute("productCategoryVoList", productCategoryVoList);
        addRecentProduct(request,product);
        return "/pre/product/productDeatil";
    }

    /**
     * 查询最近商品
     * @return
     */
    private List<Product> queryRecentProducts(HttpServletRequest request)throws Exception{
        HttpSession session=request.getSession();
        List<Product> recentProducts= (List<Product>) session.getAttribute("recentProducts");
        if(EmptyUtils.isEmpty(recentProducts)){
            recentProducts=new ArrayList<Product>();
        }
        return recentProducts;
    }
    /**
     * 添加最近浏览商品
     * @param request
     * @param product
     */
    private void addRecentProduct(HttpServletRequest request,Product product)throws Exception{
        HttpSession session=request.getSession();
        List<Product> recentProducts=queryRecentProducts(request);
        //判断是否满了
        if(recentProducts.size()>0 &&  recentProducts.size()==10){
            recentProducts.remove(0);
        }
        recentProducts.add(recentProducts.size(),product);
        session.setAttribute("recentProducts",recentProducts);
    }
}
