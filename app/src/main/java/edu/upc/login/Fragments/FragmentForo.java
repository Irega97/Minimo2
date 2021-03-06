package edu.upc.login.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.upc.login.API;
import edu.upc.login.Adaptadores.AdapterForo;
import edu.upc.login.Entidades.Comentario;
import edu.upc.login.Entidades.Foro;
import edu.upc.login.Entidades.Item;
import edu.upc.login.R;
import edu.upc.login.iComunicaFragments;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentForo extends Fragment {

    AdapterForo adapterForo;
    RecyclerView recyclerViewForo;
    List<Foro> comentarios;
    Button newComm;
    TextView comentario;
    private API api;

    //referencias para comunicar fragment
    Activity actividad;
    iComunicaFragments interfaceComunicaFragments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.foro_fragment,container,false);
        recyclerViewForo = view.findViewById(R.id.recyclerForoId);
        comentarios = new LinkedList<>();
        comentario = view.findViewById(R.id.newComentario);
        newComm = view.findViewById(R.id.sendCommentBtn);
        //cargar la lista
        cargarLista();
        newComm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarComentario();
            }
        });
        return view;
    }

    //Funcion que lee SharedPreferences para obtener el token
    private String obtenerToken(){
        SharedPreferences preferences = this.getActivity().getSharedPreferences("tokenUsuario", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "Login required");
        return token;
    }

    private void enviarComentario() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //Creamos cliente
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        //Crear retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://147.83.7.203:8080/dsaApp/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        //Llamamos a servicios que hemos definido en la API
        api = retrofit.create(API.class);
        String token = obtenerToken();
        Comentario c = new Comentario(comentario.getText().toString());
        Call<Void> call = api.addComment(token, c);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                    cargarLista();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cargarLista(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //Creamos cliente
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        //Crear retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://147.83.7.203:8080/dsaApp/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        //Llamamos a servicios que hemos definido en la API
        api = retrofit.create(API.class);

        Call<List<Foro>> getComentarios = api.getComments();
        getComentarios.enqueue(new Callback<List<Foro>>() {
            @Override
            public void onResponse(Call<List<Foro>> call, Response<List<Foro>> response) {
                comentarios = response.body();
                recyclerViewForo.setLayoutManager(new LinearLayoutManager(getContext()));
                adapterForo = new AdapterForo(getContext(), comentarios);
                recyclerViewForo.setAdapter(adapterForo);
            }

            @Override
            public void onFailure(Call<List<Foro>> call, Throwable t) {
                Log.d("minimo2", "Error", t);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            this.actividad=(Activity) context;
            interfaceComunicaFragments=(iComunicaFragments) this.actividad;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
