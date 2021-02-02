package com.rest.api.persistance ;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * HibernateTemplateSpring genericDAO class implements the various methods for
 * CRUD operations that are defined in GenericDAO.java interface.
 * 
 * @author TCS
 * @version 1.0
 */

@Transactional(propagation=Propagation.REQUIRED)
public class HibernateTemplateSpringGenericDAO implements GenericDAO
{
  //
  // Inner Classes:
  //
 
  
  HibernateTemplate hibernateTemplate ;

  /**
   * This is the utility method
   * 
   * @param crit
   * @return List
   */

  @SuppressWarnings ( "unchecked" )
  private <T> List<T> getTList ( Criteria crit )
  {
    return ( crit.list () ) ;
  }

 
  @SuppressWarnings("deprecation")
public void executeNoResultStoredProcedure ( String storedProcedureName, Map<String, Object> arguments )
  {
    StringBuilder sb = new StringBuilder ( "{call " ).append ( storedProcedureName ).append ( "(" ) ;
    boolean first = true ;
    for ( int i = 0 ; i < arguments.size () ; i++ )
    {
      if ( first )
      {
        sb.append ( "?" ) ;
        first = false ;
      }
      else
      {
        sb.append ( ", ?" ) ;
      }
    }
    sb.append ( ")}" ) ;

    CallableStatement stmt = null ;
    try
    {
      if ( !hibernateTemplate.getSessionFactory ().getCurrentSession ().isOpen () )
      {
        throw new RuntimeException ( "No Session open for executing the Stored Procedure " + storedProcedureName ) ;
      }
      //stmt = hibernateTemplate.getSessionFactory ().getCurrentSession ().doWork(new Work(){});;
      for ( Map.Entry<String, Object> e: arguments.entrySet () )
      {
        Object value = e.getValue () ;
        if ( value instanceof String )
        {
          stmt.setString ( e.getKey (), ( String ) e.getValue () ) ;

        }
        else if ( value instanceof Long )
        {
          stmt.setLong ( e.getKey (), ( Long ) e.getValue () ) ;

        }

      }

      stmt.execute () ;
    }
    catch ( HibernateException e1 )
    {
      throw new RuntimeException ( e1 ) ;
    }
    catch ( SQLException e1 )
    {
      throw new RuntimeException ( e1 ) ;
    }
    finally
    {
      if ( stmt != null )
      {
        try
        {
          stmt.close () ;
        }
        catch ( SQLException e1 )
        {

        }
      }
    }
  }


  /**
   * to get the NativeSQLQueryList for the given query string.
   * 
   * @param persistentClass
   *            a Class object
   * @param query
   *            a String object
   * @return List
   */
  @SuppressWarnings ( { "unchecked", "rawtypes" } )
  public <T> List<T> getNativeSQLQueryList ( final Class<T> persistentClass, final String query )
  {

    return ( List<T> ) getHibernateTemplate ().execute ( new NativeSQLQueryListCallBack ( persistentClass, query ) ) ;

  }


  /**
   * This is the DAO method to find a record with the given idclass and id.
   * 
   * @param persistentClass
   *            a Class object
   * @param idClass
   *            a Class object
   * @param id
   *            object of type ID
   * @param lock
   *            boolean type object
   * @return T
   * @throws HibernateException
   */
  @SuppressWarnings ( "unchecked" )
  public <T, ID extends Serializable> T findById ( Class<T> persistentClass, Class<ID> idClass, ID id, boolean lock ) throws HibernateException
  {
    T entity ;
    if ( !hibernateTemplate.getSessionFactory ().getCurrentSession ().isOpen () )
    {
      throw new RuntimeException ( "No Session open for getting the entity" ) ;
    }

    if ( lock )
    {
      entity = ( T ) getHibernateTemplate ().getSessionFactory ().getCurrentSession ().get ( persistentClass, id, LockMode.UPGRADE ) ;
    }
    else
    {
      entity = ( T ) getHibernateTemplate ().get ( persistentClass, id ) ;
    }

    return entity ;
  }

  /**
   * This is the DAO method to select particualr instance/record of the
   * persistent class with the given id.
   * 
   * @param persistentClass
   *            a Class object
   * @param idClass
   *            a Class object
   * @param id
   *            object of type ID
   * @return T
   */
  public <

  T, ID extends Serializable> T findById ( Class<T> persistentClass, Class<ID> idClass, ID id )
  {

    return findById ( persistentClass, idClass, id, false ) ;
  }


  /**
   * This is the DAO method to find all the records/instances for the given
   * persistent class.
   * 
   * @param persistentClass
   *            a Class object
   * @return List
   */
  @SuppressWarnings ( "unchecked" )
  public <T> List<T> findAll ( final Class<T> persistentClass )
  {

    return ( List<T> ) getHibernateTemplate ().execute ( new HibernateCallback ()
        {
          public Object doInHibernate ( final Session session )
          {
            Criteria criteria = session.createCriteria ( persistentClass ) ;

            return new ArrayList<T> ( criteria.list () ) ;
          }
        } ) ;
  }


  /**
   * This is the DAO method to select records for the given exmple instance.
   * 
   * @param persistentClass
   *            a Class object
   * @param exampleInstance
   *            an object of type T
   * @return List
   */
  public <T> List<T> findByExample ( Class<T> persistentClass, T exampleInstance )
  {
    return findByCriteria ( persistentClass, Example.create ( exampleInstance ) ) ;
  }


  /**
   * This is the DAO method to save the particular entity.
   * 
   * @param entity
   *            an object of type T
   * @return T
   */
  public <T> T save ( T entity )
  {
   getHibernateTemplate ().saveOrUpdate ( entity ) ;
    return entity ;
  }


  /**
   * This is the DAO method to save all the entities of a collection.
   * 
   * @param entities
   *            a collection object
   * @return Collection of type T
   */
  public <T> Collection<T> saveAll ( Collection<T> entities )
  {
    hibernateTemplate.saveOrUpdate ( entities ) ;
    return entities ;
  }


  /**
   * This is the DAO method that deletes the particular entity.
   * 
   * @param entity
   *            object
   */
  public void delete ( Object entity )
  {

    getHibernateTemplate ().delete ( entity ) ;
  }


  /**
   * This is the DAO method that deletes all the entities in a collection.
   * 
   * @param entities
   *            a collection object
   */
  public void deleteAll ( Collection<Object> entities )
  {
    getHibernateTemplate ().deleteAll ( entities ) ;
  }

  /**
   * This is the DAO method to find a record/instance matching the Property
   * value.
   * 
   * @param persistentClass
   *            a collection object
   * @param propertiesAndValues
   *            a map
   * 
   * @return T
   */
  public <T> T findFirstByPropertyMatch ( Class<T> persistentClass, Map<String, Object> propertiesAndValues )
  {
    List<T> all = findAllByPropertyMatch ( persistentClass, propertiesAndValues ) ;
    if ( all != null && all.size () > 0 )
    {
      // one or more found:
      return all.get ( 0 ) ;
    } // endif
    // none found:
    return null ;
  }

  

  /**
   * This is the DAO method to find all instances matching with Property
   * value.
   * 
   * @param persistentClass
   *            a Class object
   * @param propertiesAndValues
   *            map
   * @return List
   */
  public <

  T> List<T> findAllByPropertyMatch ( final Class<T> persistentClass, final Map<String, Object> propertiesAndValues )
  {
    Criteria crit = buildCriteriaAllByProperties ( persistentClass, propertiesAndValues, false ) ;
    List<T> hits = getTList ( crit ) ;
    if ( hits != null && hits.size () > 0 )
    {
      return hits ;

    } // endif
    return Collections.emptyList () ;
  }


  /**
   * This method builds the criteria for getting all like columns
   * 
   * @param persistentClass
   *            a Class object
   * @param propertiesAndValues
   *            map
   * @return Criteria
   */
  private <T> Criteria buildCriteriaAllByProperties ( final Class<T> persistentClass, Map<String, Object> propertiesAndValues, boolean useLike )
  {
    Iterator<Entry<String, Object>> itor = propertiesAndValues.entrySet ().iterator () ;
    Criteria crit = ( Criteria ) getHibernateTemplate ().execute ( new HibernateCallback ()
        {
          public Object doInHibernate ( final Session session )
          {
            return session.createCriteria ( persistentClass ) ;
          }
        } ) ;
    Map<Criteria, Criteria> critMap = new HashMap<Criteria, Criteria> () ;

    while ( itor.hasNext () )
    {
      Entry<String, Object> entry = itor.next () ;
      if ( entry.getValue () == null )
      {
        // Ignore nulls:
        itor.remove () ;
      }
      else
      {
        String key = ( String ) entry.getKey () ;
        int firstDot = key.indexOf ( "." ) ;
        if ( firstDot > 0 )
        {
          String joinEntity = null ;
          // If there is a dot notation do joins
          Criteria newCrit = null ;
          while ( firstDot > 0 )
          {
            joinEntity = key.substring ( 0, key.indexOf ( "." ) ) ;

            // /////////////////////////////////////////////////////////////////
            // There might be an issue if the alias is already
            // defined
            // /////////////////////////////////////////////////////////////////

            if ( newCrit == null )
            {
              newCrit = critMap.get ( crit ) ;
              if ( newCrit == null )
              {
                newCrit = crit.createCriteria ( joinEntity, joinEntity ) ;
                critMap.put ( crit, newCrit ) ;
              }
            }
            else
            {
              Criteria parentCrit = newCrit ;
              newCrit = critMap.get ( parentCrit ) ;
              if ( newCrit == null )
              {
                newCrit = parentCrit.createCriteria ( joinEntity, joinEntity ) ;
                critMap.put ( parentCrit, newCrit ) ;
              }
            }
            key = key.substring ( firstDot + 1 ) ;
            firstDot = key.indexOf ( "." ) ;
          }
          if ( useLike && entry.getValue () instanceof String )
          {
            String s = ( ( String ) entry.getValue () ).concat ( "%" ) ;
            newCrit.add ( Restrictions.ilike ( joinEntity + "." + key, s ) ) ;
          }
          else
          {
            newCrit.add ( Restrictions.eq ( joinEntity + "." + key, entry.getValue () ) ) ;
          }

        }
        else
        {
          if ( useLike && entry.getValue () instanceof String )
          {
            String s = ( ( String ) entry.getValue () ).concat ( "%" ) ;
            crit.add ( Restrictions.ilike ( entry.getKey (), s ) ) ;
          }
          else
          {
            crit.add ( Restrictions.eq ( entry.getKey (), entry.getValue () ) ) ;
          }
        }
      }
    } // endwhile
    return crit ;
  }

  /**
   * Find all Properties using 'like' criteria for String objects that are
   * starting with the value or 'equals' criteria for non-string objects that
   * are equal. If a Property value is null then it is ignored.
   * 
   * @param persistentClass
   *            a Class object
   * @param propertiesAndValues
   *            a map
   * @return List
   */
  public <T> List<T> findAllByPropertiesLike ( Class<T> persistentClass, Map<String, Object> propertiesAndValues )
  {
    Criteria crit = buildCriteriaAllByProperties ( persistentClass, propertiesAndValues, true ) ;
    List<T> hits = getTList ( crit ) ;
    if ( hits != null && hits.size () > 0 )
    {
      return hits ;
    } // endif
    return Collections.emptyList () ;

  }

  /**
   * Find all Properties using 'like' criteria for String objects that are
   * starting with the value or 'equals' criteria for non-string objects that
   * are equal. If a Property value is null then it is ignored.
   * 
   * @param persistentClass
   *            a Class object
   * @param propertiesAndValues
   *            map
   * @param maxResults
   *            an int specifying maxium results
   * @param firstResult
   *            an int specifying the first result
   * @return List
   */
  public <T> List<T> findAllByPropertiesLikeWithPagination ( Class<T> persistentClass, Map<String, Object> propertiesAndValues, int maxResults, int firstResult )
  {
    Criteria crit = buildCriteriaAllByProperties ( persistentClass, propertiesAndValues, true ) ;
    crit.setFirstResult ( firstResult ) ;
    crit.setMaxResults ( maxResults ) ;
    List<T> hits = getTList ( crit ) ;
    if ( hits != null && hits.size () > 0 )
    {
      return hits ;
    } // endif
    return Collections.emptyList () ;

  }

  /**
   * Find all Properties using 'like' criteria for String objects that are
   * starting with the value or 'equals' criteria for non-string objects that
   * are equal. If a Property value is null then it is ignored.
   * 
   * @param persistentClass
   *            a Class object
   * @param propertiesAndValues
   *            a map
   * @return int the count of all like properties
   */
  @SuppressWarnings ( "unchecked" )
  public <T> int countAllByPropertiesLike ( final Class<T> persistentClass, final Map<String, Object> propertiesAndValues )
  {

    Criteria crit = buildCriteriaAllByProperties ( persistentClass, propertiesAndValues, true ) ;
    crit.setProjection ( Projections.rowCount () ) ;
    List<T> results = crit.list () ;
    return ( ( Integer ) results.get ( 0 ) ).intValue () ;

  }

  /**
   * Use this inside subclasses as a convenience method.
   * 
   * @param persistentClass
   *            a Class object
   * @param criterion
   *            an array of Criterion objects
   * @return a List object
   */
  protected <T> List<T> findByCriteria ( final Class<T> persistentClass, Criterion... criterion )
  {

    Criteria crit = ( Criteria ) getHibernateTemplate ().execute ( new HibernateCallback ()
        {
          public Object doInHibernate ( final Session session )
          {
            return session.createCriteria ( persistentClass ) ;
          }
        } ) ;
    for ( Criterion c: criterion )
    {
      crit.add ( c ) ;
    } // endfor
    return getTList ( crit ) ;
  }

  /**
   * Find first Properties that matches the criteria with the object value.
   * 
   * @param persistentClass
   *            a Class object
   * @param propertyName
   *            a String
   * @param value
   *            an Object
   * @return T
   */
  public <T> T findFirstByPropertyMatch ( Class<T> persistentClass, String propertyName, Object value )
  {
    return findFirstByPropertyMatch ( persistentClass, Collections.singletonMap ( propertyName, value ) ) ;
  }

  /**
   * Find all Properties that matches the criteria with the object value.
   * 
   * @param persistentClass
   *            a Class object
   * @param propertyName
   *            a String
   * @param value
   *            an Object
   * @return T
   */
  public <T> List<T> findAllByPropertyMatch ( Class<T> persistentClass, String propertyName, Object value )
  {
    return findAllByPropertyMatch ( persistentClass, Collections.singletonMap ( propertyName, value ) ) ;
  }

  /**
   * Find all Properties that matches the criteria with the any object value
   * in the collection.
   * 
   * @param persistentClass
   *            a Class object
   * @param propertyName
   *            a String
   * @param values
   *            a Collection object
   * @return T
   */
  public <T> List<T> findAllByPropertyMatchAny ( final Class<T> persistentClass, String propertyName, Collection<Object> values )
  {
    Criteria crit = ( Criteria ) getHibernateTemplate ().execute ( new HibernateCallback ()
        {
          public Object doInHibernate ( final Session session )
          {
            return session.createCriteria ( persistentClass ) ;
          }
        } ) ;

    crit.add ( Restrictions.in ( propertyName, values ) ) ;
    List<T> hits = getTList ( crit ) ;

    if ( hits != null )
    {
      return hits ;
    } // endif
    return Collections.emptyList () ;
  }

  /**
   * Find all Properties that matches the genericDAOquerycriteria.
   * 
   * @param persistentClass
   *            a Class object
   * @param genericDaoQueryCriterion
   *            a List object
   * @return List
   */
  public <T> List<T> findAllByCriteriaPagination ( final Class<T> persistentClass, List<GenericDAOQueryCriteria> Operatorandcriteriaobject,int maxResults, int firstResult)
  {

    if ( Operatorandcriteriaobject == null || Operatorandcriteriaobject.isEmpty () )
    {
      return Collections.emptyList () ;
    }

    Criteria crit = ( Criteria ) getHibernateTemplate ().execute ( new HibernateCallback ()
        {
          public Object doInHibernate ( final Session session )
          {
            return session.createCriteria ( persistentClass ) ;
          }
        } ) ;
    crit.setFirstResult ( firstResult ) ;
    crit.setMaxResults ( maxResults ) ;
    buildCriteria ( crit, Operatorandcriteriaobject ) ;
   
    final List<T> hits = getTList ( crit ) ;
    if ( hits != null && hits.size () > 0 )
    {
      return hits ;
    } // endif
    return Collections.emptyList () ;

  }
  
  public <T> List<T> findAllByCriteria ( final Class<T> persistentClass, List<GenericDAOQueryCriteria> Operatorandcriteriaobject )
  {

    if ( Operatorandcriteriaobject == null || Operatorandcriteriaobject.isEmpty () )
    {
      return Collections.emptyList () ;
    }

    Criteria crit = ( Criteria ) getHibernateTemplate ().execute ( new HibernateCallback ()
        {
          public Object doInHibernate ( final Session session )
          {
            return session.createCriteria ( persistentClass ) ;
          }
        } ) ;

    buildCriteria ( crit, Operatorandcriteriaobject ) ;

    final List<T> hits = getTList ( crit ) ;
    if ( hits != null && hits.size () > 0 )
    {
      return hits ;
    } // endif
    return Collections.emptyList () ;

  }

  /**
   * @param crit
   *            a Criteria object
   * @param genericDaoQueryCriterion
   *            a List
   */
  private static void buildCriteria ( Criteria crit, List<GenericDAOQueryCriteria> genericDaoQueryCriterion )
  {

    if ( genericDaoQueryCriterion == null || genericDaoQueryCriterion.isEmpty () )
    {
      return ;
    }

    for ( GenericDAOQueryCriteria genericDaoQueryCriteria: genericDaoQueryCriterion )
    {
      if ( GenericDAOQueryCriteria.CriteriaOperator.EQUALS.equals ( genericDaoQueryCriteria.getCriteriaOperator () ) )
      {
        crit.add ( Restrictions.eq ( genericDaoQueryCriteria.getPropertyName (), genericDaoQueryCriteria.getPropertyValue () ) ) ;
      }
      else if ( GenericDAOQueryCriteria.CriteriaOperator.LIKE_CASE_INSENSITIVE.equals ( genericDaoQueryCriteria.getCriteriaOperator () ) )
      {
         crit.add ( Restrictions.ilike ( genericDaoQueryCriteria.getPropertyName (), genericDaoQueryCriteria.getPropertyValue () ) ) ;
      }
      else if ( GenericDAOQueryCriteria.CriteriaOperator.LIKE_BEGINS_WITH.equals ( genericDaoQueryCriteria.getCriteriaOperator () ) )
      {
        final String s = ( ( String ) genericDaoQueryCriteria.getPropertyValue () ) + "%" ;
        crit.add ( Restrictions.ilike ( genericDaoQueryCriteria.getPropertyName (), s ) ) ;
      }
      else if ( GenericDAOQueryCriteria.CriteriaOperator.LIKE_IN_BETWEEN.equals ( genericDaoQueryCriteria.getCriteriaOperator () ) )
      {
        final String s = "%" + ( ( String ) genericDaoQueryCriteria.getPropertyValue () ) + "%" ;
        crit.add ( Restrictions.ilike ( genericDaoQueryCriteria.getPropertyName (), s ) ) ;
      }
      else if ( GenericDAOQueryCriteria.CriteriaOperator.LIKE_ENDS_WITH.equals ( genericDaoQueryCriteria.getCriteriaOperator () ) )
      {
        final String s = "%" + ( ( String ) genericDaoQueryCriteria.getPropertyValue () ) ;
        crit.add ( Restrictions.ilike ( genericDaoQueryCriteria.getPropertyName (), s ) ) ;
      }
      else if ( GenericDAOQueryCriteria.CriteriaOperator.IN.equals ( genericDaoQueryCriteria.getCriteriaOperator () ) )
      {
        final List<Object> colValues = new ArrayList<Object> ( ( List<?> ) genericDaoQueryCriteria.getPropertyValue () ) ;
        crit.add ( Restrictions.in ( genericDaoQueryCriteria.getPropertyName (), colValues ) ) ;
      }
      else if ( GenericDAOQueryCriteria.CriteriaOperator.NOT_IN.equals ( genericDaoQueryCriteria.getCriteriaOperator () ) )
      {
        final List<Object> colValues = new ArrayList<Object> ( ( List<?> ) genericDaoQueryCriteria.getPropertyValue () ) ;
        crit.add ( Restrictions.not ( Restrictions.in ( genericDaoQueryCriteria.getPropertyName (), colValues ) ) ) ;
      }
      else if ( GenericDAOQueryCriteria.CriteriaOperator.LE.equals ( genericDaoQueryCriteria.getCriteriaOperator () ) )
      {
        crit.add ( Restrictions.le ( genericDaoQueryCriteria.getPropertyName (), genericDaoQueryCriteria.getPropertyValue () ) ) ;
      }
      else if ( GenericDAOQueryCriteria.CriteriaOperator.GE.equals ( genericDaoQueryCriteria.getCriteriaOperator () ) )
      {
        crit.add ( Restrictions.ge ( genericDaoQueryCriteria.getPropertyName (), genericDaoQueryCriteria.getPropertyValue () ) ) ;
      }
      else if ( GenericDAOQueryCriteria.CriteriaOperator.LT.equals ( genericDaoQueryCriteria.getCriteriaOperator () ) )
      {
        crit.add ( Restrictions.lt ( genericDaoQueryCriteria.getPropertyName (), genericDaoQueryCriteria.getPropertyValue () ) ) ;
      }
      else if ( GenericDAOQueryCriteria.CriteriaOperator.GT.equals ( genericDaoQueryCriteria.getCriteriaOperator () ) )
      {
        crit.add ( Restrictions.gt ( genericDaoQueryCriteria.getPropertyName (), genericDaoQueryCriteria.getPropertyValue () ) ) ;
      }
      else if ( GenericDAOQueryCriteria.CriteriaOperator.NOT_EQUALS.equals ( genericDaoQueryCriteria.getCriteriaOperator () ) )
      {
        crit.add ( Restrictions.ne ( genericDaoQueryCriteria.getPropertyName (), genericDaoQueryCriteria.getPropertyValue () ) ) ;
      }
      else if ( GenericDAOQueryCriteria.CriteriaOperator.IS_EMPTY.equals ( genericDaoQueryCriteria.getCriteriaOperator () ) )
      {
        crit.add ( Restrictions.isEmpty ( genericDaoQueryCriteria.getPropertyName () ) ) ;
      }
      else if ( GenericDAOQueryCriteria.CriteriaOperator.IS_NOT_EMPTY.equals ( genericDaoQueryCriteria.getCriteriaOperator () ) )
      {
        crit.add ( Restrictions.isNotEmpty ( genericDaoQueryCriteria.getPropertyName () ) ) ;
      }
      else if ( GenericDAOQueryCriteria.CriteriaOperator.IS_NULL.equals ( genericDaoQueryCriteria.getCriteriaOperator () ) )
      {
        crit.add ( Restrictions.isNull ( genericDaoQueryCriteria.getPropertyName () ) ) ;
      }
      else if ( GenericDAOQueryCriteria.CriteriaOperator.IS_NOT_NULL.equals ( genericDaoQueryCriteria.getCriteriaOperator () ) )
      {
        crit.add ( Restrictions.isNotNull ( genericDaoQueryCriteria.getPropertyName () ) ) ;
      }
      if ( GenericDAOQueryCriteria.CriteriaOperator.ASC.equals ( genericDaoQueryCriteria.getCriteriaOperator () ) )
      {
        crit.addOrder ( Order.asc ( genericDaoQueryCriteria.getPropertyName () ) ) ;
      }
      else if ( GenericDAOQueryCriteria.CriteriaOperator.DESC.equals ( genericDaoQueryCriteria.getCriteriaOperator () ) )
      {
        crit.addOrder ( Order.desc ( genericDaoQueryCriteria.getPropertyName () ) ) ;
      }
      else if ( GenericDAOQueryCriteria.CriteriaOperator.BETWEEN.equals ( genericDaoQueryCriteria.getCriteriaOperator () ) )
      {
    	  crit.add ( Restrictions.between ( genericDaoQueryCriteria.getPropertyName (), genericDaoQueryCriteria.getPropertyValue (),genericDaoQueryCriteria.getPropertyValue1 () ) ) ;
      }
    }
  } // endForLoop

  /**
   * This method flushes the session.
   */
  public void flush ()
  {
    getHibernateTemplate ().flush () ;
  }


  /**
   * This method clears the session.
   */
  public void clear ()
  {
    getHibernateTemplate ().clear () ;

  }

  public void clearCurrentSession ()
  {

    getHibernateTemplate ().getSessionFactory ().getCurrentSession ().clear () ;

  }

  /**
   * This method loads a proxy for the object by id.
   * 
   * @param persistentClass
   *            a Class object
   * @param idClass
   *            a Class object
   * @param id
   *            a Serializable object
   * @return an Object
   */
  @SuppressWarnings ( "unchecked" )
  public <T, ID extends Serializable> T load ( Class<T> persistentClass, Class<ID> idClass, ID id )
  {

    return ( T ) getHibernateTemplate ().load ( persistentClass, id ) ;
  }

 

  /**
   * Returns the hibernate template value.
   * 
   * @return HibernateTemplate
   * @see #setHibernateTemplate
   */
  public HibernateTemplate getHibernateTemplate ()
  {
    return hibernateTemplate ;
  }

  /**
   * Specifies the hibernate template value.
   * 
   * @param hibernateTemplate
   *            a HibernateTemplate object
   * @see #getHibernateTemplate
   */
  public void setHibernateTemplate ( HibernateTemplate hibernateTemplate )
  {
    this.hibernateTemplate = hibernateTemplate ;
  }

  // Static classes
  // Start of NativeSQLQueryListCallBack

  static class NativeSQLQueryListCallBack<T> implements HibernateCallback
  {
    Class<T> persistentClass ;
    String query ;

    public NativeSQLQueryListCallBack ( Class<T> persistentClass, String query )
    {
      super () ;
      this.persistentClass = persistentClass ;
      this.query = query ;
    }

   
    @SuppressWarnings ( "unchecked" )
    public Object doInHibernate ( final Session session )
    {
      SQLQuery sqlQuery = session.createSQLQuery ( query ).addEntity ( persistentClass ) ;

      return new ArrayList<T> ( sqlQuery.list () ) ;
    }

    
    @SuppressWarnings ( "unchecked" )
    @Override
    public boolean equals ( Object obj )
    {
      if ( obj == null )
      {
        return false ;
      }
      else if ( obj instanceof NativeSQLQueryListCallBack )
      {
        NativeSQLQueryListCallBack<T> n = ( NativeSQLQueryListCallBack<T> ) obj ;
        return this.persistentClass.equals ( n.persistentClass ) && this.query.equals ( n.query ) ;

      }
      return false ;
    }

    /*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Object#hashCode()
		 */
    // @Override
    public int hashCode ()
    {
      return persistentClass.hashCode () + query.hashCode () ;
    }

  }

  // End of NativeSQLQueryListCallBack

  @SuppressWarnings ( "unchecked" )
  public <T> List<T> getNamedQueryList ( Class<T> persistentClass, String queryName, Map<String, Object> arguments )
  {
    return ( List<T> ) getHibernateTemplate ().execute ( new NamedQueryCallBack<T> ( persistentClass, queryName, arguments ) ) ;
  }

  static class NamedQueryCallBack<T> implements HibernateCallback
  {
    Class<T> persistentClass ;
    String queryName ;
    Map<String, Object> arguments ;

    public NamedQueryCallBack ( Class<T> persistentClass, String queryName, Map<String, Object> arguments )
    {
      super () ;
      this.persistentClass = persistentClass ;
      this.queryName = queryName ;
      this.arguments = arguments ;
    }

    /*
		 * (non-Javadoc)
		 *
		 * @see org.springframework.orm.hibernate3.HibernateCallback#doInHibernate(org.hibernate.Session)
		 */
    @SuppressWarnings ( "unchecked" )
    public Object doInHibernate ( final Session session )
    {
      Query query = session.getNamedQuery ( queryName ) ;
      for ( Map.Entry<String, Object> e: arguments.entrySet () )
      {
        query.setParameter ( e.getKey (), e.getValue () ) ;
      }
      return new ArrayList<T> ( query.list () ) ;
    }

  }

  /**
   * This is the generic method to execute the SQL query
   */
  @SuppressWarnings("unchecked")
public <T> List<T> executeSQLQueryList ( Class<?> dtoClass, String query )
  {

    List<T> l = new ArrayList<T> () ;
    if ( !hibernateTemplate.getSessionFactory ().getCurrentSession ().isOpen () )
    {
      throw new RuntimeException ( "No Session open for executing the query " + query ) ;
    }
    l = hibernateTemplate.getSessionFactory ().getCurrentSession ().createSQLQuery ( query ).setResultTransformer ( Transformers.aliasToBean ( dtoClass ) ).list () ;
    return Collections.unmodifiableList ( l ) ;
  }

  /**
   * This is the generic method to execute the SQL query
   */
  @SuppressWarnings("unchecked")
public <T> List<T> executeSQLQueryList ( Class<?> dtoClass, String query, String[] agrs )
  {

    List<T> l = new ArrayList<T> () ;
    if ( !hibernateTemplate.getSessionFactory ().getCurrentSession ().isOpen () )
    {
      throw new RuntimeException ( "No Session open for executing the query " + query ) ;
    }
    SQLQuery sqlQuery = hibernateTemplate.getSessionFactory ().getCurrentSession ().createSQLQuery ( query ) ;
    for ( int i = 0 ; i < agrs.length ; i++ )
    {
      sqlQuery.setString ( i, agrs[ i ] ) ;
    }
    l = sqlQuery.setResultTransformer ( Transformers.aliasToBean ( dtoClass ) ).list () ;
    return Collections.unmodifiableList ( l ) ;
  }

  @SuppressWarnings("unchecked")
public <T> List<T> executeHQLQueryList ( Class<?> dtoClass, String query)
  {
	  List<T> l = new ArrayList<T> () ;
	    try{
	    if ( !hibernateTemplate.getSessionFactory ().getCurrentSession ().isOpen () )
	    {
	      throw new RuntimeException ( "No Session open for executing the query " + query ) ;
	    }
	    else
	    	l = hibernateTemplate.getSessionFactory ().getCurrentSession().createQuery ( query ).setResultTransformer ( Transformers.aliasToBean ( dtoClass ) ).list () ;
	    }
	    catch(Exception e)
	    {	  
	    	e.printStackTrace();
	      
	    }
	     
	    return Collections.unmodifiableList ( l ) ;
  }
  
  @SuppressWarnings("rawtypes")
public Object[] executeHQLQueryObjectPair (String query)
  {
	  Iterator iterator= null;
	  Object[] pairs =null;	  
	  try{
	    if ( !hibernateTemplate.getSessionFactory ().getCurrentSession ().isOpen () )
	    {
	      throw new RuntimeException ( "No Session open for executing the query " + query ) ;
	    }
	    else	    	
	    	iterator=hibernateTemplate.getSessionFactory ().getCurrentSession().createQuery ( query ).list().iterator();
		    while(iterator.hasNext())
		    {
	        	pairs = (Object[]) iterator.next();	        	
		    }
	    }
	    catch(Exception e)
	    {	     
	      e.printStackTrace();
	    }      
	  return pairs;
  }
  
  @SuppressWarnings("unchecked")
public <T> List<T> executeHQLQueryList ( Class<?> dtoClass, String query, Object[] agrs)
  {
    List<T> l = new ArrayList<T> () ;
    try {
		if ( !hibernateTemplate.getSessionFactory ().getCurrentSession ().isOpen () )
		{
		  throw new RuntimeException ( "No Session open for executing the query " + query ) ;
		}
		Query hqlQuery = hibernateTemplate.getSessionFactory ().getCurrentSession ().createQuery ( query ) ;
		for ( int i = 0 ; i < agrs.length ; i++ )
		{
		  hqlQuery.setParameter ( i, agrs[ i ] ) ;
		}
		l = hqlQuery.setResultTransformer ( Transformers.aliasToBean ( dtoClass ) ).list () ;
	} catch (Exception e) {
		e.printStackTrace();
	}
    return Collections.unmodifiableList ( l ) ;
  }


  @SuppressWarnings("rawtypes")
public Object[] executeHQLQueryObjectPair (String query,String[] args)
	{		 
		  Object[] pairs =null;		  
		  try
		  {
			    if ( !hibernateTemplate.getSessionFactory ().getCurrentSession ().isOpen () )
			    {
			      throw new RuntimeException ( "No Session open for executing the query " + query ) ;
			    }		    		    	
			    	
			    Query queryObj = hibernateTemplate.getSessionFactory ().getCurrentSession().createQuery ( query );
			    
			    for ( int i = 0 ; i < args.length ; i++ )
			    {
			    	queryObj.setParameter( i, args[ i ] ) ;
			    }	
			    
		    	Iterator iterator=queryObj.list().iterator();
			    while(iterator.hasNext())
			    {
		        	pairs = (Object[]) iterator.next();	        	
			    }
		    }
		    catch(Exception e)
		    {	     
		      e.printStackTrace();
		    }      
		  return pairs;
	}
  
  /**
	 * This method returns a Long type  max of properties in the given  persistentClass ,a map of properties and Values,
	 *  @param persistentClass      a Class object
	 * @param Operatorandcriteriaobject a Map object
	 * @param propertyString           a property for which max has to be determined
	 * @return a Long object
	 */
 @SuppressWarnings ( "unchecked" )
  public <T> Long getMaxByPropertyCriteria ( final Class<T> persistentClass, final String propertyValue,List<GenericDAOQueryCriteria> Operatorandcriteriaobject)
  {
     Long maxVal=0L; 
	 Criteria crit = ( Criteria ) getHibernateTemplate ().execute ( new HibernateCallback ()
          {
            public Object doInHibernate ( final Session session )
            {
              return session.createCriteria ( persistentClass ) ;
            }
          } ) ; 
      buildCriteria ( crit, Operatorandcriteriaobject ) ;      
      crit.setProjection(Projections.max(propertyValue));
      List<T> results = crit.list () ;
      if(!results.isEmpty())
    	  maxVal= ( Long ) results.get ( 0 );
      return ( maxVal ) ;       
  }
 
 public <T> List<T> findAllByPropertyMatchJoinTable ( final Class<T> persistentClass, final Map<String, Object> propertiesAndValues )
 {
   Criteria crit = buildCriteriaAllByPropertiesJoinTable ( persistentClass, propertiesAndValues, false ) ;
   List<T> hits = getTList ( crit ) ;
   if ( hits != null && hits.size () > 0 )
   {
     return hits ;

   } // endif
   return Collections.emptyList () ;
 }
 /**
  * This method builds the criteria for getting all like columns
  * 
  * @param persistentClass
  *            a Class object
  * @param propertiesAndValues
  *            map
  * @return Criteria
  */
 private <T> Criteria buildCriteriaAllByPropertiesJoinTable ( final Class<T> persistentClass, Map<String, Object> propertiesAndValues, boolean useLike )
 {
     Iterator<Entry<String, Object>> itor = propertiesAndValues.entrySet ().iterator () ;
     Criteria crit = ( Criteria ) getHibernateTemplate ().execute ( new HibernateCallback ()
         {
           public Object doInHibernate ( final Session session )
           {
             return session.createCriteria ( persistentClass ) ;
           }
         } ) ;
     Map<Criteria, Criteria> critMap = new HashMap<Criteria, Criteria> () ;

     while ( itor.hasNext () )
     {
       Entry<String, Object> entry = itor.next () ;
       if ( entry.getValue () == null )
       {
         // Ignore nulls:
         itor.remove () ;
       }
       else
       {
         String key = ( String ) entry.getKey () ;
         int firstDot = key.indexOf ( "." ) ;
         if ( firstDot > 0 )
         {
           String joinEntity = null ;
           // If there is a dot notation do joins
           Criteria newCrit = null ;
           while ( firstDot > 0 )
           {
             joinEntity = key.substring ( 0, key.indexOf ( "." ) ) ;

             // /////////////////////////////////////////////////////////////////
             // There might be an issue if the alias is already
             // defined
             // /////////////////////////////////////////////////////////////////

             
               
                 newCrit = crit.createCriteria ( joinEntity, joinEntity ) ;
                 critMap.put ( crit, newCrit ) ;
               
           
             key = key.substring ( firstDot + 1 ) ;
             firstDot = key.indexOf ( "." ) ;
           }
           if ( useLike && entry.getValue () instanceof String )
           {
             String s = ( ( String ) entry.getValue () ).concat ( "%" ) ;
             newCrit.add ( Restrictions.ilike ( joinEntity + "." + key, s ) ) ;
           }
           else
           {
             newCrit.add ( Restrictions.eq ( joinEntity + "." + key, entry.getValue () ) ) ;
           }

         }
         else
         {
           if ( useLike && entry.getValue () instanceof String )
           {
             String s = ( ( String ) entry.getValue () ).concat ( "%" ) ;
             crit.add ( Restrictions.ilike ( entry.getKey (), s ) ) ;
           }
           else
           {
             crit.add ( Restrictions.eq ( entry.getKey (), entry.getValue () ) ) ;
           }
         }
       }
     } // endwhile
     return crit ;
   
 	}
}