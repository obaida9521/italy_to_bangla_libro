package com.developerobaida.italytobanglalibro.adapters;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.developerobaida.italytobanglalibro.databinding.ItemTableRowBinding;
import com.developerobaida.italytobanglalibro.models.Model2;
import java.util.ArrayList;

public class Adapter2 extends RecyclerView.Adapter {

    Context context;
    ArrayList<Model2> arrayList;
    TextToSpeech textToSpeech;

    public Adapter2(Context context, ArrayList<Model2> arrayList ,TextToSpeech textToSpeech) {
        this.context = context;
        this.arrayList = arrayList;
        this.textToSpeech = textToSpeech;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTableRowBinding binding = ItemTableRowBinding.inflate(LayoutInflater.from(context),parent,false);

        return new MyViewHoder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyViewHoder holder1 = (MyViewHoder) holder;

        Model2 model = arrayList.get(position);

//        textToSpeech = new TextToSpeech(context, status -> {
//            if (status == TextToSpeech.SUCCESS) {
//                int result = textToSpeech.setLanguage(Locale.ITALIAN);
//
//                if (result == TextToSpeech.LANG_MISSING_DATA ||
//                        result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                    Log.e("TTS", "Language not supported");
//                } else {
//                    holder1.binding.speak.setEnabled(true);
//                }
//            } else {
//                Log.e("TTS", "Initialization failed");
//            }
//        });

        holder1.binding.bangla.setText(model.getBangla());
        holder1.binding.spelling.setText(model.getSpelling());
        holder1.binding.italian.setText(model.getItalian());

        holder1.binding.speak.setOnClickListener(view -> {
            speak(model.getItalian());
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHoder extends RecyclerView.ViewHolder{
        ItemTableRowBinding binding;
        public MyViewHoder(ItemTableRowBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }

    private void speak(String text) {
        if (text.isEmpty()) {
            Toast.makeText(context, "Enter text to speak", Toast.LENGTH_SHORT).show();
            return;
        }
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

}
