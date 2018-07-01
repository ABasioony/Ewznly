package domain.ewznly;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class PreviewActivity extends AppCompatActivity {
    private TextView textView;
    private Button Ana_BTN;
    private Button Url_BTN;
    private ImageView imageView;
    private int serverResponseCode = 0;
    private ProgressDialog dialog = null;

    private String upLoadServerUri = null;
    private String imagepath = null;
    FirebaseStorage storage;
    String name;
    StorageReference storageReference;
    Uri file;
    String generatedFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Bundle extra = getIntent().getExtras();
        final String FilePath;
        if (extra != null) {
            file = Uri.parse(extra.getString("ImageUri"));
            FilePath = extra.getString("Image");
            name = extra.getString("name");
        }
        final StorageReference ref = storageReference.child("images/" + name);
        imageView = findViewById(R.id.imageview);
        imageView.setImageURI(file);
        textView = findViewById(R.id.TxtView);
        Ana_BTN = findViewById(R.id.AnalysisBTN);
        Url_BTN = findViewById(R.id.get_url);
        textView.setText("Result is ");
        Url_BTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot uri) {
                        textView.setText(uri..toString);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }
        });
        Ana_BTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (file != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(PreviewActivity.this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    ref.putFile(file)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    Toast.makeText(PreviewActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(PreviewActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });

                }
            }

        });

    }

}
