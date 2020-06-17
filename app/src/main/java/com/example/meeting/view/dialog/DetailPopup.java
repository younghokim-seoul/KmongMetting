package com.example.meeting.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.core.widget.NestedScrollView;

import com.example.meeting.R;
import com.example.meeting.utils.DisplayUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailPopup {

    @BindView(R.id.popup_default_content)
    TextView contentTextview;
    @BindView(R.id.popup_default_confirm_button)
    Button confirmButton;

    @BindView(R.id.svList)
    NestedScrollView svList;

    private Context context;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private View inflatedView;

    private DetailPopup.OnClickListener listener;

    public DetailPopup(Context context) {
        this.context = context;
        this.builder = new AlertDialog.Builder(context);
        this.builder.setCancelable(true);
        this.inflatedView = LayoutInflater.from(context).inflate(R.layout.view_popup_detail, null);
        ButterKnife.bind(this, inflatedView);

    }

    @OnClick(R.id.popup_default_confirm_button)
    void onClickedConfirm(View view) {
        if (listener != null) {
            listener.onClick(view, dialog);
        }
    }

    public void show() {
        int defaultSidePadding = (int) DisplayUtils.convertDpToPixel(context, 10);
        this.dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setView(inflatedView, defaultSidePadding, defaultSidePadding, defaultSidePadding, defaultSidePadding);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        if (context != null && !((Activity) context).isFinishing()) {
            dialog.show();
        }
    }

    private void setHeight(){
        svList.measure(0,0);
        if (svList.getMeasuredHeight() > DisplayUtils.dpToPx(context, 300)) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, (int) DisplayUtils.dpToPx(context, 300));
            svList.setLayoutParams(lp);
        }
    }

    public DetailPopup setContentText(String content) {
        this.contentTextview.setText(content);
        setHeight();
        return this;
    }

    public DetailPopup setConfirmButtonText(String content) {
        this.confirmButton.setText(content);

        return this;
    }

    public DetailPopup setConfirmButtonListener(OnClickListener val) {
        this.listener = val;

        return this;
    }

    public interface OnClickListener {
        void onClick(View view, AlertDialog dialog);
    }
}
