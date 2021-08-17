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

import com.ahtc.demoretrofit.net.retrofit.RetrofitInstance;
import com.ahtc.demoretrofit.net.retrofit.RetrofitServices;
import com.ahtc.demoretrofit.net.retrofit.model.response.CSVUploadResponse;
import com.ahtc.demoretrofit.util.ExportCSV;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private int PERMISSION = 100;
    private String[] permissions = {
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

    private void initialize() {
        ExportCSV exportCSV = new ExportCSV(this);
        if(exportCSV.isCSVExist()) {
            sendCSVToServer(exportCSV.getCsv());
        } else {
            showMessage("El archivo no fue generado");
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
                showMessage("A fallado el envio de los datos");
                btn.setEnabled(true);
            }
        });
    }

    private void showMessage(String message) {
        message = (message.isEmpty()) ? "No hay mensaje que mostrar" : message;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    private void verificPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            showPermissionRequest();
        }
    }

    private void showPermissionRequest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Información");
        builder.setMessage("A continuación se mostrara un cuadro de dialogo donde se le pedira que autorize los permisos de manejo de medios, por favor aceptelos para que el aplicativo pueda seguir trabajando de manera adeucada...");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
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
            initialize();
        }
    }
}