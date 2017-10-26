package Services;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DataSource {

    public void establishConnection() throws SQLException, ClassNotFoundException ;


    public void closeConnection() throws SQLException ;

    public ResultSet doExecuteQuery(String query) throws SQLException, ClassNotFoundException ;

    public void doExecute(String query) throws SQLException, ClassNotFoundException ;

    public void doExecuteUpdate(String query) throws SQLException, ClassNotFoundException ;

    public ResultSet getTable(String catalog,String schemaPattern ,String tableNamePattern,String[] types) throws SQLException;
}
