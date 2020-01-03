package cn.easybuy.controller.pre;

import cn.easybuy.service.product.ProductService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class ProductCategoryController {
    @Resource
    private ProductService productService;
}
