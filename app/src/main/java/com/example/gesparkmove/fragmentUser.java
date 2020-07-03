package com.example.gesparkmove;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragmentUser extends Fragment {
    //declaração de variáveis
    TextView textViewUserName, textViewUserAddress, textViewUserPostalCode, textViewUserContacts, textViewUserFiscalCode;
    Handler userHandler = new Handler();
    User user;
    CircleImageView imageViewUserAvatar;
    //listener para tratar a informação devolvida pelo taskUserData
    onUserListener listener = new onUserListener() {
        @Override
        public void onUserCompleted(ArrayList<String> data) {
            textViewUserName.setText(data.get(1));
            textViewUserAddress.setText(data.get(2));
            textViewUserPostalCode.setText(data.get(3));
            textViewUserContacts.setText(data.get(4));
            textViewUserFiscalCode.setText(data.get(0));
            if(data.size() == 6)
                Picasso.get().load("https://gespark.pt/" + data.get(5)).into(imageViewUserAvatar);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
        user = bundle.getParcelable("USER");
        //associação das variáveis aos elementos visuais
        textViewUserName = view.findViewById(R.id.textViewUserName);
        textViewUserAddress = view.findViewById(R.id.textViewUserAddress);
        textViewUserPostalCode = view.findViewById(R.id.textViewUserPostalCode);
        textViewUserContacts = view.findViewById(R.id.textViewUserContact);
        textViewUserFiscalCode = view.findViewById(R.id.textViewUserFiscalCode);
        imageViewUserAvatar = view.findViewById(R.id.imageViewUserAvatar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //asynctask para carregar os dados do utilizador.
        new taskUserData(getActivity(), listener, userHandler).execute(String.valueOf(user.getId()));
    }
}