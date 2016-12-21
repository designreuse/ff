package org.ff.common.db;

import java.sql.Types;

public class MSSQLDialect extends org.hibernate.dialect.SQLServer2008Dialect {

	public MSSQLDialect() {
		super();
		registerColumnType( Types.NCLOB, "ntext" );
	}

}
