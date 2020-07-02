package com.lsc.bootstore.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();   // 使用 BCrypt 加密
    }

    /**
     * 认证信息管理
     * @param auth
     * @throws Exception
     * 认证信息是存储在数据库的，所以我们通过 UserDetailsService 把认证信息从数据库中取出来。
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**","/js/**","/js/*","/index").permitAll()  //都允许访问
                .antMatchers("/admins/**").hasRole("ADMIN")   //需要相应角色才能访问
                .and()
                .formLogin()    //基于Form表单的登录验证
                .loginPage("/login").failureUrl("/login-error")
                .and()
                .logout().logoutSuccessUrl("/index").permitAll();//定制注销行为,注销请求可任意访问
        http.csrf().disable();
    }
}
