package com.lsc.bootstore.service;

import com.lsc.bootstore.dao.SysRoleDao;
import com.lsc.bootstore.entity.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {
    @Autowired
    SysRoleDao sysRoleDao;

    public SysRole getRoleById(Long id){
        return sysRoleDao.findById(id).orElse(null);
    }

}
