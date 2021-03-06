package fr.creads.midipile.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;

/**
 * Author : Hugo Gresse
 * Date : 02/09/14
 */
public class DealNetworkDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_network_error)
                .setPositiveButton(R.string.dialog_network_error_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // retry load data
                    ((HomeActivity)getActivity()).loadLastDeals();
                    }
                })
                .setNegativeButton(R.string.dialog_network_error_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    // no nothing
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
