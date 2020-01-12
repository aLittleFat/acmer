package cn.edu.scau.acm.acmer.config.security;

import cn.edu.scau.acm.acmer.filter.AnyRolesAuthorizationFilter;
import cn.edu.scau.acm.acmer.filter.JwtAuthFilter;
import cn.edu.scau.acm.acmer.service.UserService;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.Arrays;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean(SecurityManager securityManager, UserService userService) throws Exception{
        FilterRegistrationBean<Filter> filterRegistration = new FilterRegistrationBean<Filter>();
        filterRegistration.setFilter((Filter)shiroFilter(securityManager, userService).getObject());
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setAsyncSupported(true);
        filterRegistration.setEnabled(true);
        filterRegistration.setDispatcherTypes(javax.servlet.DispatcherType.REQUEST, DispatcherType.ASYNC);

        return filterRegistration;
    }

    @Bean
    public Authenticator authenticator(UserService userService) {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setRealms(Arrays.asList(jwtShiroRealm(userService), dbShiroRealm(userService)));
        authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        return authenticator;
    }

    @Bean
    protected SessionStorageEvaluator sessionStorageEvaluator(){
        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }

    @Bean("dbRealm")
    public Realm dbShiroRealm(UserService userService) {
        DbShiroRealm myShiroRealm = new DbShiroRealm(userService);
        return myShiroRealm;
    }

    @Bean("jwtRealm")
    public Realm jwtShiroRealm(UserService userService) {
        JWTShiroRealm myShiroRealm = new JWTShiroRealm(userService);
        return myShiroRealm;
    }

    /**
     * 设置过滤器
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, UserService userService) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filterMap = factoryBean.getFilters();
        filterMap.put("authcToken", createAuthFilter(userService));
        filterMap.put("anyRole", createRolesFilter());
        factoryBean.setFilters(filterMap);
        factoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition().getFilterChainMap());

        return factoryBean;
    }

    @Bean
    protected ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
//        chainDefinition.addPathDefinition("/auth/**", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/auth/logout", "noSessionCreation,authcToken[permissive]");
        chainDefinition.addPathDefinition("/image/**", "anon");
        chainDefinition.addPathDefinition("/api/admin/**", "noSessionCreation,authcToken,anyRole[admin]"); //只允许admin角色的用户访问
        chainDefinition.addPathDefinition("/api/student/**", "noSessionCreation,authcToken,anyRole[student]"); //只允许student角色的用户访问
        chainDefinition.addPathDefinition("/api/common/**", "noSessionCreation,authcToken");
        chainDefinition.addPathDefinition("/article/list", "noSessionCreation,authcToken");
        chainDefinition.addPathDefinition("/article/*", "noSessionCreation,authcToken[permissive]");
        chainDefinition.addPathDefinition("/**", "noSessionCreation,anon");

//        chainDefinition.addPathDefinition("/swagger-ui.html", "noSessionCreation,anon");
//        chainDefinition.addPathDefinition("/favicon.ico", "noSessionCreation,anon");
//        chainDefinition.addPathDefinition("/swagger-resources", "noSessionCreation,anon");
//        chainDefinition.addPathDefinition("/v2/api-docs", "noSessionCreation,anon");
//        chainDefinition.addPathDefinition("/webjars/springfox-swagger-ui/**", "noSessionCreation,anon");
//        chainDefinition.addPathDefinition("/configuration/security", "noSessionCreation,anon");
//        chainDefinition.addPathDefinition("/configuration/ui", "noSessionCreation,anon");
        return chainDefinition;
    }

    protected JwtAuthFilter createAuthFilter(UserService userService){
        return new JwtAuthFilter(userService);
    }

    protected AnyRolesAuthorizationFilter createRolesFilter(){
        return new AnyRolesAuthorizationFilter();
    }

}