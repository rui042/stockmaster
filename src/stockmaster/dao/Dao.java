package stockmaster.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public abstract class Dao {
    /** データソース */
    private static DataSource ds;

    /**
     * データベースへのコネクションを返す
     * @return Connection
     * @throws SQLException
     * @throws NamingException
     */
    protected Connection getConnection() throws SQLException, NamingException {
        if (ds == null) {
            InitialContext ic = new InitialContext();
            ds = (DataSource) ic.lookup("java:/comp/env/jdbc/stms");
        }
        return ds.getConnection();
    }
}