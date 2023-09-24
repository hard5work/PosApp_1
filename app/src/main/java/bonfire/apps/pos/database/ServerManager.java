package bonfire.apps.pos.database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import bonfire.apps.pos.activities.LoginAndServerActivity;

public class ServerManager {
    private static final String PREFNAME = "PREFNAME";
     int PRIVATE_MODE = 0;
    private static final String SERVERNAME = "SERVERNAME";
    private static final String USERNAME = "USERNAME";
    private static final String IS_LOGIN = "ISLOGIN";
    private static final String USER_ID = "USERID";
    private static final String DEPARTMENTID = "DEPARTMENTID";
    private static final String TYPE = "TYPE";
    private static final String REMARKS = "REMARKS";
    private static final String TABLENO = "TABLENO";
    private static final String TABLEID = "TABLEID";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Context context;

    public ServerManager(Context context) {
        this.context = context;
        this.sp = this.context.getSharedPreferences(PREFNAME, PRIVATE_MODE);
        this.editor = this.sp.edit();
    }

    public void setServer(String url){
        this.editor.putString(SERVERNAME, url);
        this.editor.commit();
    }

    public String getServer(){

        return  sp.getString(SERVERNAME, "");

    }

    public void setUsername(String username){
        this.editor.putBoolean(IS_LOGIN, true);
        this.editor.putString(USERNAME, username);
        this.editor.commit();
    }

    public void setType(String type){
        this.editor.putBoolean(IS_LOGIN, true);
        this.editor.putString(TYPE, type);
        this.editor.commit();

    }

    public String getUsername(){
        return sp.getString(USERNAME, "");

    }
    public void setTableno(String tableno){
        this.editor.putString(TABLENO, tableno);
        this.editor.commit();
    }
    public String getTableno(){
        return sp.getString(TABLENO, "");

    }
    public void setTableID(String tableid){
        this.editor.putString(TABLEID, tableid);
        this.editor.commit();
    }
    public String getTableID(){
        return sp.getString(TABLEID, "");

    }
    public void setUserId(String id){
        this.editor.putBoolean(IS_LOGIN, true);
        this.editor.putString(USER_ID,id);
        this.editor.commit();
    }
    public void setDeId(String id){
        this.editor.putString(DEPARTMENTID, id);
        this.editor.commit();
    }
    public String getDId(){
        return this.sp.getString(DEPARTMENTID, "");
    }
    public void setRemarks(String rem){
        this.editor.putString(REMARKS, rem);
        this.editor.commit();
    }

    public String getRemark(){
        return this.sp.getString(REMARKS, "");
    }


    public String getId(){
        return this.sp.getString(USER_ID, "");
    }
    public String getType(){
        return sp.getString(TYPE, "");
    }

    public void logOut(){
        this.editor.putBoolean(IS_LOGIN, false);
        this.editor.putString(USERNAME, "");
        this.editor.putString(TYPE,"");
        this.editor.putString(USER_ID,"");
        this.editor.putString(REMARKS,"");
        this.editor.putString(DEPARTMENTID, "");
        this.editor.commit();
        Intent intent = new Intent(context, LoginAndServerActivity.class);   //personal logout
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.context.startActivity(intent);
        ((Activity) this.context).finish();
    }

    public boolean isLoggedIn(){
        return this.sp.getBoolean(IS_LOGIN, false);
    }
}
