package org.ff.db;

import java.sql.Types;

public class MySQLDialect extends org.hibernate.dialect.MySQL5Dialect {

	public MySQLDialect() {
		super();
		registerColumnType( Types.NCLOB, "longtext" );
	}

}
