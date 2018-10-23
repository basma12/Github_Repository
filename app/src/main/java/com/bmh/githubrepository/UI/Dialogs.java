package com.bmh.githubrepository.UI;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bmh.githubrepository.R;
import com.bmh.githubrepository.Tools.StaticValues;

public class Dialogs  extends DialogFragment {


    public static Dialogs newInstance(String message, int dialogType) {
        Dialogs dialogs = new Dialogs();
        Bundle args = new Bundle();
        args.putString(StaticValues.DIALOG_MESSAGE, message);
        args.putInt(StaticValues.DIALOG_TYPE, dialogType);
        dialogs.setArguments(args);
        return dialogs;
    }

     @NonNull@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog=null;
         String message="";
         int dialogType=0;
        if(getArguments()!=null) {
            message=getArguments().getString(StaticValues.DIALOG_MESSAGE, "");
            dialogType = getArguments().getInt(StaticValues.DIALOG_TYPE, 0);
        }
        if(dialogType== StaticValues.DIALOG_TYPE_SYSTEM_ALERT){
            dialog= getSystemAlert(message);
        }
        else if(dialogType== StaticValues.DIALOG_TYPE_YES_NO){
            dialog= getYesNoDialog(message);
        }

        return dialog;

    }

    private Dialog getSystemAlert(String message) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.show();
        ((TextView) dialog.findViewById(R.id.txtTitle)).setText(message);
        dialog.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send the positive button event back to the host activity
                ((DialogsInterface) getActivity()).onDialogPositiveClick(Dialogs.this);
            }
        });
        dialog.findViewById(R.id.btnNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send the negative button event back to the host activity
                ((DialogsInterface) getActivity()).onDialogNegativeClick(Dialogs.this);
            }
        });

        dialog.setCancelable(false);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;

        return dialog;
    }
    /*
    private Dialog getOkDialog(String message) {
        Dialog confirmDialog = new Dialog(getActivity());
        confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confirmDialog.setContentView(R.layout.message);
        confirmDialog.show();
        ((TextView) confirmDialog.findViewById(R.id.txtTitle)).setText(message);
        confirmDialog.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send the positive button event back to the host activity
                ((DialogsInterface) getActivity()).onDialogPositiveClick(Dialogs.this);
            }
        });

        return confirmDialog;
    }
*/
    private Dialog getYesNoDialog(String message) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.show();
        ((TextView) dialog.findViewById(R.id.txtTitle)).setText(message);
        dialog.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send the positive button event back to the host activity
                ((DialogsInterface) getActivity()).onDialogPositiveClick(Dialogs.this);
            }
        });
        dialog.findViewById(R.id.btnNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send the negative button event back to the host activity
                ((DialogsInterface) getActivity()).onDialogNegativeClick(Dialogs.this);
            }
        });
        return dialog;
    }

}