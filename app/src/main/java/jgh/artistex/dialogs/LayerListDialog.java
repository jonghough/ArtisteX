package jgh.artistex.dialogs;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import jgh.artistex.R;

/**
 * Dialog for listing current layers, in drawing order
 * on canvas.
 */
public class LayerListDialog extends BasicDialog{

    private ListView mList;

    public LayerListDialog(final Activity activity, int rootLayout, ArrayList<String> layerNames, final LayerOptionsCallbackHandler callbackHandler) {

        super(activity, rootLayout);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1);

        if(layerNames!=null){
            adapter.addAll(layerNames);
            mList = (ListView)getLayout().findViewById(R.id.layerlist_item);
            mList.setAdapter(adapter);
            mList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                    new SingleLayerOptionsDialog(activity, position, callbackHandler);
                    getDialog().dismiss();


                }

            });
        }

        getDialog().show();
    }
}
