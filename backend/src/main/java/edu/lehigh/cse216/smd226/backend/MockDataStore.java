package edu.lehigh.cse216.smd226.backend;

import java.util.ArrayList;

/**
 * MockDataStore provides access to a set of objects, and makes sure that each has
 * a unique identifier that remains unique even after the object is deleted.
 * 
 * We follow the convention that member fields of a class have names that start
 * with a lowercase 'm' character, and are in camelCase.
 * 
 * NB: The methods of MockDataStore are synchronized, since they will be used from a 
 * web framework and there may be multiple concurrent accesses to the MockDataStore.
 */
public class MockDataStore {
    /**
     * The rows of data in our MockDataStore
     */
    private ArrayList<MockDataRow> mRows;

    /**
     * A counter for keeping track of the next ID to assign to a new row
     */
    private int mCounter;

    /**
     * Construct the MockDataStore by resetting its counter and creating the
     * ArrayList for the rows of data.
     */
    MockDataStore() {
        mCounter = 0;
        mRows = new ArrayList<>();
    }

    /**
     * Add a new row to the MockDataStore
     * 
     * Note: we return -1 on an error.  There are many good ways to handle an 
     * error, to include throwing an exception.  In robust code, returning -1 
     * may not be the most appropriate technique, but it is sufficient for this 
     * tutorial.
     * 
     * @param message The message for this newly added row
     * @param likes Number of likes for this post
     * @return the ID of the new row, or -1 if no row was created
     */
    public synchronized int createEntry(String message, int likes) {
        if (message == null || likes == 0)
            return -1;
        // NB: we can safely assume that id is greater than the largest index in 
        //     mRows, and thus we can use the index-based add() method
        int id = mCounter++;
        mRows.add(id, new MockDataRow(id, message, likes) );
        return id;
    }

    /**
     * Get one complete row from the MockDataStore using its ID to select it
     * Because we are using a record instead of POJO, we do not make a
     * defensive copy of the object before returning (it's immutable).
     * @param id The id of the row to select
     * @return the data in the row, if it exists, or null otherwise
     */
    public synchronized MockDataRow readOne(int id) {
        if (id >= mRows.size())
            return null;
        return mRows.get(id);
    }

    /**
     * Get all of the ids and messages that are present in the MockDataStore
     * Why a MockDataRowLite rather than MockDataRow? To conserve bandwidth.
     * @return An ArrayList with all of the data; everything is immutable
     */
    public synchronized ArrayList<MockDataRowLite> readAll() {
        ArrayList<MockDataRowLite> data = new ArrayList<>();
        // NB: our ArrayList only has ids and messages to conserve data transfer
        for (MockDataRow row : mRows) {
            if (row != null)
                data.add(new MockDataRowLite(row));
        }
        return data;
    }
     /**
     * Update the message and likes of a row in the MockDataStore
     *
     * @param id The Id of the row to update
     * @param message The new message for the row
     * @param likes new likes for the row
     * @return the row if it exists (copy not required because its immutable), or null otherwise
     */
    public synchronized MockDataRow updateOne(int id, String message, int likes) {
        // Do not update if we don't have valid data
        if (message== null || likes == 0){
            System.err.println( "ERROR: attempt to updateOne using a null message or likes" );
            return null;
        }
        // Only update if the current entry is valid (not off the end of our ArrayList)
        if (id >= mRows.size() || id < 0){
            System.err.println( "ERROR: attempt to updateOne using an id > mRows.size() or id < 0" );
            return null;
        }
        // Retrieve, update, and then return the MockDataRow
        MockDataRow rowOrig = mRows.get(id);
        MockDataRow rowUpdated = new MockDataRow(rowOrig.mId(), message, likes);
        mRows.set(id, rowUpdated);
        return mRows.get(id);
    }

    /**
     * Delete a row from the MockDataStore
     * 
     * @param id the Id of the row to delete
     * @return true if the row was deleted, false otherwise
     */
    public synchronized boolean deleteOne(int id) {
        // Deletion fails for an invalid Id or an Id that has already been deleted
        if (id >= mRows.size() || id < 0 ){
            System.err.println( "ERROR: attempt to deleteOne using an invalid id" );
            return false;
        }
        if (mRows.get(id) == null){
            System.out.println( "Warning: attempt to deleteOne with an id of a row not present in data store" );
            return false;
        }
        // Delete by setting to null, so that any Ids used by other clients
        // still refer to the same positions in the ArrayList.
        mRows.set(id, null);
        return true;
    }
}