package Services;//5810404936 Yarnadhis Poolsawat

import java.sql.*;

//5810404936 Yarnadhis Poolsawat

public class DatabaseService {


    private String dbURL;
    private Connection conn;
    private String query ;
    private Statement statement ;
    private DatabaseMetaData metaData;
    private ResultSet resultSet;



    public void establishConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        dbURL = "jdbc:sqlite:AppointmentsDB.db" ;
        conn = DriverManager.getConnection(dbURL);
        statement = conn.createStatement();
    }


    public void closeConnection() throws SQLException {
        statement.close();
        conn.close();
    }

    public ResultSet doExecuteQuery(String query) throws SQLException, ClassNotFoundException {
        if (conn != null){ return statement.executeQuery(query); }
        else {
            establishConnection();
            resultSet = statement.executeQuery(query);
            closeConnection();
        }
        return resultSet ;
    }

    public void doExecute(String query) throws SQLException, ClassNotFoundException {
        statement.execute(query);
    }

    public void doExecuteUpdate(String query) throws SQLException, ClassNotFoundException {
        statement.executeUpdate(query);
    }
//    DatabaseMetaData metaData = conn.getMetaData();
//    ResultSet resultSet = metaData.getTables(null,null,"Appointments",null);

    public ResultSet getTable(String catalog,String schemaPattern ,String tableNamePattern,String[] types) throws SQLException {
        metaData = conn.getMetaData();
        return metaData.getTables(catalog,schemaPattern,tableNamePattern,types);
    }



}



