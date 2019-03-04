package aiwac.admin.com.healthrobot.activity.skin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import aiwac.admin.com.healthrobot.R;


public class ViewDialogFragment extends DialogFragment {

    public interface Callback{
        void onClick(int which);
    }

    private Callback callback;

    public void show(FragmentManager fragmentManager){
        show(fragmentManager, "ViewDialogFragment");
    }

    @Override
    public void onStart(){
        super.onStart();
        Dialog dialog=getDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_view_dialog, null);
        builder.setView(view);
        Button selfTestBtn = view.findViewById(R.id.selftestbtn);
        Button otherTestBtn = view.findViewById(R.id.othertestbtn);
       // LinearLayout fromFileBtn = view.findViewById(R.id.fromfilebtn);



        selfTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null){
                    callback.onClick(1);
                    dismiss();
                }
            }
        });
        otherTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null){
                    callback.onClick(2);
                    dismiss();
                }
            }
        });
      //  fromFileBtn.setOnClickListener(new View.OnClickListener() {
      //      @Override
      //      public void onClick(View view) {
      //          if (callback != null){
      //              callback.onClick(3);
      //              dismiss();
       //         }
       //     }
      //  });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback){
            callback = (Callback) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement Callback");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }



}
