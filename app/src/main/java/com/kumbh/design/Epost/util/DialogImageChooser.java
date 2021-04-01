package com.kumbh.design.Epost.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kumbh.design.Epost.R;

public class DialogImageChooser extends BottomSheetDialogFragment {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    View view;
    //    DialogNationalitySelectionBinding mBinding;
    Context mContext;
    private bottomSheetListener mListener;
    TextView tvClickImageText, tvChooseGalleryText;

    public DialogImageChooser() {
    }

    @SuppressLint("ValidFragment")
    public DialogImageChooser(@NonNull Context context, bottomSheetListener mListener) {
        this.mListener = mListener;
        mContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.image_choose_bottom_sheet, container, false);

        tvClickImageText = view.findViewById(R.id.tvClickImageText);
        tvChooseGalleryText = view.findViewById(R.id.tvChooseGalleryText);

        tvClickImageText.setOnClickListener(v -> {
            mListener.onCardClicked("camera");
            dismiss();
        });

        tvChooseGalleryText.setOnClickListener(v -> {
            mListener.onCardClicked("gallery");
            dismiss();
        });

        return view;
    }


    public interface bottomSheetListener {
        void onCardClicked(String option);
    }
}