package com.example.yosef.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
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
import java.io.IOException;
import java.util.LinkedHashMap;

public class UpdateSeries extends AppCompatActivity {

    private ImageView imageViewCard1;
    private ImageView imageViewCard2;
    private ImageView imageViewCard3;
    private ImageView imageViewCard4;

    private Button buttonChooseCard1;
    private Button buttonChooseCard2;
    private Button buttonChooseCard3;
    private Button buttonChooseCard4;
    private Button buttonUpdateSeries;

    private TextView category_name;
    private EditText category;
    private EditText card1;
    private EditText card2;
    private EditText card3;
    private EditText card4;
    int myId;

    final String imageRelativePat = "http://10.0.2.2/final_project/images/";


    Series selctedSeries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_series);
        buttonUpdateSeries=(Button)findViewById(R.id.buttonUpdateSeries);

        buttonChooseCard1 = (Button) findViewById(R.id.buttonChooseCard1);
        buttonChooseCard2 = (Button) findViewById(R.id.buttonChooseCard2);
        buttonChooseCard3 = (Button) findViewById(R.id.buttonChooseCard3);
        buttonChooseCard4 = (Button) findViewById(R.id.buttonChooseCard4);

        //category=(EditText)findViewById(R.id.series);
        category_name=(TextView)findViewById(R.id.categoryName);
        card1=(EditText)findViewById(R.id.card1);
        card2=(EditText)findViewById(R.id.card2);
        card3=(EditText)findViewById(R.id.card3);
        card4=(EditText)findViewById(R.id.card4);

        imageViewCard1 = (ImageView) findViewById(R.id.imageViewCard1);
        imageViewCard2 = (ImageView) findViewById(R.id.imageViewCard2);
        imageViewCard3 = (ImageView) findViewById(R.id.imageViewCard3);
        imageViewCard4 = (ImageView) findViewById(R.id.imageViewCard4);

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

        imageLoader.DisplayImage((imageRelativePat + selctedSeries.getImage1()), R.mipmap.ic_launcher, imageViewCard1);
        imageLoader.DisplayImage((imageRelativePat + selctedSeries.getImage2()), R.mipmap.ic_launcher, imageViewCard2);
        imageLoader.DisplayImage((imageRelativePat + selctedSeries.getImage3()), R.mipmap.ic_launcher, imageViewCard3);
        imageLoader.DisplayImage((imageRelativePat + selctedSeries.getImage4()), R.mipmap.ic_launcher, imageViewCard4);


    }


    public void onClick(View v) {
        if(v==buttonUpdateSeries){
            new UpdateSeriesTask().execute(category_name.getText().toString(),
                                            card1.getText().toString(),
                                            card2.getText().toString(),
                                            card3.getText().toString(),
                                            card4.getText().toString());
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
       // String UpdateSeriesTask = "http://mysite.lidordigital.co.il/Quertets/db/UpdateSeries.php";
        String UpdateSeriesTask = "http://10.0.2.2/final_project/db/UpdateSeries.php";

        LinkedHashMap<String,String> parms=new LinkedHashMap<>();
        @Override
        protected Boolean doInBackground(String... params) {
           /* HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(UpdateSeriesTask);

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            entityBuilder.addTextBody("category_id", String.valueOf(category_id));
            entityBuilder.addTextBody("category_name", params[0]);
            entityBuilder.addTextBody("user_id",String.valueOf(currentPlayer.getUserID()));
            entityBuilder.addTextBody("card1", params[1]);
            entityBuilder.addTextBody("card2", params[2]);
            entityBuilder.addTextBody("card3", params[3]);
            entityBuilder.addTextBody("card4", params[4]);
            entityBuilder.addPart("image1",new FileBody(new File(imageName1)));
            entityBuilder.addPart("image2",new FileBody(new File(imageName2)));
            entityBuilder.addPart("image3",new FileBody(new File(imageName3)));
            entityBuilder.addPart("image4",new FileBody(new File(imageName4)));
            httppost.setEntity(entityBuilder.build());

            try {
                HttpResponse response = httpclient.execute(httppost);
                String json= EntityUtils.toString(response.getEntity());
                if(response.getStatusLine().getStatusCode()==200){
                    if(json=="1")
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
            }*/
            return true;
        }
        protected void onPostExecute(Boolean result) {
            Intent resultIntent = getIntent();

            if(result) {
                setResult(RESULT_OK, resultIntent);
                finish();
            }
            else{
                setResult(RESULT_CANCELED, resultIntent);
                finish();
            }





        }
    }
}
