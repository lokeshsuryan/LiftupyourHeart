package com.liftupmyheart.rest;


/**
 * Created by Scorpion on 11-09-2015.
 */
public class Singleton {
    private static final Object LOCK = new Object();
    public static FinalWrapper<Singleton> helperWrapper;
    private final RestClient mRestOkClient;
    private final RestClient mRestAuthenticatedOkClient;

    private Singleton() {
        // Rest client without basic authorization
        mRestOkClient = ServiceGenerator.createService(RestClient.class);
        mRestAuthenticatedOkClient = ServiceGenerator.createAuthenticated(RestClient.class);

    }

    /**
     * @return
     */
    public static Singleton getInstance() {
        FinalWrapper<Singleton> wrapper = helperWrapper;

        if (wrapper == null) {
            synchronized (LOCK) {
                if (helperWrapper == null) {
                    helperWrapper = new FinalWrapper<>(new Singleton());
                }
                wrapper = helperWrapper;
            }
        }
        return wrapper.value;
    }

    public RestClient getRestOkClient() {
        return mRestOkClient;
    }

    public RestClient getRestAuthenticatedOkClient() {
        return mRestAuthenticatedOkClient;
    }


}