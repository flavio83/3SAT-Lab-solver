package com.net.flavix.dto;

import java.sql.Types;

import org.hibernate.dialect.MySQL5InnoDBDialect;




public class MariaDBCustomDialect extends MySQL5InnoDBDialect {
    protected void registerColumnType(int code, String name) {
    	System.out.println(code + " " + name);
        if (code == Types.TIMESTAMP) {
            super.registerColumnType(code, "TIMESTAMP(3)");
        } else if (code == 2003) {
        this.registerColumnType(Types.ARRAY, "object[]");
        
        } else {
            super.registerColumnType(code, name);
        }
    }
}