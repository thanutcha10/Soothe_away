package com.example.champ.soothe_away;

import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;


public class Task extends AsyncTask<Request, Void, String> {

    private Callback callback;
    private int responseCode;
    private JSONObject result;
    ;

    public void setWebserviceListener(Callback callback) {
        synchronized (this) {
            this.callback = callback;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Request... params) {
        Request request = params[0];

        OkHttpClient okHttpClient = new OkHttpClient();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                responseCode = response.code();
                return response.body().string();
            } else {
                return "Not Success - code : " + response.code();
            }

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            if (responseCode == 200 && result != null) {
                JSONObject jsonObject = new JSONObject(result);
                this.setResult(jsonObject);
                callback.Complete(this);
            } else {
                callback.Error("response code" + responseCode);
            }
        } catch (Exception e) {
            callback.Error(e.getMessage());
        }


    }

    public JSONObject getResult() {
        return this.result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

    public interface Callback {
        void Complete(Task task);

        void Error(String errorMsg);

    }
}
