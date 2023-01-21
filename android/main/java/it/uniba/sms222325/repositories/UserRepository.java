package it.uniba.sms222325.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import it.uniba.sms222325.entities.User;

public class UserRepository {
    private static UserRepository myUserRepositoryInstance = null;
    private final CollectionReference userCollection;

    private UserRepository(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String collectionName = "users";
        this.userCollection = db.collection(collectionName);
    }

    public static UserRepository getInstance(){
        if (myUserRepositoryInstance == null)
            myUserRepositoryInstance = new UserRepository();
        return myUserRepositoryInstance;
    }

    public Task<User> getUser(final String field, final String value) {
        return userCollection.whereEqualTo(field, value).get().continueWith(task -> {
            if(task.getResult().isEmpty())
                return null;
            List<User> users = task.getResult().toObjects(User.class);
            return users.get(0);
        });
    }

    public Task<User> getUserByUsername(final String username) {
        return userCollection.document(username).get().continueWith(task -> {
            if (task.getResult().exists())
                return task.getResult().toObject(User.class);
            else return null;
        });
    }

    public Task<Void> addUser(User user) {
        return userCollection.document(user.getUsername()).set(user);
    }
}
