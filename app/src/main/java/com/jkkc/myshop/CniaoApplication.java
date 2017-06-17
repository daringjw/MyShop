package com.jkkc.myshop;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;



public class CniaoApplication extends Application {





    private static  CniaoApplication mInstance;


    public static  CniaoApplication getInstance(){

        return  mInstance;
    }



    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        Fresco.initialize(this);
    }








    private  Intent intent;
    public void putIntent(Intent intent){
        this.intent = intent;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public void jumpToTargetActivity(Context context){

        context.startActivity(intent);
        this.intent =null;
    }

}
