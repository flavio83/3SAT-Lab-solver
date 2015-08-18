package com.net.flavix.dto;

import org.hibernate.SessionFactory;
import org.hibernate.annotations.common.reflection.MetadataProviderInjector;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.net.flavix.persistflow.UUIDTypeInsertingMetadataProvider;




public class HibernateUtil {
	
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
        	// Perform some test to verify that the current database is Postgres.
        	//if (connectionString.startsWith("jdbc:postgresql:")) {
        	    // Replace the metadata provider with our custom metadata provider.
        	Configuration configuration = new Configuration().configure();
        	    MetadataProviderInjector reflectionManager = (MetadataProviderInjector)configuration.getReflectionManager();
        	    reflectionManager.setMetadataProvider(new UUIDTypeInsertingMetadataProvider(reflectionManager.getMetadataProvider()));
        	//}
        	
        	StandardServiceRegistryBuilder builder = 
        			new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        	return configuration.buildSessionFactory(builder.build());
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
}