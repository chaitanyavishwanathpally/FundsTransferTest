package com.rest.api.persistance;


import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate5.HibernateTemplate;

public abstract interface GenericDAO
{
  public abstract <T, ID extends Serializable> T findById(Class<T> paramClass, Class<ID> paramClass1, ID paramID, boolean paramBoolean);

  public abstract <T, ID extends Serializable> T load(Class<T> paramClass, Class<ID> paramClass1, ID paramID);

  public abstract <T, ID extends Serializable> T findById(Class<T> paramClass, Class<ID> paramClass1, ID paramID);

  public abstract <T> T findFirstByPropertyMatch(Class<T> paramClass, String paramString, Object paramObject);

  public abstract <T> T findFirstByPropertyMatch(Class<T> paramClass, Map<String, Object> paramMap);

  public abstract <T> List<T> findAllByPropertyMatch(Class<T> paramClass, String paramString, Object paramObject);

  public abstract <T> List<T> findAllByPropertyMatch(Class<T> paramClass, Map<String, Object> paramMap);

  public abstract <T> List<T> findAllByPropertyMatchAny(Class<T> paramClass, String paramString, Collection<Object> paramCollection);

  public abstract <T> List<T> findAll(Class<T> paramClass);

  public abstract <T> List<T> findAllByPropertiesLike(Class<T> paramClass, Map<String, Object> paramMap);

  public abstract <T> int countAllByPropertiesLike(Class<T> paramClass, Map<String, Object> paramMap);

  public abstract <T> List<T> findAllByPropertiesLikeWithPagination(Class<T> paramClass, Map<String, Object> paramMap, int paramInt1, int paramInt2);

  public abstract <T> List<T> findByExample(Class<T> paramClass, T paramT);

  public abstract <T> T save(T paramT);

  public abstract void flush();

  public abstract void clear();

  public abstract void clearCurrentSession();

  public abstract <T> Collection<T> saveAll(Collection<T> paramCollection);

  public abstract void delete(Object paramObject);

  public abstract void deleteAll(Collection<Object> paramCollection);

  public abstract <T> List<T> findAllByCriteria(Class<T> paramClass, List<GenericDAOQueryCriteria> paramList);

  public abstract <T> List<T> getNativeSQLQueryList(Class<T> paramClass, String paramString);

  public abstract <T> List<T> getNamedQueryList(Class<T> paramClass, String paramString, Map<String, Object> paramMap);

  public abstract void executeNoResultStoredProcedure(String paramString, Map<String, Object> paramMap);

  public abstract HibernateTemplate getHibernateTemplate();

  public abstract <T> List<T> executeSQLQueryList(Class<?> paramClass, String paramString);

  public abstract <T> List<T> executeSQLQueryList(Class<?> paramClass, String paramString, String[] paramArrayOfString);

  public abstract <T> List<T> executeHQLQueryList(Class<?> paramClass, String paramString);

  public abstract Object[] executeHQLQueryObjectPair(String paramString);

  public abstract <T> List<T> executeHQLQueryList(Class<?> paramClass, String paramString, Object[] paramArrayOfObject);

  public abstract Object[] executeHQLQueryObjectPair(String paramString, String[] paramArrayOfString);

  public abstract <T> Long getMaxByPropertyCriteria(Class<T> paramClass, String paramString, List<GenericDAOQueryCriteria> paramList);

  public <T> List<T> findAllByPropertyMatchJoinTable(Class<T> persistentClass, Map<String, Object> propertiesAndValues);
  public <T> List<T> findAllByCriteriaPagination ( final Class<T> persistentClass, List<GenericDAOQueryCriteria> Operatorandcriteriaobject,int maxResults, int firstResult);
}