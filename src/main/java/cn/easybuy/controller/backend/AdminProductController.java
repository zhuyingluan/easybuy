package cn.easybuy.controller.backend;

import cn.easybuy.entity.Product;
import cn.easybuy.entity.ProductCategory;
import cn.easybuy.param.ProductCategoryParam;
import cn.easybuy.service.product.ProductCategoryService;
import cn.easybuy.service.product.ProductService;
import cn.easybuy.util.EmptyUtils;
import cn.easybuy.util.FileUpLoadUtil;
import cn.easybuy.util.Pager;
import cn.easybuy.util.ReturnResult;
import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RequestMapping(value = "/admin/product")
@Controller
public class AdminProductController {
    @Resource
    private ProductCategoryService productCategoryService;
    @Resource
    private ProductService productService;

    /**
     * 商品的主页面
     * @param request
     * @return
     */
    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request, String currentPage)throws Exception{
        //获取页大小
        String pageSize = request.getParameter("pageSize");
        int rowPerPage  = EmptyUtils.isEmpty(pageSize)?5:Integer.parseInt(pageSize);
        int currentPageInt = EmptyUtils.isEmpty(currentPage)?1:Integer.parseInt(currentPage);
        int total=productService.count(null, null, null);
        Pager pager=new Pager(total,rowPerPage,currentPageInt);
        pager.setUrl("admin/product/index");
        List<Product> productList=productService.getProductList(currentPageInt,rowPerPage, null, null, null);
        request.setAttribute("productList", productList);
        request.setAttribute("pager", pager);
        request.setAttribute("menu",5);
        return "/backend/product/productList";
    }
    /**
     * 添加商品
     * @return
     */
    @RequestMapping(value = "/toAddProduct")
    public String toAddProduct(HttpServletRequest request,HttpServletResponse response)throws Exception{
        request.setAttribute("menu",6);
        request.setAttribute("product",new Product());
        //查询一级分类
        ProductCategoryParam params =new ProductCategoryParam();
        params.setType(1);
        List<ProductCategory> productCategoryList=productCategoryService.queryProductCategoryList(params);
        //一级分类列表
        request.setAttribute("productCategoryList1", productCategoryList);
        return "/backend/product/toAddProduct";
    }

    /**
     * 添加商品
     * @throws Exception
     */
    @RequestMapping(value = "/addProduct")
    public String addProduct(HttpServletRequest request, Product product, @RequestParam(value = "photo", required = false) MultipartFile attach)throws Exception {
        String fileName = null;
        if (!attach.isEmpty()) {
            String originalFileName = attach.getOriginalFilename();
            String suffix = FilenameUtils.getExtension(originalFileName);
            if (attach.getSize()> FileUpLoadUtil.FILE_SIZE){
                return "/admin/product/addProduct";
            } else if (suffix.equalsIgnoreCase("jpg")
                    || suffix.equalsIgnoreCase("png")
                    || suffix.equalsIgnoreCase("jpeg")){
                fileName = FileUpLoadUtil.getFileName(suffix);
                String realPath = request.getServletContext().getRealPath("/files");
                File destinationDir = new File(realPath);
                destinationDir.mkdirs();
                if (!destinationDir.isDirectory()) {
                    System.err.println("不是目录");
                    throw new ServletException(realPath
                            + " is not a directory");
                }
                File targetFile = new File(destinationDir, fileName);
                if (!targetFile.exists()){
                    targetFile.mkdirs();
                }
                try {
                    attach.transferTo(targetFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "/admin/product/addProduct";
                }
            }else {
                return "/admin/product/addProduct";
            }
        }
        //获取商品参数
        product.setFileName(fileName);
        Integer id = product.getId();
        if (EmptyUtils.isEmpty(id) || id.equals("0")) {
            productService.add(product);
        } else {
            if(EmptyUtils.isEmpty(product.getFileName())|| product.getFileName().length()<5){
                Product productDemo=productService.getProductById(id);
                product.setFileName(productDemo.getFileName());
            }
            productService.update(product);
        }
        return "redirect:/admin/product/index";
    }
    /**
     * 修改商品
     * @return
     */
    @RequestMapping(value = "/toUpdateProduct")
    public String toUpdateProduct(HttpServletRequest request, String id)throws Exception{
        Product product=productService.getProductById(Integer.valueOf(id));
        request.setAttribute("menu",6);
        //查询一级分类
        ProductCategoryParam params =new ProductCategoryParam();
        params.setType(1);
        List<ProductCategory> productCategoryList1=productCategoryService.queryProductCategoryList(params);
        params.setType(2);
        List<ProductCategory> productCategoryList2=productCategoryService.queryProductCategoryList(params);
        params.setType(3);
        List<ProductCategory> productCategoryList3=productCategoryService.queryProductCategoryList(params);
        //一级分类列表
        request.setAttribute("productCategoryList1", productCategoryList1);
        request.setAttribute("productCategoryList2", productCategoryList2);
        request.setAttribute("productCategoryList3", productCategoryList3);
        request.setAttribute("product", product);
        return "/backend/product/toAddProduct";
    }
    /**
     * 根据id删除商品
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/deleteById")
    public String deleteById(String id)throws Exception{
        ReturnResult result=new ReturnResult();
        productService.deleteProductById(Integer.parseInt(id));
        result.returnSuccess();
        return JSON.toJSONString(result);
    }
}
