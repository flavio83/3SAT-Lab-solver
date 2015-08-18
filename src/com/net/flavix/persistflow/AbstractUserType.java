package com.net.flavix.persistflow;

import java.util.Objects;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;




public abstract class AbstractUserType implements UserType {

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x, y);
    }


    @Override
    public int hashCode(Object x) throws HibernateException {
        return Objects.hashCode(x);
    }
}