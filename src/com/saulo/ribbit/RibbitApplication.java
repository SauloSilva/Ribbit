package com.saulo.ribbit;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class RibbitApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "BzndyrUACCSGllQOVPRQjKgjkvixnG0yRdqU1ybr", "k7O9nL0amuyEDErhnAeUfwaOqWIhIeWmwkJ1u55n");
	}
}
