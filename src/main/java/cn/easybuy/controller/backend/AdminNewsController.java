package cn.easybuy.controller.backend;

import cn.easybuy.entity.News;
import cn.easybuy.param.NewsParams;
import cn.easybuy.service.news.NewsService;
import cn.easybuy.util.EmptyUtils;
import cn.easybuy.util.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping(value = "/admin/news")
@Controller
public class AdminNewsController {
    @Resource
    private NewsService newsService;

    /**
     * 查询新闻列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryNewsList")
    public String queryNewsList(HttpServletRequest request)throws Exception{
        //获取当前页数
        String currentPageStr = request.getParameter("currentPage");
        //获取页大小
        String pageSize = request.getParameter("pageSize");
        int rowPerPage = EmptyUtils.isEmpty(pageSize)?10:Integer.parseInt(pageSize);
        int currentPage = EmptyUtils.isEmpty(currentPageStr)?1:Integer.parseInt(currentPageStr);
        int total=newsService.queryNewsCount(new NewsParams());
        Pager pager=new Pager(total,rowPerPage,currentPage);
        pager.setUrl("/admin/news/queryNewsList");
        NewsParams params = new NewsParams();
        params.openPage((pager.getCurrentPage() - 1) * pager.getRowPerPage(),pager.getRowPerPage());
        List<News> newsList = newsService.queryNewsList(params);
        request.setAttribute("newsList", newsList);
        request.setAttribute("pager", pager);
        request.setAttribute("menu", 7);
        return "/backend/news/newsList";
    }
    /**
     * 查询新闻详情
     * @param request
     * @return
     */
    @RequestMapping(value = "/newsDeatil")
    public String newsDeatil(HttpServletRequest request, String id)throws Exception{
        News news=newsService.findNewsById(id);
        request.setAttribute("news",news);
        request.setAttribute("menu", 7);
        return "/backend/news/newsDetail";
    }
}
