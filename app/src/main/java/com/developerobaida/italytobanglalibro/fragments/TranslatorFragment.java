package com.developerobaida.italytobanglalibro.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.developerobaida.italytobanglalibro.R;
import com.developerobaida.italytobanglalibro.adLibrary.AdmobAd;
import com.developerobaida.italytobanglalibro.adLibrary.AdmobAdCallBack;
import com.developerobaida.italytobanglalibro.databinding.FragmentTranslatorBinding;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.Locale;

public class TranslatorFragment extends Fragment {
    private static final int REQ_FOR_VOICE = 5252;
    FragmentTranslatorBinding binding;
    TextToSpeech textToSpeech;
    Translator translator;
    String sourceLanguageCode = TranslateLanguage.BENGALI;
    String targetLanguageCode = TranslateLanguage.ITALIAN;
    Locale locale = Locale.ITALIAN;
    String locale2;

    private void downloadAllLanguageModels() {
        String[] languages = {TranslateLanguage.ENGLISH, TranslateLanguage.BENGALI, TranslateLanguage.ITALIAN};

        for (String sourceLanguage : languages) {
            for (String targetLanguage : languages) {
                if (!sourceLanguage.equals(targetLanguage)) {
                    TranslatorOptions options = new TranslatorOptions.Builder()
                            .setSourceLanguage(sourceLanguage)
                            .setTargetLanguage(targetLanguage)
                            .build();

                    Translator translator = Translation.getClient(options);

                    DownloadConditions conditions = new DownloadConditions.Builder().build();

                    translator.downloadModelIfNeeded(conditions)
                            .addOnSuccessListener(unused -> Log.d("TranslatorFragment",
                                    "Model downloaded: " + sourceLanguage + " to " + targetLanguage))
                            .addOnFailureListener(e -> Log.e("TranslatorFragment",
                                    "Failed to download model: " + sourceLanguage + " to " + targetLanguage + " - " + e.getMessage()));
                }
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTranslatorBinding.inflate(inflater,container,false);

        new AdmobAd(getActivity(), new AdmobAdCallBack() {
            @Override
            public void onAdClicked() {

            }
        }).loadBanner(binding.bannerAd);
        initTxtToSpeech();

        setupLanguageSpinners();
        setupTranslateButton();
        downloadAllLanguageModels();

        binding.voiceType.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO},1414);
            }else speak(locale2);
        });
        binding.copy.setOnClickListener(view -> copyText(binding.output.getText().toString(),getContext()));

        binding.share.setOnClickListener(view -> shareText(getContext(),binding.output.getText().toString()));

        return binding.getRoot();
    }

    private void initTxtToSpeech() {
        textToSpeech = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(locale);

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported");
                }
            } else {
                Log.e("TTS", "Initialization failed");
                Toast.makeText(getContext(),"Initialization failed",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setupLanguageSpinners() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.sourceLanguageSpinner.setAdapter(adapter);
        binding.sourceLanguageSpinner.setSelection(1);
        binding.targetLanguageSpinner.setAdapter(adapter);
        binding.targetLanguageSpinner.setSelection(2);

        binding.sourceLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sourceLanguageCode = getLanguageCode(parent.getItemAtPosition(position).toString());
                locale2 = getSourceLanguageLocale(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.targetLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                targetLanguageCode = getLanguageCode(parent.getItemAtPosition(position).toString());
                locale = getLanguageLocale(parent.getItemAtPosition(position).toString());
                initTxtToSpeech();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private String getLanguageCode(String language) {
        switch (language) {
            case "English":
                return TranslateLanguage.ENGLISH;
            case "বাংলা":
                return TranslateLanguage.BENGALI;
            case "Italian":
                return TranslateLanguage.ITALIAN;
            default:
                return TranslateLanguage.ENGLISH;
        }
    }
    private Locale getLanguageLocale(String language) {
        switch (language) {
            case "English":
                return Locale.ENGLISH;
            case "বাংলা":
                return new Locale("bn-IN");
            case "Italian":
                return new Locale("it","IT");//Locale.ITALIAN;
            default:
                return new Locale("it-IT");
        }
    }

    private String getSourceLanguageLocale(String language) {
        switch (language) {
            case "English":
                return Locale.ENGLISH.toString();
            case "বাংলা":
                return new Locale("bn-IN").toString();
            //return new Locale("bn");
            case "Italian":
                return new Locale("it-IT").toString();//Locale.ITALIAN.toString();
            default:
                return new Locale("bn-IN").toString();
        }
    }

    private void setupTranslateButton() {
        binding.translate.setOnClickListener(view -> {
            String inputText = binding.input.getText().toString();
            if (inputText.isEmpty()) {
                Toast.makeText(getContext(), "Write something.", Toast.LENGTH_SHORT).show();
                return;
            }
            binding.progress.setVisibility(View.VISIBLE);
            translate(inputText);
        });
    }

    private void translate(String inputText) {
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguageCode)
                .setTargetLanguage(targetLanguageCode).build();
        translator = Translation.getClient(options);


        DownloadConditions conditions = new DownloadConditions.Builder().build();


        translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(unused -> {
                    translator.translate(inputText)
                            .addOnSuccessListener(translatedText -> {

                                binding.progress.setVisibility(View.GONE);
                                binding.output.setText(translatedText);

                                binding.voice.setOnClickListener(view -> speech(translatedText));
                            })
                            .addOnFailureListener(e -> {
                                binding.output.setText("Translation failed!");
                                binding.progress.setVisibility(View.GONE);
                            });
                }).addOnFailureListener(e -> {
                    binding.output.setText("Model download failed!");
                });
    }

    private void speech(String text) {
        if (text.isEmpty()) {
            Toast.makeText(getContext(), "Enter text to speak", Toast.LENGTH_SHORT).show();
            return;
        }
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private void speak(String locale) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi, speak something");
        try {
            startActivityForResult(intent, REQ_FOR_VOICE);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Voice input not supported on this device.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data!=null){

            ArrayList<String> arrayList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            binding.input.setText(arrayList.get(0));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (translator != null) translator.close();
        textToSpeech.shutdown();
    }

    public void shareText(Context context, String text){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,text);
        intent.putExtra(Intent.EXTRA_SUBJECT,"Subject");
        intent.setType("text/");
        context.startActivity(Intent.createChooser(intent,"Share via"));
    }

    public void copyText(String text,Context context){
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", text);
        clipboardManager.setPrimaryClip(clipData);
    }
}
//    private void myDialog(){
//        dialog = new Dialog(getActivity());
//        dialog.setContentView(R.layout.dialog_waiting);
//
//        dialog.setCancelable(false);
//
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().setWindowAnimations(R.style.dialogAnimation);
//        dialog.create();
//        dialog.show();
//
//    }