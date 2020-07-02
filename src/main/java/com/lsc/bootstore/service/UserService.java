package com.lsc.bootstore.service;

import com.lsc.bootstore.dao.UserDao;
import com.lsc.bootstore.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    @Transactional
    public User saveOrUpdateUser(User user) {
        return userDao.save(user);
    }

    @Transactional
    public User registerUser(User user) {
        return userDao.save(user);
    }

    @Transactional
    public void removeUser(Long id) {
        userDao.deleteById(id);
    }


    public User getUserById(Long id) {
        return userDao.findById(id).orElse(null);
    }


    public User getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }


    public User getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public Page<User> listUsersByNameLike(String name, Pageable pageable) {
        // 模糊查询
        name = "%" + name + "%";
        Page<User> users = userDao.findByUsernameLike(name, pageable);
        return users;
    }

    /**
     * //根据用户的账号来加载用户的一些认证信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("用户名不存在！");
        }
        //当前我们用户实现了UserDetails接口，可直接返回给 Spring Security 使用
        return user;
    }


}
