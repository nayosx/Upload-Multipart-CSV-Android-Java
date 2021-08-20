package com.ahtc.demoretrofit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ahtc.demoretrofit.retrofit.RetrofitInstance;
import com.ahtc.demoretrofit.retrofit.RetrofitServices;
import com.ahtc.demoretrofit.retrofit.model.response.CSVUploadResponse;
import com.ahtc.demoretrofit.util.ExportCSVFaker;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final int PERMISSION = 100;
    private final String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.button);
        verificPermission();
    }

    public void send(View v) {
        btn.setEnabled(false);
        initialize();
    }

    private void verificPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            showPermissionRequest();
        }
    }

    private void showPermissionRequest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Information");
        builder.setMessage("Next, a dialog box will be shown where you will be asked to authorize the media handling permissions, please accept them so that the application can continue working properly ...");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getPermissions();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void getPermissions() {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION) {
            showMessage("permissions granted");
        }
    }

    private void showMessage(String message) {
        message = (message.isEmpty()) ? "There is no message to show" : message;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void initialize() {
        ExportCSVFaker exportCSVFaker = new ExportCSVFaker(this);
        if(exportCSVFaker.isCSVExist()) {
            sendCSVToServer(exportCSVFaker.getCsv());
        } else {
            showMessage("The file was not generated");
        }
    }

    private void sendCSVToServer(File csv) {
        MultipartBody.Part filePart = MultipartBody.Part.createFormData(
                "uploadFile",
                csv.getName(),
                RequestBody.create(MediaType.parse("text/plain" + "; charset=utf-8"), csv)
        );
        RetrofitServices retroServices = RetrofitInstance.getInstance().create(RetrofitServices.class);
        Call<CSVUploadResponse> responseCall = retroServices.uploadAttachment(filePart);

        responseCall.enqueue(new Callback<CSVUploadResponse>() {
            @Override
            public void onResponse(Call<CSVUploadResponse> call, Response<CSVUploadResponse> response) {
                showMessage(response.message());
                btn.setEnabled(true);
            }

            @Override
            public void onFailure(Call<CSVUploadResponse> call, Throwable t) {
                t.printStackTrace();
                showMessage("The sending of the data has failed");
                btn.setEnabled(true);
            }
        });
    }
}