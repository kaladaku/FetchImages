package com.images.fetch.fetchimages;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    ImageView img_gal;
    Button btn_fetch,btn_rename,btn_delete;
    private static int RESULT_LOAD_IMG = 1;
    EditText et_fileName;
    TextView tv_displayName;
    public static final String tag="1";
    String imgDecodableString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img_gal=(ImageView)findViewById(R.id.imageView);
        btn_fetch=(Button)findViewById(R.id.fetch);
        btn_rename=(Button)findViewById(R.id.rename);
        btn_delete=(Button)findViewById(R.id.delete);
        et_fileName=(EditText)findViewById(R.id.editText);
        tv_displayName=(TextView)findViewById(R.id.path);
        et_fileName.setVisibility(View.INVISIBLE);
    }

    public void Fetch(View view)
    {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);


    }


    public void Rename(View view)
    {


        String user_file=et_fileName.getText().toString();
        File   from = new File(imgDecodableString);
        String par= from.getParent();
        File to=new File(par+"/"+user_file+".jpg");
        boolean res= from.renameTo(to);


        String to_path= to.getPath();
        tv_displayName.setText(to_path);


        if (from.exists()) {
            if (from.delete()) {
            }
            else {

            }
        }



        MediaScannerConnection.scanFile(this, new String[] {to.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String path, Uri uri) {

            }
        });

        MediaScannerConnection.scanFile(this, new String[] {from.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String path, Uri uri) {

//                    tv_displayName.setText(path);
            }
        });




    }


    public void Delete(View view)
    {

        File fdelete = new File(imgDecodableString);
        if (fdelete.exists())
        {
            if (fdelete.delete())
            {
                Toast.makeText(this,"File Deleted:"+fdelete.getName(),Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this,"File Not Deleted:",Toast.LENGTH_LONG).show();
            }
        }

        MediaScannerConnection.scanFile(this, new String[] {fdelete.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String path, Uri uri) {

//                    tv_displayName.setText(path);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();


                BitmapFactory.Options bmo=new BitmapFactory.Options();
                bmo.inSampleSize=4;
                // Set the Image in ImageView after decoding the String

                Bitmap b1=new BitmapFactory().decodeFile(imgDecodableString,bmo);
                img_gal.setImageBitmap(b1);

                //img_gal.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                tv_displayName.setText(imgDecodableString);
                et_fileName.setVisibility(View.VISIBLE);


            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }

}