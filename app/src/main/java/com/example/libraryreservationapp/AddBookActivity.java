package com.example.libraryreservationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddBookActivity extends AppCompatActivity
{
    private Button addBookBtn;
    private RadioGroup courseRadioGroup;
    private EditText bookEditText;
    private EditText authorEditText;
    private EditText isbnEditText;
    private FirebaseFirestore fStore;
    private RadioButton testRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        fStore = FirebaseFirestore.getInstance();
        addBookBtn = findViewById(R.id.btnAddBook);
        courseRadioGroup = findViewById(R.id.radioGroupCourses);
        bookEditText = findViewById(R.id.editBook);
        authorEditText = findViewById(R.id.editAuthor);
        isbnEditText = findViewById(R.id.editISBN);
        testRadioButton = findViewById(R.id.radioButtonC);

        addBookBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int flags = 0;
                String course ="", title = "", author = "", isbn ="";
                bookEditText.setError(null);
                authorEditText.setError(null);
                isbnEditText.setError(null);
                testRadioButton.setError(null);

                String test_title = bookEditText.getText().toString();
                if(test_title.equals("")){
                    flags++;
                    bookEditText.setError("Please enter a book title");
                }
                else{
                    title = bookEditText.getText().toString();
                }
                String test_author = authorEditText.getText().toString();
                if(test_author.equals("")){
                    flags++;
                    authorEditText.setError("Please enter a author");
                }
                else{
                    author = authorEditText.getText().toString();
                }
                String test_isbn = isbnEditText.getText().toString();
                if(test_isbn.equals("")){
                    flags++;
                    authorEditText.setError("please enter a isbn");
                }
                else{
                    isbn = isbnEditText.getText().toString();
                }


                int selectedCourse = courseRadioGroup.getCheckedRadioButtonId();
                if(selectedCourse == -1){
                    flags++;
                    testRadioButton.setError("Please choose a course");
                }
                else{
                    switch (selectedCourse){
                        case R.id.radioButtonUnix:
                            course = getString(R.string.unix);
                            break;
                        case R.id.radioButtonC:
                            course = getString(R.string.Cplus);
                            break;
                        case R.id.radioButtonJava:
                            course = getString(R.string.java);
                            break;
                        case R.id.radioButtonHistory:
                            course = getString(R.string.history);
                            break;
                        case R.id.radioButtonEconomics:
                            course = getString(R.string.economics);
                            break;
                        case R.id.radioButtonEnglish:
                            course = getString(R.string.english);
                            break;
                        case R.id.radioButtonRam:
                            course = getString(R.string.RAM);
                            break;
                        case R.id.radioButtonScience:
                            course = getString(R.string.science);
                            break;
                        case R.id.radioButtonMovie:
                            course = getString(R.string.movie);
                            break;
                        case R.id.radioButtonMath:
                            course = getString(R.string.math);
                            break;
                    }
                }

                if(flags == 0){
                    Map<String, Object> bookInfo = new HashMap<>();
                    bookInfo.put("course", course);
                    bookInfo.put("title", title );
                    bookInfo.put("author", author);
                    bookInfo.put("isbn", isbn);


                    fStore.collection("books").add(bookInfo).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Add was successful!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Add failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    finish();
                }



            }
        });

    }

}





