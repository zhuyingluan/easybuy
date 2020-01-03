package cn.easybuy.controller.pre;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NewsController {
    @RequestMapping(value = "/newsList")
    public String newsList()throws Exception{
        return "/pre/newsList";
    }
}
