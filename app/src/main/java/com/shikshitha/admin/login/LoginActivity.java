package com.shikshitha.admin.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.shikshitha.admin.R;
import com.shikshitha.admin.dao.ServiceDao;
import com.shikshitha.admin.dao.TeacherDao;
import com.shikshitha.admin.group.GroupActivity;
import com.shikshitha.admin.model.Credentials;
import com.shikshitha.admin.model.TeacherCredentials;
import com.shikshitha.admin.fcm.FCMIntentService;
import com.shikshitha.admin.util.SharedPreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginView{
    @BindView(R.id.login_id_et)
    EditText loginId;
    @BindView(R.id.password_et) EditText password;
    @BindView(R.id.login_id)
    TextInputLayout loginLayout;
    @BindView(R.id.password) TextInputLayout passwordLayout;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        presenter = new LoginPresenterImpl(this);
    }

    public void login(View view) {
        if(validate()) {
            Credentials c = new Credentials();
            c.setUsername(loginId.getText().toString());
            c.setPassword(password.getText().toString());
            presenter.validateCredentials(c);
        }
    }

    public void forgotPassword(View view) {
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if(loginId.getText().toString().length() != 10) {
            loginLayout.setError(getString(R.string.valid_mobile));
        } else {
            presenter.pwdRecovery(loginId.getText().toString());
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        showSnackbar(message);
    }

    @Override
    public void pwdRecovered() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Password has been sent to your registered mobile");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void noUser() {
        showSnackbar(getString(R.string.no_user));
    }

    @Override
    public void saveUser(TeacherCredentials teacherCredentials) {
        TeacherDao.clear();
        TeacherDao.insert(teacherCredentials.getTeacher());
        ServiceDao.clear();
        ServiceDao.insert(teacherCredentials.getService());
        teacherCredentials.setMobileNo(loginId.getText().toString());
        SharedPreferenceUtil.saveTeacher(this, teacherCredentials);
        startService(new Intent(this, FCMIntentService.class));
    }

    @Override
    public void navigateToDashboard() {
        startActivity(new Intent(this, GroupActivity.class));
        finish();
    }

    public boolean validate(){
        if(loginId.getText().toString().isEmpty()){
            loginLayout.setError(getString(R.string.username_error));
            return false;
        } else if (loginId.getText().toString().length() != 10) {
            loginLayout.setError(getString(R.string.valid_mobile));
            return false;
        }else if (password.getText().toString().isEmpty() ||
                password.getText().toString().length() < 6) {
            passwordLayout.setError(getString(R.string.valid_password));
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
