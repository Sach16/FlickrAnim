package com.codepath.apps.restclienttemplate.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.interfaces.ServerCallback;

/**
 * Created by S.K. Pissay on 20/3/16.
 */
public abstract class FlickrFragmentBaseClass extends Fragment implements View.OnClickListener, ServerCallback{

    protected FlickrBaseActivity m_cObjMainActivity;
    protected UIHandler m_cObjUIHandler;
    protected View m_cObjMainView;
    protected boolean m_cIsActivityAttached;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity pObjActivity) {
        super.onAttach(pObjActivity);
        m_cObjMainActivity = (FlickrBaseActivity) getActivity();
        m_cObjUIHandler = new UIHandler();
        m_cIsActivityAttached = true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        m_cIsActivityAttached = false;
    }

    @Override
    public void onClick(View v) {
    }

    public final class UIHandler extends Handler {
        public void handleMessage(Message pObjMessage) {
            handleUIMessage(pObjMessage);
        }
    }

    public void handleUIhandler(Message pObjMessage) {
        Message lObJMsg = new Message();
        lObJMsg.what = pObjMessage.what;
        lObJMsg.arg1 = pObjMessage.arg1;
        lObJMsg.arg2 = pObjMessage.arg2;
        lObJMsg.obj = pObjMessage.obj;
        m_cObjUIHandler.sendMessage(lObJMsg);
    }

    protected abstract void handleUIMessage(Message pObjMessage);

    @Override
    public void complete(int code) {

    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {

    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        if(error instanceof NoConnectionError){
            Toast.makeText(m_cObjMainActivity, getResources().getString(R.string.network_connection), Toast.LENGTH_SHORT).show();
            m_cObjMainActivity.hideDialog();
        }
        if(error.networkResponse.statusCode == 401){
        }
    }

}
