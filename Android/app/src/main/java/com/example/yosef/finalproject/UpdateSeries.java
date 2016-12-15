package com.example.yosef.finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

public class UpdateSeries extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageViewCard1;
    private ImageView imageViewCard2;
    private ImageView imageViewCard3;
    private ImageView imageViewCard4;

    private Button buttonChooseCard1;
    private Button buttonChooseCard2;
    private Button buttonChooseCard3;
    private Button buttonChooseCard4;

    private Boolean isbuttonChooseCard1=false;
    private Boolean isbuttonChooseCard2=false;
    private Boolean isbuttonChooseCard3=false;
    private Boolean isbuttonChooseCard4=false;

    private Boolean imageCoosen;
    private int PICK_IMAGE_REQUEST = 1;

    private Button buttonUpdateSeries;

    private String imageName1="";
    private String imageName2="";
    private String imageName3="";
    private String imageName4="";

    private TextView category_name;
    private EditText category;
    private EditText card1;
    private EditText card2;
    private EditText card3;
    private EditText card4;
    int myId;
    private Uri filePath;
    private File imageFile;
    private Bitmap bitmap;


    Series selctedSeries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_series);
        buttonUpdateSeries=(Button)findViewById(R.id.ubuttonUpdateSeries);

        buttonChooseCard1 = (Button) findViewById(R.id.ubuttonChooseCard1);
        buttonChooseCard2 = (Button) findViewById(R.id.ubuttonChooseCard2);
        buttonChooseCard3 = (Button) findViewById(R.id.ubuttonChooseCard3);
        buttonChooseCard4 = (Button) findViewById(R.id.ubuttonChooseCard4);

        //category=(EditText)findViewById(R.id.series);
        category_name=(TextView)findViewById(R.id.ucategoryName);
        card1=(EditText)findViewById(R.id.ucard1);
        card2=(EditText)findViewById(R.id.ucard2);
        card3=(EditText)findViewById(R.id.ucard3);
        card4=(EditText)findViewById(R.id.ucard4);

        imageViewCard1 = (ImageView) findViewById(R.id.uimageViewCard1);
        imageViewCard2 = (ImageView) findViewById(R.id.uimageViewCard2);
        imageViewCard3 = (ImageView) findViewById(R.id.uimageViewCard3);
        imageViewCard4 = (ImageView) findViewById(R.id.uimageViewCard4);

        selctedSeries=(Series)getIntent().getSerializableExtra("SelectedSeries");
        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(this);
        myId = myPref.getInt("user_id", 0);
        if(myId==0)
            Toast.makeText(this,"user id is null",Toast.LENGTH_SHORT).show();

        category_name.setText(selctedSeries.getCategory_name());
        card1.setText(selctedSeries.getCard_name1());
        card2.setText(selctedSeries.getCard_name2());
        card3.setText(selctedSeries.getCard_name3());
        card4.setText(selctedSeries.getCard_name4());

        ImageLoader imageLoader = new ImageLoader(this);

        imageLoader.DisplayImage((ServerUtils.imageRelativePat + selctedSeries.getImage1()), R.mipmap.ic_launcher, imageViewCard1);
        imageLoader.DisplayImage((ServerUtils.imageRelativePat + selctedSeries.getImage2()), R.mipmap.ic_launcher, imageViewCard2);
        imageLoader.DisplayImage((ServerUtils.imageRelativePat + selctedSeries.getImage3()), R.mipmap.ic_launcher, imageViewCard3);
        imageLoader.DisplayImage((ServerUtils.imageRelativePat + selctedSeries.getImage4()), R.mipmap.ic_launcher, imageViewCard4);

        /*imageName1=selctedSeries.getImage1();
        imageName2=selctedSeries.getImage2();
        imageName3=selctedSeries.getImage3();
        imageName4=selctedSeries.getImage4();*/


        buttonChooseCard1.setOnClickListener(this);
        buttonChooseCard2.setOnClickListener(this);
        buttonChooseCard3.setOnClickListener(this);
        buttonChooseCard4.setOnClickListener(this);
        imageViewCard1.setOnClickListener(this);
        imageViewCard2.setOnClickListener(this);
        imageViewCard3.setOnClickListener(this);
        imageViewCard4.setOnClickListener(this);
        buttonUpdateSeries.setOnClickListener(this);



    }

    public void onBackPressed() {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.back_Main_Menu))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    finish();                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,getResources().getString(R.string.Select_Picture)), PICK_IMAGE_REQUEST);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            imageFile=new File(filePath.getPath());
            Bitmap rotatedBitmap=null;

            Matrix matrix = new Matrix();
            matrix.postRotate(180);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                if(isbuttonChooseCard1) {
                    imageName1=getRealPathFromURI_BelowAPI11(getBaseContext(),filePath);
                    //file1= new File(Environment.getExternalStorageDirectory().getAbsolutePath(),imageFile.getPath());
                    imageViewCard1.setImageBitmap(rotatedBitmap);
                    isbuttonChooseCard1=false;
                }
                else if(isbuttonChooseCard2) {
                    imageName2=getRealPathFromURI_BelowAPI11(getBaseContext(),filePath);
                    //file2= new File(Environment.getExternalStorageDirectory().getAbsolutePath(),imageFile.getPath());

                    imageViewCard2.setImageBitmap(rotatedBitmap);
                    isbuttonChooseCard2=false;
                }
                else if(isbuttonChooseCard3) {
                    imageName3=getRealPathFromURI_BelowAPI11(getBaseContext(),filePath);
                    //file3= new File(Environment.getExternalStorageDirectory().getAbsolutePath(),imageFile.getPath());

                    imageViewCard3.setImageBitmap(rotatedBitmap);
                    isbuttonChooseCard3=false;
                }
                else if(isbuttonChooseCard4) {
                    imageName4=getRealPathFromURI_BelowAPI11(getBaseContext(),filePath);
                    //file4= new File(Environment.getExternalStorageDirectory().getAbsolutePath(),imageFile.getPath());
                    imageViewCard4.setImageBitmap(rotatedBitmap);
                    isbuttonChooseCard4=false;
                }

                imageCoosen=true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getRealPathFromURI_BelowAPI11(Context contex, Uri uri){

        String name="";
        if (uri.getHost().contains("com.android.providers.media")) {
            // Image pick from recent
            String wholeID = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = getBaseContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                name = cursor.getString(columnIndex);
            }
            cursor.close();
        }else {
            // image pick from gallery
            getRealPathFromURI_BelowAPI11(contex,uri);
        }
        return name;


    }

    public void onClick(View v) {
            if (v == buttonChooseCard1) {
            isbuttonChooseCard1=true;
            showFileChooser();
        }else if(v ==buttonChooseCard2){
            isbuttonChooseCard2=true;
            showFileChooser();
        }else if(v ==buttonChooseCard3){
            isbuttonChooseCard3=true;
            showFileChooser();
        }else if(v ==buttonChooseCard4){
            isbuttonChooseCard4=true;
            showFileChooser();
        }else if(v==buttonUpdateSeries){
                if(chekIfAllParmInit()) {
                        new UpdateSeriesTask().execute(category_name.getText().toString(),
                                card1.getText().toString(),
                                card2.getText().toString(),
                                card3.getText().toString(),
                                card4.getText().toString());

                }else
                    Toast.makeText(UpdateSeries.this,
                            getResources().getString(R.string.empty_field),
                            Toast.LENGTH_SHORT).show();
        }else if(v==imageViewCard1){
                ImageView image = (ImageView)findViewById(R.id.uimageViewCard1);
                showImageInDialog(image);
            }else if(v==imageViewCard2){
                ImageView image = (ImageView)findViewById(R.id.uimageViewCard2);
                showImageInDialog(image);
            }else if(v==imageViewCard3){
                ImageView image = (ImageView)findViewById(R.id.uimageViewCard3);
                showImageInDialog(image);
            }else if(v==imageViewCard4){
                ImageView image = (ImageView)findViewById(R.id.uimageViewCard4);
                showImageInDialog(image);
            }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public class UpdateSeriesTask  extends AsyncTask<String, Void, Boolean> {

        LinkedHashMap<String,String> parms=new LinkedHashMap<>();
        @Override
        protected Boolean doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(ServerUtils.UpdateSeries);

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            entityBuilder.addTextBody("category_id", String.valueOf(selctedSeries.getCategory_id()));
            entityBuilder.addTextBody("category_name", params[0]);
            entityBuilder.addTextBody("user_id",String.valueOf(myId));
            entityBuilder.addTextBody("card1", params[1]);
            entityBuilder.addTextBody("card2", params[2]);
            entityBuilder.addTextBody("card3", params[3]);
            entityBuilder.addTextBody("card4", params[4]);

            entityBuilder.addPart("image1",new FileBody(new File(imageName1.equals("")?getEmptyFile():imageName1)));
            entityBuilder.addPart("image2",new FileBody(new File(imageName2.equals("")?getEmptyFile():imageName2)));
            entityBuilder.addPart("image3",new FileBody(new File(imageName3.equals("")?getEmptyFile():imageName3)));
            entityBuilder.addPart("image4",new FileBody(new File(imageName4.equals("")?getEmptyFile():imageName4)));
            httppost.setEntity(entityBuilder.build());
            try {
                HttpResponse response = httpclient.execute(httppost);
                String json= EntityUtils.toString(response.getEntity());
                if(response.getStatusLine().getStatusCode()==200){
                    if(json.equals("1"))
                        return true;
                    else
                        return false;
                }else
                    return false;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        protected void onPostExecute(Boolean result) {
            Intent resultIntent = getIntent();

            if(result) {
                Toast.makeText(UpdateSeries.this,"Update successes",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(UpdateSeries.this,MainMenu.class);
                startActivity(intent);
                finish();
                //setResult(RESULT_OK, resultIntent);
            }
            else{
                Toast.makeText(UpdateSeries.this,"Update failed",Toast.LENGTH_SHORT).show();
                finish();
            }






        }
    }

    private Boolean chekIfAllParmInit(){
        if(card1.getText().toString()==""||
                card2.getText().toString()==""||
                card3.getText().toString()==""||
                card4.getText().toString()=="")
            return false;


        if(imageViewCard1.getDrawable()==null||
                imageViewCard2.getDrawable()==null||
                imageViewCard3.getDrawable()==null||
                imageViewCard4.getDrawable()==null)
            return false;


        return true;

    }

    public void showImageInDialog(ImageView imageView){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.zoom_image_layout, null);

        ImageView imageDialog = (ImageView) dialogLayout.findViewById(R.id.dialogImage);
        Bitmap icon = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        imageDialog.setImageBitmap(icon);



        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.show();
    }

    public String getEmptyFile(){
        try {
            String  h = DateFormat.format("MM-dd-yyyyy-h-mmssaa", System.currentTimeMillis()).toString();
            // this will create a new name everytime and unique
            File file = new File(this.getFilesDir(), "text.txt");

           /* File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            // if external memory exists and folder with name Notes
            if (!root.exists()) {
                root.mkdirs(); // this will create folder.
            }
            File filepath = new File(root, h + ".txt");  // file path to save*/
            FileWriter writer = new FileWriter(file);
            writer.flush();
            writer.close();
            return file.toString();
           // String m = "File generated with name " + h + ".txt";
            //return h+=".txt";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }

}
