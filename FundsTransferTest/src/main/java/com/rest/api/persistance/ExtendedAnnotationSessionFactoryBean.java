package com.rest.api.persistance;

import java.util.Set;

import org.hibernate.AnnotationException;
import org.hibernate.HibernateException;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.util.ClassUtils;



public class ExtendedAnnotationSessionFactoryBean extends LocalSessionFactoryBean
{
  private String[] baseAnnotatedPackages;
  private ClassLoader classLoader;

  public void setBaseAnnotatedPackages(String[] baseAnnotatedPackages)
  {
    this.baseAnnotatedPackages = ((String[])baseAnnotatedPackages.clone());
  }

  public void setBeanClassLoader(ClassLoader classLoader)
  {
    this.classLoader = classLoader;
  }

  protected void postProcessAnnotationConfiguration(AnnotationException config) throws HibernateException
  {
    for (String baseAnnotatedPackage : this.baseAnnotatedPackages) {
      ExtendedClassPathScanningCandidateComponentProvider scanner = 
        new ExtendedClassPathScanningCandidateComponentProvider(false);
      Set<BeanDefinition> annotatedClasses = scanner.findCandidateComponents(baseAnnotatedPackage);

      /*for (BeanDefinition bd : annotatedClasses)
        config.addAnnotatedClass(ClassUtils.resolveClassName(bd.getBeanClassName(), this.classLoader));*/
    }
  }
}