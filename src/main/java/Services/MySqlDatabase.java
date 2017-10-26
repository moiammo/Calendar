package Services;//5810404936 Yarnadhis Poolsawat

import java.sql.*;

//5810404936 Yarnadhis Poolsawat

public class MySqlDatabase implements  DataSource {


    private String dbURL;
    private Connection conn;
    private String query ;
    private Statement statement ;
    private DatabaseMetaData metaData;
    private ResultSet resultSet;


    @Override
    public void establishConnection() throws SQLException, ClassNotFoundException {

    }

    @Override
    public void closeConnection() throws SQLException {

    }

    @Override
    public ResultSet doExecuteQuery(String query) throws SQLException, ClassNotFoundException {
        return resultSet ;
    }

    @Override
    public void doExecute(String query) throws SQLException, ClassNotFoundException {

    }
    @Override
    public void doExecuteUpdate(String query) throws SQLException, ClassNotFoundException {

    }


    @Override
    public ResultSet getTable(String catalog,String schemaPattern ,String tableNamePattern,String[] types) throws SQLException {
        return resultSet ;
    }



}



