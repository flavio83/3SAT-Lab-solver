package com.net.flavix.persistflow;

import java.sql.Types;
import org.hibernate.dialect.PostgreSQL9Dialect;
 



public class ArrayPostgreSQLDialect extends PostgreSQL9Dialect {
 
    public ArrayPostgreSQLDialect() {
        super();
        this.registerColumnType(Types.ARRAY, "text[]");
    }
}
 