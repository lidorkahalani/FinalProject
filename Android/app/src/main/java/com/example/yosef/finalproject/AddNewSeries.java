package com.example.yosef.finalproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class AddNewSeries extends AppCompatActivity implements View.OnClickListener {
    //public static final String UPLOAD_URL = "http://mysite.lidordigital.co.il/Quertets/db/add_image.php";
    //public static final String UPLOAD_URL = "http://10.0.2.2/final_project/db/add_image.php";



    public static final String UPLOAD_KEY = "image";
    public static final String TAG = "MY MESSAGE";

    private int PICK_IMAGE_REQUEST = 1;

    //private Button buttonChoose;
    private Button buttonUploadSeries;
    private User currentPlayer;


    private Button buttonUpload;

    private ImageView imageViewCard1;
    private ImageView imageViewCard2;
    private ImageView imageViewCard3;
    private ImageView imageViewCard4;

    Button buttonChooseCard1;
    Button buttonChooseCard2;
    Button buttonChooseCard3;
    Button buttonChooseCard4;

    Boolean isbuttonChooseCard1;
    Boolean isbuttonChooseCard2;
    Boolean isbuttonChooseCard3;
    Boolean isbuttonChooseCard4;

    private EditText category;
    private EditText card1;
    private EditText card2;
    private EditText card3;
    private EditText card4;
    private Boolean upload_image_status;
    private String cardName;
    TextView categoryName;
    private int picCnt=0;

    static private ArrayList<Card>newSeries=new ArrayList<>();

    private Bitmap bitmap;
    private File imageFile;
    private boolean imageCoosen;
    private static int category_id;



    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_series);

        buttonChooseCard1 = (Button) findViewById(R.id.buttonChooseCard1);
        buttonChooseCard2 = (Button) findViewById(R.id.buttonChooseCard2);
        buttonChooseCard3 = (Button) findViewById(R.id.buttonChooseCard3);
        buttonChooseCard4 = (Button) findViewById(R.id.buttonChooseCard4);

        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        //buttonUploadSeries=(Button) findViewById(R.id.buttonUploadSeries);
        category=(EditText)findViewById(R.id.series);
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
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,getResources().getString(R.string.Select_Picture)), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            imageFile=new File(filePath.getPath());
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                if(isbuttonChooseCard1) {
                    imageViewCard1.setImageBitmap(bitmap);
                    isbuttonChooseCard1=false;
                }
                else if(isbuttonChooseCard2) {
                    imageViewCard2.setImageBitmap(bitmap);
                    isbuttonChooseCard2=false;
                }
                else if(isbuttonChooseCard3) {
                    imageViewCard3.setImageBitmap(bitmap);
                    isbuttonChooseCard3=false;
                }
                else if(isbuttonChooseCard4) {
                    imageViewCard4.setImageBitmap(bitmap);
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
            String getMaxCategoryId_url="http://10.0.2.2/final_project/db/getMaxCategoryId.php";
           // String getMaxCategoryId_url="http://mysite.lidordigital.co.il/Quertets/db/getMaxCategoryId.php";
            LinkedHashMap<String, String> parms = new LinkedHashMap<>();
            @Override
            protected Integer doInBackground(String... params) {
                JSONParser json = new JSONParser();
                try {
                    JSONObject response = json.makeHttpRequest(getMaxCategoryId_url, "GET", parms);
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
            //public static final String UPLOAD_URL = "http://mysite.lidordigital.co.il/Quertets/db/AddCard.php";
            public static final String UPLOAD_URL = "http://10.0.2.2/final_project/db/AddCard.php";
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
                JSONObject response = json.makeHttpRequest(UPLOAD_URL, "POST", data);
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
                        Intent myIntent = new Intent(AddNewSeries.this, MainMenu.class);
                        startActivity(myIntent);
                        //finish();
                        finish();
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
        }



        if(v == buttonUpload){

            if(chekIfAllParmInit()){//chekIfAllParmInit() {
                Toast.makeText(this,"All good",Toast.LENGTH_SHORT).show();
                new sendSeriesToServer().execute(categoryName.getText().toString(),card1.getText().toString(),card2.getText().toString(),
                                        card3.getText().toString(),card4.getText().toString(),
                                        getStringImage(((BitmapDrawable)imageViewCard1.getDrawable()).getBitmap()),
                                        getStringImage(((BitmapDrawable)imageViewCard2.getDrawable()).getBitmap()),
                                        getStringImage(((BitmapDrawable)imageViewCard3.getDrawable()).getBitmap()),
                                        getStringImage(((BitmapDrawable)imageViewCard4.getDrawable()).getBitmap()));


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


    public class sendSeriesToServer extends AsyncTask<String, Void, Boolean> {
        String upload_series = "http://10.0.2.2/final_project/db/upload_series.php";
        // String upload_series = "http://mysite.lidordigital.co.il/Quertets/db/upload_series.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("category_id",String.valueOf(category_id));
            parms.put("category_name",params[0]);
            parms.put("user_id",String.valueOf(currentPlayer.getUserID()));
            parms.put("card1",params[1]);
            parms.put("card2",params[2]);
            parms.put("card3",params[4]);
            parms.put("card4",params[5]);
            parms.put("image1",params[5]);
            parms.put("image2",params[6]);
            parms.put("image3",params[7]);
            parms.put("image4",params[8]);

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(upload_series, "POST", parms);
                if (response.getInt("succsses")==1) {
                    newSeries.clear();
                    return true;
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }
            return false;

        }


        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(AddNewSeries.this, "Series upload sucssfully", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(AddNewSeries.this, "Series upload failed", Toast.LENGTH_SHORT).show();
        }

    }
}
