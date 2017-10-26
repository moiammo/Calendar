package Services;//5810404936 Yarnadhis Poolsawat

import java.sql.*;

//5810404936 Yarnadhis Poolsawat

public class SqliteDatabase implements  DataSource {


    private String dbURL;
    private Connection conn;
    private String query ;
    private Statement statement ;
    private DatabaseMetaData metaData;
    private ResultSet resultSet;


    @Override
    public void establishConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        dbURL = "jdbc:sqlite:AppointmentsDB.db" ;
        conn = DriverManager.getConnection(dbURL);
        statement = conn.createStatement();
    }

    @Override
    public void closeConnection() throws SQLException {
        statement.close();
        conn.close();
    }
    @Override
    public ResultSet doExecuteQuery(String query) throws SQLException, ClassNotFoundException {
        if (conn != null){ return statement.executeQuery(query); }
        else {
            establishConnection();
            resultSet = statement.executeQuery(query);
            closeConnection();
        }
        return resultSet ;
    }
    @Override
    public void doExecute(String query) throws SQLException, ClassNotFoundException {
        statement.execute(query);
    }
    @Override
    public void doExecuteUpdate(String query) throws SQLException, ClassNotFoundException {
        statement.executeUpdate(query);
    }


    @Override
    public ResultSet getTable(String catalog,String schemaPattern ,String tableNamePattern,String[] types) throws SQLException {
        metaData = conn.getMetaData();
        return metaData.getTables(catalog,schemaPattern,tableNamePattern,types);
    }



}



