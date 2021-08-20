package com.ahtc.demoretrofit.util;

import android.content.Context;
import android.os.Environment;

import com.ahtc.demoretrofit.model.Note;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExportCSVFaker {

    private final File directory;
    private final String SEPARATOR = ", ";
    private final String FILE_EXPORT = "file_export.csv";
    private final List<Note> listNotes;
    private final File csv;

    public ExportCSVFaker(Context context) {
        directory = new File(context.getExternalFilesDir(""), Environment.DIRECTORY_DOCUMENTS);
        listNotes = new ArrayList<>();
        csv = new File(directory, FILE_EXPORT);
    }

    public File getCsv() {
        return this.csv;
    }

    public boolean isCSVExist() {
        boolean isOk = false;
        if(prepareDirectory()) {
            if(csv.exists()) {
                isOk = true;
            } else {
                isOk = create();
            }
        }
        return isOk;
    }

    private boolean prepareDirectory() {
        boolean isOk = false;
        try {
            if(directory.exists()) {
                isOk = true;
            } else {
                isOk = directory.mkdir();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return isOk;
    }

    private boolean create() {
        boolean isOK = false;
        try {
            FileWriter fileWriter = new FileWriter(directory.getAbsolutePath() + "/" + FILE_EXPORT);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            //Cabecera
            bufferedWriter.write("\ufeff");
            bufferedWriter.write(generateHeader());
            bufferedWriter.newLine();

            //Cuerpo
            bufferedWriter.write(generateBody());

            bufferedWriter.close();
            fileWriter.close();

            isOK = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isOK;
    }

    private String generateHeader() {
        return "id" + SEPARATOR + "note" + SEPARATOR + "idUser" ;
    }

    private String generateBody() {
        generateDummyData();
        StringBuilder stringBuilder = new StringBuilder();
        for(Note note: listNotes) {
            stringBuilder.append(note.getId());
            stringBuilder.append(SEPARATOR);
            stringBuilder.append(note.getText());
            stringBuilder.append(SEPARATOR);
            stringBuilder.append(note.getIdUser());
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }

    private void generateDummyData() {
       for (int i = 1; i <= 10; i++) {
           listNotes.add(new Note(i, "note " + i));
       }
    }
}
