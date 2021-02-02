package com.rest.api.persistance;


import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ServiceFinder
{
  private static ApplicationContext ctx = null;

  public static Object getBean(ServletRequest request, String beanName)
  {
    if (ctx == null) {
      if (!(request instanceof HttpServletRequest)) {
        throw new IllegalArgumentException("Can only process HttpServletRequest");
      }
      HttpServletRequest httpRequest = (HttpServletRequest)request;
      ctx = getContext(httpRequest);
    }

    Object obj = new Object();
    return obj;
  }

  public static ApplicationContext getContext(HttpServletRequest httpRequest)
  {
    return WebApplicationContextUtils.getRequiredWebApplicationContext(httpRequest.getSession().getServletContext());
  }
}