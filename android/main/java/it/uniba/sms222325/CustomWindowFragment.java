package it.uniba.sms222325;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class CustomWindowFragment extends Fragment {
    Fragment fragment;
    String titleWindow;

    public CustomWindowFragment(Fragment fragment, String titleWindow) {
        this.fragment = fragment;
        this.titleWindow = titleWindow;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.window_fragment_container, container, false);
        TextView text = view.findViewById(R.id.actionBarTitle);
        ImageButton imageButton = view.findViewById(R.id.backButton);

        imageButton.setOnClickListener(l -> getParentFragmentManager().popBackStack());

        if(titleWindow == null)
            text.setText("");
        else
            text.setText(titleWindow);

        getParentFragmentManager()
                .beginTransaction()
                .add(R.id.customFragmentContainer, fragment)
                .commit();
        return view;
    }
}
