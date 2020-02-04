package com.liftupmyheart.rest;

/**
 * Created by MIT on 10/24/2016.
 */
public class APIManager {
    private static APIManager instance = new APIManager();
    private RestClient restClient;
    private APIManager() {
        restClient = Singleton.getInstance().getRestAuthenticatedOkClient();

    }

    public static APIManager getInstance() {
        if (instance == null) {
            instance = new APIManager();
        }
        return instance;
    }
    public interface APICallback<T> {
        void onSuccess(T response);
    }
}
