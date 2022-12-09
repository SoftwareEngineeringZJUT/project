package com.app.controller;

import com.alibaba.fastjson.JSON;
import com.app.dao.AdminDao;
import com.app.dao.ProductDao;
import com.app.domain.Admin;
import com.app.domain.Product;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

import static com.app.core.util.MyJSONUtil.addKeyValue;

@Controller
@RequestMapping("/admin")
public class AdminController {

    /**
     *      产品编排
     *
     *      总体数据统计（区分权限）
     *
     *
     * */


    @Resource
    private ProductDao productDao;
    @Resource
    private AdminDao adminDao;

    final Integer SUPER_ADMIN = 5;

    @PostMapping("/getProducts")
    public String getProducts(Admin _admin){
        String retJSON = "[]";

        Admin admin = adminDao.getAdminByAccount(_admin.getAccount());
        List<Product> productList ;

        if(admin == null){
            return retJSON;
        }

        System.out.println(admin);

        // 超级管理员
        if(admin.getRank() > SUPER_ADMIN){
            productList = productDao.getAll();
        }
        // 普通管理员
        else{
            productList = productDao.getProductByPublisher(admin.getAdmin_id());
        }

        retJSON = JSON.toJSONString(productList);

        return retJSON;
    }

    @PostMapping("/addProduct")
    public String addProduct(Admin _admin , Product product){
        String retJSON = "{}";
        Admin admin = adminDao.getAdminByAccount(_admin.getAccount());

        if(admin == null){
            retJSON = addKeyValue(retJSON , "status" , "ADMIN_NOT_FOUND");
            return retJSON;
        }



        return retJSON;
    }
}
