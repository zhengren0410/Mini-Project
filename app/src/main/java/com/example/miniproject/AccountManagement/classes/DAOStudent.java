package com.example.miniproject.AccountManagement.classes;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DAOStudent {
    private DatabaseReference databaseReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://miniproject-87501-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private DatabaseReference mDatabase = database.getReference();

    public DAOStudent(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference( Student.class.getSimpleName() );
    }

    public Task<Void> add( Student stu ){      // Separate business logic & ui
//        return databaseReference.push().setValue( stu );
        return mDatabase.child("Student").child(stu.userID).setValue( stu );

        // we are use add value event listener method
        // which is called with database reference.

    }

    public Task<Void> editName( String userID, String newName ){      // Separate business logic & ui
        Map<String, String> name = new HashMap<>();
        name.put("fullname",newName);
        return mDatabase.child("Student").child(userID).setValue( name );

        // we are use add value event listener method
        // which is called with database reference.

    }
}