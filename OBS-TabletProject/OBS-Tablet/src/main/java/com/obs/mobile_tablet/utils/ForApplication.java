package com.obs.mobile_tablet.utils;

/**
 * Created by david on 12/23/13.
 */

import java.lang.annotation.Retention;
import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier @Retention(RUNTIME)
public @interface ForApplication {
}

