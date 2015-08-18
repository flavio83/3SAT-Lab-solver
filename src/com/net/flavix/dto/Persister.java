package com.net.flavix.dto;

import java.lang.reflect.Array;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;




public class Persister<T> {
	
	public void persist(List<T> t) throws Exception {
		persist(toArray(t));
	}
	
	public static <T> T[] toArray(Collection<T> c, T[] a) {
	    return c.size()>a.length ?
	        c.toArray((T[])Array.newInstance(a.getClass().getComponentType(), c.size())) :
	        c.toArray(a);
	}

	/** The collection CAN be empty */
	public static <T> T[] toArray(Collection<T> c, Class klass) {
	    return toArray(c, (T[])Array.newInstance(klass, c.size()));
	}

	/** The collection CANNOT be empty! */
	public static <T> T[] toArray(Collection<T> c) {
	    return toArray(c, c.iterator().next().getClass());
	}
	
	public void merge() throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			session.merge(this);
			transaction.commit();
			session.close();
		} catch (Exception e) {
			if (session!=null && session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			throw e;
		}
	}
	
	public void merge(T... t) throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			for(T p : t) {
				session.merge(p);
			}
			transaction.commit();
			session.close();
		} catch (Exception e) {
			if (session!=null && session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			throw e;
		}
	}
	
	
	public void persist(T... t) throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			for(T p : t) {
				session.save(p);
			}
			transaction.commit();
			session.close();
		} catch (Exception e) {
			if (session!=null && session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			throw e;
		}
	}
	
	public void persistUnique(T t) throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();
			session.save(t);
			transaction.commit();
			session.close();
		} catch (Exception e) {
			if (session!=null && session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			throw e;
		}
	}
	
	public T loadPerID(UUID uuid) throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			Criteria criteria = session.createCriteria(this.getClass());
			criteria.add(Restrictions.idEq(uuid));
			return (T)criteria.uniqueResult();
		} catch (Exception e) {
			if (session!=null && session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			throw e;
		}
	}

	public T loadPerCategory(int category) throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			Criteria criteria = session.createCriteria(this.getClass());
			criteria.add(Restrictions.eq("category", category));
			return (T)criteria.uniqueResult();
		} catch (Exception e) {
			if (session!=null && session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			throw e;
		}
	}
	
	public List<T> loadPerDate(ZonedDateTime time) throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			Criteria criteria = session.createCriteria(this.getClass());
			criteria.add(Restrictions.eq("date", time));
			List list = criteria.list();
			List<T> listt = new ArrayList<T>(list.size());
			for (Object o : list) {
				listt.add((T) o);
			}
			return listt;
		} catch (Exception e) {
			if (session!=null && session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			throw e;
		}
	}
	
	public List<T> loadPerDateRange(ZonedDateTime start, ZonedDateTime end) throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			Criteria criteria = session.createCriteria(this.getClass());
			System.out.println(start.toLocalDateTime() + " " + end.toLocalDateTime());
			criteria.add(Restrictions.ge("date", start.toLocalDateTime()));
			criteria.add(Restrictions.le("date", end.toLocalDateTime()));
			List list = criteria.list();
			List<T> listt = new ArrayList<T>(list.size());
			for (Object o : list) {
				listt.add((T) o);
			}
			return listt;
		} catch (Exception e) {
			if (session!=null && session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			throw e;
		}
	}
	
	public List<T> loadAll() throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			Criteria criteria = session.createCriteria(this.getClass());
			List list = criteria.list();
			List<T> listt = new ArrayList<T>(list.size());
			for (Object o : list) {
				listt.add((T) o);
			}
			return listt;
		} catch (Exception e) {
			if (session!=null && session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			throw e;
		}
	}

}
