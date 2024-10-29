package edu.lehigh.cse216.smd226.backend;

// these imports allow us to connect to a database
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
// these imports are for interacting with a connected database
import java.sql.PreparedStatement;
import java.sql.ResultSet;
// standard imports like common data structures
import java.util.Map;
import java.util.ArrayList;

/**
 * Database has all our logic for connecting to and interacting with a database.
 */
public class Database {
    ////////////////////////// UPDATE ONE 2-ARG //////////////////////////
    /** ps to replace the message in tabledata for specified row with given value */
    private PreparedStatement mUpdateOne_2arg;
    /** the SQL for mUpdateOne */
    private static final String SQL_UPDATE_ONE_TBLDATA_2ARG = "UPDATE tblData" +
            " SET likes = ?, message = ?" +
            " WHERE id = ?";

    /**
     * safely performs mUpdateOne = mConnection.prepareStatement("UPDATE tblData SET
     * message = ? WHERE id = ?");
     */
    private boolean init_mUpdateOne_2arg() {
        // return true on success, false otherwise
        try {
            mUpdateOne_2arg = mConnection.prepareStatement(SQL_UPDATE_ONE_TBLDATA_2ARG);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mUpdateOne");
            System.err.println("Using SQL: " + SQL_UPDATE_ONE_TBLDATA_2ARG);
            e.printStackTrace();
            this.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Update the likes and message for a row in the database
     * 
     * @param id      The id of the row to update
     * @param likes   The new message like
     * @param message The new message contents
     * @return The number of rows that were updated. -1 indicates an error.
     */
    int updateOne(int id, int likes, String message) {
        if (mUpdateOne_2arg == null) // not yet initialized, do lazy init
            init_mUpdateOne_2arg(); // lazy init
        int res = -1;
        try {
            System.out.println("Database operation: updateOne(int id, int likes, String message)");
            mUpdateOne_2arg.setInt(1, likes);
            mUpdateOne_2arg.setString(2, message);
            mUpdateOne_2arg.setInt(3, id);
            res = mUpdateOne_2arg.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * RowData is like a struct in C: we use it to hold data, and we allow
     * direct access to its immutable fields. In the context of this Database,
     * RowData represents the data we'd see in a row.
     * 
     * We make RowData a static class of Database because we don't really want
     * to encourage users to think of RowData as being anything other than an
     * abstract representation of a row of the database. RowData and the
     * Database are tightly coupled: if one changes, the other should too.
     */
    public static record RowData(int mId, int mLikes, String mMessage) {
    }

    /** Connection to db. An open connection if non-null, null otherwise */
    private Connection mConnection;

    ////////////////////////// CREATE TABLE //////////////////////////
    /** precompiled SQL statement for creating the table in our database */
    private PreparedStatement mCreateTable;
    /** the SQL for creating the table in our database */
    private static final String SQL_CREATE_TABLE = "CREATE TABLE tblData (" +
            " id SERIAL PRIMARY KEY," +
            " likes INT NOT NULL," +
            " message VARCHAR(500) NOT NULL)";
    // NB: we can easily get ourselves in trouble here by typing the
    // SQL incorrectly. We really should have things like "tblData"
    // as constants, and then build the strings for the statements
    // from those constants.

    /**
     * safely performs mCreateTable =
     * mConnection.prepareStatement(SQL_CREATE_TABLE);
     */
    private boolean init_mCreateTable() {
        // return true on success, false otherwise
        try {
            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table
            // creation/deletion, so multiple executions will cause an exception
            mCreateTable = mConnection.prepareStatement(SQL_CREATE_TABLE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mCreateTable");
            System.err.println("Using SQL: " + SQL_CREATE_TABLE);
            e.printStackTrace();
            this.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Create tblData. If it already exists, this will print an error
     */
    void createTable() {
        if (mCreateTable == null) // not yet initialized, do lazy init
            init_mCreateTable(); // lazy init
        try {
            System.out.println("Database operation: createTable()");
            mCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ////////////////////////// DROP TABLE //////////////////////////
    /** ps to delete table tbldata from the database */
    private PreparedStatement mDropTable;
    /** the SQL for mDropTable */
    private static final String SQL_DROP_TABLE_TBLDATA = "DROP TABLE tblData";

    /**
     * safely performs mDropTable = mConnection.prepareStatement("DROP TABLE
     * tblData");
     */
    private boolean init_mDropTable() {
        // return true on success, false otherwise
        try {
            mDropTable = mConnection.prepareStatement(SQL_DROP_TABLE_TBLDATA);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mDropTable");
            System.err.println("Using SQL: " + SQL_DROP_TABLE_TBLDATA);
            e.printStackTrace();
            this.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Remove tblData from the database. If it does not exist, this will print
     * an error.
     */
    void dropTable() {
        if (mDropTable == null) // not yet initialized, do lazy init
            init_mDropTable(); // lazy init
        try {
            System.out.println("Database operation: dropTable()");
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ////////////////////////// INSERT //////////////////////////
    /**
     * ps to insert into tbldata a new row with next auto-gen id and the two given
     * values
     */
    private PreparedStatement mInsertOne;
    /** the SQL for mInsertOne */
    private static final String SQL_INSERT_ONE_TBLDATA = "INSERT INTO tblData" +
            " VALUES (default, ?, ?);";

    /**
     * safely performs mInsertOne = mConnection.prepareStatement("INSERT INTO
     * tblData VALUES (default, ?, ?)");
     */
    private boolean init_mInsertOne() {
        // return true on success, false otherwise
        try {
            mInsertOne = mConnection.prepareStatement(SQL_INSERT_ONE_TBLDATA);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mInsertOne");
            System.err.println("Using SQL: " + SQL_INSERT_ONE_TBLDATA);
            e.printStackTrace();
            this.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Insert a row into the database
     * 
     * @param likes   The likes for this new row
     * @param message The message body for this new row
     * @return The number of rows that were inserted
     */
    int insertRow(int likes, String message) {
        if (mInsertOne == null) // not yet initialized, do lazy init
            init_mInsertOne(); // lazy init
        int count = 0;
        try {
            System.out.println("Database operation: insertRow(Int, String)");
            mInsertOne.setInt(1, likes);
            mInsertOne.setString(2, message);
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    ////////////////////////// UPDATE ONE //////////////////////////
    /** ps to replace the message in tabledata for specified row with given value */
    private PreparedStatement mUpdateOne;
    /** the SQL for mUpdateOne */
    private static final String SQL_UPDATE_ONE_TBLDATA = "UPDATE tblData" +
            " SET message = ?" +
            " WHERE id = ?";

    /**
     * safely performs mUpdateOne = mConnection.prepareStatement("UPDATE tblData SET
     * message = ? WHERE id = ?");
     */
    private boolean init_mUpdateOne() {
        // return true on success, false otherwise
        try {
            mUpdateOne = mConnection.prepareStatement(SQL_UPDATE_ONE_TBLDATA);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mUpdateOne");
            System.err.println("Using SQL: " + SQL_UPDATE_ONE_TBLDATA);
            e.printStackTrace();
            this.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Update the message for a row in the database
     * 
     * @param id      The id of the row to update
     * @param message The new message contents
     * @return The number of rows that were updated. -1 indicates an error.
     */
    int updateOne(int id, String message) {
        if (mUpdateOne == null) // not yet initialized, do lazy init
            init_mUpdateOne(); // lazy init
        int res = -1;
        try {
            System.out.println("Database operation: updateOne(int id, String message)");
            mUpdateOne.setString(1, message);
            mUpdateOne.setInt(2, id);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    ////////////////////////// DELETE //////////////////////////
    /** ps for deleting a row from tblData */
    private PreparedStatement mDeleteOne;
    /** the SQL for mDeleteOne */
    private static final String SQL_DELETE_ONE = "DELETE FROM tblData" +
            " WHERE id = ?";

    /**
     * safely performs mDeleteOne = mConnection.prepareStatement(SQL_DELETE_ONE);
     */
    private boolean init_mDeleteOne() {
        // return true on success, false otherwise
        try {
            mDeleteOne = mConnection.prepareStatement(SQL_DELETE_ONE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mDeleteOne");
            System.err.println("Using SQL: " + SQL_DELETE_ONE);
            e.printStackTrace();
            this.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Delete a row by ID
     * 
     * @param id The id of the row to delete
     * @return The number of rows that were deleted. -1 indicates an error.
     */
    int deleteRow(int id) {
        if (mDeleteOne == null) // not yet initialized, do lazy init
            init_mDeleteOne(); // lazy init
        int res = -1;
        try {
            System.out.println("Database operation: deleteRow(int)");
            mDeleteOne.setInt(1, id);
            res = mDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    ////////////////////////// SELECT ALL //////////////////////////
    /** ps to return all rows from tblData, but only the id and likes columns */
    private PreparedStatement mSelectAll;
    /** the SQL for mSelectAll */
    private static final String SQL_SELECT_ALL_TBLDATA = "SELECT id, likes, message" +
            " FROM tblData;";

    /**
     * safely performs mSelectAll = mConnection.prepareStatement("SELECT id, likes
     * FROM tblData");
     */
    private boolean init_mSelectAll() {
        // return true on success, false otherwise
        try {
            mSelectAll = mConnection.prepareStatement(SQL_SELECT_ALL_TBLDATA);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectAll");
            System.err.println("Using SQL: " + SQL_SELECT_ALL_TBLDATA);
            e.printStackTrace();
            this.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Query the database for a list of all likes and their IDs
     * 
     * @return All rows, as an ArrayList; note that message is intentionally not
     *         returned
     */
    ArrayList<RowData> selectAll() {
        if (mSelectAll == null) // not yet initialized, do lazy init
            init_mSelectAll(); // lazy init
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            System.out.println("Database operation: selectAll()");
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int likes = rs.getInt("likes");
                String message = rs.getString("message");
                RowData data = new RowData(id, likes, message);
                res.add(data);
            }
            rs.close(); // remember to close the result set
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    ////////////////////////// SELECT ONE //////////////////////////
    /** ps to return row from tblData with matching id */
    private PreparedStatement mSelectOne;
    /** the SQL for mSelectOne */
    private static final String SQL_SELECT_ONE_TBLDATA = "SELECT *" +
            " FROM tblData" +
            " WHERE id=? ;";

    /**
     * safely performs mSelectOne = mConnection.prepareStatement("SELECT * from
     * tblData WHERE id=?");
     */
    private boolean init_mSelectOne() {
        // return true on success, false otherwise
        try {
            mSelectOne = mConnection.prepareStatement(SQL_SELECT_ONE_TBLDATA);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectOne");
            System.err.println("Using SQL: " + SQL_SELECT_ONE_TBLDATA);
            e.printStackTrace();
            this.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowData selectOne(int row_id) {
        if (mSelectOne == null) // not yet initialized, do lazy init
            init_mSelectOne(); // lazy init
        RowData data = null;
        try {
            System.out.println("Database operation: selectOne(int)");
            mSelectOne.setInt(1, row_id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                int likes = rs.getInt("likes");
                String message = rs.getString("message");
                data = new RowData(id, likes, message);
            }
            rs.close(); // remember to close the result set
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    ////////////////////////// CONNECT & DISCONNECT //////////////////////////

    /**
     * Close the current connection to the database, if one exists.
     * 
     * NB: The connection will always be null after this call, even if an
     * error occurred during the closing operation.
     * 
     * @return true if the connection was cleanly closed, false otherwise
     */
    boolean disconnect() {
        if (mConnection != null) {
            // for this simple example, we disconnect from the db when done
            System.out.print("Disconnecting from database.");
            try {
                mConnection.close();
            } catch (SQLException e) {
                System.err.println("\n\tError: close() threw a SQLException");
                e.printStackTrace();
                mConnection = null; // set it to null rather than leave broken
                return false;
            }
            System.out.println(" ...  connection successfully closed");
            mConnection = null; // connection is gone, so null this out
            return true;
        }
        // else connection was already null
        System.err.print("Unable to close connection: Connection was null");
        return false;
    }

    /**
     * The Database constructor is private: we only create Database objects
     * through one or more static getDatabase() methods.
     */
    private Database() {
    }

    /**
     * Uses dbUri to create a connection to a database, and stores it in the
     * returned Database object
     * 
     * @param dbUri the connection string for the database
     * @return null upon connection failure, otherwise a valid Database with open
     *         connection
     */
    static Database getDatabase(String dbUri) {
        // Connect to the database or fail
        if (dbUri != null && dbUri.length() > 0) {
            // DATABASE_URI appears to be set
            Database returned_val = new Database();
            System.out.println("Attempting to use provided DATABASE_URI env var.");
            try {
                returned_val.mConnection = DriverManager.getConnection(dbUri);
                if (returned_val.mConnection == null) {
                    System.err.println("Error: DriverManager.getConnection() returned a null object");
                    return null; // we return null to indicate failure; callers should know this
                } else {
                    System.out.println(" ... successfully connected");
                }
            } catch (SQLException e) {
                System.err.println("Error: DriverManager.getConnection() threw a SQLException");
                e.printStackTrace();
                return null; // we return null to indicate failure; callers should know this
            }
            return returned_val; // the valid object with active connection
        }
        return null;
    }

    /**
     * Uses params to manually construct string for connecting to a database,
     * and stores it in the returned Database object
     * 
     * @param ip     not strictly an ip, can also be a host name
     * @param port   default tends to be 5432 or something like it
     * @param dbname generally should be non null
     * @param user   generally required
     * @param pass   generally required
     * @return null upon connection failure, otherwise a valid Database with open
     *         connection
     */
    static Database getDatabase(String ip, String port, String dbname, String user, String pass) {
        if (ip != null && port != null &&
                dbname != null &&
                user != null && pass != null) {
            // POSTGRES_* variables appear to be set
            Database returned_val = new Database();
            System.out.println("Attempting to use provided POSTGRES_{IP, PORT, USER, PASS, DBNAME} env var.");
            System.out.println("Connecting to " + ip + ":" + port);
            try {
                // Open a connection, fail if we cannot get one
                returned_val.mConnection = DriverManager
                        .getConnection("jdbc:postgresql://" + ip + ":" + port + "/" + dbname, user, pass);
                if (returned_val.mConnection == null) {
                    System.out.println("\n\tError: DriverManager.getConnection() returned a null object");
                    return null; // we return null to indicate failure; callers should know this
                } else {
                    System.out.println(" ... successfully connected");
                }
            } catch (SQLException e) {
                System.out.println("\n\tError: DriverManager.getConnection() threw a SQLException");
                e.printStackTrace();
                return null; // we return null to indicate failure; callers should know this
            }
            return returned_val; // the valid object with active connection
        }
        return null;
    }

    /**
     * Uses the presence of environment variables to invoke the correct
     * overloaded `getDatabase` method. Either DATABASE_URL should be set,
     * or the values of four other variables POSTGRES_{IP, PORT, USER, PASS,
     * DBNAME}.
     * 
     * @return a valid Database object with active connection on success, null
     *         otherwise
     */
    static Database getDatabase() {
        // get the Postgres configuration from environment variables;
        // you could name them almost anything
        Map<String, String> env = System.getenv();
        String ip = env.get("POSTGRES_IP");
        String port = env.get("POSTGRES_PORT");
        String user = env.get("POSTGRES_USER");
        String pass = env.get("POSTGRES_PASS");
        String dbname = env.get("POSTGRES_DBNAME");
        String dbUri = env.get("DATABASE_URI");

        System.out.printf("Using the following environment variables:%n%s%n", "-".repeat(45));
        System.out.printf("POSTGRES_IP=%s%n", ip);
        System.out.printf("POSTGRES_PORT=%s%n", port);
        System.out.printf("POSTGRES_USER=%s%n", user);
        System.out.printf("POSTGRES_PASS=%s%n", "omitted");
        System.out.printf("POSTGRES_DBNAME=%s%n", dbname);
        System.out.printf("DATABASE_URI=%s%n", dbUri);

        if (dbUri != null && dbUri.length() > 0) {
            return Database.getDatabase(dbUri);
        } else if (ip != null && port != null && dbname != null && user != null && pass != null) {
            return Database.getDatabase(ip, port, dbname, user, pass);
        }
        // else insufficient information to connect
        System.err.println("Insufficient information to connect to database.");
        return null;
    }

}
