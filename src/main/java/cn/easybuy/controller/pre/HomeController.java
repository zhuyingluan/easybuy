package cn.easybuy.controller.pre;

import cn.easybuy.entity.News;
import cn.easybuy.entity.Product;
import cn.easybuy.param.NewsParams;
import cn.easybuy.service.news.NewsService;
import cn.easybuy.service.product.ProductCategoryService;
import cn.easybuy.service.product.ProductService;
import cn.easybuy.util.Pager;
import cn.easybuy.util.ProductCategoryVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class HomeController {
    @Resource
    private ProductService productService;
    @Resource
    private NewsService newsService;
    @Resource
    private ProductCategoryService productCategoryService;

    /**
     * 商城主页的方法
     * @param request
     * @return
     */
    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request)throws Exception {
        //查询商品分裂
        List<ProductCategoryVo> productCategoryVoList = productCategoryService.queryAllProductCategoryList();
        Pager pager= new Pager(5, 5, 1);
        NewsParams params = new NewsParams();
        params.openPage((pager.getCurrentPage() - 1) * pager.getRowPerPage(),pager.getRowPerPage());
        List<News> news = newsService.queryNewsList(params);
        //查询一楼
        for (ProductCategoryVo vo : productCategoryVoList) {
            List<Product> productList = productService.getProductList(1, 8, null, vo.getProductCategory().getId(), 1);
            vo.setProductList(productList);
        }
        //封装返回
        request.setAttribute("productCategoryVoList", productCategoryVoList);
        request.setAttribute("news", news);
        return "/pre/index";
    }
}
