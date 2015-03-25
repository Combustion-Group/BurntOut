package com.burntout.burntout;

import org.json.JSONObject;

/**
 * Created by Alex on 12/3/14.
 */
public interface Communicator {
    public void gotResponse(JSONObject s);
}
