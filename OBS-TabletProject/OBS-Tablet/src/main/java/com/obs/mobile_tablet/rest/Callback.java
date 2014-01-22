package com.obs.mobile_tablet.rest;

/**
 * Created by david on 12/21/13.
 */
public abstract class Callback<T> {
        /**
         * Called when the HTTP request completes.
         *
         * @param result The result of the HTTP request.
         */
        public abstract void onTaskCompleted(T result);

        public abstract void onTaskFailure(String Reason);
}
