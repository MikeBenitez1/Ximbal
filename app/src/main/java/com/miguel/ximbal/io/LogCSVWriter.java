package com.miguel.ximbal.io;

import android.os.Environment;
import android.util.Log;

import com.miguel.ximbal.net.ObdReading;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

public class LogCSVWriter {

    private static final String TAG = LogCSVWriter.class.getName();
    private static final String HEADER_CSV = "Ximbal Dataset";
    private static final String[] NAMES_COLUMNS = {"TIME", "LATITUDE", "LONGITUDE", "ALTITUDE", "VEHICLE_ID",
            "BAROMETRIC_PRESSURE", "ENGINE_COOLANT_TEMP", "FUEL_LEVEL", "ENGINE_LOAD", "AMBIENT_AIR_TEMP",
            "ENGINE_RPM", "INTAKE_MANIFOLD_PRESSURE", "MAF", "Term Fuel Trim Bank 1",
            "FUEL_ECONOMY", "Long Term Fuel Trim Bank 2", "FUEL_TYPE", "AIR_INTAKE_TEMP",
            "FUEL_PRESSURE", "SPEED", "Short Term Fuel Trim Bank 2",
            "Short Term Fuel Trim Bank 1", "ENGINE_RUNTIME", "THROTTLE_POS", "DTC_NUMBER",
            "TROUBLE_CODES", "TIMING_ADVANCE", "EQUIV_RATIO"};
    private static final String[] NAMES_COLUMNS_ONLY_READINGS = {
            "BAROMETRIC_PRESSURE", "ENGINE_COOLANT_TEMP", "FUEL_LEVEL", "ENGINE_LOAD", "AMBIENT_AIR_TEMP",
            "ENGINE_RPM", "INTAKE_MANIFOLD_PRESSURE", "MAF", "Term Fuel Trim Bank 1",
            "FUEL_ECONOMY", "Long Term Fuel Trim Bank 2", "FUEL_TYPE", "AIR_INTAKE_TEMP",
            "FUEL_PRESSURE", "SPEED", "Short Term Fuel Trim Bank 2",
            "Short Term Fuel Trim Bank 1", "ENGINE_RUNTIME", "THROTTLE_POS", "DTC_NUMBER",
            "TROUBLE_CODES", "TIMING_ADVANCE", "EQUIV_RATIO"};
    private boolean isFirstLine;
    private BufferedWriter buf;

    public LogCSVWriter(String filename, String dirname) throws FileNotFoundException, RuntimeException {
        try{
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + File.separator + dirname);
            if (!dir.exists()) dir.mkdirs();
            Log.d(TAG, "Path is " + sdCard.getAbsolutePath() + File.separator + dirname);
            File file = new File(dir, filename);
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            this.buf = new BufferedWriter(osw);
            this.isFirstLine = true;
            Log.d(TAG, "Constructed the LogCSVWriter");
        }
        catch (Exception e) {
            Log.e(TAG, "LogCSVWriter constructor failed");
        }
    }

    public void closeLogCSVWriter() {
        try {
            buf.flush();
            buf.close();
            Log.d(TAG, "Flushed and closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLineCSV(ObdReading reading) {
        String crl;

        if (isFirstLine) {
            crl = HEADER_CSV + reading.toString();
            addLine(crl);
            isFirstLine = false;

            // Add line with the columns
            crl = "";
            for (String ccln : NAMES_COLUMNS) {
                crl += ccln + ";";
            }
            addLine(crl.substring(0, crl.length() - 1)); // remove last ";"

        } else {

            crl = reading.getTimestamp() + ";" +
                    reading.getLatitude() + ";" +
                    reading.getLongitude() + ";" +
                    reading.getAltitude() + ";" +
                    reading.getVin() + ";";


            Map<String, String> read = reading.getReadings();

            for (String ccln : NAMES_COLUMNS_ONLY_READINGS) {
                crl += read.get(ccln) + ";";
            }

            addLine(crl.substring(0, crl.length() - 1));
        }
    }


    private void addLine(String line) {
        if (line != null) {
            try {
                buf.write(line, 0, line.length());
                buf.newLine();
                Log.d(TAG, "LogCSVWriter: Wrote" + line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
