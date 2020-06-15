package com.example.meeting.view.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meeting.App;
import com.example.meeting.AppContainer;

import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;


//공통된 부분을 사용하기위한 baseActivity 공통적인 함수를 매번호출하면 보일러플레이트 코드발생으로인해 공용으로 들어갈 기능들은 base 에정의한다.
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {
    protected P presenter;
    protected AppContainer appContainer;
    private Unbinder unbinder;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContainer = ((App) getApplication()).getAppContainer();
        presenter = getPresenter();
    }

    public void setUnBinder(Unbinder unBinder) {
        unbinder = unBinder;
    }



    @Override
    protected void onDestroy() {

        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
        compositeDisposable.clear();
        super.onDestroy();
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    public AppContainer getAppContainer() {
        return appContainer;
    }

    protected abstract P getPresenter();

    public void hideKeyboard() {
        View currentFocus = getCurrentFocus();
        if (currentFocus == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
