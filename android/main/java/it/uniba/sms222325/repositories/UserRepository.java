package it.uniba.sms222325.repositories;

import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import it.uniba.sms222325.entities.User;

//TODO: write documentation
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

    /**
     * Creates a task that returns the first reference if already exists a User stored in the database with the specified field and value.
     * @param field The name of the field to compare
     * @param value The value for comparison
     * @return The created task
     * @throws RuntimeExecutionException if the task failed with an exception
     * @throws IllegalStateException if the task is not yet complete
     */
    public Task<User> getUser(final String field, final String value) throws RuntimeExecutionException, IllegalStateException{
        return userCollection.whereEqualTo(field, value).get().continueWith(task -> {
            if(task.getResult().isEmpty())
                return null;
            List<User> users = task.getResult().toObjects(User.class);
            return users.get(0);
        });
    }

    /**
     * Creates a task that control if already exists a User stored in the database with the specified username
     * @param username The value of the username to find
     * @return The created task
     * @throws IllegalArgumentException if in the task a function operates on a null reference
     * @throws RuntimeExecutionException if the task failed with an exception
     * @throws IllegalStateException if the task is not yet complete
     */
    public Task<User> getUserByUsername (final String username) throws IllegalArgumentException, RuntimeExecutionException, IllegalStateException{
        return userCollection.document(username).get().continueWith(task -> {
            if (task.getResult().exists())
                return task.getResult().toObject(User.class);
            else return null;
        });
    }

    /**
     * Creates a task that add a user in the database.
     * Be careful, if document already exists this task will override the stored document
     * @param user The user reference to store in the database
     * @return The created task
     * @throws IllegalArgumentException if in the task a function operates on a null reference
     */
    public Task<Void> addUser(User user) throws IllegalArgumentException{
        return userCollection.document(user.getUsername()).set(user);
    }
}
