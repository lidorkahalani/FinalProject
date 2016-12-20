package com.myApplication.yosef.finalproject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.http.multipart.MultipartEntity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.EntityUtilsHC4;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.myApplication.yosef.finalproject.ServerUtils;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class AddNewSeries extends AppCompatActivity implements View.OnClickListener {

    public static final String UPLOAD_KEY = "image";
    public static final String TAG = "MY MESSAGE";

    private int PICK_IMAGE_REQUEST = 1;

    //private Button buttonChoose;
    private Button buttonUploadSeries;
    private User currentPlayer;


    private Button buttonUpload;
    private Game game;

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

    private TextView category;
    private EditText card1;
    private EditText card2;
    private EditText card3;
    private EditText card4;
    private Boolean upload_image_status;
    private String cardName;
    private TextView categoryName;
    private int picCnt=0;

    Bitmap bitmapResize1;
    Bitmap bitmapResize2;
    Bitmap bitmapResize3;
    Bitmap bitmapResize4;

    static private ArrayList<Card>newSeries=new ArrayList<>();

    private Bitmap bitmap;
    private File imageFile;
    private boolean imageCoosen;
    private static int category_id;

    private File file1;
    private File file2;
    private File file3;
    private File file4;

    private String imageName1;
    private String imageName2;
    private String imageName3;
    private String imageName4;

    private String charset = "UTF-8";
    private HttpURLConnection conn;
    private DataOutputStream wr;
    private StringBuilder result;
    private URL urlObj;
    private JSONObject jObj = null;
    private StringBuilder sbParams;
    private String paramsString;
    final int MY_PERMISSIONS_REQUEST_READ_STORAGE =1;
    ContentType contentType= ContentType.create("text/plain", Charset.forName("UTF-8"));
    //ContentType contentType = ContentType.create(
    //        HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);



    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_series);
        game=(Game) getIntent().getSerializableExtra("Game");
        buttonChooseCard1 = (Button) findViewById(R.id.buttonChooseCard1);
        buttonChooseCard2 = (Button) findViewById(R.id.buttonChooseCard2);
        buttonChooseCard3 = (Button) findViewById(R.id.buttonChooseCard3);
        buttonChooseCard4 = (Button) findViewById(R.id.buttonChooseCard4);

        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        //buttonUploadSeries=(Button) findViewById(R.id.buttonUploadSeries);
        category=(TextView)findViewById(R.id.series);
        card1=(EditText)findViewById(R.id.card1);
        card2=(EditText)findViewById(R.id.card2);
        card3=(EditText)findViewById(R.id.card3);
        card4=(EditText)findViewById(R.id.card4);

        categoryName=(TextView)findViewById(R.id.categoryName);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Matias_Webfont.ttf");
        buttonChooseCard1.setTypeface(typeface);
        buttonChooseCard2.setTypeface(typeface);
        buttonChooseCard3.setTypeface(typeface);
        buttonChooseCard4.setTypeface(typeface);
        buttonUpload.setTypeface(typeface);
        currentPlayer=(User)getIntent().getSerializableExtra("currenUsrt");

        getMaxCategoryId();

        imageViewCard1 = (ImageView) findViewById(R.id.imageViewCard1);
        imageViewCard2 = (ImageView) findViewById(R.id.imageViewCard2);
        imageViewCard3 = (ImageView) findViewById(R.id.imageViewCard3);
        imageViewCard4 = (ImageView) findViewById(R.id.imageViewCard4);

        categoryName.setText(getIntent().getStringExtra("categoryName"));
        buttonChooseCard1.setOnClickListener(this);
        buttonChooseCard2.setOnClickListener(this);
        buttonChooseCard3.setOnClickListener(this);
        buttonChooseCard4.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        imageViewCard1.setOnClickListener(this);
        imageViewCard2.setOnClickListener(this);
        imageViewCard3.setOnClickListener(this);
        imageViewCard4.setOnClickListener(this);

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

            //deteect the oriention and resize degree
            ExifInterface ei = null;
            try {
                ei = new ExifInterface(imageFile.getAbsolutePath());
                matrix.postRotate(setOriention(ei));
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {

                bitmap=Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), filePath),200,200,true);
                rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                if(isbuttonChooseCard1) {
                    bitmapResize1=bitmap;
                    imageName1=getImageNameFromUriAcordingApi(data);
                    String[] name=imageName1.split("/");

                    //file1 = new File(imageName1);
                    file1 =convertBitmapToFile(bitmapResize1,name[name.length-1]);
                    imageViewCard1.setImageBitmap(rotatedBitmap);
                    isbuttonChooseCard1=false;
                }
                else if(isbuttonChooseCard2) {
                  //  bitmapResize2=Bitmap.createScaledBitmap(rotatedBitmap,200,200,true);
                    bitmapResize2=bitmap;
                    imageName2=getImageNameFromUriAcordingApi(data);
                    String[] name=imageName2.split("/");

                   // file2= new File(Environment.getExternalStorageDirectory().getAbsolutePath(),imageFile.getPath());
                    //file2= new File(imageName2);
                    file2 =convertBitmapToFile(bitmapResize2,name[name.length-1]);


                    imageViewCard2.setImageBitmap(rotatedBitmap);
                    isbuttonChooseCard2=false;
                }
                else if(isbuttonChooseCard3) {
                 //   bitmapResize3=Bitmap.createScaledBitmap(rotatedBitmap,200,200,true);
                    bitmapResize3=bitmap;
                    imageName3=getImageNameFromUriAcordingApi(data);
                    String[] name=imageName3.split("/");

                    //file3= new File(Environment.getExternalStorageDirectory().getAbsolutePath(),imageFile.getPath());
                    //file3=new File(imageName3);
                    file3 =convertBitmapToFile(bitmapResize3,name[name.length-1]);



                    imageViewCard3.setImageBitmap(rotatedBitmap);
                    isbuttonChooseCard3=false;
                }
                else if(isbuttonChooseCard4) {
                    //bitmapResize4=Bitmap.createScaledBitmap(rotatedBitmap,200,200,true);
                    bitmapResize4=bitmap;
                    imageName4=getImageNameFromUriAcordingApi(data);
                    String[] name=imageName4.split("/");

                    // file4=new File(imageName4);
                    file4 =convertBitmapToFile(bitmapResize4,name[name.length-1]);

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

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void getMaxCategoryId() {
        final int[] maxId = {0};
        class GetMaxCategoryId extends AsyncTask<String, Void, Integer> {
            LinkedHashMap<String, String> parms = new LinkedHashMap<>();
            @Override
            protected Integer doInBackground(String... params) {
                JSONParser json = new JSONParser();
                try {
                    JSONObject response = json.makeHttpRequest( ServerUtils.getMaxCategoryId_url, "GET", parms);
                    if(response.getInt("succsses")==1)
                    return response.getInt("max_category_id");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return 0;
            }

            protected void onPostExecute(Integer s) {
                if(s!=0)
                    category_id =(s+1);
            }
        }
        GetMaxCategoryId g= new GetMaxCategoryId();
        g.execute();

    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,Boolean>{
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddNewSeries.this,
                        getResources().getString(R.string.string_upload_image),
                        getResources().getString(R.string.please_wait),true,true);
            }

            @Override
            protected void onPostExecute(Boolean s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s) {
                    upload_image_status = true;
                    Toast.makeText(AddNewSeries.this,
                            getResources().getString(R.string.upload_image_success),
                            Toast.LENGTH_LONG).show();
                }else{
                    upload_image_status = false;
                    Toast.makeText(AddNewSeries.this,
                            getResources().getString(R.string.upload_image_failed),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected Boolean doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];

                String uploadImage = getStringImage(bitmap);
                LinkedHashMap<String,String> data = new LinkedHashMap<>();
                data.put("card_name",cardName);
                data.put("category_id",String.valueOf( category_id));
                data.put("user_id",String.valueOf(currentPlayer.getUserID()));
                //cant send image as FIle
                //data.put("uploaded_file", imageFile);
                JSONParser json = new JSONParser();
                //String result = rh.sendPostRequest(UPLOAD_URL,data);
                JSONObject response = json.makeHttpRequest(ServerUtils.UPLOAD_URL, "POST", data);
                try {
                    if (response.getInt("succsses") == 1) {
                        return true;
                    }else
                         return false;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    public void onBackPressed() {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.back_Main_Menu))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        //new setGameToInactive().execute(String.valueOf(game.getGame_id()));
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
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
        }else if(v==imageViewCard1){
            ImageView image = (ImageView)findViewById(R.id.imageViewCard1);
            showImageInDialog(image);
        }else if(v==imageViewCard2){
            ImageView image = (ImageView)findViewById(R.id.imageViewCard2);
            showImageInDialog(image);
        }else if(v==imageViewCard3){
            ImageView image = (ImageView)findViewById(R.id.imageViewCard3);
            showImageInDialog(image);
        }else if(v==imageViewCard4){
            ImageView image = (ImageView)findViewById(R.id.imageViewCard4);
            showImageInDialog(image);
        }




        if(v == buttonUpload){

            if(chekIfAllParmInit()){//chekIfAllParmInit() {
               // Toast.makeText(this,"All good",Toast.LENGTH_SHORT).show();
                new sendSeriesToServer().execute(categoryName.getText().toString(),card1.getText().toString(),
                                                                                card2.getText().toString(),
                                                                                card3.getText().toString(),
                                                                                card4.getText().toString());

            }
            else
                Toast.makeText(AddNewSeries.this,
                        getResources().getString(R.string.empty_field),
                        Toast.LENGTH_SHORT).show();
        }if (v==buttonUploadSeries){
            uploadImage();
            if(upload_image_status)
                addThisCardToLocalArray();
            sendSeries();
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

    public void addThisCardToLocalArray(){
        if(newSeries.size()<4) {
            int defuletColor = 111;
            String cardsList[] = new String[4];
            cardsList[0] = card1.getText().toString();
            cardsList[1] = card2.getText().toString();
            cardsList[2] = card3.getText().toString();
            cardsList[3] = card4.getText().toString();

            Card card = new Card(category.getText().toString(),
                    card1.getText().toString(),
                    defuletColor, filePath.toString(), cardsList);

            newSeries.add(card);
            //refrashe screen
           // StartActivity(getIntent());

        }else {
            Toast.makeText(this, getResources().getString(R.string.series_full), Toast.LENGTH_LONG).show();
        }
    }

    public void sendSeries(){
        if(newSeries.size()==4){
            new sendSeriesToServer().execute();
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
        }else if(uri.getHost().contains("com.android.externalstorage.documents")){

        }
        else {
            // image pick from gallery
            getRealPathFromURI_BelowAPI11(contex,uri);
        }
        return name;


    }

    public class sendSeriesToServer extends AsyncTask<String, Void, Boolean> {
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();
        File imageFile1;
        File imageFile2;
        File imageFile3;
        File imageFile4;
        Boolean isAllImage=true;
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            loading = ProgressDialog.show(AddNewSeries.this,
                    getResources().getString(R.string.string_upload_image),
                    getResources().getString(R.string.please_wait),true,true);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(ServerUtils.upload_series);

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            entityBuilder.addTextBody("category_id", String.valueOf(category_id));
            entityBuilder.addTextBody("category_name", params[0],contentType);
            entityBuilder.addTextBody("user_id",String.valueOf(currentPlayer.getUserID()));
            entityBuilder.addTextBody("card1", params[1],contentType);
            entityBuilder.addTextBody("card2", params[2],contentType);
            entityBuilder.addTextBody("card3", params[3],contentType);
            entityBuilder.addTextBody("card4", params[4],contentType);
            entityBuilder.addPart("image1",new FileBody(file1));
            entityBuilder.addPart("image2",new FileBody(file2));
            entityBuilder.addPart("image3",new FileBody(file3));
            entityBuilder.addPart("image4",new FileBody(file4));
            httppost.setEntity(entityBuilder.build());

            try {
                HttpResponse response = httpclient.execute(httppost);
                String json= EntityUtils.toString(response.getEntity());
                if(response.getStatusLine().getStatusCode()==200){
                    if(json.equals("1"))
                        return true;
                    else if(json.equals("-1")){
                        isAllImage=false;
                        return true;
                    }
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
            loading.dismiss();
            if (result) {
                if(isAllImage)
                    Toast.makeText(AddNewSeries.this,getResources().getString(R.string.sereis_uplaod_success), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(AddNewSeries.this,getResources().getString(R.string.upload_just_image), Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(AddNewSeries.this, getResources().getString(R.string.series_uplaod_failed), Toast.LENGTH_SHORT).show();
            }
            finish();
        }

    }

    public File convertBitmapToFile(Bitmap bitmap,String imageName){
        File x=getFilesDir();
        File f = new File(x,imageName);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

//Convert bitmap to byte array

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return  f;
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

    public int setOriention(ExifInterface ei){

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            case ExifInterface.ORIENTATION_NORMAL:
              return 1;
            default:
                break;
        }
        return 0;
    }

    public String getImageNameFromUriAcordingApi(Intent uri){
        String name;
        if (Build.VERSION.SDK_INT < 11) {
            name = RealPathUtil.getRealPathFromURI_BelowAPI11(this, uri.getData());
        }

        // SDK >= 11 && SDK < 19
        else if (Build.VERSION.SDK_INT < 19) {
            name = RealPathUtil.getRealPathFromURI_API11to18(this, uri.getData());
        }

        // SDK > 19 (Android 4.4)
        else {
            name = RealPathUtil.getRealPathFromURI_API19(this, uri.getData());
        }
        return name;
    }
}
