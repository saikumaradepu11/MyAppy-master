package com.example.myappyy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import static android.support.v7.widget.RecyclerView.*;

public class Main3Activityand_retrieve extends AppCompatActivity {

    RecyclerView eventslist;
    DatabaseReference reference;

    String key;

    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3_activityand_retrieve);

        eventslist = (RecyclerView) findViewById(R.id.recyclerviewandroid);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        eventslist.setLayoutManager(linearLayoutManager);



        validateeventslist();



    }


    private void validateeventslist() {

        reference= FirebaseDatabase.getInstance().getReference().child("Clubs").child("Androidclub");

        final FirebaseRecyclerAdapter<Events, Main3Activityand_retrieve.eventslistviewholder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Events, Main3Activityand_retrieve.eventslistviewholder>
                (
                        Events.class,R.layout.events_info, Main3Activityand_retrieve.eventslistviewholder.class,reference
                ) {
            @Override
            protected void populateViewHolder(final Main3Activityand_retrieve.eventslistviewholder viewHolder, Events model, int position) {

                viewHolder.setEventname(model.getEventname());
                viewHolder.setImage(model.getImage());

                final ImageView img=viewHolder.itemView.findViewById(R.id.retrivalimg);

                final String s1=model.getEventname();
                final String s3=model.getInfo();
                final String s5=model.getImage();
                final String s4=model.getStudents();
                final String s2=model.getDate();
               final String s6=model.getLink();
               final String s7=model.getUid();



                viewHolder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent=new Intent(Main3Activityand_retrieve.this,Android_details_student.class);
                        ActivityOptionsCompat activityOptionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(Main3Activityand_retrieve.this,img, ViewCompat.getTransitionName(img));
                        intent.putExtra("a1",s1);
                        intent.putExtra("a2",s2);
                        intent.putExtra("a3",s3);
                        intent.putExtra("a4",s4);
                        intent.putExtra("a5",s5);
                        intent.putExtra("a6",s6);
                        intent.putExtra("a7",s7);
                        startActivity(intent,activityOptionsCompat.toBundle());


                    }
                });



            }
        };
        eventslist.setAdapter(firebaseRecyclerAdapter);


    }

    public static class eventslistviewholder extends RecyclerView.ViewHolder {
        View mview;

        ImageView imageView;

        public eventslistviewholder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;
        }

        public void setEventname(String eventname) {
            TextView t1 = mview.findViewById(R.id.retrivalevent);
            t1.setText(eventname);
        }



        public void setImage(String image) {
            {
                imageView = mview.findViewById(R.id.retrivalimg);
                Picasso.get().load(image).into(imageView);
            }

        }

        public void setInfo(String info)
        {
            TextView t3=mview.findViewById(R.id.retrivalt1);
            t3.setText(info);
        }
        public void setStudents(String students)
        {
            TextView t3=mview.findViewById(R.id.retrivalt2);
            t3.setText(students);
        }
        public void setDate(String date)
        {
            TextView t3=mview.findViewById(R.id.retrivalbranch);
            t3.setText(date);
        }
        public void setLink(String link) {
            TextView t4 = mview.findViewById(R.id.retrivallink);
            if(!TextUtils.isEmpty(link))
            {
                t4.setVisibility(View.GONE);
                t4.setText(" ");
            }

        }
    }


}






