package edu.lehigh.cse216.smd226.backend;

/**
 * StructuredResponse provides a common format for success and failure messages,
 * with an optional payload of type Object that can be converted into JSON.
 * @param mStatus for applications to determine if response indicates an error
 * @param mMessage only useful when mStatus indicates an error, or when data is null
 * @param mData any JSON-friendly object can be referenced here, so a client gets a rich reply
 */
public record StructuredResponse(String mStatus, String mMessage, Object mData){
    /**
     * If the mStatus is not provided, set it to "invalid".
     * @param mStatus the status of the response, typically "ok" or "error"
     * @param mMessage the message to go along with an error status
     * @param mData an object with additional data to send to the client
     */
    public StructuredResponse {
        mStatus = (mStatus != null) ? mStatus : "invalid";
    }
}