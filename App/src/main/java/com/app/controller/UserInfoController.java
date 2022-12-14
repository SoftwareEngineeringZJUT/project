package com.app.controller;

import com.alibaba.fastjson.JSON;
import com.app.dao.AdminDao;
import com.app.dao.BankDao;
import com.app.dao.UserDao;
import com.app.domain.Admin;
import com.app.domain.Bank;
import com.app.domain.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static com.app.core.util.MyJSONUtil.addKeyValue;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    /**
     * 用户信息增删改查
     */

    @Resource
    private UserDao userDao;
    @Resource
    private BankDao bankDao;

    /**
     * 删除用户
     * @param  user
     * @return String
     */
    @PostMapping("/delUser")
    public String delUser(User user){

        String retJSON = "{}";

        user = userDao.getUserByAccount(user.getAccount());
        if(user == null){
            retJSON = addKeyValue(retJSON , "status" , "USER_NOT_FOUND");
            return retJSON;
        }
        userDao.deleteByUid(user.getUid());
        retJSON = addKeyValue(retJSON , "status" , "APPROVED");
        return retJSON;
    }

    /**
     * 获取所有用户
     * @return
     */
    @PostMapping("/getUser")
    public String getUser(){
        String retJSON = "[]";

        List<User> userList;
        userList = userDao.getAll();

        retJSON = JSON.toJSONString(userList);

        return retJSON;
    }

    /**
     * 更新用户
     * @param _user
     * @return
     */
    @PostMapping("/updateUser")
    public String updateUser(User _user){
        String retJSON = "{}";

        // 存在性检验
        User user = userDao.getUserByUid(_user.getUid());
        if(user == null){
            retJSON = addKeyValue(retJSON , "status" , "USER_NOT_FOUND");
            return retJSON;
        }

        // 更新账户信息
        String newaccount = _user.getAccount();
        user = userDao.getUserByAccount(newaccount);

//        System.out.println(_user);
//        System.out.println(user);

        if(user != null && !user.getUid().equals(_user.getUid())  && !user.getAccount().equals(_user.getAccount()) ){
            retJSON = addKeyValue(retJSON , "status" , "ACCOUNT_DUPLICATED");
            return retJSON;
        }

        String bankcard = _user.getBank_card();
        Bank bank = bankDao.getBankByBankCard(bankcard);
        if(bank == null){
            retJSON = addKeyValue(retJSON , "status" , "BANKCARD_NOT_FOUND");
            return retJSON;
        }

        _user.setBalance(bankDao.getBalanceByCard(bankcard));

        userDao.update(_user);
        retJSON = addKeyValue(retJSON , "status" , "APPROVED");
        return retJSON;
    }

}
