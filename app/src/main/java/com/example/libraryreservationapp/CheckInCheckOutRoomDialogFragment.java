package com.example.libraryreservationapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class CheckInCheckOutRoomDialogFragment extends DialogFragment {
    //for event callbacks
    public interface CheckInCheckOutRoomDialogListener{
        void onDialogPositiveClick(DialogFragment dialog, boolean checkedIn);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    public CheckInCheckOutRoomDialogFragment.CheckInCheckOutRoomDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //get value from bundle
        final boolean checkedIn = getArguments().getBoolean("checkedIn");

        String positiveButtonText;
        String messageText;
        //sets the message text and positive button text to be different if you are already checked in or not
        if(checkedIn){
            positiveButtonText = getContext().getResources().getString(R.string.check_out);
            messageText = getContext().getResources().getString(R.string.check_out_question);
        }
        else{
            positiveButtonText = getContext().getResources().getString(R.string.check_in);
            messageText = getContext().getResources().getString(R.string.check_in_question);
        }

        // Inflate and set the layout for the dialog
        builder.setMessage(messageText)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the HomeFragment
                        listener.onDialogPositiveClick(CheckInCheckOutRoomDialogFragment.this, !checkedIn);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //send the negative button event back to the HomeFragment
                        listener.onDialogNegativeClick(CheckInCheckOutRoomDialogFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
