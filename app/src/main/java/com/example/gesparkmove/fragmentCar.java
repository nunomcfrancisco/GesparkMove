package com.example.gesparkmove;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragmentCar extends Fragment {
    //declaração de variaveis
    Handler carHandler = new Handler();
    User user;
    Car car;
    TextView textViewLicensePlate, textViewBrand, textViewModel, textViewColor, textViewWarning;
    Button buttonDeleteCar, buttonSaveCar;
    Switch switchActive;
    Spinner spinnerPayment;
    CircleImageView imageViewCar;
    int active, plane;
    onCarListener listener = new onCarListener() {
        @Override
        public void onVeiculoCompleted(Integer plane, Integer history) {
            switch (plane){
                case 0:
                    spinnerPayment.setSelection(1);
                break;
                case 1:
                    spinnerPayment.setSelection(2);
                break;
                case 2:
                    spinnerPayment.setSelection(3);
                break;
            }
            if(history == 1)
                buttonDeleteCar.setEnabled(false);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car, container, false);
        //inicialização dos elementos visuais
        textViewLicensePlate = view.findViewById(R.id.textViewCarLicensePlate);
        textViewBrand = view.findViewById(R.id.textViewCarBrand);
        textViewModel = view.findViewById(R.id.textViewCarModel);
        textViewColor = view.findViewById(R.id.textViewCarColor);
        textViewWarning = view.findViewById(R.id.textViewCarWarning);
        switchActive = view.findViewById(R.id.switchCarActive);
        buttonDeleteCar = view.findViewById(R.id.buttonCarDelete);
        buttonSaveCar = view.findViewById(R.id.buttonCarSave);
        spinnerPayment = view.findViewById(R.id.spinnerCarPayment);
        imageViewCar = view.findViewById(R.id.imageViewCarAvatar);
        //Vai buscar a informação do veiculo selecionado
        Bundle bundle = getActivity().getIntent().getExtras();
        car = bundle.getParcelable("DATAVEICULO");
        user = bundle.getParcelable("USER");
        //elementos do spinner
        String[] pp = new String[]{"", "Avença", "Fracção"};
        ArrayAdapter<String> adapterPlane = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, pp);
        spinnerPayment.setAdapter(adapterPlane);
        //Se o veiculo estiver estacionado bloqueia os botões, spinner e switch
        if(car.getParked() == 1){
            spinnerPayment.setEnabled(false);
            switchActive.setEnabled(false);
            buttonDeleteCar.setEnabled(false);
            buttonSaveCar.setEnabled(false);
            textViewWarning.setVisibility(view.VISIBLE);
        }
        //mostra a informação do veiculo
        if(car.getAvatar() != null){
            Picasso.get().load("https://gespark.pt/" + car.getAvatar()).into(imageViewCar);
        }
        textViewLicensePlate.setText(car.getLicensePlate());
        textViewBrand.setText("Marca: " + car.getBrand());
        textViewModel.setText("Modelo: " + car.getModel());
        textViewColor.setText("Cor: " + car.getColor());
        //coloca o switch ativo ou não conforme a informação do veiculo.
        if(car.getActive() == 1) {
            active = 1;
            switchActive.setChecked(true);
        }
        else{
            active = 0;
            switchActive.setChecked(false);
        }
        //listener para ativar/desativar o spinner conforme a posição do switch
        switchActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    active = 1;
                else
                    active = 0;
            }
        });
        //clickar no botão gravar
        buttonSaveCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinnerPayment.getSelectedItem().toString().equals("Avença")) plane = 1;
                else plane = 2;
                FragmentManager manager = getFragmentManager();
                //asynctask para adicionar o veiculo à base de dados
                new taskSaveCar(getActivity(), carHandler, manager).execute(String.valueOf(car.getId()), String.valueOf(active), String.valueOf(plane), String.valueOf(user.getId()));
            }
        });
        //clickar no boão apagar
        buttonDeleteCar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager manager = getFragmentManager();
                //asynctask para apagar o veiculo da base de dados
                new taskDeleteCar(getActivity(), carHandler, manager, getActivity()).execute(String.valueOf(car.getId()));
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        new taskCar(getActivity(), carHandler, listener).execute(String.valueOf(car.getId()));
    }
}