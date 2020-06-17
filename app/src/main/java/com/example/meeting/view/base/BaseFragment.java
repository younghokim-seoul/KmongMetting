package com.example.meeting.view.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseFragment extends Fragment {

    @LayoutRes
    public abstract int contentViewLayoutId();

    protected abstract void setUp(View view);

    private Context context;
    private Unbinder unbinder;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(contentViewLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (context == null) {
            context = requireContext();
        }

        unbinder = ButterKnife.bind(this, view);
        setUp(view);
    }

    @Override
    public void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Nullable
    @Override
    public Context getContext() {
        if (context != null) {
            return context;
        }
        return super.getContext();
    }


    public static <T extends Fragment> T newInstance(T fragment) {
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }
}
