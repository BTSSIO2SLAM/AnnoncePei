package com.example.annoncepei;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.annoncepei.Models.Annonce;
import com.example.annoncepei.Models.Favoris;
import com.example.annoncepei.Networking.ApiConfig;
import com.example.annoncepei.Networking.AppConfig;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageListener;

import retrofit2.Call;
import retrofit2.Callback;

public class AnnonceActivity extends AppCompatActivity {

    ImageView imageView;

    int[] sampleImages = {R.drawable.ike, R.drawable.inkling, R.drawable.link, R.drawable.lucario};
    private TextView txtTitre, txtDetails, textPrix;
    private int id;
    private float prix;
    private String titre, details, urlphoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonce);

        txtTitre = findViewById(R.id.editTitre);
        txtDetails = findViewById(R.id.editDetails);
        textPrix = findViewById(R.id.editPrix);
        imageView = (ImageView) findViewById(R.id.imageView);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LoadExtraContent();

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    private void LoadExtraContent() {
        id = getIntent().getIntExtra("id",0);
        titre = getIntent().getStringExtra("titre");
        details = getIntent().getStringExtra("details");
        prix = getIntent().getFloatExtra("prix",0);
        urlphoto = getIntent().getStringExtra("urlphoto");
        txtTitre.setText(titre);
        txtDetails.setText(details+"\n" +
                "\n" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla dignissim ante vel leo vestibulum commodo. Sed lorem nulla, laoreet vel tempus non, porttitor nec tellus. Sed mi felis, sodales nec interdum sit amet, faucibus finibus nunc. Nulla placerat sodales eleifend. Sed feugiat sagittis leo feugiat faucibus. Curabitur fermentum neque vitae faucibus posuere. Nunc bibendum tempus purus a tincidunt. Aliquam mi lectus, tristique non odio ut, elementum sollicitudin tortor. Fusce sed lacus dictum, semper magna at, tempus odio.\n" +
                "\n" +
                "Mauris nunc ex, vulputate id malesuada quis, ullamcorper ut libero. Sed leo odio, venenatis at cursus sit amet, egestas a ante. Vestibulum dapibus vel turpis sed auctor. In at fringilla augue. Phasellus nibh est, mattis at est sit amet, gravida tristique urna. Sed consequat volutpat arcu a lobortis. Nullam finibus lacinia augue, sit amet fringilla nisi porta nec. Fusce aliquam mauris bibendum dui gravida imperdiet. Pellentesque nec sollicitudin erat. Vestibulum volutpat et nisi quis consequat. Phasellus porta non enim in iaculis. Etiam malesuada pharetra sem, sed aliquam orci vehicula ut. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.\n" +
                "\n" +
                "Aenean ultricies volutpat nibh, quis tristique diam accumsan a. Suspendisse potenti. Nulla varius elementum libero vitae pellentesque. Vestibulum at facilisis libero. Integer nec justo porttitor, fringilla eros ac, volutpat velit. Vestibulum ut urna metus. Quisque molestie eleifend convallis. Ut dui odio, ullamcorper nec rutrum eget, lacinia cursus neque. Pellentesque et convallis est, ut porttitor ligula. Sed elementum leo ipsum, a pretium turpis tincidunt eget.\n" +
                "\n" +
                "Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Morbi varius mattis nunc eget sodales. Mauris dictum lacus nec justo venenatis sodales. Cras sit amet justo eget augue tincidunt venenatis ut nec purus. Proin eget nibh lobortis, egestas neque id, venenatis dolor. Aliquam viverra ipsum facilisis, blandit massa ac, ullamcorper arcu. Mauris vel eros ac dolor dictum faucibus. Quisque a feugiat leo. Fusce eleifend ultricies ipsum, a tincidunt turpis scelerisque ut. Duis sodales rhoncus dui in vehicula. Praesent finibus, ligula non ullamcorper consequat, nulla nisi vulputate nunc, et imperdiet ipsum diam at libero. In mi risus, egestas vitae commodo ac, vestibulum sit amet lectus. Maecenas tempor aliquam ipsum sit amet sagittis.\n" +
                "\n" +
                "Praesent faucibus tortor nec tellus sollicitudin faucibus. Maecenas eget ultrices est, sit amet pharetra sem. Curabitur et felis suscipit, ornare arcu at, lobortis quam. Integer mattis felis at sodales consectetur. Interdum et malesuada fames ac ante ipsum primis in faucibus. Interdum et malesuada fames ac ante ipsum primis in faucibus. Etiam laoreet tellus at nulla ultrices congue. Sed ut blandit elit. Etiam auctor dolor metus, et tempus tortor sollicitudin eget. Duis vitae magna lacinia, gravida ex id, fermentum massa. Etiam condimentum porta egestas. ");
        textPrix.setText(String.valueOf(prix)+"€");

        String urlOfImage = urlphoto.replaceAll("localhost","10.0.2.2");
        Picasso.with(AnnonceActivity.this)
                .load(urlOfImage)
                .into(imageView);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.call:
                    dialPhoneNumber("0692214026");
                case R.id.email:
                    return true;
                case R.id.fav:
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_annonce, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.call) {
            dialPhoneNumber("0692214026");
        } else if (id == R.id.email) {

        } else if (id == R.id.fav) {

        }

        return super.onOptionsItemSelected(item);
    }

    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
