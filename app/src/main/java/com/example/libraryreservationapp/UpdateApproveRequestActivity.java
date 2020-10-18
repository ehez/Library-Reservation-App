package com.example.libraryreservationapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateApproveRequestActivity extends AppCompatActivity {
    private Button approveBtn;
    private Button denyBtn;
    private EditText bookEditText;
    private EditText classEditText;
    private EditText isbnEditText;
    private EditText quantityText;
    private FirebaseFirestore fStore;
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_approve_request);


        approveBtn = findViewById(R.id.btnApprove);
        denyBtn = findViewById(R.id.btnDeny);
        bookEditText = findViewById(R.id.booksNameUpdate);
        classEditText = findViewById(R.id.classNameUpdate);
        isbnEditText = findViewById(R.id.isbnUpdate);
        quantityText = findViewById(R.id.booksQuantityUpdate);

        // gets firestore instance
        fStore = FirebaseFirestore.getInstance();

        //gets the document ID of the item clicked on from the intent
        Intent intent = getIntent();


        String documentID = intent.getStringExtra("docID");

        //creates a reference to a specific document in the collection
        documentReference = fStore.collection("requests").document(documentID);

        //retrieves the data from firestore for the room that is selected
        getDataFromFirestore();



        approveBtn.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view)
            {

                // set up a channel becuase it gave problems with invoking channel = null;
                String id = "channel_1";
                String description = "1";
                int importance = NotificationManager.IMPORTANCE_LOW;
                NotificationChannel channel = null;
                // checks to see SDK version to see if its compatable
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    channel = new NotificationChannel(id, description, importance);
                }
                String message = "Your Book Request Has Been Approved !!!.";


                // creates the notification
                Notification.Builder notification = new Notification.Builder(
                        UpdateApproveRequestActivity.this,id
                )
                        .setSmallIcon(R.drawable.ic_library)
                        .setContentTitle("Requests")
                        .setContentText(message)
                        .setAutoCancel(true);

                //Starts NotificationActivity if the notification is clicked
                Intent intent = new Intent(UpdateApproveRequestActivity.this, NotificationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("message",message);

                // Pending intent to grant the right to perform the action later
                PendingIntent pendingIntent = PendingIntent.getActivity(UpdateApproveRequestActivity.this,
                        0,intent,PendingIntent.FLAG_UPDATE_CURRENT );
                notification.setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager)getSystemService(
                        Context.NOTIFICATION_SERVICE
                );

                notificationManager.createNotificationChannel(channel);
                notificationManager.notify(1,notification.build());

            }
        });

       denyBtn.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view)
            {

                // set up a channel becuase it gave problems with invoking channel = null;
                String id = "channel_2";
                String description = "2";
                int importance = NotificationManager.IMPORTANCE_LOW;
                NotificationChannel channel = null;
                // checks to see SDK version to see if its compatable
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    channel = new NotificationChannel(id, description, importance);
                }
                String message = "Your Book Request Has Been DENIED !!!.";


                // creates the notification
                Notification.Builder notification = new Notification.Builder(
                        UpdateApproveRequestActivity.this,id
                )
                        .setSmallIcon(R.drawable.ic_library)
                        .setContentTitle("Requests")
                        .setContentText(message)
                        .setAutoCancel(true);

                //Starts NotificationActivity if the notification is clicked
                Intent intent = new Intent(UpdateApproveRequestActivity.this, NotificationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("message",message);

                // Pending intent to grant the right to perform the action later
                PendingIntent pendingIntent = PendingIntent.getActivity(UpdateApproveRequestActivity.this,
                        0,intent,PendingIntent.FLAG_UPDATE_CURRENT );
                notification.setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager)getSystemService(
                        Context.NOTIFICATION_SERVICE
                );

                notificationManager.createNotificationChannel(channel);
                notificationManager.notify(2,notification.build());

            }
        });

    }
    //Pulls  data from firestore to display information about the course
    public void getDataFromFirestore() {

        //gets the document for the specific book request
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    //gets the document
                    DocumentSnapshot document = task.getResult();
                    // gets the title of the book requested
                    String title = document.getString("title");
                    // sets the title to its appropriate editText
                    bookEditText.setText(title);
                    // gets author of the book requested
                    String className = document.getString("course");
                    // sets the author to its appropriate editText
                    classEditText.setText(className);
                    // gets the isbn of the book requested
                    String isbn = document.getString("isbn");
                    // sets the isbn to its appropriate editText
                    isbnEditText.setText(isbn);
                    // gets the quantity of the book requested
                    String Quantity = document.getString("quantity");
                    // sets the quantity to its appropriate editText
                    quantityText.setText(Quantity);

                }
                     else
                    {
                    Log.d("MYDEBUG", "Error getting document values");
                    }

            }
        });
    }
}