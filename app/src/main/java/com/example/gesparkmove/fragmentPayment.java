package com.example.gesparkmove;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class fragmentPayment extends Fragment {
    //declaração das variáveis
    TextView textViewPaymentName, textViewPaymentNumber, textViewPaymentDate;
    EditText editTextPaymentName, editTextPaymentNumber, editTextPaymentCV;
    Spinner spinnerPaymentMonth, spinnerPaymentYear, spinnerPaymentPayment;
    LinearLayout linearLayoutPaymentData;
    ImageView imageViewLogoMetodosPagamentos;
    Button buttonPaymentSave;
    Handler paymentHandler = new Handler();
    User user;
    Handler handler = new Handler();
    //interface para trabalhar a informação recebida da taskPayment
    onPaymentListener listener = new onPaymentListener() {
        @Override
        public void onPagamentosCompleted(List<String> data) {
            switch (data.size()){
                case 1:
                    imageViewLogoMetodosPagamentos.setImageDrawable(getResources().getDrawable(R.drawable.mbway));
                    textViewPaymentName.setText(data.get(0));
                break;
                case 2:
                    imageViewLogoMetodosPagamentos.setImageDrawable(getResources().getDrawable(R.drawable.db));
                    textViewPaymentName.setText(data.get(0));
                    textViewPaymentNumber.setText(data.get(1));
                break;
                case 3:
                    imageViewLogoMetodosPagamentos.setImageDrawable(getResources().getDrawable(R.drawable.visa));
                    textViewPaymentName.setText(data.get(0));
                    textViewPaymentNumber.setText(data.get(1));
                    textViewPaymentDate.setText(data.get(2));
                break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        //associação das varáveis aos elementos visuais
        textViewPaymentName = view.findViewById(R.id.textViewPaymentName);
        textViewPaymentNumber = view.findViewById(R.id.textViewPaymentNumber);
        textViewPaymentDate = view.findViewById(R.id.textViewPaymentDate);
        imageViewLogoMetodosPagamentos = view.findViewById(R.id.imageViewPaymentLogo);
        editTextPaymentName = view.findViewById(R.id.editTextPaymentName);
        editTextPaymentName.addTextChangedListener(paymentTextWatcher);
        editTextPaymentNumber = view.findViewById(R.id.editTextPaymentName);
        editTextPaymentNumber.addTextChangedListener(paymentTextWatcher);
        editTextPaymentCV = view.findViewById(R.id.editTextPaymentCV);
        editTextPaymentCV.addTextChangedListener(paymentTextWatcher);
        spinnerPaymentMonth = view.findViewById(R.id.spinnerPaymentMonth);
        spinnerPaymentYear = view.findViewById(R.id.spinnerPaymentYear);
        buttonPaymentSave = view.findViewById(R.id.buttonPaymentSave);
        spinnerPaymentPayment = view.findViewById(R.id.spinnerPaymentPayment);
        linearLayoutPaymentData = view.findViewById(R.id.linearLayoutPaymentData);
        //declaração dos valores a apresentar nos spinners
        String[] pp = new String[]{"", "Cartão de Crédito", "MBWay", "Débito Direto"};
        String[] month = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[]{String.valueOf(year), String.valueOf(year + 1), String.valueOf(year + 2), String.valueOf(year + 3), String.valueOf(year + 4), String.valueOf(year + 5), String.valueOf(year + 6)};
        ArrayAdapter<String> adapterPayment = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, pp);
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, month);
        ArrayAdapter<String> adapterYear = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, years);
        spinnerPaymentPayment.setAdapter(adapterPayment);
        spinnerPaymentMonth.setAdapter(adapterMonth);
        spinnerPaymentYear.setAdapter(adapterYear);
        Bundle bundle = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        user = Objects.requireNonNull(bundle).getParcelable("USER");
        //listener para esconder ou mostrar elementos conforme o novo modo de pagamento selecionado
        spinnerPaymentPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        editTextPaymentName.setVisibility(View.GONE);
                        editTextPaymentNumber.setVisibility(View.GONE);
                        editTextPaymentCV.setVisibility(View.GONE);
                        buttonPaymentSave.setVisibility(View.GONE);
                        linearLayoutPaymentData.setVisibility(View.GONE);
                        break;
                    case 1:
                        editTextPaymentName.setVisibility(View.VISIBLE);
                        editTextPaymentName.setText("");
                        editTextPaymentName.setHint("Nome");
                        editTextPaymentNumber.setVisibility(View.VISIBLE);
                        editTextPaymentNumber.setText("");
                        editTextPaymentNumber.setHint("Número");
                        editTextPaymentCV.setVisibility(View.VISIBLE);
                        editTextPaymentCV.setText("");
                        editTextPaymentCV.setHint("CVV2/CVC2");
                        linearLayoutPaymentData.setVisibility(View.VISIBLE);
                        buttonPaymentSave.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        editTextPaymentName.setVisibility(View.GONE);
                        editTextPaymentNumber.setVisibility(View.VISIBLE);
                        editTextPaymentNumber.setText("");
                        editTextPaymentNumber.setHint("Telemovel");
                        editTextPaymentCV.setVisibility(View.GONE);
                        linearLayoutPaymentData.setVisibility(View.GONE);
                        buttonPaymentSave.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        editTextPaymentName.setVisibility(View.VISIBLE);
                        editTextPaymentName.setText("");
                        editTextPaymentName.setHint("Nome");
                        editTextPaymentNumber.setVisibility(View.VISIBLE);
                        editTextPaymentNumber.setText("");
                        editTextPaymentNumber.setHint("IBAN");
                        editTextPaymentCV.setVisibility(View.GONE);
                        linearLayoutPaymentData.setVisibility(View.GONE);
                        buttonPaymentSave.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //ação do botão salvar
        buttonPaymentSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                switch (spinnerPaymentPayment.getSelectedItemPosition()){
                    case 1:
                        String data = spinnerPaymentMonth.getSelectedItem().toString() + "/" + spinnerPaymentYear.getSelectedItem().toString().substring(2);
                        new taskSavePayment(getActivity(), handler, manager).execute(String.valueOf(user.getId()), String.valueOf(spinnerPaymentPayment.getSelectedItemPosition()), editTextPaymentName.getText().toString(), editTextPaymentNumber.getText().toString(),
                                data, editTextPaymentCV.getText().toString());
                    break;
                    case 2:
                        new taskSavePayment(getActivity(), handler, manager).execute(String.valueOf(user.getId()), String.valueOf(spinnerPaymentPayment.getSelectedItemPosition()), editTextPaymentNumber.getText().toString());
                    break;
                    case 3:
                        new taskSavePayment(getActivity(), handler, manager).execute(String.valueOf(user.getId()), String.valueOf(spinnerPaymentPayment.getSelectedItemPosition()), editTextPaymentName.getText().toString(), editTextPaymentNumber.getText().toString());
                    break;
                }

            }
        });

        return view;
    }
    //textwatch para habilitar o botão de salvar caso os campos estejam todos completos
    private TextWatcher paymentTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch (spinnerPaymentPayment.getSelectedItemPosition()){
                case 1:
                    String nomeCC = editTextPaymentName.getText().toString().trim();
                    String numeroCC = editTextPaymentNumber.getText().toString().trim();
                    String cv = editTextPaymentCV.getText().toString().trim();
                    buttonPaymentSave.setEnabled(!nomeCC.isEmpty() && !numeroCC.isEmpty() && !cv.isEmpty());
                    break;
                case 2:
                    String numeroMB = editTextPaymentNumber.getText().toString().trim();
                    buttonPaymentSave.setEnabled(!numeroMB.isEmpty());
                    break;
                case 3:
                    String nomeDD = editTextPaymentName.getText().toString().trim();
                    String numeroDD = editTextPaymentNumber.getText().toString().trim();
                    buttonPaymentSave.setEnabled(!nomeDD.isEmpty() && !numeroDD.isEmpty());
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //asynctask para ir buscar à base de dados o metodo de pagamento do utilizador logado
        new taskPayment(getActivity(), paymentHandler, listener).execute(String.valueOf(user.getId()));
    }
}
