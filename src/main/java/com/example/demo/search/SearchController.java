package com.example.demo.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class SearchController {
    private SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService){
        this.searchService=searchService;
    }

    //矩阵变量参数，如someurl/param;var1;var2;var3  匹配  Map<String>
    //如：someurl/param;var1=value1,value2;var2=value3,value4  匹配     Map<String,List<?>>
    //http://127.0.0.1:8080/search/mixed;keywords=xxxx,yyyy
    @RequestMapping("/search/{searchType}")
    public ModelAndView search(@PathVariable String searchType,@MatrixVariable List<String> keywords){
        List<String> tweets=searchService.search(searchType,keywords);
        ModelAndView modelAndView=new ModelAndView("resultPage");
        modelAndView.addObject("tweets",tweets);
        modelAndView.addObject("search",String.join(",",keywords));
        System.out.println("searchType参数值为："+searchType);
        System.out.println("keywords参数值为："+keywords);
        return modelAndView;
    }
}
