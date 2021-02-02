package com.rest.api.hibernateConfiguration;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.rest.api.persistance.HibernateTemplateSpringGenericDAO;

@Configuration
@EnableTransactionManagement
public class HibernateConfiguration {

	@Value("${hibernate.db.driver}")
	private String DB_DRIVER;

	@Value("${hibernate.dialect}")
	private String HIBERNATE_DIALECT;
	
	@Value("${fm.db.username}")
	private String FM_DB_USERNAME;

	@Value("${fm.db.password}")
	private String FM_DB_PASSWORD;

	@Value("${fm.db.url}")
	private String FM_DB_URL;
	
	@Value("${hibernate.show_sql}")
	private String HIBERNATE_SHOW_SQL;
	
	@Value("${hibernate.hbm2ddl.auto}")
	private String HIBERNATE_HBM2DDL_AUTO;
	
	@Value("${hibernate.packagesToScan}")
	private String ENTITYMANAGER_PACKAGES_TO_SCAN;
	
	@Bean(name = "fmDataSource")
	public DataSource fmDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(DB_DRIVER);
		dataSource.setUrl(FM_DB_URL);
		dataSource.setUsername(FM_DB_USERNAME);
		dataSource.setPassword(FM_DB_PASSWORD);
		return dataSource;
	}
	

	@Bean(name = "fmSessionFactory")
	public LocalSessionFactoryBean fmSessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(fmDataSource());
		sessionFactory.setPackagesToScan(ENTITYMANAGER_PACKAGES_TO_SCAN);
		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.dialect", HIBERNATE_DIALECT);
		hibernateProperties.setProperty("hibernate.show_sql", HIBERNATE_SHOW_SQL);
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", HIBERNATE_HBM2DDL_AUTO);
		hibernateProperties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
		hibernateProperties.setProperty("hibernate.connection.release_mode", "auto");
		hibernateProperties.setProperty("hibernate.connection.isolation", "2");
		sessionFactory.setHibernateProperties(hibernateProperties);
		return sessionFactory;
	}

	@Bean(name = "fmTransactionManager")
	public PlatformTransactionManager fmTransactionManager(
			@Qualifier("fmSessionFactory") LocalSessionFactoryBean sessFactory) {

		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(fmSessionFactory().getObject());
		txManager.setDataSource(fmDataSource());
		return txManager;
	}

	@Bean(name = "fmEnterpriseHibernateTemplate")
	public HibernateTemplate fmEnterpriseHibernateTemplate() {
		HibernateTemplate hibernateTemplate = new HibernateTemplate();
		hibernateTemplate.setSessionFactory(fmSessionFactory().getObject());
		return hibernateTemplate;
	}

	@Bean(name = "fmGenericDao")
	public HibernateTemplateSpringGenericDAO fmGenericDao() {
		HibernateTemplateSpringGenericDAO fmTemplate = new HibernateTemplateSpringGenericDAO();
		fmTemplate.setHibernateTemplate(fmEnterpriseHibernateTemplate());
		System.out.println("postgres DB CONFIGURED");
		return fmTemplate;
	}
}
