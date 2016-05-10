package jgh.artistex.engine;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

/**
 * Async Task for background ops
 */
public class BackgroundTask extends AsyncTask<Void,Void,Boolean> {

    private final String TAG = "BackgroundTask";
    private DrawingEngine.IEngineTask mEngineTask;

    public BackgroundTask(DrawingEngine.IEngineTask engineTask){
        mEngineTask = engineTask;
    }

    @Override
    protected void onPreExecute() {
        mEngineTask.setup();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return mEngineTask.doTask();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mEngineTask.onResult(result);
    }
}
