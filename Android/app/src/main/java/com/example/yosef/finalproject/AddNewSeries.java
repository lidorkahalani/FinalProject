package com.example.yosef.finalproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
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
import org.apache.http.util.EntityUtils;
import org.apache.http.util.EntityUtilsHC4;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.yosef.finalproject.ServerUtils;
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
            matrix.postRotate(180);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                if(isbuttonChooseCard1) {
                    bitmapResize1=Bitmap.createScaledBitmap(rotatedBitmap,200,200,true);
                    imageName1=getRealPathFromURI_BelowAPI11(getBaseContext(),filePath);
                    file1= new File(Environment.getExternalStorageDirectory().getAbsolutePath(),imageFile.getPath());
                    imageViewCard1.setImageBitmap(rotatedBitmap);
                    isbuttonChooseCard1=false;
                }
                else if(isbuttonChooseCard2) {
                    bitmapResize2=Bitmap.createScaledBitmap(rotatedBitmap,200,200,true);
                    imageName2=getRealPathFromURI_BelowAPI11(getBaseContext(),filePath);
                    file2= new File(Environment.getExternalStorageDirectory().getAbsolutePath(),imageFile.getPath());

                    imageViewCard2.setImageBitmap(rotatedBitmap);
                    isbuttonChooseCard2=false;
                }
                else if(isbuttonChooseCard3) {
                    bitmapResize3=Bitmap.createScaledBitmap(rotatedBitmap,200,200,true);
                    imageName3=getRealPathFromURI_BelowAPI11(getBaseContext(),filePath);
                    file3= new File(Environment.getExternalStorageDirectory().getAbsolutePath(),imageFile.getPath());

                    imageViewCard3.setImageBitmap(rotatedBitmap);
                    isbuttonChooseCard3=false;
                }
                else if(isbuttonChooseCard4) {
                    bitmapResize4=Bitmap.createScaledBitmap(rotatedBitmap,200,200,true);
                    imageName4=getRealPathFromURI_BelowAPI11(getBaseContext(),filePath);
                    file4= new File(Environment.getExternalStorageDirectory().getAbsolutePath(),imageFile.getPath());
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
            public static final String UPLOAD_URL = "http://mysite.lidordigital.co.il/Quertets/db/php/AddCard.php";
           // public static final String UPLOAD_URL = "http://10.0.2.2/final_project/db/AddCard.php";
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
                        new setGameToInactive().execute(String.valueOf(game.getGame_id()));
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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            final AlertDialog dialog = builder.create();
            LayoutInflater inflater = getLayoutInflater();
            View dialogLayout = inflater.inflate(R.layout.zoom_image_layout, null);
            dialog.setView(dialogLayout);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.show();

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface d) {
                    ImageView image = (ImageView) dialog.findViewById(R.id.goProDialogImage);
                    Bitmap icon = ((BitmapDrawable)image.getDrawable()).getBitmap();
                    float imageWidthInPX = (float)image.getWidth();

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                            Math.round(imageWidthInPX * (float)icon.getHeight() / (float)icon.getWidth()));
                    image.setLayoutParams(layoutParams);
                }
            });
        }




        if(v == buttonUpload){

            if(chekIfAllParmInit()){//chekIfAllParmInit() {
               // Toast.makeText(this,"All good",Toast.LENGTH_SHORT).show();
                new sendSeriesToServer().execute(categoryName.getText().toString(),card1.getText().toString(),
                                                                                card2.getText().toString(),
                                                                                card3.getText().toString(),
                                                                                card4.getText().toString());


                /*cardName=card1.getText().toString();
                uploadImage();
                if(upload_image_status){
                    addThisCardToLocalArray();
                }*/
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
        }else {
            // image pick from gallery
            getRealPathFromURI_BelowAPI11(contex,uri);
        }
        return name;


    }

    public class sendSeriesToServer extends AsyncTask<String, Void, Boolean> {
        // upload_series = "http://10.0.2.2/final_project/db/upload_series.php";
         String upload_series = "http://mysite.lidordigital.co.il/Quertets/php/db/upload_series.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();
        File imageFile1;
        File imageFile2;
        File imageFile3;
        File imageFile4;
        protected void onPreExecute(){
            super.onPreExecute();
            imageFile1=convertBitmapToFile(bitmapResize1,imageName1);
            imageFile2=convertBitmapToFile(bitmapResize2,imageName2);
            imageFile3=convertBitmapToFile(bitmapResize3,imageName3);
            imageFile4=convertBitmapToFile(bitmapResize4,imageName4);


        }

        @Override
        protected Boolean doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(ServerUtils.upload_series);

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            entityBuilder.addTextBody("category_id", String.valueOf(category_id));
            entityBuilder.addTextBody("category_name", params[0]);
            entityBuilder.addTextBody("user_id",String.valueOf(currentPlayer.getUserID()));
            entityBuilder.addTextBody("card1", params[1]);
            entityBuilder.addTextBody("card2", params[2]);
            entityBuilder.addTextBody("card3", params[3]);
            entityBuilder.addTextBody("card4", params[4]);
            entityBuilder.addPart("image1",new FileBody(imageFile1));
            entityBuilder.addPart("image2",new FileBody(imageFile2));
            entityBuilder.addPart("image3",new FileBody(imageFile3));
            entityBuilder.addPart("image4",new FileBody(imageFile4));
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
            if (result) {
                Toast.makeText(AddNewSeries.this, "Series upload successfully!", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(AddNewSeries.this, "Series upload failed", Toast.LENGTH_SHORT).show();
            }
            finish();
        }

    }

    public class setGameToInactive extends AsyncTask<String, Void, Boolean> {
       // String setGameToInactive = "http://10.0.2.2/final_project/db/setGameToInactive.php";
        String setGameToInactive = "http://mysite.lidordigital.co.il/Quertets/php/db/setGameToInactive.php";

        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("game_id", params[0]);

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.setGameToInactive, "POST", parms);
                if (response.getInt("successes") == 1)
                    return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                Intent myIntent = new Intent(AddNewSeries.this, MainMenu.class);
                startActivity(myIntent);
                finish();
            } else
                Toast.makeText(AddNewSeries.this, "There was problem on log out", Toast.LENGTH_SHORT).show();
        }

    }

    public File convertBitmapToFile(Bitmap bitmap,String imageName){
        File f = new File(imageName);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

//Convert bitmap to byte array

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
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
}
