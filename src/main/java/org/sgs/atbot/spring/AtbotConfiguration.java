package org.sgs.atbot.spring;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.sgs.atbot.service.AuthService;
import org.sgs.atbot.service.RedditService;
import org.sgs.atbot.service.impl.OauthServiceImpl;
import org.sgs.atbot.service.impl.RedditServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.AuthenticationMethod;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;


@Configuration
@EnableTransactionManagement
@PropertySource(value = {"classpath:org/sgs/atbot/atbot.properties", "classpath:org/sgs/atbot/security.properties"})
public class AtbotConfiguration {
    private final Environment environment;


    @Autowired
    public AtbotConfiguration(Environment environment) {
        this.environment = environment;
    }


    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
        return dataSource;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws NamingException {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan(new String[]{"org.sgs.atbot.model", "org.sgs.atbot.service"});
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        factoryBean.setJpaProperties(jpaProperties());
        return factoryBean;
    }


    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }


    @Bean
    @Autowired
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(emf);
        return txManager;
    }


    @Bean
    public Credentials getCredentials() {
        return new Credentials(AuthenticationMethod.SCRIPT,
                environment.getRequiredProperty("reddit.username"),
                environment.getRequiredProperty("reddit.password"),
                environment.getRequiredProperty("reddit.clientId"),
                environment.getRequiredProperty("reddit.clientSecret"),
                null, //deviceId, not used by us
                environment.getRequiredProperty("reddit.redirectUrl"));
    }


    @Bean
    public AuthService getAuthService() {
        return new OauthServiceImpl(getCredentials());
    }


    @Bean
    public UserAgent getUserAgent() {
        return UserAgent.of("desktop", "org.sgs.atbot", "0.1.1", "ArchiveThisBot");
    }


    @Bean
    public RedditClient getRedditClient() {
        return new RedditClient(getUserAgent());
    }


    @Bean
    public List<String> getSubredditList() {
        List<String> subredditList = new ArrayList<>();
        subredditList.addAll(Arrays.asList(environment.getRequiredProperty("subreddit.list").split(",")));
        return subredditList;
    }


    @Bean
    public RedditService getRedditService() {
        return new RedditServiceImpl(getAuthService(), getRedditClient(), getSubredditList());
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        return properties;
    }

}