package cn.edu.scau.acm.acmer.config.security;

import cn.edu.scau.acm.acmer.filter.AnyRolesAuthorizationFilter;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean(SecurityManager securityManager) throws Exception{
        FilterRegistrationBean<Filter> filterRegistration = new FilterRegistrationBean<Filter>();
        filterRegistration.setFilter((Filter)shiroFilter(securityManager).getObject());
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setAsyncSupported(true);
        filterRegistration.setEnabled(true);
        filterRegistration.setDispatcherTypes(javax.servlet.DispatcherType.REQUEST, DispatcherType.ASYNC);

        return filterRegistration;
    }

//    @Bean
//    public Authenticator authenticator(UserService userService) {
//        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
//        authenticator.setRealms(Arrays.asList(jwtShiroRealm(userService), dbShiroRealm(userService)));
//        authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
//        return authenticator;
//    }

//    @Bean
//    protected SessionStorageEvaluator sessionStorageEvaluator(){
//        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
//        sessionStorageEvaluator.setSessionStorageEnabled(false);
//        return sessionStorageEvaluator;
//    }

    @Bean
    public DefaultSecurityManager securityManager() {
        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
        defaultSecurityManager.setRealm(dbShiroRealm());
        defaultSecurityManager.setSessionManager(sessionManager());
        defaultSecurityManager.setCacheManager(cacheManager());
        return defaultSecurityManager;
    }

    @Bean("dbRealm")
    public Realm dbShiroRealm() {
        return new DbShiroRealm();
    }

    /**
     * 2.自定义sessionManager，用户的唯一标识，即Token或Authorization的认证
     */
    @Bean
    public SessionManager sessionManager() {
        MySessionManager mySessionManager = new MySessionManager();
        mySessionManager.setSessionDAO(redisSessionDAO());
        return mySessionManager;
    }

    /**
     * 配置shiro redisManager 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host + ":" + port);
//        redisManager.setPort(port);
//        redisManager.setExpire(1800);// 配置缓存过期时间
//        redisManager.setTimeout(timeout);
//        redisManager.setPassword(password);
        return redisManager;
    }

    /**
     * cacheManager 缓存 redis实现
     * <p>
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        // 自定义session管理 使用redis
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /**
     * 设置过滤器
     */
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);

        Map<String, Filter> filterMap = factoryBean.getFilters();
//        filterMap.put("authcToken", createAuthFilter(userService));
        filterMap.put("anyRole", createRolesFilter());
        factoryBean.setFilters(filterMap);
        Map<String, String> chainDefinition = new LinkedHashMap<String,String>();
        chainDefinition.put("/auth/login", "anon");
        chainDefinition.put("/auth/logout", "authc[permissive]");
        chainDefinition.put("/image/**", "anon");
        chainDefinition.put("/api/admin/**", "authc,anyRole[admin]"); //只允许admin角色的用户访问
        chainDefinition.put("/api/student/**", "authc,anyRole[student]"); //只允许student角色的用户访问
        chainDefinition.put("/api/common/**", "authc");
        chainDefinition.put("/api/unauth/**", "anon");
        chainDefinition.put("/article/list", "authc");
        chainDefinition.put("/article/*", "authc[permissive]");
        chainDefinition.put("/**", "anon");
        factoryBean.setFilterChainDefinitionMap(chainDefinition);

        factoryBean.setLoginUrl("http://localhost/auth/unauth");

        return factoryBean;
    }

//    protected JwtAuthFilter createAuthFilter(UserService userService){
//        return new JwtAuthFilter(userService);
//    }

    protected AnyRolesAuthorizationFilter createRolesFilter(){
        return new AnyRolesAuthorizationFilter();
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 注册全局异常处理
     * @return
     */
    @Bean(name = "exceptionHandler")
    public HandlerExceptionResolver handlerExceptionResolver() {
        return new MyExceptionHandler();
    }

}