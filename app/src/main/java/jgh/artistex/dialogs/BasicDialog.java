package jgh.artistex.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;

import jgh.artistex.R;

/**
 * A basic dialog class, used as the base class for other dialogs.
 */
public class BasicDialog {

    /**
     * calling activity
     */
    private Activity mActivity;
    /**
     * the alertdialog builder
     */
    private AlertDialog.Builder mBuilder;

    private final AlertDialog mDialog;

    private View mLayout;


    /**
     * Instantiates a dialog instance.
     * @param activity caller activity
     * @param rootLayout layout to use.
     */
    public BasicDialog(Activity activity, int rootLayout) {
        mActivity = activity;

        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        mLayout = inflater.inflate(rootLayout, null);

        mBuilder = new AlertDialog.Builder(new ContextThemeWrapper(mActivity, R.style.ArtisteXTheme));
        mBuilder.setView(mLayout);
        mDialog = mBuilder.create();
    }

    /**
     * Gets the activity used as context for this dialog.
     *
     * @return Activity
     */
    protected Activity getActivity() {
        return mActivity;
    }

    /**
     * Gets the AlertDialog builder.
     *
     * @return
     */
    protected AlertDialog.Builder getBuilder() {
        return mBuilder;
    }

    /**
     * Gets the AlertDialog.
     *
     * @return
     */
    protected AlertDialog getDialog() {
        return mDialog;
    }

    /**
     * Gets the base layout.
     *
     * @return base view layout.
     */
    protected View getLayout() {
        return mLayout;
    }

}