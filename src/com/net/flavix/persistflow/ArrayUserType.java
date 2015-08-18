package com.net.flavix.persistflow;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;




public class ArrayUserType<T> extends AbstractMutableUserType {

    protected String getDialectPrimitiveName() {
    	return "VARCHAR(20)";
    }

    @Override
    public int[] sqlTypes() {
        return new int[] { Types.ARRAY };
    }


    @Override
    public Class<?> returnedClass() {
        return Array.class;
    }


    @SuppressWarnings("unchecked")
    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
            throws HibernateException, SQLException {
        Array sqlArray = rs.getArray(names[0]);

        if (rs.wasNull()) {
            return Collections.EMPTY_LIST;
        } else {
            List<T> list = new ArrayList<>();
            for (Object element : (Object[]) sqlArray.getArray()) {
                list.add((T) element);
            }
            return list;
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public void nullSafeSet(final PreparedStatement statement, final Object object, final int i,
            SessionImplementor session) throws HibernateException, SQLException {
        Connection connection = session.connection();
        List<T> list = (List<T>) object;
        Object[] elements = list == null ? new Object[] {} : list.toArray();
        Array array = connection.createArrayOf(getDialectPrimitiveName(), elements);
        statement.setArray(i, array);
    }


    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if (value == null) {
            return new Object[]{};
        } else {
            return (Object[])value;
        }
    }

}